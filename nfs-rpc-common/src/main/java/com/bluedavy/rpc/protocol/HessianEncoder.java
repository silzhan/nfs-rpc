package com.bluedavy.rpc.protocol;

import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.HessianOutput;

public class HessianEncoder implements Encoder {

	public byte[] encode(Object object) throws Exception {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		HessianOutput output = new HessianOutput(byteArray);
		output.writeObject(object);
		byte[] bytes = byteArray.toByteArray();
		return bytes;
	}

}
