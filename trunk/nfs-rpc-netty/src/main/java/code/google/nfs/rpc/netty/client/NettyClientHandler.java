package code.google.nfs.rpc.netty.client;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import code.google.nfs.rpc.ResponseWrapper;
/**
 * Netty Client Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyClientHandler extends SimpleChannelUpstreamHandler {

//	private static final int MIN_RESPONSES = 60;
	
	private static final Log LOGGER = LogFactory.getLog(NettyClientHandler.class);
	
	private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
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
		@SuppressWarnings("unchecked")
		List<ResponseWrapper> responses = (List<ResponseWrapper>)e.getMessage();
//		if(isDebugEnabled){
			// for performance trace
//			LOGGER.debug("receive response from server: "+ctx.getChannel().getRemoteAddress()+",request id is:"+response.getRequestId());
//		}
		client.putResponses(responses);
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
			// only log
			LOGGER.error("catch some exception not IOException",e.getCause());
		}
	}
	
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		LOGGER.warn("connection closed: "+ctx.getChannel().getRemoteAddress());
		factory.removeClient(key,client);
	}
	
}
