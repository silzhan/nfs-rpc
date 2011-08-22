package com.bluedavy.rpc.server;

import com.bluedavy.rpc.RequestWrapper;
import com.bluedavy.rpc.ResponseWrapper;

public interface ServerHandler {

	public void registerProcessor(String instanceName, Object instance);

	public ResponseWrapper handleRequest(final RequestWrapper request);

}