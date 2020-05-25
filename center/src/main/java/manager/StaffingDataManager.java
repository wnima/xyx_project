//package manager;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.game.dao.impl.p.ServerLogDao;
//import com.game.dataMgr.StaticStaffingDataMgr;
//import com.game.domain.Player;
//import com.game.util.TimeHelper;
//import com.google.inject.Singleton;
//
//import config.bean.ConfStaffing;
//import config.bean.ConfStaffingWorld;
//import data.bean.Lord;
//import data.bean.WorldLog;
//
//// class ComparatorGroup implements Comparator<Player> {
////
//// /**
//// * Overriding: compare
//// *
//// * @param o1
//// * @param o2
//// * @return
//// * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//// */
//// @Override
//// public int compare(Player o1, Player o2) {
//// // TODO Auto-generated method stub
//// Lord l1 = o1.lord;
//// Lord l2 = o2.lord;
////
//// if (l1.getStaffingLv() < l2.getStaffingLv())
//// return 1;
//// else if (l1.getStaffingLv() > l2.getStaffingLv()) {
//// return -1;
//// } else {
//// if (l1.getStaffingExp() < l2.getStaffingExp()) {
//// return 1;
//// } else if (l1.getStaffingExp() > l2.getStaffingExp()) {
//// return -1;
//// }
//// return 0;
//// }
//// }
//// }
//
//class ComparatorRank implements Comparator<StaffingRank> {
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
//	public int compare(StaffingRank o1, StaffingRank o2) {
//		// TODO Auto-generated method stub
//		Lord l1 = o1.player.lord;
//		Lord l2 = o2.player.lord;
//
//		if (o1.id < o2.id) {
//			return 1;
//		} else if (o1.id > o2.id) {
//			return -1;
//		} else {
//			if (l1.getStaffingLv() < l2.getStaffingLv())
//				return 1;
//			else if (l1.getStaffingLv() > l2.getStaffingLv()) {
//				return -1;
//			} else {
//				if (l1.getStaffingExp() < l2.getStaffingExp()) {
//					return 1;
//				} else if (l1.getStaffingExp() > l2.getStaffingExp()) {
//					return -1;
//				}
//				return 0;
//			}
//		}
//	}
//}
//
//class StaffingRank {
//	public Player player;
//	public int id;
//
//	/**
//	 * @param player
//	 * @param id
//	 */
//	public StaffingRank(Player player, int id) {
//		super();
//		this.player = player;
//		this.id = id;
//	}
//
//}
//
//@Singleton
//public class StaffingDataManager {
//
//	private StaticStaffingDataMgr staticStaffingDataMgr;
//
////	private RankDataManager rankDataManager;
//
//	private PlayerDataManager playerDataManager;
//
//	private WorldLog worldLog;
//
//	private List<StaffingRank> totalList = new ArrayList<>();
//
//	public void addStaffingPlayer(Player player) {
//		if (player.lord.getStaffing() >= 6) {
//			totalList.add(new StaffingRank(player, player.lord.getStaffing()));
//		}
//	}
//
//	public void sortStaffing() {
//
//		Collections.sort(totalList, new ComparatorRank());
//	}
//
//	@PostConstruct
//	public void init() {
//
//		worldLog = serverLogDao.selectLastWorldLog();
//		int currentDay = TimeHelper.getCurrentDay();
//		if (worldLog == null || worldLog.getLvTime() < currentDay) {
//			List<Lord> list = rankDataManager.getRankList(9);
//			int totalLv = 0;
//			for (Lord lord : list) {
//				totalLv += lord.getStaffingLv();
//			}
//
//			staffingWorld = staticStaffingDataMgr.calcWolrdLv(totalLv);
//
//			worldLog = new WorldLog();
//			worldLog.setLvTime(currentDay);
//			worldLog.setTotalLv(totalLv);
//			worldLog.setWorldLv(staffingWorld.getWorldLv());
//			flushWarLog();
//		} else {
//			staffingWorld = staticStaffingDataMgr.calcWolrdLv(worldLog.getTotalLv());
//		}
//	}
//
//	public void reCalcWorldLv() {
//		int currentDay = TimeHelper.getCurrentDay();
//		if (currentDay != worldLog.getLvTime()) {
//			List<Lord> list = rankDataManager.getRankList(9);
//			int totalLv = 0;
//			for (Lord lord : list) {
//				totalLv += lord.getStaffingLv();
//				if (lord.getStaffingLv() == 999) {
//					lord.setStaffingExp((int) (lord.getStaffingExp() * 0.9));
//					playerDataManager.synStaffingToPlayer(playerDataManager.getPlayer(lord.getLordId()));
//				}
//			}
//
//			staffingWorld = staticStaffingDataMgr.calcWolrdLv(totalLv);
//
//			worldLog = new WorldLog();
//			worldLog.setLvTime(currentDay);
//			worldLog.setTotalLv(totalLv);
//			worldLog.setWorldLv(staffingWorld.getWorldLv());
//			flushWarLog();
//		}
//	}
//
//	public int getWorldLv() {
//		return staffingWorld.getWorldLv();
//	}
//
//
//
//	final static int[] LIMIT = { 9999, 30, 10, 6, 3, 1 };
//
//	private void rerank() {
//		if (totalList.isEmpty()) {
//			return;
//		}
//
//		Collections.sort(totalList, new ComparatorRank());
//
//		int maxId = totalList.get(0).id;
//		int[] count = { 0, 0, 0, 0, 0, 0 };
//
//		int id;
//		for (int i = 0; i < totalList.size(); i++) {
//			StaffingRank e = totalList.get(i);
//			id = e.id;
//			if (id > maxId) {
//				id = maxId;
//			}
//
//			if (count[id - 6] < LIMIT[id - 6]) {
//				if (id != e.player.lord.getStaffing()) {
//					e.player.lord.setStaffing(id);
//					playerDataManager.synStaffingToPlayer(e.player);
//				}
//
//				count[id - 6]++;
//			} else {
//				maxId = id - 1;
//				i--;
//			}
//		}
//	}
//
//	private void removeRank(Player player) {
//		for (Iterator<StaffingRank> iterator = totalList.iterator(); iterator.hasNext();) {
//			if (iterator.next().player.roleId == player.roleId) {
//				iterator.remove();
//				return;
//			}
//		}
//	}
//
//	private StaffingRank findRank(Player player) {
//		StaffingRank rank = null;
//		for (Iterator<StaffingRank> iterator = totalList.iterator(); iterator.hasNext();) {
//			rank = iterator.next();
//			if (rank.player.roleId == player.roleId) {
//				return rank;
//			}
//		}
//		return null;
//	}
//
//	public int calcStaffing(Player player) {
//		int calcId = 0;
//		int preId = player.lord.getStaffing();
//
//		ConfStaffing staticStaffing = staticStaffingDataMgr.calcStaffing(player.lord.getStaffingLv(), player.lord.getRanks());
//		if (staticStaffing == null) {
//			calcId = 0;
//		} else {
//			calcId = staticStaffing.getStaffingId();
//		}
//
//		if (preId < 6 && calcId < 6) {
//			player.lord.setStaffing(calcId);
//			playerDataManager.synStaffingToPlayer(player);
//			return calcId;
//		}
//
//		if (preId < 6) {// 上排行榜
//			totalList.add(new StaffingRank(player, calcId));
//			rerank();
//		} else {
//			if (calcId < 6) {// 下榜
//				removeRank(player);
//				player.lord.setStaffing(calcId);
//				playerDataManager.synStaffingToPlayer(player);
//
//				rerank();
//			} else {// 重新排
//				StaffingRank rank = findRank(player);
//				if (rank != null)
//					rank.id = calcId;
//				rerank();
//			}
//		}
//
//		return player.lord.getStaffing();
//	}
//}
