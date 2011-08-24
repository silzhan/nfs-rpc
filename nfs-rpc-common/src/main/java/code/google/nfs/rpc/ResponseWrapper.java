package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */

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
	
	private Integer codecType = Codecs.JAVA_CODEC;

	public Integer getCodecType() {
		return codecType;
	}

	public void setCodecType(Integer codecType) {
		this.codecType = codecType;
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
