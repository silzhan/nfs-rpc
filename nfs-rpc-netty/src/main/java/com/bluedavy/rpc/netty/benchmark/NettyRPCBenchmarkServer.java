package com.bluedavy.rpc.netty.benchmark;

import com.bluedavy.rpc.benchmark.AbstractRPCBenchmarkServer;
import com.bluedavy.rpc.netty.server.NettyServer;
import com.bluedavy.rpc.server.Server;

public class NettyRPCBenchmarkServer extends AbstractRPCBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettyRPCBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
