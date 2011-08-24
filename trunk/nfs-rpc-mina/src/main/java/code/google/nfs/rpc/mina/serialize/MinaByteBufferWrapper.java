package code.google.nfs.rpc.mina.serialize;

import org.apache.mina.common.ByteBuffer;

import code.google.nfs.rpc.protocol.ByteBufferWrapper;

public class MinaByteBufferWrapper implements ByteBufferWrapper {

	private ByteBuffer byteBuffer;
	
	public MinaByteBufferWrapper(){
		;
	}
	
	public MinaByteBufferWrapper(ByteBuffer byteBuffer){
		this.byteBuffer = byteBuffer;
	}
	
	public ByteBufferWrapper get(int capacity) {
		byteBuffer = ByteBuffer.allocate(capacity,false);
		return this;
	}

	public byte readByte() {
		return byteBuffer.get();
	}

	public void readBytes(byte[] dst) {
		byteBuffer.get(dst);
	}

	public int readInt() {
		return byteBuffer.getInt();
	}

	public int readableBytes() {
		return byteBuffer.remaining();
	}

	public int readerIndex() {
		return byteBuffer.position();
	}

	public void setReaderIndex(int index) {
		byteBuffer.position(index);
	}

	public void writeByte(byte data) {
		byteBuffer.put(data);
	}

	public void writeBytes(byte[] data) {
		byteBuffer.put(data);
	}

	public void writeInt(int data) {
		byteBuffer.putInt(data);
	}
	
	public ByteBuffer getByteBuffer(){
		return byteBuffer;
	}

}
