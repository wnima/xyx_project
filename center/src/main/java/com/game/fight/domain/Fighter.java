package com.game.fight.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.game.fight.FightLogic;

import config.bean.ConfBuff;

public class Fighter {
	// public Map<Integer, Effect> effect;
	public Force[] forces = new Force[6];
	public Fighter oppoFighter;
	public int totalTank = 0;
	public boolean isAttacker = false;
	public AttrData[] auraData = new AttrData[4];
	public Map<Integer, LinkedList<ConfBuff>> aura = new HashMap<Integer, LinkedList<ConfBuff>>();
	public FightLogic fightLogic;
	public int firstValue = 0;
	public boolean boss = false;
	public long hurt = 0;

	public Fighter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addForce(Force force, int i) {
		forces[i - 1] = force;
		if (force != null) {
			force.fighter = this;
			totalTank += force.count;
			int type = force.staticTank.getType();
			if (auraData[type - 1] == null) {
				auraData[type - 1] = new AttrData();
			}
		}
	}

	public void effectAura() {
		Iterator<LinkedList<ConfBuff>> it = aura.values().iterator();
		while (it.hasNext()) {
			LinkedList<ConfBuff> list = it.next();
			// System.out.println("effectAura list size:" + list.size());
			addAuraAttr(list.peekFirst());
		}
	}

	public void addAura(ConfBuff buff) {
		LinkedList<ConfBuff> group = aura.get(buff.getGroupId());
		if (group == null) {
			group = new LinkedList<>();
			aura.put(buff.getGroupId(), group);
		}

		boolean add = false;
		int value = Math.abs(buff.getEffectValue());
		Iterator<ConfBuff> it = group.iterator();
		while (it.hasNext()) {
			ConfBuff staticBuff = (ConfBuff) it.next();
			if (value > Math.abs(staticBuff.getEffectValue())) {
				group.addFirst(buff);
				add = true;
				break;
			}
		}

		if (!add) {
			group.addLast(buff);
		}
	}

	public void removeAura(ConfBuff buff) {
		LinkedList<ConfBuff> group = aura.get(buff.getGroupId());
		group.pollFirst();
		subAuraAttr(buff);

		ConfBuff next = group.peekFirst();
		if (next != null) {
			addAuraAttr(next);
		}
	}

	private void addAuraData(int type, ConfBuff buff) {
		if (auraData[type - 1] != null) {
			auraData[type - 1].addValue(buff.getEffectType(), buff.getEffectValue());
		}
	}

	private void subAuraData(int type, ConfBuff buff) {
		if (auraData[type - 1] != null) {
			auraData[type - 1].addValue(buff.getEffectType(), -buff.getEffectValue());
		}
	}

	public void addAuraAttr(ConfBuff buff) {
		int target = buff.getTarget();
		if (target == 0) {
			addAuraData(1, buff);
			addAuraData(2, buff);
			addAuraData(3, buff);
			addAuraData(4, buff);
		} else {
			addAuraData(target, buff);
		}
	}

	private void subAuraAttr(ConfBuff buff) {
		int target = buff.getTarget();
		if (target == 0) {
			subAuraData(1, buff);
			subAuraData(2, buff);
			subAuraData(3, buff);
			subAuraData(4, buff);
		} else {
			subAuraData(target, buff);
		}
	}

	public void changeGod() {
		for (Force force : forces) {
			if (force != null) {
				if (force.alive()) {
					force.god = false;
					break;
				}
			}
		}
	}
}
