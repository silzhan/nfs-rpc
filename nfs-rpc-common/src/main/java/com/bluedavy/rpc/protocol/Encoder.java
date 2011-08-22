package com.bluedavy.rpc.protocol;

public interface Encoder {

	public byte[] encode(Object object) throws Exception;
	
}
