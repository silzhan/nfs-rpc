package code.google.nfs.rpc.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.ProtocolFactory.TYPE;
import code.google.nfs.rpc.client.ClientFactory;
/**
 * Test for RPC based on direct call Benchmark
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractSimpleProcessorBenchmarkClient extends AbstractBenchmarkClient{
	
	public Runnable getRunnable(String targetIP, int targetPort,
			int clientNums, int rpcTimeout, int dataType, int requestSize,
			CyclicBarrier barrier, CountDownLatch latch, long endTime) {
		ProtocolFactory.setProtocol(TYPE.SIMPLE);
		return new SimpleProcessorBenchmarkClientRunnable(
				getClientFactory(), targetIP, targetPort,
				clientNums, rpcTimeout, dataType, requestSize, barrier, latch,
				endTime);
	}

	public abstract ClientFactory getClientFactory();

}
