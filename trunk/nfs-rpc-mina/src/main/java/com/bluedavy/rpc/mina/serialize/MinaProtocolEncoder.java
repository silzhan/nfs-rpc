package com.bluedavy.rpc.mina.serialize;

import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.bluedavy.rpc.RPCProtocolUtil;
/**
 * encode message
 */
public class MinaProtocolEncoder extends ProtocolEncoderAdapter {
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		MinaByteBufferWrapper wrapper = new MinaByteBufferWrapper(); 
		RPCProtocolUtil.encode(message, wrapper);
		wrapper.getByteBuffer().flip();
		out.write(wrapper.getByteBuffer());
	}

}
