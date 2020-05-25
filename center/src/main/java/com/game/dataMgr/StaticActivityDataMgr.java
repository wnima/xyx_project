package com.game.dataMgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.game.domain.ActivityBase;
//import com.game.util.DateHelper;
import com.game.util.RandomHelper;

import config.bean.ConfActAward;
import config.bean.ConfActCourse;
import config.bean.ConfActDestory;
import config.bean.ConfActEverydayPay;
import config.bean.ConfActExchange;
import config.bean.ConfActFortune;
import config.bean.ConfActGamble;
import config.bean.ConfActGeneral;
import config.bean.ConfActMecha;
import config.bean.ConfActPartResolve;
import config.bean.ConfActProfoto;
import config.bean.ConfActQuota;
import config.bean.ConfActRaffle;
import config.bean.ConfActRank;
import config.bean.ConfActRebate;
import config.bean.ConfActTech;
import config.bean.ConfActVacationland;
import config.bean.ConfActivity;
import config.bean.ConfActivityPlan;
import define.ActivityConst;

public class StaticActivityDataMgr extends BaseDataMgr {

	private Map<Integer, List<ConfActAward>> awardMap = new HashMap<Integer, List<ConfActAward>>();

	private Map<Integer, ConfActAward> actAwardMap = new HashMap<Integer, ConfActAward>();

	private List<ActivityBase> activityList = new ArrayList<ActivityBase>();

	private Map<Integer, ConfActMecha> actMechaMap = new HashMap<Integer, ConfActMecha>();

	private Map<Integer, ConfActQuota> actQuotaMap = new HashMap<Integer, ConfActQuota>();

	private Map<Integer, ConfActRebate> actRebateMap = new HashMap<Integer, ConfActRebate>();

	private Map<Integer, ConfActFortune> actFortuneMap = new HashMap<Integer, ConfActFortune>();

	private Map<Integer, ConfActProfoto> actProfotoMap = new HashMap<Integer, ConfActProfoto>();

	private Map<Integer, List<ConfActRaffle>> actRaffleMap = new HashMap<Integer, List<ConfActRaffle>>();

	private Map<Integer, Map<Integer, List<ConfActRank>>> actRankMap = new HashMap<Integer, Map<Integer, List<ConfActRank>>>();

	private Map<Integer, Map<Integer, ConfActCourse>> actCourseMap = new HashMap<Integer, Map<Integer, ConfActCourse>>();

	private Map<Integer, ConfActTech> actTechMap = new HashMap<Integer, ConfActTech>();

	private Map<Integer, ConfActGeneral> actGeneralMap = new HashMap<Integer, ConfActGeneral>();

	private Map<Integer, ConfActEverydayPay> actEverydayPayMap = new HashMap<Integer, ConfActEverydayPay>();

	private Map<Integer, ConfActDestory> actDestoryMap = new HashMap<Integer, ConfActDestory>();

	private Map<Integer, ConfActVacationland> actVacationlandMap = new HashMap<Integer, ConfActVacationland>();

	private List<ConfActVacationland> villageList = new ArrayList<ConfActVacationland>();

	private Map<Integer, List<ConfActExchange>> exchangeMap = new HashMap<Integer, List<ConfActExchange>>();

	private Map<Integer, List<ConfActPartResolve>> resolveMap = new HashMap<Integer, List<ConfActPartResolve>>();

	private Map<Integer, Map<Integer, Map<Integer, Integer>>> resolveSlugMap = new HashMap<Integer, Map<Integer, Map<Integer, Integer>>>();

	private Map<Integer, List<ConfActGamble>> gambleMap = new HashMap<Integer, List<ConfActGamble>>();


	@Override
	public void init() {
//		List<StaticActAward> list = staticDataDao.selectActAward();
//		for (StaticActAward e : list) {
//			int activityId = e.getActivityId();
//			actAwardMap.put(e.getKeyId(), e);
//			// 活动
//			List<StaticActAward> eeList = awardMap.get(activityId);
//			if (eeList == null) {
//				eeList = new ArrayList<StaticActAward>();
//				awardMap.put(activityId, eeList);
//			}
//			eeList.add(e);
//		}
//
//		List<StaticActRank> srankList = staticDataDao.selectActRankList();
//		for (StaticActRank e : srankList) {
//			int activityId = e.getActivityId();
//			Map<Integer, List<StaticActRank>> rankListMap = actRankMap.get(activityId);
//			if (rankListMap == null) {
//				rankListMap = new HashMap<Integer, List<StaticActRank>>();
//				actRankMap.put(activityId, rankListMap);
//			}
//			int sortId = e.getSortId();
//			List<StaticActRank> sortRankList = rankListMap.get(sortId);
//			if (sortRankList == null) {
//				sortRankList = new ArrayList<StaticActRank>();
//				rankListMap.put(sortId, sortRankList);
//			}
//			sortRankList.add(e);
//		}
//
//		actMechaMap = staticDataDao.selectActMecha();
//
//		actQuotaMap = staticDataDao.selectActQuota();
//
//		actRebateMap = staticDataDao.selectActRebate();
//
//		actFortuneMap = staticDataDao.selectActFortune();
//
//		actProfotoMap = staticDataDao.selectActProfoto();
//
//		tankRaffle();
//		course();
//		activity();
//		initTech();
//		initGeneral();
//		initEverydayPay();
//		initDestory();
//		initVacationland();
//		initExchange();
//		initPartResolve();
//		initGamble();
	}

	private void tankRaffle() {
//		List<StaticActRaffle> list = staticDataDao.selectActRaffle();
//		for (StaticActRaffle e : list) {
//			int activityId = e.getActivityId();
//			List<StaticActRaffle> raffleList = actRaffleMap.get(activityId);
//			if (raffleList == null) {
//				raffleList = new ArrayList<StaticActRaffle>();
//				actRaffleMap.put(activityId, raffleList);
//			}
//			raffleList.add(e);
//		}
	}

	private void course() {
//		List<StaticActCourse> list = staticDataDao.selectActCourse();
//		for (StaticActCourse e : list) {
//			int activityId = e.getActivityId();
//			Map<Integer, StaticActCourse> courseMap = actCourseMap.get(activityId);
//			if (courseMap == null) {
//				courseMap = new HashMap<Integer, StaticActCourse>();
//				actCourseMap.put(activityId, courseMap);
//			}
//			courseMap.put(e.getSctionId(), e);
//		}
	}

	private void activity() {
//		int activityMoldId = serverSetting.getActMoldId();
//		Map<Integer, StaticActivity> activityMap = staticDataDao.selectStaticActivity();
//		List<StaticActivityPlan> planList = staticDataDao.selectStaticActivityPlan();
//		Date openTime = DateHelper.parseDate(serverSetting.getOpenTime());
//		for (StaticActivityPlan e : planList) {
//			int activityId = e.getActivityId();
//			StaticActivity staticActivity = activityMap.get(activityId);
//			if (staticActivity == null) {
//				continue;
//			}
//			int moldId = e.getMoldId();
//			if (activityMoldId != moldId) {
//				continue;
//			}
//			ActivityBase activityBase = new ActivityBase();
//			activityBase.setOpenTime(openTime);
//			activityBase.setPlan(e);
//			activityBase.setStaticActivity(staticActivity);
//			boolean flag = activityBase.initData();
//			if (flag) {
//				activityList.add(activityBase);
//			}
//		}
	}

	private void initTech() {
//		actTechMap = staticDataDao.selectActTech();
	}

	private void initGeneral() {
//		actGeneralMap = staticDataDao.selectActGeneral();
	}

	private void initEverydayPay() {
//		actEverydayPayMap = staticDataDao.selectActEveryDayPay();
	}

	private void initDestory() {
//		actDestoryMap = staticDataDao.selectActDestory();
	}

	private void initVacationland() {
//		actVacationlandMap = staticDataDao.selectActVacationland();
//		Iterator<StaticActVacationland> it = actVacationlandMap.values().iterator();
//		int vid = 0;
//		while (it.hasNext()) {
//			StaticActVacationland next = it.next();
//			if (next.getVillageId() != vid) {
//				villageList.add(next);
//				vid = next.getVillageId();
//			}
//		}
	}

	private void initExchange() {
//		List<StaticActExchange> exchangeList = staticDataDao.selectActExchange();
//		for (StaticActExchange e : exchangeList) {
//			int activityId = e.getActivityId();
//
//			List<StaticActExchange> eList = exchangeMap.get(activityId);
//			if (eList == null) {
//				eList = new ArrayList<StaticActExchange>();
//				exchangeMap.put(activityId, eList);
//			}
//			eList.add(e);
//		}
	}

	private void initPartResolve() {
//		List<StaticActPartResolve> partResolveList = staticDataDao.selectActPartResolve();
//		for (StaticActPartResolve e : partResolveList) {
//			int activityId = e.getActivityId();
//
//			List<StaticActPartResolve> eList = resolveMap.get(activityId);
//			if (eList == null) {
//				eList = new ArrayList<StaticActPartResolve>();
//				resolveMap.put(activityId, eList);
//			}
//
//			List<List<Integer>> resolveList = e.getResolveList();
//			if (resolveList != null) {
//				for (List<Integer> eresolve : resolveList) {
//					int type = eresolve.get(0);
//					int quality = eresolve.get(1);
//					int count = eresolve.get(2);
//
//					Map<Integer, Map<Integer, Integer>> amap = resolveSlugMap.get(activityId);
//					if (amap == null) {
//						amap = new HashMap<Integer, Map<Integer, Integer>>();
//						resolveSlugMap.put(activityId, amap);
//					}
//					Map<Integer, Integer> tmap = amap.get(type);
//					if (tmap == null) {
//						tmap = new HashMap<Integer, Integer>();
//						amap.put(type, tmap);
//					}
//					if (!tmap.containsKey(quality)) {
//						tmap.put(quality, count);
//					}
//				}
//			}
//
//			eList.add(e);
//		}
	}

	private void initGamble() {
//		List<StaticActGamble> gambleList = staticDataDao.selectActGamble();
//		for (StaticActGamble e : gambleList) {
//			int activityId = e.getActivityId();
//
//			List<StaticActGamble> eList = gambleMap.get(activityId);
//			if (eList == null) {
//				eList = new ArrayList<StaticActGamble>();
//				gambleMap.put(activityId, eList);
//			}
//
//			eList.add(e);
//		}
	}

	public List<ActivityBase> getActivityList() {
		return activityList;
	}

	public List<ConfActAward> getActAwardById(int activityId) {
		return awardMap.get(activityId);
	}

	public ConfActAward getActAward(int keyId) {
		return actAwardMap.get(keyId);
	}

	public ActivityBase getActivityById(int activityId) {
		for (ActivityBase e : activityList) {
			ConfActivity a = e.getStaticActivity();
			ConfActivityPlan plan = e.getPlan();
			if (a == null || plan == null) {
				continue;
			}
			if (a.getActivityId() == activityId && e.getStep() != ActivityConst.OPEN_CLOSE) {
				return e;
			}
		}
		return null;
	}

	public ActivityBase getActivityById(int activityId, int plat) {
		int platFlag = 1;// 默认安卓用户
		if (plat == 94 || plat == 95 || plat > 500) {
			platFlag = 2;// IOS用户
		}
		for (ActivityBase e : activityList) {
			ConfActivity a = e.getStaticActivity();
			ConfActivityPlan plan = e.getPlan();
			if (a == null || plan == null) {
				continue;
			}
			if (plan.getPlat() == 1 && platFlag == 2) {// 如果是安卓平台,IOS玩家不可见
				continue;
			} else if (plan.getPlat() == 2 && platFlag == 1) {// 如果是IOS平台,安卓玩家不可见
				continue;
			}
			if (a.getActivityId() == activityId && e.getStep() != ActivityConst.OPEN_CLOSE) {
				return e;
			}
		}
		return null;
	}

	public ConfActMecha getMechaById(int mechaId) {
		return actMechaMap.get(mechaId);
	}

	public ConfActMecha getMechaById(int activityId, int count) {
		Iterator<ConfActMecha> it = actMechaMap.values().iterator();
		while (it.hasNext()) {
			ConfActMecha next = it.next();
			if (next.getActivityId() == activityId && next.getCount() == count) {
				return next;
			}
		}
		return null;
	}

	public Map<Integer, ConfActMecha> getActMechaMap() {
		return actMechaMap;
	}

	public List<ConfActQuota> getQuotaList(int activityId) {
		List<ConfActQuota> rs = new ArrayList<>();
		Iterator<ConfActQuota> it = actQuotaMap.values().iterator();
		while (it.hasNext()) {
			ConfActQuota next = it.next();
			if (next.getActivityId() == activityId) {
				rs.add(next);
			}
		}
		return rs;
	}

	public ConfActQuota getQuotaById(int quotaId) {
		return actQuotaMap.get(quotaId);
	}

	public Map<Integer, ConfActQuota> getActQuotaMap() {
		return actQuotaMap;
	}

	public ConfActRebate getRebateById(int rebateId) {
		return actRebateMap.get(rebateId);
	}

	public ConfActRebate getRebateByMoney(int money) {
		Iterator<ConfActRebate> it = actRebateMap.values().iterator();
		ConfActRebate rebate = null;
		while (it.hasNext()) {
			ConfActRebate next = it.next();
			if (rebate == null) {
				if (next.getMoney() <= money) {
					rebate = next;
				}
			} else {
				if (next.getMoney() <= money && next.getMoney() >= rebate.getMoney()) {
					rebate = next;
				}
			}
		}
		return rebate;
	}

	public Set<Integer> getSorts(int activityId) {
		Set<Integer> sets = new HashSet<>();
		List<ConfActAward> list = awardMap.get(activityId);
		for (ConfActAward ee : list) {
			if (!sets.contains(ee.getSortId())) {
				sets.add(ee.getSortId());
			}
		}
		return sets;
	}

	public ConfActFortune getActFortune(int fortuneId) {
		return actFortuneMap.get(fortuneId);
	}

	public List<ConfActFortune> getActFortuneList(int activityId) {
		List<ConfActFortune> list = new ArrayList<ConfActFortune>();
		Iterator<ConfActFortune> it = actFortuneMap.values().iterator();
		while (it.hasNext()) {
			ConfActFortune next = it.next();
			if (next.getActivityId() == activityId) {
				list.add(next);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param awardList格式为
	 *            List<Integer>:类型,ID,数量,权重
	 * @return
	 */
	public List<Integer> randomAwardList(List<List<Integer>> awardList) {
		if (awardList == null || awardList.size() == 0) {
			return null;
		}
		int[] seeds = { 0, 0 };
		for (List<Integer> entity : awardList) {
			if (entity.size() < 4) {
				continue;
			}
			seeds[0] += entity.get(3);
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (List<Integer> entity : awardList) {
			if (entity.size() < 4) {
				continue;
			}
			seeds[1] += entity.get(3);
			if (seeds[0] <= seeds[1]) {
				return entity;
			}
		}
		return null;
	}

	public ConfActRank getActRank(int activityId, int sortId, int rank) {
		if (!actRankMap.containsKey(activityId)) {
			return null;
		}
		List<ConfActRank> list = actRankMap.get(activityId).get(sortId);
		if (list == null) {
			return null;
		}
		for (ConfActRank e : list) {
			if (rank <= e.getRankEnd() && rank >= e.getRankBegin()) {
				return e;
			}
		}
		return null;
	}

	public List<ConfActRank> getActRankList(int activityId, int sortId) {
		List<ConfActRank> list = new ArrayList<ConfActRank>();
		if (!actRankMap.containsKey(activityId)) {
			return list;
		}
		return actRankMap.get(activityId).get(sortId);
	}

	public ConfActProfoto getActProfoto(int activityId) {
		return actProfotoMap.get(activityId);
	}

	public ConfActRaffle getActRaffle(int activityId) {
		List<ConfActRaffle> list = actRaffleMap.get(activityId);
		if (list == null) {
			return null;
		}
		int[] seeds = { 0, 0 };
		for (ConfActRaffle e : list) {
			seeds[0] += e.getProbability();
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (ConfActRaffle e : list) {
			seeds[1] += e.getProbability();
			if (seeds[0] <= seeds[1]) {
				return e;
			}
		}
		return null;
	}

	public int[] getColor(ConfActRaffle staticActRaffle) {
		// 奖励,颜色1,颜色2,颜色3
		int[] colors = { 0, 0, 0, 0 };
		if (staticActRaffle.getScale() == 1) {// 一等奖(颜色都一样)
			int color = RandomHelper.randomInSize(4) + 1;
			for (int i = 0; i < 4; i++) {
				colors[i] = color;
			}
		} else if (staticActRaffle.getScale() == 2) {// 二等奖{2个值相同,1个不同}
			int seed = RandomHelper.randomInSize(15) + 1;
			colors[1] = seed / 4 + 1;
			colors[2] = seed % 4 + 1;
			if (colors[1] == colors[2]) {// 已相同有2种,第三种颜色不相同
				colors[3] = 5 - colors[0];
				colors[0] = colors[1];
			} else {
				int color = RandomHelper.randomInSize(2);
				if (color == 0) {
					colors[3] = colors[1];
					colors[0] = colors[1];
				} else {
					colors[3] = colors[2];
					colors[0] = colors[2];
				}
			}
		} else if (staticActRaffle.getScale() == 3) {// 三等奖{3个值不同1-4之间}
			int seed = RandomHelper.randomInSize(4) + 1;
			colors[0] = seed;
			colors[1] = seed;
			colors[2] = 5 - seed;
			colors[3] = (6 - seed) % 4 + 1;
		}
		return colors;
	}

	public ConfActCourse getActCourse(int activityId, int sectionId) {
		Map<Integer, ConfActCourse> courseMap = actCourseMap.get(activityId);
		if (courseMap != null) {
			return courseMap.get(sectionId);
		}
		return null;
	}

	public List<Integer> getActTechAward(ConfActTech staticActTech) {
		if (staticActTech == null) {
			return null;
		}
		List<List<Integer>> awardList = staticActTech.getAwardList();
		if (awardList.size() == 1) {
			return awardList.get(0);
		}
		int[] seeds = { 0, 0 };
		for (List<Integer> e : awardList) {
			if (e.size() < 4) {
				continue;
			}
			seeds[0] += e.get(3);
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (List<Integer> e : awardList) {
			seeds[1] += e.get(3);
			if (seeds[0] <= seeds[1]) {
				return e;
			}
		}
		return null;
	}

	public ConfActTech getActTech(int techId) {
		return actTechMap.get(techId);
	}

	public Map<Integer, ConfActTech> getActTechMap() {
		return actTechMap;
	}

	public List<ConfActGeneral> getActGeneralList(int activityId) {
		List<ConfActGeneral> list = new ArrayList<ConfActGeneral>();
		Iterator<ConfActGeneral> it = actGeneralMap.values().iterator();
		while (it.hasNext()) {
			ConfActGeneral next = it.next();
			if (next.getActivityId() == activityId) {
				list.add(next);
			}
		}
		return list;
	}

	public ConfActGeneral getActGeneral(int generalId) {
		return actGeneralMap.get(generalId);
	}

	public ConfActEverydayPay getActEverydayPay(int dayiy) {
		return actEverydayPayMap.get(dayiy);
	}

	public ConfActDestory getActDestory(int tankId) {
		return actDestoryMap.get(tankId);
	}

	public boolean isSpecial(ConfActEverydayPay everyDay, int type, int id) {
		List<List<Integer>> list = everyDay.getSpecialList();
		for (List<Integer> e : list) {
			if (e.size() < 3) {
				continue;
			}
			if (e.get(0) == type && e.get(1) == id) {
				return true;
			}
		}
		return false;
	}

	public Map<Integer, ConfActVacationland> getActVacationlandMap() {
		return actVacationlandMap;
	}

	public List<ConfActVacationland> getVillageList() {
		return villageList;
	}

	public ConfActVacationland getVillage(int landId) {
		return actVacationlandMap.get(landId);
	}

	public List<ConfActExchange> getActExchange(int activityId) {
		return exchangeMap.get(activityId);
	}

	public ConfActExchange getActExchange(int activityId, int exchangeId) {
		List<ConfActExchange> list = exchangeMap.get(activityId);
		if (list == null) {
			return null;
		}
		for (ConfActExchange e : list) {
			if (e.getExchangeId() == exchangeId) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取配件分解改造
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ConfActPartResolve> getActPartResolveList(int activityId) {
		if (!resolveMap.containsKey(activityId)) {
			return null;
		}
		List<ConfActPartResolve> list = resolveMap.get(activityId);
		return list;
	}

	public int getResolveSlug(int activityId, int type, int quality) {
		if (!resolveSlugMap.containsKey(activityId)) {
			return 0;
		}
		Map<Integer, Map<Integer, Integer>> amap = resolveSlugMap.get(activityId);
		if (amap == null) {
			return 0;
		}
		Map<Integer, Integer> tmap = amap.get(type);
		if (tmap == null) {
			return 0;
		}
		if (!tmap.containsKey(quality)) {
			return 0;
		}
		return tmap.get(quality);
	}

	/**
	 * 获取配件分解改造
	 * 
	 * @param activityId
	 * @param resolveId
	 * @return
	 */
	public ConfActPartResolve getActPartResolve(int activityId, int resolveId) {
		if (!resolveMap.containsKey(activityId)) {
			return null;
		}
		List<ConfActPartResolve> list = resolveMap.get(activityId);
		for (ConfActPartResolve e : list) {
			if (e.getResolveId() == resolveId) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取下注赢金币列表
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ConfActGamble> getActGambleList(int activityId) {
		if (!gambleMap.containsKey(activityId)) {
			return null;
		}
		List<ConfActGamble> list = gambleMap.get(activityId);
		return list;
	}

	/**
	 * 赢取下注
	 * 
	 * @param activityId
	 * @param topup总充值
	 * @param price已下注额
	 * @return
	 */
	public ConfActGamble getActGamble(int activityId, int topup, int price) {
		if (!gambleMap.containsKey(activityId)) {
			return null;
		}
		List<ConfActGamble> list = gambleMap.get(activityId);
		for (ConfActGamble e : list) {
			if (e.getPrice() > price && topup >= e.getTopup()) {
				return e;
			}
		}
		return null;
	}
}
