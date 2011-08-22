package com.bluedavy.rpc.mina.benchmark;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bluedavy.rpc.benchmark.AbstractBenchmarkClient;
import com.bluedavy.rpc.benchmark.BenchmarkTestService;
import com.bluedavy.rpc.mina.client.MinaClientInvocationHandler;

public class MinaBenchmarkClient extends AbstractBenchmarkClient {

	public static void main(String[] args) throws Exception {
		new MinaBenchmarkClient().run(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bluedavy.rpc.benchmark.AbstractBenchmarkClient#getProxyInstance(java
	 * .util.List, int, int, java.lang.String, java.util.Map)
	 */
	public BenchmarkTestService getProxyInstance(
			List<InetSocketAddress> servers, int clientNums,
			int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts) {
		return (BenchmarkTestService) Proxy.newProxyInstance(
				MinaBenchmarkClient.class.getClassLoader(),
				new Class<?>[] { BenchmarkTestService.class },
				new MinaClientInvocationHandler(servers, clientNums,
						connectTimeout, targetInstanceName, methodTimeouts));
	}

}
