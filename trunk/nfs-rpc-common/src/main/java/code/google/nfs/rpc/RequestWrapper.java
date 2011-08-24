package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.atomic.AtomicInteger;

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
	
	private Integer codecType = Codecs.HESSIAN_CODEC;
	
	public RequestWrapper(Object message,int timeout,Integer codecType){
		this(message,timeout,incId.incrementAndGet(),codecType);
	}
	
	public RequestWrapper(Object message,int timeout,int id,Integer codecType){
		this.message = message;
		this.id = id;
		this.timeout = timeout;
		this.codecType = codecType;
	}

	public RequestWrapper(String targetInstanceName,String methodName,String[] argTypes,
						  Object[] requestObjects,int timeout,Integer codecType){
		this(targetInstanceName,methodName,argTypes,requestObjects,timeout,incId.incrementAndGet(),codecType);
	}

	public RequestWrapper(String targetInstanceName,String methodName,String[] argTypes,
						  Object[] requestObjects,int timeout,int id,Integer codecType){
		this.requestObjects = requestObjects;
		this.id = id;
		this.timeout = timeout;
		this.targetInstanceName = targetInstanceName;
		this.methodName = methodName;
		this.argTypes = argTypes;
	}

	public Integer getCodecType() {
		return codecType;
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
