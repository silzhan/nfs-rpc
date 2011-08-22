package com.bluedavy.rpc.mina.benchmark;

import com.bluedavy.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import com.bluedavy.rpc.mina.server.MinaServer;
import com.bluedavy.rpc.server.Server;

public class MinaSimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaSimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
