package code.google.nfs.rpc.grizzly.serialize;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;
import code.google.nfs.rpc.protocol.ProtocolUtils;

/**
 * Grizzly Protocol Decoder
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GrizzlyProtocolFilter extends BaseFilter{
	
	private static final Log LOGGER = LogFactory.getLog(GrizzlyProtocolFilter.class);
	
	// decode object
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		final Buffer buffer = ctx.getMessage();
		final int bufferLen = buffer.remaining();
		Object errorReturnObject = new Object();
		GrizzlyByteBufferWrapper wrapper = new GrizzlyByteBufferWrapper(buffer);
		try {
			Object object = ProtocolUtils.decode(wrapper, errorReturnObject);
			if(object == errorReturnObject){
				return ctx.getStopAction(buffer);
			}
			else{
				ctx.setMessage(object);
				int completeMessageLen = 0;
				if(object instanceof RequestWrapper){
					completeMessageLen = ((RequestWrapper)object).getMessageLen();
				}
				else{
					completeMessageLen = ((ResponseWrapper)object).getMessageLen();
				}
				final Buffer remainder = bufferLen > completeMessageLen ? 
						buffer.split(completeMessageLen) : null;
				buffer.tryDispose();
				return ctx.getInvokeAction(remainder);
			}
		} 
		catch (Exception e) {
			LOGGER.error("decode message error",e);
			throw new IOException(e);
		}
	}
	
	// encode object
	public NextAction handleWrite(FilterChainContext ctx) throws IOException {
		GrizzlyByteBufferWrapper wrapper = new GrizzlyByteBufferWrapper(ctx);
		try {
			ProtocolUtils.encode(ctx.getMessage(), wrapper);
			ctx.setMessage(wrapper.getBuffer().flip());
		} 
		catch (Exception e) {
			throw new IOException("encode message to byte error",e);
		}
		return ctx.getInvokeAction();
	}

}
