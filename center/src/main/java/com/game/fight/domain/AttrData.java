package com.game.fight.domain;

import java.util.List;

import config.bean.ConfTank;
import data.bean.BossFight;
import define.AttrId;

public class AttrData {
	public long hp;
	public int attack;
	public int hit;
	public int dodge;
	public int crit;
	public int critDef;
	public int impale;
	public int defend;

	public int hpF;
	public int attackF;
	public int injuredF;

	public int tenacity;// 坚韧
	public int burst;// 爆裂

	public AttrData() {

	}

	public AttrData(ConfTank staticTank) {
		hp = staticTank.getHp();
		attack = staticTank.getAttack();
		hit = staticTank.getHit();
		dodge = staticTank.getDodge();
		crit = staticTank.getCrit();
		critDef = staticTank.getCritDef();
		impale = staticTank.getImpale();
		defend = staticTank.getDefend();
		tenacity = 0;
		burst = 0;
	}

	public AttrData(ConfTank staticTank, BossFight bossFight) {
		hp = staticTank.getHp();
		attack = staticTank.getAttack();
		hit = staticTank.getHit();
		dodge = staticTank.getDodge();
		crit = staticTank.getCrit() + bossFight.getBless2() * 250;
		critDef = staticTank.getCritDef();
		impale = staticTank.getImpale() + bossFight.getBless1() * 25000;
		defend = staticTank.getDefend();
		attackF = bossFight.getBless3() * 2500;
		tenacity = 0;
		burst = 0;
	}

	public AttrData(List<Integer> attrs) {
		hp = attrs.get(0);
		attack = attrs.get(1);
		hit = attrs.get(2) * 10;
		dodge = attrs.get(3) * 10;
		crit = attrs.get(4) * 10;
		critDef = attrs.get(5) * 10;
		impale = attrs.get(6) * 1000;
		defend = attrs.get(7) * 1000;
		attackF = attrs.get(8);
		injuredF = attrs.get(9);
		tenacity = 0;
		burst = 0;
	}

	public void addValue(int attrId, int value) {
		switch (attrId) {
		case AttrId.HP:// origin
			hp += value;
			break;
		case AttrId.HP_F:// 10000
			hpF += value;
			break;
		case AttrId.ATTACK:// orgin
			attack += value;
			break;
		case AttrId.ATTACK_F:// 10000
			attackF += value;
			break;
		case AttrId.HIT:// 1000
			hit += value;
			break;
		// case AttrId.HIT_F:// 10000
		// hitF += value;
		// break;
		case AttrId.DODGE:// 1000
			dodge += value;
			break;
		// case AttrId.DODGE_F:
		// dodgeF += value;
		// break;
		case AttrId.CRIT:// 1000
			crit += value;
			break;
		// case AttrId.CRIT_F:
		// critF += value;
		// break;
		case AttrId.CRITDEF:// 1000
			critDef += value;
			break;
		// case AttrId.CRITDEF_F:
		// critDefF += value;
		// break;
		case AttrId.IMPALE:// 1000
			impale += value;
			break;
		// case AttrId.IMPALE_F:
		// impaleF += value;
		// break;
		case AttrId.DEFEND:// 1000
			defend += value;
			break;
		// case AttrId.DEFEND_F:
		// defendF += value;
		// break;
		// case AttrId.INJURED:
		// injured += value;
		// break;
		case AttrId.INJURED_F:// 10000
			injuredF += value;
			break;
		case AttrId.TENACITY:
			tenacity += value;
			break;
		case AttrId.BURST:
			burst += value;
			break;
		default:
			break;
		}

	}
}
