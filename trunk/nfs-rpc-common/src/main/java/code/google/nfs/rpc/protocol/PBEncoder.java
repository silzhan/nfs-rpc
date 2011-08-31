/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.protocol;
/**
 * ProtocolBuf Encoder
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class PBEncoder implements Encoder {

	public byte[] encode(Object object) throws Exception {
		return PBCoders.get(object.getClass().getName()).encode(object);
	}

}
