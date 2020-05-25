package service.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import packet.CocoPacket;

public class RobotEncoder extends MessageToByteEncoder<CocoPacket> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, CocoPacket packet, ByteBuf byteBuf) throws Exception {
		ByteBuf buffer = Unpooled.buffer();
		if (packet.getBytes() == null) {
			buffer.writeShort(2);
			buffer.writeShort(packet.getReqId());
		} else {
			buffer.writeShort(2 + packet.getBytes().length);
			buffer.writeShort(packet.getReqId());
			buffer.writeBytes(packet.getBytes());
		}
		byteBuf.writeBytes(buffer);
	}
}
