package com.bluedavy.rpc.protocol;


public interface Protocol {

	public static final int JAVA_DATA = 1;
	
	public static final int HESSIAN_DATA = 2;
	
	/**
	 * encode Message to byte & write to io framework
	 * 
	 * @param message
	 * @param byteBuffer
	 * @throws Exception
	 */
	public ByteBufferWrapper encode(Object message,ByteBufferWrapper bytebufferWrapper) throws Exception;

	/**
	 * decode stream to object
	 * 
	 * @param wrapper
	 * @param errorObject stream not enough,then return this object
	 * @return Object 
	 * @throws Exception
	 */
	public Object decode(ByteBufferWrapper wrapper, Object errorObject) throws Exception;

}