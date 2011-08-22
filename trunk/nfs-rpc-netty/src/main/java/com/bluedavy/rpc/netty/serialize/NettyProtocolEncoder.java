package com.bluedavy.rpc.netty.serialize;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.bluedavy.rpc.RPCProtocolUtil;
/**
 * encode message to byte
 */
public class NettyProtocolEncoder extends OneToOneEncoder {
	
	protected Object encode(ChannelHandlerContext ctx, Channel channel,Object message) throws Exception {
		NettyByteBufferWrapper byteBufferWrapper = new NettyByteBufferWrapper();
		RPCProtocolUtil.encode(message, byteBufferWrapper);
		return byteBufferWrapper.getBuffer();
	}

}
