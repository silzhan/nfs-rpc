package com.bluedavy.rpc.mina.client;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.bluedavy.rpc.ResponseWrapper;

public class MinaClientProcessor extends IoHandlerAdapter {

	private static final Log LOGGER = LogFactory.getLog(MinaClientProcessor.class);
	
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
		if(!(message instanceof ResponseWrapper)){
			LOGGER.error("receive message error,only support ResponseWrapper");
			throw new Exception("receive message error,only support ResponseWrapper");
		}
		ResponseWrapper response = (ResponseWrapper)message;
		try{
			client.putResponse(response);
		}
		catch(Exception e){
			LOGGER.error("receive message from :"+session.getRemoteAddress()+",but occurs error",e);
		}
	}
	
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// 不做复杂处理，任何filter chain上抛出的异常，全部关闭连接
		if(!(cause instanceof IOException)){
			LOGGER.error("catch some exception not IOException,so close session",cause);
			session.close();
			cause.printStackTrace();
		}
	}

	public void sessionClosed(IoSession session) throws Exception {
		factory.removeClient(key,client);
	}
	
}
