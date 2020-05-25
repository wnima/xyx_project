package http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

public class GateHttpHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpRequest request = null;
		if (msg instanceof FullHttpRequest) {
			request = (FullHttpRequest) msg;
			System.out.println(request.getUri());
			System.out.println(request.getMethod().name());
		}

//		String requestString = request.content().toString(Charset.forName("utf-8"));
		Map<String, String> params = convertToMap(request.getUri(), request);
		String result = ResourceManager.getInst().execute(request.getUri(), params);
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

	private Map<String, String> convertToMap(String uri, FullHttpRequest req) {
		Map<String, String> params = new HashMap<>();

		// 是GET请求
		if (HttpMethod.GET.equals(req.getMethod())) {
			// 解析请求参数
			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
			Map<String, List<String>> paramMap = queryStringDecoder.parameters();
			for (Map.Entry<String, List<String>> entry : paramMap.entrySet()) {
				params.put(entry.getKey(), entry.getValue().get(0));
			}
		}

		if (HttpMethod.POST.equals(req.getMethod())) {
			// 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
			List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : postList) {
				if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
					MemoryAttribute attribute = (MemoryAttribute) data;
					params.put(attribute.getName(), attribute.getValue());
				}
			}
		}
		return params;
	}

}
