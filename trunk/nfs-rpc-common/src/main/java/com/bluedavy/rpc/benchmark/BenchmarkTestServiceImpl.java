package com.bluedavy.rpc.benchmark;

public class BenchmarkTestServiceImpl implements BenchmarkTestService {

	private int responseSize;
	
	public BenchmarkTestServiceImpl(int responseSize){
		this.responseSize = responseSize;
	}
	
	public ResponseObject execute(RequestObject request) {
		return new ResponseObject(responseSize);
	}

}
