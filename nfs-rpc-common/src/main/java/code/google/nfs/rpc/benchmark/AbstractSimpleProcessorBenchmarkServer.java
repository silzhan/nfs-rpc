package code.google.nfs.rpc.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.ProtocolFactory.TYPE;
import code.google.nfs.rpc.server.ServerProcessor;

/**
 * Test for RPC based on direct call Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractSimpleProcessorBenchmarkServer extends AbstractBenchmarkServer{

	public Object getServerProcessor(final int responseSize) {
		ProtocolFactory.setProtocol(TYPE.SIMPLE);
		return new ServerProcessor() {
			public Object handle(Object request) throws Exception {
				return new ResponseObject(responseSize);
			}
		};
	}

}
