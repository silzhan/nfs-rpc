package code.google.nfs.rpc.mina.benchmark;

import code.google.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import code.google.nfs.rpc.mina.server.MinaServer;
import code.google.nfs.rpc.server.Server;


public class MinaSimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaSimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
