package com.bluedavy.rpc.benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.bluedavy.rpc.ProtocolFactory;
import com.bluedavy.rpc.ProtocolFactory.TYPE;
import com.bluedavy.rpc.client.ClientFactory;

/**
 * 用于Simple Processor Pattern Benchmark测试的客户端，主要支持如下测试场景： 1、不同的并发数 2、不同的请求包的大小
 * 
 * 测试需产生的报告为： 1、执行请求总数、成功率以及失败率 2、tps以及其分布状况 3、平均响应时间以及其分布状况
 * 
 * Usage: -Dwrite.statistics=false BenchmarkClient serverIP serverPort
 * concurrents timeout requestSize runtime(seconds) clientNums
 */
public abstract class AbstractSimpleProcessorBenchmarkClient {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static long maxTPS = 0;

	private static long minTPS = 0;

	private static long allRequestSum;

	private static long allResponseTimeSum;

	private static long allErrorRequestSum;

	private static long allErrorResponseTimeSum;

	private static int runtime;

	// < 0
	private static long below0sum;

	// (0,1]
	private static long above0sum;

	// (1,5]
	private static long above1sum;

	// (5,10]
	private static long above5sum;

	// (10,50]
	private static long above10sum;

	// (50,100]
	private static long above50sum;

	// (100,500]
	private static long above100sum;

	// (500,1000]
	private static long above500sum;

	// > 1000
	private static long above1000sum;

	public void run(String[] args) throws Exception {
		if (args == null || (args.length != 6 && args.length != 7)) {
			throw new IllegalArgumentException(
					"must give six or seven args, serverIP serverPort concurrents timeout requestSize runtime(seconds) clientNums");
		}

		final String serverIP = args[0];
		final int serverPort = Integer.parseInt(args[1]);
		final int concurrents = Integer.parseInt(args[2]);
		final int timeout = Integer.parseInt(args[3]);
		final int requestSize = Integer.parseInt(args[4]);
		runtime = Integer.parseInt(args[5]);
		final long endtime = System.currentTimeMillis() + runtime * 1000;
		int tmpClientNums = 1;
		if (args.length == 7) {
			tmpClientNums = Integer.parseInt(args[6]);
		}
		final int clientNums = tmpClientNums;

		// Print start info
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.SECOND, runtime);
		StringBuilder startInfo = new StringBuilder(
				dateFormat.format(currentDate));
		startInfo.append(" ready to start client benchmark,server is ");
		startInfo.append(serverIP).append(":").append(serverPort);
		startInfo.append(",concurrents is: ").append(concurrents);
		startInfo.append(",clientNums is: ").append(clientNums);
		startInfo.append(",timeout is:").append(timeout);
		startInfo.append(",requestSize is:").append(requestSize);
		startInfo.append(" bytes,the benchmark will end at:").append(
				dateFormat.format(calendar.getTime()));
		System.out.println(startInfo.toString());

		ProtocolFactory.setProtocol(TYPE.SIMPLE);
		CyclicBarrier barrier = new CyclicBarrier(concurrents);
		CountDownLatch latch = new CountDownLatch(concurrents);
		for (int i = 0; i < concurrents; i++) {
			Thread thread = new Thread(
					new SimpleProcessorBenchmarkClientRunnable(
							getClientFactory(), serverIP, serverPort,
							clientNums, timeout, requestSize, barrier, latch,
							endtime), "benchmarkclient-" + i);
			thread.start();
		}

		// 从1分钟后的开始计算benchmark的result，让java进行一定的热身
		long benchmarkBeginTime = System.currentTimeMillis() + 60000;
		latch.await();

		// read result files, & add all
		// key为秒的区间, value为一个Long[2]的数组，其中Long[0]代表执行的次数，Long[2]代表总耗时
		Map<String, Long[]> times = new HashMap<String, Long[]>();
		File path = new File(".");
		File[] resultFiles = path.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith("benchmark.results.")) {
					return true;
				}
				return false;
			}
		});
		long maxTimeRange = 0;
		for (File resultFile : resultFiles) {
			BufferedReader reader = new BufferedReader(new FileReader(
					resultFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] info = line.split(",");
				long recordTime = Long.parseLong(info[0]);
				long responseTime = Long.parseLong(info[1]);
				if (recordTime - benchmarkBeginTime < 0)
					continue;
				long tmpTimeRange = (recordTime - benchmarkBeginTime) / 1000;
				if (tmpTimeRange > maxTimeRange) {
					maxTimeRange = tmpTimeRange;
				}
				String timeRange = String.valueOf(tmpTimeRange);
				sumResponseTimeSpread(responseTime);
				if (times.containsKey(timeRange)) {
					Long[] values = times.get(timeRange);
					values[0] += 1;
					values[1] += responseTime;
					times.put(timeRange, values);
				} else {
					Long[] values = new Long[2];
					values[0] = 1l;
					values[1] = responseTime;
					times.put(timeRange, values);
				}
			}
			reader.close();
			resultFile.delete();
		}

		// 去掉最后10秒的请求，以便统计更加准确
		for (int i = 0; i < 10; i++) {
			times.remove(String.valueOf(maxTimeRange - i));
		}

		for (Long[] infos : times.values()) {
			allRequestSum += infos[0];
			allResponseTimeSum += infos[1];
			if (infos[0] > maxTPS) {
				maxTPS = infos[0];
			}
			if (minTPS == 0 || infos[0] < minTPS) {
				minTPS = infos[0];
			}
		}

		boolean isWriteResult = Boolean.parseBoolean(System.getProperty(
				"write.statistics", "false"));
		if (isWriteResult) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"benchmark.all.results"));
			for (Map.Entry<String, Long[]> entry : times.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue()[0] + ","
						+ entry.getValue()[1] + "\r\n");
			}
			writer.close();
		}

		System.out.println("----------Benchmark Statistics--------------");
		System.out.println(" Concurrents: " + concurrents);
		System.out.println(" Runtime: " + runtime + " seconds");
		System.out.println(" Benchmark Time: " + times.keySet().size());
		long allRequest = (allRequestSum + allErrorRequestSum);
		System.out.println(" Requests: " + allRequest + " Success: "
				+ allRequestSum * 100 / allRequest + "% (" + allRequestSum
				+ ") Error: " + allErrorRequestSum * 100 / allRequest + "% ("
				+ allErrorRequestSum + ")");
		System.out.println(" Avg TPS: " + allRequest / times.keySet().size()
				+ " Max TPS: " + maxTPS + " Min TPS: " + minTPS);
		System.out.println(" Avg RT: "
				+ (allErrorResponseTimeSum + allResponseTimeSum) / allRequest
				+ "ms");
		System.out.println(" RT <= 0: " + (below0sum * 100 / allRequest) + "% "
				+ below0sum + "/" + allRequest);
		System.out.println(" RT (0,1]: " + (above0sum * 100 / allRequest)
				+ "% " + above0sum + "/" + allRequest);
		System.out.println(" RT (1,5]: " + (above1sum * 100 / allRequest)
				+ "% " + above1sum + "/" + allRequest);
		System.out.println(" RT (5,10]: " + (above5sum * 100 / allRequest)
				+ "% " + above5sum + "/" + allRequest);
		System.out.println(" RT (10,50]: " + (above10sum * 100 / allRequest)
				+ "% " + above10sum + "/" + allRequest);
		System.out.println(" RT (50,100]: " + (above50sum * 100 / allRequest)
				+ "% " + above50sum + "/" + allRequest);
		System.out.println(" RT (100,500]: " + (above100sum * 100 / allRequest)
				+ "% " + above100sum + "/" + allRequest);
		System.out.println(" RT (500,1000]: "
				+ (above500sum * 100 / allRequest) + "% " + above500sum + "/"
				+ allRequest);
		System.out.println(" RT > 1000: " + (above1000sum * 100 / allRequest)
				+ "% " + above1000sum + "/" + allRequest);
		System.exit(0);
	}

	private void sumResponseTimeSpread(long responseTime) {
		if (responseTime <= 0) {
			below0sum++;
		} else if (responseTime > 0 && responseTime <= 1) {
			above0sum++;
		} else if (responseTime > 1 && responseTime <= 5) {
			above1sum++;
		} else if (responseTime > 5 && responseTime <= 10) {
			above5sum++;
		} else if (responseTime > 10 && responseTime <= 50) {
			above10sum++;
		} else if (responseTime > 50 && responseTime <= 100) {
			above50sum++;
		} else if (responseTime > 100 && responseTime <= 500) {
			above100sum++;
		} else if (responseTime > 500 && responseTime <= 1000) {
			above500sum++;
		} else if (responseTime > 1000) {
			above1000sum++;
		}
	}

	public abstract ClientFactory getClientFactory();

}
