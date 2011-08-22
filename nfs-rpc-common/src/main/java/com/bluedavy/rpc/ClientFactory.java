package com.bluedavy.rpc;

public interface ClientFactory {

	/**
	 * 根据targetIP,targetPort获取Client，如不存在，则创建一个，默认每个targetIP :
	 * targetPort只会创建一个Client
	 * 
	 * 默认情况下以targetIP : targetPort 为key，如需要采用其他key，则请自定义的传入
	 */
	public abstract Client get(final String targetIP, final int targetPort,
			final int connectTimeout, String... customKey) throws Exception;

	/**
	 * 根据targetIP,targetPort获取Client，如不存在，则创建，默认每个targetIP :
	 * targetPort创建clientNums个数的Client instance
	 * 
	 * 选择时Random
	 * 
	 * 默认情况下以targetIP : targetPort 为key，如需要采用其他key，则请自定义的传入
	 */
	public abstract Client get(final String targetIP, final int targetPort,
			final int connectTimeout, final int clientNums, String... customKey)
			throws Exception;

	/**
	 * 移除掉某个key下的某个Client
	 * 
	 * @param key
	 * @param client
	 */
	public abstract void removeClient(String key, Client client);

}