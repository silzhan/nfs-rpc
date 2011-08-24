package code.google.nfs.rpc.netty.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkServer;
import code.google.nfs.rpc.netty.server.NettyServer;
import code.google.nfs.rpc.server.Server;

/**
 * Netty Direct Call RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettySimpleBenchmarkServer extends AbstractSimpleProcessorBenchmarkServer {

	public static void main(String[] args) throws Exception{
		new NettySimpleBenchmarkServer().run(args);
	}
	
	public Server getServer() {
		return new NettyServer();
	}

}
