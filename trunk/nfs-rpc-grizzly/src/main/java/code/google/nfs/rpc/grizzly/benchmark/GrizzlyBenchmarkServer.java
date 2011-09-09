package code.google.nfs.rpc.grizzly.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.benchmark.AbstractBenchmarkServer;
import code.google.nfs.rpc.grizzly.server.GrizzlyServer;
import code.google.nfs.rpc.server.Server;
/**
 * Grizzly RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyBenchmarkServer extends AbstractBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new GrizzlyBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new GrizzlyServer();
	}

}
