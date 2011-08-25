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
/**
 * RPC Benchmark client thread
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class RPCBenchmarkClientRunnable implements Runnable {

	private static final Log LOGGER = LogFactory.getLog(RPCBenchmarkClientRunnable.class);
	
	private static final AtomicInteger fileNameIndex = new AtomicInteger();
	
	private static final AtomicInteger errorFileNameIndex = new AtomicInteger();
	
	private int requestSize;

	private CyclicBarrier barrier;

	private CountDownLatch latch;

	private long endTime;

	private boolean running = true;
	
	private BufferedWriter writer;
	
	private BufferedWriter errorWriter;
	
	private BenchmarkTestService testService;
	
	public RPCBenchmarkClientRunnable(BenchmarkTestService testService, int requestSize, CyclicBarrier barrier,
			CountDownLatch latch, long endTime) {
		this.testService = testService;
		this.requestSize = requestSize;
		this.barrier = barrier;
		this.latch = latch;
		this.endTime = endTime;
		File file = new File("benchmark.results." + fileNameIndex.incrementAndGet());
		File errorFile = new File("benchmark.error.results." + errorFileNameIndex.incrementAndGet());
		try{
			this.writer = new BufferedWriter(new FileWriter(file));
			this.errorWriter = new BufferedWriter(new FileWriter(errorFile));
		}
		catch(Exception e){
			// IGNORE
		}
	}

	public void run() {
		try {
			barrier.await();
		} 
		catch (Exception e) {
			// IGNORE
		}
		while (running) {
			long beginTime = System.nanoTime();
			try {
				ResponseObject response = testService.execute(new RequestObject(requestSize));
				if(response.getBytes() !=null ){
					writer.write(System.currentTimeMillis()+","+((System.nanoTime() - beginTime)/1000000)+"\r\n");
				}
				else{
					LOGGER.error("server return response is null");
					errorWriter.write(System.currentTimeMillis()+","+((System.nanoTime() - beginTime)/1000000)+"\r\n");
				}
			} catch (Exception e) {
				LOGGER.error("testService.execute error",e);
				try{
					errorWriter.write(System.currentTimeMillis()+","+((System.nanoTime() - beginTime)/1000000)+"\r\n");
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
		} catch (Exception e) {
			// IGNORE
		}
		finally{
			latch.countDown();
		}
	}

}