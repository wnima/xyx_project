package packet;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import protocol.s2c.ResponseCode;

public class PacketCreator {

	public static ByteBuf create(ResponseCode code, MessageLite message) {
		return create(code.getValue(), message);
	}

	public static ByteBuf create(int code, MessageLite message) {
		return create(code, message == null ? null : message.toByteArray());
	}

	public static ByteBuf create(int code, byte[] bytes) {
		ByteBuf buffer = Unpooled.buffer();
		int length = 2;
		if (bytes != null) {
			length += bytes.length;
		}
		buffer.writeShort(length);
		buffer.writeShort(code);
		if (bytes != null) {
			buffer.writeBytes(bytes);
		}
		return buffer;
	}
	
	public static BinaryWebSocketFrame createCocos(int code, byte[] bytes) {
		ByteBuf buffer = Unpooled.buffer();
		int length = 4;
		if (bytes != null) {
			length += bytes.length;
		}
		buffer.writeInt(length);
		buffer.writeInt(code);
		if (bytes != null) {
			buffer.writeBytes(bytes);
		}
		return new BinaryWebSocketFrame(buffer);
	}


}
