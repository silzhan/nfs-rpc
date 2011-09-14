package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
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
	
//	private static final Log LOGGER = LogFactory.getLog(ProtocolFactory.class);
	
	private static Protocol[] protocolHandlers = new Protocol[10];
	
	private static ServerHandler[] serverHandlers = new ServerHandler[10];
	
	static{
		registerProtocol(RPCProtocol.TYPE, new RPCProtocol(), new RPCServerHandler());
		registerProtocol(SimpleProcessorProtocol.TYPE, new SimpleProcessorProtocol(), new SimpleProcessorServerHandler());
	}
	
	public static void registerProtocol(Integer type,Protocol customProtocol,ServerHandler customServerHandler){
		protocolHandlers[type] = customProtocol;
		serverHandlers[type] = customServerHandler;
	}
	
	public static Protocol getProtocol(Integer type){
		return protocolHandlers[type];
	}
	
	public static ServerHandler getServerHandler(Integer type){
		return serverHandlers[type];
	}
	
}
