package code.google.nfs.rpc.protocol;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import code.google.nfs.rpc.Codecs;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;

/**
 * Protocol Header
 * 	VERSION(1B): Protocol Version
 *  TYPE(1B):    Protocol Type,so u can custom your protocol
 *  Simple Processor Protocol
 * 	VERSION(1B):   
 *  TYPE(1B):      request/response 
 *  CODECTYPE(1B):  serialize/deserialize type
 *  KEEPED(1B):    
 *  KEEPED(1B):    
 *  KEEPED(1B):    
 *  ID(4B):        request id
 *  TIMEOUT(4B):   request timeout
 *  LENGTH(4B):    body length
 *  BODY
 *  
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class SimpleProcessorProtocol implements Protocol{
	
	public static final Integer TYPE = 2;
	
	private static final Log LOGGER = LogFactory.getLog(SimpleProcessorProtocol.class);
	
	private static final int CUSTOMPROTOCOL_HEADER_LEN = 1 * 6 + 3 * 4;
	
	private static final byte VERSION = (byte)1;
	
	private static final byte REQUEST = (byte)0;
	
	private static final byte RESPONSE = (byte)1;
	
	public ByteBufferWrapper encode(Object message,ByteBufferWrapper bytebufferWrapper) throws Exception{
		if(!(message instanceof RequestWrapper) && !(message instanceof ResponseWrapper)){
			throw new Exception("only support send RequestWrapper && ResponseWrapper");
		}
		int id = 0;
		byte type = REQUEST;
		byte[] body = null;
		int timeout = 0;
		Integer codecType = 0;
		if(message instanceof RequestWrapper){
			try{
				RequestWrapper wrapper = (RequestWrapper) message;
				codecType = wrapper.getCodecType();
				body = Codecs.getEncoder(codecType).encode(wrapper.getMessage()); 
				id = wrapper.getId();
				timeout = wrapper.getTimeout();
			}
			catch(Exception e){
				LOGGER.error("encode request object error",e);
				throw e;
			}
		}
		else{
			ResponseWrapper wrapper = (ResponseWrapper) message;
			try{
				codecType = wrapper.getCodecType();
				body = Codecs.getEncoder(codecType).encode(wrapper.getResponse()); 
				id = wrapper.getRequestId();
			}
			catch(Exception e){
				LOGGER.error("encode response object error",e);
				// still create response,so client can get it
				wrapper.setResponse(new Exception("encode response object error",e));
				body = Codecs.getEncoder(wrapper.getCodecType()).encode(wrapper.getResponse()); 
			}
			type = RESPONSE;
		}
		int capacity = ProtocolUtils.HEADER_LEN + CUSTOMPROTOCOL_HEADER_LEN + body.length;
		ByteBufferWrapper byteBuffer = bytebufferWrapper.get(capacity);
		byteBuffer.writeByte(ProtocolUtils.CURRENT_VERSION);
		byteBuffer.writeByte((byte)TYPE.intValue());
		byteBuffer.writeByte(VERSION);
		byteBuffer.writeByte(type);
		byteBuffer.writeByte((byte)codecType.intValue());
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeInt(id);
		byteBuffer.writeInt(timeout);
		byteBuffer.writeInt(body.length);
		byteBuffer.writeBytes(body);
		return byteBuffer;
	}
	
	public Object decode(ByteBufferWrapper wrapper,Object errorObject,int...originPosArray) throws Exception{
		final int originPos;
		if(originPosArray!=null && originPosArray.length == 1){
			originPos = originPosArray[0];
		}
		else{
			originPos = wrapper.readerIndex();
		}
		if(wrapper.readableBytes() < CUSTOMPROTOCOL_HEADER_LEN){
			wrapper.setReaderIndex(originPos);
        	return errorObject;
        }
        byte version = wrapper.readByte();
        if(version == (byte)1){
        	byte type = wrapper.readByte();
        	int codecType = wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
    		int requestId = wrapper.readInt();
    		int timeout = wrapper.readInt();
    		int expectedLen = wrapper.readInt();
    		if(wrapper.readableBytes() < expectedLen){
    			wrapper.setReaderIndex(originPos);
    			return errorObject;
    		}
    		byte[] body = new byte[expectedLen];
    		wrapper.readBytes(body);
        	if(type == REQUEST){
        		RequestWrapper requestWrapper = new RequestWrapper(body,timeout,requestId,codecType, TYPE);
        		return requestWrapper;
        	}
        	else if(type == RESPONSE){
        		ResponseWrapper responseWrapper = new ResponseWrapper(requestId,codecType,TYPE);
            	responseWrapper.setResponse(body);
	        	return responseWrapper;
        	}
        	else{
        		throw new UnsupportedOperationException("protocol type : "+type+" is not supported!");
        	}
        }
        else{
        	throw new UnsupportedOperationException("protocol version :"+version+" is not supported!");
        }
	}

}
