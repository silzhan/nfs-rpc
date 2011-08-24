package code.google.nfs.rpc.mina.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;

import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;

public class MinaServerHandler extends IoHandlerAdapter {

	private static final Log LOGGER = LogFactory
			.getLog(MinaServerHandler.class);

	private ExecutorService threadpool;

	public MinaServerHandler(ExecutorService threadpool) {
		this.threadpool = threadpool;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if (!(cause instanceof IOException)) {
			cause.printStackTrace();
			LOGGER.error(
					"catch some exception not IOException,so close session",
					cause);
			session.close();
		}
	}

	@Override
	public void messageReceived(final IoSession session, final Object message)
			throws Exception {
		if (!(message instanceof RequestWrapper)) {
			LOGGER.error("receive message error,only support RequestWrapper");
			throw new Exception(
					"receive message error,only support RequestWrapper");
		}
		final RequestWrapper request = (RequestWrapper) message;
		try {
			threadpool.execute(new Runnable() {
				public void run() {
					long beginTime = System.currentTimeMillis();
					ResponseWrapper responseWrapper = ProtocolFactory.getServerHandler().handleRequest(request);
					int consumeTime = Integer.parseInt(""+ (System.currentTimeMillis() - beginTime));
					// 说明客户端已超时，没必要返回
					if (consumeTime >= request.getTimeout()) {
						LOGGER.warn("timeout,so give up send response to client,requestId is:"
								+ request.getId()
								+ ",client is:"
								+ session.getRemoteAddress());
						return;
					}
					WriteFuture wf = session.write(responseWrapper);
					wf.addListener(new IoFutureListener() {
						public void operationComplete(IoFuture future) {
							WriteFuture writeFuture = (WriteFuture) future;
							if (!writeFuture.isWritten()) {
								LOGGER.error("server write response error,maybe sendbuffer full or session is closed,session connected status: "
										+ session.isConnected());
							}
						}
					});
				}
			});
		} catch (RejectedExecutionException exception) {
			LOGGER.error("server threadpool full,threadpool maxsize is:"
					+ ((ThreadPoolExecutor) threadpool).getMaximumPoolSize());
			ResponseWrapper responseWrapper = new ResponseWrapper();
			responseWrapper.setRequestId(request.getId());
			responseWrapper
					.setException(new Exception("server threadpool full"));
			WriteFuture wf = session.write(responseWrapper);
			wf.addListener(new IoFutureListener() {
				public void operationComplete(IoFuture future) {
					WriteFuture writeFuture = (WriteFuture) future;
					if (!writeFuture.isWritten()) {
						LOGGER.error("server write response error,maybe sendbuffer full or session is closed,session connected status: "
								+ session.isConnected());
					}
				}
			});
		}
	}

}
