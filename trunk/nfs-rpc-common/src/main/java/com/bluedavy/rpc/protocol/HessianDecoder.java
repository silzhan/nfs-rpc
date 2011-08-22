package com.bluedavy.rpc.protocol;

import java.io.ByteArrayInputStream;

import com.caucho.hessian.io.HessianInput;

public class HessianDecoder implements Decoder {

	public Object decode(byte[] bytes) throws Exception {
		HessianInput input = new HessianInput(new ByteArrayInputStream(bytes));
		Object resultObject = input.readObject();
//		input.close();
		return resultObject;
	}

}
