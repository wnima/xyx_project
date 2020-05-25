package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import network.handler.HttpHandler;

public class NettyHttpServer {

	private int port;

	public NettyHttpServer(int port) {
		this.port = port;
	}

	public void start() {
		EventLoopGroup parent = new NioEventLoopGroup();
		EventLoopGroup child = new NioEventLoopGroup();
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(parent, child);
		boot.channel(NioServerSocketChannel.class);
		boot.childHandler(new ChannelInitializer<NioSocketChannel>() {
			protected void initChannel(NioSocketChannel ch) throws Exception {
				ch.pipeline().addLast(new HttpRequestDecoder());
				ch.pipeline().addLast(new HttpResponseEncoder());
				ch.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
				ch.pipeline().addLast(new HttpHandler());
			}
		});
		boot.localAddress(port);
		boot.childOption(ChannelOption.TCP_NODELAY, true);
		boot.childOption(ChannelOption.SO_KEEPALIVE, true);
		try {
			boot.bind().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
