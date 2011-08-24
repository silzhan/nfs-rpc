package code.google.nfs.rpc.netty.client;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.ChannelFuture;

import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.client.AbstractClient;

public class NettyClient extends AbstractClient {

	private ChannelFuture cf;
	
	private String key;
	
	private int connectTimeout;
	
	public NettyClient(ChannelFuture cf,String key,int connectTimeout){
		this.cf = cf;
		this.connectTimeout = connectTimeout;
	}
	
	public void sendRequest(RequestWrapper wrapper, int timeout) throws Exception {
		ChannelFuture writeFuture = cf.getChannel().write(wrapper);
		writeFuture.awaitUninterruptibly(timeout);
		// 确保写入os sendBuffer了
		if(!writeFuture.isDone()){
			throw new Exception("Send request to "+cf.getChannel().toString()+" timeout("+timeout+"ms)");
		}
		if(writeFuture.isCancelled()){
			throw new Exception("Send request to "+cf.getChannel().toString()+" cancelled by user");
		}
		if(!writeFuture.isSuccess()){
			if(cf.getChannel().isConnected()){
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
