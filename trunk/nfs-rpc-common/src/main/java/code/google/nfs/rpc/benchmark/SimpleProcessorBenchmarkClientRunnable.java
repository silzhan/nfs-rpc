package code.google.nfs.rpc.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import code.google.nfs.rpc.client.ClientFactory;
/**
 * Simple Processor RPC Benchmark Client Thread
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorBenchmarkClientRunnable implements Runnable {

	private static final Log LOGGER = LogFactory.getLog(SimpleProcessorBenchmarkClientRunnable.class);
	
	private static final AtomicInteger fileNameIndex = new AtomicInteger();
	
	private static final AtomicInteger errorFileNameIndex = new AtomicInteger();

	private int requestSize;

	private CyclicBarrier barrier;

	private CountDownLatch latch;

	private long endTime;

	private boolean running = true;

	private BufferedWriter writer;
	
	private BufferedWriter errorWriter;

	private ClientFactory factory;

	private String targetIP;

	private int targetPort;

	private int clientNums;

	private int rpcTimeout;

	private int dataType;
	
	public SimpleProcessorBenchmarkClientRunnable(ClientFactory factory,
			String targetIP, int targetPort, int clientNums, int rpcTimeout, int dataType,
			int requestSize, CyclicBarrier barrier, CountDownLatch latch,
			long endTime) {
		this.factory = factory;
		this.targetIP = targetIP;
		this.targetPort = targetPort;
		this.clientNums = clientNums;
		this.rpcTimeout = rpcTimeout;
		this.dataType = dataType;
		this.requestSize = requestSize;
		this.barrier = barrier;
		this.latch = latch;
		this.endTime = endTime;
		File file = new File("benchmark.results."
				+ fileNameIndex.incrementAndGet());
		File errorFile = new File("benchmark.error.results." + errorFileNameIndex.incrementAndGet());
		try {
			this.writer = new BufferedWriter(new FileWriter(file));
			this.errorWriter = new BufferedWriter(new FileWriter(errorFile));
		} catch (Exception e) {
			// IGNORE
		}
	}

	public void run() {
		try {
			barrier.await();
		} catch (Exception e) {
			// IGNORE
		}
		while (running) {
			long beginTime = System.currentTimeMillis();
			try {
				ResponseObject response = (ResponseObject) factory.get(
						targetIP, targetPort, 1000, clientNums).invokeSync(
						new RequestObject(requestSize), rpcTimeout, dataType);
				if (response.getBytes() != null) {
					writer.write(System.currentTimeMillis() + ","
							+ (System.currentTimeMillis() - beginTime) + "\r\n");
				} else {
					LOGGER.error("server return response is null");
					errorWriter.write(System.currentTimeMillis()+","+(System.currentTimeMillis() - beginTime) + "\r\n");
				}
			} catch (Exception e) {
				LOGGER.error("client.invokeSync error",e);
				try{
					errorWriter.write(System.currentTimeMillis()+","+(System.currentTimeMillis() - beginTime) + "\r\n");
				}
				catch(Exception t){
					// IGNORE
				}
			}
			if (System.currentTimeMillis() >= endTime) {
				running = false;
			}
		}
		try {
			writer.close();
			errorWriter.close();
		} 
		catch (Exception e) {
			// IGNORE
		}
		finally{
			latch.countDown();
		}
	}

}