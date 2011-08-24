package code.google.nfs.rpc.mina.client;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;

import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
import code.google.nfs.rpc.client.AbstractClient;

public class MinaClient extends AbstractClient {

	private static final Log LOGGER = LogFactory.getLog(MinaClient.class);

	private static final boolean isWarnEnabled = LOGGER.isWarnEnabled();

	private IoSession session;

	private String key;

	private int connectTimeout;

	public MinaClient(IoSession session, String key, int connectTimeout) {
		this.session = session;
		this.key = key;
		this.connectTimeout = connectTimeout;
	}

	public void sendRequest(final RequestWrapper wrapper, int timeout)
			throws Exception {
		WriteFuture writeFuture = session.write(wrapper);
		writeFuture.addListener(new IoFutureListener() {
			public void operationComplete(IoFuture future) {
				WriteFuture wfuture = (WriteFuture) future;
				if (wfuture.isWritten()) {
					return;
				}
				String error = "send message to server: "
						+ session.getRemoteAddress()
						+ " error,maybe because sendbuffer is full or connection closed: "
						+ !session.isConnected();
				LOGGER.error(error);
				ResponseWrapper response = new ResponseWrapper();
				response.setRequestId(wrapper.getId());
				response.setException(new Exception(error));
				try {
//					List<ResponseWrapper> wrappers = new ArrayList<ResponseWrapper>();
//					wrappers.add(response);
					putResponse(response);
				} 
				catch (Exception e) {
					// IGNORE, should not happen
				}
				// maybe是连接出了点问题，因此关闭连接
				if (session.isConnected()) {
					if (isWarnEnabled) {
						LOGGER.warn("close the session because send request error,server:"
								+ session.getRemoteAddress());
					}
					session.close();
				} 
				else {
					// TODO:
					MinaClientFactory.getInstance().removeClient(key, null);
				}
			}
		});
		// 如未完成，则直接抛出异常
		if (!writeFuture.join(timeout)) {
			String error = "write message to send buffer error,maybe sendbuffer is full,so blocked so long("
					+ timeout + " ms),current need write requests: "+session.getScheduledWriteRequests()+",bytes is: "+session.getScheduledWriteBytes();
			throw new Exception(error);
		}
	}

	public String getServerIP() {
		return ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
	}

	public int getServerPort() {
		return ((InetSocketAddress) session.getRemoteAddress()).getPort();
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

}
