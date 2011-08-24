package com.bluedavy.rpc;

import com.bluedavy.rpc.protocol.Protocol;
import com.bluedavy.rpc.protocol.RPCProtocol;
import com.bluedavy.rpc.protocol.SimpleProcessorProtocol;
import com.bluedavy.rpc.server.RPCServerHandler;
import com.bluedavy.rpc.server.ServerHandler;
import com.bluedavy.rpc.server.SimpleProcessorServerHandler;

public class ProtocolFactory {

	private static Protocol protocol;
	
	private static ServerHandler serverHandler;
	
	public static enum TYPE{
		SIMPLE,REFLECTION
	}
	
	static{
		setProtocol(TYPE.REFLECTION);
	}
	
	public static void setProtocol(TYPE type){
		if(type == TYPE.SIMPLE){
			protocol = new SimpleProcessorProtocol();
			serverHandler = new SimpleProcessorServerHandler();
		}
		else{
			protocol = new RPCProtocol();
			serverHandler = new RPCServerHandler();
		}
	}
	
	public static void setProtocol(Protocol customProtocol,ServerHandler customServerHandler){
		protocol = customProtocol;
		serverHandler = customServerHandler;
	}
	
	public static Protocol getProtocol(){
		return protocol;
	}
	
	public static ServerHandler getServerHandler(){
		return serverHandler;
	}
	
}
