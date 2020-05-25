package com.game.warFight.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.domain.PartyData;

public class WarParty {
	private Map<Long, WarMember> members = new HashMap<Long, WarMember>();
	private List<WarMember> fighters = new ArrayList<>();
	private int order = 0;
	private int outCount = 0;

	private PartyData partyData;

	public WarParty(PartyData partyData) {
		this.partyData = partyData;
		partyData.setRegLv(partyData.getPartyLv());
	}

	public void prepair() {
		fighters.clear();
		fighters.addAll(members.values());
		Collections.shuffle(fighters);
	}

	public boolean allOut() {
		return outCount == members.size();
	}

	public WarMember aquireFighter() {
		while (true) {
			WarMember warMember = fighters.get(order % fighters.size());
			order++;
			if (warMember.getState() == 1) {
				continue;
			}

			return warMember;
		}
	}

	public void fighterOut(WarMember warMember) {
		warMember.setState(1);
		outCount++;
	}

	public void add(WarMember warMember) {
		members.put(warMember.getPlayer().roleId, warMember);
		warMember.setWarParty(this);
		partyData.setRegFight(partyData.getRegFight() + warMember.getMember().getRegFight());
	}
	
	public void load(WarMember warMember) {
		members.put(warMember.getPlayer().roleId, warMember);
		warMember.setWarParty(this);
	}

	public WarMember getMember(long roleId) {
		return members.get(roleId);
	}

	public void remove(WarMember warMember) {
		members.remove(warMember.getPlayer().roleId);
		partyData.setRegFight(partyData.getRegFight() - warMember.getMember().getRegFight());
		warMember.getMember().setRegParty(0);
		warMember.getMember().setRegLv(0);
		warMember.getMember().setRegTime(0);
		warMember.getMember().setRegFight(0);
	}

	public Map<Long, WarMember> getMembers() {
		return members;
	}

	public PartyData getPartyData() {
		return partyData;
	}

	public void setPartyData(PartyData partyData) {
		this.partyData = partyData;
	}
}
