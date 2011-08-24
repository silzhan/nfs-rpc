package code.google.nfs.rpc.netty.serialize;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import code.google.nfs.rpc.ProtocolFactory;
/**
 * decode byte[]
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyProtocolDecoder extends FrameDecoder {
	
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
		NettyByteBufferWrapper wrapper = new NettyByteBufferWrapper(in);
		return ProtocolFactory.getProtocol().decode(wrapper, null);
	}

}
