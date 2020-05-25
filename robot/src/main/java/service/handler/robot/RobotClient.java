package service.handler.robot;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bean.Robot;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import logic.RobotManager;
import service.handler.codec.RobotDecoder;
import service.handler.codec.RobotEncoder;

public class RobotClient {

	private static Logger logger = LoggerFactory.getLogger(RobotClient.class);

	private Robot robot;
	private String host;
	private int port;
	private Bootstrap boot;

	public static final int RECONNECT_SECOND = 3;

	public RobotClient(Robot robot) {
		this.robot = robot;
		this.host = robot.host;
		this.port = robot.port;
		this.init();
	}

	public void init() {
		boot = new Bootstrap();
		boot.group(RobotManager.getInst().getEventGroup());
		boot.channel(NioSocketChannel.class);
		boot.option(ChannelOption.SO_KEEPALIVE, true);
		boot.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new RobotDecoder());
				socketChannel.pipeline().addLast(new RobotEncoder());
				socketChannel.pipeline().addLast(new RobotHandler(robot));
			}
		});
	}

	public void doConnect() {
		ChannelFuture future = boot.connect(host, port);
		future.addListener(e -> {
			if (!e.isSuccess()) {
				logger.info(" connect to the server un success and reconnect {} sec later ", RECONNECT_SECOND);
				future.channel().eventLoop().schedule(() -> doConnect(), RECONNECT_SECOND, TimeUnit.SECONDS);
			} else {
				logger.info("connect to server{}：{} success  ", this.host, this.port);
			}
		});
	}
}
