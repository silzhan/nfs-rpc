package com.bluedavy.rpc.mina.serialize;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

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
 *  KEEPED(1B):    保留协议头6 // 保证协议头的对齐
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
 *  KEEPED(1B):    保留协议头6 // 保证协议头的对齐
 *  ID(4B):        请求ID
 *  LENGTH(4B):    包长度
 *  BODY
 */
public class MinaProtocolEncoder extends ProtocolEncoderAdapter {

	private static final Log LOGGER = LogFactory.getLog(MinaProtocolEncoder.class);
	
	private static final int REQUEST_HEADER_LEN = 1 * 7 + 5 * 4;
	
	private static final int RESPONSE_HEADER_LEN = 1 * 7 + 2 * 4;
	
	private static final byte VERSION = (byte)1;
	
	private static final byte REQUEST = (byte)0;
	
	private static final byte RESPONSE = (byte)1;
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if(!(message instanceof RequestWrapper) && !(message instanceof ResponseWrapper)){
			throw new Exception("only support send RequestWrapper && ResponseWrapper");
		}
		int id = 0;
		byte type = REQUEST;
		if(message instanceof RequestWrapper){
			try{
				int requestArgTypesLen = 0;
				int requestArgsLen = 0;
				List<byte[]> requestArgTypes = new ArrayList<byte[]>();
				List<byte[]> requestArgs = new ArrayList<byte[]>();
				RequestWrapper wrapper = (RequestWrapper) message;
				String[] requestArgTypeStrings = wrapper.getArgTypes();
				for (String requestArgType : requestArgTypeStrings) {
					byte[] argTypeByte = requestArgType.getBytes();
					requestArgTypes.add(argTypeByte);
					requestArgTypesLen += argTypeByte.length;
				}
				Object[] requestObjects = wrapper.getRequestObjects();
				for (Object requestArg : requestObjects) {
					ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
					ObjectOutputStream output = new ObjectOutputStream(byteArray);
					output.writeObject(requestArg);
					output.flush();
					output.close();
					byte[] requestArgByte = byteArray.toByteArray(); 
					requestArgs.add(requestArgByte);
					requestArgsLen += requestArgByte.length;
				}
				byte[] targetInstanceNameByte = wrapper.getTargetInstanceName().getBytes();
				byte[] methodNameByte = wrapper.getMethodName().getBytes();
				id = wrapper.getId();
				int timeout = wrapper.getTimeout();
				int capacity = REQUEST_HEADER_LEN + requestArgTypesLen + requestArgsLen;
				ByteBuffer byteBuffer = ByteBuffer.allocate(capacity,false);
				byteBuffer.put(VERSION);
				byteBuffer.put(type);
				byteBuffer.put((byte)0);
				byteBuffer.put((byte)0);
				byteBuffer.put((byte)0);
				byteBuffer.put((byte)0);
				byteBuffer.put((byte)0);
				byteBuffer.put((byte)0);
				byteBuffer.putInt(id);
				byteBuffer.putInt(timeout);
				byteBuffer.putInt(targetInstanceNameByte.length);
				byteBuffer.putInt(methodNameByte.length);
				byteBuffer.putInt(requestArgs.size());
				for (byte[] requestArgType : requestArgTypes) {
					byteBuffer.putInt(requestArgType.length);
				}
				for (byte[] requestArg : requestArgs) {
					byteBuffer.putInt(requestArg.length);
				}
				byteBuffer.put(targetInstanceNameByte);
				byteBuffer.put(methodNameByte);
				for (byte[] requestArgType : requestArgTypes) {
					byteBuffer.put(requestArgType);
				}
				for (byte[] requestArg : requestArgs) {
					byteBuffer.put(requestArg);
				}
				byteBuffer.flip();
				out.write(byteBuffer);
			}
			catch(Exception e){
				LOGGER.error("serialize request object error",e);
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
				LOGGER.error("serialize response object error",e);
				// 仍然创建响应客户端，以便客户端快速接到响应做相应的处理
				wrapper.setResponse(new Exception("serialize response object error",e));
				ObjectOutputStream output = new ObjectOutputStream(byteArray);
				output.writeObject(wrapper.getResponse());
				output.flush();
				output.close();
			}
			type = RESPONSE;
			byte[] body = byteArray.toByteArray();
			int capacity = RESPONSE_HEADER_LEN + body.length;
			ByteBuffer byteBuffer = ByteBuffer.allocate(capacity,false);
			byteBuffer.put(VERSION);
			byteBuffer.put(type);
			byteBuffer.put((byte)0);
			byteBuffer.put((byte)0);
			byteBuffer.put((byte)0);
			byteBuffer.put((byte)0);
			byteBuffer.put((byte)0);
			byteBuffer.put((byte)0);
			byteBuffer.putInt(id);
			byteBuffer.putInt(body.length);
			byteBuffer.put(body);
			byteBuffer.flip();
			out.write(byteBuffer);
		}
	}

}
