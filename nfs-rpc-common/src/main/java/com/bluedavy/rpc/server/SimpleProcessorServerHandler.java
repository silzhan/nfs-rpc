package com.bluedavy.rpc.server;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

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
		try{
			ObjectInputStream objectIn = new ObjectInputStream(
							new ByteArrayInputStream((byte[]) request.getMessage()));
			Object requestObject = objectIn.readObject();
			objectIn.close();
			responseWrapper.setResponse(processor.handle(requestObject));
		}
		catch(Exception e){
			responseWrapper.setException(e);
		}
		return responseWrapper;
	}
}
