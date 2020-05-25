package manager;
//
//import io.netty.util.internal.ConcurrentSet;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.stereotype.Component;
//
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.p.Equip;
//import com.game.domain.p.Lord;
//import com.game.domain.p.PartyLvRank;
//import com.game.domain.p.PartyRank;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.GamePb.GetRankRs;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//
//class EquipRank {
//	private Lord lord;
//	private Equip equip;
//
//	public Lord getLord() {
//		return lord;
//	}
//
//	public void setLord(Lord lord) {
//		this.lord = lord;
//	}
//
//	public Equip getEquip() {
//		return equip;
//	}
//
//	public void setEquip(Equip equip) {
//		this.equip = equip;
//	}
//
//	/**
//	 * @param lord
//	 * @param equip
//	 */
//	public EquipRank(Lord lord, Equip equip) {
//		super();
//		this.lord = lord;
//		this.equip = equip;
//	}
//
//}
//
//class RankList {
//	private LinkedList<Lord> list = new LinkedList<>();
//	private int size = 0;
//
//	public LinkedList<Lord> getList() {
//		return list;
//	}
//
//	public void setList(LinkedList<Lord> list) {
//		this.list = list;
//	}
//
//	public int getSize() {
//		return size;
//	}
//
//	public void setSize(int size) {
//		this.size = size;
//	}
//
//	public void add(Lord lord) {
//		list.add(lord);
//		++size;
//	}
//
//	public long removeLast() {
//		Lord lord = list.removeLast();
//		--size;
//		return lord.getLordId();
//	}
//}
//
//class EquipRankList {
//	private LinkedList<EquipRank> list = new LinkedList<>();
//	private int size = 0;
//
//	public LinkedList<EquipRank> getList() {
//		return list;
//	}
//
//	public void setList(LinkedList<EquipRank> list) {
//		this.list = list;
//	}
//
//	public int getSize() {
//		return size;
//	}
//
//	public void setSize(int size) {
//		this.size = size;
//	}
//
//	public void add(Lord lord, Equip equip) {
//		EquipRank equipRank = new EquipRank(lord, equip);
//		list.add(equipRank);
//		++size;
//	}
//
//	public void removeLast() {
//		list.removeLast();
//		--size;
//	}
//}
//
//class ComparatorFight implements Comparator<Lord> {
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
//	public int compare(Lord o1, Lord o2) {
//		// TODO Auto-generated method stub
//		long d1 = o1.getFight();
//		long d2 = o2.getFight();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		}
//
//		return 0;
//	}
//}
//
//class ComparatorStars implements Comparator<Lord> {
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
//	public int compare(Lord o1, Lord o2) {
//		// TODO Auto-generated method stub
//		int d1 = o1.getStars();
//		int d2 = o2.getStars();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		}
//
//		return 0;
//	}
//}
//
//class ComparatorExtreme implements Comparator<Player> {
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
//	public int compare(Player o1, Player o2) {
//		// TODO Auto-generated method stub
//		if (o1.extrMark < o2.extrMark)
//			return 1;
//		else if (o1.extrMark > o2.extrMark) {
//			return -1;
//		} else {
//			long v1 = o1.lord.getFight();
//			long v2 = o2.lord.getFight();
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
//class ComparatorHonour implements Comparator<Lord> {
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
//	public int compare(Lord o1, Lord o2) {
//		// TODO Auto-generated method stu
//		int d1 = o1.getHonour();
//		int d2 = o2.getHonour();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		}
//
//		return 0;
//	}
//}
//
//class ComparatorStaffing implements Comparator<Lord> {
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
//	public int compare(Lord o1, Lord o2) {
//		// TODO Auto-generated method stu
//		if (o1.getStaffingLv() < o2.getStaffingLv())
//			return 1;
//		else if (o1.getStaffingLv() > o2.getStaffingLv()) {
//			return -1;
//		} else {
//			long v1 = o1.getStaffingExp();
//			long v2 = o2.getStaffingExp();
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
//class ComparatorEquip implements Comparator<EquipRank> {
//
//	public static final int[] FACTOR = { 10, 1, 3, 5 };
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
//	public int compare(EquipRank o1, EquipRank o2) {
//		// TODO Auto-generated method stub
//		// 这个地方的装备加成比较，为了提高性能，暂时写死
//		int d1 = (o1.getEquip().getLv() + 9) * FACTOR[o1.getEquip().getEquipId() % 4];
//		int d2 = (o2.getEquip().getLv() + 9) * FACTOR[o2.getEquip().getEquipId() % 4];
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		}
//
//		return 0;
//	}
//}
//
//class ComparatorPartyFight implements Comparator<PartyRank> {
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
//	public int compare(PartyRank o1, PartyRank o2) {
//		// TODO Auto-generated method stub
//		long d1 = o1.getFight();
//		long d2 = o2.getFight();
//
//		if (d1 < d2)
//			return 1;
//		else if (d1 > d2) {
//			return -1;
//		}
//
//		return 0;
//	}
//}
//
//class ComparatorPartyLv implements Comparator<PartyLvRank> {
//
//	@Override
//	public int compare(PartyLvRank o1, PartyLvRank o2) {
//		if (o1.getPartyLv() < o2.getPartyLv()) {
//			return 1;
//		} else if (o1.getPartyLv() > o2.getPartyLv()) {
//			return -1;
//		} else {// 等级相同则已科技馆等级进行倒叙排序
//			if (o1.getScienceLv() < o2.getScienceLv()) {
//				return 1;
//			} else if (o1.getScienceLv() > o2.getScienceLv()) {
//				return -1;
//			} else {// 科技馆等级相同则以福利院进行倒叙排序
//				if (o1.getWealLv() < o2.getWealLv()) {
//					return 1;
//				} else if (o1.getWealLv() > o2.getWealLv()) {
//					return -1;
//				} else {// 福利院等级相同则已贡献度进行排名
//					if (o1.getBuild() < o2.getBuild()) {
//						return 1;
//					} else if (o1.getBuild() > o2.getBuild()) {
//						return -1;
//					}
//				}
//			}
//		}
//		return 0;
//	}
//}
//
//class PartyLvRankList {
//	public int status = 0;// 活动开启时间
//	private ConcurrentSet<Integer> sets = new ConcurrentSet<Integer>();
//	private LinkedList<PartyLvRank> list = new LinkedList<PartyLvRank>();
//
//	public LinkedList<PartyLvRank> getList() {
//		return list;
//	}
//
//	public ConcurrentSet<Integer> getSets() {
//		return sets;
//	}
//
//	public void addPartylvRank(PartyLvRank partyLvRank) {
//		list.add(partyLvRank);
//	}
//}
//
//@Component
//public class RankDataManager {
//	public RankList fightRankList = new RankList();
//	public Set<Long> fightRankSet = new HashSet<>();
//
//	public RankList starsRankList = new RankList();
//	public Set<Long> starsRankSet = new HashSet<>();
//
//	public RankList honourRankList = new RankList();
//	public Set<Long> honourRankSet = new HashSet<>();
//
//	public EquipRankList attackRankList = new EquipRankList();
//	public EquipRankList critRankList = new EquipRankList();
//	public EquipRankList dodgeRankList = new EquipRankList();
//
//	public PartyLvRankList partyLvRankList = new PartyLvRankList();
//
//	public LinkedList<Player> extremeRankList = new LinkedList<>();
//	public Set<Long> extremeRankSet = new HashSet<>();
//
//	public RankList staffingRankList = new RankList();
//	public Set<Long> staffingRankSet = new HashSet<>();
//	
//	/**
//	 * 判断是否战力前5
//	* Method: isFightRankTop5    
//	* @Description: TODO    
//	* @param lordId
//	* @return    
//	* @return boolean    
//	* @throws
//	 */
//	public boolean isFightRankTop5(long lordId) {
//		if (fightRankList.getList().size() > 5) {
//			return (fightRankList.getList().get(0).getLordId() == lordId) || (fightRankList.getList().get(1).getLordId() == lordId)
//					|| (fightRankList.getList().get(2).getLordId() == lordId) || (fightRankList.getList().get(3).getLordId() == lordId)
//					|| (fightRankList.getList().get(4).getLordId() == lordId);
//		}
//		return true;
//	}
//
//	public void load(Player player) {
//		Lord lord = player.lord;
//		fightRankList.add(lord);
//		if (lord.getStars() > 0) {
//			starsRankList.add(lord);
//		}
//
//		honourRankList.add(lord);
//
//		// if (TimeHelper.isStaffingOpen())
//		staffingRankList.add(lord);
//
//		if (player.extrMark > 0) {
//			extremeRankList.add(player);
//		}
//
//		loadEquip(lord, player.equips);
//	}
//
//	private void loadEquip(Lord lord, HashMap<Integer, HashMap<Integer, Equip>> equips) {
//		for (int i = 0; i < 7; i++) {
//			Map<Integer, Equip> map = equips.get(i);
//			Iterator<Equip> it = map.values().iterator();
//			int index = 0;
//			while (it.hasNext()) {
//				Equip equip = (Equip) it.next();
//				index = equip.getEquipId() / 100;
//				if (index == 1) {
//					attackRankList.add(lord, equip);
//				} else if (index == 4) {
//					dodgeRankList.add(lord, equip);
//				} else if (index == 5) {
//					critRankList.add(lord, equip);
//				}
//			}
//		}
//	}
//
//	public void sort() {
//		Collections.sort(fightRankList.getList(), new ComparatorFight());
//		while (fightRankList.getSize() > 100) {
//			fightRankList.removeLast();
//		}
//
//		Collections.sort(starsRankList.getList(), new ComparatorStars());
//		while (starsRankList.getSize() > 100) {
//			starsRankList.removeLast();
//		}
//
//		Collections.sort(honourRankList.getList(), new ComparatorHonour());
//		while (honourRankList.getSize() > 100) {
//			honourRankList.removeLast();
//		}
//
//		Collections.sort(staffingRankList.getList(), new ComparatorStaffing());
//		while (staffingRankList.getSize() > 100) {
//			staffingRankList.removeLast();
//		}
//
//		Collections.sort(extremeRankList, new ComparatorExtreme());
//		while (extremeRankList.size() > 100) {
//			extremeRankList.removeLast();
//		}
//
//		Collections.sort(attackRankList.getList(), new ComparatorEquip());
//		while (attackRankList.getSize() > 100) {
//			attackRankList.removeLast();
//		}
//
//		Collections.sort(critRankList.getList(), new ComparatorEquip());
//		while (critRankList.getSize() > 100) {
//			critRankList.removeLast();
//		}
//
//		Collections.sort(dodgeRankList.getList(), new ComparatorEquip());
//		while (dodgeRankList.getSize() > 100) {
//			dodgeRankList.removeLast();
//		}
//
//		for (Lord lord : fightRankList.getList()) {
//			fightRankSet.add(lord.getLordId());
//		}
//
//		for (Lord lord : starsRankList.getList()) {
//			starsRankSet.add(lord.getLordId());
//		}
//
//		for (Lord lord : honourRankList.getList()) {
//			honourRankSet.add(lord.getLordId());
//		}
//
//		for (Lord lord : staffingRankList.getList()) {
//			staffingRankSet.add(lord.getLordId());
//		}
//
//		for (Player player : extremeRankList) {
//			extremeRankSet.add(player.roleId);
//		}
//
//		for (Lord lord : fightRankList.getList()) {
//			LogHelper.ERROR_LOGGER.error(lord.getNick() + "|" + lord.getFight());
//		}
//	}
//
//	public void setStars(Lord lord) {
//		if (starsRankSet.contains(lord.getLordId())) {
//			Collections.sort(starsRankList.getList(), new ComparatorStars());
//		} else {
//			int size = starsRankList.getSize();
//			if (size == 0) {
//				starsRankList.add(lord);
//				starsRankSet.add(lord.getLordId());
//			} else {
//				boolean added = false;
//				ListIterator<Lord> it = starsRankList.getList().listIterator(size);
//				while (it.hasPrevious()) {
//					if (lord.getStars() <= it.previous().getStars()) {
//						it.next();
//						it.add(lord);
//						added = true;
//						break;
//					}
//				}
//
//				if (!added) {
//					starsRankList.getList().addFirst(lord);
//				}
//
//				starsRankList.setSize(size + 1);
//				starsRankSet.add(lord.getLordId());
//
//				if (starsRankList.getSize() > 100) {
//					starsRankSet.remove(starsRankList.removeLast());
//				}
//			}
//
//		}
//	}
//
//	public void setHonour(Lord lord) {
//		if (honourRankSet.contains(lord.getLordId())) {
//			Collections.sort(honourRankList.getList(), new ComparatorHonour());
//		} else {
//			int size = honourRankList.getSize();
//			if (size == 0) {
//				honourRankList.add(lord);
//				honourRankSet.add(lord.getLordId());
//			} else {
//				boolean added = false;
//				ListIterator<Lord> it = honourRankList.getList().listIterator(size);
//				Lord e = null;
//				while (it.hasPrevious()) {
//					e = it.previous();
//					// LogHelper.ERROR_LOGGER.error("setHonour " +
//					// lord.getNick() + ":" + lord.getHonour() + "|" +
//					// e.getNick() + ":" + e.getHonour());
//					if (lord.getHonour() <= e.getHonour()) {
//						it.next();
//						it.add(lord);
//						added = true;
//						// LogHelper.ERROR_LOGGER.error("setHonour added");
//						break;
//					}
//				}
//
//				if (!added) {
//					honourRankList.getList().addFirst(lord);
//				}
//
//				honourRankList.setSize(size + 1);
//				honourRankSet.add(lord.getLordId());
//
//				if (honourRankList.getSize() > 100) {
//					honourRankSet.remove(honourRankList.removeLast());
//				}
//			}
//		}
//	}
//
//	public void setStaffing(Lord lord) {
//		if (staffingRankSet.contains(lord.getLordId())) {
//			Collections.sort(staffingRankList.getList(), new ComparatorStaffing());
//		} else {
//			int size = staffingRankList.getSize();
//			if (size == 0) {
//				staffingRankList.add(lord);
//				staffingRankSet.add(lord.getLordId());
//			} else {
//				boolean added = false;
//				ListIterator<Lord> it = staffingRankList.getList().listIterator(size);
//				Lord e = null;
//				while (it.hasPrevious()) {
//					e = it.previous();
//					if (lord.getStaffingLv() < e.getStaffingLv()) {
//						it.next();
//						it.add(lord);
//						added = true;
//						break;
//					} else if (lord.getStaffingLv() == e.getStaffingLv()) {
//						if (lord.getStaffingExp() <= e.getStaffingExp()) {
//							it.next();
//							it.add(lord);
//							added = true;
//							break;
//						}
//					}
//				}
//
//				if (!added) {
//					staffingRankList.getList().addFirst(lord);
//				}
//
//				staffingRankList.setSize(size + 1);
//				staffingRankSet.add(lord.getLordId());
//
//				if (staffingRankList.getSize() > 100) {
//					staffingRankSet.remove(staffingRankList.removeLast());
//				}
//			}
//		}
//	}
//
//	public void setExtreme(Player player) {
//		if (extremeRankSet.contains(player.roleId)) {
//			Collections.sort(extremeRankList, new ComparatorExtreme());
//		} else {
//			int size = extremeRankList.size();
//			if (size == 0) {
//				extremeRankList.add(player);
//				extremeRankSet.add(player.roleId);
//			} else {
//				boolean added = false;
//				ListIterator<Player> it = extremeRankList.listIterator(size);
//				Player e = null;
//				while (it.hasPrevious()) {
//					e = it.previous();
//					if (player.extrMark < e.extrMark) {
//						it.next();
//						it.add(player);
//						added = true;
//						break;
//					} else if (player.extrMark == e.extrMark) {
//						if (player.lord.getFight() <= e.lord.getFight()) {
//							it.next();
//							it.add(player);
//							added = true;
//							break;
//						}
//					}
//				}
//
//				if (!added) {
//					extremeRankList.addFirst(player);
//				}
//
//				extremeRankSet.add(player.roleId);
//
//				if (extremeRankList.size() > 100) {
//					extremeRankSet.remove(extremeRankList.removeLast().roleId);
//				}
//			}
//		}
//	}
//
//	public void setFight(Lord lord) {
//		if (fightRankSet.contains(lord.getLordId())) {
//			Collections.sort(fightRankList.getList(), new ComparatorFight());
//		} else {
//			int size = fightRankList.getSize();
//			if (size == 0) {
//				fightRankList.add(lord);
//				fightRankSet.add(lord.getLordId());
//			} else {
//				boolean added = false;
//				ListIterator<Lord> it = fightRankList.getList().listIterator(size);
//				while (it.hasPrevious()) {
//					if (lord.getFight() <= it.previous().getFight()) {
//						it.next();
//						it.add(lord);
//						added = true;
//						break;
//					}
//				}
//
//				if (!added) {
//					fightRankList.getList().addFirst(lord);
//				}
//
//				fightRankList.setSize(size + 1);
//				fightRankSet.add(lord.getLordId());
//
//				if (fightRankList.getSize() > 100) {
//					fightRankSet.remove(fightRankList.removeLast());
//				}
//			}
//		}
//	}
//
//	public void setEquip(Lord lord, Equip equip, EquipRankList list) {
//		boolean find = false;
//		for (EquipRank e : list.getList()) {
//			if (e.getLord().getLordId() == lord.getLordId() && e.getEquip().getKeyId() == equip.getKeyId()) {
//				find = true;
//				break;
//			}
//		}
//
//		if (find) {
//			Collections.sort(list.getList(), new ComparatorEquip());
//		} else {
//			int size = list.getSize();
//			if (size == 0) {
//				list.add(lord, equip);
//			} else {
//				boolean added = false;
//				Equip in = null;
//				ListIterator<EquipRank> it = list.getList().listIterator(size);
//				while (it.hasPrevious()) {
//					in = it.previous().getEquip();
//					int d1 = (equip.getLv() + 9) * ComparatorEquip.FACTOR[equip.getEquipId() % 4];
//					int d2 = (in.getLv() + 9) * ComparatorEquip.FACTOR[in.getEquipId() % 4];
//
//					if (d1 <= d2) {
//						it.next();
//						it.add(new EquipRank(lord, equip));
//						added = true;
//						break;
//					}
//				}
//
//				if (!added) {
//					list.getList().addFirst(new EquipRank(lord, equip));
//				}
//
//				list.setSize(size + 1);
//
//				if (list.getSize() > 100) {
//					list.removeLast();
//				}
//			}
//		}
//	}
//
//	public void updatePartyLv(PartyData partyData) {
//		int partyId = partyData.getPartyId();
//		Iterator<PartyLvRank> it = partyLvRankList.getList().iterator();
//		while (it.hasNext()) {
//			PartyLvRank e = it.next();
//			if (e.getPartyId() == partyId) {
//				e.setPartyLv(partyData.getPartyLv());
//				e.setScienceLv(partyData.getScienceLv());
//				e.setWealLv(partyData.getWealLv());
//				e.setBuild(partyData.getBuild());
//				break;
//			}
//		}
//		Collections.sort(partyLvRankList.getList(), new ComparatorPartyLv());
//	}
//
//	public LinkedList<PartyLvRank> getPartyLvRankList() {
//		return partyLvRankList.getList();
//	}
//
//	public void LoadPartyLv(int partyId, String partyName, int partyLv, int scienceLv, int wealLv, int build) {
//		PartyLvRank partyLvRank = new PartyLvRank(partyId, partyName, partyLv, scienceLv, wealLv, build);
//		getPartyLvRankList().add(partyLvRank);
//	}
//
//	public void setAttack(Lord lord, Equip equip) {
//		setEquip(lord, equip, attackRankList);
//	}
//
//	public void setCrit(Lord lord, Equip equip) {
//		setEquip(lord, equip, critRankList);
//	}
//
//	public void setDodge(Lord lord, Equip equip) {
//		setEquip(lord, equip, dodgeRankList);
//	}
//
//	public int getPlayerRank(int type, long lordId) {
//		int rank = 0;
//		switch (type) {
//		case 1: {// 战力榜
//			Iterator<Lord> it = fightRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 2: {// 关卡榜
//			Iterator<Lord> it = starsRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 3: {// 荣誉榜
//			Iterator<Lord> it = honourRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 4: {// 攻击强化
//			Iterator<EquipRank> it = attackRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLord().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 5: {// 暴击强化
//			Iterator<EquipRank> it = critRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLord().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 6: {// 闪避强化
//			Iterator<EquipRank> it = dodgeRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLord().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 8: {// 极限副本
//			Iterator<Player> it = extremeRankList.iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().roleId == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		case 9: {// 编制榜
//			Iterator<Lord> it = staffingRankList.getList().iterator();
//			while (it.hasNext()) {
//				rank++;
//				if (it.next().getLordId() == lordId) {
//					return rank;
//				}
//			}
//			break;
//		}
//		}
//
//		return 0;
//	}
//
//	/**
//	 * Function:活动时
//	 * 
//	 * @param type
//	 *            1等级 ，2战斗力
//	 * @param partyId
//	 * @return
//	 */
//	public PartyLvRank getPartyRank(int partyId) {
//		int rank = 0;
//		Iterator<PartyLvRank> it = partyLvRankList.getList().iterator();
//		while (it.hasNext()) {
//			rank++;
//			PartyLvRank partyLvRank = it.next();
//			if (partyLvRank.getPartyId() == partyId) {
//				partyLvRank.setRank(rank);
//				return partyLvRank;
//			}
//		}
//		return null;
//	}
//
//	public List<PartyLvRank> getPartyLvRank(int page) {
//		List<PartyLvRank> rs = new ArrayList<PartyLvRank>();
//		int count = 0;
//		int[] pages = { page * 20, (page + 1) * 20 };
//		Iterator<PartyLvRank> it = partyLvRankList.getList().iterator();
//		while (it.hasNext()) {
//			PartyLvRank next = it.next();
//			if (count >= pages[0]) {
//				rs.add(next);
//			}
//			if (++count >= pages[1]) {
//				break;
//			}
//		}
//
//		return rs;
//	}
//
//	public void getRank(int type, int page, ClientHandler handler) {
//		GetRankRs.Builder builder = GetRankRs.newBuilder();
//		int begin = (page - 1) * 20;
//		int end = page * 20;
//		int index = 0;
//		switch (type) {
//		case 1: {// 战力榜
//			Iterator<Lord> it = fightRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				Lord lord = (Lord) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(lord.getNick(), lord.getLevel(), lord.getFight()));
//				}
//
//				++index;
//			}
//
//			break;
//		}
//		case 2: {// 关卡榜
//			Iterator<Lord> it = starsRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				Lord lord = (Lord) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(lord.getNick(), lord.getLevel(), lord.getStars()));
//				}
//
//				++index;
//			}
//			break;
//		}
//		case 3: {// 荣誉榜
//			Iterator<Lord> it = honourRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				Lord lord = (Lord) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(lord.getNick(), lord.getLevel(), lord.getHonour()));
//				}
//				++index;
//			}
//			break;
//		}
//		case 4: {// 攻击强化
//			Iterator<EquipRank> it = attackRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				EquipRank e = (EquipRank) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(e.getLord().getNick(), e.getEquip().getLv(), e.getEquip().getEquipId()));
//				}
//				++index;
//			}
//			break;
//		}
//		case 5: {// 暴击强化
//			Iterator<EquipRank> it = critRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				EquipRank e = (EquipRank) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(e.getLord().getNick(), e.getEquip().getLv(), e.getEquip().getEquipId()));
//				}
//				++index;
//			}
//			break;
//		}
//		case 6: {// 闪避强化
//			Iterator<EquipRank> it = dodgeRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				EquipRank e = (EquipRank) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(e.getLord().getNick(), e.getEquip().getLv(), e.getEquip().getEquipId()));
//				}
//				++index;
//			}
//			break;
//		}
//		case 8: {// 极限副本
//			Iterator<Player> it = extremeRankList.iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				Player player = (Player) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(player.lord.getNick(), player.extrMark, player.lord.getFight()));
//				}
//				++index;
//			}
//			break;
//		}
//		case 9: {// 编制
//			Iterator<Lord> it = staffingRankList.getList().iterator();
//			while (it.hasNext()) {
//				if (index >= end) {
//					break;
//				}
//
//				Lord lord = (Lord) it.next();
//				if (index >= begin) {
//					builder.addRankData(PbHelper.createRankData(lord.getNick(), lord.getStaffing(), lord.getStaffingLv()));
//				}
//				++index;
//			}
//			break;
//		}
//		default:
//			break;
//		}
//
//		if (page == 1) {
//			builder.setRank(getPlayerRank(type, handler.getRoleId()));
//		}
//
//		handler.sendMsgToPlayer(GetRankRs.ext, builder.build());
//	}
//
//	public LinkedList<Lord> getRankList(int type) {
//		if (type == 1) {
//			return fightRankList.getList();
//		} else if (type == 2) {
//			return starsRankList.getList();
//		} else if (type == 3) {
//			return honourRankList.getList();
//		} else if (type == 9) {
//			return staffingRankList.getList();
//		}
//		return null;
//	}
//}
