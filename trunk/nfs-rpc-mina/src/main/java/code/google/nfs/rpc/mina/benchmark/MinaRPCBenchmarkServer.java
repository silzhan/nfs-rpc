package code.google.nfs.rpc.mina.benchmark;

import code.google.nfs.rpc.benchmark.AbstractRPCBenchmarkServer;
import code.google.nfs.rpc.mina.server.MinaServer;
import code.google.nfs.rpc.server.Server;


public class MinaRPCBenchmarkServer extends AbstractRPCBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaRPCBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
