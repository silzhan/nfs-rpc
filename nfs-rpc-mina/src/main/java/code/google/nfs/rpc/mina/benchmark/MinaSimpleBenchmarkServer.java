package code.google.nfs.rpc.mina.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import code.google.nfs.rpc.mina.server.MinaServer;
import code.google.nfs.rpc.server.Server;

/**
 * Mina Direct Call RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaSimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new MinaSimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new MinaServer();
	}

}
