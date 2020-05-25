package network.handler;

import java.nio.charset.Charset;

import http.ResourceManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpRequest request = null;
		if (msg instanceof FullHttpRequest) {
			request = (FullHttpRequest) msg;
			System.out.println(request.getUri());
			System.out.println(request.getMethod().name());
		}
		String requestString = request.content().toString(Charset.forName("utf-8"));
		String result = ResourceManager.getInst().executeRequest(request.getUri(), request, requestString);
		FullHttpResponse response;
		if (result == null) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
		} else {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes()));
		}
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
		// if (HttpHeaders.isKeepAlive(request)) {
		// response.headers().set(HttpHeaders.Names.CONNECTION,
		// HttpHeaders.Values.KEEP_ALIVE);
		// }
		ChannelFuture future = ctx.writeAndFlush(response);
		future.addListener(e -> {
			if (e.isSuccess()) {
				future.channel().close();
			}
		});
	}
}
