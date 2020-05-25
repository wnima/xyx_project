package com.game.fight;

import com.game.fight.domain.AttrData;
import com.game.fight.domain.Force;
import com.game.util.RandomHelper;

import define.TankType;

public class FightCalc {
	final static private float[] COUNT_RATIO = { 0, 0.1f, 0.3f, 0.5f, 0.65f, 0.7f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f, 1.05f, 1.1f, 1.15f, 1.2f, 1.25f, 1.4f, 1.6f,
			2.0f, 5.0f, 10.0f };

	// final static private float[] COUNT_RATIO_FACTOR = { 0.25f, 0.3f, 0.4f,
	// 0.5f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f,
	// 1.4f, 1.5f,
	// 1.7f, 2f, 2.5f, 3.0f, 4.0f };
	//
	// final static private float[] IMPALE_SUB_FACTOR = { 0.8f, 0.5f, 0.3f,
	// 0.2f, 0.1f };

	final static private int[] IMPALE_SUB = { 100 * 100, 200 * 100, 300 * 100, 500 * 100, 999999999 };

	final static private float[] COUNT_RATIO_FACTOR = { 0.25f, 0.3f, 0.4f, 0.5f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 1.0f, 1.1f, 1.1f, 1.1f, 1.2f, 1.3f,
			1.4f, 1.5f, 1.7f, 2.0f, 2.5f };

	final static private float[] IMPALE_SUB_FACTOR = { 0.25f, 0.2f, 0.15f, 0.1f, 0.05f };

	final static public double BASE = 10000.0;

	static public boolean isDodge(Force force, Force target) {
		if (target.fighter.boss || force.fighter.boss) {
			return false;
		}

		int dodge = target.calcDodge();
		int hit = force.calcHit();
		int prob = (dodge - hit) / 2;
		if (prob < 20) {
			prob = 20;
		} else if (prob > 900) {
			prob = 900;
		}

		return RandomHelper.isHitRangeIn1000(prob);
	}

	static public boolean isCrit(Force force, Force target) {
		if (force.fighter.boss) {
			return false;
		}

		int crit = force.calcCrit();
		int critDef = target.calcCritDef();
		int prob = (crit - critDef) / 2;
		if (prob < 80) {
			prob = 80;
		} else if (prob > 700) {
			prob = 700;
		}

		return RandomHelper.isHitRangeIn1000(prob);
	}

	static public boolean isImpale(Force force, Force target) {
		if (target.fighter.boss || force.fighter.boss) {
			return false;
		}

		float countRatio = force.count / (float) target.count;
		float countFactor = 0;
		for (int i = 0; i < COUNT_RATIO.length; i++) {
			if (countRatio > COUNT_RATIO[i]) {
				countFactor = COUNT_RATIO_FACTOR[i];
			} else {
				break;
			}
		}

		int impaleSub = force.calcImpale() - target.calcDefend();

		float impaleFactor = 0;

		if (impaleSub > 0) {
			int temp = impaleSub;
			for (int i = 0; i < IMPALE_SUB.length; i++) {// 每一档遍历
				if (impaleSub > IMPALE_SUB[i]) {
					temp -= IMPALE_SUB[i];
					impaleFactor += IMPALE_SUB[i] * IMPALE_SUB_FACTOR[i];
				} else {
					impaleFactor += temp * IMPALE_SUB_FACTOR[i];
					break;
				}
			}
		}

		int imf1 = force.staticTank.getImpaleFactor();
		int imf2 = target.staticTank.getImpaleFactor();
		imf1 = (imf1 != 0 ? imf1 : 1);
		imf2 = (imf2 != 0 ? imf2 : 1);
		int prob = (int) (countFactor * impaleFactor * imf1 / imf2);
		if (prob > 80000) {
			prob = 80000;
		}

//		LogHelper.ERROR_LOGGER.error("is impale:" + force.calcImpale() + "|" + target.calcDefend() + "|" + countFactor + "|" + prob);

		prob = shakeValue(prob);
		return RandomHelper.isHitRangeIn100000(prob);
	}

	static public long calcHurt(Force force, Force target, float crit) {
		AttrData attackData = force.attrData;
		AttrData defendData = target.attrData;

		Integer restriction = force.staticTank.getRestriction().get(target.staticTank.getSubType());
		if (restriction == null) {
			restriction = 0;
		}

		int impaleSub = (force.calcImpale() - target.calcDefend()) / 1000;

		// 调整伤害加成下限为0.2，上限不变
		double addtionValue = ((BASE + attackData.attackF + force.fighter.auraData[force.type - 1].attackF - defendData.injuredF
				- target.fighter.auraData[target.type - 1].injuredF + restriction * 100 + impaleSub * 80) / BASE);
		addtionValue = addtionValue <= 0 ? 0.2 : addtionValue;

		long hurt = (long) (attackData.attack * addtionValue * force.count * crit);
		
		// 若是战车的话，伤害削减40%
		if (force.type == TankType.Chariot) {
			hurt *= 0.4;
		}

		if (hurt < 1) {
			hurt = 1;
		}

		return hurt;
	}

	static public int calcAlive(Force target) {
		return (int) Math.ceil(target.hp / (double) target.maxHp);
	}

	static public int shakeValue(int value) {
		int shake = (int) (value * 0.04);
		if (shake != 0) {
			shake = RandomHelper.randomInSize(shake);
		}
		int v = (int) (shake + value * 0.98);
		if (v < 1) {
			v = 1;
		}
		return v;
	}

	static public long shakeValue(long value) {
		long shake = (long) (value * 0.04);
		if (shake != 0) {
			shake = RandomHelper.randomInSize(shake);
		}

		long v = (long) (shake + value * 0.98);
		if (v < 1) {
			v = 1;
		}
		return v;
	}

	/**   
	* Method: calcCrit    
	* @Description: 计算爆裂和坚韧后的暴击比
	* @param force
	* @param target
	* @param crit
	* @return    
	* @return float    
	* @throws    
	*/
	public static float calcCrit(Force force, Force target, float crit) {
		AttrData attackData = force.attrData;
		AttrData defendData = target.attrData;
		return (crit * 10000 + attackData.tenacity - defendData.burst)/10000;
	}
}
