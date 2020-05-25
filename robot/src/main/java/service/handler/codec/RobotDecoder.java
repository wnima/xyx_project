package service.handler.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import packet.CocoPacket;
import util.NettyUtil;

public class RobotDecoder extends ByteToMessageDecoder {

	private static final String DECODER_STATE_KEY = "DECODE_STATE";

	private static class DecoderState {
		public int length = -1;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		RobotDecoder.DecoderState state = NettyUtil.getAttribute(ctx, DECODER_STATE_KEY);
		if (state == null) {
			state = new RobotDecoder.DecoderState();
			NettyUtil.setAttribute(ctx, DECODER_STATE_KEY, state);
		}
		if (in.readableBytes() < 2 && state.length == -1) {
			return;
		}
		if (state.length != -1) {
			if (in.readableBytes() < state.length) {
				return;
			}
		} else {
			state.length = in.readShort();
			if (in.readableBytes() < state.length) {
				return;
			}
		}
		ByteBuf obj = Unpooled.buffer();
		obj.writeBytes(in, state.length);
		int reqCode = obj.readShort();
		int length = obj.readableBytes();
		byte[] bytes = new byte[obj.readableBytes()];
		obj.readBytes(bytes, 0, length);
		out.add(new CocoPacket(reqCode, bytes));
		state.length = -1;
	}
}
