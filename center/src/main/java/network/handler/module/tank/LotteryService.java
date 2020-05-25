package network.handler.module.tank;
//package com.game.module;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.ActivityConst;
//import com.game.constant.AwardFrom;
//import com.game.constant.AwardType;
//import com.game.constant.GameError;
//import com.game.constant.GoldCost;
//import com.game.constant.ItemFrom;
//import com.game.constant.LotteryCost;
//import com.game.constant.PropId;
//import com.game.constant.SysChatId;
//import com.game.dataMgr.StaticAwardsDataMgr;
//import com.game.dataMgr.StaticEquipDataMgr;
//import com.game.dataMgr.StaticPropDataMgr;
//import com.game.dataMgr.StaticTankDataMgr;
//import com.game.domain.Player;
//import com.game.domain.p.Award;
//import com.game.domain.p.Lord;
//import com.game.domain.p.LotteryEquip;
//import com.game.domain.p.Prop;
//import com.game.domain.s.StaticEquip;
//import com.game.domain.s.StaticProp;
//import com.game.domain.s.StaticTank;
//import com.game.manager.ActivityDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.GamePb.DoLotteryRq;
//import com.game.pb.GamePb.DoLotteryRs;
//import com.game.pb.GamePb.GetLotteryEquipRs;
//import com.game.pb.GamePb.GetLotteryExploreRs;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.TimeHelper;
//
///**
// * @author ChenKui
// * @version 创建时间：2015-9-7 上午11:12:23
// * @declare
// */
//@Service
//public class LotteryService {
//
//	@Autowired
//	private StaticAwardsDataMgr staticAwardsDataMgr;
//
//	@Autowired
//	private StaticEquipDataMgr staticEquipDataMgr;
//
//	@Autowired
//	private StaticPropDataMgr staticPropDataMgr;
//
//	@Autowired
//	private StaticTankDataMgr staticTankDataMgr;
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private ActivityDataManager activityDataManager;
//
//	@Autowired
//	private ChatService chatService;
//
//	public void GetLotteryEquip(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Map<Integer, LotteryEquip> lotteryEquipMap = player.lotteryEquips;
//		LotteryEquip green = lotteryEquipMap.get(1);
//		LotteryEquip blue = lotteryEquipMap.get(2);
//		LotteryEquip purple = lotteryEquipMap.get(3);
//		GetLotteryEquipRs.Builder builder = GetLotteryEquipRs.newBuilder();
//		int currentTime = TimeHelper.getCurrentSecond();
//		if (green == null) {// 绿色单抽(开局3次免费)
//			green = new LotteryEquip();
//			green.setLotteryId(1);
//			green.setFreetimes(3);// 剩余次数
//			green.setCd(LotteryCost.GREEN_CD);// 倒计时
//			green.setTime(currentTime);
//			lotteryEquipMap.put(1, green);
//		}
//		currentLottery(green, LotteryCost.GREEN_CD, 5);
//		builder.addLotteryEquip(PbHelper.createLotteryEquipPb(green));
//		if (blue == null) {// 蓝色单抽(剩余2小时)
//			blue = new LotteryEquip();
//			blue.setLotteryId(2);
//			blue.setFreetimes(0);
//			int cdTime = 2 * 3600;
//			int passTime = LotteryCost.BLUE_CD - cdTime;
//			int lotteryTime = currentTime - passTime;
//			blue.setCd(cdTime);
//			blue.setTime(lotteryTime);
//			lotteryEquipMap.put(2, blue);
//		}
//		currentLottery(blue, LotteryCost.BLUE_CD, 1);
//		builder.addLotteryEquip(PbHelper.createLotteryEquipPb(blue));
//		if (purple == null) {
//			purple = new LotteryEquip();
//			purple.setLotteryId(3);
//			blue.setFreetimes(0);
//			int cdTime = 1800;
//			int passTime = LotteryCost.PURPLE_CD - cdTime;
//			int lotteryTime = currentTime - passTime;
//			purple.setPurple(0);
//			purple.setCd(cdTime);
//			purple.setTime(lotteryTime);
//			lotteryEquipMap.put(3, purple);
//		}
//		currentLottery(purple, LotteryCost.PURPLE_CD, 1);
//		builder.addLotteryEquip(PbHelper.createLotteryEquipPb(purple));
//		handler.sendMsgToPlayer(GetLotteryEquipRs.ext, builder.build());
//	}
//
//	public void GetLotteryExplore(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		int currentDate = TimeHelper.getCurrentDay();
//		int getTime = lord.getLotterExplore();
//		GetLotteryExploreRs.Builder builder = GetLotteryExploreRs.newBuilder();
//		if (getTime != currentDate) {
//			builder.setSingleFree(1);
//		} else {
//			builder.setSingleFree(0);
//		}
//		handler.sendMsgToPlayer(GetLotteryExploreRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * @param lottery装扮抽取记录
//	 * @param period免费周期时间
//	 * @param frees总免费次数
//	 * @return
//	 */
//	public LotteryEquip currentLottery(LotteryEquip lottery, int period, int frees) {
//		int currentTime = TimeHelper.getCurrentSecond();// 当前系统时间
//		int freetimes = lottery.getFreetimes();// 剩余免费次数
//		if (freetimes < frees) {
//			int lotteryTime = lottery.getTime();// 抽取时间
//			int passSecond = currentTime - lotteryTime;
//			int addFreeTimes = passSecond / period;// 产生免费次数
//			if (addFreeTimes > 0) {
//				freetimes += addFreeTimes;
//				if (freetimes >= frees) {
//					lottery.setFreetimes(frees);
//					lottery.setCd(0);
//					lottery.setTime(currentTime);
//				} else {
//					lottery.setFreetimes(freetimes);
//					lottery.setTime(lotteryTime + addFreeTimes * period);
//				}
//			}
//		}
//
//		// 重新计算CD时间
//		if (frees > 0 && lottery.getFreetimes() < frees) {
//			int passSecond = currentTime - lottery.getTime();
//			lottery.setCd(period - passSecond);
//		}
//		return lottery;
//	}
//
//	public void doLottery(DoLotteryRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		int type = req.getType();
//		List<Award> awardList = null;
//		List<Award> displayAward = null;
//		int isDisplay = 0;
//		int cd = 0;
//		int currentTime = TimeHelper.getCurrentSecond();
//		AwardFrom awardFrom = null;
//		switch (type) {
//		case LotteryCost.WASTE_SMALL: {
//			int huangbao = lord.getHuangbao();
//			if (huangbao < 10) {
//				handler.sendErrorMsgToPlayer(GameError.HUANGBAO_NOT_ENOUGH);
//				return;
//			}
//			awardList = lottery(12);
//			playerDataManager.subHuangbao(lord, 10);
//			awardFrom = AwardFrom.WASTE_SMALL;
//			break;
//		}
//		case LotteryCost.WASTE: {
//			int huangbao = lord.getHuangbao();
//			if (huangbao < 20) {
//				handler.sendErrorMsgToPlayer(GameError.HUANGBAO_NOT_ENOUGH);
//				return;
//			}
//			playerDataManager.subHuangbao(lord, 20);
//			awardList = lottery(13);
//			awardFrom = AwardFrom.WASTE;
//			break;
//		}
//		case LotteryCost.WASTE_LARGE: {
//			int huangbao = lord.getHuangbao();
//			if (huangbao < 40) {
//				handler.sendErrorMsgToPlayer(GameError.HUANGBAO_NOT_ENOUGH);
//				return;
//			}
//			playerDataManager.subHuangbao(lord, 40);
//			awardList = lottery(14);
//			awardFrom = AwardFrom.WASTE_LARGE;
//			break;
//		}
//		case LotteryCost.EXPLORE_SINGLE: {
//			int nowDay = TimeHelper.getCurrentDay();
//			int getTime = lord.getLotterExplore();
//			if (getTime == nowDay) {
//				Map<Integer, Prop> propMap = player.props;
//				Prop prop = propMap.get(PropId.LUCK_COIN);
//				if (prop == null || prop.getCount() < 1) {
//					handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//					return;
//				}
//
//				playerDataManager.subProp(prop, 1);
//			} else {
//				lord.setLotterExplore(nowDay);
//			}
//
//			isDisplay = 1;
//			awardList = lottery(15);
//			displayAward = staticAwardsDataMgr.getDisplay(15);
//			awardFrom = AwardFrom.EXPLORE_SINGLE;
//			break;
//		}
//		case LotteryCost.EXPLORE_THREE: {
//			if (lord.getLevel() < 15) {
//				handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//				return;
//			}
//			Map<Integer, Prop> propMap = player.props;
//			Prop prop = propMap.get(PropId.LUCK_COIN);
//			if (prop == null || prop.getCount() < 3) {
//				handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//				return;
//			}
//			isDisplay = 1;
//			awardList = lottery(16);
//			displayAward = staticAwardsDataMgr.getDisplay(16);
//			playerDataManager.subProp(prop, 3);
//			awardFrom = AwardFrom.EXPLORE_THREE;
//			break;
//		}
//		case LotteryCost.GREEN_SINGLE: {// 绿色单抽
//			Map<Integer, LotteryEquip> lotteryEquipMap = player.lotteryEquips;
//			LotteryEquip lotteryEquip = lotteryEquipMap.get(1);
//			currentLottery(lotteryEquip, LotteryCost.GREEN_CD, 5);
//			int freeTimes = lotteryEquip.getFreetimes();
//			if (freeTimes > 0) {
//				if (freeTimes >= 5) {
//					lotteryEquip.setTime(currentTime);
//				}
//				lotteryEquip.setFreetimes(freeTimes - 1);
//			} else {
//				if (player.lord.getGold() < 20) {
//					handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//					return;
//				}
//				playerDataManager.subGold(lord, 20, GoldCost.LOTTERY_EQUIP_GREEN);
//			}
//			cd = LotteryCost.GREEN_CD - (currentTime - lotteryEquip.getTime());
//			cd = cd < 0 ? 0 : cd;
//			awardList = lottery(17);
//			awardFrom = AwardFrom.GREEN_SINGLE;
//			break;
//		}
//		case LotteryCost.BLUE_SINGLE: {// 蓝色单抽
//			Map<Integer, LotteryEquip> lotteryEquipMap = player.lotteryEquips;
//			LotteryEquip lotteryEquip = lotteryEquipMap.get(2);
//			currentLottery(lotteryEquip, LotteryCost.BLUE_CD, 1);
//			int freeTimes = lotteryEquip.getFreetimes();
//			if (freeTimes > 0) {
//				lotteryEquip.setFreetimes(0);
//				lotteryEquip.setTime(currentTime);
//			} else {
//				if (player.lord.getGold() < 100) {
//					handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//					return;
//				}
//				playerDataManager.subGold(lord, 100, GoldCost.LOTTERY_EQUIP_BLUE);
//			}
//			cd = LotteryCost.BLUE_CD - (currentTime - lotteryEquip.getTime());
//			cd = cd < 0 ? 0 : cd;
//			awardList = lottery(18);
//			awardFrom = AwardFrom.BLUE_SINGLE;
//			break;
//		}
//		case LotteryCost.PURPLE_SINGLE: {// 紫色单抽(首抽得紫色装备)
//			Map<Integer, LotteryEquip> lotteryEquipMap = player.lotteryEquips;
//			LotteryEquip lotteryEquip = lotteryEquipMap.get(3);
//			currentLottery(lotteryEquip, LotteryCost.PURPLE_CD, 1);
//			int freeTimes = lotteryEquip.getFreetimes();
//			if (freeTimes > 0) {
//				lotteryEquip.setFreetimes(0);
//				lotteryEquip.setTime(currentTime);
//			} else {
//				float a = activityDataManager.discountActivity(ActivityConst.ACT_LOTTEY_EQUIP, 0);
//				int cost = (int) (300 * a / 100f);
//				if (player.lord.getGold() < cost) {
//					handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//					return;
//				}
//				playerDataManager.subGold(lord, cost, GoldCost.LOTTERY_EQUIP_PURPLE_SINGLE);
//			}
//			cd = LotteryCost.PURPLE_CD - (currentTime - lotteryEquip.getTime());
//			cd = cd < 0 ? 0 : cd;
//			int pureple = lotteryEquip.getPurple() + 1;
//			pureple = pureple == 1 ? 10 : pureple;
//			if (pureple % 10 == 0) {
//				lotteryEquip.setPurple(pureple);
//				awardList = lottery(21);
//			} else {
//				lotteryEquip.setPurple(pureple);
//				awardList = lottery(19);
//			}
//			awardFrom = AwardFrom.PURPLE_SINGLE;
//			break;
//		}
//		case LotteryCost.PURPLE_NINE: {// 十次必定出一个紫色装备
//			float a = activityDataManager.discountActivity(ActivityConst.ACT_LOTTEY_EQUIP, 1);
//			int cost = (int) (2700 * a / 100f);
//			if (player.lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			awardList = lottery(20);
//			Map<Integer, LotteryEquip> lotteryEquipMap = player.lotteryEquips;
//			LotteryEquip lotteryEquip = lotteryEquipMap.get(3);
//			currentLottery(lotteryEquip, LotteryCost.PURPLE_CD, 1);
//			int purple = lotteryEquip.getPurple();
//			if (purple == 0) {// 首抽必得紫色装备
//				List<Award> purpleAward = lottery(21);
//				int index = RandomHelper.randomInSize(awardList.size());
//				awardList.set(index, purpleAward.get(0));
//			}
//			int p1 = purple / 10;
//			purple += 9;
//			int p2 = purple / 10;
//			if (p1 != p2) {// 累计10次必出一个紫装
//				List<Award> purpleAward = lottery(21);
//				int index = RandomHelper.randomInSize(awardList.size());
//				awardList.set(index, purpleAward.get(0));
//			}
//			playerDataManager.subGold(lord, cost, GoldCost.LOTTERY_EQUIP_PURPLE_TEN);
//			lotteryEquip.setPurple(purple);
//			cd = LotteryCost.PURPLE_CD - (currentTime - lotteryEquip.getTime());
//			cd = cd < 0 ? 0 : cd;
//			awardFrom = AwardFrom.PURPLE_NINE;
//			break;
//		}
//
//		default:
//			break;
//		}
//
//		int serverId = player.account.getServerId();
//
//		DoLotteryRs.Builder builder = DoLotteryRs.newBuilder();
//		for (Award award : awardList) {
//			int keyId = playerDataManager.addAward(player, award.getType(), award.getId(), award.getCount(), awardFrom);
//			builder.addAward(PbHelper.createAwardPb(award.getType(), award.getId(), award.getCount(), keyId));
//			if (award.getType() == AwardType.EQUIP && award.getId() < 701 && award.getId() % 100 == 4) {
//				StaticEquip staticEquip = staticEquipDataMgr.getStaticEquip(award.getId());
//				if (staticEquip != null) {
//					chatService.sendWorldChat(chatService.createSysChat(SysChatId.GET_EQUIP, player.lord.getNick(), staticEquip.getEquipName()));
//					
//					LogHelper.logItem(lord, ItemFrom.DRAW_EQUIP, serverId, award.getType(), award.getId(), 1, String.valueOf(type));
//				}
//			} else if (award.getType() == AwardType.PROP) {
//				if (award.getId() == PropId.COMMAND_BOOK) {
//					StaticProp staticProp = staticPropDataMgr.getStaticProp(award.getId());
//					if (staticProp != null) {
//						chatService.sendWorldChat(chatService.createSysChat(SysChatId.EXPLORE_PROP, player.lord.getNick(), staticProp.getPropName()));
//					}
//				}
//			} else if (award.getType() == AwardType.TANK) {
//				StaticTank staticTank = staticTankDataMgr.getStaticTank(award.getId());
//				if (staticTank != null && staticTank.getGrade() >= 5) {
//					chatService.sendWorldChat(chatService.createSysChat(SysChatId.EXPLORE_TANK, player.lord.getNick(), staticTank.getName()));
//				}
//			}
//		}
//
//		builder.setIsDisplay(isDisplay);
//		if (isDisplay != 0 && displayAward != null) {
//			for (Award award : displayAward) {
//				builder.addDisplayAward(PbHelper.createAwardPb(award.getType(), award.getId(), award.getCount()));
//			}
//		}
//		builder.setCd(cd);
//		builder.setGold(lord.getGold());
//		handler.sendMsgToPlayer(DoLotteryRs.ext, builder.build());
//	}
//
//	public List<Award> lottery(int awardId) {
//		List<Award> rs = new ArrayList<Award>();
//		List<List<Integer>> awardList = staticAwardsDataMgr.getAwards(awardId);
//		for (List<Integer> e : awardList) {
//			Award award = new Award();
//			award.setType(e.get(0));
//			award.setId(e.get(1));
//			award.setCount(e.get(2));
//			rs.add(award);
//		}
//		return rs;
//	}
//
//	public List<Award> display(int awardId) {
//		List<Award> rs = new ArrayList<Award>();
//		return rs;
//	}
//}
