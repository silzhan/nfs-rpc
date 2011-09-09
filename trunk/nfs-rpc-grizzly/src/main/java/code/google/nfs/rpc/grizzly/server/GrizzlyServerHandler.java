package code.google.nfs.rpc.grizzly.server;
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
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.WriteResult;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
/**
 * Grizzly Server Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyServerHandler extends BaseFilter {

	private static final Log LOGGER = LogFactory.getLog(GrizzlyServerHandler.class);
	
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		Object message = ctx.getMessage();
		if (!(message instanceof RequestWrapper) && !(message instanceof List)) {
			LOGGER.error("receive message error,only support RequestWrapper || List");
			throw new IOException("receive message error,only support RequestWrapper || List");
		}
		if(message instanceof List){
			@SuppressWarnings("unchecked")
			List<RequestWrapper> requests = (List<RequestWrapper>) message;
			for (final RequestWrapper request : requests) {
				handleOneRequest(ctx, request);
			}
		}
		else{
			handleOneRequest(ctx, (RequestWrapper)message);
		}
		return ctx.getStopAction();
	}
	
	public NextAction handleClose(FilterChainContext ctx) throws IOException {
		System.err.println("session closed");
		return super.handleClose(ctx);
	}
	
	public void exceptionOccurred(FilterChainContext ctx, Throwable error) {
		error.printStackTrace();
		super.exceptionOccurred(ctx, error);
	}
	
	@SuppressWarnings("rawtypes")
	private void handleOneRequest(final FilterChainContext ctx, final RequestWrapper request) {
//		try {
//			threadpool.execute(new Runnable() {
//				public void run() {
					long beginTime = System.currentTimeMillis();
					ResponseWrapper responseWrapper = ProtocolFactory.getServerHandler(request.getProtocolType()).handleRequest(request);
					int consumeTime = Integer.parseInt(""+ (System.currentTimeMillis() - beginTime));
					// already timeout,so not return
					if (consumeTime >= request.getTimeout()) {
						LOGGER.warn("timeout,so give up send response to client,requestId is:"
								+ request.getId()
								+ ",client is:"
								+ ctx.getConnection().toString()+",consumetime is:"+consumeTime+",timeout is:"+request.getTimeout());
						return;
					}
					try {
						ctx.write(responseWrapper, new CompletionHandler<WriteResult>() {
							
							public void updated(WriteResult result) {
								;
							}
							
							public void failed(Throwable t) {
								LOGGER.error("server write response error",t);
							}
							
							public void completed(WriteResult result) {
								// IGNORE
							}
							
							public void cancelled() {
								LOGGER.error("server write response cancelled");
							}
						});
					} 
					catch (IOException e) {
						LOGGER.error("server write response error",e);
					}
//					ctx.resume();
//				}
//			});
//		} 
//		catch (RejectedExecutionException exception) {
//			LOGGER.error("server threadpool full,threadpool maxsize is:"
//					+ ((ThreadPoolExecutor) threadpool).getMaximumPoolSize());
//			ResponseWrapper responseWrapper = new ResponseWrapper(request.getId(),request.getCodecType(),request.getProtocolType());
//			responseWrapper
//					.setException(new Exception("server threadpool full,maybe because server is slow or too many requests"));
//			try {
//				ctx.write(responseWrapper, new CompletionHandler<WriteResult>() {
//					
//					public void updated(WriteResult result) {
//						;
//					}
//					
//					public void failed(Throwable t) {
//						LOGGER.error("server write response error",t);
//					}
//					
//					public void completed(WriteResult result) {
//						// IGNORE
//					}
//					
//					public void cancelled() {
//						LOGGER.error("server write response cancelled");
//					}
//				});
//			} 
//			catch (IOException e) {
//				LOGGER.error("server write response error",e);
//			}
//		}
	}
	
}
