package com.bluedavy.rpc.netty.benchmark;

import com.bluedavy.rpc.Server;
import com.bluedavy.rpc.benchmark.AbstractBenchmarkServer;
import com.bluedavy.rpc.netty.server.NettyServer;

public class NettyBenchmarkServer extends AbstractBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettyBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
