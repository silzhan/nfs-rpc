package com.bluedavy.rpc.client;

/**
 * RPC Client Interface
 */
public interface Client {

	/**
	 * invoke sync via simple call
	 * 
	 * @param message
	 * @param timeout
	 * @param dataType data serialize/deserialize type
	 * @return Object
	 * @throws Exception
	 */
	public Object invokeSync(Object message, int timeout, int dataType)
			throws Exception;

	/**
	 * invoke sync via rpc
	 * 
	 * @param targetInstanceName
	 *            server instance name
	 * @param methodName
	 *            server method name
	 * @param argTypes
	 *            server method arg types
	 * @param args
	 *            send to server request args
	 * @param timeout
	 *            rcp timeout
	 * @param dataType
	 *            data serialize/deserialize type
	 * @return Object return response
	 * @throws Exception
	 *             if some exception,then will be throwed
	 */
	public Object invokeSync(String targetInstanceName, String methodName,
			String[] argTypes, Object[] args, int timeout, int dataType)
			throws Exception;

	/**
	 * server address
	 * 
	 * @return String
	 */
	public String getServerIP();

	/**
	 * server port
	 * 
	 * @return int
	 */
	public int getServerPort();

	/**
	 * connect timeout
	 * 
	 * @return
	 */
	public int getConnectTimeout();

}
