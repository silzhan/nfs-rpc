package code.google.nfs.rpc.netty.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;

public class NettyServerHandler extends SimpleChannelUpstreamHandler {

	private ExecutorService threadpool;
	
	public NettyServerHandler(ExecutorService threadpool){
		this.threadpool = threadpool;
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception {
		if(!(e.getCause() instanceof IOException)){
			ctx.getChannel().close();
		}
	}
	
	public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e)
		throws Exception {
		if(!(e.getMessage() instanceof RequestWrapper)){
			throw new Exception("receive message error,only support RequestWrapper");
		}
		final RequestWrapper request = (RequestWrapper) e.getMessage();
		try{
			threadpool.execute(new Runnable() {
				public void run() {
					long beginTime = System.currentTimeMillis();
					ResponseWrapper responseWrapper = ProtocolFactory.getServerHandler().handleRequest(request);
					int consumeTime = Integer.parseInt(""+(System.currentTimeMillis() - beginTime));
					// 说明客户端已超时，没必要返回
					if(consumeTime >= request.getTimeout()){
						return;
					}
					ChannelFuture wf = ctx.getChannel().write(responseWrapper);
					wf.awaitUninterruptibly(request.getTimeout());
					if(!wf.isSuccess()){
						System.err.println("server write response error");
						// TODO: 记录异常
					}
				}
			});
		}
		catch(RejectedExecutionException exception){
			ResponseWrapper responseWrapper = new ResponseWrapper();
			responseWrapper.setRequestId(request.getId());
			responseWrapper.setException(new Exception("server threadpool full"));
			ChannelFuture wf = ctx.getChannel().write(responseWrapper);
			wf.awaitUninterruptibly(request.getTimeout());
			if(!wf.isSuccess()){
				System.err.println("server write response error");
				// TODO: 记录异常
			}
		}
	}
	
}
