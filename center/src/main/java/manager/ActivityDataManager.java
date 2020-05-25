package manager;
//package com.game.manager;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//
//
//import com.game.constant.ActivityConst;
//import com.game.constant.AwardType;
//import com.game.constant.MailType;
//import com.game.dataMgr.StaticActivityDataMgr;
//import com.game.dataMgr.StaticTankDataMgr;
//import com.game.domain.ActivityBase;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.UsualActivityData;
//import com.game.domain.p.Activity;
//import com.game.domain.p.Cash;
//import com.game.domain.p.Grab;
//import com.game.domain.p.Lord;
//import com.game.domain.p.PartResolve;
//import com.game.domain.p.PartyLvRank;
//import com.game.domain.p.Prop;
//import com.game.domain.p.RptTank;
//import com.game.domain.p.UsualActivity;
//import com.game.domain.s.StaticActAward;
//import com.game.domain.s.StaticActCourse;
//import com.game.domain.s.StaticActDestory;
//import com.game.domain.s.StaticActExchange;
//import com.game.domain.s.StaticActProfoto;
//import com.game.domain.s.StaticActRebate;
//import com.game.domain.s.StaticEquip;
//import com.game.domain.s.StaticTank;
//import com.game.pb.CommonPb;
//import com.game.util.DateHelper;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.TimeHelper;
//import com.google.protobuf.InvalidProtocolBufferException;
//
//
//public class ActivityDataManager {
//
////	@Autowired
////	private ActivityDao activityDao;
//
//	@Autowired
//	private RankDataManager rankDataManager;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private StaticActivityDataMgr staticActivityDataMgr;
//
//	@Autowired
//	private StaticTankDataMgr staticTankDataMgr;
//
//	private Map<Integer, UsualActivityData> activityMap = new HashMap<Integer, UsualActivityData>();
//
//	@PostConstruct
//	public void init() throws InvalidProtocolBufferException {
//		iniUsualActivity();
//	}
//
//	public void iniUsualActivity() throws InvalidProtocolBufferException {
//		List<UsualActivity> list = activityDao.selectUsualActivity();
//		if (list != null) {
//			for (UsualActivity e : list) {
//				UsualActivityData usualActivity = new UsualActivityData(e);
//				activityMap.put(e.getActivityId(), usualActivity);
//			}
//		}
//	}
//
//	public Map<Integer, UsualActivityData> getActivityMap() {
//		return activityMap;
//	}
//
//	public void updateActivityData(UsualActivity activity) {
//		activityDao.updateActivity(activity);
//	}
//
//	/**
//	 * Function:活动信息通用部分,开启,记录重置
//	 * 
//	 * @param player
//	 * @param activityId
//	 * @param handler
//	 * @return
//	 */
//	public Activity getActivityInfo(Player player, int activityId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			return null;
//		}
//		Date beginTime = activityBase.getBeginTime();
//		int begin = TimeHelper.getDay(beginTime);
//		Activity activity = player.activitys.get(activityId);
//		if (activity == null) {
//			activity = new Activity(activityBase, begin);
//			refreshStatus(activity);
//			player.activitys.put(activityId, activity);
//		} else {
//			activity.isReset(begin);// 是否重新设置活动
//			activity.autoDayClean(activityBase);
//		}
//		activity.setOpen(activityBase.getBaseOpen());
//		return activity;
//	}
//
//	/**
//	 * Function:活动信息通用部分,开启,记录重置
//	 * 
//	 * @param player
//	 * @param activityId
//	 * @param handler
//	 * @return
//	 */
//	public Activity getActivityInfo(PartyData partyData, int activityId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			return null;
//		}
//		Date beginTime = activityBase.getBeginTime();
//		int begin = TimeHelper.getDay(beginTime);
//		Activity activity = partyData.getActivitys().get(activityId);
//		if (activity == null) {
//			activity = new Activity(activityBase, begin);
//			refreshStatus(activity);
//			partyData.getActivitys().put(activityId, activity);
//		} else {
//			activity.isReset(begin);// 是否重新设置活动
//			activity.autoDayClean(activityBase);// 自动每日清理
//		}
//		activity.setOpen(activityBase.getBaseOpen());
//		return activity;
//	}
//
//	public UsualActivityData getUsualActivity(int activityId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			return null;
//		}
//		int open = activityBase.getBaseOpen();
//		Date beginTime = activityBase.getBeginTime();
//		int begin = TimeHelper.getDay(beginTime);
//		UsualActivityData activity = activityMap.get(activityId);
//		if (activity == null) {
//			activity = new UsualActivityData(activityBase, begin);
//			activityMap.put(activityId, activity);
//		} else {
//			activity.isReset(begin);
//			activity.autoDayClean(activityBase);
//		}
//		activity.setOpen(open);
//		return activity;
//	}
//
//	/**
//	 * 更新玩家活动记录
//	 * 
//	 * @param player
//	 * @param activityId
//	 * @param schedule
//	 */
//	public void updActivity(Player player, int activityId, long schedule, int sortId) {
//		try {
//			ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//			if (activityBase == null) {
//				return;
//			}
//			int step = activityBase.getStep();
//			if (step != ActivityConst.OPEN_STEP) {
//				return;
//			}
//			Date beginTime = activityBase.getBeginTime();
//			int begin = TimeHelper.getDay(beginTime);
//			Activity activity = player.activitys.get(activityId);
//			if (activity == null) {
//				activity = new Activity(activityBase, begin);
//				refreshStatus(activity);
//				player.activitys.put(activityId, activity);
//				activity.setEndTime(TimeHelper.getCurrentDay());
//			} else {
//				activity.isReset(begin);// 是否重新设置活动
//				activity.autoDayClean(activityBase);
//			}
//			long state = activity.getStatusList().get(sortId);
//			state = state + schedule;
//			activity.getStatusList().set(sortId, state);
//		} catch (Exception e) {
//			LogHelper.ERROR_LOGGER.error("Activity Exception : " + activityId, e);
//		}
//	}
//
//	/**
//	 * Function:紫装升级
//	 * 
//	 * @param player
//	 * @param add
//	 * @return
//	 */
//	public void purpleEquipUp(Player player, StaticEquip staticEquip, int lv, int toLv) {
//		if (staticEquip.getQuality() < 4) {
//			return;
//		}
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PURPLE_UP);
//		if (activityBase == null) {
//			return;
//		}
//		List<StaticActAward> list = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_PURPLE_UP);
//		for (StaticActAward e : list) {
//			int plv = Integer.valueOf(e.getParam().trim());
//			if (plv > lv && plv <= toLv) {
//				int sortId = e.getSortId();
//				updActivity(player, ActivityConst.ACT_PURPLE_UP, 1, sortId);
//			}
//		}
//	}
//
//	/**
//	 * Function:紫装升级
//	 * 
//	 * @param player
//	 * @param add
//	 * @return
//	 */
//	public void ActCostGold(Lord lord, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_COST_GOLD);
//		if (activityBase == null) {
//			return;
//		}
//		Player player = playerDataManager.getPlayer(lord.getLordId());
//		updActivity(player, ActivityConst.ACT_COST_GOLD, gold, 0);
//	}
//
//	/**
//	 * 升装暴击,喂装备额外增加50%经验
//	 * 
//	 * @param exp
//	 * @return
//	 */
//	public int upEquipExp(int exp) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_UP_EQUIP_CRIT);
//		if (activityBase == null) {
//			return exp;
//		}
//		exp = (int) (exp * 1.5f);
//		return exp;
//	}
//
//	/**
//	 * 关卡拦截
//	 * 
//	 * @return
//	 */
//	public Prop combatCourse(int sectionId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_COMBAT_INTER);
//		if (activityBase == null) {
//			return null;
//		}
//		StaticActCourse course = staticActivityDataMgr.getActCourse(activityBase.getKeyId(), sectionId);
//		if (course == null) {
//			return null;
//		}
//		int seeds[] = { 0, 0 };
//		seeds[0] = RandomHelper.randomInSize(course.getDeno());
//		for (List<Integer> elist : course.getDropList()) {
//			if (elist.size() < 4) {
//				continue;
//			}
//			seeds[1] += elist.get(3);
//			if (seeds[0] <= seeds[1]) {
//				Prop prop = new Prop(elist.get(1), elist.get(2));
//				return prop;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 装配兑换关卡掉落
//	 * 
//	 * @param sectionId
//	 * @return
//	 */
//	public Prop combatCash(int activityId, int sectionId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			return null;
//		}
//		StaticActCourse course = staticActivityDataMgr.getActCourse(activityBase.getKeyId(), sectionId);
//		if (course == null) {
//			course = staticActivityDataMgr.getActCourse(activityBase.getKeyId(), 0);
//		}
//		if (course == null) {
//			return null;
//		}
//		int seeds[] = { 0, 0 };
//		seeds[0] = RandomHelper.randomInSize(course.getDeno());
//		for (List<Integer> elist : course.getDropList()) {
//			if (elist.size() < 4) {
//				continue;
//			}
//			seeds[1] += elist.get(3);
//			if (seeds[0] <= seeds[1]) {
//				Prop prop = new Prop(elist.get(1), elist.get(2));
//				return prop;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 资源采集活动
//	 * 
//	 * @param player
//	 * @param activityId
//	 * @param grab
//	 */
//	public void resourceCollect(Player player, int activityId, Grab grab) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_COLLECT_RESOURCE);
//		if (activityBase == null) {
//			return;
//		}
//		// 资源采集活动（数据填错位,讲错就错的方式处理）
//		long iron = grab.rs[0];
//		long oil = grab.rs[1];
//		long copper = grab.rs[2];
//		long silicon = grab.rs[3];
//		long stone = grab.rs[4];
//		if (iron > 0)
//			updActivity(player, ActivityConst.ACT_COLLECT_RESOURCE, grab.rs[0], 1);// 铁
//		if (oil > 0)
//			updActivity(player, ActivityConst.ACT_COLLECT_RESOURCE, grab.rs[1], 2);// 石油
//		if (copper > 0)
//			updActivity(player, ActivityConst.ACT_COLLECT_RESOURCE, grab.rs[2], 3);// 铜
//		if (silicon > 0)
//			updActivity(player, ActivityConst.ACT_COLLECT_RESOURCE, grab.rs[3], 4);// 钛
//		if (stone > 0)
//			updActivity(player, ActivityConst.ACT_COLLECT_RESOURCE, grab.rs[4], 0);// 水晶
//	}
//
//	/**
//	 * 勤劳致富
//	 * 
//	 * @param player
//	 * @param activityId
//	 * @param grab
//	 */
//	public void beeCollect(Player player, int activityId, Grab grab) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_BEE_ID);
//		if (activityBase == null) {
//			return;
//		}
//
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return;
//		}
//
//		UsualActivityData usualActivityData = getUsualActivity(ActivityConst.ACT_BEE_ID);
//		if (usualActivityData == null) {
//			return;
//		}
//		// 勤劳致富活动
//		long iron = grab.rs[0];
//		long oil = grab.rs[1];
//		long copper = grab.rs[2];
//		long silicon = grab.rs[3];
//		long stone = grab.rs[4];
//
//		long lordId = player.lord.getLordId();
//		int rankType = -1;// 排名类型0铁,1石油,2铜,3钛,4水晶
//		long rankValue = 0;
//		if (iron > 0) {
//			rankType = 0;
//			updActivity(player, ActivityConst.ACT_BEE_ID, iron, 0);// 铁
//			Activity activity = player.activitys.get(ActivityConst.ACT_BEE_ID);
//			rankValue = activity.getStatusList().get(0);
//		} else if (oil > 0) {
//			rankType = 1;
//			updActivity(player, ActivityConst.ACT_BEE_ID, oil, 1);// 石油
//			Activity activity = player.activitys.get(ActivityConst.ACT_BEE_ID);
//			rankValue = activity.getStatusList().get(1);
//		} else if (copper > 0) {
//			rankType = 2;
//			updActivity(player, ActivityConst.ACT_BEE_ID, copper, 2);// 铜
//			Activity activity = player.activitys.get(ActivityConst.ACT_BEE_ID);
//			rankValue = activity.getStatusList().get(2);
//		} else if (silicon > 0) {
//			rankType = 3;
//			updActivity(player, ActivityConst.ACT_BEE_ID, silicon, 3);// 钛
//			Activity activity = player.activitys.get(ActivityConst.ACT_BEE_ID);
//			rankValue = activity.getStatusList().get(3);
//		} else if (stone > 0) {
//			rankType = 4;
//			updActivity(player, ActivityConst.ACT_BEE_ID, stone, 4);// 水晶
//			Activity activity = player.activitys.get(ActivityConst.ACT_BEE_ID);
//			rankValue = activity.getStatusList().get(4);
//		}
//		if (rankType == -1 || rankValue <= 0) {
//			return;
//		}
//		// 更新排名
//		usualActivityData.addPlayerRank(lordId, rankType, rankValue, ActivityConst.RANK_BEE, ActivityConst.DESC);
//	}
//
//	/**
//	 * Function:疯狂进阶
//	 * 
//	 * @param player
//	 * @param add
//	 * @return
//	 */
//	public void heroImprove(Player player, int toStar) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CRAZY_HERO);
//		if (activityBase == null) {
//			return;
//		}
//		List<StaticActAward> list = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_CRAZY_HERO);
//		for (StaticActAward e : list) {
//			int pstar = Integer.valueOf(e.getParam().trim());
//			if (pstar == toStar) {
//				int sortId = e.getSortId();
//				updActivity(player, ActivityConst.ACT_CRAZY_HERO, 1, sortId);
//			}
//		}
//	}
//
//	/**
//	 * Function:装备探险免费次数(每天免费5次)
//	 * 
//	 * @param player
//	 * @param add
//	 * @return
//	 */
//	public int freeLotEquip() {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_LOT_EQUIP);
//		if (activityBase == null) {
//			return 0;
//		}
//		return 5;
//	}
//
//	/**
//	 * Function:配件探险免费次数(每天免费5次)
//	 * 
//	 * @param player
//	 * @param add
//	 * @return
//	 */
//	public int freeLotPart() {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_LOT_PART);
//		if (activityBase == null) {
//			return 0;
//		}
//		return 5;
//	}
//
//	/**
//	 * 捐献金币折扣
//	 * 
//	 * @return
//	 */
//	public float discountDonate(int resourceId) {
//		if (resourceId != 6) {
//			return 100f;
//		}
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_DONATE_RE);
//		if (activityBase == null) {
//			return 100f;
//		}
//		return 40f;
//	}
//
//	/**
//	 * 打折活动
//	 * 
//	 * @param activityId
//	 * @param sortId
//	 * @return
//	 */
//	public float discountActivity(int activityId, int sortId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			return 100f;
//		}
//		switch (activityId) {
//		case ActivityConst.ACT_PART_EVOLVE:// 配件失败扣费85%
//			return 85f;
//		case ActivityConst.ACT_ENLARGE: {// 金币：单次8折，五次7折。宝石：单次6折，五次5折
//			if (sortId == 0) {
//				return 80f;
//			} else if (sortId == 1) {
//				return 70f;
//			} else if (sortId == 2) {
//				return 60f;
//			} else if (sortId == 3) {
//				return 50f;
//			}
//		}
//		case ActivityConst.ACT_LOTTEY_EQUIP:// 紫色单抽8折，紫色九抽7折
//			if (sortId == 0) {
//				return 80f;
//			} else if (sortId == 1) {
//				return 70f;
//			}
//		case ActivityConst.ACT_EQUIP_FEED:// 装备补给5折
//			return 50f;
//		default:
//			break;
//		}
//		return 100f;
//	}
//
//	public boolean partEvolve() {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_EVOLVE);
//		if (activityBase == null) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 装备补给，活动开启攻打装备关卡增加30%伤害
//	 * 
//	 * @return
//	 */
//	public float equipFeed() {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EQUIP_FEED);
//		if (activityBase == null) {
//			return 100f;
//		}
//		return 130f;
//	}
//
//	/**
//	 * 全民狂欢
//	 * 
//	 * @return index0统帅成功率index1资源点资源和关卡的经验index2资源点和道具掉落率
//	 */
//	public int[] revelry() {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_REVELRY);
//		if (activityBase == null) {
//			return new int[] { 0, 0, 0 };
//		}
//		// 统帅10%值填写100
//		return new int[] { 100, 20, 30 };
//	}
//
//	/**
//	 * 连续充值
//	 * 
//	 * @param gold
//	 * @return
//	 */
//	public void payContinue(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CONTU_PAY);
//		if (activityBase == null) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_CONTU_PAY);
//		if (activity == null) {
//			return;
//		}
//		int serverId = player.account.getServerId();
//		Date now = new Date();
//		Date beginTime = activityBase.getBeginTime();
//		StaticActAward staticActAward = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_CONTU_PAY).get(0);
//		if (gold >= Integer.parseInt(staticActAward.getParam())) {
//			List<Long> statusList = activity.getStatusList();
//
//			int dayiy = DateHelper.dayiy(beginTime, now);
//			if (dayiy > 7) {
//				return;
//			}
//			for (int i = 0; i < dayiy; i++) {
//				long v = statusList.get(i).longValue();
//				if (i < dayiy - 1 && v == 0) {
//					break;
//				}
//				if (i == dayiy - 1 && v == 0) {
//					statusList.set(i, (long) gold);
//				}
//			}
//
//			Lord lord = player.lord;
//			if (lord != null) {
//				int addGold = gold / 10;
//				List<CommonPb.Award> awards = new ArrayList<CommonPb.Award>();
//				awards.add(PbHelper.createAwardPb(AwardType.GOLD, 0, addGold));
//				playerDataManager.sendAttachMail(player, awards, MailType.MOLD_ACT_2, TimeHelper.getCurrentSecond(), String.valueOf(addGold));
//				LogHelper.logActivity(lord, ActivityConst.LOG_PAY_CONTINUE, 0, AwardType.GOLD, 0, addGold, serverId);
//			}
//		}
//	}
//
//	/**
//	 * 每日首笔充值
//	 * 
//	 * @param gold
//	 * @return
//	 */
//	public void reFirstPay(Player player, int gold) {
//		int plat = player.account.getPlatNo();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RE_FRIST_PAY, plat);
//		if (activityBase == null) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_RE_FRIST_PAY);
//		if (activity == null) {
//			return;
//		}
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(activityBase.getKeyId());
//		StaticActAward actAward = null;
//		for (int i = 0; i < condList.size(); i++) {
//			StaticActAward en = condList.get(i);
//			if (gold >= en.getCond()) {
//				if (actAward == null) {
//					actAward = en;
//				} else {
//					if (en.getCond() > actAward.getCond()) {
//						actAward = en;
//					}
//				}
//			}
//
//		}
//		List<Long> statusList = activity.getStatusList();
//		if (actAward != null && statusList.get(actAward.getSortId()) == 0) {
//			statusList.set(actAward.getSortId(), (long) gold);
//		}
//	}
//
//	/**
//	 * 每日首笔充值
//	 * 
//	 * @param gold
//	 * @return
//	 */
//	public void giftPay(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GIFT_PAY);
//		if (activityBase == null) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_GIFT_PAY);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long state = statusList.get(0);
//		state += gold;
//		statusList.set(0, state);
//	}
//
//	/**
//	 * 充值丰收
//	 * 
//	 * @param gold
//	 * @return
//	 */
//	public void payFoison(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAY_FOISON);
//		if (activityBase == null) {
//			return;
//		}
//		int serverId = player.account.getServerId();
//		int addGold = gold / 5;
//		Lord lord = player.lord;
//		if (lord != null) {
//			int stone = gold * 2000;
//			List<CommonPb.Award> awards = new ArrayList<CommonPb.Award>();
//			awards.add(PbHelper.createAwardPb(AwardType.GOLD, 0, addGold));
//			awards.add(PbHelper.createAwardPb(AwardType.RESOURCE, 5, stone));
//			playerDataManager
//					.sendAttachMail(player, awards, MailType.MOLD_ACT_1, TimeHelper.getCurrentSecond(), String.valueOf(addGold), String.valueOf(stone));
//			LogHelper.logActivity(lord, ActivityConst.ACT_PAY_FOISON, 0, AwardType.GOLD, 0, addGold, serverId);
//			LogHelper.logActivity(lord, ActivityConst.ACT_PAY_FOISON, 0, AwardType.RESOURCE, 5, stone, serverId);
//		}
//	}
//
//	public void payEveryday(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EDAY_PAY_ID);
//		if (activityBase == null) {
//			return;
//		}
//
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_EDAY_PAY_ID);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		if (statusList.get(0) == 0) {
//			statusList.set(0, 1L);
//		}
//	}
//
//	public void payVacationland(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_VACATIONLAND_ID);
//		if (activityBase == null) {
//			return;
//		}
//
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_VACATIONLAND_ID);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0) + gold;
//		statusList.set(0, topup);
//	}
//
//	/**
//	 * 充值下注
//	 * 
//	 * @param player
//	 * @param gold
//	 */
//	public void payGamble(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GAMBLE_ID);
//		if (activityBase == null) {
//			return;
//		}
//
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_GAMBLE_ID);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0) + gold;
//		statusList.set(0, topup);
//	}
//
//	/**
//	 * 充值转盘
//	 * 
//	 * @param player
//	 * @param gold
//	 */
//	public void payTrunTable(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activityBase == null) {
//			return;
//		}
//
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0) + gold;
//		statusList.set(0, topup);
//	}
//
//	/**
//	 * 建军返利
//	 * 
//	 * @param player
//	 * @param money
//	 * @param resource
//	 */
//	public void amyRebate(Player player, int money, long[] resource) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_AMY_ID);
//		if (activityBase == null) {
//			return;
//		}
//		UsualActivityData usualActivityData = getUsualActivity(ActivityConst.ACT_AMY_ID);
//		int score = 0;
//		if (money > 0) {
//			Activity activity = getActivityInfo(player, ActivityConst.ACT_AMY_ID);
//			if (activity != null) {
//				StaticActRebate staticRebate = staticActivityDataMgr.getRebateByMoney(money);
//				if (staticRebate != null) {
//					long rebateId = (long) staticRebate.getRebateId();
//					List<Long> statusList = activity.getStatusList();
//					if (statusList.size() == 0) {
//						statusList.add(rebateId);
//					} else {
//						boolean flag = false;
//						for (int i = 0; i < statusList.size(); i++) {
//							Long se = statusList.get(i);
//							if (se.longValue() == 0) {
//								statusList.set(i, rebateId);
//								flag = true;
//								break;
//							}
//						}
//						if (!flag) {
//							statusList.add(rebateId);
//						}
//					}
//				}
//			}
//			score += money * 30;
//		}
//		if (resource != null) {//
//			long res = resource[0] + resource[1] + resource[2] + resource[3] + resource[4];
//			res = (res / 1000000) * 30;
//			if (res > 0) {
//				score += res;
//			}
//		}
//		usualActivityData.setGoal(usualActivityData.getGoal() + score);
//	}
//
//	/**
//	 * 宝藏
//	 * 
//	 * @param player
//	 * @param mintLv
//	 */
//	public void profoto(Player player, int mineLv) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PROFOTO_ID);
//		if (activityBase == null) {
//			return;
//		}
//		StaticActProfoto staticActProfoto = staticActivityDataMgr.getActProfoto(ActivityConst.ACT_PROFOTO_ID);
//		if (staticActProfoto == null) {
//			return;
//		}
//		List<List<Integer>> dropList = staticActProfoto.getDropList();
//		for (List<Integer> entity : dropList) {
//			if (entity.size() != 4) {
//				continue;
//			}
//			int lv = entity.get(0);
//			if (lv != mineLv) {
//				continue;
//			}
//			int seed = entity.get(1);
//			int part = entity.get(2);
//			int trust = entity.get(3);
//			int random = RandomHelper.randomInSize(seed);
//
//			if (random <= part) {
//				int partId = RandomHelper.randomInSize(4) + 1;
//				if (partId == 1) {
//					playerDataManager.addProp(player, staticActProfoto.getPart1(), 1);
//				} else if (partId == 2) {
//					playerDataManager.addProp(player, staticActProfoto.getPart2(), 1);
//				} else if (partId == 3) {
//					playerDataManager.addProp(player, staticActProfoto.getPart3(), 1);
//				} else if (partId == 4) {
//					playerDataManager.addProp(player, staticActProfoto.getPart4(), 1);
//				}
//			}
//			if (random > part && random <= part + trust) {
//				playerDataManager.addProp(player, staticActProfoto.getTrust(), 1);
//			}
//			break;
//		}
//	}
//
//	/**
//	 * 连续充值
//	 * 
//	 * @param player
//	 * @param mintLv
//	 */
//	public void payContu4(Player player, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAY_CONTINUE4);
//		if (activityBase == null) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_PAY_CONTINUE4);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//
//		Date now = new Date();
//		Date beginTime = activityBase.getBeginTime();
//		StaticActAward staticActAward = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_PAY_CONTINUE4).get(0);
//		int serverId = player.account.getServerId();
//		if (gold >= Integer.parseInt(staticActAward.getParam())) {
//			int dayiy = DateHelper.dayiy(beginTime, now);
//			dayiy = dayiy > 4 ? 4 : dayiy;
//			for (int i = 0; i < dayiy; i++) {
//				long v = statusList.get(i).longValue();
//				if (i < dayiy - 1 && v == 0) {
//					break;
//				}
//				if (i == dayiy - 1 && v == 0) {
//					statusList.set(i, (long) gold);
//				}
//			}
//			Lord lord = player.lord;
//			if (lord != null) {
//				int addGold = gold / 10;
//				List<CommonPb.Award> awards = new ArrayList<CommonPb.Award>();
//				awards.add(PbHelper.createAwardPb(AwardType.GOLD, 0, addGold));
//				playerDataManager.sendAttachMail(player, awards, MailType.MOLD_ACT_2, TimeHelper.getCurrentSecond(), String.valueOf(addGold));
//				LogHelper.logActivity(lord, ActivityConst.LOG_PAY_CONTINUE, 0, AwardType.GOLD, 0, addGold, serverId);
//			}
//		}
//	}
//
//	/**
//	 * 疯狂歼灭坦克
//	 * 
//	 * @param player
//	 * @param destoryTanks
//	 */
//	public void tankDestory(Player player, Map<Integer, RptTank> destoryTanks, boolean cal) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activityBase == null) {
//			return;
//		}
//		if (destoryTanks == null) {
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return;
//		}
//
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activity == null) {
//			return;
//		}
//
//		UsualActivityData usualActivityData = getUsualActivity(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (usualActivityData == null) {
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		Map<Integer, Integer> statusMap = activity.getStatusMap();
//		int nowDay = TimeHelper.getCurrentDay();
//		if (activity.getEndTime() != nowDay) {// 清理歼灭数据{坦克,战车,火炮,火箭}
//			List<StaticActAward> actAwardList = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_TANK_DESTORY_ID);
//			if (actAwardList != null) {
//				for (StaticActAward e : actAwardList) {
//					int sortId = e.getSortId();
//					int param = Integer.parseInt(e.getParam().trim());
//					if (param != 0) {
//						statusList.set(sortId, 0L);
//						statusMap.remove(e.getKeyId());
//					}
//				}
//			}
//			activity.setEndTime(nowDay);
//		}
//
//		// 坦克,战车,火炮,火箭,全部类型坦克,积分
//		int[] destorys = { 0, 0, 0, 0, 0, 0 };
//		Iterator<RptTank> it = destoryTanks.values().iterator();
//		while (it.hasNext()) {
//			RptTank rptTank = it.next();
//			StaticTank staticTank = staticTankDataMgr.getStaticTank(rptTank.getTankId());
//			if (staticTank != null) {
//				StaticActDestory staticActDestory = staticActivityDataMgr.getActDestory(rptTank.getTankId());
//				int dscore = 0;
//				if (staticActDestory != null) {
//					dscore = staticActDestory.getScore();
//				}
//				int type = staticTank.getType();
//				destorys[type - 1] += rptTank.getCount();// 坦克类型消耗
//				destorys[4] += rptTank.getCount();// 总击杀任意消耗
//				if (cal) {
//					destorys[5] += rptTank.getCount() * dscore;
//				}
//			}
//		}
//
//		for (int i = 0; i < 6; i++) {// 记录坦克,战车,火炮,火箭,总击杀,积分
//			if (destorys[i] > 0) {
//				updActivity(player, ActivityConst.ACT_TANK_DESTORY_ID, destorys[i], i);
//			}
//		}
//		if (destorys[5] > 0) {// 积分纳入排行榜
//			long score = activity.getStatusList().get(5);
//			if (score >= 50000) {
//				usualActivityData.addPlayerRank(player.lord.getLordId(), score, ActivityConst.RANK_TANK_DESTORY, ActivityConst.DESC);
//			}
//		}
//	}
//
//	/**
//	 * 消费转盘
//	 * 
//	 * @param lord
//	 * @param gold
//	 */
//	public void actConsumeDail(Lord lord, int gold) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityBase == null) {
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return;
//		}
//		Player player = playerDataManager.getPlayer(lord.getLordId());
//		if (player == null) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activity == null) {
//			return;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long consume = statusList.get(0);
//		statusList.set(0, consume + gold);
//
//	}
//
//	/**
//	 * 刷新配件/装备公式
//	 * 
//	 * @param player
//	 * @param actExchange
//	 */
//	public Cash freshCash(Player player, Cash cash, StaticActExchange actExchange, boolean reset) {
//		int exchangeId = actExchange.getExchangeId();
//		int formulaId = actExchange.getFormulaId();
//		int price = actExchange.getPrice();
//		if (cash == null) {
//			cash = new Cash();
//			cash.setFree(1);// 免费次数
//			cash.setState(actExchange.getLimit());// 可购买次数
//		}
//		if (reset) {
//			cash.setFree(1);// 免费次数
//			cash.setState(actExchange.getLimit());// 可购买次数
//		}
//
//		cash.setCashId(exchangeId);
//		cash.setFormulaId(formulaId);
//		cash.setPrice(price);
//
//		List<List<Integer>> rets = new ArrayList<List<Integer>>();
//		rets.add(random(actExchange.getMeta1()));
//		rets.add(random(actExchange.getMeta2()));
//		rets.add(random(actExchange.getMeta3()));
//		List<Integer> m4 = random(actExchange.getMeta4());
//		if (m4 != null) {
//			rets.add(m4);
//		}
//		List<Integer> m5 = random(actExchange.getMeta5());
//		if (m5 != null) {
//			rets.add(m5);
//		}
//		// 刷新材料
//		cash.setList(rets);
//		// 刷新奖励
//		cash.setAwardList(random(actExchange.getAwardList()));
//		return cash;
//	}
//
//	/**
//	 * 随机奖励
//	 * 
//	 * @param metaList
//	 * @return
//	 */
//	public List<Integer> random(List<List<Integer>> metaList) {
//		if (metaList == null || metaList.size() == 0) {
//			return null;
//		}
//		int[] seeds = { 0, 0 };
//		for (List<Integer> e : metaList) {
//			if (e.size() != 4) {
//				continue;
//			}
//			seeds[0] += e.get(3);
//		}
//		seeds[0] = RandomHelper.randomInSize(seeds[0]);
//		for (List<Integer> e : metaList) {
//			if (e.size() != 4) {
//				continue;
//			}
//			seeds[1] += e.get(3);
//			if (seeds[0] <= seeds[1]) {
//				return e;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 配件补给：配件关卡伤害+30%,购买次数返回60%金币
//	 * 
//	 * @param player
//	 * @return
//	 */
//	public float partSupply(Player player) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_SUPPLY);
//		if (activityBase == null) {
//			return 1f;
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return 1f;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_PART_SUPPLY);
//		if (activity == null) {
//			return 1f;
//		}
//		return 0.5f;
//	}
//
//	/**
//	 * 科技优惠：资源类升级提速100%（相当于20级科技馆建筑）;资源类升级消耗资源减少50%
//	 * 
//	 * @return
//	 */
//	public int[] scienceDiscount(Player player, int scienceId) {
//		// 铁，石头，铜，钛，水晶
//		if (scienceId != 101 && scienceId != 102 && scienceId != 103 && scienceId != 104 && scienceId != 105) {
//			return new int[] { 0, 1 };
//		}
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_SCIENCE_MATERIAL);
//		if (activityBase == null) {
//			return new int[] { 0, 1 };
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return new int[] { 0, 1 };
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_SCIENCE_MATERIAL);
//		if (activity == null) {
//			return new int[] { 0, 1 };
//		}
//		return new int[] { 20, 2 };
//	}
//
//	/**
//	 * 火力全开：军团大厅建设贡献提高50%，科技捐献经营和贡献提高50%
//	 * 
//	 * @return
//	 */
//	public int fireSheet(Player player, int partyId, int build) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_FIRE_SHEET);
//		if (activityBase == null) {
//			return build;
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return build;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_FIRE_SHEET);
//		if (activity == null) {
//			return build;
//		}
//		UsualActivityData activityData = getUsualActivity(ActivityConst.ACT_FIRE_SHEET);
//		if (activityData == null) {
//			return build;
//		}
//		// 添加排名记录
//		build = (int) Math.ceil(build * 1.5f);
//		activityData.addPartyRank(partyId, build, ActivityConst.RANK_FIRE_SHEET, ActivityConst.DESC);
//		return build;
//	}
//
//	/**
//	 * 配件分解兑换
//	 * 
//	 * @return
//	 */
//	public void partResolve(Player player, List<PartResolve> resolveList) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activityBase == null) {
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			return;
//		}
//		Activity activity = getActivityInfo(player, ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activity == null) {
//			return;
//		}
//		int add = 0;
//		for (PartResolve e : resolveList) {
//			int quality = e.getQuality();
//			int type = e.getType();
//			int count = e.getCount();
//			int score = staticActivityDataMgr.getResolveSlug(activityBase.getKeyId(), type, quality);
//			add += count * score;
//		}
//		List<Long> statusList = activity.getStatusList();
//		long score = statusList.get(0).longValue();
//		statusList.set(0, score + add);
//
//	}
//
//	public void initPartyLvRank(int begin) {
//		int status = rankDataManager.partyLvRankList.status;
//		if (status != begin) {// 初始化
//			rankDataManager.partyLvRankList.status = begin;// 开启时间设置为当前
//			rankDataManager.partyLvRankList.getList().clear();// 清除已有历史
//			Map<Integer, PartyData> partyMap = partyDataManager.getPartyMap();
//			if (partyMap != null && !partyMap.isEmpty()) {
//				Iterator<PartyData> it = partyMap.values().iterator();
//				while (it.hasNext()) {
//					PartyData next = it.next();
//					int partyId = next.getPartyId();
//					String partyName = next.getPartyName();
//					int partyLv = next.getPartyLv();
//					int scienceLv = next.getScienceLv();
//					int wealLv = next.getWealLv();
//					int build = next.getBuild();
//					rankDataManager.LoadPartyLv(partyId, partyName, partyLv, scienceLv, wealLv, build);
//				}
//			}
//			Collections.sort(rankDataManager.partyLvRankList.getList(), new ComparatorPartyLv());
//		}
//	}
//
//	/**
//	 * 
//	 * @param activityId
//	 */
//	public void resetPlayerRank(int activityId, int maxRank) {
//		Map<Long, Player> playerMap = playerDataManager.getPlayers();
//		Iterator<Player> it = playerMap.values().iterator();
//		while (it.hasNext()) {
//			Player player = it.next();
//			Activity activity = getActivityInfo(player, activityId);
//			if (activity == null) {
//				continue;
//			}
//		}
//		// Set<Integer> sets = staticActivityDataMgr.getSorts(activityId);
//		// List<ActivityRankList> rankList = new ArrayList<ActivityRankList>();
//		// for (int i = 0; i < sets.size(); i++) {
//		// ActivityRankList ranks = new ActivityRankList();
//		// rankList.add(ranks);
//		// }
//		// activityRankMap.put(activityId, rankList);
//	}
//
//	/**
//	 * 获取活动排行
//	 * 
//	 * @return
//	 */
//	public PartyLvRank getPartyLvRank(int partyId) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RANK_PARTY_LV);
//		if (activityBase == null) {
//			return null;
//		}
//		int step = activityBase.getStep();
//		if (step == ActivityConst.OPEN_STEP) {// begin-end之间
//			int begin = TimeHelper.getDay(activityBase.getBeginTime());
//			initPartyLvRank(begin);// 如果未初始化则初始化
//		}
//		PartyLvRank partyLvRank = rankDataManager.getPartyRank(partyId);
//		return partyLvRank;
//	}
//
//	public List<PartyLvRank> getPartyLvRankList(int page) {
//		List<PartyLvRank> list = new ArrayList<PartyLvRank>();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RANK_PARTY_LV);
//		if (activityBase == null) {
//			return list;
//		}
//		int step = activityBase.getStep();
//		if (step == ActivityConst.OPEN_STEP) {// beginTime-endTime之间
//			int begin = TimeHelper.getDay(activityBase.getBeginTime());
//			initPartyLvRank(begin);// 如果未初始化则初始化
//		}
//		return rankDataManager.getPartyLvRank(page);
//	}
//
//	public List<PartyLvRank> getPartyLvRankList() {
//		List<PartyLvRank> list = new ArrayList<PartyLvRank>();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RANK_PARTY_LV);
//		if (activityBase == null) {
//			return list;
//		}
//		int step = activityBase.getStep();
//		if (step == ActivityConst.OPEN_STEP) {// beginTime-endTime之间
//			int begin = TimeHelper.getDay(activityBase.getBeginTime());
//			initPartyLvRank(begin);// 如果未初始化则初始化
//		}
//		return rankDataManager.getPartyLvRankList();
//	}
//
//	/**
//	 * 更新帮派等级排行
//	 * 
//	 * @param partyId
//	 * @param partyLv
//	 */
//	public void updatePartyLvRank(PartyData partyData) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RANK_PARTY_LV);
//		if (activityBase == null) {
//			return;
//		}
//		int begin = TimeHelper.getDay(activityBase.getBeginTime());
//		initPartyLvRank(begin);// 如果未初始化则初始化
//		rankDataManager.updatePartyLv(partyData);
//	}
//
//	public void addPartyLvRank(PartyData partyData) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_RANK_PARTY_LV);
//		if (activityBase == null) {
//			return;
//		}
//		int begin = TimeHelper.getDay(activityBase.getBeginTime());
//		initPartyLvRank(begin);// 如果未初始化则初始化
//		int partyId = partyData.getPartyId();
//		String partyName = partyData.getPartyName();
//		int partyLv = partyData.getPartyLv();
//		int scienceLv = partyData.getScienceLv();
//		int wealLv = partyData.getWealLv();
//		int build = partyData.getBuild();
//		PartyLvRank partyLvRank = new PartyLvRank(partyId, partyName, partyLv, scienceLv, wealLv, build);
//		rankDataManager.getPartyLvRankList().add(partyLvRank);
//	}
//
//	public void refreshDay(Activity activity) {
//		if (activity == null) {
//			return;
//		}
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activity.getActivityId());
//		if (activityBase == null) {
//			return;
//		}
//		activity.autoDayClean(activityBase);
//	}
//
//	/**
//	 * Function：活动状态刷新：创建和重开活动状态重置
//	 * 
//	 * @param activity
//	 * @param beginTime
//	 */
//	public void refreshStatus(Activity activity) {
//		List<Long> statusList = new ArrayList<Long>();
//		switch (activity.getActivityId()) {
//		case ActivityConst.ACT_LEVEL:// 等级
//			break;
//		case ActivityConst.ACT_ATTACK: {// 攻打玩家
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		}
//		case ActivityConst.ACT_RANK_FIGHT:// 战力排行
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RANK_COMBAT:// 关卡排行
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RANK_HONOUR:// 荣誉排行
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RANK_PARTY_LV:// 军团等级排行
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PARTY_DONATE:// 军团捐献
//			for (int i = 0; i < 4; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_LOT_EQUIP:// 装备探险
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_LOT_PART:// 配件探险
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_COLLECT_RESOURCE:// 资源收集
//			for (int i = 0; i < 5; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_COMBAT:// 关卡送技能书
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RANK_PARTY_FIGHT:// 军团战力排行
//			statusList.add(0L);// 军团战力排行
//			for (int i = 0; i < 10; i++) {// 1-10名玩家ID
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_INVEST:// 投资计划
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RED_GIFT:// 充值红包
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAY_EVERYDAY:// 每日充值活动
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAY_FIRST:// 首次充值活动
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_QUOTA:// 折扣半价
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//
//		case ActivityConst.ACT_PURPLE_COLL:// 紫装收集
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PURPLE_UP:// 紫装升级
//			for (int i = 0; i < 20; i++) {
//				statusList.add(0L);//
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_CRAZY_ARENA:// 疯狂竞技
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_CRAZY_HERO:// 疯狂进阶
//			for (int i = 0; i < 20; i++) {
//				statusList.add(0L);//
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PART_EVOLVE:// 配件进化
//			break;
//		case ActivityConst.ACT_FLASH_SALE:// 限时出售
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_ENLARGE:// 招兵买将
//			break;
//		case ActivityConst.ACT_LOTTEY_EQUIP:// 抽将折扣
//			break;
//		case ActivityConst.ACT_COST_GOLD:// 消费有奖
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_EQUIP_FEED:// 装备补给
//			break;
//		case ActivityConst.ACT_CONTU_PAY:// 连续充值
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);//
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAY_FOISON:// 充值丰收
//			break;
//		case ActivityConst.ACT_DAY_PAY:// 天天充值
//			statusList.add(0L);//
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_DAY_BUY:// 天天限购
//		case ActivityConst.ACT_FLASH_META:// 材料限购
//		case ActivityConst.ACT_MONTH_SALE:// 月末限购
//		case ActivityConst.ACT_GIFT_OL:// 在线好礼
//		case ActivityConst.ACT_MONTH_LOGIN:// 每月登录
//		case ActivityConst.ACT_ENEMY_SALE:// 敌军兜售
//		case ActivityConst.ACT_UP_EQUIP_CRIT:// 升装暴击
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_RE_FRIST_PAY:// 每天首充返利
//		case ActivityConst.ACT_GIFT_PAY:// 充值送礼
//			for (int i = 0; i < 10; i++)
//				statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_VIP_GIFT:// vip礼包
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAY_CONTINUE4:// 连续充值
//			for (int i = 0; i < 4; i++)
//				statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PART_SUPPLY:// 配件补给
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//
//		/******* 精彩活动 ******/
//		case ActivityConst.ACT_MECHA:// 机甲洪流
//			for (int i = 0; i < 4; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_BEE_ID:// 勤劳致富
//			for (int i = 0; i < 20; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_AMY_ID:// 建军节
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAWN_ID:// 极限单兵
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PART_DIAL_ID:// 配件转盘
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_TANK_DESTORY_ID:// 疯狂歼灭
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_GENERAL_ID:// 将领招募
//			statusList.add(0L);// 积分
//			statusList.add(0L);// 招募次数
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_EDAY_PAY_ID:// 每日充值
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_CONSUME_DIAL_ID:// 消费转盘
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_VACATIONLAND_ID:// 度假胜地
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PART_EXCHANGE_ID:// 配件兑换
//			for (int i = 0; i < 50; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_EQUIP_EXCHANGE_ID:// 装备兑换
//			for (int i = 0; i < 50; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PART_RESOLVE_ID:// 配件分解兑换
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_GAMBLE_ID:// 累充下注
//			statusList.add(0L);
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_PAY_TURNTABLE_ID:// 充值转盘
//			statusList.add(0L);
//			statusList.add(0L);
//			activity.setStatusList(statusList);
//			break;
//		case ActivityConst.ACT_SPRING_ID:// 新春狂欢
//			for (int i = 0; i < 10; i++) {
//				statusList.add(0L);
//			}
//			activity.setStatusList(statusList);
//			break;
//		default:
//			break;
//		}
//	}
//}
