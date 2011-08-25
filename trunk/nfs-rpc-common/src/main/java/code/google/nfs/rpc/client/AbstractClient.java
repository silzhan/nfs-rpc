package code.google.nfs.rpc.client;

/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import code.google.nfs.rpc.Codecs;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;

/**
 * Common Client,support sync invoke
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractClient implements Client {

	private static final Log LOGGER = LogFactory.getLog(AbstractClient.class);

	private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	private static final boolean isWarnEnabled = LOGGER.isWarnEnabled();

	private static final long PRINT_CONSUME_MINTIME = Long.parseLong(System
			.getProperty("nfs.rpc.print.consumetime", "0"));

	protected static ConcurrentHashMap<Integer, ArrayBlockingQueue<ResponseWrapper>> responses = 
			new ConcurrentHashMap<Integer, ArrayBlockingQueue<ResponseWrapper>>();

	public Object invokeSync(Object message, int timeout, int dataType)
			throws Exception {
		RequestWrapper wrapper = new RequestWrapper(message, timeout, dataType);
		return invokeSyncIntern(wrapper);
	}

	public Object invokeSync(String targetInstanceName, String methodName,
			String[] argTypes, Object[] args, int timeout, int dataType)
			throws Exception {
		RequestWrapper wrapper = new RequestWrapper(targetInstanceName,
				methodName, argTypes, args, timeout, dataType);
		return invokeSyncIntern(wrapper);
	}

	private Object invokeSyncIntern(RequestWrapper wrapper) throws Exception {
		long beginTime = System.currentTimeMillis();
		ArrayBlockingQueue<ResponseWrapper> responseQueue = new ArrayBlockingQueue<ResponseWrapper>(1);
		responses.put(wrapper.getId(), responseQueue);
		ResponseWrapper responseWrapper = null;
		try {
			if(isDebugEnabled){
				// for performance trace
				LOGGER.debug("client ready to send message,request id: "+wrapper.getId());
			}
			sendRequest(wrapper, wrapper.getTimeout());
			if(isDebugEnabled){
				// for performance trace
				LOGGER.debug("client write message to send buffer,wait for response,request id: "+wrapper.getId());
			}
		} 
		catch (Exception e) {
			responses.remove(wrapper.getId());
			responseQueue = null;
			LOGGER.error("send request to os sendbuffer error", e);
			throw e;
		}
		try {
			responseWrapper = responseQueue.poll(
					wrapper.getTimeout() - (System.currentTimeMillis() - beginTime),
					TimeUnit.MILLISECONDS);
		}
		catch(Exception e){
			responses.remove(wrapper.getId());
			LOGGER.error("Get response error", e);
			throw new Exception("Get response error", e);
		}
		responses.remove(wrapper.getId());
		
		if (PRINT_CONSUME_MINTIME > 0 && isWarnEnabled) {
			long consumeTime = System.currentTimeMillis() - beginTime;
			if (consumeTime > PRINT_CONSUME_MINTIME) {
				LOGGER.warn("client.invokeSync consume time: "
						+ consumeTime + " ms, server is: " + getServerIP()
						+ ":" + getServerPort() + " request id is:"
						+ wrapper.getId());
			}
		}
		if (responseWrapper == null) {
			String errorMsg = "receive response timeout("
					+ wrapper.getTimeout() + " ms),server is: "
					+ getServerIP() + ":" + getServerPort()
					+ " request id is:" + wrapper.getId();
			throw new Exception(errorMsg);
		}
		
		try{
			// do deserialize in business threadpool
			if (responseWrapper.getResponse() instanceof byte[]) {
				Object responseObject = Codecs.getDecoder(responseWrapper.getCodecType()).decode(
						(byte[]) responseWrapper.getResponse());
				if (responseObject instanceof Throwable) {
					responseWrapper.setException((Throwable) responseObject);
				} 
				else {
					responseWrapper.setResponse(responseObject);
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Deserialize response object error", e);
			throw new Exception("Deserialize response object error", e);
		}
		if (responseWrapper.isError()) {
			Throwable t = responseWrapper.getException();
			t.fillInStackTrace();
			String errorMsg = "server error,server is: " + getServerIP()
					+ ":" + getServerPort() + " request id is:"
					+ wrapper.getId();
			LOGGER.error(errorMsg, t);
			throw new Exception(errorMsg, t);
		}
		return responseWrapper.getResponse();
	}

	/**
	 * receive response
	 */
	public void putResponse(ResponseWrapper wrapper) throws Exception {
		if (!responses.containsKey(wrapper.getRequestId())) {
			LOGGER.warn("give up the response,request id is:" + wrapper.getRequestId() + ",maybe because timeout!");
			return;
		}
		try {
			ArrayBlockingQueue<ResponseWrapper> queue = responses.get(wrapper.getRequestId());
			if (queue != null) {
				queue.put(wrapper);
			} 
			else {
				LOGGER.warn("give up the response,request id is:"
						+ wrapper.getRequestId() + ",because queue is null");
			}
		} 
		catch (InterruptedException e) {
			LOGGER.error("put response error,request id is:" + wrapper.getRequestId(), e);
		}
	}
	
	/**
	 * receive responses
	 */
	public void putResponses(List<ResponseWrapper> wrappers) throws Exception {
		for (ResponseWrapper wrapper : wrappers) {
			if (!responses.containsKey(wrapper.getRequestId())) {
				LOGGER.warn("give up the response,request id is:" + wrapper.getRequestId() + ",maybe because timeout!");
				return;
			}
			try {
				ArrayBlockingQueue<ResponseWrapper> queue = responses.get(wrapper.getRequestId());
				if (queue != null) {
					queue.put(wrapper);
				} 
				else {
					LOGGER.warn("give up the response,request id is:"
							+ wrapper.getRequestId() + ",because queue is null");
				}
			} 
			catch (InterruptedException e) {
				LOGGER.error("put response error,request id is:" + wrapper.getRequestId(), e);
			}
		}
		// TODO: pipeline
	}

	/**
	 * send request to os sendbuffer,must ensure write result
	 */
	public abstract void sendRequest(RequestWrapper wrapper, int timeout) throws Exception;

}
