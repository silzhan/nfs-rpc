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
	
	private Integer codecType = Codecs.HESSIAN_CODEC;
	
	private Integer protocolType;
	
	private int messageLen;
	
	private String responseClassName;

	public ResponseWrapper(Integer requestId,Integer codecType,Integer protocolType){
		this.requestId = requestId;
		this.codecType = codecType;
		this.protocolType = protocolType;
	}
	
	public int getMessageLen() {
		return messageLen;
	}

	public void setMessageLen(int messageLen) {
		this.messageLen = messageLen;
	}

	public Integer getProtocolType() {
		return protocolType;
	}

	public Integer getCodecType() {
		return codecType;
	}

	public Integer getRequestId() {
		return requestId;
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
	
	public String getResponseClassName() {
		return responseClassName;
	}

	public void setResponseClassName(String responseClassName) {
		this.responseClassName = responseClassName;
	}
	
}
