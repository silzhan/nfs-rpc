package code.google.nfs.rpc;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.protocol.Decoder;
import code.google.nfs.rpc.protocol.Encoder;
import code.google.nfs.rpc.protocol.HessianDecoder;
import code.google.nfs.rpc.protocol.HessianEncoder;
import code.google.nfs.rpc.protocol.JavaDecoder;
import code.google.nfs.rpc.protocol.JavaEncoder;
import code.google.nfs.rpc.protocol.PBDecoder;
import code.google.nfs.rpc.protocol.PBEncoder;
/**
 * Encoder & Decoder Register
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class Codecs {
	
	public static final Integer JAVA_CODEC = 1;
	
	public static final Integer HESSIAN_CODEC = 2;
	
	public static final Integer PB_CODEC = 3;
	
	private static Encoder[] encoders = new Encoder[10];
	
	private static Decoder[] decoders = new Decoder[10];
	
	static{
		addEncoder(JAVA_CODEC, new JavaEncoder());
		addEncoder(HESSIAN_CODEC, new HessianEncoder());
		addEncoder(PB_CODEC, new PBEncoder());
		addDecoder(JAVA_CODEC, new JavaDecoder());
		addDecoder(HESSIAN_CODEC, new HessianDecoder());
		addDecoder(PB_CODEC, new PBDecoder());
	}
	
	public static void addEncoder(Integer encoderKey,Encoder encoder){
		encoders[encoderKey] = encoder;
	}
	
	public static void addDecoder(Integer decoderKey,Decoder decoder){
		decoders[decoderKey] = decoder;
	}
	
	public static Encoder getEncoder(Integer encoderKey){
		return encoders[encoderKey];
	}
	
	public static Decoder getDecoder(Integer decoderKey){
		return decoders[decoderKey];
	}
	
}
