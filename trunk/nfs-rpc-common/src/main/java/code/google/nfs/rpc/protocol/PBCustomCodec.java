/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.protocol;
/**
 * ProtocolBuf Custom Codec
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface PBCustomCodec {

	public Object decode(byte[] bytes) throws Exception;
	
	public byte[] encode(Object object) throws Exception;
	
}
