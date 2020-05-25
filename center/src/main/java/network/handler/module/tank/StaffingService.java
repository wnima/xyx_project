package network.handler.module.tank;

import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.GamePb.GetStaffingRs;
import protocol.s2c.ResponseCode;
import util.MsgHelper;
import util.WarHelper;

@Singleton
public class StaffingService implements IModuleMessageHandler {

	public static StaffingService getInst() {
		return BeanManager.getBean(StaffingService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	public void getStaffing(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		if (!WarHelper.isStaffingOpen()) {
			// 屏蔽掉返回错误码
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
			return;
		}

//		int ranking = rankDataManager.getPlayerRank(9, playerId);

		// Player player = playerDataManager.getPlayer(handler.getRoleId());

		GetStaffingRs.Builder builder = GetStaffingRs.newBuilder();
		builder.setRanking(1);
//		builder.setWorldLv(staffingDataManager.getWorldLv());
		builder.setWorldLv(1);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetStaffingRs, builder.build());
	}

	public void recalcWorldLv() {
//		StaffingDataManager.reCalcWorldLv();
	}
}
