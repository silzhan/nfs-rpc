package code.google.nfs.rpc.mina.serialize;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.mina.filter.codec.ProtocolCodecFilter;

/**
 * Encoder & Decoder Filter for mina
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class MinaProtocolCodecFilter extends ProtocolCodecFilter {

	public MinaProtocolCodecFilter() {
		super(new MinaProtocolEncoder(),new MinaProtocolDecoder());
	}

}
