package service.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import network.AbstractHandlers;
import service.dispath.MessageFromClient;
import service.handler.codec.WebSocketDecoder;
import service.handler.network.websocket.WebSocketHandler;

public class WebSocketServer {

	ServerBootstrap boot = new ServerBootstrap();

	private AbstractHandlers handler;

	private int port;

	public WebSocketServer(int port) {
		this.port = port;
	}

	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup wokerGroup = new NioEventLoopGroup();

		boot.group(bossGroup, wokerGroup);
		boot.localAddress(port);
		boot.channel(NioServerSocketChannel.class);
		boot.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline pipeline = socketChannel.pipeline();
				pipeline.addLast("http-codec",new HttpServerCodec());
				pipeline.addLast("http-chunked",new ChunkedWriteHandler());
				pipeline.addLast("aggregator",new HttpObjectAggregator(1024 * 1024 * 1024));
				pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65535));
				pipeline.addLast(new WebSocketDecoder());
				pipeline.addLast(new WebSocketHandler(new MessageFromClient()));
			}
		});
		try {
			boot.option(ChannelOption.SO_BACKLOG, 128);
			boot.childOption(ChannelOption.SO_KEEPALIVE, true);
			boot.bind().sync().channel();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public AbstractHandlers getHandler() {
		return handler;
	}

	public void setHandler(AbstractHandlers handler) {
		this.handler = handler;
	}

	public int getPort() {
		return port;
	}

	public static void main(String[] args) {
		WebSocketServer servers = new WebSocketServer(10003);
		try {
			servers.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
