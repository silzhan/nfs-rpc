package com.bluedavy.rpc.benchmark;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bluedavy.rpc.NamedThreadFactory;
import com.bluedavy.rpc.server.Server;

/**
 * 用于RPC Benchmark测试的服务器端 性能数据暂时均在客户端收集，以客户端数据为准 Usage: BenchmarkServer
 * listenPort maxThreads responseSize
 */
public abstract class AbstractBenchmarkServer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 外部可传入的参数为： args[0]: 监听端口 args[1]: server线程数 args[2]: 返回的响应对象的大小
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void run(String[] args) throws Exception {
		if (args == null || args.length != 3) {
			throw new IllegalArgumentException(
					"must give three args: listenPort | maxThreads | responseSize");
		}
		int listenPort = Integer.parseInt(args[0]);
		int maxThreads = Integer.parseInt(args[1]);
		final int responseSize = Integer.parseInt(args[2]);
		System.out.println(dateFormat.format(new Date())
				+ " ready to start server,listenPort is: " + listenPort
				+ ",maxThreads is:" + maxThreads + ",responseSize is:"
				+ responseSize + " bytes");

		Server server = getServer();
		server.registerProcessor("testservice", getServerProcessor(responseSize));
		ThreadFactory tf = new NamedThreadFactory("BUSINESSTHREADPOOL");
		ExecutorService threadPool = new ThreadPoolExecutor(20, maxThreads,
				300, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
		server.start(listenPort, threadPool);
	}

	public abstract Object getServerProcessor(int responseSize);
	
	/**
	 * 获取具体的Server Instance
	 */
	public abstract Server getServer();

}
