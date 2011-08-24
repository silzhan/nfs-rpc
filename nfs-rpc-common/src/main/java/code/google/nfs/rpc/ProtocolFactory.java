package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import code.google.nfs.rpc.protocol.Protocol;
import code.google.nfs.rpc.protocol.RPCProtocol;
import code.google.nfs.rpc.protocol.SimpleProcessorProtocol;
import code.google.nfs.rpc.server.RPCServerHandler;
import code.google.nfs.rpc.server.ServerHandler;
import code.google.nfs.rpc.server.SimpleProcessorServerHandler;

/**
 * Protocol Factory,for set Protocol class & serverHandler class
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class ProtocolFactory {
	
	private static final Log LOGGER = LogFactory.getLog(ProtocolFactory.class);
	
	private static Protocol protocol;
	
	private static ServerHandler serverHandler;
	
	public static enum TYPE{
		SIMPLE,REFLECTION
	}
	
	static{
		setProtocol(TYPE.REFLECTION);
	}
	
	public static void setProtocol(TYPE type){
		LOGGER.warn("set protocol type to: " + type);
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
		LOGGER.warn("set protocol to: " + customProtocol + "," + "set serverhandler to: " +  customServerHandler);
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
