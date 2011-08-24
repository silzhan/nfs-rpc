package code.google.nfs.rpc.server;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import code.google.nfs.rpc.Codecs;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
/**
 * Direct Call RPC Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorServerHandler implements ServerHandler{

	private static final Log LOGGER = LogFactory.getLog(SimpleProcessorServerHandler.class);
	
	private ServerProcessor processor = null;
	
	public void registerProcessor(String instanceName,Object instance){
		processor = (ServerProcessor) instance;
	}
	
	public ResponseWrapper handleRequest(final RequestWrapper request){
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setRequestId(request.getId());
		responseWrapper.setCodecType(request.getCodecType());
		try{
			Object requestObject = Codecs.getDecoder(request.getCodecType()).decode((byte[])request.getMessage());
			responseWrapper.setResponse(processor.handle(requestObject));
		}
		catch(Exception e){
			LOGGER.error("server direct call handler to handle request error",e);
			responseWrapper.setException(e);
		}
		return responseWrapper;
	}
}
