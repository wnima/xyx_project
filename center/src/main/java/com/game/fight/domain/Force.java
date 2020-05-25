package com.game.fight.domain;

import com.game.fight.FightCalc;

import config.bean.ConfTank;

public class Force {
	public int pos;
	public long hp;
	public long maxHp;
	public AttrData attrData;
	public ConfTank staticTank;
	public int count;
	public int killed;
	public boolean dizzy;
	public Fighter fighter;
	public int key;
	public int type;
	public boolean god;

	public Force(ConfTank staticTank, AttrData attrData, int pos, int count) {
		this.pos = pos;
		this.staticTank = staticTank;
		this.attrData = attrData;
		this.count = count;
		type = staticTank.getType();
		dizzy = false;
		god = false;
		killed = 0;
		hp = 0;
	}

	public void initHp() {
		maxHp = calcHp();
		hp = maxHp * count;
	}

	public boolean alive() {
		return count > 0;
	}

	public long hurt(long hurt) {
		if (god) {
			return 0;
		}
		
		hp -= hurt;
		if (hp < 0) {
			hp = 0;
		}

		int alive = FightCalc.calcAlive(this);
		int killed = count - alive;

		if (count != alive) {
			count = alive;
			this.killed += killed;
		}

		if (hp <= 0) {
			fighter.fightLogic.onForceDie(this);
		}

		// GameServer.GAME_LOGGER.error("force " + key + " count:" + count +
		// " bekilled:" + this.killed + " alive:" + alive);

		return count;
	}

	// public int calcDodge() {
	// return (int) ((attrData.dodge + fighter.auraData[type - 1].dodge) *
	// ((FightCalc.BASE + attrData.dodgeF + fighter.auraData[type - 1].dodgeF) /
	// FightCalc.BASE));
	// }
	//
	// public int calcHit() {
	// return (int) ((attrData.hit + fighter.auraData[type - 1].hit) *
	// ((FightCalc.BASE + attrData.hitF + fighter.auraData[type - 1].hitF) /
	// FightCalc.BASE));
	// }
	//
	// public int calcCrit() {
	// return (int) ((attrData.crit + fighter.auraData[type - 1].crit) *
	// ((FightCalc.BASE + attrData.critF + fighter.auraData[type - 1].critF) /
	// FightCalc.BASE));
	// }
	//
	// public int calcCritDef() {
	// return (int) ((attrData.critDef + fighter.auraData[type - 1].critDef) *
	// ((FightCalc.BASE + attrData.critDefF + fighter.auraData[type -
	// 1].critDefF) / FightCalc.BASE));
	// }
	//
	// public int calcImpale() {
	// return (int) ((attrData.impale + fighter.auraData[type - 1].impale) *
	// ((FightCalc.BASE + attrData.impaleF + fighter.auraData[type - 1].impaleF)
	// / FightCalc.BASE));
	// }
	//
	// public int calcDefend() {
	// return (int) ((attrData.defend + fighter.auraData[type - 1].defend) *
	// ((FightCalc.BASE + attrData.defendF + fighter.auraData[type - 1].defendF)
	// / FightCalc.BASE));
	// }

	public int calcDodge() {
		return (int) (attrData.dodge + fighter.auraData[type - 1].dodge);
	}

	public int calcHit() {
		return (int) (attrData.hit + fighter.auraData[type - 1].hit);
	}

	public int calcCrit() {
		return (int) (attrData.crit + fighter.auraData[type - 1].crit);
	}

	public int calcCritDef() {
		return (int) (attrData.critDef + fighter.auraData[type - 1].critDef);
	}

	public int calcImpale() {
		return (int) (attrData.impale + fighter.auraData[type - 1].impale);
	}

	public int calcDefend() {
		return (int) (attrData.defend + fighter.auraData[type - 1].defend);
	}

	public long calcHp() {
		return (long) (attrData.hp * (FightCalc.BASE + attrData.hpF) / FightCalc.BASE);
	}
}
