package code.google.nfs.rpc.server;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.concurrent.ExecutorService;
/**
 * RPC Server Interface
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface Server {

	/**
	 * 指定listenPort启动Server
	 * 
	 * @param listenPort 监听端口
	 * @param businessThreadPool 业务线程池
	 * @throws Exception
	 */
	public void start(int listenPort,ExecutorService businessThreadPool) throws Exception;
	
	/**
	 * 注册业务处理器
	 * 
	 * @param serviceName
	 * @param serviceInstance
	 */
	public void registerProcessor(String serviceName,Object serviceInstance);
	
	/**
	 * 停止Server
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception;
	
}
