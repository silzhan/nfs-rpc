package code.google.nfs.rpc.grizzly.client;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;

import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

import code.google.nfs.rpc.ResponseWrapper;
import code.google.nfs.rpc.client.Client;
/**
 * Grizzly Client Handler
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyClientHandler extends BaseFilter {
	
	private Client client;
	
	public void setClient(Client client){
		this.client = client;
	}
	
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		try {
			client.putResponse((ResponseWrapper) ctx.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ctx.getStopAction();
	}

}
