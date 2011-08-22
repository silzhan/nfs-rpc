package com.bluedavy.rpc.mina.benchmark;

import com.bluedavy.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import com.bluedavy.rpc.client.ClientFactory;
import com.bluedavy.rpc.mina.client.MinaClientFactory;

public class MinaSimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

	public static void main(String[] args) throws Exception{
		new MinaSimpleBenchmarkClient().run(args);
	}
	
	public ClientFactory getClientFactory() {
		return MinaClientFactory.getInstance();
	}

}
