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
/**
 * Encoder & Decoder Register
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class Codecs {
	
	public static final Integer JAVA_CODEC = 1;
	
	public static final Integer HESSIAN_CODEC = 2;
	
	private static Map<Integer, Encoder> encoders = new ConcurrentHashMap<Integer, Encoder>();
	
	private static Map<Integer, Decoder> decoders = new ConcurrentHashMap<Integer, Decoder>();
	
	static{
		addEncoder(JAVA_CODEC, new JavaEncoder());
		addEncoder(HESSIAN_CODEC, new HessianEncoder());
		addDecoder(JAVA_CODEC, new JavaDecoder());
		addDecoder(HESSIAN_CODEC, new HessianDecoder());
	}
	
	public static void addEncoder(Integer encoderKey,Encoder encoder){
		encoders.put(encoderKey, encoder);
	}
	
	public static void addDecoder(Integer decoderKey,Decoder decoder){
		decoders.put(decoderKey, decoder);
	}
	
	public static Encoder getEncoder(Integer encoderKey){
		return encoders.get(encoderKey);
	}
	
	public static Decoder getDecoder(Integer decoderKey){
		return decoders.get(decoderKey);
	}
	
}
