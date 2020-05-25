package manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.game.bossFight.domain.Boss;
import com.game.util.LogHelper;
import com.google.inject.Singleton;

import data.bean.BossFight;
import data.provider.BossFightProvider;
import define.BossState;
import util.DateUtil;

class ComparatorHurtRank implements Comparator<BossFight> {

	/**
	 * Overriding: compare
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(BossFight o1, BossFight o2) {
		// TODO Auto-generated method stub
		long d1 = o1.getHurt();
		long d2 = o2.getHurt();

		if (d1 < d2)
			return 1;
		else if (d1 > d2) {
			return -1;
		} else {
			return 0;
		}
	}
}

@Singleton
public class BossDataManager {

	// 伤害排行
	private LinkedList<BossFight> hurtRankList = new LinkedList<>();

	// 伤害排行玩家id
	private Set<Long> hurtRankSet = new HashSet<>();

	private Map<Long, BossFight> playerMap = new HashMap<Long, BossFight>();

	private Boss boss;

	@PostConstruct
	public void init() {
		fillPlayer();
		initData();
		initBoss();
	}

	private void fillPlayer() {
		List<BossFight> list = BossFightProvider.getInst().getAllBean();
		BossFight bossFight;
		for (int i = 0; i < list.size(); i++) {
			bossFight = list.get(i);
			if (!SmallIdManager.getInst().isSmallId(bossFight.getLordId())) {
				playerMap.put(bossFight.getLordId(), bossFight);
			}
		}
	}

	private void initData() {
		List<Long> list = GlobalDataManager.getInst().gameGlobal.getHurtRank();
		for (Long roleId : list) {
			BossFight bossFight = playerMap.get(roleId);
			if (bossFight != null) {
				hurtRankList.add(bossFight);
				hurtRankSet.add(roleId);
			}
		}
	}

	private void initBoss() {
		boss = new Boss();
		boss.setBossCreateTime(GlobalDataManager.getInst().gameGlobal.getBossTime());
		boss.setBossLv(GlobalDataManager.getInst().gameGlobal.getBossLv());
		boss.setBossHp(GlobalDataManager.getInst().gameGlobal.getBossHp());
		boss.setBossWhich(GlobalDataManager.getInst().gameGlobal.getBossWhich());
		boss.setBossState(GlobalDataManager.getInst().gameGlobal.getBossState());
	}

	public Map<Long, BossFight> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(Map<Long, BossFight> playerMap) {
		this.playerMap = playerMap;
	}

	public void refreshBoss() {
		Iterator<BossFight> it = playerMap.values().iterator();
		while (it.hasNext()) {
			BossFight bossFight = (BossFight) it.next();
			bossFight.setBless1(0);
			bossFight.setBless2(0);
			bossFight.setBless3(0);
			bossFight.setHurt(0);
		}

		setBossTime(DateUtil.getToday());
		if (boss.getBossState() == BossState.BOSS_DIE) {
			if (boss.getBossLv() < 55) {
				setBossLv(boss.getBossLv() + 1);
			}
		} else if (boss.getBossState() == BossState.INIT_STATE) {
			setBossLv(45);
		}

		boss.setHurt(0);
		setBossHp(10000);
		setBossWhich(0);
		setBossState(BossState.PREPAIR_STATE);
		setKiller("");
		GlobalDataManager.getInst().gameGlobal.getHurtRank().clear();
		GlobalDataManager.getInst().gameGlobal.getGetHurtRank().clear();
		hurtRankList.clear();
		hurtRankSet.clear();

		LogHelper.BOSS_LOGGER.error("refresh boss:" + boss.getBossLv());
	}

	public void setBossState(int state) {
		boss.setBossState(state);
		GlobalDataManager.getInst().gameGlobal.setBossState(state);
		LogHelper.BOSS_LOGGER.error("boss state:" + state);
	}

	public void setBossHp(int hp) {
		boss.setBossHp(hp);
		GlobalDataManager.getInst().gameGlobal.setBossHp(hp);
	}

	public void setBossWhich(int which) {
		boss.setBossWhich(which);
		GlobalDataManager.getInst().gameGlobal.setBossWhich(which);
	}

	public void setBossTime(int time) {
		boss.setBossCreateTime(time);
		GlobalDataManager.getInst().gameGlobal.setBossTime(time);
	}

	public void setBossLv(int lv) {
		boss.setBossLv(lv);
		GlobalDataManager.getInst().gameGlobal.setBossLv(lv);
	}

	public void setKiller(String name) {
		GlobalDataManager.getInst().gameGlobal.setBossKiller(name);
	}

	public BossFight getBossFight(long lordId) {
		return playerMap.get(lordId);
	}

	public int getHurtRank(long lordId) {
		int rank = 0;
		for (BossFight bossFight : hurtRankList) {
			rank++;
			if (bossFight.getLordId() == lordId) {
				return rank;
			}
		}

		return 0;
	}

	public BossFight createBossFight(long lordId) {
		BossFight bossFight = new BossFight();
		bossFight.setLordId(lordId);
		playerMap.put(lordId, bossFight);
		return bossFight;
	}

	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}

	public String getKiller() {
		return GlobalDataManager.getInst().gameGlobal.getBossKiller();
	}

	public LinkedList<BossFight> getHurtRankList() {
		return hurtRankList;
	}

	public void setHurtRankList(LinkedList<BossFight> hurtRankList) {
		this.hurtRankList = hurtRankList;
	}

	public void addHurt(BossFight b, long hurt) {
		b.setHurt(b.getHurt() + hurt);
		setHurtRank(b);
		boss.setHurt(boss.getHurt() + hurt);
	}

	private void setHurtRank(BossFight b) {

		if (hurtRankSet.contains(b.getLordId())) {
			Collections.sort(hurtRankList, new ComparatorHurtRank());
		} else {
			if (hurtRankList.isEmpty()) {
				hurtRankList.add(b);
			} else {
				boolean added = false;
				ListIterator<BossFight> rankIt = hurtRankList.listIterator(hurtRankList.size());
				while (rankIt.hasPrevious()) {
					BossFight e = rankIt.previous();
					if (b.getHurt() <= e.getHurt()) {
						rankIt.next();
						rankIt.add(b);
						added = true;
						break;
					}
				}

				if (!added) {
					hurtRankList.addFirst(b);
				}
			}

			hurtRankSet.add(b.getLordId());
			if (hurtRankList.size() > 10) {
				hurtRankSet.remove(hurtRankList.removeLast().getLordId());
			}
		}
	}

	public boolean hadGetHurtRankAward(long roleId) {
		return GlobalDataManager.getInst().gameGlobal.getGetHurtRank().contains(roleId);
	}

	public void setHurtRankAward(long roleId) {
		GlobalDataManager.getInst().gameGlobal.getGetHurtRank().add(roleId);
	}
}
