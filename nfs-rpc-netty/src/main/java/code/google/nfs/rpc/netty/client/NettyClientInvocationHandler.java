package code.google.nfs.rpc.netty.client;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import code.google.nfs.rpc.client.AbstractClientInvocationHandler;
import code.google.nfs.rpc.client.ClientFactory;

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
