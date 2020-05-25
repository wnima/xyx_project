package com.game.warFight.domain;

import com.game.domain.Player;

import data.bean.Form;
import domain.Member;

public class WarMember {
	private Player player;
	private Member member;

	private WarParty warParty;
	private Form form;

	private int state;
	private Form instForm;

	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Form getInstForm() {
		return instForm;
	}

	public void setInstForm(Form instForm) {
		this.instForm = instForm;
	}

	public int calcHp() {
		Form baseForm = getForm();
		Form curForm = getInstForm();
		int base = baseForm.c[0] + baseForm.c[1] + baseForm.c[2] + baseForm.c[3] + baseForm.c[4] + baseForm.c[5];
		int cur = curForm.c[0] + curForm.c[1] + curForm.c[2] + curForm.c[3] + curForm.c[4] + curForm.c[5];
		return cur * 100 / base;
	}

	public WarParty getWarParty() {
		return warParty;
	}

	public void setWarParty(WarParty warParty) {
		this.warParty = warParty;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
