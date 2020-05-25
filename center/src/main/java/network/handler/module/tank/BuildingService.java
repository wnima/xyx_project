package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfBuilding;
import config.bean.ConfBuildingLv;
import config.bean.ConfProp;
import config.bean.ConfVip;
import config.provider.ConfBuildingLvProvider;
import config.provider.ConfBuildingProvider;
import config.provider.ConfPropProvider;
import config.provider.ConfVipProvider;
import data.bean.BuildQue;
import data.bean.Building;
import data.bean.Lord;
import data.bean.Mill;
import data.bean.PartyScience;
import data.bean.Prop;
import data.bean.Resource;
import data.bean.Science;
import define.BuildingId;
import define.EffectType;
import define.GoldCost;
import define.ScienceId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.GamePb.BuyAutoBuildRs;
import pb.GamePb.CancelQueRq;
import pb.GamePb.CancelQueRs;
import pb.GamePb.DestroyMillRs;
import pb.GamePb.GetBuildingRs;
import pb.GamePb.SetAutoBuildRq;
import pb.GamePb.SetAutoBuildRs;
import pb.GamePb.SpeedQueRq;
import pb.GamePb.SpeedQueRs;
import pb.GamePb.UpBuildingRq;
import pb.GamePb.UpBuildingRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

class Build {
	public int id;
	public int lv;
	public int pos;

	/**
	 * @param id
	 * @param lv
	 * @param pos
	 */
	public Build(int id, int lv, int pos) {
		super();
		this.id = id;
		this.lv = lv;
		this.pos = pos;
	}
}

class ComparatorBuild implements Comparator<Build> {

	/**
	 * Overriding: compare
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Build o1, Build o2) {
		// TODO Auto-generated method stub
		if (o1.lv < o2.lv)
			return -1;
		else if (o1.lv > o2.lv) {
			return 1;
		} else {
			if (o1.id < o2.id) {
				return -1;
			} else if (o1.id > o2.id) {
				return 1;
			}

			return 0;
		}
	}
}

@Singleton
public class BuildingService implements IModuleMessageHandler {

	public static BuildingService getInst() {
		return BeanManager.getBean(BuildingService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: getBuilding
	 * 
	 * @Description: 客户端获取建筑数据 @param handler @return void @throws
	 */
	public void getBuilding(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GetBuildingRs.Builder builder = GetBuildingRs.newBuilder();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Building building = player.building;
		// List<BuildQue> list = player.buildQue;
		Iterator<BuildQue> it1 = player.buildQue.iterator();
		while (it1.hasNext()) {
			builder.addQueue(PbHelper.createBuildQuePb(it1.next()));
		}

		Iterator<Mill> it2 = player.mills.values().iterator();
		while (it2.hasNext()) {
			builder.addMill(PbHelper.createMillPb(it2.next()));
		}

		builder.setWare1(building.getWare1());
		builder.setWare2(building.getWare2());
		builder.setTech(building.getTech());
		builder.setFactory1(building.getFactory1());
		builder.setFactory2(building.getFactory2());
		builder.setRefit(building.getRefit());
		builder.setCommand(building.getCommand());
		builder.setWorkShop(building.getWorkShop());

		builder.setUpBuildTime(player.lord.getUpBuildTime());
		builder.setOnBuild(player.lord.getOnBuild());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetBuildingRs, builder.build());
	}

	// /**
	// *
	// * Method: getMill
	// *
	// * @Description: 获取城外建筑
	// * @param handler
	// * @return void
	// * @throws
	// */
	// public void getMill(ChannelHandlerContext ctx, CocoPacket packet,
	// AbstractHandlers.MessageHolder<MessageLite> msg) {
	// Player player = PlayerDataManager.getInst().getPlayer(playerId);
	// GetMillRs.Builder builder = GetMillRs.newBuilder();
	//
	// Iterator<Mill> it = player.mills.values().iterator();
	// while (it.hasNext()) {
	// builder.addMill(PbHelper.createMillPb(it.next()));
	// }
	//
	// handler.sendMsgToPlayer(GetMillRs.ext, builder.build());
	// }

	/**
	 * 
	 * Method: destoryMill
	 * 
	 * @Description: 拆除工厂 @param pos @param handler @return void @throws
	 */
	public void destroyMill(int pos, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Mill mill = player.mills.get(pos);
		if (mill == null || mill.getLv() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_BUILDING);
			return;
		}

		int buildingId = mill.getId();
		int pros;
		ConfBuilding staticBuilding = ConfBuildingProvider.getInst().getConfigById(buildingId);
		if (staticBuilding != null) {
			pros = staticBuilding.getPros() * mill.getLv();
		} else {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		int curPros = player.lord.getPros();
		if (curPros > pros) {
			curPros -= pros;
		} else {
			curPros = 0;
		}

		PlayerDataManager.getInst().subProsMax(player.lord, pros);
		PlayerDataManager.getInst().subPros(player.lord, pros);

		if (staticBuilding.getCanResource() == 1) {
			PlayerDataManager.getInst().subResourceOutAndMax(buildingId, mill.getLv(), player.resource);
		}

		player.mills.remove(pos);

		DestroyMillRs.Builder builder = DestroyMillRs.newBuilder();
		builder.setProsMax(player.lord.getProsMax());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DestroyMillRs, builder.build());
	}

	private void setBuildingLv(int buildingId, Building building, int lv) {
		switch (buildingId) {
		case BuildingId.COMMAND:
			building.setCommand(lv);
			break;
		case BuildingId.WARE_1:
			building.setWare1(lv);
			break;
		case BuildingId.REFIT:
			building.setRefit(lv);
			break;
		case BuildingId.WORKSHOP:
			building.setWorkShop(lv);
			break;
		case BuildingId.TECH:
			building.setTech(lv);
			break;
		case BuildingId.FACTORY_1:
			building.setFactory1(lv);
			break;
		case BuildingId.FACTORY_2:
			building.setFactory2(lv);
			break;
		case BuildingId.WARE_2:
			building.setWare2(lv);
			break;
		}
	}

	private int upBuildingLv(int buildingId, Building building) {
		int lv = 0;
		switch (buildingId) {
		case BuildingId.COMMAND:
			lv = building.getCommand() + 1;
			building.setCommand(lv);
			break;
		case BuildingId.WARE_1:
			lv = building.getWare1() + 1;
			building.setWare1(lv);
			break;
		case BuildingId.REFIT:
			lv = building.getRefit() + 1;
			building.setRefit(lv);
			break;
		case BuildingId.WORKSHOP:
			lv = building.getWorkShop() + 1;
			building.setWorkShop(lv);
			break;
		case BuildingId.TECH:
			lv = building.getTech() + 1;
			building.setTech(lv);
			break;
		case BuildingId.FACTORY_1:
			lv = building.getFactory1() + 1;
			building.setFactory1(lv);
			break;
		case BuildingId.FACTORY_2:
			lv = building.getFactory2() + 1;
			building.setFactory2(lv);
			break;
		case BuildingId.WARE_2:
			lv = building.getWare2() + 1;
			building.setWare2(lv);
			break;
		}
		return lv;
	}

	private int upMillLv(Player player, BuildQue buildQue) {
		Mill mill = player.mills.get(buildQue.getPos());
		if (mill == null) {
			mill = new Mill(buildQue.getPos(), buildQue.getBuildingId(), 1);
			player.mills.put(buildQue.getPos(), mill);
		} else {
			mill.setLv(mill.getLv() + 1);
		}
		return mill.getLv();
	}

	// private int getBuildQueCount(Player player) {
	// int count = 0;
	// ConfVip staticVip =
	// ConfVipProvider.getInst().getConfigById(player.lord.getVip());
	// if (staticVip != null) {
	// count += staticVip.getBuildQue();
	// } else {
	// count += 2;
	// }
	//
	// if (player.effects.containsKey(EffectType.CHANGE_SURFACE_3)) {
	// count += 1;
	// }
	//
	// return count;
	// }

	private int getBuildQueCount(Player player) {
		int count = 2 + player.lord.getBuildCount();
		if (player.effects.containsKey(EffectType.CHANGE_SURFACE_3)) {
			count += 1;
		}

		return count;
	}

	/**
	 * 
	 * Method: speedQue
	 * 
	 * @Description: 加速建筑升级 @param req @param handler @return void @throws
	 */
	public void speedQue(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SpeedQueRq req = msg.get();
		int keyId = req.getKeyId();
		int cost = req.getCost();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<BuildQue> list = player.buildQue;
		BuildQue que = null;

		for (BuildQue buildQue : list) {
			if (buildQue.getKeyId() == keyId) {
				que = buildQue;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		int now = DateUtil.getSecondTime();

		SpeedQueRs.Builder builder = SpeedQueRs.newBuilder();
		if (cost == 1) {// 金币
			int leftTime = que.getEndTime() - now;
			if (leftTime <= 0) {
				leftTime = 1;
			}

			int sub = (int) Math.ceil(leftTime / 60.0);
			Lord lord = player.lord;
			if (lord.getGold() < sub) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, sub, GoldCost.SPEED_BUILD);
			que.setEndTime(now);

			dealBuildQue(player, now);

			builder.setGold(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.SpeedQueRs, builder.build());
			return;
		} else {// 道具
			if (!req.hasField(SpeedQueRq.getDescriptor().findFieldByName("propId"))) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			int propId = req.getPropId();
			Prop prop = player.props.get(propId);
			if (prop == null || prop.getCount() < 1) {
//				handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
				return;
			}

			ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
			if (staticProp.getEffectType() != 3) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			List<List<Integer>> value = staticProp.getEffectValue();
			if (value == null || value.isEmpty()) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			List<Integer> one = value.get(0);
			if (one.size() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			int type = one.get(0);
			int speedTime = one.get(1);
			if (type != 1) {// 建筑加速
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			PlayerDataManager.getInst().subProp(prop, 1);

			que.setEndTime(que.getEndTime() - speedTime);
			dealBuildQue(player, now);

			builder.setCount(prop.getCount());
			builder.setEndTime(que.getEndTime());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.SpeedQueRs, builder.build());
			return;
		}
	}

	/**
	 * 
	 * Method: cancelQue
	 * 
	 * @Description: 玩家取消升级建筑 @param req @param handler @return void @throws
	 */
	public void cancelQue(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		CancelQueRq req = msg.get();
		int keyId = req.getKeyId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<BuildQue> list = player.buildQue;
		BuildQue que = null;

		for (BuildQue buildQue : list) {
			if (buildQue.getKeyId() == keyId) {
				que = buildQue;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		Building building = player.building;
		int buildLevel = PlayerDataManager.getBuildingLv(que.getBuildingId(), building);
		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(que.getBuildingId(), buildLevel + 1);
		if (staticBuildingLevel == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		list.remove(que);

		if (que.getPos() != 0) {
			Mill mill = player.mills.get(que.getPos());
			if (mill.getLv() == 0) {
				player.mills.remove(que.getPos());
			}
		}

		Resource resource = player.resource;
		CancelQueRs.Builder builder = CancelQueRs.newBuilder();

		int add = staticBuildingLevel.getIronCost();
		if (add > 0) {
			PlayerDataManager.getInst().modifyIron(resource, add / 2);
			builder.setIron(resource.getIron());
		}

		add = staticBuildingLevel.getOilCost();
		if (add > 0) {
			PlayerDataManager.getInst().modifyOil(resource, add / 2);
			builder.setOil(resource.getOil());
		}

		add = staticBuildingLevel.getCopperCost();
		if (add > 0) {
			PlayerDataManager.getInst().modifyCopper(resource, add / 2);
			builder.setCopper(resource.getCopper());
		}

		add = staticBuildingLevel.getSiliconCost();
		if (add > 0) {
			PlayerDataManager.getInst().modifySilicon(resource, add / 2);
			builder.setSilicon(resource.getSilicon());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CancelQueRs, builder.build());
	}

	private BuildQue createQue(Player player, int buildingId, int pos, int period, int endTime) {

		// ConfVip staticVip = ConfVipProvider.getInst().getConfigById(lord.getVip());
		// if (staticVip != null) {
		// return staticVip.getWaitQue();
		// }

		BuildQue buildQue = new BuildQue(player.maxKey(), buildingId, pos, period, endTime);
		return buildQue;
	}

	private int calcBuildTime(Player player, int baseTime) {
		float factor = 1;
		// 建筑设计科技
		Science science = player.sciences.get(ScienceId.BUILD);
		if (science != null) {
			factor += (science.getScienceLv() * 5 / 100.0f);
		}

		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			factor += (staticVip.getSpeedBuild() / 100.0f);
		}
		return (int) (baseTime / factor);
	}

	/**
	 * 
	 * Method: upBuilding
	 * 
	 * @Description: 升级建筑 @param req @param handler @return void @throws
	 */
	public void upBuilding(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UpBuildingRq req = msg.get();
		int type = req.getType();
		int buildingId = req.getBuildingId();
		ConfBuilding staticBuilding = ConfBuildingProvider.getInst().getConfigById(buildingId);
		if (staticBuilding == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticBuilding.getCanUp() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		List<BuildQue> buildQue = player.buildQue;
		if (buildQue.size() >= getBuildQueCount(player)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_BUILD_QUE);
			return;
		}

		for (BuildQue build : buildQue) {
			if (build.getBuildingId() == buildingId) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_BUILD);
				return;
			}
		}

		Building building = player.building;
		int buildLevel = PlayerDataManager.getBuildingLv(buildingId, building);
		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(buildingId, buildLevel + 1);
		if (staticBuildingLevel == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (lord.getLevel() < staticBuildingLevel.getLordLv()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		if (building.getCommand() < staticBuildingLevel.getCommandLv()) {
//			handler.sendErrorMsgToPlayer(GameError.COMMAND_LV_NOT_ENOUGH);
			return;
		}

		if (type == 1) {// 金币升级
			int cost = staticBuildingLevel.getGoldCost();
			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.UP_BUILDING);

			int now = DateUtil.getSecondTime();
			int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
			BuildQue que = createQue(player, buildingId, 0, haust, now + haust);
			buildQue.add(que);

			UpBuildingRs.Builder builder = UpBuildingRs.newBuilder();
			builder.setGold(lord.getGold());
			builder.setQueue(PbHelper.createBuildQuePb(que));

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpBuildingRs, builder.build());
//			handler.sendMsgToPlayer(UpBuildingRs.ext, builder.build());
		} else if (type == 2) {// 资源升级
			int ironCost = staticBuildingLevel.getIronCost();
			int oilCost = staticBuildingLevel.getOilCost();
			int copperCost = staticBuildingLevel.getCopperCost();
			int siliconCost = staticBuildingLevel.getSiliconCost();

			Resource resource = player.resource;
			if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
//				handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().modifyIron(resource, -ironCost);
			PlayerDataManager.getInst().modifyOil(resource, -oilCost);
			PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
			PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);

			int now = DateUtil.getSecondTime();
			int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
			BuildQue build = createQue(player, buildingId, 0, haust, now + haust);
			buildQue.add(build);

			UpBuildingRs.Builder builder = UpBuildingRs.newBuilder();
			builder.setQueue(PbHelper.createBuildQuePb(build));

			if (ironCost > 0) {
				builder.setIron(resource.getIron());
			}

			if (oilCost > 0) {
				builder.setOil(resource.getOil());
			}

			if (copperCost > 0) {
				builder.setCopper(resource.getCopper());
			}

			if (siliconCost > 0) {
				builder.setSilicon(resource.getSilicon());
			}

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpBuildingRs, builder.build());
		}
	}

	private int normalMillCount(Player player) {
		int count = 0;
		Iterator<Mill> it = player.mills.values().iterator();
		int buildingId;
		while (it.hasNext()) {
			buildingId = it.next().getId();
			if (buildingId == BuildingId.OIL || buildingId == BuildingId.COPPER || buildingId == BuildingId.IRON)
				count++;
		}
		return count;
	}

	private int siliconMillCount(Player player) {
		int count = 0;
		Iterator<Mill> it = player.mills.values().iterator();
		int buildingId;
		while (it.hasNext()) {
			buildingId = it.next().getId();
			if (buildingId == BuildingId.SILICON)
				count++;
		}
		return count;
	}

	private boolean checkMillCount(Player player, int millId) {
		int commandLv = player.building.getCommand();
		int max = 0;
		switch (millId) {
		case BuildingId.OIL:
		case BuildingId.COPPER:
		case BuildingId.IRON:
			max = commandLv / 2 + 5;
			max = (max > 35) ? 35 : max;
			if (normalMillCount(player) < max)
				return true;
			break;
		case BuildingId.SILICON:
			max = commandLv / 10;
			max = (max > 6) ? 6 : max;
			if (siliconMillCount(player) < max)
				return true;
			break;
		case BuildingId.STONE:
			if (player.mills.get(42) == null)
				return true;
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 
	 * Method: upMill
	 * 
	 * @Description: 升级城外建筑 @param req @param handler @return void @throws
	 */
	public void upMill(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UpBuildingRq req = msg.get();
		int type = req.getType();
		int pos = req.getPos();
		int buildingId = req.getBuildingId();

		if (pos < 1 || pos > 42) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfBuilding staticBuilding = ConfBuildingProvider.getInst().getConfigById(buildingId);
		if (staticBuilding == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticBuilding.getCanUp() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;

		if (pos == 42 && buildingId != BuildingId.STONE) {// 30只能放宝石矿
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (pos != 42 && buildingId == BuildingId.STONE) {// 宝石矿只能放在30
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if ((pos < 36 || pos > 41) && buildingId == BuildingId.SILICON) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if ((pos > 35 && pos < 42) && buildingId != BuildingId.SILICON) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		List<BuildQue> buildQue = player.buildQue;
		if (buildQue.size() >= getBuildQueCount(player)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_BUILD_QUE);
			return;
		}

		for (BuildQue build : buildQue) {
			if (build.getPos() == pos) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_BUILD);
				return;
			}
		}

		Mill mill = player.mills.get(pos);
		if (mill == null) {
			if (!checkMillCount(player, buildingId)) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_MILL);
				return;
			}

			mill = new Mill(pos, buildingId, 0);
		}

		int buildLevel = mill.getLv();
		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(buildingId, buildLevel + 1);
		if (staticBuildingLevel == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (lord.getLevel() < staticBuildingLevel.getLordLv()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		if (player.building.getCommand() < staticBuildingLevel.getCommandLv()) {
//			handler.sendErrorMsgToPlayer(GameError.COMMAND_LV_NOT_ENOUGH);
			return;
		}

		if (type == 1) {// 金币升级
			int cost = staticBuildingLevel.getGoldCost();
			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.UP_BUILDING);

			int now = DateUtil.getSecondTime();
			int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
			BuildQue que = createQue(player, buildingId, pos, haust, now + haust);
			buildQue.add(que);

			player.mills.put(pos, mill);
			UpBuildingRs.Builder builder = UpBuildingRs.newBuilder();
			builder.setGold(lord.getGold());
			builder.setQueue(PbHelper.createBuildQuePb(que));

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpBuildingRs, builder.build());
		} else if (type == 2) {// 资源升级
			int ironCost = staticBuildingLevel.getIronCost();
			int oilCost = staticBuildingLevel.getOilCost();
			int copperCost = staticBuildingLevel.getCopperCost();
			int siliconCost = staticBuildingLevel.getSiliconCost();

			Resource resource = player.resource;
			if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
//				handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().modifyIron(resource, -ironCost);
			PlayerDataManager.getInst().modifyOil(resource, -oilCost);
			PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
			PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);

			int now = DateUtil.getSecondTime();
			int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
			BuildQue build = createQue(player, buildingId, pos, haust, now + haust);
			buildQue.add(build);
			player.mills.put(pos, mill);

			UpBuildingRs.Builder builder = UpBuildingRs.newBuilder();
			builder.setQueue(PbHelper.createBuildQuePb(build));

			if (ironCost > 0) {
				builder.setIron(resource.getIron());
			}

			if (oilCost > 0) {
				builder.setOil(resource.getOil());
			}

			if (copperCost > 0) {
				builder.setCopper(resource.getCopper());
			}

			if (siliconCost > 0) {
				builder.setSilicon(resource.getSilicon());
			}

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpBuildingRs, builder.build());
		}
	}

	private List<Build> selectLowLv(Player player) {
		Building building = player.building;
		List<Build> list = new ArrayList<>();

		if (building.getCommand() > 0) {
			list.add(new Build(BuildingId.COMMAND, building.getCommand(), 0));
		}

		if (building.getWare1() > 0) {
			list.add(new Build(BuildingId.WARE_1, building.getWare1(), 0));
		}

		if (building.getRefit() > 0) {
			list.add(new Build(BuildingId.REFIT, building.getRefit(), 0));
		}

		if (building.getTech() > 0) {
			list.add(new Build(BuildingId.TECH, building.getTech(), 0));
		}

		if (building.getFactory1() > 0) {
			list.add(new Build(BuildingId.FACTORY_1, building.getFactory1(), 0));
		}

		if (building.getFactory2() > 0) {
			list.add(new Build(BuildingId.FACTORY_2, building.getFactory2(), 0));
		}

		if (building.getWare2() > 0) {
			list.add(new Build(BuildingId.WARE_2, building.getWare2(), 0));
		}

		Iterator<Mill> it = player.mills.values().iterator();
		while (it.hasNext()) {
			Mill mill = it.next();
			if (mill.getLv() > 0) {
				list.add(new Build(mill.getId(), mill.getLv(), mill.getPos()));
			}
		}

		Collections.sort(list, new ComparatorBuild());
		return list;
	}

	private boolean doAutoMill(Player player, Build build) {
		for (BuildQue que : player.buildQue) {
			if (build.id == que.getBuildingId() && build.pos == que.getPos()) {
				return false;
			}
		}

		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(build.id, build.lv + 1);
		if (staticBuildingLevel == null) {
			return false;
		}

		if (player.lord.getLevel() < staticBuildingLevel.getLordLv()) {
			return false;
		}

		if (player.building.getCommand() < staticBuildingLevel.getCommandLv()) {
			return false;
		}

		int ironCost = staticBuildingLevel.getIronCost();
		int oilCost = staticBuildingLevel.getOilCost();
		int copperCost = staticBuildingLevel.getCopperCost();
		int siliconCost = staticBuildingLevel.getSiliconCost();

		Resource resource = player.resource;
		if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
			return false;
		}

		PlayerDataManager.getInst().modifyIron(resource, -ironCost);
		PlayerDataManager.getInst().modifyOil(resource, -oilCost);
		PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
		PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);

		int now = DateUtil.getSecondTime();
		int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
		BuildQue que = createQue(player, build.id, build.pos, haust, now + haust);
		player.buildQue.add(que);

		PlayerDataManager.getInst().synBuildToPlayer(player, que, 1);
		PlayerDataManager.getInst().synResourceToPlayer(player, -ironCost, -oilCost, -copperCost, -siliconCost, 0);
		return true;
	}

	private boolean doAutoBuild(Player player, Build build) {
		for (BuildQue que : player.buildQue) {
			if (build.id == que.getBuildingId()) {
				return false;
			}
		}

		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(build.id, build.lv + 1);
		if (staticBuildingLevel == null) {
			return false;
		}

		if (player.lord.getLevel() < staticBuildingLevel.getLordLv()) {
			return false;
		}

		if (player.building.getCommand() < staticBuildingLevel.getCommandLv()) {
			return false;
		}

		int ironCost = staticBuildingLevel.getIronCost();
		int oilCost = staticBuildingLevel.getOilCost();
		int copperCost = staticBuildingLevel.getCopperCost();
		int siliconCost = staticBuildingLevel.getSiliconCost();

		Resource resource = player.resource;
		if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
			return false;
		}

		PlayerDataManager.getInst().modifyIron(resource, -ironCost);
		PlayerDataManager.getInst().modifyOil(resource, -oilCost);
		PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
		PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);

		int now = DateUtil.getSecondTime();
		int haust = calcBuildTime(player, staticBuildingLevel.getUpTime());
		BuildQue que = createQue(player, build.id, 0, haust, now + haust);
		player.buildQue.add(que);

		PlayerDataManager.getInst().synBuildToPlayer(player, que, 1);
		PlayerDataManager.getInst().synResourceToPlayer(player, -ironCost, -oilCost, -copperCost, -siliconCost, 0);
		return true;
	}

	private void autoBuild(Player player) {
		int count = getBuildQueCount(player) - player.buildQue.size();
		List<Build> list = selectLowLv(player);
		int b = 0;

		for (Build build : list) {
			if (b == count) {
				break;
			}

			if (build.pos > 0) {
				if (doAutoMill(player, build)) {
					b++;
				}
			} else {
				if (doAutoBuild(player, build)) {
					b++;
				}
			}
		}
	}

	private void dealAutoBuild(Player player) {
		autoBuild(player);
		int leftTime = player.lord.getUpBuildTime();
		player.lord.setUpBuildTime(leftTime - 1);
		if (leftTime <= 0) {
			player.lord.setUpBuildTime(0);
			player.lord.setOnBuild(0);
		}
	}

	private int dealOneQue(Player player, BuildQue buildQue) {
		int pros = 0;
		int buildingId = buildQue.getBuildingId();
		int buildingLv;

		if (buildQue.getPos() != 0) {
			buildingLv = upMillLv(player, buildQue);
			TaskManager.getInst().updTask(player, TaskType.COND_BUILDING_LV_UP, 1, null);
		} else {
			buildingLv = upBuildingLv(buildingId, player.building);
			TaskManager.getInst().updTask(player, TaskType.COND_BUILDING_LV_UP, 1, null);
		}

		ConfBuilding staticBuilding = ConfBuildingProvider.getInst().getConfigById(buildingId);
		if (staticBuilding != null) {
			pros += staticBuilding.getPros();
		}

		if (staticBuilding.getCanResource() == 1 || buildingId == BuildingId.WARE_1 || buildingId == BuildingId.WARE_1) {
			PlayerDataManager.getInst().addResourceOutAndMax(buildingId, buildingLv, player.resource);
		}

		PlayerDataManager.getInst().synBuildToPlayer(player, buildQue, 2);
		return pros;
	}

	private void dealBuildQue(Player player, int now) {
		List<BuildQue> list = player.buildQue;
		Iterator<BuildQue> it = list.iterator();
		boolean update = false;
		int pros = 0;
		while (it.hasNext()) {
			BuildQue buildQue = it.next();
			if (now >= buildQue.getEndTime()) {
				pros += dealOneQue(player, buildQue);
				update = true;
				it.remove();
				continue;
			}
		}

		if (update) {
			PlayerDataManager.getInst().addProsMax(player, pros);
		}
	}

	public void buyAutoBuild(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		int cost = 238;
		if (player.lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
			return;
		}

		PlayerDataManager.getInst().subGold(player.lord, cost, GoldCost.BUY_AUTO_BUILD);

		player.lord.setUpBuildTime(player.lord.getUpBuildTime() + 4 * 60 * 60);
		BuyAutoBuildRs.Builder builder = BuyAutoBuildRs.newBuilder();
		builder.setGold(player.lord.getGold());
		builder.setUpBuildTime(player.lord.getUpBuildTime());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyAutoBuildRs, builder.build());
	}

	public void setAutoBuild(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SetAutoBuildRq req = msg.get();
		boolean state = req.getState();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		if (state) {
			if (player.lord.getUpBuildTime() > 0) {
				player.lord.setOnBuild(1);
			}
		} else {
			player.lord.setOnBuild(0);
		}

		SetAutoBuildRs.Builder builder = SetAutoBuildRs.newBuilder();
		builder.setOnBuild(player.lord.getOnBuild());
		builder.setUpBuildTime(player.lord.getUpBuildTime());

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SetAutoBuildRs, builder.build());
	}

	/**
	 * 
	 * Method: buildQueTimerLogic
	 * 
	 * @Description: 建筑队列定时器逻辑 @return void @throws
	 */
	public void buildQueTimerLogic() {
		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
		int now = DateUtil.getSecondTime();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (player.isActive()) {
				if (!player.buildQue.isEmpty()) {
					dealBuildQue(player, now);
				}

				if (player.lord.getOnBuild() == 1) {
					dealAutoBuild(player);
				}
			}
		}
	}

	private void dealResourceMinute(Player player, int now) {
		Resource resource = player.resource;
		int reduce = 0;
		if (PlayerDataManager.getInst().isRuins(player)) {
			reduce = 100;
		}

		int stoneOut = (int) (resource.getStoneOut() * (100 + resource.getStoneOutF() - reduce) / 6000.0f);
		int ironOut = (int) (resource.getIronOut() * (100 + resource.getIronOutF() - reduce) / 6000.0f);
		int oilOut = (int) (resource.getOilOut() * (100 + resource.getOilOutF() - reduce) / 6000.0f);
		int copperOut = (int) (resource.getCopperOut() * (100 + resource.getCopperOutF() - reduce) / 6000.0f);
		int siliconOut = (int) (resource.getSiliconOut() * (100 + resource.getSiliconOutF() - reduce) / 6000.0f);

		long add = 0;
		int storeF = 100 + resource.getStoreF();
		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science = sciences.get(ScienceId.PARTY_STORAGE);
			if (science != null) {
				storeF += science.getScienceLv();
			}
		}

		int max = (int) (resource.getStoneMax() * storeF / 100.0f);
		if (resource.getStone() < max) {
			add = (resource.getStone() + stoneOut > max) ? (max - resource.getStone()) : stoneOut;
			if (add > 0) {
				PlayerDataManager.getInst().modifyStone(resource, (int) add);
			}
		}

		max = (int) (resource.getIronMax() * storeF / 100.0f);
		if (resource.getIron() < max) {
			add = (resource.getIron() + ironOut > max) ? (max - resource.getIron()) : ironOut;
			if (add > 0) {
				PlayerDataManager.getInst().modifyIron(resource, (int) add);
			}
		}

		max = (int) (resource.getOilMax() * storeF / 100.0f);
		if (resource.getOil() < max) {
			add = (resource.getOil() + oilOut > max) ? (max - resource.getOil()) : oilOut;
			if (add > 0) {
				PlayerDataManager.getInst().modifyOil(resource, (int) add);
			}
		}

		max = (int) (resource.getCopperMax() * storeF / 100.0f);
		if (resource.getCopper() < max) {
			add = (resource.getCopper() + copperOut > max) ? (max - resource.getCopper()) : copperOut;
			if (add > 0) {
				PlayerDataManager.getInst().modifyCopper(resource, (int) add);
			}
		}

		max = (int) (resource.getCopperMax() * storeF / 100.0f);
		if (resource.getSilicon() < max) {
			add = (resource.getSilicon() + siliconOut > max) ? (max - resource.getSilicon()) : siliconOut;
			if (add > 0) {
				PlayerDataManager.getInst().modifySilicon(resource, (int) add);
			}
		}

		resource.setStoreTime(now);
	}

	private void dealResource(Player player, int now) {
		if (player.resource.getStoreTime() < now) {
			dealResourceMinute(player, now);
		}
	}

	/**
	 * 
	 * Method: resourceTimerLogic
	 * 
	 * @Description: 资源生产逻辑 @return void @throws
	 */
	public void resourceTimerLogic() {
		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
		int now = DateUtil.getMinuteTime();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			// if (player.account.getCreated() == 1) {
			dealResource(player, now);
			// }
		}
	}

	public void recalcResourceOut(Player player) {
		Resource resource = player.resource;
		resource.setCopperOut(0);
		resource.setIronOut(0);
		resource.setOilOut(0);
		resource.setStoneOut(0);
		resource.setSiliconOut(0);

		resource.setStoneMax(0);
		resource.setIronMax(0);
		resource.setOilMax(0);
		resource.setCopperMax(0);
		resource.setSiliconMax(0);

		int lv = 0;
		for (int id = BuildingId.COMMAND; id < BuildingId.WARE_2 + 1; id++) {
			lv = PlayerDataManager.getBuildingLv(id, player.building);
			if (lv == 0) {
				continue;
			}

			ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(id, lv);
			if (staticBuildingLevel != null) {
				resource.setStoneMax(staticBuildingLevel.getStoneMax() + resource.getStoneMax());
				resource.setIronMax(staticBuildingLevel.getIronMax() + resource.getIronMax());
				resource.setOilMax(staticBuildingLevel.getOilMax() + resource.getOilMax());
				resource.setCopperMax(staticBuildingLevel.getCopperMax() + resource.getCopperMax());
				resource.setSiliconMax(staticBuildingLevel.getSiliconMax() + resource.getSiliconMax());

				resource.setStoneOut(staticBuildingLevel.getStoneOut() + resource.getStoneOut());
				resource.setIronOut(staticBuildingLevel.getIronOut() + resource.getIronOut());
				resource.setOilOut(staticBuildingLevel.getOilOut() + resource.getOilOut());
				resource.setCopperOut(staticBuildingLevel.getCopperOut() + resource.getCopperOut());
				resource.setSiliconOut(staticBuildingLevel.getSiliconOut() + resource.getSiliconOut());
			}
		}

		Iterator<Mill> it = player.mills.values().iterator();
		while (it.hasNext()) {
			Mill mill = it.next();
			lv = mill.getLv();
			if (lv == 0) {
				continue;
			}

			ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(mill.getId(), lv);
			if (staticBuildingLevel != null) {
				resource.setStoneMax(staticBuildingLevel.getStoneMax() + resource.getStoneMax());
				resource.setIronMax(staticBuildingLevel.getIronMax() + resource.getIronMax());
				resource.setOilMax(staticBuildingLevel.getOilMax() + resource.getOilMax());
				resource.setCopperMax(staticBuildingLevel.getCopperMax() + resource.getCopperMax());
				resource.setSiliconMax(staticBuildingLevel.getSiliconMax() + resource.getSiliconMax());

				resource.setStoneOut(staticBuildingLevel.getStoneOut() + resource.getStoneOut());
				resource.setIronOut(staticBuildingLevel.getIronOut() + resource.getIronOut());
				resource.setOilOut(staticBuildingLevel.getOilOut() + resource.getOilOut());
				resource.setCopperOut(staticBuildingLevel.getCopperOut() + resource.getCopperOut());
				resource.setSiliconOut(staticBuildingLevel.getSiliconOut() + resource.getSiliconOut());
			}
		}
	}

	public void recalcResourceOut() {
		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();

		while (iterator.hasNext()) {
			Player player = iterator.next();
			recalcResourceOut(player);

		}
	}
}
