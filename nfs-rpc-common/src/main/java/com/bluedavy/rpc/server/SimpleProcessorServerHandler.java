package com.bluedavy.rpc.server;

import com.bluedavy.rpc.Coders;
import com.bluedavy.rpc.RequestWrapper;
import com.bluedavy.rpc.ResponseWrapper;

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
