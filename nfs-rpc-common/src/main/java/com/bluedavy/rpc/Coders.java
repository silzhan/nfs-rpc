package com.bluedavy.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bluedavy.rpc.protocol.Decoder;
import com.bluedavy.rpc.protocol.Encoder;
import com.bluedavy.rpc.protocol.HessianDecoder;
import com.bluedavy.rpc.protocol.HessianEncoder;
import com.bluedavy.rpc.protocol.JavaDecoder;
import com.bluedavy.rpc.protocol.JavaEncoder;
import com.bluedavy.rpc.protocol.Protocol;

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
