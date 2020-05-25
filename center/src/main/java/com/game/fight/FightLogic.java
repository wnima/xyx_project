package com.game.fight;

import java.util.ArrayList;
import java.util.List;

import com.game.fight.domain.Fighter;
import com.game.fight.domain.Force;
import com.game.util.PbHelper;

import config.bean.ConfBuff;
import data.bean.Form;
import define.FirstActType;
import pb.CommonPb;

public class FightLogic {
	public boolean recordFlag = true;
	private Fighter first;
	private Fighter second;
	private int loopCounter = 100;
	private Force[] forces = new Force[12];

	// 进攻方胜负 1.胜 2.负
	private int winState = 0;

	private CommonPb.Record.Builder recordData;
	private CommonPb.Round.Builder roundData;
	private CommonPb.Action.Builder actionData;

	public FightLogic(Fighter attacker, Fighter defencer, int firstStrategy, boolean recordFlag) {
		attacker.isAttacker = true;
		this.recordFlag = recordFlag;
		if (recordFlag) {
			recordData = CommonPb.Record.newBuilder();
		}
		if (firstStrategy == FirstActType.ATTACKER) {
			first = attacker;
			second = defencer;
		} else if (firstStrategy == FirstActType.DEFENCER) {
			first = defencer;
			second = attacker;
		} else if (firstStrategy == FirstActType.FISRT_VALUE_1) {
			if (attacker.firstValue > defencer.firstValue) {
				first = attacker;
				second = defencer;
			} else {
				first = defencer;
				second = attacker;
			}
		} else if (firstStrategy == FirstActType.FISRT_VALUE_2) {
			if (attacker.firstValue >= defencer.firstValue) {
				first = attacker;
				second = defencer;
			} else {
				first = defencer;
				second = attacker;
			}
		}

		first.oppoFighter = second;
		second.oppoFighter = first;

		first.fightLogic = this;
		second.fightLogic = this;

		numberForce();
		initAura();
		for (Force force : forces) {
			if (force != null) {
				force.initHp();
				if (recordFlag) {
					recordData.addHp(force.hp);
				}
			}
		}
	}

	public void packForm(Form a, Form b) {
		recordData.setFormA(PbHelper.createFormPb(a));
		if (b != null) {
			recordData.setFormB(PbHelper.createFormPb(b));
		}
	}

	private void initAura() {
		for (Force force : first.forces) {
			if (force != null) {
				List<ConfBuff> buffs = force.staticTank.getBuffs();
				for (ConfBuff staticBuff : buffs) {
					if (staticBuff.getType() == 1) {
						first.addAura(staticBuff);
					} else {
						second.addAura(staticBuff);
					}
				}
			}
		}

		for (Force force : second.forces) {
			if (force != null) {
				List<ConfBuff> buffs = force.staticTank.getBuffs();
				for (ConfBuff staticBuff : buffs) {
					if (staticBuff.getType() == 1) {
						second.addAura(staticBuff);
					} else {
						first.addAura(staticBuff);
					}
				}
			}
		}

		first.effectAura();
		second.effectAura();
	}

	private void arrangeAura(Force dieForce) {
		List<ConfBuff> list = dieForce.staticTank.getBuffs();
		if (list != null) {
			for (ConfBuff buff : list) {
				if (buff.getType() == 1) {
					dieForce.fighter.removeAura(buff);
				} else {
					dieForce.fighter.oppoFighter.removeAura(buff);
				}
			}
		}
	}

	/**
	 * 
	 * Method: calcStar
	 * 
	 * @Description: 星级评定
	 * @return
	 * @return int
	 * @throws
	 */
	public int estimateStar() {
		Fighter fighter = null;
		if (first.isAttacker) {
			fighter = first;
		} else {
			fighter = second;
		}

		int left = 0;
		for (Force force : fighter.forces) {
			if (force != null && force.alive()) {
				left += force.count;
			}
		}

		float ratio = left / (float) fighter.totalTank;
		if (ratio >= 0.9f) {
			return 3;
		} else if (ratio >= 0.8f) {
			return 2;
		} else {
			return 1;
		}
	}

	public void fight() {
		while (loopCounter-- > 0 && winState == 0) {
			round();
			checkDie();
		}
	}

	public void fightBoss() {
		while (loopCounter-- > 0 && winState == 0) {
			bossRound();
			checkDie();
		}
	}

	public CommonPb.Record generateRecord() {
		recordData.setFirst(first.isAttacker);
		return recordData.build();
	}

	public boolean attackerIsFirst() {
		return first.isAttacker;
	}

	private void checkDie() {
		boolean firstAlive = false;
		boolean secondAlive = false;
		for (Force force : first.forces) {
			if (force != null) {
				if (force.alive()) {
					firstAlive = true;
				}
			}
		}

		for (Force force : second.forces) {
			if (force != null) {
				if (force.alive()) {
					secondAlive = true;
				}
			}
		}

		if (firstAlive && secondAlive) {
			setWinState(0);
		} else {
			if (first.isAttacker) {
				setWinState(firstAlive ? 1 : 2);
			} else {
				setWinState(firstAlive ? 2 : 1);
			}
		}
	}

	private void numberForce() {
		// int initiate = 1;
		// for (Force force : first.forces) {
		// if (force != null) {
		// force.key = initiate;
		// forces[force.key - 1] = force;
		// initiate += 2;
		// }
		// }
		//
		// initiate = 2;
		// for (Force force : second.forces) {
		// if (force != null) {
		// force.key = initiate;
		// forces[force.key - 1] = force;
		// initiate += 2;
		// }
		// }

		for (Force force : first.forces) {
			if (force != null) {
				force.key = force.pos * 2 - 1;
				forces[force.key - 1] = force;
			} else {

			}
		}

		for (Force force : second.forces) {
			if (force != null) {
				force.key = force.pos * 2;
				forces[force.key - 1] = force;
			} else {

			}
		}
	}

	/**
	 * 
	 * Method: round
	 * 
	 * @Description: 一回合，双方坦克都act一次，算一回合
	 * @return void
	 * @throws
	 */
	private void round() {
		int indexA = 0;
		int indexB = 0;
		for (int i = 0; i < 6; i++) {
			while (indexA < 6) {
				Force force = first.forces[indexA++];
				if (force != null && force.alive()) {
					if (!force.dizzy) {
						if (act(force)) {
							return;
						}
					}
					break;
				}
			}

			while (indexB < 6) {
				Force force = second.forces[indexB++];
				if (force != null && force.alive()) {
					if (!force.dizzy) {
						if (act(force)) {
							return;
						}
					}
					break;
				}
			}
		}
	}

	private void bossRound() {
		int indexA = 0;
		int indexB = 0;

		while (indexA < 6) {
			Force force = first.forces[indexA++];
			if (force != null && force.alive()) {
				if (!force.dizzy) {
					if (act(force)) {
						return;
					}
				}
			}
		}

		while (indexB < 6) {
			Force force = second.forces[indexB++];
			if (force != null && force.alive()) {
				if (!force.dizzy) {
					if (act(force)) {
						return;
					}
				}
			}
		}
	}

	private boolean act(Force force) {
		List<Force> targets = selectTarget(force);
		if (targets.isEmpty()) {
			return true;
		}

		if (recordFlag) {
			roundData = CommonPb.Round.newBuilder();
			roundData.setKey(force.key);
		}

		for (Force target : targets) {
			attack(force, target);
			if (recordFlag) {
				roundData.addAction(actionData);
			}
		}

		if (recordFlag) {
			recordData.addRound(roundData);
		}

		return false;
	}

	private void attack(Force force, Force target) {
		if (recordFlag) {
			actionData = CommonPb.Action.newBuilder();
			actionData.setTarget(target.key);
		}

		if (FightCalc.isDodge(force, target)) {
			if (recordFlag) {
				actionData.setDodge(true);
			}
			return;
		}

		float crit = 1;
		if (FightCalc.isCrit(force, target)) {
			if (recordFlag) {
				actionData.setCrit(true);
			}
			crit = 2;
			// 加入爆裂和坚韧后重新计算暴击率
			crit = FightCalc.calcCrit(force, target, crit);
		}

		if (FightCalc.isImpale(force, target)) {
			target.dizzy = true;
			if (recordFlag) {
				actionData.setImpale(true);
			}
		}

		if (force.staticTank.getAttackMode() == 4) {
			long hurt = 0;
			for (int i = 0; i < 5; i++) {
				if (target.alive()) {
					hurt = FightCalc.shakeValue(FightCalc.calcHurt(force, target, crit));
					target.hurt(hurt);
					force.fighter.hurt += hurt;
					if (recordFlag) {
						actionData.addHurt(hurt);
					}
				}
			}
		} else {
			long hurt = FightCalc.shakeValue(FightCalc.calcHurt(force, target, crit));
			target.hurt(hurt);
			force.fighter.hurt += hurt;
			if (recordFlag) {
				actionData.addHurt(hurt);
			}
		}

		if (recordFlag) {
			actionData.setCount(target.count);
		}
	}

	/**
	 * 
	 * Method: horizantalTarget
	 * 
	 * @Description: 横排目标
	 * @param target
	 * @return
	 * @return ArrayList<Force>
	 * @throws
	 */
	private List<Force> horizantalTarget(Fighter target) {
		ArrayList<Force> targets = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Force force = target.forces[i];
			if (force != null && force.alive()) {
				targets.add(force);
			}
		}

		if (targets.isEmpty()) {
			for (int i = 3; i < 6; i++) {
				Force force = target.forces[i];
				if (force != null && force.alive()) {
					targets.add(force);
				}
			}
		}
		return targets;
	}

	private void addColumnTarget(Force[] targets, ArrayList<Force> list, int column) {
		Force force = targets[column - 1];
		if (force != null && force.alive()) {
			list.add(force);
		}

		// if (!list.isEmpty()) {
		// return;
		// }

		force = targets[column + 2];
		if (force != null && force.alive()) {
			list.add(force);
		}

		// System.out.println("addColumnTarget list:" + list.size() + " column:"
		// + column);
		// printForce(targets);
	}

	protected void printForce(Force[] targets) {
		for (int i = 0; i < targets.length; i++) {
			Force force = targets[i];
			if (force != null) {
				System.out.println("force " + i + "|tankid:" + force.staticTank.getTankId() + "|key:" + force.key + "|pos:" + force.pos + "|count:"
						+ force.count);
			} else {
				System.out.println("force " + i);
			}
		}
	}

	/**
	 * 
	 * Method: verticalTarget
	 * 
	 * @Description: 竖排目标
	 * @param target
	 * @param pos
	 * @return
	 * @return ArrayList<Force>
	 * @throws
	 */
	private List<Force> verticalTarget(Fighter target, int pos) {
		// System.out.println("verticalTarget pos:" + pos);
		ArrayList<Force> targets = new ArrayList<>();
		int column = (pos - 1) % 3 + 1;
		int[] order = new int[3];
		if (column == 1) {
			order[0] = 1;
			order[1] = 2;
			order[2] = 3;
		} else if (column == 2) {
			order[0] = 2;
			order[1] = 1;
			order[2] = 3;
		} else {
			order[0] = 3;
			order[1] = 2;
			order[2] = 1;
		}

		for (int i : order) {
			addColumnTarget(target.forces, targets, i);
			if (!targets.isEmpty()) {
				break;
			}
		}
		return targets;
	}

	/**
	 * 
	 * Method: allTarget
	 * 
	 * @Description: 全体目标
	 * @param target
	 * @return
	 * @return ArrayList<Force>
	 * @throws
	 */
	private List<Force> allTarget(Fighter target) {
		ArrayList<Force> targets = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			Force force = target.forces[i];
			if (force != null && force.alive()) {
				targets.add(force);
			}
		}

		return targets;
	}

	/**
	 * 
	 * Method: oneTarget
	 * 
	 * @Description: 单体
	 * @param target
	 * @return
	 * @return ArrayList<Force>
	 * @throws
	 */
	private List<Force> oneTarget(Fighter target, int pos) {
		List<Force> targets = new ArrayList<>();
		List<Force> fromVertical = verticalTarget(target, pos);
		if (!fromVertical.isEmpty()) {
			targets.add(fromVertical.get(0));
		}

		return targets;
	}

	private List<Force> selectTarget(Force acter) {
		switch (acter.staticTank.getAttackMode()) {// 1.横排 2.竖排 3.全体 4.单体五连击
		case 1:
			return horizantalTarget(acter.fighter.oppoFighter);
		case 2:
			return verticalTarget(acter.fighter.oppoFighter, acter.pos);
		case 3:
			return allTarget(acter.fighter.oppoFighter);
		case 4:
			return oneTarget(acter.fighter.oppoFighter, acter.pos);
		default:
			break;
		}
		return null;
	}

	public int getWinState() {
		return winState;
	}

	public void setWinState(int winState) {
		this.winState = winState;
	}

	public void onForceDie(Force force) {
		arrangeAura(force);
		if (force.fighter.boss) {
			force.fighter.changeGod();
		}
	}
}
