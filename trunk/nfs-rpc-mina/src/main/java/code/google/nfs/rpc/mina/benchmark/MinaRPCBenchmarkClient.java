package code.google.nfs.rpc.mina.benchmark;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import code.google.nfs.rpc.benchmark.AbstractRPCBenchmarkClient;
import code.google.nfs.rpc.benchmark.BenchmarkTestService;
import code.google.nfs.rpc.mina.client.MinaClientInvocationHandler;


public class MinaRPCBenchmarkClient extends AbstractRPCBenchmarkClient {

	public static void main(String[] args) throws Exception {
		new MinaRPCBenchmarkClient().run(args);
	}

	public BenchmarkTestService getProxyInstance(
			List<InetSocketAddress> servers, int clientNums,
			int connectTimeout, String targetInstanceName,
			Map<String, Integer> methodTimeouts, int datatype) {
		return (BenchmarkTestService) Proxy.newProxyInstance(
				MinaRPCBenchmarkClient.class.getClassLoader(),
				new Class<?>[] { BenchmarkTestService.class },
				new MinaClientInvocationHandler(servers, clientNums,
						connectTimeout, targetInstanceName, methodTimeouts,datatype));
	}

}
