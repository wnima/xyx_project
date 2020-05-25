package service.handler.network.websocket;

import java.nio.charset.Charset;

import http.ResourceManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		System.out.println("收到的http请求");
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

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace(System.err);
	}
}
