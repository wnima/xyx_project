package network.handler.module;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import define.AppId;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import network.ServerManager;
import network.ServerSession;
import packet.CocoPacket;
import pb.util.PBStringList;
import protocol.c2s.RequestCode;
import service.CenterServer;
import util.NettyUtil;
import util.PBUtilHelper;

@Singleton
public class CenterModule implements IModuleMessageHandler {

	public static CenterModule getInst() {
		return BeanManager.getBean(CenterModule.class);
	}

	private static final Logger logger = LoggerFactory.getLogger(CenterModule.class);

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
		handler.registerAction(RequestCode.CENTER_REGISTER_SERVER.getValue(), this::actionRegisterServer, PBStringList.getDefaultInstance());
		handler.registerAction(RequestCode.CENTER_SERVER_PING.getValue(), this::actionServerPing);
		handler.registerAction(RequestCode.CENTER_GATE_FACTOR.getValue(), this::sysnGateFactor);

	}

	private void sysnGateFactor(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		if (packet.getPlayerId() != 1) {
			return;
		}
		ServerSession serverSession = NettyUtil.getAttribute(ioSession, ServerSession.KEY);
		if (serverSession != null) {
		}
	}

	private void actionRegisterServer(ChannelHandlerContext ioSession, CocoPacket srcPacket, AbstractHandlers.MessageHolder<MessageLite> message) {
		PBStringList req = message.get();
		ServerSession session = NettyUtil.getAttribute(ioSession, ServerSession.KEY);

		int paramAppId = Integer.valueOf(req.getList(0));
		AppId appId = AppId.getByValue(paramAppId);
		if (appId == null) {
			logger.warn(" the app id is null and the id is {}", paramAppId);
			return;
		}
		session.setAppId(appId);
		if (appId == AppId.LOGIC) {
			CenterServer.getInst().setLogicRegist(true);
		}

		logger.info("actionRegisterServer app:{} params:{}", AppId.getByValue(paramAppId), req.getListList());

		long centerStartTime = CenterServer.getInst().getStartTime();
		ServerManager.getInst().registerServer(centerStartTime, session, req.getListList());
		int dynamicId = session.getServerId();

		List<String> list = new ArrayList<>();
		list.add(dynamicId + "");
		list.add(centerStartTime + "");
		CocoPacket packet = new CocoPacket(RequestCode.CENTER_REGISTER_SERVER.getValue(), PBUtilHelper.pbStringList(list));
		packet.setSeqId(srcPacket.getSeqId());
		ioSession.writeAndFlush(packet);
	}

	private void actionServerPing(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		ServerSession session = NettyUtil.getAttribute(ioSession, ServerSession.KEY);
		if (session == null) {
			logger.info(" server ping  and session is null");
			return;
		}
	}
}
