package network.handler.module.tank;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfHero;
import config.bean.ConfProp;
import config.bean.ConfTank;
import config.bean.ConfVip;
import config.provider.ConfHeroProvider;
import config.provider.ConfPropProvider;
import config.provider.ConfTankProvider;
import config.provider.ConfVipProvider;
import data.bean.Arena;
import data.bean.Army;
import data.bean.Form;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Prop;
import data.bean.RefitQue;
import data.bean.Resource;
import data.bean.Tank;
import data.bean.TankQue;
import define.BuildingId;
import define.FormType;
import define.GoldCost;
import define.HeroId;
import define.PropId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.ArenaDataManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb;
import pb.GamePb.BuildTankRq;
import pb.GamePb.BuildTankRs;
import pb.GamePb.CancelQueRq;
import pb.GamePb.CancelQueRs;
import pb.GamePb.GetArmyRs;
import pb.GamePb.GetFormRs;
import pb.GamePb.GetTankRs;
import pb.GamePb.RefitTankRq;
import pb.GamePb.RefitTankRs;
import pb.GamePb.RepairRq;
import pb.GamePb.RepairRs;
import pb.GamePb.SetFormRq;
import pb.GamePb.SetFormRs;
import pb.GamePb.SpeedQueRq;
import pb.GamePb.SpeedQueRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class ArmyService implements IModuleMessageHandler {

	public static ArmyService getInst() {
		return BeanManager.getBean(ArmyService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	private boolean dealTankQue(Player player, List<TankQue> list, int now) {
		Iterator<TankQue> it = list.iterator();
		int endTime = 0;
		boolean complete = false;
		Tank tank;
		while (it.hasNext()) {
			TankQue tankQue = it.next();
			if (tankQue.getState() == 1) {
				endTime = tankQue.getEndTime();
				if (now >= endTime) {
					tank = PlayerDataManager.getInst().addTank(player, tankQue.getTankId(), tankQue.getCount());
					TaskManager.getInst().updTask(player, TaskType.COND_TANK_PRODUCT, tankQue.getCount(), tankQue.getTankId());
					LogHelper.logCompleteTank(player.lord, tankQue, tank);
					it.remove();
					complete = true;
					continue;
				}
				break;
			} else {
				if (endTime == 0) {
					endTime = now;
				}

				endTime += tankQue.getPeriod();
				if (now >= endTime) {
					tank = PlayerDataManager.getInst().addTank(player, tankQue.getTankId(), tankQue.getCount());
					TaskManager.getInst().updTask(player, TaskType.COND_TANK_PRODUCT, tankQue.getCount(), tankQue.getTankId());
					LogHelper.logCompleteTank(player.lord, tankQue, tank);
					it.remove();
					complete = true;
					continue;
				}

				tankQue.setState(1);
				tankQue.setEndTime(endTime);
				break;
			}
		}

		return complete;
	}

	private boolean dealRefitQue(Player player, List<RefitQue> list, int now) {
		Iterator<RefitQue> it = list.iterator();
		int endTime = 0;
		boolean complete = false;
		Tank tank;
		while (it.hasNext()) {
			RefitQue refitQue = it.next();
			if (refitQue.getState() == 1) {
				endTime = refitQue.getEndTime();
				if (now >= endTime) {
					tank = PlayerDataManager.getInst().addTank(player, refitQue.getRefitId(), refitQue.getCount());
					LogHelper.logCompleteTank(player.lord, refitQue, tank);
					it.remove();
					complete = true;
					continue;
				}
				break;
			} else {
				if (endTime == 0) {
					endTime = now;
				}

				endTime += refitQue.getPeriod();
				if (now >= endTime) {
					tank = PlayerDataManager.getInst().addTank(player, refitQue.getRefitId(), refitQue.getCount());
					LogHelper.logCompleteTank(player.lord, refitQue, tank);
					it.remove();
					complete = true;
					continue;
				}

				refitQue.setState(1);
				refitQue.setEndTime(endTime);
				break;
			}
		}

		return complete;
	}

	/**
	 * 
	 * Method: getTank
	 * 
	 * @Description: 获取玩家坦克数据 @param ctx @return void @throws
	 */
	public void getTank(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player != null) {
			GetTankRs.Builder builder = GetTankRs.newBuilder();
			for (Map.Entry<Integer, Tank> entry : player.tanks.entrySet()) {
				builder.addTank(PbHelper.createTankPb(entry.getValue()));
			}

			for (TankQue tankQue : player.tankQue_1) {
				builder.addQueue1(PbHelper.createTankQuePb(tankQue));
			}

			for (TankQue tankQue : player.tankQue_2) {
				builder.addQueue2(PbHelper.createTankQuePb(tankQue));
			}

			for (RefitQue refitQue : player.refitQue) {
				builder.addRefit(PbHelper.createRefitQuePb(refitQue));
			}

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetTankRs, builder.build());
		}
	}

	/**
	 * 
	 * Method: getArmy
	 * 
	 * @Description: 客户端获取部队数据 @param ctx @return void @throws
	 */
	public void getArmy(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player != null) {
			GetArmyRs.Builder builder = GetArmyRs.newBuilder();
			List<Army> list = player.armys;
			for (Army army : list) {
				builder.addArmy(PbHelper.createArmyPb(army));
			}

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetArmyRs, builder.build());
		}
	}

	/**
	 * 
	 * Method: getForm
	 * 
	 * @Description: 客户端获取阵型数据 @param ctx @return void @throws
	 */
	public void getForm(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player != null) {
			GetFormRs.Builder builder = GetFormRs.newBuilder();
			Iterator<Form> it = player.forms.values().iterator();
			while (it.hasNext()) {
				builder.addForm(PbHelper.createFormPb(it.next()));
			}

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetFormRs, builder.build());
		}
	}

	// private void setLordFormByIndex(Form lordForm, int index, int tankId, int
	// count) {
	// switch (index) {
	// case 1:
	// lordForm.setP1(tankId);
	// lordForm.setC1(count);
	// break;
	// case 2:
	// lordForm.setP2(tankId);
	// lordForm.setC2(count);
	// break;
	// case 3:
	// lordForm.setP3(tankId);
	// lordForm.setC3(count);
	// break;
	// case 4:
	// lordForm.setP4(tankId);
	// lordForm.setC4(count);
	// break;
	// case 5:
	// lordForm.setP5(tankId);
	// lordForm.setC5(count);
	// break;
	// case 6:
	// lordForm.setP6(tankId);
	// lordForm.setC6(count);
	// break;
	// default:
	// break;
	// }
	// }

	// private TwoInt dealForm(Map<Integer, Tank> tankMap, TwoInt v, Form
	// lordForm, int index) {
	// int tankId = v.getV1();
	// int count = v.getV2();
	// if (count >= 0) {
	// Tank tank = tankMap.get(tankId);
	// if (tank != null) {
	// int hasCount = tank.getCount();
	// if (hasCount >= 0) {
	// int putCount = (hasCount >= count) ? count : hasCount;
	// tank.setCount(hasCount - putCount);
	// setLordFormByIndex(lordForm, index, tankId, putCount);
	// TwoInt.Builder bd = TwoInt.newBuilder();
	// bd.setV1(tankId);
	// bd.setV2(putCount);
	// return bd.build();
	// }
	// } else if (tankId == 0) {
	// setLordFormByIndex(lordForm, index, 0, 0);
	// TwoInt.Builder bd = TwoInt.newBuilder();
	// bd.setV1(tankId);
	// bd.setV2(count);
	// return bd.build();
	// }
	// }
	// return null;
	// }

	/**
	 * 
	 * Method: setForm
	 * 
	 * @Description: 玩家设置阵型 @param req @param ctx @return void @throws
	 */
	public void setForm(SetFormRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		boolean clean = false;
		if (req.hasField(SetFormRq.getDescriptor().findFieldByName("clean"))) {
			clean = req.getClean();
		}

		CommonPb.Form form = req.getForm();
		if (!form.hasField(CommonPb.Form.getDescriptor().findFieldByName("type"))) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		int formType = form.getType();
		if (formType < FormType.TEMPLATE || formType > FormType.BOSS) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (formType == FormType.ARENA) {
			if (player.lord.getLevel() < ArenaService.ARENA_LV) {
//				handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
				return;
			}
		} else if (formType == FormType.BOSS) {
			if (player.lord.getLevel() < 30) {
//				handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
				return;
			}
		}

		Form destForm = PbHelper.createForm(form);

		ConfHero staticHero = null;
		if (destForm.getCommander() > 0) {
			Hero hero = player.heros.get(destForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);
		if (!clean) {
			if (!PlayerDataManager.getInst().checkTank(player, destForm, maxTankCount)) {
//				handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
				return;
			}
		}

		int fight = FightService.getInst().calcFormFight(player, destForm);

		if (formType == FormType.ARENA) {
			Arena arena = ArenaDataManager.getInst().getArena(player.roleId);
			if (arena == null) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}
			arena.setFight(fight);

		} else if (formType == FormType.VIP2) {
			if (player.lord.getVip() < 2) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
				return;
			}
		} else if (formType == FormType.VIP5) {
			if (player.lord.getVip() < 5) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
				return;
			}
		} else if (formType == FormType.VIP8) {
			if (player.lord.getVip() < 8) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
				return;
			}
		}

		if (clean && formType != FormType.ARENA) {
			player.forms.remove(formType);
		}

		if (!clean) {
			player.forms.put(formType, destForm);
		}

		SetFormRs.Builder builder = SetFormRs.newBuilder();
		builder.setForm(PbHelper.createFormPb(destForm));
		builder.setFight(fight);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SetFormRs, builder.build());
	}

	private void repairOne(int tankId, int repairType, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		if (staticTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Tank tank = player.tanks.get(tankId);
		if (tank == null || tank.getRest() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_REPAIR);
			return;
		}

		if (repairType == 1) {// 宝石修理
			int rest = tank.getRest();
			Resource resource = player.resource;
			int cost = rest * staticTank.getRepair();
			if (cost < 0) {
//				handler.sendErrorMsgToPlayer(GameError.DATA_EXCEPTION);
				return;
			}

			if (resource.getStone() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
				return;
			}

			resource.setStone(resource.getStone() - cost);

			tank.setCount(tank.getCount() + rest);
			tank.setRest(0);
			// armyDao.updateTank(tank);

			RepairRs.Builder builder = RepairRs.newBuilder();
			builder.setCount(rest);
			builder.setCur(resource.getStone());

			MsgHelper.sendResponse(ctx, playerId, ResponseCode.RepairRs, builder.build());
			return;
		} else if (repairType == 2) {// 金币修
			int rest = tank.getRest();
			Lord lord = player.lord;
			if (lord == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_LORD);
				return;
			}

			int cost = rest / 10 + (rest % 10 > 0 ? 1 : 0);
			if (cost < 0) {
//				handler.sendErrorMsgToPlayer(GameError.DATA_EXCEPTION);
				return;
			}

			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.REPAIR);

			tank.setCount(tank.getCount() + rest);
			tank.setRest(0);
			// armyDao.updateTank(tank);

			RepairRs.Builder builder = RepairRs.newBuilder();
			builder.setCount(rest);
			builder.setCur(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.RepairRs, builder.build());
			return;
		}
	}

	private void repairAllByGold(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		// List<Tank> updateList = new ArrayList<>();
		Lord lord = player.lord;
		Map<Integer, Tank> tanks = player.tanks;

		int cost = 0;
		int count = 0;
		for (Tank tank : tanks.values()) {
			int tankId = tank.getTankId();
			if (tank == null || tank.getRest() == 0) {
				continue;
			}

			ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
			if (staticTank == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			count += tank.getRest();
			cost += tank.getRest() / 10 + (tank.getRest() % 10 > 0 ? 1 : 0);
			if (cost < 0) {
//				handler.sendErrorMsgToPlayer(GameError.DATA_EXCEPTION);
				return;
			}

			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
		}

		for (Tank tank : tanks.values()) {
			tank.setCount(tank.getCount() + tank.getRest());
			tank.setRest(0);
		}

		PlayerDataManager.getInst().subGold(lord, cost, GoldCost.REPAIR);

		// for (TankDb tank : updateList) {
		// armyDao.updateTank(tank);
		// }

		RepairRs.Builder builder = RepairRs.newBuilder();
		builder.setCount(count);
		builder.setCur(lord.getGold());

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.RepairRs, builder.build());
	}

	private void repairAllByStone(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		// List<Tank> updateList = new ArrayList<>();
		Resource resource = player.resource;
		Map<Integer, Tank> tanks = player.tanks;

		int cost = 0;
		int count = 0;
		for (Tank tank : tanks.values()) {
			int tankId = tank.getTankId();
			if (tank.getRest() == 0) {
				continue;
			}

			ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
			if (staticTank == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			count += tank.getRest();
			cost += tank.getRest() * staticTank.getRepair();
			if (cost < 0) {
//				handler.sendErrorMsgToPlayer(GameError.DATA_EXCEPTION);
				return;
			}

			if (resource.getStone() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
				return;
			}

			// updateList.add(tank);
		}

		for (Tank tank : tanks.values()) {
			tank.setCount(tank.getCount() + tank.getRest());
			tank.setRest(0);
		}

		resource.setStone(resource.getStone() - cost);
		// resourceDao.updateResource(resource);

		// for (TankDb tank : updateList) {
		// armyDao.updateTank(tank);
		// }

		RepairRs.Builder builder = RepairRs.newBuilder();
		builder.setCount(count);
		builder.setCur(resource.getStone());

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.RepairRs, builder.build());
	}

	private void repairAll(int repairType, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		if (repairType == 1) {
			repairAllByStone(ctx, packet, msg);
		} else if (repairType == 2) {
			repairAllByGold(ctx, packet, msg);
		} else {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
		}
	}

	/**
	 * 
	 * Method: repair
	 * 
	 * @Description: TODO @param req @param ctx @return void @throws
	 */
	public void repair(RepairRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int tankId = req.getTankId();
		int repairType = req.getRepairType();
		if (tankId == 0) {
			repairAll(repairType, ctx, packet, msg);
		} else {
			repairOne(tankId, repairType, ctx, packet, msg);
		}
	}

	private int getTankQueWaitCount(Lord lord) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(lord.getVip());
		if (staticVip != null) {
			return staticVip.getWaitQue();
		}
		return 0;
	}

	private TankQue createTankWaitQue(Player player, int tankId, int count, int period, int endTime) {
		TankQue tankQue = new TankQue(player.maxKey(), tankId, count, 0, period, endTime);
		return tankQue;
	}

	private TankQue createTankQue(Player player, int tankId, int count, int period, int endTime) {
		TankQue tankQue = new TankQue(player.maxKey(), tankId, count, 1, period, endTime);
		return tankQue;
	}

	private RefitQue createRefitWaitQue(Player player, int tankId, int refitId, int count, int period, int endTime) {
		RefitQue refitQue = new RefitQue(player.maxKey(), tankId, refitId, count, 0, period, endTime);
		return refitQue;
	}

	private RefitQue createRefitQue(Player player, int tankId, int refitId, int count, int period, int endTime) {
		RefitQue refitQue = new RefitQue(player.maxKey(), tankId, refitId, count, 1, period, endTime);
		return refitQue;
	}

	/**
	 * 
	 * Method: speedQue
	 * 
	 * @Description: 加速生产坦克 @param req @param handler @return void @throws
	 */
	public void speedTankQue(SpeedQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		if (!req.hasField(SpeedQueRq.getDescriptor().findFieldByName("which"))) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}
		int keyId = req.getKeyId();
		int cost = req.getCost();
		int which = req.getWhich();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<TankQue> list = (which == 1) ? player.tankQue_1 : player.tankQue_2;
		TankQue que = null;
		for (TankQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		if (que.getState() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.SPEED_WAIT_QUE);
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

			dealTankQue(player, list, now);

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
			if (type != 2) {// 坦克加速
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			PlayerDataManager.getInst().subProp(prop, 1);

			que.setEndTime(que.getEndTime() - speedTime);
			dealTankQue(player, list, now);

			builder.setCount(prop.getCount());
			builder.setEndTime(que.getEndTime());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.SpeedQueRs, builder.build());
			return;
		}
	}

	/**
	 * 
	 * Method: speedRefitQue
	 * 
	 * @Description: 加速改装坦克 @param req @param handler @return void @throws
	 */
	public void speedRefitQue(SpeedQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int keyId = req.getKeyId();
		int cost = req.getCost();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<RefitQue> list = player.refitQue;
		RefitQue que = null;
		for (RefitQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		if (que.getState() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.SPEED_WAIT_QUE);
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

			dealRefitQue(player, list, now);

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
			if (type != 2) {// 坦克加速
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			PlayerDataManager.getInst().subProp(prop, 1);

			que.setEndTime(que.getEndTime() - speedTime);
			dealRefitQue(player, list, now);

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
	 * @Description: 玩家取消生产坦克 @param req @param handler @return void @throws
	 */
	public void cancelTankQue(CancelQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		if (!req.hasField(CancelQueRq.getDescriptor().findFieldByName("which"))) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		int keyId = req.getKeyId();
		int which = req.getWhich();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<TankQue> list = (which == 1) ? player.tankQue_1 : player.tankQue_2;

		TankQue que = null;
		for (TankQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		int tankId = que.getTankId();
		int count = que.getCount();
		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		if (staticTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		list.remove(que);

		Resource resource = player.resource;
		int ironCost = staticTank.getIron() * count / 2;
		int oilCost = staticTank.getOil() * count / 2;
		int copperCost = staticTank.getCopper() * count / 2;
		int siliconCost = staticTank.getSilicon() * count / 2;

		int bookCount = staticTank.getBook() * count / 2;
		int drawingId = staticTank.getDrawing();
		int drawingCount = count / 2;

		if (bookCount > 0) {
			PlayerDataManager.getInst().addProp(player, PropId.SKILL_BOOK, bookCount);
		}

		if (drawingId > 0 && drawingCount > 0) {
			PlayerDataManager.getInst().addProp(player, drawingId, drawingCount);
		}

		CancelQueRs.Builder builder = CancelQueRs.newBuilder();
		if (ironCost > 0) {
			PlayerDataManager.getInst().modifyIron(resource, ironCost);
			builder.setIron(resource.getIron());
		}

		if (oilCost > 0) {
			PlayerDataManager.getInst().modifyOil(resource, oilCost);
			builder.setOil(resource.getOil());
		}

		if (copperCost > 0) {
			PlayerDataManager.getInst().modifyCopper(resource, copperCost);
			builder.setCopper(resource.getCopper());
		}

		if (siliconCost > 0) {
			PlayerDataManager.getInst().modifySilicon(resource, siliconCost);
			builder.setSilicon(resource.getSilicon());
		}

		// resourceDao.updateResource(resource);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CancelQueRs, builder.build());
	}

	/**
	 * 
	 * Method: cancelRefitQue
	 * 
	 * @Description: 玩家取消改装 @param req @param handler @return void @throws
	 */
	public void cancelRefitQue(CancelQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int keyId = req.getKeyId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<RefitQue> list = player.refitQue;
		RefitQue que = null;
		for (RefitQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		int tankId = que.getTankId();
		int refitId = que.getRefitId();
		int count = que.getCount();
		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		ConfTank refitTank = ConfTankProvider.getInst().getConfigById(refitId);
		if (staticTank == null || refitTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		list.remove(que);

		PlayerDataManager.getInst().addTank(player, tankId, count);

		int ironCost = (refitTank.getIron() - staticTank.getIron()) * count / 2;
		int oilCost = (refitTank.getOil() - staticTank.getOil()) * count / 2;
		int copperCost = (refitTank.getCopper() - staticTank.getCopper()) * count / 2;
		int siliconCost = (refitTank.getSilicon() - staticTank.getSilicon()) * count / 2;

		int bookCount = refitTank.getBook() * count / 2;
		int drawingId = refitTank.getDrawing();
		int drawingCount = count / 2;

		if (bookCount > 0) {
			PlayerDataManager.getInst().addProp(player, PropId.SKILL_BOOK, bookCount);
		}

		if (drawingId > 0 && drawingCount > 0) {
			PlayerDataManager.getInst().addProp(player, drawingId, drawingCount);
		}

		Resource resource = player.resource;
		CancelQueRs.Builder builder = CancelQueRs.newBuilder();
		if (ironCost > 0) {
			PlayerDataManager.getInst().modifyIron(resource, ironCost);
			builder.setIron(resource.getIron());
		}

		if (oilCost > 0) {
			PlayerDataManager.getInst().modifyOil(resource, oilCost);
			builder.setOil(resource.getOil());
		}

		if (copperCost > 0) {
			PlayerDataManager.getInst().modifyCopper(resource, copperCost);
			builder.setCopper(resource.getCopper());
		}

		if (siliconCost > 0) {
			PlayerDataManager.getInst().modifySilicon(resource, siliconCost);
			builder.setSilicon(resource.getSilicon());
		}

		// resourceDao.updateResource(resource);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CancelQueRs, builder.build());
	}

	private int buildTankTime(Player player, ConfTank staticTank, int count, int factoryLv) {
		float add = 0;
		if (player.heros.containsKey(HeroId.SHENG_CHAN_GUAN)) {
			add = ConfHeroProvider.getInst().getConfigById(HeroId.SHENG_CHAN_GUAN).getSkillValue() / 100.0f;
		} else if (player.heros.containsKey(HeroId.SHENG_CHAN_BING)) {
			add = ConfHeroProvider.getInst().getConfigById(HeroId.SHENG_CHAN_BING).getSkillValue() / 100.0f;
		}

		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			add += (staticVip.getSpeedTank() / 100.0f);
		}

		// 获取军工科技效率加成的时间
//		int reduceTime = militaryScienceService.caulMilitaryProduceReduceTime(player, staticTank.getTankId());
		int reduceTime = 0;
		float needTime = (float) ((staticTank.getBuildTime() - reduceTime) / (1 + factoryLv * 0.05 + add));
		if (needTime <= 0) {
			needTime = 0f;
		}

		return (int) Math.ceil(needTime * count);
	}

	private int refitTankTime(Player player, int time, int tankId, int count, int factoryLv) {
		float add = 0;
		if (player.heros.containsKey(HeroId.GAI_ZAO_GUAN)) {
			add = ConfHeroProvider.getInst().getConfigById(HeroId.GAI_ZAO_GUAN).getSkillValue() / 100.0f;
		} else if (player.heros.containsKey(HeroId.GAI_ZAO_BING)) {
			add = ConfHeroProvider.getInst().getConfigById(HeroId.GAI_ZAO_BING).getSkillValue() / 100.0f;
		}

		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			add += (staticVip.getSpeedRefit() / 100.0f);
		}

		// 获取军工科技效率加成的时间
//		int reduceTime = MilitaryScienceService.caulMilitaryProduceReduceTime(player, tankId);
		int reduceTime = 0;
		float needTime = (float) ((time - reduceTime) / (1 + factoryLv * 0.05 + add));
		if (needTime <= 0) {
			needTime = 0f;
		}

		return (int) Math.ceil(needTime * count);
	}

	/**
	 * 
	 * Method: buildTank
	 * 
	 * @Description: 生产坦克 @param req @param handler @return void @throws
	 */
	public void buildTank(BuildTankRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		int which = req.getWhich();
		int tankId = req.getTankId();
		int count = req.getCount();
		int factoryLv = 0;
		List<TankQue> tankQue = null;

		if (count <= 0 || count > 100) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		if (staticTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (staticTank.getCanBuild() == 1) {
//			handler.sendErrorMsgToPlayer(GameError.CANT_BUILD);
			return;
		}

		if (which == 1) {// 第一工厂
			which = 1;
			factoryLv = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_1, player.building);
			tankQue = player.tankQue_1;
		} else {// 第二工厂
			which = 2;
			factoryLv = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_2, player.building);
			tankQue = player.tankQue_2;
		}

		if (factoryLv < staticTank.getFactoryLv()) {
//			handler.sendErrorMsgToPlayer(GameError.BUILD_LEVEL);
			return;
		}

		if (player.lord.getLevel() < staticTank.getLordLv()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		Resource resource = player.resource;
		int ironCost = staticTank.getIron() * count;
		int oilCost = staticTank.getOil() * count;
		int copperCost = staticTank.getCopper() * count;
		int siliconCost = staticTank.getSilicon() * count;

		if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
//			handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
			return;
		}

		Prop book = null;
		int needBook = 0;
		if (staticTank.getBook() != 0) {
			book = player.props.get(PropId.SKILL_BOOK);
			needBook = staticTank.getBook() * count;
			if (book == null || book.getCount() < needBook) {
//				handler.sendErrorMsgToPlayer(GameError.BOOK_NOT_ENOUGH);
				return;
			}
		}

		Prop drawing = null;
		if (staticTank.getDrawing() != 0) {
			drawing = player.props.get(staticTank.getDrawing());
			if (drawing == null || drawing.getCount() < count) {
//				handler.sendErrorMsgToPlayer(GameError.DRAWING_NOT_ENOUGH);
				return;
			}
		}

		int queSize = tankQue.size();
		TankQue que = null;
		int now = DateUtil.getSecondTime();
		int haust = buildTankTime(player, staticTank, count, factoryLv);
		if (queSize == 0) {
			que = createTankQue(player, tankId, count, haust, now + haust);
			tankQue.add(que);
		} else {
			if (queSize > 0 && queSize < getTankQueWaitCount(player.lord) + 1) {
				que = createTankWaitQue(player, tankId, count, haust, now + haust);
				tankQue.add(que);
			} else {
//				handler.sendErrorMsgToPlayer(GameError.MAX_TANK_QUE);
				return;
			}
		}

		if (needBook > 0) {
			PlayerDataManager.getInst().subProp(book, needBook);
		}

		if (drawing != null) {
			PlayerDataManager.getInst().subProp(drawing, count);
		}

		BuildTankRs.Builder builder = BuildTankRs.newBuilder();

		if (ironCost > 0) {
			PlayerDataManager.getInst().modifyIron(resource, -ironCost);
			builder.setIron(resource.getIron());
		}

		if (oilCost > 0) {
			PlayerDataManager.getInst().modifyOil(resource, -oilCost);
			builder.setOil(resource.getOil());
		}

		if (copperCost > 0) {
			PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
			builder.setCopper(resource.getCopper());
		}

		if (siliconCost > 0) {
			PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);
			builder.setSilicon(resource.getSilicon());
		}

		builder.setQueue(PbHelper.createTankQuePb(que));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuildTankRs, builder.build());
		LogHelper.logBuildTank(player.lord, que);
	}

	/**
	 * 
	 * Method: refitTank
	 * 
	 * @Description: 改装坦克 @param req @param handler @return void @throws
	 */
	public void refitTank(RefitTankRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		int tankId = req.getTankId();
		int count = req.getCount();

		if (count <= 0 || count > 100) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		if (staticTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (staticTank.getCanRefit() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.CANT_REFIT);
			return;
		}

		int refitId = staticTank.getRefitId();
		ConfTank refitTank = ConfTankProvider.getInst().getConfigById(refitId);
		if (refitTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Tank tank = player.tanks.get(tankId);
		if (tank == null || tank.getCount() < count) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		int refitLv = PlayerDataManager.getBuildingLv(BuildingId.REFIT, player.building);
		int factory1 = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_1, player.building);
		int factory2 = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_2, player.building);
		int factoryLv = (factory1 > factory2) ? factory1 : factory2;

		if (factoryLv < refitTank.getRefitLv()) {
//			handler.sendErrorMsgToPlayer(GameError.BUILD_LEVEL);
			return;
		}

		if (player.lord.getLevel() < refitTank.getLordLv()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		Resource resource = player.resource;
		int ironCost = (refitTank.getIron() - staticTank.getIron()) * count;
		int oilCost = (refitTank.getOil() - staticTank.getOil()) * count;
		int copperCost = (refitTank.getCopper() - staticTank.getCopper()) * count;
		int siliconCost = (refitTank.getSilicon() - staticTank.getSilicon()) * count;

		if (resource.getIron() < ironCost || resource.getOil() < oilCost || resource.getCopper() < copperCost || resource.getSilicon() < siliconCost) {
//			handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
			return;
		}

		int bookCount = refitTank.getBook() * count;
		int drawingId = refitTank.getDrawing();
		int drawingCount = count;

		Prop bookProp = null;
		Prop drawingProp = null;
		if (bookCount > 0) {
			bookProp = player.props.get(PropId.SKILL_BOOK);
			if (bookProp == null || bookProp.getCount() < bookCount) {
//				handler.sendErrorMsgToPlayer(GameError.BOOK_NOT_ENOUGH);
				return;
			}
		}

		if (drawingId > 0 && drawingCount > 0) {
			drawingProp = player.props.get(drawingId);
			if (drawingProp == null || drawingProp.getCount() < drawingCount) {
//				handler.sendErrorMsgToPlayer(GameError.DRAWING_NOT_ENOUGH);
				return;
			}
		}

		List<RefitQue> refitQue = player.refitQue;
		int queSize = refitQue.size();
		RefitQue que = null;
		int now = DateUtil.getSecondTime();
		int refitBaseTime = refitTank.getBuildTime() - staticTank.getBuildTime();
		int haust = refitTankTime(player, refitBaseTime, refitTank.getTankId(), count, refitLv);

		if (queSize == 0) {
			que = createRefitQue(player, tankId, refitId, count, haust, now + haust);
			refitQue.add(que);
		} else {
			if (queSize > 0 && queSize < getTankQueWaitCount(player.lord) + 1) {
				que = createRefitWaitQue(player, tankId, refitId, count, haust, now + haust);
				refitQue.add(que);
			} else {
//				handler.sendErrorMsgToPlayer(GameError.MAX_REFIT_QUE);
				return;
			}
		}

		PlayerDataManager.getInst().subTank(tank, count);

		if (bookCount > 0) {
			PlayerDataManager.getInst().subProp(bookProp, bookCount);
		}

		if (drawingId > 0) {
			PlayerDataManager.getInst().subProp(drawingProp, drawingCount);
		}

		RefitTankRs.Builder builder = RefitTankRs.newBuilder();

		if (ironCost > 0) {
			PlayerDataManager.getInst().modifyIron(resource, -ironCost);
			builder.setIron(resource.getIron());
		}

		if (oilCost > 0) {
			PlayerDataManager.getInst().modifyOil(resource, -oilCost);
			builder.setOil(resource.getOil());
		}

		if (copperCost > 0) {
			PlayerDataManager.getInst().modifyCopper(resource, -copperCost);
			builder.setCopper(resource.getCopper());
		}

		if (siliconCost > 0) {
			PlayerDataManager.getInst().modifySilicon(resource, -siliconCost);
			builder.setSilicon(resource.getSilicon());
		}

		builder.setQueue(PbHelper.createRefitQuePb(que));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.RefitTankRs, builder.build());

		LogHelper.logRefitTank(player.lord, que);
	}

	// public static HashMap<Integer, Tank> cloneTankMap(Map<Integer, Tank> src)
	// {
	// HashMap<Integer, Tank> tanks = new HashMap<>();
	// for (Map.Entry<Integer, Tank> entry : src.entrySet()) {
	// tanks.put(entry.getKey(), (Tank) entry.getValue().clone());
	// }
	// return tanks;
	// }

	/**
	 * 
	 * Method: tankQueTimerLogic
	 * 
	 * @Description: 坦克建造、改装队列定时器逻辑 @return void @throws
	 */
	public void tankQueTimerLogic() {
		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
		int now = DateUtil.getSecondTime();
		while (iterator.hasNext()) {
			boolean completeBuild = false;
			Player player = iterator.next();
			if (!player.isActive()) {
				continue;
			}

			if (!player.tankQue_1.isEmpty()) {
				completeBuild = dealTankQue(player, player.tankQue_1, now);
			}

			if (!player.tankQue_2.isEmpty()) {
				completeBuild = dealTankQue(player, player.tankQue_2, now);
			}

			if (!player.refitQue.isEmpty()) {
				completeBuild = dealRefitQue(player, player.refitQue, now);
			}

			if (completeBuild) {
				PlayerDataManager.getInst().updateFight(player);
			}
		}
	}

}
