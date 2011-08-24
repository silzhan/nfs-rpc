package code.google.nfs.rpc.mina.serialize;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import code.google.nfs.rpc.ProtocolFactory;
/**
 * decode byte[]
 */
public class MinaProtocolDecoder extends CumulativeProtocolDecoder {
	
	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		MinaByteBufferWrapper wrapper = new MinaByteBufferWrapper(in);
		Object returnObject = ProtocolFactory.getProtocol().decode(wrapper, false);
		if(returnObject instanceof Boolean){
			return false;
		}
		out.write(returnObject);
		return true;
	}

}
