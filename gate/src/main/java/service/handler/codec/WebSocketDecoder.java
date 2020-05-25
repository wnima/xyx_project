package service.handler.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import packet.CocoPacket;
import util.NettyUtil;

public class WebSocketDecoder extends MessageToMessageDecoder<WebSocketFrame> {

	private static final String DECODER_STATE_KEY = "DECODE_STATE";

	private static class DecoderState {
		public int length = -1;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
		if (frame instanceof BinaryWebSocketFrame) {
			ByteBuf in = ((BinaryWebSocketFrame) frame).content();
			WebSocketDecoder.DecoderState state = NettyUtil.getAttribute(ctx, DECODER_STATE_KEY);
			if (state == null) {
				state = new WebSocketDecoder.DecoderState();
				NettyUtil.setAttribute(ctx, DECODER_STATE_KEY, state);
			}
			if (in.readableBytes() < 4 && state.length == -1) {
				return;
			}
			if (state.length != -1) {
				if (in.readableBytes() < state.length) {
					return;
				}
			} else {
				state.length = in.readInt();
				if (in.readableBytes() < state.length) {
					return;
				}
			}

			if (state.length < 2) {
				ctx.close();
			}
			ByteBuf obj = Unpooled.buffer();
			obj.writeBytes(in, state.length);
			int reqCode = obj.readInt();
			int length = obj.readableBytes();
			byte[] bytes = new byte[obj.readableBytes()];
			obj.readBytes(bytes, 0, length);
			out.add(new CocoPacket(reqCode, bytes));
			state.length = -1;
		}

		// 文本暂时不用
		if (frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame text = (TextWebSocketFrame) frame;
			out.add(text.text());
		}
		
	}
}
