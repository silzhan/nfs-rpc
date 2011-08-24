package code.google.nfs.rpc.netty.client;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.net.InetSocketAddress;

import org.jboss.netty.channel.ChannelFuture;

import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.client.AbstractClient;
/**
 * Netty Client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyClient extends AbstractClient {
	
	private ChannelFuture cf;
	
	private String key;
	
	private int connectTimeout;
	
	public NettyClient(ChannelFuture cf,String key,int connectTimeout){
		this.cf = cf;
		this.key = key;
		this.connectTimeout = connectTimeout;
	}
	
	public void sendRequest(RequestWrapper wrapper, int timeout) throws Exception {
		ChannelFuture writeFuture = cf.getChannel().write(wrapper);
		// wait until success or timeout
		writeFuture.awaitUninterruptibly(timeout);
		if(!writeFuture.isDone()){
			throw new Exception("Send request to "+cf.getChannel().toString()+" timeout("+timeout+"ms)");
		}
		if(writeFuture.isCancelled()){
			throw new Exception("Send request to "+cf.getChannel().toString()+" cancelled by user");
		}
		if(!writeFuture.isSuccess()){
			if(cf.getChannel().isConnected()){
				// maybe some exception,so close the channel
				cf.getChannel().close();
			}
			else{
				NettyClientFactory.getInstance().removeClient(key,this);
			}
			throw new Exception("Send request to "+cf.getChannel().toString()+" error",writeFuture.getCause());
		}
	}

	public String getServerIP() {
		return ((InetSocketAddress)cf.getChannel().getRemoteAddress()).getHostName();
	}

	public int getServerPort() {
		return ((InetSocketAddress)cf.getChannel().getRemoteAddress()).getPort();
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

}
