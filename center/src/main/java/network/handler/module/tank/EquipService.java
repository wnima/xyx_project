package network.handler.module.tank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.game.domain.Player;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfEquip;
import config.bean.ConfEquipLv;
import config.provider.ConfEquipLvProvider;
import config.provider.ConfEquipProvider;
import data.bean.Equip;
import data.bean.Lord;
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
import pb.GamePb.AllEquipRq;
import pb.GamePb.AllEquipRs;
import pb.GamePb.GetEquipRs;
import pb.GamePb.OnEquipRq;
import pb.GamePb.OnEquipRs;
import pb.GamePb.SellEquipRq;
import pb.GamePb.SellEquipRs;
import pb.GamePb.UpCapacityRs;
import pb.GamePb.UpEquipRq;
import pb.GamePb.UpEquipRs;
import protocol.s2c.ResponseCode;
import util.MsgHelper;

@Singleton
public class EquipService implements IModuleMessageHandler {

	public static EquipService getInst() {
		return BeanManager.getBean(EquipService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: getEquip
	 * 
	 * @Description: 客户端获取装备数据 @param handler @return void @throws
	 */
	public void getEquip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		GetEquipRs.Builder builder = GetEquipRs.newBuilder();
		for (int i = 0; i < 7; i++) {
			Map<Integer, Equip> equipMap = player.equips.get(i);
			Iterator<Equip> it = equipMap.values().iterator();
			while (it.hasNext()) {
				builder.addEquip(PbHelper.createEquipPb(it.next()));
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: sellEquip
	 * 
	 * @Description: 出售装备 @param req @param handler @return void @throws
	 */
	public void sellEquip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SellEquipRq req = msg.get();
		List<Integer> list = req.getKeyIdList();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		Map<Integer, Equip> store = player.equips.get(0);

		if (list.isEmpty()) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		for (Integer keyId : list) {
			if (!store.containsKey(keyId)) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
				return;
			}
		}

		int serverId = player.account.getServerId();

		int stoneAdd = 0;
		for (Integer keyId : list) {
			Equip equip = store.get(keyId);
			ConfEquip staticEquip = ConfEquipProvider.getInst().getConfigById(equip.getEquipId());
			if (staticEquip == null) {
				continue;
			}

			store.remove(keyId);
			stoneAdd += staticEquip.getPrice();
			if (staticEquip.getQuality() >= 3 && equip.getEquipId() < 700) {
				LogHelper.logItem(lord, ItemFrom.SALE_EQUIP, serverId, AwardType.EQUIP, equip.getEquipId(), -1, String.valueOf(keyId));
			}
		}

		PlayerDataManager.getInst().modifyStone(player.resource, stoneAdd);

		SellEquipRs.Builder builder = SellEquipRs.newBuilder();
		builder.setStone(player.resource.getStone());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SellEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: upEquip
	 * 
	 * @Description: 玩家升级装备 @param req @param handler @return void @throws
	 */
	public void upEquip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UpEquipRq req = msg.get();
		int keyId = req.getKeyId();
		int pos = req.getPos();
		if (pos < 0 || pos > 6) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		Map<Integer, Equip> equips = player.equips.get(pos);

		Equip to = equips.get(keyId);
		if (to == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
			return;
		}

		if (to.getLv() >= PlayerDataManager.MAX_EQUIP_LV) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_LV);
			return;
		}

		if (to.getLv() >= player.lord.getLevel()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		if (to.getEquipId() >= 701) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfEquip staticToEquip = ConfEquipProvider.getInst().getConfigById(to.getEquipId());
		if (staticToEquip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		List<Integer> from = req.getFromList();
		List<Equip> list = new ArrayList<>();
		Map<Integer, Equip> store = player.equips.get(0);
		boolean find = true;
		for (Integer key : from) {
			Equip equip = store.get(key);
			if (equip == null) {
				find = false;
				break;
			}
			list.add(equip);
		}

		if (!find) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
			return;
		}

		int serverId = player.account.getServerId();

		int addExp = 0;
		int cardExp = 0;
		for (Equip equip : list) {
			ConfEquip staticEquip = ConfEquipProvider.getInst().getConfigById(equip.getEquipId());
			if (equip.getEquipId() < 701) {
				ConfEquipLv staticEquipLv = ConfEquipLvProvider.getInst().getConfEquipLv(staticEquip.getQuality(), equip.getLv());
				addExp += staticEquipLv.getGiveExp();
				if (staticEquip.getQuality() > 3) {
					LogHelper.logItem(lord, ItemFrom.EAT_EQUIP, serverId, AwardType.EQUIP, equip.getEquipId(), -1, String.valueOf(keyId));
				}
			} else {
				cardExp += staticEquip.getA();
			}

			store.remove(equip.getKeyId());
		}

//		cardExp = activityDataManager.upEquipExp(cardExp);
		addExp += cardExp;

		int lv = to.getLv();
		if (ConfEquipProvider.getInst().addEquipExp(to, addExp) && pos != 0) {
			PlayerDataManager.getInst().updateFight(player);
		}

		TaskManager.getInst().updTask(player, TaskType.COND_EQUIP_LV_UP, 1, null);
		if (lv != to.getLv()) {
//			activityDataManager.purpleEquipUp(player, staticToEquip, lv, to.getLv());
		}

		UpEquipRs.Builder builder = UpEquipRs.newBuilder();
		builder.setLv(to.getLv());
		builder.setExp(to.getExp());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpEquipRs, builder.build());

		if (staticToEquip.getQuality() == 4 && (lv != to.getLv())) {// 紫装升级
			if (to.getLv() == 20) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PURPLE_EQUIP, player.lord.getNick(), String.valueOf(staticToEquip.getEquipId()), "20"));
			} else if (to.getLv() == 40) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PURPLE_EQUIP, player.lord.getNick(), String.valueOf(staticToEquip.getEquipId()), "40"));
			} else if (to.getLv() == 60) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PURPLE_EQUIP, player.lord.getNick(), String.valueOf(staticToEquip.getEquipId()), "60"));
			} else if (to.getLv() == 80) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PURPLE_EQUIP, player.lord.getNick(), String.valueOf(staticToEquip.getEquipId()), "80"));
			}
		}
	}

	/**
	 * 
	 * Method: onEquip
	 * 
	 * @Description: 穿戴装备 @param req @param handler @return void @throws
	 */
	public void onEquip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		OnEquipRq req = msg.get();
		int from = req.getFrom();
		int fromPos = req.getFromPos();
		int toPos = req.getToPos();
		int to = 0;
		if (req.hasField(OnEquipRq.getDescriptor().findFieldByName("to"))) {
			to = req.getTo();
		}

		if (fromPos < 0 || fromPos > 6) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (toPos < 0 || toPos > 6) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (toPos == 0) {// 卸下
			offEquip(from, fromPos, ctx, packet, msg);
		} else if ((to != 0) && (from != 0)) {// 单个替换
			replaceEquip(from, fromPos, to, toPos, ctx, packet, msg);
		} else if (to == 0 && from == 0) {// 部队互换
			exchangeEquip(fromPos, toPos, ctx, packet, msg);
		} else {// 装备到空位子
			onEquip(from, fromPos, toPos, ctx, packet, msg);
		}
	}

	/**
	 * 
	 * Method: allEquip
	 * 
	 * @Description: 一键装备、卸下 @param req @param handler @return void @throws
	 */
	public void allEquip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		AllEquipRq req = msg.get();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Equip> store = player.equips.get(0);
		List<Integer> onList = req.getOnList();
		List<Integer> offList = req.getOffList();
		int pos = req.getPos();
		if (pos < 0 || pos > 6 || onList.size() > 6) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		int storeCount = offList.size() - onList.size();
		if (storeCount > player.lord.getEquip() - store.size()) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
			return;
		}

		Set<Integer> set = new HashSet<>();
		for (Integer on : onList) {
			Equip equip = store.get(on);
			if (equip == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
				return;
			}

			int index = equip.getEquipId() / 100;
			if (set.contains(index) || index == 7) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			set.add(index);
		}

		Map<Integer, Equip> slot = player.equips.get(pos);
		for (Integer off : offList) {
			if (!slot.containsKey(off)) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
				return;
			}
		}

		for (Integer off : offList) {
			Equip equip = slot.get(off);
			slot.remove(off);
			equip.setPos(0);
			store.put(off, equip);
		}

		for (Integer on : onList) {
			Equip equip = store.get(on);
			store.remove(on);
			equip.setPos(pos);
			slot.put(on, equip);
		}

		PlayerDataManager.getInst().updateFight(player);

		AllEquipRs.Builder builder = AllEquipRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.AllEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: onEquip
	 * 
	 * @Description: 穿戴装备 @param keyId @param pos @param handler @return
	 *               void @throws
	 */
	private void onEquip(int from, int fromPos, int toPos, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		// Map<Integer, Equip> store = player.equips.get(0);
		Map<Integer, Equip> fromSlot = player.equips.get(fromPos);
		Map<Integer, Equip> toSlot = player.equips.get(toPos);
		if (toSlot.size() == 6) {
//			handler.sendErrorMsgToPlayer(GameError.FULL_EQUIP_ON);
			return;
		}

		Equip equip = fromSlot.get(from);
		if (equip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
			return;
		}

		ConfEquip staticEquip = ConfEquipProvider.getInst().getConfigById(equip.getEquipId());
		if (staticEquip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		int index = equip.getEquipId() / 100;
		if (index == 7) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Iterator<Equip> it = toSlot.values().iterator();
		while (it.hasNext()) {
			Equip e = it.next();
			if ((e.getEquipId() / 100) == index) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}
		}

		fromSlot.remove(equip.getKeyId());
		equip.setPos(toPos);
		toSlot.put(equip.getKeyId(), equip);

		PlayerDataManager.getInst().updateFight(player);

		OnEquipRs.Builder builder = OnEquipRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.OnEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: replaceEquip
	 * 
	 * @Description: 替换装备 @param from @param fromPos @param to @param toPos @param
	 *               handler @return void @throws
	 */
	private void replaceEquip(int from, int fromPos, int to, int toPos, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Equip> fromSlot = player.equips.get(fromPos);
		Map<Integer, Equip> toSlot = player.equips.get(toPos);
		if (fromPos == 0 || fromPos == toPos) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Equip fromEquip = fromSlot.get(from);
		if (fromEquip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
			return;
		}

		ConfEquip staticFrom = ConfEquipProvider.getInst().getConfigById(fromEquip.getEquipId());
		if (staticFrom == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Equip toEquip = toSlot.get(to);
		if (toEquip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
			return;
		}

		ConfEquip staticTo = ConfEquipProvider.getInst().getConfigById(toEquip.getEquipId());
		if (staticTo == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticTo.getAttributeId() != staticFrom.getAttributeId()) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		toSlot.remove(toEquip.getKeyId());
		toEquip.setPos(fromPos);
		fromSlot.put(toEquip.getKeyId(), toEquip);

		fromSlot.remove(fromEquip.getKeyId());
		fromEquip.setPos(toPos);
		toSlot.put(fromEquip.getKeyId(), fromEquip);

		PlayerDataManager.getInst().updateFight(player);

		OnEquipRs.Builder builder = OnEquipRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.OnEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: exchangeEquip
	 * 
	 * @Description: 互换部队装备 @param fromPos @param toPos @param handler @return
	 *               void @throws
	 */
	private void exchangeEquip(int fromPos, int toPos, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Equip> fromSlot = player.equips.get(fromPos);
		Map<Integer, Equip> toSlot = player.equips.get(toPos);
		if (fromPos == 0 || fromPos == toPos) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Map<Integer, Equip> fromMap = new HashMap<Integer, Equip>();
		fromMap.putAll(fromSlot);

		Map<Integer, Equip> toMap = new HashMap<Integer, Equip>();
		toMap.putAll(toSlot);

		Iterator<Equip> fromIt = fromSlot.values().iterator();
		while (fromIt.hasNext()) {
			Equip equip = (Equip) fromIt.next();
			equip.setPos(toPos);
			fromIt.remove();
		}

		Iterator<Equip> toIt = toSlot.values().iterator();
		while (toIt.hasNext()) {
			Equip equip = (Equip) toIt.next();
			equip.setPos(fromPos);
			toIt.remove();
		}

		toSlot.putAll(fromMap);
		fromSlot.putAll(toMap);

		PlayerDataManager.getInst().updateFight(player);

		OnEquipRs.Builder builder = OnEquipRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.OnEquipRs, builder.build());
	}

	/**
	 * 
	 * Method: offEquip
	 * 
	 * @Description: 卸下装备 @param keyId @param handler @return void @throws
	 */
	private void offEquip(int keyId, int pos, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Equip> store = player.equips.get(0);
		Map<Integer, Equip> slot = player.equips.get(pos);
		Equip equip = slot.get(keyId);
		if (equip == null || equip.getPos() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (store.size() >= player.lord.getEquip()) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
			return;
		}

		slot.remove(keyId);
		equip.setPos(0);
		store.put(keyId, equip);

		PlayerDataManager.getInst().updateFight(player);

		OnEquipRs.Builder builder = OnEquipRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.OnEquipRs, builder.build());
	}

	private static final int[] UP_CAPACITY_COST = { 10, 10, 20, 20, 30, 30, 40, 40, 50, 50 };

	/**
	 * 
	 * Method: upCapacity
	 * 
	 * @Description: 增加装备仓库容量 @param handler @return void @throws
	 */
	public void upCapacity(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		if (lord.getEquip() >= PlayerDataManager.EQUIP_STORE_LIMIT) {
//			handler.sendErrorMsgToPlayer(GameError.EQUIP_STORE_LIMIT);
			return;
		}

		int cost = UP_CAPACITY_COST[(lord.getEquip() - 100) / 20];
		PlayerDataManager.getInst().addEquipCapacity(lord, cost, 20);
		UpCapacityRs.Builder builder = UpCapacityRs.newBuilder();
		builder.setGold(lord.getGold());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpCapacityRs, builder.build());
	}
}
