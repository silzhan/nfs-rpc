package com.bluedavy.rpc.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.bluedavy.rpc.NamedThreadFactory;
import com.bluedavy.rpc.client.AbstractClientFactory;
import com.bluedavy.rpc.client.Client;

public class NettyClientFactory extends AbstractClientFactory {

	private ClientBootstrap bootstrap = null;
	
	private static AbstractClientFactory _self = new NettyClientFactory();
	
	private NettyClientFactory(){
		ThreadFactory bossThreadFactory = new NamedThreadFactory("NETTYCLIENT-BOSS-");
		ThreadFactory workerThreadFactory = new NamedThreadFactory("NETTYCLIENT-WORKER-");
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
										Executors.newCachedThreadPool(bossThreadFactory),
										Executors.newCachedThreadPool(workerThreadFactory)));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("connectTimeoutMillis", 1000);
		bootstrap.setOption("reuseAddress", true);
	}
	
	public static AbstractClientFactory getInstance() {
		return _self;
	}
	
	protected Client createClient(String targetIP, int targetPort,
			int connectTimeout, String key) throws Exception {
		NettyClientHandler handler = new NettyClientHandler(this, key);
		bootstrap.setPipelineFactory(new NettyClientPipelineFactory(handler));
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(targetIP, targetPort));
		future.awaitUninterruptibly(connectTimeout);
		if (!future.isDone()) {
			throw new Exception("Create connection to " + targetIP + ":" + targetPort + " timeout!");
		}
		if (future.isCancelled()) {
			throw new Exception("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
		}
		if (!future.isSuccess()) {
			throw new Exception("Create connection to " + targetIP + ":" + targetPort + " error", future.getCause());
		}
		NettyClient client = new NettyClient(future,key,connectTimeout);
		handler.setClient(client);
		return client;
	}

}
