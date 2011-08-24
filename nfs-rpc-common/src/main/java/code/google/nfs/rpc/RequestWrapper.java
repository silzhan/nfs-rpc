package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.atomic.AtomicInteger;

import code.google.nfs.rpc.protocol.Protocol;

/**
 * RequestWrapper
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
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
