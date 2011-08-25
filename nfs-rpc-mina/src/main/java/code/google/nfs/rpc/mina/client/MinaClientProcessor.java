package code.google.nfs.rpc.mina.client;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import code.google.nfs.rpc.ResponseWrapper;
/**
 * Mina Client processor for receive message,handle exception
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaClientProcessor extends IoHandlerAdapter {
	
	private static final Log LOGGER = LogFactory.getLog(MinaClientProcessor.class);
	
	private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	private MinaClient client=null;
	
	private MinaClientFactory factory=null;
	
	private String key=null;
	
	public MinaClientProcessor(MinaClientFactory factory,String key){
		this.factory = factory;
		this.key = key;
	}
	
	public void setClient(MinaClient minaClient){
		this.client = minaClient;
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
//		if(!(message instanceof ResponseWrapper)){
//			LOGGER.error("receive message error,only support ResponseWrapper");
//			throw new Exception("receive message error,only support ResponseWrapper");
//		}
		List<ResponseWrapper> response = (List<ResponseWrapper>)message;
//		if(isDebugEnabled){
			// for performance trace
//			LOGGER.debug("receive response from server: "+session.getRemoteAddress()+", request id is:"+response.getRequestId());
//		}
		client.putResponses(response);
//		responses.add(response);
//		receiveResponseIndex ++;
//		if(receiveResponseIndex > MIN_RESPONSES || System.nanoTime() - checkTime >= 1000000){
//			try{
//				client.putResponse(responses);
//			}
//			catch(Exception e){
//				LOGGER.error("receive message from :"+session.getRemoteAddress()+",but occurs error",e);
//			}
//			responses.clear();
//			receiveResponseIndex = 0;
//			checkTime = System.nanoTime();
//		}
	}
	
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if(!(cause instanceof IOException)){
			// only log
			LOGGER.error("catch some exception not IOException",cause);
		}
	}

	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.warn("session closed,server is: "+session.getRemoteAddress());
		factory.removeClient(key,client);
	}
	
}
