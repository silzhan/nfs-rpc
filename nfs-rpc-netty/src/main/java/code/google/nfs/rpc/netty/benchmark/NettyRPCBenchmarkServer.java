package code.google.nfs.rpc.netty.benchmark;

import code.google.nfs.rpc.benchmark.AbstractRPCBenchmarkServer;
import code.google.nfs.rpc.netty.server.NettyServer;
import code.google.nfs.rpc.server.Server;


public class NettyRPCBenchmarkServer extends AbstractRPCBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettyRPCBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
