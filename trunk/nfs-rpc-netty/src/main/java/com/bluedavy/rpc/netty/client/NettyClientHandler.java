package com.bluedavy.rpc.netty.client;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bluedavy.rpc.ResponseWrapper;

public class NettyClientHandler extends SimpleChannelUpstreamHandler {

	private NettyClientFactory factory;
	
	private String key;
	
	private NettyClient client;
	
	public NettyClientHandler(NettyClientFactory factory,String key){
		this.factory = factory;
		this.key = key;
	}
	
	public void setClient(NettyClient client){
		this.client = client;
	}
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ResponseWrapper response = (ResponseWrapper)e.getMessage();
		client.putResponse(response);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if(!(e.getCause() instanceof IOException)){
			ctx.getChannel().close();
			e.getCause().printStackTrace();
		}
	}
	
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		factory.removeClient(key,client);
	}
	
}
