package com.game.domain;

import data.bean.Arena;
import data.bean.BossFight;
import data.bean.Building;
import data.bean.LordData;
import data.bean.Lord;
import data.bean.PartyMember;
import data.bean.Resource;
import domain.Member;

public class Role {
	private long roleId;
	private LordData data;
	private Lord lord;
	private Building building;
	private Resource resource;
	private Arena arena;
	private PartyMember partyMember;
	private BossFight bossFight;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public LordData getData() {
		return data;
	}

	public void setData(LordData data) {
		this.data = data;
	}

	public Lord getLord() {
		return lord;
	}

	public void setLord(Lord lord) {
		this.lord = lord;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public PartyMember getPartyMember() {
		return partyMember;
	}

	public void setPartyMember(PartyMember partyMember) {
		this.partyMember = partyMember;
	}

	public Role(Player player, Arena arena, Member member, BossFight bossFight) {
		roleId = player.roleId;
		data = player.serNewData();
		lord = (Lord) player.lord.clone();
		building = (Building) player.building;
		resource = (Resource) player.resource;
		if (arena != null) {
			this.setArena((Arena) arena);
		}

		if (member != null) {
			this.setPartyMember(member.copyData());
		}

		if (bossFight != null) {
			this.setBossFight(bossFight);
		}
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public BossFight getBossFight() {
		return bossFight;
	}

	public void setBossFight(BossFight bossFight) {
		this.bossFight = bossFight;
	}
}
