package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.ConcurrentHashMap;

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
	
	private static ConcurrentHashMap<Integer, Protocol> protocolHandlers = 
		new ConcurrentHashMap<Integer, Protocol>();
	
	private static ConcurrentHashMap<Integer, ServerHandler> serverHandlers = 
			new ConcurrentHashMap<Integer, ServerHandler>();
	
	static{
		registerProtocol(RPCProtocol.TYPE, new RPCProtocol(), new RPCServerHandler());
		registerProtocol(SimpleProcessorProtocol.TYPE, new SimpleProcessorProtocol(), new SimpleProcessorServerHandler());
	}
	
	public static void registerProtocol(Integer type,Protocol customProtocol,ServerHandler customServerHandler){
		Protocol existProtocol = protocolHandlers.putIfAbsent(type, customProtocol);
		if(existProtocol !=  null){
			LOGGER.warn("protocol type: "+type+" registered more than once,now used is: "+existProtocol);
			return;
		}
		serverHandlers.putIfAbsent(type, customServerHandler);
	}
	
	public static Protocol getProtocol(Integer type){
		return protocolHandlers.get(type);
	}
	
	public static ServerHandler getServerHandler(Integer type){
		return serverHandlers.get(type);
	}
	
}
