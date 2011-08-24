package code.google.nfs.rpc.mina.serialize;

import org.apache.mina.filter.codec.ProtocolCodecFilter;


public class MinaProtocolCodecFilter extends ProtocolCodecFilter {

	public MinaProtocolCodecFilter() {
		super(new MinaProtocolEncoder(),new MinaProtocolDecoder());
	}

}
