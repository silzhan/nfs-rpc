package code.google.nfs.rpc.protocol;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.Coders;
import code.google.nfs.rpc.RequestWrapper;
import code.google.nfs.rpc.ResponseWrapper;

/**
 * Simple Processor Protocol
 * 	VERSION(1B):   
 *  TYPE(1B):      request/response 
 *  DATATYPE(1B):  serialize/deserialize type
 *  KEEPED(1B):    
 *  KEEPED(1B):    
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
	
	private static final int HEADER_LEN = 1 * 8 + 3 * 4;
	
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
		int dataType = 0;
		if(message instanceof RequestWrapper){
			try{
				RequestWrapper wrapper = (RequestWrapper) message;
				dataType = wrapper.getDataType();
				body = Coders.getEncoder(String.valueOf(dataType)).encode(wrapper.getMessage()); 
				id = wrapper.getId();
				timeout = wrapper.getTimeout();
			}
			catch(Exception e){
				e.printStackTrace();
				// TODO: 处理异常
				// LOGGER.error("serialize request object error",e);
				// TODO: 直接创建一个响应返回，避免需要等到超时
				throw e;
			}
		}
		else{
			ResponseWrapper wrapper = (ResponseWrapper) message;
			try{
				dataType = wrapper.getDataType();
				body = Coders.getEncoder(String.valueOf(dataType)).encode(wrapper.getResponse()); 
				id = wrapper.getRequestId();
			}
			catch(Exception e){
				// TODO: 处理异常
				e.printStackTrace();
				// LOGGER.error("serialize response object error",e);
				// 仍然创建响应客户端，以便客户端快速接到响应做相应的处理
				wrapper.setResponse(new Exception("serialize response object error",e));
				body = Coders.getEncoder(String.valueOf(wrapper.getDataType())).encode(wrapper.getResponse()); 
			}
			type = RESPONSE;
		}
		int capacity = HEADER_LEN + body.length;
		ByteBufferWrapper byteBuffer = bytebufferWrapper.get(capacity);
		byteBuffer.writeByte(VERSION);
		byteBuffer.writeByte(type);
		byteBuffer.writeByte((byte)dataType);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeByte((byte)0);
		byteBuffer.writeInt(id);
		byteBuffer.writeInt(timeout);
		byteBuffer.writeInt(body.length);
		byteBuffer.writeBytes(body);
		return byteBuffer;
	}
	
	public Object decode(ByteBufferWrapper wrapper,Object errorObject) throws Exception{
		final int originPos = wrapper.readerIndex();
		if(wrapper.readableBytes() < HEADER_LEN){
			wrapper.setReaderIndex(originPos);
        	return errorObject;
        }
        byte version = wrapper.readByte();
        if(version == (byte)1){
        	byte type = wrapper.readByte();
        	int dataType = wrapper.readByte();
    		wrapper.readByte();
    		wrapper.readByte();
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
        		RequestWrapper requestWrapper = new RequestWrapper(body,timeout,requestId,dataType);
        		return requestWrapper;
        	}
        	else if(type == RESPONSE){
        		ResponseWrapper responseWrapper = new ResponseWrapper();
            	responseWrapper.setRequestId(requestId);
            	responseWrapper.setResponse(body);
            	responseWrapper.setDataType(dataType);
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
