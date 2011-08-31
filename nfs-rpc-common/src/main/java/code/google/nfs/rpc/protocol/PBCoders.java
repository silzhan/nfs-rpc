package code.google.nfs.rpc.protocol;

import java.util.concurrent.ConcurrentHashMap;

public class PBCoders {

	private static final ConcurrentHashMap<String, PBCustomCodec> pbCodecs = new ConcurrentHashMap<String, PBCustomCodec>();
	
	public static void add(String className,PBCustomCodec codec){
		pbCodecs.put(className, codec);
	}
	
	public static PBCustomCodec get(String className){
		return pbCodecs.get(className);
	}
	
}
