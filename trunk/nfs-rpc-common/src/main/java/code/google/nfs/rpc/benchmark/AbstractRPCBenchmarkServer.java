/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.benchmark;
/**
 * Test for RPC based on reflection Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractRPCBenchmarkServer extends AbstractBenchmarkServer{

	public Object getServerProcessor(int responseSize) {
		return new BenchmarkTestServiceImpl(responseSize);
	}

}
