package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import code.google.nfs.rpc.protocol.Decoder;
import code.google.nfs.rpc.protocol.Encoder;
import code.google.nfs.rpc.protocol.HessianDecoder;
import code.google.nfs.rpc.protocol.HessianEncoder;
import code.google.nfs.rpc.protocol.JavaDecoder;
import code.google.nfs.rpc.protocol.JavaEncoder;
import code.google.nfs.rpc.protocol.Protocol;
/**
 * Encoder & Decoder Register
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class Coders {
	
	private static Map<String, Encoder> encoders = new ConcurrentHashMap<String, Encoder>();
	
	private static Map<String, Decoder> decoders = new ConcurrentHashMap<String, Decoder>();
	
	static{
		addEncoder(String.valueOf(Protocol.JAVA_DATA), new JavaEncoder());
		addEncoder(String.valueOf(Protocol.HESSIAN_DATA), new HessianEncoder());
		addDecoder(String.valueOf(Protocol.JAVA_DATA), new JavaDecoder());
		addDecoder(String.valueOf(Protocol.HESSIAN_DATA), new HessianDecoder());
	}
	
	public static void addEncoder(String encoderKey,Encoder encoder){
		encoders.put(encoderKey, encoder);
	}
	
	public static void addDecoder(String decoderKey,Decoder decoder){
		decoders.put(decoderKey, decoder);
	}
	
	public static Encoder getEncoder(String encoderKey){
		return encoders.get(encoderKey);
	}
	
	public static Decoder getDecoder(String decoderKey){
		return decoders.get(decoderKey);
	}
	
}
