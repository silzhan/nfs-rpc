package com.bluedavy.rpc.netty.serialize;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.bluedavy.rpc.RPCProtocolUtil;
/**
 * encode message
 */
public class NettyProtocolDecoder extends FrameDecoder {
	
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
		NettyByteBufferWrapper wrapper = new NettyByteBufferWrapper(in);
		return RPCProtocolUtil.decode(wrapper, null);
	}

}
