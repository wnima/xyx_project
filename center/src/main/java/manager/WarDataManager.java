package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import com.game.domain.PartyData;
import com.game.domain.Player;
import com.game.util.PbHelper;
import com.game.warFight.domain.FightPair;
import com.game.warFight.domain.WarMember;
import com.game.warFight.domain.WarParty;
import com.google.inject.Singleton;

import data.bean.Army;
import data.bean.Form;
import data.bean.WarLog;
import define.ArmyState;
import define.WarState;
import domain.Member;
import domain.WarFight;
import inject.BeanManager;
import pb.CommonPb;
import pb.CommonPb.RptAtkWar;
import pb.CommonPb.WarRecord;
import util.DateUtil;

class ComparatorWinRank implements Comparator<WarMember> {

	@Override
	public int compare(WarMember o1, WarMember o2) {
		long d1 = o1.getMember().getWinCount();
		long d2 = o2.getMember().getWinCount();

		if (d1 < d2)
			return 1;
		else if (d1 > d2) {
			return -1;
		} else {
			long v1 = o1.getMember().getRegFight();
			long v2 = o2.getMember().getRegFight();
			if (v1 < v2) {
				return 1;
			} else if (v1 > v2) {
				return -1;
			}

			return 0;
		}
	}
}

@Singleton
public class WarDataManager {
	static final int MAX_RECORD_COUNT = 20;

	private WarLog warLog;

	private WarFight warFight;

	// 所有报名军团
	private Map<Integer, WarParty> partyMap = new HashMap<>();

	// 战力排行
	private Map<Integer, Long> partyFightMap = new LinkedHashMap<>();

	// 军团排名
	private Map<Integer, WarParty> rankMap = new TreeMap<>();

	// 连胜排行
	private LinkedList<WarMember> winRankList = new LinkedList<>();

	// 连胜排行玩家id
	private Set<Long> winRankSet = new HashSet<>();

	public static WarDataManager getInst() {
		return BeanManager.getBean(WarDataManager.class);
	}

	// // 已经领取过连胜排行的玩家
	// private Set<Long> getWinRank = new HashSet<>();

	public Map<Integer, WarParty> getRankMap() {
		return rankMap;
	}

	public void setRankMap(Map<Integer, WarParty> rankMap) {
		this.rankMap = rankMap;
	}

	public LinkedList<WarMember> getWinRankList() {
		return winRankList;
	}

	public void setWinRankList(LinkedList<WarMember> winRankList) {
		this.winRankList = winRankList;
	}

	public WarLog getWarLog() {
		return warLog;
	}

	public void setWarLog(WarLog warLog) {
		this.warLog = warLog;
	}

	@PostConstruct
	public void init() {
		initWarLog();
		initData();
	}

	private void initData() {
		Map<Integer, PartyData> parties = PartyDataManager.getInst().getPartyMap();
		Iterator<PartyData> itParty = parties.values().iterator();
		while (itParty.hasNext()) {
			PartyData partyData = (PartyData) itParty.next();
			if (partyData.getRegLv() > 0 && partyData.getRegFight() > 0) {
				partyFightMap.put(partyData.getPartyId(), partyData.getRegFight());
			}
		}

		Iterator<Member> it = PartyDataManager.getInst().getMemberMap().values().iterator();
		while (it.hasNext()) {
			Member member = it.next();
			if (!SmallIdManager.getInst().isSmallId(member.getLordId())) {

				if (member.getRegLv() != 0) {
					if (member.getRegParty() != 0) {
						PartyData partyData = PartyDataManager.getInst().getParty(member.getRegParty());
						loadReg(partyData, member);
					} else if (member.getPartyId() != 0) {
						PartyData partyData = PartyDataManager.getInst().getParty(member.getPartyId());
						loadReg(partyData, member);
					}
				}
			}
		}

		List<Long> list = GlobalDataManager.getInst().gameGlobal.getWinRank();
		for (Long roleId : list) {
			if (!SmallIdManager.getInst().isSmallId(roleId)) {
				Member member = PartyDataManager.getInst().getMemberById(roleId);
				WarMember warMember;
				if (member.getRegParty() != 0) {
					warMember = partyMap.get(member.getRegParty()).getMember(roleId);
				} else {
					warMember = partyMap.get(member.getPartyId()).getMember(roleId);
				}

				winRankList.add(warMember);
				winRankSet.add(roleId);
			}
		}

		partyFightMap = sortMap(partyFightMap);

		Iterator<WarParty> partyIt = partyMap.values().iterator();
		while (partyIt.hasNext()) {
			WarParty warParty = (WarParty) partyIt.next();
			setWarRank(warParty, warParty.getPartyData().getWarRank());
		}

	}

	public void loadReg(PartyData partyData, Member member) {
		int partyId = member.getRegParty();
		WarParty warParty = partyMap.get(partyId);
		if (warParty == null) {
			warParty = new WarParty(partyData);
			partyMap.put(partyId, warParty);
		}

		WarMember warMember = loadWarMember(member, partyData);
		warParty.load(warMember);
		// setWinRank(warMember);
	}

	public void refreshForWarFight() {
		Map<Integer, PartyData> parties = PartyDataManager.getInst().getPartyMap();
		Iterator<PartyData> it = parties.values().iterator();
		while (it.hasNext()) {
			PartyData partyData = (PartyData) it.next();
			partyData.getWarRecords().clear();
			partyData.setRegLv(0);
			partyData.setRegFight(0);
			partyData.setWarRank(0);
		}

		Iterator<Member> itMember = PartyDataManager.getInst().getMemberMap().values().iterator();
		while (itMember.hasNext()) {
			Member member = itMember.next();
			if (member.getRegLv() != 0) {
				member.setRegParty(0);
				member.setRegLv(0);
				member.setRegFight(0);
				member.setWinCount(0);
				member.getWarRecords().clear();
			}
		}

		partyMap.clear();
		partyFightMap.clear();
		GlobalDataManager.getInst().clearWarRecord();

		rankMap.clear();
		winRankList.clear();
		winRankSet.clear();

		GlobalDataManager.getInst().gameGlobal.getWinRank().clear();
		GlobalDataManager.getInst().gameGlobal.getGetWinRank().clear();
		GlobalDataManager.getInst().gameGlobal.setWarTime(DateUtil.getToday());
	}

	public void cancelWarFight() {
		Map<Integer, PartyData> parties = PartyDataManager.getInst().getPartyMap();
		Iterator<PartyData> it = parties.values().iterator();
		while (it.hasNext()) {
			PartyData partyData = (PartyData) it.next();
			partyData.getWarRecords().clear();
			partyData.setRegLv(0);
			partyData.setRegFight(0);
			partyData.setWarRank(0);
		}

		Iterator<Member> itMember = PartyDataManager.getInst().getMemberMap().values().iterator();
		while (itMember.hasNext()) {
			Member member = itMember.next();
			if (member.getRegLv() != 0 && member.getRegParty() != 0) {
				member.setRegParty(0);
				member.setRegLv(0);
				member.setRegFight(0);
				member.setWinCount(0);
				member.getWarRecords().clear();
			}
		}

		partyMap.clear();
		partyFightMap.clear();
		GlobalDataManager.getInst().clearWarRecord();
		rankMap.clear();
		winRankList.clear();
		winRankSet.clear();

		GlobalDataManager.getInst().gameGlobal.getWinRank().clear();
		GlobalDataManager.getInst().gameGlobal.getGetWinRank().clear();

	}

	public void addRecord(FightPair fightPair, WarRecord record, RptAtkWar rpt) {
		GlobalDataManager.getInst().addWarRecord(record);

		LinkedList<CommonPb.WarRecord> party = fightPair.attacker.getWarParty().getPartyData().getWarRecords();
		party.add(record);
		if (party.size() > MAX_RECORD_COUNT) {
			party.removeFirst();
		}

		party = fightPair.defencer.getWarParty().getPartyData().getWarRecords();
		party.add(record);
		if (party.size() > MAX_RECORD_COUNT) {
			party.removeFirst();
		}

		Member target = fightPair.attacker.getMember();
		CommonPb.WarRecordPerson personRecord = PbHelper.createPersonWarRecordPb(record, rpt);
		target.warRecords.add(personRecord);
		if (target.warRecords.size() > MAX_RECORD_COUNT) {
			target.warRecords.removeFirst();
		}

		target = fightPair.defencer.getMember();
		target.warRecords.add(personRecord);
		if (target.warRecords.size() > MAX_RECORD_COUNT) {
			target.warRecords.removeFirst();
		}
	}

	public void addWorldAndPartyRecord(WarParty warParty, WarRecord record) {
		GlobalDataManager.getInst().addWarRecord(record);

		LinkedList<CommonPb.WarRecord> party = warParty.getPartyData().getWarRecords();
		party.add(record);
		if (party.size() > MAX_RECORD_COUNT) {
			party.removeFirst();
		}
	}

	private void initWarLog() {
		// warLog = serverLogDao.selectLastWarLog();
		// if (warLog == null) {
		// warLog = new WarLog();
		// warLog.setWarTime(TimeHelper.getCurrentDay());
		// }
	}

	public void flushWarLog() {
		// serverLogDao.insertWarLog(warLog);
	}

	private Map<Integer, Long> sortMap(Map<Integer, Long> oldMap) {
		ArrayList<Map.Entry<Integer, Long>> list = new ArrayList<Map.Entry<Integer, Long>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Entry<Integer, Long>>() {

			@Override
			public int compare(Entry<Integer, Long> o1, Entry<Integer, Long> o2) {
				// TODO Auto-generated method stub
				long d1 = o1.getValue();
				long d2 = o2.getValue();
				if (d1 < d2)
					return 1;
				else if (d1 > d2) {
					return -1;
				}

				return 0;
			}
		});

		Map<Integer, Long> newMap = new LinkedHashMap<Integer, Long>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}

	public WarMember createWarMember(Player player, Member member, Form form, int fight) {
		WarMember warMember = new WarMember();
		warMember.setPlayer(player);
		warMember.setMember(member);
		warMember.setForm(form);
		member.setRegParty(member.getPartyId());
		member.setRegFight(fight);
		member.setRegLv(player.lord.getLevel());
		member.setRegTime(DateUtil.getSecondTime());
		warMember.setInstForm(new Form(form));
		return warMember;
	}

	private WarMember loadWarMember(Member member, PartyData partyData) {
		Player player = PlayerDataManager.getInst().getPlayer(member.getLordId());
		WarMember warMember = new WarMember();
		warMember.setPlayer(player);
		warMember.setMember(member);
		return warMember;
	}

	public boolean warReg(WarMember warMember) {
		int partyId = warMember.getMember().getPartyId();
		WarParty warParty = partyMap.get(partyId);
		if (warParty == null) {
			PartyData partyData = PartyDataManager.getInst().getParty(partyId);
			if (partyData == null) {
				return false;
			}
			warParty = new WarParty(partyData);
			partyMap.put(partyId, warParty);
		}

		warParty.add(warMember);

		Long fight = partyFightMap.get(partyId);
		if (fight == null) {
			partyFightMap.put(partyId, warMember.getMember().getRegFight());
		} else {
			partyFightMap.put(partyId, fight + warMember.getMember().getRegFight());
		}

		partyFightMap = sortMap(partyFightMap);
		return true;
	}

	public void cancelArmy(WarMember warMember) {
		Form form = warMember.getForm();
		Player target = warMember.getPlayer();
		for (int i = 0; i < form.c.length; i++) {
			if (form.c[i] > 0) {
				PlayerDataManager.getInst().addTank(target, form.p[i], form.c[i]);
			}
		}

		if (form.getCommander() > 0) {
			HeroManager.getInst().addHero(target, form.getCommander(), 1);
		}

		Iterator<Army> it = target.armys.iterator();
		while (it.hasNext()) {
			Army army = (Army) it.next();
			if (army.getState() == ArmyState.WAR) {
				it.remove();
				break;
			}
		}
	}

	public void warUnReg(int partyId, long roleId) {
		WarParty warParty = partyMap.get(partyId);
		if (warParty != null) {
			WarMember warMember = warParty.getMember(roleId);
			if (warMember != null) {
				Long fight = partyFightMap.get(partyId);
				if (fight != null) {
					fight -= warMember.getMember().getRegFight();
					if (fight > 0) {
						partyFightMap.put(partyId, fight);
					} else {
						partyFightMap.remove(partyId);
					}
				}

				warParty.remove(warMember);
				// cancelArmy(warMember);
			}

			if (warParty.getMembers().size() == 0) {
				warParty.getPartyData().setRegFight(0);
				warParty.getPartyData().setRegLv(0);
				partyMap.remove(partyId);
			}
		}
	}

	public WarParty getWarParty(int partyId) {
		return partyMap.get(partyId);
	}

	public Map<Integer, WarParty> getParties() {
		return partyMap;
	}

	public Long getPartyFight(int partyId) {
		return partyFightMap.get(partyId);
	}

	public Map<Integer, Long> getPartyFightMap() {
		return partyFightMap;
	}

	public LinkedList<CommonPb.WarRecord> getPartyWarRecord(int partyId) {
		WarParty warParty = partyMap.get(partyId);
		if (warParty != null) {
			return warParty.getPartyData().getWarRecords();
		}

		return null;
	}

	// public LinkedList<CommonPb.WarRecord> getWorldWarRecord() {
	// return worldRecord;
	// }

	// public void recordRank() {
	// arrangeWinRank();
	// arrangePartyRank();
	// }

	public void setWarRank(WarParty warParty, int rank) {
		rankMap.put(rank, warParty);
	}

	public void setWinRank(WarMember warMember) {
		if (warMember.getMember().getWinCount() == 0) {
			return;
		}

		if (winRankSet.contains(warMember.getPlayer().roleId)) {
			Collections.sort(winRankList, new ComparatorWinRank());
		} else {
			if (winRankList.isEmpty()) {
				winRankList.add(warMember);
			} else {
				boolean added = false;
				ListIterator<WarMember> rankIt = winRankList.listIterator(winRankList.size());
				while (rankIt.hasPrevious()) {
					Member e = rankIt.previous().getMember();
					if (warMember.getMember().getWinCount() < e.getWinCount()) {
						rankIt.next();
						rankIt.add(warMember);
						added = true;
						break;
					} else if (warMember.getMember().getWinCount() == e.getWinCount()) {
						if (warMember.getMember().getRegFight() <= e.getRegFight()) {
							rankIt.next();
							rankIt.add(warMember);
							added = true;
							break;
						}
					}
				}

				if (!added) {
					winRankList.addFirst(warMember);
				}
			}

			winRankSet.add(warMember.getPlayer().roleId);
			if (winRankList.size() > 10) {
				winRankSet.remove(winRankList.removeLast().getPlayer().roleId);
			}
		}
	}

	public int getWinRank(long roleId) {
		if (winRankSet.contains(roleId)) {
			int rank = 0;
			for (WarMember warMember : winRankList) {
				rank++;
				if (warMember.getPlayer().roleId == roleId) {
					return rank;
				}
			}
		}
		return 0;
	}

	public boolean hadGetWinRankAward(long roleId) {
		return GlobalDataManager.getInst().gameGlobal.getGetWinRank().contains(roleId);
	}

	public void setWinRankAward(long roleId) {
		GlobalDataManager.getInst().gameGlobal.getGetWinRank().add(roleId);
	}

	public WarFight getWarFight() {
		return warFight;
	}

	public void setWarFight(WarFight warFight) {
		this.warFight = warFight;
		refreshForWarFight();
	}

	public boolean inRegTime() {
		if (GlobalDataManager.getInst().gameGlobal.getWarState() == WarState.REG_STATE) {
			return true;
		}

		return false;
	}

}
