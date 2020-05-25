package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.game.domain.PartyData;
import com.game.util.PbHelper;
import com.game.warFight.domain.FightPair;
import com.game.warFight.domain.WarMember;
import com.game.warFight.domain.WarParty;

import define.SysChatId;
import manager.GlobalDataManager;
import manager.WarDataManager;
import network.handler.module.tank.ChatService;
import network.handler.module.tank.WarService;
import pb.CommonPb.RptAtkWar;
import pb.CommonPb.WarRecord;
import util.DateUtil;

public class WarFight {

	private List<WarParty> fighters = new ArrayList<>();
	private int outCount = 0;
	private int tick = 0;
	private int fightDay = 0;
	private int state = 0;
	private List<FightPair> pairs;

	public WarFight(int fightDay) {
		this.setFightDay(fightDay);
	}

	public void prepairForFight() {
		Map<Integer, WarParty> parties = WarDataManager.getInst().getParties();
		fighters.addAll(parties.values());
		PartyData partyData;
		for (WarParty warParty : fighters) {
			warParty.prepair();
			partyData = warParty.getPartyData();
			// LogHelper.WAR_LOGGER.trace(partyData.getPartyName() + " lv:" +
			// partyData.getRegLv() + " reg in war");
		}
	}

	public List<FightPair> arrangePair() {
		List<FightPair> pairs = new LinkedList<>();

		Collections.shuffle(fighters);
		int size = fighters.size();
		for (int i = 0; i < size / 2; i += 2) {
			FightPair fightPair = new FightPair();
			fightPair.attacker = fighters.get(i).aquireFighter();
			fightPair.defencer = fighters.get(i + 1).aquireFighter();
			pairs.add(fightPair);
		}

		return pairs;
	}

	public boolean round() {
		tick++;
		if (tick % 3 != 0) {
			return false;
		}

		int time = DateUtil.getSecondTime();
		// int fightCount = 0;
		// while (fightCount < 1) {
		if (pairs == null || pairs.isEmpty()) {
			pairs = arrangePair();
		}

		int result;
		int rank;
		int hp1;
		int hp2;
		Iterator<FightPair> it = pairs.iterator();
		while (it.hasNext()) {
			FightPair fightPair = (FightPair) it.next();
			hp1 = fightPair.attacker.calcHp();
			hp2 = fightPair.defencer.calcHp();
			RptAtkWar.Builder rptAtkWar = RptAtkWar.newBuilder();
			if (WarService.getInst().fightWarMember(fightPair.attacker, fightPair.defencer, rptAtkWar)) {
				addWinCount(fightPair.attacker);
				result = fightPair.attacker.getMember().getWinCount();
				fightPair.defencer.getWarParty().fighterOut(fightPair.defencer);
			} else {
				result = 0;
				addWinCount(fightPair.defencer);
				fightPair.attacker.getWarParty().fighterOut(fightPair.attacker);
			}

			// fightCount++;

			WarRecord record = PbHelper.createWarRecordPb(fightPair.attacker, fightPair.defencer, hp1, hp2, result, time);
			WarService.getInst().synWarRecord(record);
			WarDataManager.getInst().addRecord(fightPair, record, rptAtkWar.build());

			WarParty warParty = null;
			if (result == 0) {
				warParty = fightPair.attacker.getWarParty();
				if (warParty.allOut()) {
					rank = WarDataManager.getInst().getParties().size() - outCount;
					setWarRank(warParty, rank);

					outCount++;
					WarRecord out = PbHelper.createWarResultPb(warParty.getPartyData().getPartyName(), fightPair.defencer, rank, time);
					WarService.getInst().synWarRecord(out);
					WarDataManager.getInst().addWorldAndPartyRecord(warParty, out);

					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PARTY_WAR, warParty.getPartyData().getPartyName(), fightPair.defencer.getPlayer().lord.getNick(), String.valueOf(rank)));
				}
			} else {
				warParty = fightPair.defencer.getWarParty();
				if (warParty.allOut()) {
					rank = WarDataManager.getInst().getParties().size() - outCount;
					setWarRank(warParty, rank);

					outCount++;
					WarRecord out = PbHelper.createWarResultPb(warParty.getPartyData().getPartyName(), fightPair.attacker, rank, time);
					WarService.getInst().synWarRecord(out);
					WarDataManager.getInst().addWorldAndPartyRecord(warParty, out);

					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PARTY_WAR, warParty.getPartyData().getPartyName(), fightPair.attacker.getPlayer().lord.getNick(), String.valueOf(rank)));
				}
			}

			it.remove();
			break;
		}

		partyOut();

		if (fighters.size() == 1) {// 剩下的是冠军，比赛结束
			WarParty warParty = fighters.get(0);
			setWarRank(warParty, 1);
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PARTY_CHAMPION, warParty.getPartyData().getPartyName()));
			return true;
		}
		// }

		return false;
	}

	private void setWarRank(WarParty warParty, int rank) {
		warParty.getPartyData().setWarRank(rank);
		WarDataManager.getInst().setWarRank(warParty, rank);
	}

	private void addWinCount(WarMember warMember) {
		Member member = warMember.getMember();
		member.setWinCount(member.getWinCount() + 1);
		WarDataManager.getInst().setWinRank(warMember);
		if (member.getWinCount() == 5) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.PARTY_WAR_WIN, warMember.getWarParty().getPartyData().getPartyName(), warMember.getPlayer().lord.getNick()));
		}
	}

	public void partyOut() {
		Iterator<WarParty> it = fighters.iterator();
		while (it.hasNext()) {
			WarParty warParty = (WarParty) it.next();
			if (warParty.allOut()) {
				it.remove();
			}
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		GlobalDataManager.getInst().gameGlobal.setWarState(state);
	}

	public int getFightDay() {
		return fightDay;
	}

	public void setFightDay(int fightDay) {
		this.fightDay = fightDay;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

}
