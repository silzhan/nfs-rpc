/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.benchmark;
/**
 * Just for Reflection RPC Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class BenchmarkTestServiceImpl implements BenchmarkTestService {

	private int responseSize;
	
	public BenchmarkTestServiceImpl(int responseSize){
		this.responseSize = responseSize;
	}
	
	public ResponseObject execute(RequestObject request) {
		return new ResponseObject(responseSize);
	}

}
