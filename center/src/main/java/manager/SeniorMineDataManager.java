package manager;
//package com.game.manager;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.game.dataMgr.StaticWorldDataMgr;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.SeniorPartyScoreRank;
//import com.game.domain.SeniorScoreRank;
//import com.game.domain.p.Army;
//import com.game.domain.p.Guard;
//import com.game.domain.s.StaticMine;
//import com.game.domain.s.StaticMineForm;
//import com.game.util.Turple;
//
///**
// * @ClassName: SenioMineDataManager
// * @Description: TODO
// * @author ZhangJun
// * @date 2016年3月14日 下午2:56:50
// * 
// */
//
//class ComparatorScore implements Comparator<SeniorScoreRank> {
//
//	/**
//	 * Overriding: compare
//	 * 
//	 * @param o1
//	 * @param o2
//	 * @return
//	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public int compare(SeniorScoreRank o1, SeniorScoreRank o2) {
//		// TODO Auto-generated method stub
//		int d1 = o1.getScore();
//		int d2 = o2.getScore();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		} else {
//			long v1 = o1.getFight();
//			long v2 = o2.getFight();
//			if (v1 < v2) {
//				return 1;
//			} else if (v1 > v2) {
//				return -1;
//			}
//
//			return 0;
//		}
//	}
//}
//
//class ComparatorPartyScore implements Comparator<SeniorPartyScoreRank> {
//
//	/**
//	 * Overriding: compare
//	 * 
//	 * @param o1
//	 * @param o2
//	 * @return
//	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public int compare(SeniorPartyScoreRank o1, SeniorPartyScoreRank o2) {
//		// TODO Auto-generated method stub
//		int d1 = o1.getScore();
//		int d2 = o2.getScore();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		} else {
//			long v1 = o1.getFight();
//			long v2 = o2.getFight();
//			if (v1 < v2) {
//				return 1;
//			} else if (v1 > v2) {
//				return -1;
//			}
//
//			return 0;
//		}
//	}
//}
//
//@Component
//public class SeniorMineDataManager {
//	@Autowired
//	private StaticWorldDataMgr staticWorldDataMgr;
//
//	@Autowired
//	private GlobalDataManager globalDataManager;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	// 军事矿区驻军数据
//	private Map<Integer, List<Guard>> guardMap = new HashMap<>();
//
//	// 矿的防守阵型
//	private Map<Integer, StaticMineForm> mineFormMap = new HashMap<>();
//
//	// 积分排行玩家id
//	private Set<Long> scoreRankSet = new HashSet<>();
//
//	// 积分排行玩家id
//	private Set<Integer> partyScoreRankSet = new HashSet<>();
//
//	@PostConstruct
//	public void init() {
//		initData();
//	}
//
//	private void initData() {
//		for (SeniorScoreRank rank : globalDataManager.gameGlobal.getScoreRank()) {
//			scoreRankSet.add(rank.getLordId());
//		}
//
//		for (SeniorPartyScoreRank rank : globalDataManager.gameGlobal.getScorePartyRank()) {
//			partyScoreRankSet.add(rank.getPartyId());
//		}
//	}
//
//	public Map<Integer, List<Guard>> getGuardMap() {
//		return guardMap;
//	}
//
//	public void setGuardMap(Map<Integer, List<Guard>> guardMap) {
//		this.guardMap = guardMap;
//	}
//
//	public StaticMine evaluatePos(int pos) {
//		StaticMine staticMine = staticWorldDataMgr.getSeniorMine(pos);
//		return staticMine;
//	}
//
//	public StaticMineForm getMineForm(int pos, int lv) {
//		StaticMineForm form = mineFormMap.get(pos);
//		if (form == null) {
//			form = staticWorldDataMgr.randomForm(lv);
//			mineFormMap.put(pos, form);
//		}
//		return form;
//	}
//
//	public Guard getMineGuard(int pos) {
//		List<Guard> list = guardMap.get(pos);
//		if (list != null && !list.isEmpty()) {
//			return list.get(0);
//		}
//		return null;
//	}
//
//	public void resetMineForm(int pos, int lv) {
//		mineFormMap.put(pos, staticWorldDataMgr.randomForm(lv));
//	}
//
//	public void setGuard(Guard guard) {
//		int pos = guard.getArmy().getTarget();
//		List<Guard> list = guardMap.get(pos);
//		if (list == null) {
//			list = new ArrayList<>();
//			guardMap.put(pos, list);
//		}
//		list.add(guard);
//	}
//
//	public void removeGuard(Guard guard) {
//		int pos = guard.getArmy().getTarget();
//		guardMap.get(pos).remove(guard);
//	}
//
//	public void removeGuard(int pos) {
//		guardMap.remove(pos);
//	}
//
//	public void removeGuard(Player player, Army army) {
//		int pos = army.getTarget();
//		List<Guard> list = guardMap.get(pos);
//		Guard e;
//		if (list != null) {
//			for (int i = 0; i < list.size(); i++) {
//				e = list.get(i);
//				if (e.getPlayer() == player && e.getArmy().getKeyId() == army.getKeyId()) {
//					list.remove(i);
//					break;
//				}
//			}
//		}
//	}
//
//	private SeniorScoreRank findRank(Player player) {
//		for (SeniorScoreRank one : globalDataManager.gameGlobal.getScoreRank()) {
//			if (one.getLordId() == player.roleId) {
//				return one;
//			}
//		}
//
//		return null;
//	}
//
//	private SeniorPartyScoreRank findRank(PartyData partyData) {
//		for (SeniorPartyScoreRank one : globalDataManager.gameGlobal.getScorePartyRank()) {
//			if (one.getPartyId() == partyData.getPartyId()) {
//				return one;
//			}
//		}
//		return null;
//	}
//
//	public void setScoreRank(Player player) {
//
//		LinkedList<SeniorScoreRank> list = globalDataManager.gameGlobal.getScoreRank();
//		SeniorScoreRank rank;
//		if (scoreRankSet.contains(player.roleId)) {
//			rank = findRank(player);
//			if (rank != null) {
//				if (player.seniorScore < 150) {
//					list.remove(rank);
//					scoreRankSet.remove(player.roleId);
//					return;
//				}
//
//				rank.setScore(player.seniorScore);
//				rank.setFight(player.lord.getFight());
//				Collections.sort(list, new ComparatorScore());
//			}
//		} else {
//			if (player.seniorScore < 150) {
//				return;
//			}
//
//			if (list.isEmpty()) {
//				list.add(new SeniorScoreRank(player));
//			} else {
//				boolean added = false;
//				ListIterator<SeniorScoreRank> rankIt = list.listIterator(list.size());
//				while (rankIt.hasPrevious()) {
//					SeniorScoreRank e = rankIt.previous();
//					if (player.seniorScore < e.getScore()) {
//						rankIt.next();
//						rankIt.add(new SeniorScoreRank(player));
//						added = true;
//						break;
//					} else if (player.seniorScore == e.getScore()) {
//						if (player.lord.getFight() <= e.getFight()) {
//							rankIt.next();
//							rankIt.add(new SeniorScoreRank(player));
//							added = true;
//							break;
//						}
//					}
//				}
//
//				if (!added) {
//					list.addFirst(new SeniorScoreRank(player));
//				}
//			}
//
//			scoreRankSet.add(player.roleId);
//			if (list.size() > 10) {
//				scoreRankSet.remove(list.removeLast().getLordId());
//			}
//		}
//	}
//
//	public void setPartyScoreRank(PartyData partyData) {
//
//		LinkedList<SeniorPartyScoreRank> list = globalDataManager.gameGlobal.getScorePartyRank();
//		SeniorPartyScoreRank rank;
//		if (partyScoreRankSet.contains(partyData.getPartyId())) {
//			rank = findRank(partyData);
//			if (rank != null) {
//				if (partyData.getScore() < 800) {
//					partyScoreRankSet.remove(partyData.getPartyId());
//					list.remove(rank);
//					return;
//				}
//
//				rank.setScore(partyData.getScore());
//				rank.setFight(partyData.getFight());
//				Collections.sort(list, new ComparatorPartyScore());
//			}
//		} else {
//			if (partyData.getScore() < 800) {
//				return;
//			}
//
//			if (list.isEmpty()) {
//				list.add(new SeniorPartyScoreRank(partyData));
//			} else {
//				boolean added = false;
//				ListIterator<SeniorPartyScoreRank> rankIt = list.listIterator(list.size());
//				while (rankIt.hasPrevious()) {
//					SeniorPartyScoreRank e = rankIt.previous();
//					if (partyData.getScore() < e.getScore()) {
//						rankIt.next();
//						rankIt.add(new SeniorPartyScoreRank(partyData));
//						added = true;
//						break;
//					} else if (partyData.getScore() == e.getScore()) {
//						if (partyData.getFight() <= e.getFight()) {
//							rankIt.next();
//							rankIt.add(new SeniorPartyScoreRank(partyData));
//							added = true;
//							break;
//						}
//					}
//				}
//
//				if (!added) {
//					list.addFirst(new SeniorPartyScoreRank(partyData));
//				}
//			}
//
//			partyScoreRankSet.add(partyData.getPartyId());
//			if (list.size() > 5) {
//				partyScoreRankSet.remove(list.removeLast().getPartyId());
//			}
//		}
//	}
//
//	public Turple<Integer, SeniorScoreRank> getScoreRank(long roleId) {
//		if (scoreRankSet.contains(roleId)) {
//			int rank = 0;
//			for (SeniorScoreRank e : globalDataManager.gameGlobal.getScoreRank()) {
//				rank++;
//				if (e.getLordId() == roleId) {
//					return new Turple<Integer, SeniorScoreRank>(rank, e);
//				}
//			}
//		}
//
//		return new Turple<Integer, SeniorScoreRank>(0, null);
//	}
//
//	public Turple<Integer, SeniorPartyScoreRank> getPartyScoreRank(int partyId) {
//		if (partyScoreRankSet.contains(partyId)) {
//			int rank = 0;
//			for (SeniorPartyScoreRank e : globalDataManager.gameGlobal.getScorePartyRank()) {
//				rank++;
//				if (e.getPartyId() == partyId) {
//					return new Turple<Integer, SeniorPartyScoreRank>(rank, e);
//				}
//			}
//		}
//
//		return new Turple<Integer, SeniorPartyScoreRank>(0, null);
//	}
//
//	public List<SeniorScoreRank> getScoreRankList() {
//		return globalDataManager.gameGlobal.getScoreRank();
//	}
//
//	public List<SeniorPartyScoreRank> getScorePartyRankList() {
//		return globalDataManager.gameGlobal.getScorePartyRank();
//	}
//	
//	public void clearRank() {
//		globalDataManager.gameGlobal.getScoreRank().clear();
//		globalDataManager.gameGlobal.getScorePartyRank().clear();
//		partyScoreRankSet.clear();
//		scoreRankSet.clear();
//	}
//
//	public int getSeniorState() {
////		return SeniorState.START_STATE;
////		return SeniorState.END_STATE;
//		 return globalDataManager.gameGlobal.getSeniorState();
//	}
//
//	public void setSeniorState(int state) {
//		globalDataManager.gameGlobal.setSeniorState(state);
//	}
//
//	public void setSeniorWeek(int week) {
//		globalDataManager.gameGlobal.setSeniorWeek(week);
//	}
//}
