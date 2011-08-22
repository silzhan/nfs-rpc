package com.bluedavy.rpc.netty.benchmark;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bluedavy.rpc.benchmark.AbstractBenchmarkClient;
import com.bluedavy.rpc.benchmark.BenchmarkTestService;
import com.bluedavy.rpc.netty.client.NettyClientInvocationHandler;

public class NettyBenchmarkClient extends AbstractBenchmarkClient {

	public static void main(String[] args) throws Exception{
		new NettyBenchmarkClient().run(args);
	}

	public BenchmarkTestService getProxyInstance(
			List<InetSocketAddress> servers, int clientNums,
			int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts) {
		return (BenchmarkTestService) Proxy.newProxyInstance(
				NettyBenchmarkClient.class.getClassLoader(),
				new Class<?>[] { BenchmarkTestService.class },
				new NettyClientInvocationHandler(servers, clientNums,
						connectTimeout, targetInstanceName, methodTimeouts));
	}

}
