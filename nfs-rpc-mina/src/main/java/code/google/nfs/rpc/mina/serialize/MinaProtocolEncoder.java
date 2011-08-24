package code.google.nfs.rpc.mina.serialize;

import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import code.google.nfs.rpc.ProtocolFactory;
/**
 * encode message
 */
public class MinaProtocolEncoder extends ProtocolEncoderAdapter {
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		MinaByteBufferWrapper wrapper = new MinaByteBufferWrapper(); 
		ProtocolFactory.getProtocol().encode(message, wrapper);
		wrapper.getByteBuffer().flip();
		out.write(wrapper.getByteBuffer());
	}

}
