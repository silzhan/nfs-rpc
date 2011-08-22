package com.bluedavy.rpc.protocol;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.bluedavy.rpc.RequestWrapper;
import com.bluedavy.rpc.ResponseWrapper;

/**
 * 协议格式包：
 * 	VERSION(1B):   协议版本号
 *  TYPE(1B):      请求/响应 
 *  KEEPED(1B):    保留协议头1
 *  KEEPED(1B):    保留协议头2
 *  KEEPED(1B):    保留协议头3
 *  KEEPED(1B):    保留协议头4
 *  KEEPED(1B):    保留协议头5
 *  KEEPED(1B):    保留协议头6  // 保证对齐
 *  ID(4B):        请求ID
 *  TIMEOUT(4B):   请求超时时间
 *  LENGTH(4B):    包长度
 *  BODY
 */
public class SimpleProcessorProtocol implements Protocol{
	
	private static final int HEADER_LEN = 1 * 8 + 3 * 4;
	
	private static final byte VERSION = (byte)1;
	
	private static final byte REQUEST = (byte)0;
	
	private static final byte RESPONSE = (byte)1;
	
	/**
	 * encode Message to byte & write to io framework
	 * 
	 * @param message
	 * @param byteBuffer
	 * @throws Exception
	 */
	public ByteBufferWrapper encode(Object message,ByteBufferWrapper bytebufferWrapper) throws Exception{
		if(!(message instanceof RequestWrapper) && !(message instanceof ResponseWrapper)){
			throw new Exception("only support send RequestWrapper && ResponseWrapper");
		}
		int id = 0;
		byte type = REQUEST;
		byte[] body = null;
		int timeout = 0;
		if(message instanceof RequestWrapper){
			try{
				RequestWrapper wrapper = (RequestWrapper) message;
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				ObjectOutputStream output = new ObjectOutputStream(byteArray);
				output.writeObject(wrapper.getMessage());
				output.flush();
				output.close();
				body = byteArray.toByteArray(); 
				id = wrapper.getId();
				timeout = wrapper.getTimeout();
			}
			catch(Exception e){
				e.printStackTrace();
				// TODO: 处理异常
				// LOGGER.error("serialize request object error",e);
				// TODO: 直接创建一个响应返回，避免需要等到超时
				throw e;
			}
		}
		else{
			ResponseWrapper wrapper = (ResponseWrapper) message;
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			try{
				ObjectOutputStream output = new ObjectOutputStream(byteArray);
				output.writeObject(wrapper.getResponse());
				output.flush();
				output.close();
				id = wrapper.getRequestId();
			}
			catch(Exception e){
				// TODO: 处理异常
				e.printStackTrace();
				// LOGGER.error("serialize response object error",e);
				// 仍然创建响应客户端，以便客户端快速接到响应做相应的处理
				wrapper.setResponse(new Exception("serialize response object error",e));
				ObjectOutputStream output = new ObjectOutputStream(byteArray);
				output.writeObject(wrapper.getResponse());
				output.flush();
				output.close();
			}
			type = RESPONSE;
			body = byteArray.toByteArray();
		}
		int capacity = HEADER_LEN + body.length;
		ByteBufferWrapper byteBuffer = bytebufferWrapper.get(capacity);
		byteBuffer.writeByte(VERSION);
		byteBuffer.writeByte(type);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeInt(id);
		byteBuffer.writeInt(timeout);
		byteBuffer.writeInt(body.length);
		byteBuffer.writeBytes(body);
		return byteBuffer;
	}
	
	/**
	 * decode stream to object
	 * 
	 * @param wrapper
	 * @param errorObject stream not enough,then return this object
	 * @return Object 
	 * @throws Exception
	 */
	public Object decode(ByteBufferWrapper wrapper,Object errorObject) throws Exception{
		final int originPos = wrapper.readerIndex();
		if(wrapper.readableBytes() < HEADER_LEN){
			wrapper.setReaderIndex(originPos);
        	return errorObject;
        }
        byte version = wrapper.readByte();
        // 版本1协议的解析方式
        if(version == (byte)1){
        	byte type = wrapper.readByte();
        	wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		int requestId = wrapper.readInt();
    		int timeout = wrapper.readInt();
    		int expectedLen = wrapper.readInt();
    		if(wrapper.readableBytes() < expectedLen){
    			wrapper.setReaderIndex(originPos);
    			return errorObject;
    		}
    		byte[] body = new byte[expectedLen];
    		wrapper.readBytes(body);
        	if(type == REQUEST){
        		RequestWrapper requestWrapper = new RequestWrapper(body,timeout,requestId);
        		return requestWrapper;
        	}
        	else if(type == RESPONSE){
        		ResponseWrapper responseWrapper = new ResponseWrapper();
            	responseWrapper.setRequestId(requestId);
            	responseWrapper.setResponse(body);
	        	return responseWrapper;
        	}
        	else{
        		throw new UnsupportedOperationException("protocol type : "+type+" is not supported!");
        	}
        }
        else{
        	throw new UnsupportedOperationException("protocol version :"+version+" is not supported!");
        }
	}

}
