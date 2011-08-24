package code.google.nfs.rpc.netty.client;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import code.google.nfs.rpc.ResponseWrapper;

public class NettyClientHandler extends SimpleChannelUpstreamHandler {

//	private static final int MIN_RESPONSES = 60;
	
	private NettyClientFactory factory;
	
	private String key;
	
	private NettyClient client;
	
//	private int receiveResponseIndex = 0;
//	
//	private long checkTime = 0;
//	
//	private List<ResponseWrapper> responses = new ArrayList<ResponseWrapper>();
	
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
//		responses.add(response);
//		receiveResponseIndex ++;
//		if(receiveResponseIndex > MIN_RESPONSES || System.nanoTime() - checkTime >= 1000000){
//			try{
//				client.putResponse(responses);
//			}
//			catch(Exception t){
//				throw t;
//				// LOGGER.error("receive message from :"+session.getRemoteAddress()+",but occurs error",e);
//			}
//			responses.clear();
//			receiveResponseIndex = 0;
//			checkTime = System.nanoTime();
//		}
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
