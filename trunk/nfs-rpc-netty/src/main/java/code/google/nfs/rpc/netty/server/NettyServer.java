package code.google.nfs.rpc.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import code.google.nfs.rpc.NamedThreadFactory;
import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.netty.serialize.NettyProtocolDecoder;
import code.google.nfs.rpc.netty.serialize.NettyProtocolEncoder;
import code.google.nfs.rpc.server.Server;


public class NettyServer implements Server {

	private ServerBootstrap bootstrap = null;

	private AtomicBoolean startFlag = new AtomicBoolean(false);
	
	public NettyServer() {
		ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory("NETTYSERVER-WORKER-");
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(serverBossTF),
				Executors.newCachedThreadPool(serverWorkerTF)));
	}

	public void start(int listenPort, final ExecutorService threadPool) throws Exception {
		if(!startFlag.compareAndSet(false, true)){
			return;
		}
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = new DefaultChannelPipeline();
				pipeline.addLast("decoder", new NettyProtocolDecoder());
				pipeline.addLast("encoder", new NettyProtocolEncoder());
				pipeline.addLast("handler", new NettyServerHandler(threadPool));
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(listenPort));
		System.out.println("Server started,listen at: "+listenPort);
	}

	public void registerProcessor(String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler().registerProcessor(serviceName, serviceInstance);
	}
	
	public void stop() throws Exception {
		bootstrap.releaseExternalResources();
		startFlag.set(false);
	}

}
