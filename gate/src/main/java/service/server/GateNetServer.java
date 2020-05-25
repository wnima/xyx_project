package service.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import packet.CocoPacket;
import service.dispath.MessageFromClient;
import service.handler.codec.GateDecoder;
import service.handler.network.socket.GateNetHandler;

public class GateNetServer {
	private ServerBootstrap boot = new ServerBootstrap();

	private ByteToMessageDecoder decoder;

	private MessageToByteEncoder<CocoPacket> encoder;

	private short port;

	public GateNetServer(int port) {
		this.port = (short) port;
	}

	public void start() {
		NioEventLoopGroup parentGroup = new NioEventLoopGroup();
		NioEventLoopGroup childGroup = new NioEventLoopGroup();
		
		boot.group(parentGroup, childGroup);
		boot.localAddress(port);
		boot.channel(NioServerSocketChannel.class);
		boot.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel channel) throws Exception {
				channel.pipeline().addLast(new IdleStateHandler(90, 90, 0));
				if (decoder != null) {
					channel.pipeline().addLast(decoder.getClass().newInstance());
				}
				if (encoder != null) {
					channel.pipeline().addLast(encoder.getClass().newInstance());
				}
				channel.pipeline().addLast(new GateNetHandler(new MessageFromClient()));
			}
		});
		
		boot.option(ChannelOption.SO_BACKLOG, 128);
		boot.childOption(ChannelOption.SO_KEEPALIVE, true);
		try {
			boot.bind().sync().channel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return this.port;
	}

	public void setDecoder(ByteToMessageDecoder decoder) {
		this.decoder = decoder;
	}

	public void setEncoder(MessageToByteEncoder<CocoPacket> encoder) {
		this.encoder = encoder;
	}
	
	public static void main(String[] ar) {
		GateNetServer server = new GateNetServer(10003);
		server.setDecoder(new GateDecoder());
		server.start();
	}
}
