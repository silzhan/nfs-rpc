package com.bluedavy.rpc.benchmark;

public abstract class AbstractRPCBenchmarkServer extends AbstractBenchmarkServer{

	public Object getServerProcessor(int responseSize) {
		return new BenchmarkTestServiceImpl(responseSize);
	}

}
