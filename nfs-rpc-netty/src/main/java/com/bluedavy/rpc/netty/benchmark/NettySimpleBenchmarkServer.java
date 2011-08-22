package com.bluedavy.rpc.netty.benchmark;

import com.bluedavy.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import com.bluedavy.rpc.netty.server.NettyServer;
import com.bluedavy.rpc.server.Server;

public class NettySimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettySimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
