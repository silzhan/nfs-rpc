package com.bluedavy.rpc.netty.benchmark;

import com.bluedavy.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bluedavy.rpc.client.ClientFactory;
import com.bluedavy.rpc.netty.client.NettyClientFactory;

public class NettySimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

	public static void main(String[] args) throws Exception{
		new NettySimpleBenchmarkClient().run(args);
	}
	
	public ClientFactory getClientFactory() {
		return NettyClientFactory.getInstance();
	}

}
