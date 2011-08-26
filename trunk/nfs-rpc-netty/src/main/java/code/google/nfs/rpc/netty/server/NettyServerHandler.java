package code.google.nfs.rpc.netty.server;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import code.google.nfs.rpc.NamedThreadFactory;
import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
/**
 * Netty Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyServerHandler extends SimpleChannelUpstreamHandler {

	private static final Log LOGGER = LogFactory.getLog(NettyServerHandler.class);
	
	private static final ThreadFactory tf = new NamedThreadFactory("SERVER-RECEIVER");
	
	private ExecutorService receiveThreadPool;
	
	private ExecutorService threadpool;
	
	public NettyServerHandler(ExecutorService threadpool){
		this.receiveThreadPool = Executors.newCachedThreadPool(tf);
		this.threadpool = threadpool;
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception {
		if(!(e.getCause() instanceof IOException)){
			// only log
			LOGGER.error("catch some exception not IOException",e.getCause());
		}
	}
	
	public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e)
		throws Exception {
		if(!(e.getMessage() instanceof List)){
			LOGGER.error("receive List error,only support List");
			throw new Exception("receive List error,only support List");
		}
		@SuppressWarnings("unchecked")
		final List<RequestWrapper> requests = (List<RequestWrapper>) e.getMessage();
		receiveThreadPool.execute(new Runnable() {
			public void run() {
				for (final RequestWrapper request : requests) {
					try{
						threadpool.execute(new Runnable() {
							public void run() {
								long beginTime = System.currentTimeMillis();
								ResponseWrapper responseWrapper = ProtocolFactory.getServerHandler().handleRequest(request);
								int consumeTime = Integer.parseInt(""+(System.currentTimeMillis() - beginTime));
								// timeout,so not return
								if(consumeTime >= request.getTimeout()){
									LOGGER.warn("timeout,so give up send response to client,request id is:"
											+ request.getId()
											+ ",client is:"
											+ ctx.getChannel().getRemoteAddress()+",consumetime is:"+consumeTime+",timeout is:"+request.getTimeout());
									return;
								}
								ChannelFuture wf = ctx.getChannel().write(responseWrapper);
								wf.awaitUninterruptibly(request.getTimeout());
								if(!wf.isSuccess()){
									LOGGER.error("server write response error,request id is: "+request.getId());
								}
							}
						});
					}
					catch(RejectedExecutionException exception){
						LOGGER.error("server threadpool full,threadpool maxsize is:"
								+ ((ThreadPoolExecutor) threadpool).getMaximumPoolSize());
						ResponseWrapper responseWrapper = new ResponseWrapper();
						responseWrapper.setRequestId(request.getId());
						responseWrapper.setException(new Exception("server threadpool full,maybe because server is slow or too many requests"));
						ChannelFuture wf = ctx.getChannel().write(responseWrapper);
						wf.awaitUninterruptibly(request.getTimeout());
						if(!wf.isSuccess()){
							LOGGER.error("server write response error,request id is: "+request.getId());
						}
					}
				}
			}
		});
	}
	
}
