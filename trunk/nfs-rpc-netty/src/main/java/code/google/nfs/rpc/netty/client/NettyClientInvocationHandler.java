package code.google.nfs.rpc.netty.client;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import code.google.nfs.rpc.client.AbstractClientInvocationHandler;
import code.google.nfs.rpc.client.ClientFactory;
/**
 * Netty Client Invocation Handler for Client Proxy
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyClientInvocationHandler extends
		AbstractClientInvocationHandler {

	public NettyClientInvocationHandler(List<InetSocketAddress> servers,
			int clientNums, int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts,int datatype) {
		super(servers, clientNums, connectTimeout, targetInstanceName, methodTimeouts,datatype);
	}

	public ClientFactory getClientFactory() {
		return NettyClientFactory.getInstance();
	}

}
