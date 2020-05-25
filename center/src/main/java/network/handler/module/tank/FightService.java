package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.bossFight.domain.Boss;
import com.game.domain.Player;
import com.game.fight.domain.AttrData;
import com.game.fight.domain.Fighter;
import com.game.fight.domain.Force;
import com.game.util.Turple3;
import com.google.inject.Singleton;

import config.bean.ConfCombat;
import config.bean.ConfEquip;
import config.bean.ConfExplore;
import config.bean.ConfHero;
import config.bean.ConfMineForm;
import config.bean.ConfPart;
import config.bean.ConfPartyCombat;
import config.bean.ConfRefine;
import config.bean.ConfSkill;
import config.bean.ConfStaffing;
import config.bean.ConfTank;
import config.provider.ConfEquipProvider;
import config.provider.ConfHeroProvider;
import config.provider.ConfPartProvider;
import config.provider.ConfRefineProvider;
import config.provider.ConfSkillProvider;
import config.provider.ConfStaffingProvider;
import config.provider.ConfTankProvider;
import data.bean.BossFight;
import data.bean.Equip;
import data.bean.Form;
import data.bean.MilitaryScience;
import data.bean.MilitaryScienceGrid;
import data.bean.Part;
import data.bean.PartyScience;
import data.bean.RptTank;
import data.bean.Science;
import data.bean.Tank;
import define.AttrId;
import define.EffectType;
import define.ScienceId;
import inject.BeanManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;

@Singleton
public class FightService {

	public static FightService getInst() {
		return BeanManager.getBean(FightService.class);
	}

	private Fighter createFighter() {
		Fighter fighter = new Fighter();
		return fighter;
	}

	public Fighter createFighter(ConfCombat staticCombat) {
		Fighter fighter = createFighter();
		fighter.addForce(createForce(staticCombat, 1), 1);
		fighter.addForce(createForce(staticCombat, 2), 2);
		fighter.addForce(createForce(staticCombat, 3), 3);
		fighter.addForce(createForce(staticCombat, 4), 4);
		fighter.addForce(createForce(staticCombat, 5), 5);
		fighter.addForce(createForce(staticCombat, 6), 6);
		return fighter;
	}

	public Fighter createFighter(ConfExplore staticExplore) {
		Fighter fighter = createFighter();
		fighter.addForce(createForce(staticExplore, 1), 1);
		fighter.addForce(createForce(staticExplore, 2), 2);
		fighter.addForce(createForce(staticExplore, 3), 3);
		fighter.addForce(createForce(staticExplore, 4), 4);
		fighter.addForce(createForce(staticExplore, 5), 5);
		fighter.addForce(createForce(staticExplore, 6), 6);
		return fighter;
	}

	public Fighter createFighter(ConfMineForm staticMineForm) {
		Fighter fighter = createFighter();
		fighter.addForce(createForce(staticMineForm, 1), 1);
		fighter.addForce(createForce(staticMineForm, 2), 2);
		fighter.addForce(createForce(staticMineForm, 3), 3);
		fighter.addForce(createForce(staticMineForm, 4), 4);
		fighter.addForce(createForce(staticMineForm, 5), 5);
		fighter.addForce(createForce(staticMineForm, 6), 6);
		return fighter;
	}

	public Fighter createFighter(Player player, Form form, int type) {
		Fighter fighter = createFighter();
		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			fighter.addForce(createForce(player, i + 1, p[i], c[i]), i + 1);
		}

		if (form.getCommander() > 0) {
			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(form.getCommander());
			effectHero(fighter, staticHero, type);
			effectFirstValue(fighter, staticHero);
		}
		return fighter;
	}

	public Fighter createFighter(BossFight bossFight, Player player, Form form, int type) {
		Fighter fighter = createFighter();
		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			fighter.addForce(createForce(bossFight, player, i + 1, p[i], c[i]), i + 1);
		}

		if (form.getCommander() > 0) {
			ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(form.getCommander());
			effectHero(fighter, staticHero, type);
			effectFirstValue(fighter, staticHero);
		}
		return fighter;
	}

	public Fighter createFighter(Form form, ConfPartyCombat staticPartyCombat) {
		Fighter fighter = createFighter();
		fighter.addForce(createForce(form, staticPartyCombat, 1), 1);
		fighter.addForce(createForce(form, staticPartyCombat, 2), 2);
		fighter.addForce(createForce(form, staticPartyCombat, 3), 3);
		fighter.addForce(createForce(form, staticPartyCombat, 4), 4);
		fighter.addForce(createForce(form, staticPartyCombat, 5), 5);
		fighter.addForce(createForce(form, staticPartyCombat, 6), 6);
		return fighter;
	}

	// public Fighter createFighter(Form form, ConfMineForm staticMineForm) {
	// Fighter fighter = createFighter();
	// fighter.addForce(createForce(form, staticMineForm, 1), 1);
	// fighter.addForce(createForce(form, staticMineForm, 2), 2);
	// fighter.addForce(createForce(form, staticMineForm, 3), 3);
	// fighter.addForce(createForce(form, staticMineForm, 4), 4);
	// fighter.addForce(createForce(form, staticMineForm, 5), 5);
	// fighter.addForce(createForce(form, staticMineForm, 6), 6);
	// return fighter;
	// }

	public Fighter createBoss(Boss boss) {
		Fighter fighter = createFighter();
		fighter.boss = true;

		int lv = boss.getBossLv();
		int which = boss.getBossWhich();

		boolean dizzy = true;
		boolean god = true;
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (i < which) {
				count = 0;
			} else if (i > which) {
				count = 10000;
				god = true;
			} else {
				count = boss.getBossHp();
				god = false;
			}

			if (i == 5) {
				dizzy = false;
			} else {
				dizzy = true;
			}

			fighter.addForce(createBossForce(31 + (lv - 45) * 6 + i, count, i + 1, dizzy, god), i + 1);
		}
		return fighter;
	}

	public Force createForce(Player player, int pos, int tankId, int count) {
		if (tankId == 0) {
			return null;
		}

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		AttrData attrData = new AttrData(staticTank);
		equipAttr(attrData, player.equips.get(pos));
		partAttr(attrData, player.parts.get(staticTank.getType()));
		militaryAttr(attrData, player.militaryScienceGrids.get(tankId), player.militarySciences);
		scienceAttr(staticTank.getType(), attrData, player.sciences, PartyDataManager.getInst().getScience(player));
		skillAttr(staticTank.getType(), attrData, player.skills);
		effectAttr(player, attrData);
		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	public Force createForce(BossFight bossFight, Player player, int pos, int tankId, int count) {
		if (tankId == 0) {
			return null;
		}

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		AttrData attrData = new AttrData(staticTank, bossFight);
		equipAttr(attrData, player.equips.get(pos));
		partAttr(attrData, player.parts.get(staticTank.getType()));
		// militaryAttr(attrData, player.militaryScienceGrids.get(tankId),
		// player.militarySciences);
		scienceAttr(staticTank.getType(), attrData, player.sciences, PartyDataManager.getInst().getScience(player));
		skillAttr(staticTank.getType(), attrData, player.skills);
		effectAttr(player, attrData);
		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	public Force createForce(ConfCombat staticCombat, int pos) {
		List<Integer> slot = staticCombat.getForm().get(pos - 1);
		if (slot.isEmpty()) {
			return null;
		}

		int tankId = slot.get(0);
		int count = slot.get(1);

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		List<Integer> attrs = staticCombat.getAttr().get(pos - 1);
		AttrData attrData = new AttrData(attrs);

		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	// public Force createForce(ConfMineForm staticMineForm, int pos) {
	// List<Integer> slot = staticMineForm.getForm().get(pos - 1);
	// if (slot.isEmpty()) {
	// return null;
	// }
	//
	// int tankId = slot.get(0);
	// int count = slot.get(1);
	//
	// ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
	// List<Integer> attrs = staticMineForm.getAttr().get(pos - 1);
	// AttrData attrData = new AttrData(attrs);
	//
	// Force force = new Force(staticTank, attrData, pos, count);
	// return force;
	// }

	public Force createForce(ConfExplore staticExplore, int pos) {
		List<Integer> slot = staticExplore.getForm().get(pos - 1);
		if (slot.isEmpty()) {
			return null;
		}

		int tankId = slot.get(0);
		int count = slot.get(1);

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		List<Integer> attrs = staticExplore.getAttr().get(pos - 1);
		AttrData attrData = new AttrData(attrs);

		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	public Force createForce(ConfMineForm staticMineForm, int pos) {
		List<Integer> slot = staticMineForm.getForm().get(pos - 1);
		if (slot.isEmpty()) {
			return null;
		}

		int tankId = slot.get(0);
		int count = slot.get(1);

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		List<Integer> attrs = staticMineForm.getAttr().get(pos - 1);
		AttrData attrData = new AttrData(attrs);

		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	public Force createForce(Form form, ConfPartyCombat staticPartyCombat, int pos) {
		int p = form.p[pos - 1];
		if (p <= 0) {
			return null;
		}

		int tankId = p;
		int count = form.c[pos - 1];

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		List<Integer> attrs = staticPartyCombat.getAttr().get(pos - 1);
		AttrData attrData = new AttrData(attrs);

		Force force = new Force(staticTank, attrData, pos, count);
		return force;
	}

	// public Force createForce(Form form, ConfMineForm staticMineForm, int
	// pos) {
	// int p = form.p[pos - 1];
	// if (p <= 0) {
	// return null;
	// }
	//
	// int tankId = p;
	// int count = form.c[pos - 1];
	//
	// ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
	// List<Integer> attrs = staticMineForm.getAttr().get(pos - 1);
	// AttrData attrData = new AttrData(attrs);
	//
	// Force force = new Force(staticTank, attrData, pos, count);
	// return force;
	// }

	public Force createBossForce(int tankId, int count, int pos, boolean dizzy, boolean god) {
		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		AttrData attrData = new AttrData(staticTank);
		Force force = new Force(staticTank, attrData, pos, count);
		force.dizzy = dizzy;
		force.god = god;
		return force;
	}

	/**
	 * 
	 * Method: equipAttr
	 * 
	 * @Description: 加装备属性 @param attrData @param equips @return void @throws
	 */
	private void equipAttr(AttrData attrData, Map<Integer, Equip> equips) {
		Iterator<Equip> it = equips.values().iterator();
		int blue = 0;
		int purple = 0;
		while (it.hasNext()) {
			Equip equip = it.next();
			ConfEquip staticEquip = ConfEquipProvider.getInst().getConfigById(equip.getEquipId());
			int attrId = staticEquip.getAttributeId();
			int value = staticEquip.getA() + staticEquip.getB() * (equip.getLv() - 1);
			attrData.addValue(attrId, value);
			if (staticEquip.getQuality() == 3) {
				blue++;
			} else if (staticEquip.getQuality() == 4) {
				purple++;
			}
		}

		int v = 0;
		if (blue == 6) {
			v = 50;
		} else if (purple == 6) {
			v = 100;
		}

		if (v != 0) {
			attrData.addValue(AttrId.ATTACK_F, v * 10);
			attrData.addValue(AttrId.HIT, v);
			attrData.addValue(AttrId.CRIT, v);

			attrData.addValue(AttrId.HP_F, v * 10);
			attrData.addValue(AttrId.DODGE, v);
			attrData.addValue(AttrId.CRITDEF, v);
		}

	}

	/**
	 * 
	 * Method: partAttr
	 * 
	 * @Description: 加配件属性 @param attrData @param equips @return void @throws
	 */
	private void partAttr(AttrData attrData, Map<Integer, Part> equips) {
		Iterator<Part> it = equips.values().iterator();
		while (it.hasNext()) {
			Part part = it.next();
			ConfPart staticPart = ConfPartProvider.getInst().getConfigById(part.getPartId());
			int attrId1 = staticPart.getAttr1();
			int value1 = staticPart.getA1() * (part.getUpLv() + 1) + staticPart.getB1() * part.getRefitLv();

			int attrId2 = staticPart.getAttr2();
			int value2 = staticPart.getA2() * (part.getUpLv() + 1) + staticPart.getB2() * part.getRefitLv();

			attrData.addValue(attrId1, value1);
			attrData.addValue(attrId2, value2);
		}
	}

	/**
	 * 
	 * Method: militaryAttr
	 * 
	 * @Description: 军工科技对属性加层 @param attrData @param scienceGrids @return
	 *               void @throws
	 */
	private void militaryAttr(AttrData attrData, Map<Integer, MilitaryScienceGrid> scienceGrids, Map<Integer, MilitaryScience> sciences) {
//		if (scienceGrids != null && sciences != null) {
//			Iterator<MilitaryScienceGrid> it = scienceGrids.values().iterator();
//			while (it.hasNext()) {
//				MilitaryScienceGrid grid = it.next();
//				if (grid.getMilitaryScienceId() != 0) {
//					// 通过科技id 获取科技信息
//					MilitaryScience s = sciences.get(grid.getMilitaryScienceId());
//					if (s.getLevel() != 0) {
//						StaticMilitaryDevelopTree tree = staticMilitaryDataMgr.getStaticMilitaryDevelopTree(s.getMilitaryScienceId(), s.getLevel());
//						List<List<Integer>> list = tree.getEffect();
//						for (List<Integer> list2 : list) {
//							if (list2.size() > 0) {
//								int attrId = list2.get(0);
//								int value = list2.get(1);
//								attrData.addValue(attrId, value);
//							}
//						}
//					}
//				}
//			}
//		}
	}

	/**
	 * 
	 * Method: scienceAttr
	 * 
	 * @Description: 加科技属性 @param tankType @param attrData @param sciences @return
	 *               void @throws
	 */
	private void scienceAttr(int tankType, AttrData attrData, Map<Integer, Science> sciences, Map<Integer, PartyScience> partySciences) {
		Iterator<Science> it = sciences.values().iterator();
		while (it.hasNext()) {
			Science science = (Science) it.next();
			ConfRefine staticRefine = ConfRefineProvider.getInst().getConfigById(science.getScienceId());
			if (staticRefine.getType() == tankType) {
				int attrId = staticRefine.getAttributeId();
				int value = staticRefine.getAddtion() * science.getScienceLv();
				attrData.addValue(attrId, value);
			}
		}

		if (partySciences != null) {
			Iterator<PartyScience> ite = partySciences.values().iterator();
			while (ite.hasNext()) {
				PartyScience science = (PartyScience) ite.next();
				ConfRefine staticRefine = ConfRefineProvider.getInst().getConfigById(science.getScienceId());
				if (staticRefine.getType() == 5 || staticRefine.getType() == tankType) {
					int attrId = staticRefine.getAttributeId();
					int value = staticRefine.getAddtion() * science.getScienceLv();
					attrData.addValue(attrId, value);
				}
			}
		}
	}

	/**
	 * 
	 * Method: skillAttr
	 * 
	 * @Description: 加技能属性 @param tankType @param attrData @param skills @return
	 *               void @throws
	 */
	private void skillAttr(int tankType, AttrData attrData, Map<Integer, Integer> skills) {
		for (Map.Entry<Integer, Integer> entry : skills.entrySet()) {
			ConfSkill staticSkill = ConfSkillProvider.getInst().getConfigById(entry.getKey());
			if (staticSkill.getTarget() == 0 || staticSkill.getTarget() == tankType) {
				attrData.addValue(staticSkill.getAttr(), staticSkill.getAttrValue() * entry.getValue());
			}
		}
	}

	/**
	 * 
	 * Method: effectAttr
	 * 
	 * @Description: 效果加成 @param player @param attrData @return void @throws
	 */
	private void effectAttr(Player player, AttrData attrData) {
		// 增加己方部队20%伤害
		if (player.effects.containsKey(EffectType.ADD_HURT)) {
			attrData.addValue(AttrId.ATTACK_F, 2000);
		} else if (player.effects.containsKey(EffectType.ADD_HURT_SUPUR)) {
			attrData.addValue(AttrId.ATTACK_F, 3000);
		}

		// 降低地方部队20%伤害
		if (player.effects.containsKey(EffectType.REDUCE_HURT)) {
			attrData.addValue(AttrId.INJURED_F, 2000);
		} else if (player.effects.containsKey(EffectType.REDUCE_HURT_SUPER)) {
			attrData.addValue(AttrId.INJURED_F, 3000);
		}

		// 使用改变基地外观.命中+15%.闪避暴击抗暴+5%
		if (player.effects.containsKey(EffectType.CHANGE_SURFACE_1)) {
			attrData.addValue(AttrId.HIT, 150);
			attrData.addValue(AttrId.DODGE, 50);
			attrData.addValue(AttrId.CRIT, 50);
			attrData.addValue(AttrId.CRITDEF, 50);
		}

		// 编制加成
		ConfStaffing staffing = ConfStaffingProvider.getInst().getConfigById(player.lord.getStaffing());
		if (staffing != null) {
			List<List<Integer>> attrs = staffing.getAttr();
			for (List<Integer> attr : attrs) {
				attrData.addValue(attr.get(0), attr.get(1));
			}
		}
	}

	/**
	 * 
	 * Method: effectHeroAttr
	 * 
	 * @Description: 加武将属性和技能属性 @param fighter @param staticHero @param type 1.攻打副本
	 *               2.防守玩家 3.其他 @return void @throws
	 */
	public void effectHero(Fighter fighter, ConfHero staticHero, int type) {
		List<List<Integer>> list = staticHero.getAttr();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				List<Integer> one = list.get(i);
				if (one.size() != 2) {
					continue;
				}

				for (Force force : fighter.forces) {
					if (force != null) {
						force.attrData.addValue(one.get(0), one.get(1));
						effectHeroSkill(type, force, staticHero);
					}
				}
			}
		}
	}

	private void effectHeroAttr(AttrData attrData, ConfHero staticHero) {
		if (staticHero == null) {
			return;
		}

		List<List<Integer>> list = staticHero.getAttr();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				List<Integer> one = list.get(i);
				if (one.size() != 2) {
					continue;
				}

				attrData.addValue(one.get(0), one.get(1));
			}
		}
	}

	private void effectHeroSkill(int type, Force force, ConfHero staticHero) {
		if (type == 1) {
			if (staticHero.getSkillId() == 1) {
				force.attrData.addValue(AttrId.ATTACK_F, staticHero.getSkillValue());
			} else if (staticHero.getSkillId() == 2) {
				force.attrData.addValue(AttrId.INJURED_F, staticHero.getSkillValue());
			}
		} else if (type == 2) {
			if (staticHero.getSkillId() == 8) {
				force.attrData.addValue(AttrId.INJURED_F, staticHero.getSkillValue());
			}
		}
	}

	private void effectFirstValue(Fighter fighter, ConfHero staticHero) {
		if (staticHero.getSkillId() == 6) {
			fighter.firstValue = staticHero.getSkillValue();
		}
	}

	public float effectCombatExpAdd(Player player, ConfHero staticHero) {
		float factor = 1;

		if (staticHero != null && staticHero.getSkillId() == 3) {
			factor += (staticHero.getSkillValue() / 100.0f);
		}

		Science science1 = player.sciences.get(ScienceId.FIGHT_EXP);
		if (science1 != null) {
			factor += (5 * science1.getScienceLv() / 100.0f);
		}

		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science2 = sciences.get(ScienceId.PARTY_FIGHT_EXP);
			if (science2 != null) {
				factor += (science2.getScienceLv() / 100.0f);
			}
		}

//		int revelry[] = activityDataManager.revelry();
//		factor += Float.valueOf(revelry[1]) / 100f;

		return factor;
	}

	public float effectMineExpAdd(Player player, ConfHero staticHero) {
		float factor = 1;

		if (staticHero != null && staticHero.getSkillId() == 4) {
			factor += (staticHero.getSkillValue() / 100.0f);
		}

		Science science1 = player.sciences.get(ScienceId.FIGHT_EXP);
		if (science1 != null) {
			factor += (5 * science1.getScienceLv() / 100.0f);
		}

		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science2 = sciences.get(ScienceId.PARTY_FIGHT_EXP);
			if (science2 != null) {
				factor += (science2.getScienceLv() / 100.0f);
			}
		}

		return factor;
	}

	public float effectExpAdd(Player player) {
		float factor = 1;

		Science science1 = player.sciences.get(ScienceId.FIGHT_EXP);
		if (science1 != null) {
			factor += (5 * science1.getScienceLv() / 100.0f);
		}

		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science2 = sciences.get(ScienceId.PARTY_FIGHT_EXP);
			if (science2 != null) {
				factor += (science2.getScienceLv() / 100.0f);
			}
		}

		return factor;
	}

	/**
	 * 
	 * Method: calcTankFight
	 * 
	 * @Description: 计算坦克的战力(不包括装备) @param player @param tankId @return @return
	 *               int @throws
	 */
	private float calcTankFightWithoutEquip(Player player, int tankId, ConfHero staticHero) {
		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);

		AttrData attrData = new AttrData(staticTank);

		partAttr(attrData, player.parts.get(staticTank.getType()));
		militaryAttr(attrData, player.militaryScienceGrids.get(tankId), player.militarySciences);
		scienceAttr(staticTank.getType(), attrData, player.sciences, PartyDataManager.getInst().getScience(player));
		skillAttr(staticTank.getType(), attrData, player.skills);
		effectHeroAttr(attrData, staticHero);

		return attrData.attackF * staticTank.getAttckFactor() / 10 + attrData.hpF * staticTank.getHpFactor() / 10 + (attrData.hit - staticTank.getHit()) * 0.1f + (attrData.crit - staticTank.getCrit()) * 0.1f + (attrData.critDef - staticTank.getCritDef()) * 0.1f
				+ (attrData.dodge - staticTank.getDodge()) * 0.1f + (attrData.impale - staticTank.getImpale()) * 0.01f + (attrData.defend - staticTank.getDefend()) * 0.01f + staticTank.getFight();
	}

	/**
	 * 
	 * Method: calcTankEquipFight
	 * 
	 * @Description: 计算坦克的装备战力 @param player @param tankId @param
	 *               pos @return @return float @throws
	 */
	private float calcTankEquipFight(Player player, int tankId, int pos) {
		if (player.equips.get(pos).isEmpty()) {
			return 0;
		}

		ConfTank staticTank = ConfTankProvider.getInst().getConfigById(tankId);
		AttrData attrData = new AttrData(staticTank);
		equipAttr(attrData, player.equips.get(pos));

		return attrData.attackF * staticTank.getAttckFactor() / 10 + attrData.hpF * staticTank.getHpFactor() / 10 + (attrData.hit - staticTank.getHit()) * 0.1f + (attrData.crit - staticTank.getCrit()) * 0.1f + (attrData.critDef - staticTank.getCritDef()) * 0.1f
				+ (attrData.dodge - staticTank.getDodge()) * 0.1f + (attrData.impale - staticTank.getImpale()) * 0.01f + (attrData.defend - staticTank.getDefend()) * 0.01f;
	}

	/**
	 * 
	 * Method: calcFormFight
	 * 
	 * @Description: 计算阵型战力 @param player @param form @return @return int @throws
	 */
	public int calcFormFight(Player player, Form form) {
		ConfHero staticHero = null;
		if (form.getCommander() > 0) {
			staticHero = ConfHeroProvider.getInst().getConfigById(form.getCommander());
		}

		Float fight;
		int totalFight = 0;
		Map<Integer, Float> cacheFight = new HashMap<Integer, Float>();
		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0) {
				fight = cacheFight.get(p[i]);
				if (fight == null) {
					fight = calcTankFightWithoutEquip(player, p[i], staticHero);
					cacheFight.put(p[i], fight);
				}
				totalFight += (fight * c[i] + calcTankEquipFight(player, p[i], i + 1) * c[i]);
			}
		}

		return totalFight;
	}

	/**
	 * 
	 * Method: selectMaxFightHero
	 * 
	 * @Description: 上阵优先级最高的武将 @param player @return @return ConfHero @throws
	 */
	public ConfHero selectMaxFightHero(Player player) {
		Iterator<Integer> it = player.heros.keySet().iterator();
		int i;
		int max = 0;
		while (it.hasNext()) {
			i = it.next();
			if (i > max) {
				max = i;
			}
		}

		ConfHero staticHero = ConfHeroProvider.getInst().getConfigById(max);
		if (staticHero.getType() != 2) {
			return null;
		}

		return staticHero;
	}

	/**
	 * 
	 * Method: calcFight
	 * 
	 * @Description: 计算玩家最大战力 @param player @return @return int @throws
	 */
	public int calcMaxFight(Player player) {
		int slot = PlayerDataManager.formSlotCount(player.lord.getLevel());
		ConfHero staticHero = selectMaxFightHero(player);
		int tankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);
		Map<Integer, Tank> tanks = player.tanks;
		List<Turple3<Integer, Integer, Integer>> orderList = new ArrayList<>();
		Map<Integer, Float> cacheFight = new HashMap<Integer, Float>();
		Iterator<Tank> it = tanks.values().iterator();
		Integer tankId;
		int count;
		while (it.hasNext()) {
			Tank tank = (Tank) it.next();
			tankId = tank.getTankId();
			count = tank.getCount();
			Float fight = cacheFight.get(tankId);
			if (fight == null) {
				fight = calcTankFightWithoutEquip(player, tankId, staticHero);
				cacheFight.put(tankId, fight);
			}

			if (count > 0) {
				if (count <= tankCount) {
					orderList.add(new Turple3<Integer, Integer, Integer>(tankId, (int) (fight * count), count));
					break;
				} else {
					count -= tankCount;
					orderList.add(new Turple3<Integer, Integer, Integer>(tankId, (int) (fight * tankCount), tankCount));
				}
			}
		}

		Collections.sort(orderList, new Comparator<Turple3<Integer, Integer, Integer>>() {
			@Override
			public int compare(Turple3<Integer, Integer, Integer> o1, Turple3<Integer, Integer, Integer> o2) {
				if (o1.getB() < o2.getB()) {
					return 1;
				}
				return -1;
			}
		});

		int fight = 0;
		for (int i = 0; i < slot && i < orderList.size(); i++) {
			Turple3<Integer, Integer, Integer> one = orderList.get(i);
			fight += one.getB();
			fight += calcTankEquipFight(player, one.getA(), i + 1) * one.getC();
		}

		return fight;
	}

	/**
	 * 
	 * Method: statisticHaustTank
	 * 
	 * @Description: 只统计战斗中坦克消耗，不做扣除 @param fighter @return @return
	 *               Map<Integer,RptTank> @throws
	 */
	public Map<Integer, RptTank> statisticHaustTank(Fighter fighter) {
		Map<Integer, RptTank> map = new HashMap<Integer, RptTank>();
		int killed = 0;
		int tankId;
		for (Force force : fighter.forces) {
			if (force != null) {
				killed = force.killed;
				tankId = force.staticTank.getTankId();
				if (killed > 0) {
					RptTank rptTank = map.get(tankId);
					if (rptTank != null) {
						rptTank.setCount(rptTank.getCount() + killed);
					} else {
						rptTank = new RptTank(tankId, killed);
						map.put(tankId, rptTank);
					}
				}
			}
		}

		return map;
	}
}
