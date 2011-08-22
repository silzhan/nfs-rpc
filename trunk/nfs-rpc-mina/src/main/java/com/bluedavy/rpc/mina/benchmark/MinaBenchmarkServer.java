package com.bluedavy.rpc.mina.benchmark;

import com.bluedavy.rpc.Server;
import com.bluedavy.rpc.benchmark.AbstractBenchmarkServer;
import com.bluedavy.rpc.mina.server.MinaServer;

public class MinaBenchmarkServer extends AbstractBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
