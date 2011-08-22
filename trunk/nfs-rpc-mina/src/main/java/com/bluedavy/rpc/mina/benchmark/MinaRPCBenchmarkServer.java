package com.bluedavy.rpc.mina.benchmark;

import com.bluedavy.rpc.benchmark.AbstractRPCBenchmarkServer;
import com.bluedavy.rpc.mina.server.MinaServer;
import com.bluedavy.rpc.server.Server;

public class MinaRPCBenchmarkServer extends AbstractRPCBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaRPCBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
