package com.bluedavy.rpc.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class RPCBenchmarkClientRunnable implements Runnable {

	private static final AtomicInteger fileNameIndex = new AtomicInteger();
	
	private static final AtomicInteger errorFileNameIndex = new AtomicInteger();
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
			long beginTime = System.currentTimeMillis();
			try {
				ResponseObject response = testService.execute(new RequestObject(requestSize));
				if(response.getBytes() !=null ){
					writer.write(System.currentTimeMillis()+","+(System.currentTimeMillis() - beginTime)+"\r\n");
				}
				else{
					System.err.println(dateFormat.format(new Date())+" server return response is null");
					errorWriter.write(System.currentTimeMillis()+","+(System.currentTimeMillis() - beginTime)+"\r\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				try{
					errorWriter.write(System.currentTimeMillis()+","+(System.currentTimeMillis() - beginTime)+"\r\n");
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