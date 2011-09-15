package code.google.nfs.rpc.grizzly.server;

/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

import code.google.nfs.rpc.ProtocolFactory;
import code.google.nfs.rpc.grizzly.serialize.GrizzlyProtocolFilter;
import code.google.nfs.rpc.server.Server;

/**
 * Grizzly Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyServer implements Server {

	private static final Log LOGGER = LogFactory.getLog(GrizzlyServer.class);
	private TCPNIOTransport transport = null;
	
	public void start(int listenPort, ExecutorService threadpool) throws Exception {
		FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
		filterChainBuilder.add(new TransportFilter());
        filterChainBuilder.add(new GrizzlyProtocolFilter(true));
		filterChainBuilder.add(new GrizzlyServerHandler());
		TCPNIOTransportBuilder builder = TCPNIOTransportBuilder.newInstance();
		ThreadPoolConfig config = builder.getWorkerThreadPoolConfig();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) threadpool;
		config.setCorePoolSize(threadPoolExecutor.getCorePoolSize()).setMaxPoolSize(threadPoolExecutor.getMaximumPoolSize()).setPoolName("GRIZZLY-SERVER");
		transport = builder.build();
		transport.setIOStrategy(WorkerThreadIOStrategy.getInstance());

		transport.setProcessor(filterChainBuilder.build());
		transport.bind(listenPort);

        transport.start();
        LOGGER.warn("server started,listen at: " + listenPort);
	} 
	
	public void stop() throws Exception {
        if (transport != null) {
			transport.stop();
			LOGGER.warn("server stoped!");
		}
	}
	
	public void registerProcessor(int protocolType, String serviceName, Object serviceInstance) {
		ProtocolFactory.getServerHandler(protocolType).registerProcessor(serviceName, serviceInstance);
	}
}
