package code.google.nfs.rpc.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.ByteString;

import code.google.nfs.rpc.NamedThreadFactory;
import code.google.nfs.rpc.protocol.PBCoders;
import code.google.nfs.rpc.protocol.RPCProtocol;
import code.google.nfs.rpc.protocol.SimpleProcessorProtocol;
import code.google.nfs.rpc.server.Server;
import code.google.nfs.rpc.server.ServerProcessor;


/**
 * Abstract benchmark server
 * 
 * Usage: BenchmarkServer listenPort maxThreads responseSize
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractBenchmarkServer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public void run(String[] args) throws Exception {
		if (args == null || args.length != 3) {
			throw new IllegalArgumentException(
					"must give three args: listenPort | maxThreads | responseSize");
		}
		int listenPort = Integer.parseInt(args[0]);
		int maxThreads = Integer.parseInt(args[1]);
		final int responseSize = Integer.parseInt(args[2]);
		System.out.println(dateFormat.format(new Date())
				+ " ready to start server,listenPort is: " + listenPort
				+ ",maxThreads is:" + maxThreads + ",responseSize is:"
				+ responseSize + " bytes");

		Server server = getServer();
		server.registerProcessor(SimpleProcessorProtocol.TYPE,RequestObject.class.getName(), new ServerProcessor() {
			public Object handle(Object request) throws Exception {
				return new ResponseObject(responseSize);
			}
		});
		// for pb codec
		PBCoders.add(PB.RequestObject.class.getName(), new PBBenchmarkRequestCodec());
		PBCoders.add(PB.ResponseObject.class.getName(), new PBBenchmarkResponseCodec());
		server.registerProcessor(SimpleProcessorProtocol.TYPE,PB.RequestObject.class.getName(), new ServerProcessor() {
			public Object handle(Object request) throws Exception {
				PB.ResponseObject.Builder  builder = PB.ResponseObject.newBuilder();
				builder.setBytesObject(ByteString.copyFrom(new byte[responseSize]));
				return builder.build();
			}
		});
		server.registerProcessor(RPCProtocol.TYPE, "testservice", new BenchmarkTestServiceImpl(responseSize));
		ThreadFactory tf = new NamedThreadFactory("BUSINESSTHREADPOOL");
		ExecutorService threadPool = new ThreadPoolExecutor(20, maxThreads,
				300, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
		server.start(listenPort, threadPool);
	}
	
	/**
	 * Get server instance
	 */
	public abstract Server getServer();

}
