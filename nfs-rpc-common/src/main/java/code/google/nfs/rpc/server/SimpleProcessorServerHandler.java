package code.google.nfs.rpc.server;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.Coders;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
/**
 * Direct Call RPC Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorServerHandler implements ServerHandler{

	private ServerProcessor processor = null;
	
	public void registerProcessor(String instanceName,Object instance){
		processor = (ServerProcessor) instance;
	}
	
	public ResponseWrapper handleRequest(final RequestWrapper request){
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setRequestId(request.getId());
		responseWrapper.setDataType(request.getDataType());
		try{
			Object requestObject = Coders.getDecoder(String.valueOf(request.getDataType())).decode((byte[])request.getMessage());
			responseWrapper.setResponse(processor.handle(requestObject));
		}
		catch(Exception e){
			responseWrapper.setException(e);
		}
		return responseWrapper;
	}
}
