package network.handler.module.tank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.game.domain.Player;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfCost;
import config.bean.ConfHero;
import config.provider.ConfAwardProvider;
import config.provider.ConfCostProvider;
import config.provider.ConfHeroProvider;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Prop;
import data.bean.Resource;
import define.AwardFrom;
import define.AwardType;
import define.GoldCost;
import define.ItemFrom;
import define.PropId;
import define.SysChatId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.HeroManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb;
import pb.GamePb.GetMyHerosRs;
import pb.GamePb.HeroDecomposeRq;
import pb.GamePb.HeroDecomposeRs;
import pb.GamePb.HeroImproveRq;
import pb.GamePb.HeroImproveRs;
import pb.GamePb.HeroLevelUpRq;
import pb.GamePb.HeroLevelUpRs;
import pb.GamePb.LotteryHeroRq;
import pb.GamePb.LotteryHeroRs;
import pb.GamePb.MultiHeroImproveRq;
import pb.GamePb.MultiHeroImproveRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-1 上午11:23:50
 * @declare
 */
@Singleton
public class HeroService implements IModuleMessageHandler {

	public static HeroService getInst() {
		return BeanManager.getBean(HeroService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * Function:获取我的将领数据
	 * 
	 * @param handler
	 */
	public void GetMyHerosRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
		Map<Integer, Hero> heroMap = player.heros;
		GetMyHerosRs.Builder builder = GetMyHerosRs.newBuilder();
		Iterator<Hero> it = heroMap.values().iterator();
		while (it.hasNext()) {
			Hero next = it.next();
			if (next.getCount() <= 0) {
				next.setCount(0);
				continue;
			}
			builder.addHero(PbHelper.createHeroPb(next));
		}
		Lord lord = player.lord;
		int goldTime = lord.getGoldHeroTime();
		int currentDay = DateUtil.getToday();
		if (currentDay != goldTime) {
			builder.setCoinCount(0);
			builder.setResCount(0);
		} else {
			builder.setCoinCount(lord.getGoldHeroCount());
			builder.setResCount(lord.getStoneHeroCount());
		}
		MsgHelper.sendResponse(ctx, packet.getPlayerId(), ResponseCode.HERO_RS, builder.build());
	}

	/**
	 * Function:武将分解
	 * 
	 * @param req
	 * @param handler
	 */
	public void heroDecompose(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		HeroDecomposeRq req = msg.get();
		long playerId = packet.getPlayerId();
		int type = req.getType();
		int id = req.getId();
		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		int serverId = player.account.getServerId();

		HeroDecomposeRs.Builder builder = HeroDecomposeRs.newBuilder();
		Map<Integer, Hero> heroMap = player.heros;
		List<List<Integer>> rewardsList = new ArrayList<List<Integer>>();
		if (type == 1) {
			Hero hero = heroMap.get(id);
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}
			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			rewardsList = ConfAwardProvider.getInst().getAwards(staticHero.getResolveId());
			if (hero.getCount() > 1) {
				hero.setCount(hero.getCount() - 1);
			} else {
				heroMap.remove(id);
			}
			LogHelper.logItem(player.lord, ItemFrom.DECOMPOSE_HERO, serverId, AwardType.HERO, hero.getHeroId(), -1, "1");
		} else {
			Iterator<Hero> it = heroMap.values().iterator();
			while (it.hasNext()) {
				Hero next = it.next();
				ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(next.getHeroId());
				if (staticHero == null) {
					continue;
				}
				if (staticHero.getStar() == id) {// 星级相同
					int hcount = next.getCount();
					if (hcount <= 0) {
						continue;
					}
					List<List<Integer>> entity = ConfAwardProvider.getInst().getAwards(staticHero.getResolveId());
					for (List<Integer> dropOne : entity) {
						if (dropOne.size() != 3) {
							continue;
						}
						int count = dropOne.get(2);
						dropOne.set(2, count * hcount);
					}
					rewardsList.addAll(entity);
					it.remove();
					LogHelper.logItem(player.lord, ItemFrom.DECOMPOSE_HERO, serverId, AwardType.HERO, next.getHeroId(), -next.getCount(), "2");
				}
			}
		}

		for (int i = 0; i < rewardsList.size(); i++) {
			List<Integer> li = rewardsList.get(i);
			if (li.size() != 3) {
				continue;
			}
			int itemType = li.get(0);
			int itemId = li.get(1);
			int itemCount = li.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, itemType, itemId, itemCount, AwardFrom.HERO_DECOMPOSE);
			builder.addAward(PbHelper.createAwardPb(itemType, itemId, itemCount, keyId));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.HERO_DECOMPOSE_RS, builder.build());
	}

	/**
	 * Function:武将升级
	 * 
	 * @param req
	 * @param handler
	 */
	public void heroLevelUp(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		HeroLevelUpRq req = msg.get();
		int keyId = req.getKeyId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Hero> heroMap = player.heros;
		Hero hero = heroMap.get(keyId);
		if (hero == null) {
//			handler.sendErrorMsgToPlayer(GameError.HERO_CHIP_NOT_ENOUGH);
			return;
		}

		int heroId = hero.getHeroId();
		ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(heroId);
		if (staticHero == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		int canupHeroId = staticHero.getCanup();
		if (canupHeroId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.HERO_CANT_UP);
			return;
		}

		ConfHero staticHeroUp = ConfHeroProvider.getInst().getConfigById(canupHeroId);
		if (staticHeroUp == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		List<List<Integer>> needMeteList = staticHero.getMeta();
		for (int i = 0; i < needMeteList.size(); i++) {
			List<Integer> ll = needMeteList.get(i);
			int id = ll.get(1);
			int count = ll.get(2);
			Prop prop = player.props.get(id);
			if (prop == null || prop.getCount() < count) {
//				handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
				return;
			}
		}

		for (int i = 0; i < needMeteList.size(); i++) {
			List<Integer> ll = needMeteList.get(i);
			int id = ll.get(1);
			int count = ll.get(2);
			Prop prop = player.props.get(id);
			PlayerDataManager.getInst().subProp(prop, count);
		}

		HeroManager.getInst().addHero(player, canupHeroId, 1);
		HeroManager.getInst().addHero(player, heroId, -1);
		Hero upHero = player.heros.get(staticHero.getCanup());
		HeroLevelUpRs.Builder builder = HeroLevelUpRs.newBuilder();
		builder.setHero(PbHelper.createHeroPb(upHero));

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.HERO_LEVE_LUP_RS, builder.build());

		if (staticHero != null && staticHero.getStar() > 3) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.UPGRADE_HERO, player.lord.getNick(), String.valueOf(heroId)));
		}
	}

	/**
	 * Function:武将升阶
	 * 
	 * @param req
	 * @param handler
	 */
	public void heroImprove(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		HeroImproveRq req = msg.get();
		long playerId = packet.getPlayerId();
		List<CommonPb.Hero> heroList = req.getHeroList();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Hero> heroMap = player.heros;
		int count = 0;
		int star = 0;
		Map<Integer, Integer> costheroMap = new HashMap<Integer, Integer>();
		for (CommonPb.Hero e : heroList) {
			int heroId = e.getHeroId();
			Integer heroCount = costheroMap.get(heroId);
			if (heroCount == null) {
				costheroMap.put(heroId, e.getCount());
			} else {
				heroCount += e.getCount();
				costheroMap.put(heroId, heroCount);
			}
		}

		Iterator<Entry<Integer, Integer>> it = costheroMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> next = it.next();
			int heroId = next.getKey();
			int heroCount = next.getValue();

			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(heroId);
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
				return;
			}

			Hero hero = heroMap.get(heroId);
			if (hero == null || hero.getCount() <= 0 || hero.getCount() < heroCount) {
//				handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
				return;
			}

			if (star == 0) {
				star = staticHero.getStar();
			}
			if (staticHero.getStar() != star) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
				return;
			}

			count += heroCount;
		}

		if (count != 6) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		if (star >= 5) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		List<ConfHero> listHero = ConfHeroProvider.getInst().getStarListLv(star + 1);
		if (listHero == null || listHero.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		ConfHero staticHero = null;
		int seeds[] = { 0, 0 };
		for (ConfHero e : listHero) {
			seeds[0] += e.getProbability();
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (ConfHero e : listHero) {
			seeds[1] += e.getProbability();
			if (seeds[0] <= seeds[1]) {
				staticHero = e;
				break;
			}
		}

		if (staticHero == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		int needSoul = ConfHeroProvider.getInst().costSoul(star);
		Prop prop = player.props.get(PropId.HERO_STONE);
		if (prop == null || prop.getCount() < needSoul) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}
		int serverId = player.account.getServerId();

		int newHeroId = staticHero.getHeroId();
		for (CommonPb.Hero e : heroList) {
			HeroManager.getInst().addHero(player, e.getHeroId(), -e.getCount());
			if (staticHero.getStar() >= 3) {
				LogHelper.logItem(player.lord, ItemFrom.DRAW_HERO, serverId, AwardType.HERO, e.getHeroId(), -e.getCount(), "sig");
			}
		}

		PlayerDataManager.getInst().subProp(prop, needSoul);
		HeroManager.getInst().addHero(player, newHeroId, 1);

		Hero upHero = player.heros.get(newHeroId);
		HeroImproveRs.Builder builder = HeroImproveRs.newBuilder();
		builder.setHero(PbHelper.createHeroPb(upHero));
		TaskManager.getInst().updTask(player, TaskType.COND_HERO_UP, 1, null);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.HERO_IMPROVE_RS, builder.build());
		if (staticHero != null && staticHero.getStar() > 3) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.IMPROVE_HERO, player.lord.getNick(), String.valueOf(newHeroId)));
			LogHelper.logItem(player.lord, ItemFrom.DRAW_HERO, serverId, AwardType.HERO, staticHero.getHeroId(), 1, "sigln");
		}
	}

	/**
	 * Function:多武将武将升阶
	 * 
	 * @param req
	 * @param handler
	 */
	public void multiHeroImproveRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		MultiHeroImproveRq req = msg.get();
		List<CommonPb.Hero> heroList = req.getHeroList();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Map<Integer, Hero> heroMap = player.heros;
		int count = 0;
		int star = 0;
		Map<Integer, Integer> costheroMap = new HashMap<Integer, Integer>();
		for (CommonPb.Hero e : heroList) {
			int heroId = e.getHeroId();
			Integer heroCount = costheroMap.get(heroId);
			if (heroCount == null) {
				costheroMap.put(heroId, e.getCount());
			} else {
				heroCount += e.getCount();
				costheroMap.put(heroId, heroCount);
			}
		}

		Iterator<Entry<Integer, Integer>> it = costheroMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> next = it.next();
			int heroId = next.getKey();
			int heroCount = next.getValue();

			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(heroId);
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			Hero hero = heroMap.get(heroId);
			if (hero == null || hero.getCount() <= 0 || hero.getCount() < heroCount) {
//				handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
				return;
			}

			if (star == 0) {
				star = staticHero.getStar();
			}
			if (staticHero.getStar() != star) {
//				handler.sendErrorMsgToPlayer(GameError.HERO_STAR_NOT_SAME);
				return;
			}

			count += heroCount;
		}

		if (count % 6 != 0) {
//			handler.sendErrorMsgToPlayer(GameError.COUNT_ERROR);
			return;
		}

		if (star >= 5) {
//			handler.sendErrorMsgToPlayer(GameError.HERO_STAR_ERROR);
			return;
		}

		List<ConfHero> listHero = ConfHeroProvider.getInst().getStarListLv(star + 1);
		if (listHero == null || listHero.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		List<ConfHero> awardList = new ArrayList<ConfHero>();
		int repeat = count / 6;
		for (int i = 0; i < repeat; i++) {
			ConfHero staticHero = null;
			int seeds[] = { 0, 0 };
			for (ConfHero e : listHero) {
				seeds[0] += e.getProbability();
			}
			seeds[0] = RandomHelper.randomInSize(seeds[0]);
			for (ConfHero e : listHero) {
				seeds[1] += e.getProbability();
				if (seeds[0] <= seeds[1]) {
					staticHero = e;
					break;
				}
			}

			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}
			awardList.add(staticHero);
		}

		int needSoul = ConfHeroProvider.getInst().costSoul(star) * repeat;
		Prop prop = player.props.get(PropId.HERO_STONE);
		if (prop == null || prop.getCount() < needSoul) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}

		int serverId = player.account.getServerId();

		for (CommonPb.Hero e : heroList) {
			int heroId = e.getHeroId();
			int heroCount = e.getCount();
			HeroManager.getInst().addHero(player, e.getHeroId(), -e.getCount());
			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(e.getHeroId());
			if (staticHero.getStar() >= 3) {
				LogHelper.logItem(player.lord, ItemFrom.DRAW_HERO, serverId, AwardType.HERO, heroId, -heroCount, "multi");
			}
		}

		PlayerDataManager.getInst().subProp(prop, needSoul);

		MultiHeroImproveRs.Builder builder = MultiHeroImproveRs.newBuilder();
		for (ConfHero e : awardList) {
			int newHeroId = e.getHeroId();
			HeroManager.getInst().addHero(player, newHeroId, 1);
			Hero upHero = player.heros.get(newHeroId);
			builder.addHero(PbHelper.createHeroPb(upHero));
			TaskManager.getInst().updTask(player, TaskType.COND_HERO_UP, 1, null);
//			activityDataManager.heroImprove(player, star + 1);
			if (e != null && e.getStar() > 3) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.IMPROVE_HERO, player.lord.getNick(), String.valueOf(newHeroId)));
				LogHelper.logItem(player.lord, ItemFrom.DRAW_HERO, serverId, AwardType.HERO, newHeroId, 1, "multi");
			}
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MULTI_HERO_IMPROVE_RS, builder.build());

	}

	/**
	 * Function：招募武将
	 * 
	 * @param req
	 * @param handler
	 */
	public void LotteryHero(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		LotteryHeroRq req = msg.get();
		long playerId = packet.getPlayerId();
		int type = req.getType();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Lord lord = player.lord;
		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		int awardId = 8;
		Resource resource = player.resource;
		int costStone = 0;
		int costGold = 0;
		int goldHeroCount = lord.getGoldHeroCount();
		int stoneHeroCount = lord.getStoneHeroCount();
		int goldTime = lord.getGoldHeroTime();
		int currentDay = DateUtil.getToday();
		if (goldTime != currentDay) {
			goldHeroCount = 0;
			stoneHeroCount = 0;
			lord.setGoldHeroCount(0);
			lord.setStoneHeroCount(0);
			lord.setGoldHeroTime(currentDay);
			lord.setStoneHeroTime(currentDay);
		}
		// 1 资源1次 2 资源5次 3 金币1次 4 金币5次
		if (type == 1) {
			ConfCost staticCost = ConfCostProvider.getInst().getCost(2, stoneHeroCount + 1);
//			float a = activityDataManager.discountActivity(ActivityConst.ACT_ENLARGE, 2);
			costStone = staticCost.getPrice();
//			costStone *= a / 100f;
			if (resource.getStone() < costStone || costStone == 0) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
				return;
			}
			lord.setStoneHeroCount(stoneHeroCount + 1);
		} else if (type == 2) {
			ConfCost staticCost = ConfCostProvider.getInst().getCost(2, stoneHeroCount + 1, 5);
			costStone = staticCost.getPrice();
			if (resource.getStone() < costStone || costStone == 0) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
				return;
			}
			awardId = 10;
			lord.setStoneHeroCount(stoneHeroCount + 5);
		} else if (type == 3) {
			ConfCost staticCost = ConfCostProvider.getInst().getCost(1, goldHeroCount + 1);
			costGold = staticCost.getPrice();
			if (lord.getGold() < costGold || costGold == 0) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
			awardId = 9;
			lord.setGoldHeroCount(goldHeroCount + 1);
		} else if (type == 4) {
			ConfCost staticCost = ConfCostProvider.getInst().getCost(1, goldHeroCount + 1, 5);
			costGold = staticCost.getPrice();
			if (lord.getGold() < costGold || costGold == 0) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
			awardId = 11;
			lord.setGoldHeroCount(goldHeroCount + 5);
		}

		if (costStone > 0) {
			resource.setStone(resource.getStone() - costStone);
		}
		if (costGold > 0) {
			PlayerDataManager.getInst().subGold(lord, costGold, GoldCost.LOTTERY_HERO);
		}

		int serverId = player.account.getServerId();

		List<List<Integer>> rewardList = ConfAwardProvider.getInst().getAwards(awardId);
		LotteryHeroRs.Builder builder = LotteryHeroRs.newBuilder();
		int size = rewardList.size();
		for (int i = 0; i < size; i++) {// 添加武将数据
			List<Integer> reward = rewardList.get(i);
			int itemType = reward.get(0);
			int heroId = reward.get(1);
			int count = reward.get(2);
			PlayerDataManager.getInst().addAward(player, itemType, heroId, count, AwardFrom.LOTTERY_HERO);
			Hero hero = new Hero(heroId, heroId, count);
			builder.addHero(PbHelper.createHeroPb(hero));

			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(heroId);
			if (staticHero != null && staticHero.getStar() >= 3) {
				LogHelper.logItem(player.lord, ItemFrom.BUY_HERO, serverId, AwardType.HERO, heroId, count, "0");
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.LOTTERY_HERO, player.lord.getNick(), String.valueOf(heroId)));
			}
		}
		builder.setStone(resource.getStone());
		builder.setGold(lord.getGold());
		TaskManager.getInst().updTask(player, TaskType.COND_HERO_LOTTERY, 1, null);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.LOTTERY_HERO_RS, builder.build());
	}
}
