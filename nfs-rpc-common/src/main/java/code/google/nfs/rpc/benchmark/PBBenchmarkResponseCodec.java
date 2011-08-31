package code.google.nfs.rpc.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.ByteArrayOutputStream;

import com.google.protobuf.ByteString;

import code.google.nfs.rpc.protocol.PBCustomCodec;
/**
 * PB RequestObject encode & decode
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class PBBenchmarkResponseCodec implements PBCustomCodec{

	public Object decode(byte[] bytes) throws Exception {
		PB.ResponseObject.Builder builder = PB.ResponseObject.newBuilder();
		builder.setBytesObject(ByteString.copyFrom(bytes));
		return builder.build();
	}

	public byte[] encode(Object object) throws Exception {
		PB.ResponseObject.Builder builder = PB.ResponseObject.newBuilder((PB.ResponseObject)object);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		builder.build().writeTo(output);
		output.close();
		return output.toByteArray();
	}

}
