package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfPart;
import config.bean.ConfPartRefit;
import config.bean.ConfPartUp;
import config.bean.ConfVip;
import config.provider.ConfPartProvider;
import config.provider.ConfPartRefitProvider;
import config.provider.ConfPartUpProvider;
import config.provider.ConfVipProvider;
import data.bean.Chip;
import data.bean.Lord;
import data.bean.Part;
import data.bean.PartResolve;
import data.bean.Resource;
import define.AwardType;
import define.ItemFrom;
import define.SysChatId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.ActivityPb.LockPartRq;
import pb.ActivityPb.LockPartRs;
import pb.GamePb.CombinePartRq;
import pb.GamePb.CombinePartRs;
import pb.GamePb.ExplodeChipRq;
import pb.GamePb.ExplodeChipRs;
import pb.GamePb.ExplodePartRq;
import pb.GamePb.ExplodePartRs;
import pb.GamePb.GetChipRs;
import pb.GamePb.GetPartRs;
import pb.GamePb.OnPartRq;
import pb.GamePb.OnPartRs;
import pb.GamePb.RefitPartRq;
import pb.GamePb.RefitPartRs;
import pb.GamePb.UpPartRq;
import pb.GamePb.UpPartRs;
import protocol.s2c.ResponseCode;
import util.MsgHelper;

@Singleton
public class PartService implements IModuleMessageHandler {

	public static PartService getInst() {
		return BeanManager.getBean(PartService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: getPart
	 * 
	 * @Description: 客户端获取配件数据 @param handler @return void @throws
	 */
	public void getPart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		GetPartRs.Builder builder = GetPartRs.newBuilder();
		for (int i = 0; i < 5; i++) {
			Map<Integer, Part> map = player.parts.get(i);
			Iterator<Part> it = map.values().iterator();
			while (it.hasNext()) {
				builder.addPart(PbHelper.createPartPb(it.next()));
			}
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartRs, builder.build());
	}

	/**
	 * 
	 * Method: getChip
	 * 
	 * @Description: 客户端获取碎片数据 @param handler @return void @throws
	 */
	public void getChip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		GetChipRs.Builder builder = GetChipRs.newBuilder();

		Iterator<Chip> it = player.chips.values().iterator();
		while (it.hasNext()) {
			builder.addChip(PbHelper.createChipPb(it.next()));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetChipRs, builder.build());
	}

	// private Part createPart(Player player, int partId, int upLv, int refitLv,
	// int pos) {
	// Part part = new Part(player.maxKey++, partId, upLv, refitLv, pos);
	// return part;
	// }

	/**
	 * 
	 * Method: combinePart
	 * 
	 * @Description: 合成配件 @param req @param handler @return void @throws
	 */
	public void combinePart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		CombinePartRq req = msg.get();
		int partId = req.getPartId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		ConfPart staticPart = ConfPartProvider.getInst().getConfigById(partId);
		if (staticPart == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Chip chip = player.chips.get(partId);
		Chip whatChip = player.chips.get(901);
		int chipCount = 0;
		int whatCount = 0;
		if (chip != null) {
			chipCount = chip.getCount();
		}

		if (whatChip != null) {
			whatCount = whatChip.getCount();
		}

		if (chipCount + whatCount < staticPart.getChipCount()) {
//			handler.sendErrorMsgToPlayer(GameError.CHIP_NOT_ENOUGH);
			return;
		}

		if (chip != null) {
			if (chipCount >= staticPart.getChipCount()) {
				chip.setCount(chipCount - staticPart.getChipCount());
			} else {
				int count = staticPart.getChipCount() - chipCount;
				chip.setCount(0);
				whatChip.setCount(whatCount - (staticPart.getChipCount() - chipCount));
				LogHelper.logItem(player.lord, ItemFrom.COMBINE_PART, player.account.getServerId(), 9, 901, -count, String.valueOf(partId));
			}
		}

		Part part = PlayerDataManager.getInst().addPart(player, partId, 0);

		CombinePartRs.Builder builder = CombinePartRs.newBuilder();
		builder.setPart(PbHelper.createPartPb(part));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombinePartRs, builder.build());
	}

	/**
	 * 
	 * Method: explodePart
	 * 
	 * @Description: 分解配件 @param req @param handler @return void @throws
	 */
	public void explodePart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ExplodePartRq req = msg.get();
		int keyId = 0;
		// int quality = 0;
		if (req.hasField(ExplodePartRq.getDescriptor().findFieldByName("keyId"))) {
			keyId = req.getKeyId();
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		int stoneAdd = 0;
		int fittingAdd = 0;
		int planAdd = 0;
		int mineralAdd = 0;
		int toolAdd = 0;

		List<PartResolve> resolveList = new ArrayList<PartResolve>();
		if (keyId != 0) {// 分解单个
			Map<Integer, Part> store = player.parts.get(0);
			Part part = store.get(keyId);
			if (part == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_PART);
				return;
			}

			if (part.isLocked()) {
//				handler.sendErrorMsgToPlayer(GameError.PART_LOCKED);
				return;
			}

			ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
			if (staticPart == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			ConfPartUp staticPartUp = ConfPartUpProvider.getInst().getStaticPartUp(part.getPartId(), part.getUpLv());
			if (staticPartUp == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			ConfPartRefit staticPartRefit = ConfPartRefitProvider.getInst().getStaticPartRefit(staticPart.getQuality(), part.getRefitLv());
			if (staticPartRefit == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			store.remove(keyId);

			stoneAdd = staticPartUp.getStoneExplode();
			fittingAdd = staticPartRefit.getFittingExplode();
			planAdd = staticPartRefit.getPlanExplode();
			mineralAdd = staticPartRefit.getMineralExplode();
			toolAdd = staticPartRefit.getToolExplode();
			Lord lord = player.lord;
			PlayerDataManager.getInst().modifyStone(player.resource, stoneAdd);
			PlayerDataManager.getInst().modifyFitting(lord, fittingAdd);
			PlayerDataManager.getInst().modifyPlan(lord, planAdd);
			PlayerDataManager.getInst().modifyMineral(lord, mineralAdd);
			PlayerDataManager.getInst().modifyTool(lord, toolAdd);

			ExplodePartRs.Builder builder = ExplodePartRs.newBuilder();
			builder.setFitting(lord.getFitting());
			builder.setPlan(lord.getPlan());
			builder.setMineral(lord.getMineral());
			builder.setTool(lord.getTool());
			builder.setStone(player.resource.getStone());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.ExplodePartRs, builder.build());

			resolveList.add(new PartResolve(AwardType.PART, staticPart.getQuality(), 1));
//			activityDataManager.partResolve(player, resolveList);
			int pos = staticPart.getPartId() / 100;
			int quality = staticPart.getQuality();
			if (pos <= 4 && quality >= 3) {
				LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
			} else if (pos > 4 && pos <= 6 && quality >= 2) {
				LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
			} else if (pos > 6) {
				LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
			}
		} else {// 批量分解
			List<Integer> qualites = req.getQualityList();
			Map<Integer, Part> store = player.parts.get(0);
			Iterator<Part> it = store.values().iterator();
			while (it.hasNext()) {
				Part part = (Part) it.next();
				if (part.isLocked()) {
					continue;
				}
				ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
				if (staticPart == null) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}

				if (qualites.contains(staticPart.getQuality())) {
					ConfPartUp staticPartUp = ConfPartUpProvider.getInst().getStaticPartUp(part.getPartId(), part.getUpLv());
					if (staticPartUp == null) {
						continue;
					}

					ConfPartRefit staticPartRefit = ConfPartRefitProvider.getInst().getStaticPartRefit(staticPart.getQuality(), part.getRefitLv());
					if (staticPartRefit == null) {
						continue;
					}

					stoneAdd += staticPartUp.getStoneExplode();
					fittingAdd += staticPartRefit.getFittingExplode();
					planAdd += staticPartRefit.getPlanExplode();
					mineralAdd += staticPartRefit.getMineralExplode();
					toolAdd += staticPartRefit.getToolExplode();

					resolveList.add(new PartResolve(AwardType.PART, staticPart.getQuality(), 1));

					it.remove();
					int pos = staticPart.getPartId() / 100;
					if (pos <= 4 && staticPart.getQuality() >= 3) {
						LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
					} else if (pos > 4 && pos <= 6 && staticPart.getQuality() >= 2) {
						LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
					} else if (pos > 6) {
						LogHelper.logItem(player.lord, ItemFrom.RESOLVE_PART, player.account.getServerId(), AwardType.PART, part.getPartId(), -1, String.valueOf(keyId));
					}
				}
			}

			Lord lord = player.lord;
			PlayerDataManager.getInst().modifyStone(player.resource, stoneAdd);
			PlayerDataManager.getInst().modifyFitting(lord, fittingAdd);
			PlayerDataManager.getInst().modifyPlan(lord, planAdd);
			PlayerDataManager.getInst().modifyMineral(lord, mineralAdd);
			PlayerDataManager.getInst().modifyTool(lord, toolAdd);

//			activityDataManager.partResolve(player, resolveList);

			TaskManager.getInst().updTask(player, TaskType.COND_PART_EPR, 1, null);

			ExplodePartRs.Builder builder = ExplodePartRs.newBuilder();
			builder.setFitting(lord.getFitting());
			builder.setPlan(lord.getPlan());
			builder.setMineral(lord.getMineral());
			builder.setTool(lord.getTool());
			builder.setStone(player.resource.getStone());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.ExplodePartRs, builder.build());
		}

	}

	private boolean canOnPart(int lv, int index) {
		switch (index) {
		case 1:
		case 2:
			if (lv < 18) {
				return false;
			}
			break;
		case 3:
			if (lv < 20) {
				return false;
			}
			break;
		case 4:
			if (lv < 25) {
				return false;
			}
			break;
		case 5:
			if (lv < 30) {
				return false;
			}
			break;
		case 6:
			if (lv < 40) {
				return false;
			}
			break;
		case 7:
			if (lv < 55) {
				return false;
			}
			break;
		case 8:
			if (lv < 60) {
				return false;
			}
			break;
		default:
			return false;
		}

		return true;
	}

	/**
	 * 
	 * Method: onPart
	 * 
	 * @Description: 穿戴，卸下配件 @param req @param handler @return void @throws
	 */
	public void onPart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		OnPartRq req = msg.get();
		int keyId = req.getKeyId();
		int pos = 0;
		if (req.hasField(ExplodePartRq.getDescriptor().findFieldByName("pos"))) {
			pos = req.getPos();
		}

		if (pos < 0 || pos > 4) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Part> map = player.parts.get(pos);
		if (map == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Part part = map.get(keyId);
		if (part == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PART);
			return;
		}

		ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
		if (staticPart == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (pos == 0) {// 穿上
			int toPos = staticPart.getType();
			Map<Integer, Part> toMap = player.parts.get(toPos);
			if (toMap.size() >= 8) {
//				handler.sendErrorMsgToPlayer(GameError.FULL_PART_ON);
				return;
			}

			int index = part.getPartId() / 100;
			if (!canOnPart(player.lord.getLevel(), index)) {
//				handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
				return;
			}

			Iterator<Part> it = toMap.values().iterator();
			while (it.hasNext()) {
				if (it.next().getPartId() / 100 == index) {
//					handler.sendErrorMsgToPlayer(GameError.ALRADY_EQUIP);
					return;
				}
			}
			part.setPos(toPos);
			map.remove(keyId);
			toMap.put(keyId, part);
		} else {// 卸下
			Map<Integer, Part> storeMap = player.parts.get(0);
			if (storeMap.size() >= PlayerDataManager.PART_STORE_LIMIT) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_PART_STORE);
				return;
			}

			part.setPos(0);
			map.remove(keyId);
			player.parts.get(0).put(keyId, part);
		}

		PlayerDataManager.getInst().updateFight(player);

		OnPartRs.Builder builder = OnPartRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.OnPartRs, builder.build());
	}

	/**
	 * 
	 * Method: explodeChip
	 * 
	 * @Description: 分解碎片 @param req @param handler @return void @throws
	 */
	public void explodeChip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ExplodeChipRq req = msg.get();
		int chipId = 0;
		if (req.hasField(ExplodePartRq.getDescriptor().findFieldByName("chipId"))) {
			chipId = req.getChipId();
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		List<PartResolve> chipList = new ArrayList<PartResolve>();
		if (chipId != 0) {
			int count = req.getCount();
			if (count < 0) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			Chip chip = player.chips.get(chipId);
			if (chip == null || chip.getCount() < count) {
//				handler.sendErrorMsgToPlayer(GameError.CHIP_NOT_ENOUGH);
				return;
			}

			ConfPart staticPart = ConfPartProvider.getInst().getConfigById(chipId);
			if (staticPart == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			int fitting = 0;
			if (staticPart.getQuality() == 2) {// 蓝色
				fitting = 100 * count;
			} else if (staticPart.getQuality() == 3) {// 紫色
				fitting = 200 * count;
			} else if (staticPart.getQuality() == 4) {
				fitting = 1000 * count;
			}

			chip.setCount(chip.getCount() - count);

			PlayerDataManager.getInst().modifyFitting(player.lord, fitting);
			ExplodeChipRs.Builder builder = ExplodeChipRs.newBuilder();
			builder.setFitting(player.lord.getFitting());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.ExplodeChipRs, builder.build());

			chipList.add(new PartResolve(AwardType.CHIP, staticPart.getQuality(), count));
//			activityDataManager.partResolve(player, chipList);
		} else {
			List<Integer> qualites = req.getQualityList();
			int fitting = 0;

			Map<Integer, Chip> chips = player.chips;
			Iterator<Chip> it = chips.values().iterator();
			while (it.hasNext()) {
				Chip chip = (Chip) it.next();
				ConfPart staticPart = ConfPartProvider.getInst().getConfigById(chip.getChipId());
				if (staticPart == null) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}

				int count = chip.getCount();
				if (qualites.contains(staticPart.getQuality())) {

					if (staticPart.getQuality() == 2) {// 蓝色
						fitting += 100 * count;
					} else if (staticPart.getQuality() == 3) {// 紫色
						fitting += 200 * count;
					} else if (staticPart.getQuality() == 4) {
						fitting += 1000 * count;
					}
					chipList.add(new PartResolve(AwardType.CHIP, staticPart.getQuality(), count));
					it.remove();
				}
			}
//			activityDataManager.partResolve(player, chipList);
			PlayerDataManager.getInst().modifyFitting(player.lord, fitting);
			ExplodeChipRs.Builder builder = ExplodeChipRs.newBuilder();
			builder.setFitting(player.lord.getFitting());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.ExplodeChipRs, builder.build());
		}

	}

	/**
	 * 
	 * Method: upPart
	 * 
	 * @Description: 强化配件 @param req @param handler @return void @throws
	 */
	public void upPart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UpPartRq req = msg.get();
		int keyId = req.getKeyId();
		int pos = req.getPos();
		int metal = req.getMetal();
		if (pos < 0 || pos > 4 || metal < 0 || metal > 10) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Part> map = player.parts.get(pos);
		if (map == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Part part = map.get(keyId);
		if (part == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PART);
			return;
		}

		if (part.getUpLv() >= player.lord.getLevel()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		if (part.getUpLv() >= PlayerDataManager.MAX_PART_UP_LV) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_PART_UP_LV);
			return;
		}

		ConfPartUp staticPartUp = ConfPartUpProvider.getInst().getStaticPartUp(part.getPartId(), part.getUpLv() + 1);
		if (staticPartUp == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		Resource resource = player.resource;
		if (resource.getStone() < staticPartUp.getStone()) {
//			handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
			return;
		}

		if (metal != 0 && player.lord.getMetal() < metal) {
//			handler.sendErrorMsgToPlayer(GameError.METAL_NOT_ENOUGH);
			return;
		}
		boolean success = false;
		int prob = staticPartUp.getProb() + 50 * metal;
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			prob += (prob * staticVip.getPartProb() / 100.0f);
		}

		prob = (prob > 1000) ? 1000 : prob;
		if (RandomHelper.isHitRangeIn1000(prob)) {
			success = true;
		}

		long costStone = staticPartUp.getStone();

		if (success) {
			part.setUpLv(part.getUpLv() + 1);
		} else {
//			float a = activityDataManager.discountActivity(ActivityConst.ACT_PART_EVOLVE, 0);
//			costStone *= a / 100f;
		}

		PlayerDataManager.getInst().modifyMetal(player.lord, -metal);
		PlayerDataManager.getInst().modifyStone(player.resource, -costStone);

		if (pos != 0) {
			PlayerDataManager.getInst().updateFight(player);
		}

		TaskManager.getInst().updTask(player, TaskType.COND_UP_PART, 1, null);
		UpPartRs.Builder builder = UpPartRs.newBuilder();
		builder.setSuccess(success);
		builder.setStone(player.resource.getStone());
		builder.setMetal(player.lord.getMetal());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpPartRs, builder.build());

		if (success) {// 配件强化
			ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
			if (staticPart != null && staticPart.getQuality() >= 2) {
				if (part.getUpLv() == 20) {
					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_UPGRADE, player.lord.getNick(), String.valueOf(part.getPartId()), "20"));
				} else if (part.getUpLv() == 40) {
					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_UPGRADE, player.lord.getNick(), String.valueOf(part.getPartId()), "40"));
				} else if (part.getUpLv() == 60) {
					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_UPGRADE, player.lord.getNick(), String.valueOf(part.getPartId()), "60"));
				} else if (part.getUpLv() == 80) {
					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_UPGRADE, player.lord.getNick(), String.valueOf(part.getPartId()), "80"));
				}
			}
		}
	}

	/**
	 * 
	 * Method: refitPart
	 * 
	 * @Description: 改造配件 @param req @param handler @return void @throws
	 */
	public void refitPart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		RefitPartRq req = msg.get();
		int keyId = req.getKeyId();
		int pos = req.getPos();
		boolean draw = req.getDraw();

		if (pos < 0 || pos > 4) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Part> map = player.parts.get(pos);
		if (map == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Part part = map.get(keyId);
		if (part == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PART);
			return;
		}

		if (part.getRefitLv() >= PlayerDataManager.MAX_PART_REFIT_LV) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_PART_REFIT_LV);
			return;
		}

		ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
		if (staticPart == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		ConfPartRefit staticPartRefit = ConfPartRefitProvider.getInst().getStaticPartRefit(staticPart.getQuality(), part.getRefitLv() + 1);
		if (staticPartRefit == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Lord lord = player.lord;
		int fittingCost = staticPartRefit.getFitting();
		int planCost = staticPartRefit.getPlan();
		int mineralCost = staticPartRefit.getMineral();
		int toolCost = staticPartRefit.getTool();
		if (lord.getFitting() < fittingCost || lord.getPlan() < planCost || lord.getMineral() < mineralCost || lord.getTool() < toolCost) {
//			handler.sendErrorMsgToPlayer(GameError.INGREDIENT_NOT_ENOUGH);
			return;
		}

		if (draw && lord.getDraw() < 5) {
//			handler.sendErrorMsgToPlayer(GameError.DRAW_NOT_ENOUGH);
			return;
		}

		PlayerDataManager.getInst().modifyFitting(lord, -fittingCost);
		PlayerDataManager.getInst().modifyPlan(lord, -planCost);
		PlayerDataManager.getInst().modifyMineral(lord, -mineralCost);
		PlayerDataManager.getInst().modifyTool(lord, -toolCost);
		if (draw) {
			PlayerDataManager.getInst().modifyDraw(lord, -5);
		}

		boolean flag = false;
//		boolean flag = activityDataManager.partEvolve();// 配件进化活动开启之后配件改造不降级

		if (!draw && !flag) {
			int upLv = part.getUpLv() - 3;
			upLv = (upLv > 0) ? upLv : 0;
			part.setUpLv(upLv);
		}

		part.setRefitLv(part.getRefitLv() + 1);
		// partDao.updateLv(part);

		if (pos != 0) {
			PlayerDataManager.getInst().updateFight(player);
		}

		RefitPartRs.Builder builder = RefitPartRs.newBuilder();
		builder.setUpLv(part.getUpLv());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.RefitPartRs, builder.build());

		if (part.getRefitLv() == 1) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_REFIT, player.lord.getNick(), String.valueOf(part.getPartId()), "1"));
		} else if (part.getRefitLv() == 2) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_REFIT, player.lord.getNick(), String.valueOf(part.getPartId()), "2"));
		} else if (part.getRefitLv() == 3) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_REFIT, player.lord.getNick(), String.valueOf(part.getPartId()), "3"));
		} else if (part.getRefitLv() == 4) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PART_REFIT, player.lord.getNick(), String.valueOf(part.getPartId()), "4"));
		}
	}

	/**
	 * 锁定配件
	 * 
	 * @param req
	 * @param handler
	 */
	public void lockPart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		LockPartRq req = msg.get();
		int keyId = req.getKeyId();
		int pos = req.getPos();
		boolean locked = req.getLocked();

		if (pos < 0 || pos > 4) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Part> map = player.parts.get(pos);
		if (map == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Part part = map.get(keyId);
		if (part == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PART);
			return;
		}

		ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
		if (staticPart == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		part.setLocked(locked);

		LockPartRs.Builder builder = LockPartRs.newBuilder();
		builder.setResult(true);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.LockPartRs, builder.build());
	}
}
