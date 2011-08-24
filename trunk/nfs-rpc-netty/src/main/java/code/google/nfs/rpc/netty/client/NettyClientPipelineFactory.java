package code.google.nfs.rpc.netty.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import code.google.nfs.rpc.netty.serialize.NettyProtocolDecoder;
import code.google.nfs.rpc.netty.serialize.NettyProtocolEncoder;

public class NettyClientPipelineFactory implements ChannelPipelineFactory {

	private SimpleChannelUpstreamHandler handler;
	
	public NettyClientPipelineFactory(SimpleChannelUpstreamHandler handler){
		this.handler = handler;
	}
	
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("decoder", new NettyProtocolDecoder());
		pipeline.addLast("encoder", new NettyProtocolEncoder());
		pipeline.addLast("handler", handler);
		return pipeline;
	}

}
