/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.protocol;
/**
 * ProtocolBuf Decoder
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class PBDecoder implements Decoder {

	public Object decode(String className,byte[] bytes) throws Exception {
		return PBCoders.get(className).decode(bytes);
	}

}
