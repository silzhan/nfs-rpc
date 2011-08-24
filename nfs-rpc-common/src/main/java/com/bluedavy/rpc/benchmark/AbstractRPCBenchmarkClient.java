package com.bluedavy.rpc.benchmark;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Test for RPC based on reflection Benchmark
 */
public abstract class AbstractRPCBenchmarkClient extends AbstractBenchmarkClient{

	@Override
	public Runnable getRunnable(String targetIP, int targetPort,
			int clientNums, int rpcTimeout, int dataType, int requestSize,
			CyclicBarrier barrier, CountDownLatch latch, long endTime) {
		Map<String, Integer> methodTimeouts = new HashMap<String, Integer>();
		methodTimeouts.put("*", rpcTimeout);
		List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>();
		servers.add(new InetSocketAddress(targetIP, targetPort));
		return new RPCBenchmarkClientRunnable(
				getProxyInstance(servers, clientNums, 1000, "testservice",methodTimeouts, dataType), 
				requestSize, barrier, latch,endTime);
	}
	
	/*
	 * return ProxyObject
	 */
	public abstract BenchmarkTestService getProxyInstance(
			List<InetSocketAddress> servers, int clientNums,
			int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts, int datatype);

}
