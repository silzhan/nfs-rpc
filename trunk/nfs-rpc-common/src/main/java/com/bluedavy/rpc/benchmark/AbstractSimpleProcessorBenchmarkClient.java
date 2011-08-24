package com.bluedavy.rpc.benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.bluedavy.rpc.ProtocolFactory;
import com.bluedavy.rpc.ProtocolFactory.TYPE;
import com.bluedavy.rpc.client.ClientFactory;

/**
 * Test for RPC based on direct call Benchmark
 */
public abstract class AbstractSimpleProcessorBenchmarkClient extends AbstractBenchmarkClient{
	
	public Runnable getRunnable(String targetIP, int targetPort,
			int clientNums, int rpcTimeout, int dataType, int requestSize,
			CyclicBarrier barrier, CountDownLatch latch, long endTime) {
		ProtocolFactory.setProtocol(TYPE.SIMPLE);
		return new SimpleProcessorBenchmarkClientRunnable(
				getClientFactory(), targetIP, targetPort,
				clientNums, rpcTimeout, dataType, requestSize, barrier, latch,
				endTime);
	}

	public abstract ClientFactory getClientFactory();

}
