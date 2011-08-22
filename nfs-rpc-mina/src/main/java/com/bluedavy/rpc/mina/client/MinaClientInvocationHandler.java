package com.bluedavy.rpc.mina.client;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.bluedavy.rpc.client.AbstractClientInvocationHandler;
import com.bluedavy.rpc.client.ClientFactory;

public class MinaClientInvocationHandler extends
		AbstractClientInvocationHandler {

	public MinaClientInvocationHandler(List<InetSocketAddress> servers,
			int clientNums, int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts) {
		super(servers, clientNums, connectTimeout, targetInstanceName, methodTimeouts);
	}

	public ClientFactory getClientFactory() {
		return MinaClientFactory.getInstance();
	}

}
