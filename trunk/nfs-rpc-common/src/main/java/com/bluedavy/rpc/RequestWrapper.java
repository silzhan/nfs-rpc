package com.bluedavy.rpc;

import java.util.concurrent.atomic.AtomicInteger;

import com.bluedavy.rpc.protocol.Protocol;

/**
 * 发送的请求包，但写入输出流时不是直接把这个对象写入，因此此对象不用支持序列化
 */
public class RequestWrapper {
	
	private static AtomicInteger incId = new AtomicInteger(0);
	
	private String targetInstanceName;
	
	private String methodName;
	
	private String[] argTypes;
	
	private Object[] requestObjects = null;
	
	private Object message = null;
	
	private int timeout = 0;
	
	private int id = 0;
	
	private int dataType = Protocol.HESSIAN_DATA;
	
	public RequestWrapper(Object message,int timeout,int dataType){
		this(message,timeout,incId.incrementAndGet(),dataType);
	}
	
	public RequestWrapper(Object message,int timeout,int id,int dataType){
		this.message = message;
		this.id = id;
		this.timeout = timeout;
		this.dataType = dataType;
	}

	public RequestWrapper(String targetInstanceName,String methodName,String[] argTypes,
						  Object[] requestObjects,int timeout,int dataType){
		this(targetInstanceName,methodName,argTypes,requestObjects,timeout,incId.incrementAndGet(),dataType);
	}

	public RequestWrapper(String targetInstanceName,String methodName,String[] argTypes,
						  Object[] requestObjects,int timeout,int id,int dataType){
		this.requestObjects = requestObjects;
		this.id = id;
		this.timeout = timeout;
		this.targetInstanceName = targetInstanceName;
		this.methodName = methodName;
		this.argTypes = argTypes;
	}

	public int getDataType() {
		return dataType;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public String getTargetInstanceName() {
		return targetInstanceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public int getTimeout() {
		return timeout;
	}

	public Object[] getRequestObjects() {
		return requestObjects;
	}

	public int getId() {
		return id;
	}	
	
	public String[] getArgTypes() {
		return argTypes;
	}
	
}
