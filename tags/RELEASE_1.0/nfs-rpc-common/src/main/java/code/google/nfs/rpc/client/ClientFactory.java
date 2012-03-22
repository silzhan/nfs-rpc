/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package code.google.nfs.rpc.client;
/**
 * RPC ClientFactory Interface,help for get approviate nums client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface ClientFactory {

	/**
	 * get client,default targetIP:targetPort --> one connection
	 * u can give custom the key by give customKey
	 */
	public abstract Client get(final String targetIP, final int targetPort,
			final int connectTimeout, String... customKey) throws Exception;

	/**
	 * get client,create clientNums connections to targetIP:targetPort(or your custom key)
	 */
	public abstract Client get(final String targetIP, final int targetPort,
			final int connectTimeout, final int clientNums, String... customKey)
			throws Exception;

	/**
	 * remove some error client
	 */
	public abstract void removeClient(String key, Client client);

}