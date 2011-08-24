package code.google.nfs.rpc.mina.benchmark;

import code.google.nfs.rpc.benchmark.AbstractSimpleProcessorBenchmarkClient;
import code.google.nfs.rpc.client.ClientFactory;
import code.google.nfs.rpc.mina.client.MinaClientFactory;


public class MinaSimpleBenchmarkClient extends AbstractSimpleProcessorBenchmarkClient {

	public static void main(String[] args) throws Exception{
		new MinaSimpleBenchmarkClient().run(args);
	}
	
	public ClientFactory getClientFactory() {
		return MinaClientFactory.getInstance();
	}

}
