package network.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import actor.CenterActorManager;
import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import mxw.PlayerManager;
import mxw.UserData;
import network.AbstractHandlers;
import network.NetClient;
import network.ServerManager;
import network.ServerSession;
import network.handler.module.AccountModule;
import network.handler.module.CenterModule;
import network.handler.module.LobbyModule;
import network.handler.module.MailModule;
import network.handler.module.NoticeModule;
import network.handler.module.mxw.GameMxwModule;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import service.CenterServer;
import util.LogHelper;
import util.Pair;

public class CenterMessageHandler extends AbstractHandlers {
	private static Logger logger = LoggerFactory.getLogger("message");

	@Override
	protected void registerAction() {
		CenterModule.getInst().registerModuleHandler(this);
		LobbyModule.getInst().registerModuleHandler(this);
		GameMxwModule.getInst().registerModuleHandler(this);
		MailModule.getInst().registerModuleHandler(this);
		AccountModule.getInst().registerModuleHandler(this);
		NoticeModule.getInst().registerModuleHandler(this);
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		RequestCode reqCode = packet.getReqCode();
		if (reqCode.getSendTo() != getAppId()&&reqCode.getSendTo()!=AppId.DONE_HERE) {
			if (reqCode.getSendTo() == AppId.LOGIC) {
				UserData player = PlayerManager.getInst().getPlayerById((int) packet.getPlayerId());
				if (player == null) {
					return;
				}
				// 如果玩家在某個有
//				if (player.getDeskInfo() != null) {
//					player.getDeskInfo().writeToLogic(packet);
//				}
			} else if (reqCode.getSendTo() == AppId.GATE) {
//				Player player = PlayerManager.getInstance().getPlayerById((int) packet.getPlayerId());
//				if (player != null) {
//					player.write(packet);
//				}

			} else {
				ServerManager.getInst().getMinLoadSession(reqCode.getSendTo()).sendRequest(packet);
			}
		} else {
			Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
			if (messageAndHandler != null) {
				IActionHandler handler = messageAndHandler.getRight();
				MessageLite protoType = messageAndHandler.getLeft();
				if (handler != null) {
					try {
						final MessageLite message = protoType == null ? null : protoType.getParserForType().parseFrom(packet.getBytes());
						CenterActorManager.getLobbyActor().put(() -> {
							long t1 = System.currentTimeMillis();
							handler.doAction(client, packet, new MessageHolder<>(message));
							long t2 = System.currentTimeMillis();
							if (t2 - t1 > 30) {
								logger.info(" packet id {} cost time {}", packet.getReqId(), (t2 - t1));
							}
							return null;
						});
					} catch (InvalidProtocolBufferException e) {
						LogHelper.ERROR.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	protected AppId getAppId() {
		return AppId.CENTER;
	}

	@Override
	protected NetClient getCenterClient() {
		return CenterServer.getInst().getClient();
	}

	@Override
	public void handleSessionInActive(ServerSession session) {
		logger.info("{}服务器断开连接,serverId={},ip={},port={}", session.getAppId(), session.getServerId(), session.getRemoteAddress(), session.getLocalPort());
	}
}
