/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.protocol;
/**
 * Protocol Interface,for custom network protocol
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Protocol {

	public static final int JAVA_DATA = 1;
	
	public static final int HESSIAN_DATA = 2;
	
	/**
	 * encode Message to byte & write to network framework
	 * 
	 * @param message
	 * @param byteBuffer
	 * @throws Exception
	 */
	public ByteBufferWrapper encode(Object message,ByteBufferWrapper bytebufferWrapper) throws Exception;

	/**
	 * decode stream to object
	 * 
	 * @param wrapper
	 * @param errorObject stream not enough,then return this object
	 * @return Object 
	 * @throws Exception
	 */
	public Object decode(ByteBufferWrapper wrapper, Object errorObject) throws Exception;

}