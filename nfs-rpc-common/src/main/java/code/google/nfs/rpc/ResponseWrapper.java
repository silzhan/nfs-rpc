package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.protocol.Protocol;

/**
 * ResponseWrapper
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class ResponseWrapper {

	private Integer requestId = 0;
	
	private Object response = null;
	
	private boolean isError = false;
	
	private Throwable exception = null;
	
	private int dataType = Protocol.JAVA_DATA;

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public boolean isError() {
		return isError;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
		isError = true;
	}
	
}
