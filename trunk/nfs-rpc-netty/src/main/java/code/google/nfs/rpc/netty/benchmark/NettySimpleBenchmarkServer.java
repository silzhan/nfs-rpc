package code.google.nfs.rpc.netty.benchmark;

import code.google.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import code.google.nfs.rpc.netty.server.NettyServer;
import code.google.nfs.rpc.server.Server;


public class NettySimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettySimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
