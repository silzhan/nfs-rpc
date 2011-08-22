package com.bluedavy.rpc.netty.benchmark;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bluedavy.rpc.benchmark.AbstractRPCBenchmarkClient;
import com.bluedavy.rpc.benchmark.BenchmarkTestService;
import com.bluedavy.rpc.netty.client.NettyClientInvocationHandler;

public class NettyRPCBenchmarkClient extends AbstractRPCBenchmarkClient {

	public static void main(String[] args) throws Exception{
		new NettyRPCBenchmarkClient().run(args);
	}

	public BenchmarkTestService getProxyInstance(
			List<InetSocketAddress> servers, int clientNums,
			int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts) {
		return (BenchmarkTestService) Proxy.newProxyInstance(
				NettyRPCBenchmarkClient.class.getClassLoader(),
				new Class<?>[] { BenchmarkTestService.class },
				new NettyClientInvocationHandler(servers, clientNums,
						connectTimeout, targetInstanceName, methodTimeouts));
	}

}
