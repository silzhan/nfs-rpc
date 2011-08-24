package com.bluedavy.rpc.benchmark;

import com.bluedavy.rpc.ProtocolFactory;
import com.bluedavy.rpc.ProtocolFactory.TYPE;
import com.bluedavy.rpc.server.ServerProcessor;

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
