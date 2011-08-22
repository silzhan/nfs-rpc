package com.bluedavy.rpc.netty.serialize;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.bluedavy.rpc.RequestWrapper;
import com.bluedavy.rpc.ResponseWrapper;
/**
 * 请求协议格式包：
 * 	VERSION(1B):   协议版本号
 *  TYPE(1B):      请求/响应 
 *  KEEPED(1B):    保留协议头1
 *  KEEPED(1B):    保留协议头2
 *  KEEPED(1B):    保留协议头3
 *  KEEPED(1B):    保留协议头4
 *  KEEPED(1B):    保留协议头5
 *  ID(4B):        请求ID
 *  TIMEOUT(4B):   请求超时时间
 *  TARGETINSTANCELEN(4B):  目标名称的长度
 *  METHODNAMELEN(4B):      方法名的长度
 *  ARGSCOUNT(4B):          方法参数个数
 *  ARG1TYPELEN(4B):        方法参数1类型的长度
 *  ARG2TYPELEN(4B):        方法参数2类型的长度
 *  ...
 *  ARG1LEN(4B):            方法参数1的长度
 *  ARG2LEN(4B):            方法参数2的长度
 *  ...
 *  TARGETINSTANCENAME
 *  METHODNAME
 *  ARG1TYPENAME
 *  ARG2TYPENAME
 *  ...
 *  ARG1
 *  ARG2
 *  ...
 *  
 * 响应协议格式包：
 *  VERSION(1B):   协议版本号
 *  TYPE(1B):      请求/响应 
 *  KEEPED(1B):    保留协议头1
 *  KEEPED(1B):    保留协议头2
 *  KEEPED(1B):    保留协议头3
 *  KEEPED(1B):    保留协议头4
 *  KEEPED(1B):    保留协议头5
 *  ID(4B):        请求ID
 *  LENGTH(4B):    包长度
 *  BODY
 */
public class NettyProtocolDecoder extends FrameDecoder {

	private static final int REQUEST_HEADER_LEN = 1 * 7 + 5 * 4;
	
	private static final int RESPONSE_HEADER_LEN = 1 * 7 + 2 * 4;
	
	private static final byte REQUEST = (byte)0;
	
	private static final byte RESPONSE = (byte)1;
	
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
		final int originPos = in.readerIndex();
		if(in.readableBytes() < 2){
        	in.setIndex(originPos,in.writerIndex());
        	return null;
        }
        byte version = in.readByte();
        // 版本1协议的解析方式
        if(version == (byte)1){
        	byte type = in.readByte();
        	if(type == REQUEST){
        		if(in.readableBytes() < REQUEST_HEADER_LEN -2){
        			in.setIndex(originPos,in.writerIndex());
        			return null;
        		}
        		in.readByte();
        		in.readByte();
        		in.readByte();
        		in.readByte();
        		in.readByte();
        		int requestId = in.readInt();
        		int timeout = in.readInt();
        		int targetInstanceLen = in.readInt();
        		int methodNameLen = in.readInt();
        		int argsCount = in.readInt();
        		int argInfosLen = argsCount * 4 * 2;
        		int expectedLen = argInfosLen + targetInstanceLen + methodNameLen;
        		if(in.readableBytes() < expectedLen){
        			in.setIndex(originPos,in.writerIndex());
        			return null;
        		}
        		expectedLen = 0;
        		int[] argsTypeLen = new int[argsCount];
        		for (int i = 0; i < argsCount; i++) {
					argsTypeLen[i] = in.readInt();
					expectedLen += argsTypeLen[i]; 
				}
        		int[] argsLen = new int[argsCount];
        		for (int i = 0; i < argsCount; i++) {
        			argsLen[i] = in.readInt();
        			expectedLen += argsLen[i];
				}
        		byte[] targetInstanceByte = new byte[targetInstanceLen];
        		in.readBytes(targetInstanceByte);
        		String targetInstanceName = new String(targetInstanceByte);
        		byte[] methodNameByte = new byte[methodNameLen];
        		in.readBytes(methodNameByte);
        		String methodName = new String(methodNameByte);
        		if(in.readableBytes() < expectedLen){
        			in.setIndex(originPos,in.writerIndex());
        			return null;
        		}
        		String[] argTypes = new String[argsCount];
        		for (int i = 0; i < argsCount; i++) {
					byte[] argTypeByte = new byte[argsTypeLen[i]];
					in.readBytes(argTypeByte);
					argTypes[i] = new String(argTypeByte);
				}
        		Object[] args = new Object[argsCount];
        		for (int i = 0; i < argsCount; i++) {
					byte[] argByte = new byte[argsLen[i]];
					in.readBytes(argByte);
					args[i] = argByte;
				}
        		RequestWrapper wrapper = new RequestWrapper(targetInstanceName, methodName, argTypes, args, timeout, requestId);
        		return wrapper;
        	}
        	else if(type == RESPONSE){
        		if(in.readableBytes() < RESPONSE_HEADER_LEN -2){
        			in.setIndex(originPos,in.writerIndex());
        			return null;
        		}
        		in.readByte();
            	in.readByte();
            	in.readByte();
            	in.readByte();
            	in.readByte();
            	int requestId = in.readInt();
            	int bodyLen = in.readInt();
            	if(in.readableBytes() < bodyLen){
            		in.setIndex(originPos,in.writerIndex());
            		return null;
            	}
            	byte[] bodyBytes = new byte[bodyLen];
            	in.readBytes(bodyBytes);
            	ResponseWrapper wrapper = new ResponseWrapper();
        		wrapper.setRequestId(requestId);
        		wrapper.setResponse(bodyBytes);
	        	return wrapper;
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
