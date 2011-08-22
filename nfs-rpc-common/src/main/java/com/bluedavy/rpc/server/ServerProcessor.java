package com.bluedavy.rpc.server;
/**
 * Provide for simple processor pattern
 */
public interface ServerProcessor {

	public Object handle(Object request) throws Exception;
	
}
