package code.google.nfs.rpc.netty.serialize;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import code.google.nfs.rpc.ProtocolFactory;
/**
 * Encode Message
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyProtocolEncoder extends OneToOneEncoder {
	
	protected Object encode(ChannelHandlerContext ctx, Channel channel,Object message) throws Exception {
		NettyByteBufferWrapper byteBufferWrapper = new NettyByteBufferWrapper();
		ProtocolFactory.getProtocol().encode(message, byteBufferWrapper);
		return byteBufferWrapper.getBuffer();
	}

}
