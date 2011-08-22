package com.bluedavy.rpc.protocol;

public interface Decoder {

	public Object decode(byte[] bytes) throws Exception;
	
}
