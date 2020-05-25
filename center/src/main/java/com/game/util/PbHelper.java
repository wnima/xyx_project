package com.game.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.game.domain.ActivityBase;
import com.game.domain.PartyData;
import com.game.domain.Player;
import com.game.domain.SeniorPartyScoreRank;
import com.game.domain.SeniorScoreRank;
import com.game.warFight.domain.WarMember;
import com.game.warFight.domain.WarParty;

import config.bean.ConfActAward;
import config.bean.ConfActFortune;
import config.bean.ConfActGamble;
import config.bean.ConfActGeneral;
import config.bean.ConfActMecha;
import config.bean.ConfActPartResolve;
import config.bean.ConfActQuota;
import config.bean.ConfActRank;
import config.bean.ConfActTech;
import config.bean.ConfActVacationland;
import data.bean.ActPartyRank;
import data.bean.ActPlayerRank;
import data.bean.Activity;
import data.bean.Arena;
import data.bean.Army;
import data.bean.ArmyStatu;
import data.bean.Bless;
import data.bean.BossFight;
import data.bean.BuildQue;
import data.bean.Cash;
import data.bean.Chip;
import data.bean.Collect;
import data.bean.Combat;
import data.bean.Effect;
import data.bean.Equip;
import data.bean.FailNum;
import data.bean.Form;
import data.bean.Friend;
import data.bean.Grab;
import data.bean.Guard;
import data.bean.Hero;
import data.bean.LiveTask;
import data.bean.Lord;
import data.bean.LotteryEquip;
import data.bean.Mail;
import data.bean.Man;
import data.bean.March;
import data.bean.MilitaryMaterial;
import data.bean.MilitaryScience;
import data.bean.MilitaryScienceGrid;
import data.bean.Mill;
import data.bean.Mine;
import data.bean.Part;
import data.bean.PartyApply;
import data.bean.PartyCombat;
import data.bean.PartyDonate;
import data.bean.PartyLvRank;
import data.bean.PartyProp;
import data.bean.PartyRank;
import data.bean.PartyScience;
import data.bean.PartySection;
import data.bean.Prop;
import data.bean.PropQue;
import data.bean.RefitQue;
import data.bean.RptTank;
import data.bean.Ruins;
import data.bean.Science;
import data.bean.ScienceQue;
import data.bean.Store;
import data.bean.Tank;
import data.bean.TankQue;
import data.bean.Task;
import data.bean.Trend;
import data.bean.TrendParam;
import data.bean.Weal;
import define.EffectType;
import domain.Member;
import pb.CommonPb;
import pb.CommonPb.AwardPB;
import pb.CommonPb.Extreme;
import pb.CommonPb.Record;
import pb.CommonPb.RptAtkWar;
import pb.CommonPb.TwoInt;
import pb.CommonPb.WarRecord;

public class PbHelper {
	public static byte[] putShort(short s) {
		byte[] b = new byte[2];
		b[0] = (byte) (s >> 8);
		b[1] = (byte) (s >> 0);
		return b;
	}

	static public short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] & 0xff) | b[index + 0] << 8));
	}

	static public CommonPb.TwoInt createTwoIntPb(int p, int c) {
		TwoInt.Builder builder = TwoInt.newBuilder();
		builder.setV1(p);
		builder.setV2(c);
		return builder.build();
	}

	static public CommonPb.Prop createPropPb(Prop prop) {
		CommonPb.Prop.Builder builder = CommonPb.Prop.newBuilder();
		builder.setPropId(prop.getPropId());
		builder.setCount(prop.getCount());
		return builder.build();
	}

	static public CommonPb.PropQue createPropQuePb(PropQue e) {
		CommonPb.PropQue.Builder builder = CommonPb.PropQue.newBuilder();
		builder.setKeyId(e.getKeyId());
		builder.setPropId(e.getPropId());
		builder.setCount(e.getCount());
		builder.setState(e.getState());
		builder.setPeriod(e.getPeriod());
		builder.setEndTime(e.getEndTime());
		return builder.build();
	}

	static public CommonPb.Tank createTankPb(Tank tank) {
		CommonPb.Tank.Builder builder = CommonPb.Tank.newBuilder();
		builder.setTankId(tank.getTankId());
		builder.setCount(tank.getCount());
		builder.setRest(tank.getRest());
		return builder.build();
	}

	static public CommonPb.RptTank createRtpTankPb(RptTank rptTank) {
		CommonPb.RptTank.Builder builder = CommonPb.RptTank.newBuilder();
		builder.setTankId(rptTank.getTankId());
		builder.setCount(rptTank.getCount());
		return builder.build();
	}

	static public CommonPb.TankQue createTankQuePb(TankQue e) {
		CommonPb.TankQue.Builder builder = CommonPb.TankQue.newBuilder();
		builder.setKeyId(e.getKeyId());
		builder.setTankId(e.getTankId());
		builder.setCount(e.getCount());
		builder.setState(e.getState());
		builder.setPeriod(e.getPeriod());
		builder.setEndTime(e.getEndTime());
		return builder.build();
	}

	static public CommonPb.RefitQue createRefitQuePb(RefitQue e) {
		CommonPb.RefitQue.Builder builder = CommonPb.RefitQue.newBuilder();
		builder.setKeyId(e.getKeyId());
		builder.setTankId(e.getTankId());
		builder.setRefitId(e.getRefitId());
		builder.setCount(e.getCount());
		builder.setState(e.getState());
		builder.setPeriod(e.getPeriod());
		builder.setEndTime(e.getEndTime());
		return builder.build();
	}

	static public CommonPb.BuildQue createBuildQuePb(BuildQue e) {
		CommonPb.BuildQue.Builder builder = CommonPb.BuildQue.newBuilder();
		builder.setKeyId(e.getKeyId());
		builder.setBuildingId(e.getBuildingId());
		builder.setPos(e.getPos());
		builder.setPeriod(e.getPeriod());
		builder.setEndTime(e.getEndTime());
		return builder.build();
	}

	// static public CommonPb.Form createFormPb(Army army) {
	// CommonPb.Form.Builder builder = CommonPb.Form.newBuilder();
	// int v = army.getCommander();
	// if (v != 0) {
	// builder.setCommander(v);
	// }
	//
	// v = army.getP1();
	// if (v != 0) {
	// builder.setP1(PbHelper.createTwoIntPb(army.getP1(), army.getC1()));
	// }
	//
	// v = army.getP2();
	// if (v != 0) {
	// builder.setP2(PbHelper.createTwoIntPb(army.getP2(), army.getC2()));
	// }
	//
	// v = army.getP3();
	// if (v != 0) {
	// builder.setP3(PbHelper.createTwoIntPb(army.getP3(), army.getC3()));
	// }
	//
	// v = army.getP4();
	// if (v != 0) {
	// builder.setP4(PbHelper.createTwoIntPb(army.getP4(), army.getC4()));
	// }
	//
	// v = army.getP5();
	// if (v != 0) {
	// builder.setP5(PbHelper.createTwoIntPb(army.getP5(), army.getC5()));
	// }
	//
	// v = army.getP6();
	// if (v != 0) {
	// builder.setP6(PbHelper.createTwoIntPb(army.getP6(), army.getC6()));
	// }
	// return builder.build();
	// }

	static public Grab createGrab(CommonPb.Grab grab) {
		Grab e = new Grab();

		if (grab.getIron() != 0) {
			e.rs[0] = grab.getIron();
		}

		if (grab.getOil() != 0) {
			e.rs[1] = grab.getOil();
		}

		if (grab.getCopper() != 0) {
			e.rs[2] = grab.getCopper();
		}

		if (grab.getSilicon() != 0) {
			e.rs[3] = grab.getSilicon();
		}

		if (grab.getStone() != 0) {
			e.rs[4] = grab.getStone();
		}

		return e;
	}

	static public CommonPb.Grab createGrabPb(Grab grab) {
		CommonPb.Grab.Builder builder = CommonPb.Grab.newBuilder();

		if (grab.rs[0] != 0) {
			builder.setIron(grab.rs[0]);
		}

		if (grab.rs[1] != 0) {
			builder.setOil(grab.rs[1]);
		}

		if (grab.rs[2] != 0) {
			builder.setCopper(grab.rs[2]);
		}

		if (grab.rs[3] != 0) {
			builder.setSilicon(grab.rs[3]);
		}

		if (grab.rs[4] != 0) {
			builder.setStone(grab.rs[4]);
		}

		return builder.build();
	}

	static public Collect createCollect(CommonPb.Collect collect) {
		Collect e = new Collect();
		e.load = collect.getLoad();
		e.speed = collect.getSpeed();
		return e;
	}

	static public CommonPb.Collect createCollectPb(Collect collect) {
		CommonPb.Collect.Builder builder = CommonPb.Collect.newBuilder();
		builder.setLoad(collect.load);
		builder.setSpeed(collect.speed);
		return builder.build();
	}

	static public CommonPb.FailNum createFailNumPb(FailNum failNum) {
		CommonPb.FailNum.Builder builder = CommonPb.FailNum.newBuilder();
		builder.setOperType(failNum.getOperType());
		builder.setNum(failNum.getNum());
		return builder.build();
	}

	static public CommonPb.Army createArmyPb(Army army) {
		CommonPb.Army.Builder builder = CommonPb.Army.newBuilder();
		builder.setKeyId(army.getKeyId());
		builder.setTarget(army.getTarget());
		builder.setState(army.getState());
		builder.setPeriod(army.getPeriod());
		builder.setEndTime(army.getEndTime());
		builder.setForm(createFormPb(army.getForm()));
		if (army.getGrab() != null) {
			builder.setGrab(createGrabPb(army.getGrab()));
		}

		if (army.getCollect() != null) {
			builder.setCollect(createCollectPb(army.getCollect()));
		}

		if (army.getStaffingTime() != 0) {
			builder.setStaffingTime(army.getStaffingTime());
		}

		if (army.getSenior()) {
			builder.setSenior(true);
		}

		if (army.getOccupy()) {
			builder.setOccupy(true);
		}

		return builder.build();
	}

	static public CommonPb.Form createFormPb(Form form) {
		CommonPb.Form.Builder builder = CommonPb.Form.newBuilder();
		int v = form.getCommander();
		if (v != 0) {
			builder.setCommander(v);
		}

		v = form.getType();
		if (v != 0) {
			builder.setType(v);
		}

		v = form.p[0];
		if (v != 0) {
			builder.setP1(createTwoIntPb(v, form.c[0]));
		}

		v = form.p[1];
		if (v != 0) {
			builder.setP2(createTwoIntPb(v, form.c[1]));
		}

		v = form.p[2];
		if (v != 0) {
			builder.setP3(createTwoIntPb(v, form.c[2]));
		}

		v = form.p[3];
		if (v != 0) {
			builder.setP4(createTwoIntPb(v, form.c[3]));
		}

		v = form.p[4];
		if (v != 0) {
			builder.setP5(createTwoIntPb(v, form.c[4]));
		}

		v = form.p[5];
		if (v != 0) {
			builder.setP6(createTwoIntPb(v, form.c[5]));
		}
		return builder.build();
	}

	static public CommonPb.Form createFormPb(List<List<Integer>> tanks) {
		CommonPb.Form.Builder builder = CommonPb.Form.newBuilder();
		List<Integer> one;
		one = tanks.get(0);
		if (!one.isEmpty()) {
			builder.setP1(createTwoIntPb(one.get(0), one.get(1)));
		}

		one = tanks.get(1);
		if (!one.isEmpty()) {
			builder.setP2(createTwoIntPb(one.get(0), one.get(1)));
		}

		one = tanks.get(2);
		if (!one.isEmpty()) {
			builder.setP3(createTwoIntPb(one.get(0), one.get(1)));
		}

		one = tanks.get(3);
		if (!one.isEmpty()) {
			builder.setP4(createTwoIntPb(one.get(0), one.get(1)));
		}

		one = tanks.get(4);
		if (!one.isEmpty()) {
			builder.setP5(createTwoIntPb(one.get(0), one.get(1)));
		}

		one = tanks.get(5);
		if (!one.isEmpty()) {
			builder.setP6(createTwoIntPb(one.get(0), one.get(1)));
		}

		return builder.build();
	}

	static public Form createForm(List<List<Integer>> tanks) {
		Form form = new Form();
		List<Integer> one;
		for (int i = 0; i < tanks.size() && i < 6; i++) {
			one = tanks.get(i);
			if (one.isEmpty())
				continue;
			form.p[i] = one.get(0);
			form.c[i] = one.get(1);
		}

		return form;
	}

	static public Form createForm(CommonPb.Form form) {
		Form e = new Form();
		e.setType(form.getType());
		if (form.getCommander() != 0) {
			e.setCommander(form.getCommander());
		}

		if (form.hasP1()) {
			CommonPb.TwoInt p = form.getP1();
			e.p[0] = p.getV1();
			e.c[0] = p.getV2();
		}

		if (form.hasP2()) {
			CommonPb.TwoInt p = form.getP2();
			e.p[1] = p.getV1();
			e.c[1] = p.getV2();
		}

		if (form.hasP3()) {
			CommonPb.TwoInt p = form.getP3();
			e.p[2] = p.getV1();
			e.c[2] = p.getV2();
		}

		if (form.hasP4()) {
			CommonPb.TwoInt p = form.getP4();
			e.p[3] = p.getV1();
			e.c[3] = p.getV2();
		}

		if (form.hasP5()) {
			CommonPb.TwoInt p = form.getP5();
			e.p[4] = p.getV1();
			e.c[4] = p.getV2();
		}

		if (form.hasP6()) {
			CommonPb.TwoInt p = form.getP6();
			e.p[5] = p.getV1();
			e.c[5] = p.getV2();
		}

		return e;
	}

	static public CommonPb.Chip createChipPb(Chip chip) {
		CommonPb.Chip.Builder builder = CommonPb.Chip.newBuilder();
		builder.setChipId(chip.getChipId());
		builder.setCount(chip.getCount());
		return builder.build();
	}

	static public CommonPb.Hero createHeroPb(Hero hero) {
		CommonPb.Hero.Builder builder = CommonPb.Hero.newBuilder();
		builder.setKeyId(hero.getHeroId());
		builder.setHeroId(hero.getHeroId());
		builder.setCount(hero.getCount());
		return builder.build();
	}

	static public CommonPb.Combat createCombatPb(Combat combat) {
		CommonPb.Combat.Builder builder = CommonPb.Combat.newBuilder();
		builder.setCombatId(combat.getCombatId());
		builder.setStar(combat.getStar());
		return builder.build();
	}

	static public CommonPb.Equip createEquipPb(Equip equip) {
		CommonPb.Equip.Builder builder = CommonPb.Equip.newBuilder();
		builder.setKeyId(equip.getKeyId());
		builder.setEquipId(equip.getEquipId());
		builder.setLv(equip.getLv());
		builder.setExp(equip.getExp());
		builder.setPos(equip.getPos());
		return builder.build();
	}

	static public CommonPb.Part createPartPb(Part part) {
		CommonPb.Part.Builder builder = CommonPb.Part.newBuilder();
		builder.setKeyId(part.getKeyId());
		builder.setPartId(part.getPartId());
		builder.setUpLv(part.getUpLv());
		builder.setRefitLv(part.getRefitLv());
		builder.setPos(part.getPos());
		builder.setLocked(part.isLocked());
		return builder.build();
	}

	static public CommonPb.Science createSciencePb(Science science) {
		CommonPb.Science.Builder builder = CommonPb.Science.newBuilder();
		builder.setScienceId(science.getScienceId());
		builder.setScienceLv(science.getScienceLv());
		return builder.build();
	}

	static public CommonPb.Science createPartySciencePb(PartyScience science) {
		CommonPb.Science.Builder builder = CommonPb.Science.newBuilder();
		builder.setScienceId(science.getScienceId());
		builder.setScienceLv(science.getScienceLv());
		builder.setSchedule(science.getSchedule());
		return builder.build();
	}

	static public CommonPb.ScienceQue createScienceQuePb(ScienceQue e) {
		CommonPb.ScienceQue.Builder builder = CommonPb.ScienceQue.newBuilder();
		builder.setKeyId(e.getKeyId());
		builder.setScienceId(e.getScienceId());
		builder.setPeriod(e.getPeriod());
		builder.setState(e.getState());
		builder.setEndTime(e.getEndTime());
		return builder.build();
	}

	/**
	 * 
	 * Method: createAwardPb
	 * 
	 * @Description: 无keyId的奖励 @param type @param id @param count @return @return
	 *               AwardPB @throws
	 */
	static public AwardPB createAwardPb(int type, int id, int count) {
		AwardPB.Builder builder = AwardPB.newBuilder();
		builder.setType(type);
		builder.setId(id);
		builder.setCount(count);
		return builder.build();
	}

	/**
	 * 
	 * Method: createAwardPb
	 * 
	 * @Description: 有keyId的奖励 @param type @param id @param count @param
	 *               keyId @return @return AwardPB @throws
	 */
	static public AwardPB createAwardPb(int type, int id, int count, int keyId) {
		AwardPB.Builder builder = AwardPB.newBuilder();
		builder.setType(type);
		builder.setId(id);
		builder.setCount(count);
		if (keyId != 0) {
			builder.setKeyId(keyId);
		}

		return builder.build();
	}

	static public CommonPb.Section createSectionPb(int sectionId, int box) {
		CommonPb.Section.Builder builder = CommonPb.Section.newBuilder();
		builder.setSectionId(sectionId);
		builder.setBox(box);
		return builder.build();
	}

	static public CommonPb.Skill createSkillPb(int skillId, int lv) {
		CommonPb.Skill.Builder builder = CommonPb.Skill.newBuilder();
		builder.setId(skillId);
		builder.setLv(lv);
		return builder.build();
	}

	static public CommonPb.Mill createMillPb(Mill mill) {
		CommonPb.Mill.Builder builder = CommonPb.Mill.newBuilder();
		builder.setPos(mill.getPos());
		builder.setId(mill.getId());
		builder.setLv(mill.getLv());
		return builder.build();
	}

	static public CommonPb.Effect createEffectPb(Effect effect) {
		CommonPb.Effect.Builder builder = CommonPb.Effect.newBuilder();
		builder.setId(effect.getEffectId());
		builder.setEndTime(effect.getEndTime());
		return builder.build();
	}

	static public CommonPb.Man createManPb(Man man) {
		CommonPb.Man.Builder builder = CommonPb.Man.newBuilder();
		builder.setLordId(man.getLordId());
		int icon = man.getIcon();
		int sex = man.getSex();
		String nick = man.getNick();
		int level = man.getLevel();
		long fight = man.getFight();
		int ranks = man.getRanks();
		int exp = man.getExp();
		int pos = man.getPos();
		int vip = man.getVip();
		int honour = man.getHonour();
		int pros = man.getPros();
		int prosMax = man.getProsMax();
		String partyName = man.getPartyName();
		if (icon != 0) {
			builder.setIcon(icon);
		}
		if (sex != 0) {
			builder.setSex(sex);
		}
		if (nick != null) {
			builder.setNick(nick);
		}
		if (level != 0) {
			builder.setLevel(level);
		}
		if (fight != 0) {
			builder.setFight(fight);
		}
		if (ranks != 0) {
			builder.setRanks(ranks);
		}
		if (exp != 0) {
			builder.setExp(exp);
		}
		if (pos != 0) {
			builder.setPos(pos);
		}
		if (vip != -1) {
			builder.setVip(vip);
		}
		if (honour != -1) {
			builder.setHonour(honour);
		}
		if (pros != -1) {
			builder.setPros(pros);
		}
		if (prosMax != 0) {
			builder.setProsMax(prosMax);
		}
		if (partyName != null) {
			builder.setPartyName(partyName);
		}
		return builder.build();
	}

	static public CommonPb.Friend createFriendPb(Man man, Friend friend) {
		CommonPb.Friend.Builder builder = CommonPb.Friend.newBuilder();
		builder.setMan(createManPb(man));
		builder.setBless(friend.getBless());
		builder.setBlessTime(friend.getBlessTime());
		return builder.build();
	}

	static public CommonPb.DbFriend createDbFriendPb(Friend friend) {
		CommonPb.DbFriend.Builder builder = CommonPb.DbFriend.newBuilder();
		builder.setLordId(friend.getLordId());
		builder.setBless(friend.getBless());
		builder.setBlessTime(friend.getBlessTime());
		return builder.build();
	}

	static public CommonPb.Bless createBlessPb(Man man, Bless bless) {
		CommonPb.Bless.Builder builder = CommonPb.Bless.newBuilder();
		builder.setMan(createManPb(man));
		builder.setBlessTime(bless.getBlessTime());
		builder.setState(bless.getState());
		return builder.build();
	}

	static public CommonPb.DbBless createDbBlessPb(Bless bless) {
		CommonPb.DbBless.Builder builder = CommonPb.DbBless.newBuilder();
		builder.setLordId(bless.getLordId());
		builder.setState(bless.getState());
		builder.setBlessTime(bless.getBlessTime());
		return builder.build();
	}

	static public CommonPb.Mine createMinePb(Mine mine) {
		CommonPb.Mine.Builder builder = CommonPb.Mine.newBuilder();
		builder.setMineId(mine.getMineId());
		builder.setMineLv(mine.getMineLv());
		return builder.build();
	}

	static public CommonPb.Store createStorePb(Store store) {
		CommonPb.Store.Builder builder = CommonPb.Store.newBuilder();
		builder.setPos(store.getPos());
		builder.setFriend(store.getFriend());
		builder.setEnemy(store.getEnemy());
		builder.setIsMine(store.getIsMine());
		if (store.getMan() != null) {
			builder.setMan(createManPb(store.getMan()));
		}
		if (store.getMine() != null) {
			builder.setMine(createMinePb(store.getMine()));
		}
		if (store.getMark() != null) {
			builder.setMark(store.getMark());
		} else {
			builder.setMark("");
		}
		builder.setType(store.getType());
		return builder.build();
	}

	static public CommonPb.Weal createWealPb(Weal weal) {
		CommonPb.Weal.Builder builder = CommonPb.Weal.newBuilder();
		builder.setStone(weal.getStone());
		builder.setIron(weal.getIron());
		builder.setSilicon(weal.getSilicon());
		builder.setCopper(weal.getCopper());
		builder.setOil(weal.getOil());
		builder.setGold(weal.getGold());
		return builder.build();
	}

	// static public List<AwardPB> createAwardPb(List<Award> award) {
	// List<AwardPB> rs = new ArrayList<AwardPB>();
	// for (Award e : award) {
	// rs.add(createAwardPb(e.getType(), e.getId(), e.getCount(),
	// e.getKeyId()));
	// }
	// return rs;
	// }

	static public CommonPb.Mail createMailPb(Mail mail) {
		CommonPb.Mail.Builder builder = CommonPb.Mail.newBuilder();
		builder.setKeyId(mail.getKeyId());
		builder.setType(mail.getType());
		builder.setState(mail.getState());
		builder.setTime(mail.getTime());
		builder.setMoldId(mail.getMoldId());

		if (mail.getTitle() != null)
			builder.setTitle(mail.getTitle());

		if (mail.getSendName() != null)
			builder.setSendName(mail.getSendName());

		if (mail.getContont() != null)
			builder.setContont(mail.getContont());

		if (mail.getToName() != null)
			builder.addAllToName(mail.getToName());

		if (mail.getAward() != null)
			builder.addAllAward(mail.getAward());

		if (mail.getReport() != null)
			builder.setReport(mail.getReport());

		if (mail.getParam() != null) {
			for (String e : mail.getParam())
				if (e != null) {
					builder.addParam(e);
				}
		}

		return builder.build();
	}

	static public CommonPb.MailShow createMailShowPb(Mail mail) {
		CommonPb.MailShow.Builder builder = CommonPb.MailShow.newBuilder();
		builder.setKeyId(mail.getKeyId());
		builder.setType(mail.getType());
		builder.setState(mail.getState());
		builder.setTime(mail.getTime());
		builder.setMoldId(mail.getMoldId());
		if (mail.getTitle() != null)
			builder.setTitle(mail.getTitle());

		if (mail.getSendName() != null)
			builder.setSendName(mail.getSendName());

		if (mail.getParam() != null) {
			for (String e : mail.getParam()) {
				if (e != null) {
					builder.addParam(e);
				}
			}
		}
		return builder.build();
	}

	static public CommonPb.RankPlayer createRankPlayer(Arena arena, Player player) {
		CommonPb.RankPlayer.Builder builder = CommonPb.RankPlayer.newBuilder();
		builder.setRank(arena.getRank());
		builder.setName(player.lord.getNick());
		builder.setLv(player.lord.getLevel());
		builder.setFight(arena.getFight());
		return builder.build();
	}

	static public CommonPb.LotteryEquip createLotteryEquipPb(LotteryEquip lotteryEquip) {
		CommonPb.LotteryEquip.Builder builder = CommonPb.LotteryEquip.newBuilder();
		builder.setLotteryId(lotteryEquip.getLotteryId());
		builder.setFreetimes(lotteryEquip.getFreetimes());
		builder.setCd(lotteryEquip.getCd());
		if (lotteryEquip.getLotteryId() == 3) {
			if (lotteryEquip.getPurple() == 0) {
				builder.setPurple(1);
				builder.setIsFirst(0);
			} else {
				builder.setPurple(10 - lotteryEquip.getPurple() % 10);
				builder.setIsFirst(1);
			}
		}
		builder.setTime(lotteryEquip.getTime());
		// builder.setLotteryTime(lotterEquip.getLotteryTime());
		return builder.build();
	}

	static public CommonPb.LotteryEquip createDbLotteryEquipPb(LotteryEquip lotteryEquip) {
		CommonPb.LotteryEquip.Builder builder = CommonPb.LotteryEquip.newBuilder();
		builder.setLotteryId(lotteryEquip.getLotteryId());
		builder.setFreetimes(lotteryEquip.getFreetimes());
		builder.setCd(lotteryEquip.getCd());
		builder.setPurple(lotteryEquip.getPurple());
		builder.setTime(lotteryEquip.getTime());
		return builder.build();
	}

	static public CommonPb.PartyRank createPartyRankPb(PartyRank partyRank, PartyData partyData, int number) {
		CommonPb.PartyRank.Builder builder = CommonPb.PartyRank.newBuilder();
		builder.setRank(partyRank.getRank());
		builder.setPartyId(partyRank.getPartyId());
		builder.setPartyName(partyData.getPartyName());
		builder.setPartyLv(partyData.getPartyLv());
		builder.setMember(number);
		builder.setFight(partyRank.getFight());
		builder.setApplyLv(partyData.getPartyLv());
		builder.setApplyFight(partyData.getApplyFight());
		return builder.build();
	}

	static public CommonPb.PartyLvRank createPartyLvRankPb(PartyLvRank partyLvRank) {
		CommonPb.PartyLvRank.Builder builder = CommonPb.PartyLvRank.newBuilder();
		builder.setRank(partyLvRank.getRank());
		builder.setPartyId(partyLvRank.getPartyId());
		builder.setPartyName(partyLvRank.getPartyName());
		builder.setPartyLv(partyLvRank.getPartyLv());
		builder.setScienceLv(partyLvRank.getScienceLv());
		builder.setWealLv(partyLvRank.getWealLv());
		builder.setBuild(partyLvRank.getBuild());
		return builder.build();
	}

	static public CommonPb.Party createPartyPb(PartyData partyData, int member, int rank) {
		CommonPb.Party.Builder builder = CommonPb.Party.newBuilder();
		builder.setPartyId(partyData.getPartyId());
		if (partyData.getPartyName() == null) {
			builder.setPartyName("");
		} else {
			builder.setPartyName(partyData.getPartyName());
		}
		if (partyData.getLegatusName() == null) {
			builder.setLegatusName("");
		} else {
			builder.setLegatusName(partyData.getLegatusName());
		}
		builder.setPartyLv(partyData.getPartyLv());
		builder.setMember(member);
		builder.setFight(partyData.getFight());
		if (partyData.getSlogan() == null) {
			builder.setSlogan("");
		} else {
			builder.setSlogan(partyData.getSlogan());
		}
		if (partyData.getInnerSlogan() == null) {
			builder.setInnerSlogan("");
		} else {
			builder.setInnerSlogan(partyData.getInnerSlogan());
		}
		builder.setApplyType(partyData.getApply());
		builder.setApplyLv(partyData.getApplyLv());
		builder.setApplyFight(partyData.getApplyFight());
		if (partyData.getJobName1() == null) {
			builder.setJobName1("");
		} else {
			builder.setJobName1(partyData.getJobName1());
		}
		if (partyData.getJobName1() == null) {
			builder.setJobName1("");
		} else {
			builder.setJobName1(partyData.getJobName1());
		}
		if (partyData.getJobName2() == null) {
			builder.setJobName2("");
		} else {
			builder.setJobName2(partyData.getJobName2());
		}
		if (partyData.getJobName3() == null) {
			builder.setJobName3("");
		} else {
			builder.setJobName3(partyData.getJobName3());
		}
		if (partyData.getJobName4() == null) {
			builder.setJobName4("");
		} else {
			builder.setJobName4(partyData.getJobName4());
		}
		builder.setBuild(partyData.getBuild());
		builder.setRank(rank);
		builder.setScienceLv(partyData.getScienceLv());
		builder.setWealLv(partyData.getWealLv());
		return builder.build();
	}

	// static public CommonPb.PartyBuilding createPartyBuildingPb(int
	// buildingId, int buildingLv) {
	// CommonPb.PartyBuilding.Builder builder =
	// CommonPb.PartyBuilding.newBuilder();
	// builder.setBuildId(buildingId);
	// builder.setBuildLv(buildingLv);
	// return builder.build();
	// }

	static public CommonPb.PartyMember createPartyMemberPb(Member member, Lord lord, int online) {
		CommonPb.PartyMember.Builder builder = CommonPb.PartyMember.newBuilder();
		builder.setLordId(member.getLordId());
		builder.setNick(lord.getNick());
		builder.setJob(member.getJob());
		builder.setSex(lord.getSex());
		builder.setIcon(lord.getPortrait());
		builder.setLevel(lord.getLevel());
		builder.setFight(lord.getFight());
		builder.setDonate(member.getDonate());
		builder.setWeekDonate(member.getWeekDonate());
		builder.setWeekAllDonate(member.getWeekAllDonate());
		builder.setIsOnline(online);
		return builder.build();
	}

	static public CommonPb.LiveTask createLiveTaskPb(LiveTask liveTask) {
		CommonPb.LiveTask.Builder builder = CommonPb.LiveTask.newBuilder();
		builder.setTaskId(liveTask.getTaskId());
		builder.setCount(liveTask.getCount());
		return builder.build();
	}

	static public CommonPb.PartyDonate createPartyDonatePb(PartyDonate partyDonate) {
		CommonPb.PartyDonate.Builder builder = CommonPb.PartyDonate.newBuilder();
		builder.setStone(partyDonate.getStone());
		builder.setIron(partyDonate.getIron());
		builder.setSilicon(partyDonate.getSilicon());
		builder.setCopper(partyDonate.getCopper());
		builder.setOil(partyDonate.getOil());
		builder.setGold(partyDonate.getGold());
		return builder.build();
	}

	static public CommonPb.PartyProp createPartyPropPb(PartyProp partyProp) {
		CommonPb.PartyProp.Builder builder = CommonPb.PartyProp.newBuilder();
		builder.setKeyId(partyProp.getKeyId());
		builder.setCount(partyProp.getCount());
		return builder.build();
	}

	static public CommonPb.PartyProp createPartyPropPb(int keyId, int count) {
		CommonPb.PartyProp.Builder builder = CommonPb.PartyProp.newBuilder();
		builder.setKeyId(keyId);
		builder.setCount(count);
		return builder.build();
	}

	static public CommonPb.PartyApply createPartyApplyPb(Lord lord, PartyApply partyApply) {
		CommonPb.PartyApply.Builder builder = CommonPb.PartyApply.newBuilder();
		builder.setLordId(partyApply.getLordId());
		builder.setNick(lord.getNick());
		builder.setLevel(lord.getLevel());
		builder.setFight(lord.getFight());
		builder.setIcon(lord.getPortrait());
		builder.setApplyDate(partyApply.getApplyDate());
		return builder.build();
	}

	static public CommonPb.DbPartyApply createDbPartyApplyPb(PartyApply partyApply) {
		CommonPb.DbPartyApply.Builder builder = CommonPb.DbPartyApply.newBuilder();
		builder.setLordId(partyApply.getLordId());
		builder.setApplyDate(partyApply.getApplyDate());
		return builder.build();
	}

	static public CommonPb.MapData createMapDataPb(Player player, String party) {
		CommonPb.MapData.Builder builder = CommonPb.MapData.newBuilder();
		Lord lord = player.lord;
		builder.setPos(lord.getPos());
		builder.setName(lord.getNick());
		builder.setPros(lord.getPros());
		builder.setProsMax(lord.getProsMax());
		// builder.setRanks(lord.getRanks());
		// builder.setFight(lord.getFight());
		// builder.setPortrait(lord.getPortrait());
		// builder.setSex(lord.getSex());
		builder.setLv(lord.getLevel());
		if (party != null) {
			builder.setParty(party);
		}

		if (player.effects.containsKey(EffectType.ATTACK_FREE)) {
			builder.setFree(true);
		}

		if (player.surface != 0) {
			builder.setSurface(player.surface);
		}
		builder.setRuins(PbHelper.createRuinsPb(player.ruins));

		return builder.build();
	}

	static public CommonPb.PartyMine createPartyMinePb(String name, int pos) {
		CommonPb.PartyMine.Builder builder = CommonPb.PartyMine.newBuilder();
		builder.setPos(pos);
		builder.setName(name);
		return builder.build();
	}

	static public CommonPb.PartyLive createPartyLivePb(Lord lord, int job, int live) {
		CommonPb.PartyLive.Builder builder = CommonPb.PartyLive.newBuilder();
		builder.setLordId(lord.getLordId());
		builder.setIcon(lord.getPortrait());
		builder.setSex(lord.getSex());
		builder.setNick(lord.getNick());
		builder.setJob(job);
		builder.setLevel(lord.getLevel());
		builder.setLive(live);
		return builder.build();
	}

	// static public CommonPb.ScoutData createScoutDataPb(ReportScout report) {
	//
	// CommonPb.ScoutData.Builder builder = CommonPb.ScoutData.newBuilder();
	// builder.setKeyId(report.getKeyId());
	// builder.setTime(report.getTime());
	// builder.setPos(report.getPos());
	// builder.setLv(report.getLv());
	// if (report.getForm() != null) {
	// builder.setForm(createFormPb(report.getForm()));
	// }
	// builder.setPortrait(report.getPortrait());
	// builder.setSex(report.getSex());
	// builder.setName(report.getName());
	//
	// if (report.getParty() != null) {
	// builder.setParty(report.getParty());
	// }
	//
	// if (report.getFriend() != null) {
	// builder.setFriend(report.getFriend());
	// }
	//
	// builder.setPros(report.getPros());
	// builder.setProsMax(report.getProsMax());
	// builder.setStone(report.getStone());
	// builder.setIron(report.getIron());
	// builder.setSilicon(report.getSilicon());
	// builder.setCopper(report.getCopper());
	// builder.setOil(report.getOil());
	// builder.setHarvest(report.getHarvest());
	// return builder.build();
	// }

	static public CommonPb.TaskDayiy createTaskDayiyPb(Lord lord) {
		CommonPb.TaskDayiy.Builder builder = CommonPb.TaskDayiy.newBuilder();
		builder.setDayiy(lord.getTaskDayiy());
		builder.setDayiyCount(lord.getDayiyCount());
		return builder.build();
	}

	static public CommonPb.TaskLive createTaskLivePb(Lord lord) {
		CommonPb.TaskLive.Builder builder = CommonPb.TaskLive.newBuilder();
		builder.setLive(lord.getTaskLive());
		builder.setLiveAward(lord.getTaskLiveAd());
		return builder.build();
	}

	static public CommonPb.Task createTaskPb(Task task) {
		CommonPb.Task.Builder builder = CommonPb.Task.newBuilder();
		builder.setTaskId(task.getTaskId());
		builder.setSchedule(task.getSchedule());
		builder.setAccept(task.getAccept());
		builder.setStatus(task.getStatus());
		return builder.build();
	}

	static public CommonPb.Ruins createRuinsPb(Ruins r) {
		CommonPb.Ruins.Builder builder = CommonPb.Ruins.newBuilder();
		builder.setIsRuins(r.isRuins());
		builder.setLordId(r.getLordId());
		builder.setAttackerName(r.getAttackerName());
		return builder.build();
	}

	static public CommonPb.TrendParam createTrendParamPb(TrendParam trendParam) {
		CommonPb.TrendParam.Builder builder = CommonPb.TrendParam.newBuilder();
		if (trendParam.getContent() != null) {
			builder.setContent(trendParam.getContent());
		}

		if (trendParam.getMan() != null) {
			builder.setMan(PbHelper.createManPb(trendParam.getMan()));
		}
		return builder.build();
	}

	static public CommonPb.Trend createTrendPb(Trend trend, List<TrendParam> manList) {
		CommonPb.Trend.Builder builder = CommonPb.Trend.newBuilder();
		builder.setTrendId(trend.getTrendId());
		for (int i = 0; i < manList.size(); i++) {
			TrendParam trendParam = manList.get(i);
			builder.addTrendParam(createTrendParamPb(trendParam));
		}
		builder.setTrendTime(trend.getTrendTime());
		return builder.build();
	}

	static public CommonPb.DbTrend createDbTrendPb(Trend trend) {
		CommonPb.DbTrend.Builder builder = CommonPb.DbTrend.newBuilder();
		builder.setTrendId(trend.getTrendId());
		String[] param = trend.getParam();
		if (param != null) {
			for (String v : param) {
				builder.addParam(v);
			}
		}
		builder.setTrendTime(trend.getTrendTime());
		return builder.build();
	}

	static public CommonPb.PartyCombat createPartyCombatPb(PartyCombat partyCombat) {
		CommonPb.PartyCombat.Builder builder = CommonPb.PartyCombat.newBuilder();
		builder.setCombatId(partyCombat.getCombatId());
		builder.setSchedule(partyCombat.getSchedule());
		if (partyCombat.getForm() != null) {
			builder.setForm(createFormPb(partyCombat.getForm()));
		}
		return builder.build();
	}

	static public CommonPb.PartyCombat createPartyCombatInfoPb(PartyCombat partyCombat) {
		CommonPb.PartyCombat.Builder builder = CommonPb.PartyCombat.newBuilder();
		builder.setCombatId(partyCombat.getCombatId());
		builder.setSchedule(partyCombat.getSchedule());
		return builder.build();
	}

	static public CommonPb.PartySection createPartySectionPb(PartySection partySection) {
		CommonPb.PartySection.Builder builder = CommonPb.PartySection.newBuilder();
		builder.setSectionId(partySection.getSectionId());
		builder.setCombatLive(partySection.getCombatLive());
		builder.setStatus(partySection.getStatus());
		return builder.build();
	}

	static public CommonPb.Invasion createInvasionPb(March march) {
		CommonPb.Invasion.Builder builder = CommonPb.Invasion.newBuilder();
		Army army = march.getArmy();
		Lord lord = march.getPlayer().lord;
		builder.setLordId(lord.getLordId());
		builder.setKeyId(army.getKeyId());

		builder.setPortrait(lord.getPortrait());
		builder.setState(army.getState());
		builder.setName(lord.getNick());
		builder.setLv(lord.getLevel());
		builder.setEndTime(army.getEndTime());
		// builder.setTarget(army.getTarget());
		return builder.build();
	}

	static public CommonPb.Aid createAidPb(Guard guard) {
		CommonPb.Aid.Builder builder = CommonPb.Aid.newBuilder();
		Army army = guard.getArmy();
		Lord lord = guard.getPlayer().lord;
		builder.setLordId(lord.getLordId());
		builder.setKeyId(army.getKeyId());
		builder.setPortrait(lord.getPortrait());
		builder.setName(lord.getNick());
		builder.setLv(lord.getLevel());
		builder.setState(army.getState());
		builder.setForm(createFormPb(army.getForm()));
		return builder.build();
	}

	static public CommonPb.ArmyStatu createArmyStatuPb(ArmyStatu armyStatu) {
		CommonPb.ArmyStatu.Builder builder = CommonPb.ArmyStatu.newBuilder();
		builder.setLordId(armyStatu.getLordId());
		builder.setKeyId(armyStatu.getKeyId());
		builder.setState(armyStatu.getState());
		return builder.build();
	}

	static public CommonPb.Extreme createExtreme(String name, int lv, int time) {
		CommonPb.Extreme.Builder builder = CommonPb.Extreme.newBuilder();
		builder.setName(name);
		builder.setLv(lv);
		builder.setTime(time);
		return builder.build();
	}

	static public CommonPb.AtkExtreme createAtkExtreme(Extreme extreme, Record record) {
		CommonPb.AtkExtreme.Builder builder = CommonPb.AtkExtreme.newBuilder();
		builder.setRecord(record);
		builder.setAttacker(extreme);
		return builder.build();
	}

	static public CommonPb.RankData createRankData(String name, int lv, long v) {
		CommonPb.RankData.Builder builder = CommonPb.RankData.newBuilder();
		builder.setName(name);
		builder.setLv(lv);
		builder.setValue(v);
		return builder.build();
	}

	static public CommonPb.RankData createRankData(String name, int lv) {
		CommonPb.RankData.Builder builder = CommonPb.RankData.newBuilder();
		builder.setName(name);
		builder.setLv(lv);
		return builder.build();
	}

	static public CommonPb.Activity createActivityPb(ActivityBase activityBase, boolean open, int tips) {
		CommonPb.Activity.Builder builder = CommonPb.Activity.newBuilder();
		builder.setActivityId(activityBase.getActivityId());
		builder.setName(activityBase.getStaticActivity().getName());
		builder.setBeginTime((int) (activityBase.getBeginTime().getTime() / 1000));
		builder.setEndTime((int) (activityBase.getEndTime().getTime() / 1000));
		if (activityBase.getDisplayTime() != null) {
			builder.setDisplayTime((int) (activityBase.getDisplayTime().getTime() / 1000));
		}
		builder.setOpen(open);
		builder.setTips(tips);
		return builder.build();
	}

	static public CommonPb.ActivityCond createActivityCondPb(ConfActAward actAward, int status) {
		CommonPb.ActivityCond.Builder builder = CommonPb.ActivityCond.newBuilder();
		builder.setKeyId(actAward.getKeyId());
		builder.setCond(actAward.getCond());
		builder.setStatus(status);
		if (actAward.getParam() != null && !actAward.getParam().equals("")) {
			builder.setParam(actAward.getParam());
		}
		List<List<Integer>> awardList = actAward.getAwardList();
		for (List<Integer> e : awardList) {
			if (e.size() != 3) {
				continue;
			}
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			builder.addAward(PbHelper.createAwardPb(type, id, count));
		}
		return builder.build();
	}

	static public CommonPb.CondState createCondStatePb(int state, List<CommonPb.ActivityCond> listActAward) {
		CommonPb.CondState.Builder builder = CommonPb.CondState.newBuilder();
		builder.setState(state);
		builder.addAllActivityCond(listActAward);
		return builder.build();
	}

	static public CommonPb.CondState createCondStatePb(int state, ConfActAward actAward, int status) {
		CommonPb.CondState.Builder builder = CommonPb.CondState.newBuilder();
		builder.setState(state);
		builder.addActivityCond(PbHelper.createActivityCondPb(actAward, status));
		return builder.build();
	}

	static public CommonPb.DbActivity createDbActivityPb(Activity activity) {
		CommonPb.DbActivity.Builder builder = CommonPb.DbActivity.newBuilder();
		builder.setActivityId(activity.getActivityId());
		builder.setBeginTime(activity.getBeginTime());
		builder.setEndTime(activity.getEndTime());
		builder.setOpen(activity.getOpen());
		if (activity.getStatusList() != null) {
			for (Long e : activity.getStatusList())
				builder.addStatus(e);
		}
		Iterator<Entry<Integer, Integer>> it = activity.getStatusMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> next = it.next();
			int keyId = next.getKey();
			int value = next.getValue();
			builder.addTowInt(createTwoIntPb(keyId, value));
		}

		return builder.build();
	}

	static public CommonPb.Mecha createMechaPb(ConfActMecha actMecha, int free, int crit, int part) {
		CommonPb.Mecha.Builder builder = CommonPb.Mecha.newBuilder();
		builder.setMechaId(actMecha.getMechaId());
		builder.setTankId(actMecha.getTank());
		builder.setCost(actMecha.getCost());
		builder.setCount(actMecha.getCount());
		builder.setCrit(crit);
		builder.setPart(part);
		builder.setFree(free);
		return builder.build();
	}

	static public CommonPb.Quota createQuotaPb(ConfActQuota quota, int buy) {
		CommonPb.Quota.Builder builder = CommonPb.Quota.newBuilder();
		builder.setQuotaId(quota.getQuotaId());
		builder.setDisplay(quota.getDisplay());
		builder.setPrice(quota.getPrice());
		builder.setCount(quota.getCount());
		builder.setBuy(buy);
		List<List<Integer>> awardList = quota.getAwardList();
		for (List<Integer> e : awardList) {
			if (e.size() != 3) {
				continue;
			}
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			builder.addAward(PbHelper.createAwardPb(type, id, count));
		}
		return builder.build();
	}

	static public CommonPb.AmyRebate createAmyRebatePb(int rebateId, int count) {
		CommonPb.AmyRebate.Builder builder = CommonPb.AmyRebate.newBuilder();
		builder.setRebateId(rebateId);
		builder.setStatus(count);
		return builder.build();
	}

	static public CommonPb.ActPlayerRank createActPlayerRank(ActPlayerRank actPlayerRank, String nick) {
		CommonPb.ActPlayerRank.Builder builder = CommonPb.ActPlayerRank.newBuilder();
		builder.setLordId(actPlayerRank.getLordId());
		builder.setRankType(actPlayerRank.getRankType());
		builder.setRankValue(actPlayerRank.getRankValue());
		builder.setNick(nick);
		if (actPlayerRank.getParam() != null) {
			builder.setParam(actPlayerRank.getParam());
		}
		return builder.build();
	}

	static public CommonPb.ActPlayerRank createActPlayerRank(ActPlayerRank actPlayerRank) {
		CommonPb.ActPlayerRank.Builder builder = CommonPb.ActPlayerRank.newBuilder();
		builder.setLordId(actPlayerRank.getLordId());
		builder.setRankType(actPlayerRank.getRankType());
		builder.setRankValue(actPlayerRank.getRankValue());
		if (actPlayerRank.getParam() != null) {
			builder.setParam(actPlayerRank.getParam());
		}
		return builder.build();
	}

	static public CommonPb.ActPartyRank createActPartyRank(ActPartyRank actPartyRank) {
		CommonPb.ActPartyRank.Builder builder = CommonPb.ActPartyRank.newBuilder();
		builder.setPartyId(actPartyRank.getPartyId());
		builder.setRankType(actPartyRank.getRankType());
		builder.setRankValue(actPartyRank.getRankValue());
		if (actPartyRank.getParam() != null) {
			builder.setParam(actPartyRank.getParam());
		}
		List<Long> lordIds = actPartyRank.getLordIds();
		if (lordIds != null) {
			for (Long lordId : lordIds) {
				builder.addLordId(lordId);
			}
		}
		return builder.build();
	}

	static public CommonPb.ActPartyRank createPartyRankPb(ActPartyRank actPartyRank, int rank, String partyName, long fight) {
		CommonPb.ActPartyRank.Builder builder = CommonPb.ActPartyRank.newBuilder();
		builder.setPartyId(actPartyRank.getPartyId());
		builder.setPartyName(partyName);
		builder.setFight(fight);
		builder.setRankType(0);
		builder.setRankValue(actPartyRank.getRankValue());
		if (actPartyRank.getParam() != null) {
			builder.setParam(actPartyRank.getParam());
		}
		List<Long> lordIds = actPartyRank.getLordIds();
		if (lordIds != null) {
			for (Long lordId : lordIds) {
				builder.addLordId(lordId);
			}
		}
		return builder.build();
	}

	static public CommonPb.Fortune createFortunePb(ConfActFortune fortune) {
		CommonPb.Fortune.Builder builder = CommonPb.Fortune.newBuilder();
		builder.setFortuneId(fortune.getFortuneId());
		builder.setCost(fortune.getPrice());
		builder.setCount(fortune.getCount());
		builder.setScore(fortune.getPoint());
		return builder.build();
	}

	static public CommonPb.RankAward createRankAwardPb(ConfActRank rank) {
		CommonPb.RankAward.Builder builder = CommonPb.RankAward.newBuilder();
		builder.setRank(rank.getRankBegin());
		builder.setRankEd(rank.getRankEnd());
		builder.setRankType(rank.getSortId());
		List<List<Integer>> awardList = rank.getAwardList();
		for (List<Integer> e : awardList) {
			if (e.size() != 3) {
				continue;
			}
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			builder.addAward(PbHelper.createAwardPb(type, id, count));
		}
		return builder.build();
	}

	static public CommonPb.BeeRank createBeeRankPb(int resourceId, long state, int status, List<CommonPb.ActPlayerRank> playerRanks) {
		CommonPb.BeeRank.Builder builder = CommonPb.BeeRank.newBuilder();
		builder.setResourceId(resourceId);
		builder.setState(state);// 我的资源采集量
		if (playerRanks != null) {
			for (CommonPb.ActPlayerRank e : playerRanks) {
				builder.addActPlayerRank(e);
			}
		}
		builder.setStatus(status);
		return builder.build();
	}

	static public CommonPb.MemberReg createWarRegPb(WarMember warMember) {
		CommonPb.MemberReg.Builder builder = CommonPb.MemberReg.newBuilder();
		builder.setName(warMember.getPlayer().lord.getNick());
		builder.setTime(warMember.getMember().getRegTime());
		builder.setLv(warMember.getMember().getRegLv());
		builder.setFight(warMember.getMember().getRegFight());

		return builder.build();
	}

	static public CommonPb.PartyReg createPartyRegPb(String name, int lv, int count, long fight) {
		CommonPb.PartyReg.Builder builder = CommonPb.PartyReg.newBuilder();
		builder.setLv(lv);
		builder.setName(name);
		builder.setCount(count);
		builder.setFight(fight);

		return builder.build();
	}

	static public CommonPb.WarRecord createWarRecordPb(WarMember attacker, WarMember defencer, int hp1, int hp2, int result, int time) {
		CommonPb.WarRecord.Builder builder = CommonPb.WarRecord.newBuilder();
		builder.setPartyName1(attacker.getWarParty().getPartyData().getPartyName());
		builder.setName1(attacker.getPlayer().lord.getNick());
		builder.setHp1(hp1);
		builder.setPartyName2(defencer.getWarParty().getPartyData().getPartyName());
		builder.setName2(defencer.getPlayer().lord.getNick());
		builder.setHp2(hp2);
		builder.setTime(time);
		builder.setResult(result);

		return builder.build();
	}

	static public CommonPb.WarRecordPerson createPersonWarRecordPb(WarRecord record, RptAtkWar rpt) {
		CommonPb.WarRecordPerson.Builder builder = CommonPb.WarRecordPerson.newBuilder();
		builder.setRecord(record);
		builder.setRpt(rpt);
		return builder.build();
	}

	static public CommonPb.WarRecord createWarResultPb(String partyName, WarMember warMember, int rank, int time) {
		CommonPb.WarRecord.Builder builder = CommonPb.WarRecord.newBuilder();
		builder.setPartyName1(partyName);
		builder.setName1(warMember.getPlayer().lord.getNick());
		builder.setTime(time);
		builder.setRank(rank);

		return builder.build();
	}

	static public CommonPb.WarRecord createWarWinPb(String partyName, int rank, int time) {
		CommonPb.WarRecord.Builder builder = CommonPb.WarRecord.newBuilder();
		builder.setPartyName1(partyName);
		builder.setTime(time);
		builder.setRank(rank);

		return builder.build();
	}

	static public CommonPb.Profoto createProfotoPb(int propId, int count) {
		CommonPb.Profoto.Builder builder = CommonPb.Profoto.newBuilder();
		builder.setPropId(propId);
		builder.setCount(count);
		return builder.build();
	}

	static public CommonPb.WarRank createWarRankPb(WarParty warParty) {
		CommonPb.WarRank.Builder builder = CommonPb.WarRank.newBuilder();
		builder.setPartyName(warParty.getPartyData().getPartyName());
		builder.setRank(warParty.getPartyData().getWarRank());
		builder.setCount(warParty.getMembers().size());
		builder.setFight(warParty.getPartyData().getRegFight());
		return builder.build();
	}

	static public CommonPb.HurtRank createHurtRankPb(BossFight bossFight, String nick, int rank) {
		CommonPb.HurtRank.Builder builder = CommonPb.HurtRank.newBuilder();
		builder.setName(nick);
		builder.setRank(rank);
		builder.setHurt(bossFight.getHurt());
		return builder.build();
	}

	static public CommonPb.WarWinRank createWarWinRankPb(WarMember warMember, int rank) {
		CommonPb.WarWinRank.Builder builder = CommonPb.WarWinRank.newBuilder();
		builder.setName(warMember.getPlayer().lord.getNick());
		builder.setRank(rank);
		builder.setWinCount(warMember.getMember().getWinCount());
		builder.setFight(warMember.getMember().getRegFight());
		return builder.build();
	}

	static public CommonPb.Tech createTechPb(ConfActTech staticActTech) {
		CommonPb.Tech.Builder builder = CommonPb.Tech.newBuilder();
		builder.setTechId(staticActTech.getTechId());
		builder.setType(staticActTech.getType());
		builder.setUsePropId(staticActTech.getPropId());
		builder.setUsePropcount(staticActTech.getCount());
		if (staticActTech.getType() == 1) {
			int propId = staticActTech.getAwardList().get(0).get(1);
			int propCount = staticActTech.getAwardList().get(0).get(2);
			builder.setPropId(propId);
			builder.setCount(propCount);
		} else {
			builder.setPropId(0);
			builder.setCount(1);
		}
		return builder.build();
	}

	static public CommonPb.General createGeneralPb(ConfActGeneral staticActGeneral) {
		CommonPb.General.Builder builder = CommonPb.General.newBuilder();
		builder.setGeneranlId(staticActGeneral.getGeneralId());
		builder.setHeroId(staticActGeneral.getHeroId());
		builder.setPrice(staticActGeneral.getPrice());
		builder.setCount(staticActGeneral.getCount());
		builder.setPoint(staticActGeneral.getPoint());
		return builder.build();
	}

	static public CommonPb.QuotaVip createQuotaVipPb(ConfActQuota staticActQuota, int buy) {
		CommonPb.QuotaVip.Builder builder = CommonPb.QuotaVip.newBuilder();
		builder.setVip(staticActQuota.getCond());
		builder.setQuota(PbHelper.createQuotaPb(staticActQuota, buy));
		return builder.build();
	}

	static public CommonPb.SeniorMapData createSeniorMapDataPb(Player my, Player player, Army army, boolean party) {
		CommonPb.SeniorMapData.Builder builder = CommonPb.SeniorMapData.newBuilder();
		Lord lord = player.lord;
		builder.setPos(army.getTarget());
		builder.setName(lord.getNick());
		builder.setParty(party);

		if (army.getOccupy()) {
			builder.setFreeTime(army.getEndTime() - army.getPeriod() + 1800);
		}

		if (my.roleId != player.roleId) {
			builder.setMy(false);
		} else {
			builder.setMy(true);
		}

		return builder.build();
	}

	static public CommonPb.ScoreRank createScoreRankPb(String name, SeniorScoreRank scoreRank) {
		CommonPb.ScoreRank.Builder builder = CommonPb.ScoreRank.newBuilder();
		builder.setName(name);
		builder.setFight(scoreRank.getFight());
		builder.setScore(scoreRank.getScore());
		return builder.build();
	}

	static public CommonPb.ScoreRank createScoreRankPb(String name, SeniorPartyScoreRank scoreRank) {
		CommonPb.ScoreRank.Builder builder = CommonPb.ScoreRank.newBuilder();
		builder.setName(name);
		builder.setFight(scoreRank.getFight());
		builder.setScore(scoreRank.getScore());
		return builder.build();
	}

	static public CommonPb.Village createVillagePb(ConfActVacationland land) {
		CommonPb.Village.Builder builder = CommonPb.Village.newBuilder();
		builder.setVillageId(land.getVillageId());
		builder.setTopup(land.getTopup());
		builder.setPrice(land.getPrice());
		return builder.build();
	}

	static public CommonPb.VillageAward createVillageAwardPb(ConfActVacationland land, int buyId, int state, int status) {
		CommonPb.VillageAward.Builder builder = CommonPb.VillageAward.newBuilder();
		builder.setLandId(land.getLandId());
		builder.setVillageId(land.getVillageId());
		builder.setOnday(land.getOnday());
		if (land.getVillageId() != buyId) {
			builder.setState(0);
			builder.setStatus(0);
		} else {
			builder.setState(state);
			builder.setStatus(status);
		}
		List<List<Integer>> awardList = land.getAwardList();
		if (awardList != null) {
			for (List<Integer> e : awardList) {
				if (e.size() < 3) {
					continue;
				}
				int type = e.get(0);
				int id = e.get(1);
				int count = e.get(2);
				builder.addAward(PbHelper.createAwardPb(type, id, count));
			}
		}
		return builder.build();
	}

	static public CommonPb.Atom createAtomPb(int grid, int type, int id, int count) {
		CommonPb.Atom.Builder builder = CommonPb.Atom.newBuilder();
		builder.setGrid(grid);
		builder.setType(type);
		builder.setId(id);
		builder.setCount(count);
		return builder.build();
	}

	static public CommonPb.Cash createCashPb(Cash cash) {
		CommonPb.Cash.Builder builder = CommonPb.Cash.newBuilder();
		builder.setCashId(cash.getCashId());
		builder.setFormulaId(cash.getFormulaId());
		builder.setPrice(cash.getPrice());
		builder.setFree(cash.getFree());
		builder.setState(cash.getState());
		builder.setRefreshDate(cash.getRefreshDate());

		List<List<Integer>> list = cash.getList();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> e = list.get(i);
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			builder.addAtom(PbHelper.createAtomPb(i + 1, type, id, count));
		}
		int type = cash.getAwardList().get(0);
		int id = cash.getAwardList().get(1);
		int count = cash.getAwardList().get(2);
		builder.setAward(PbHelper.createAwardPb(type, id, count));
		return builder.build();
	}

	static public CommonPb.MilitaryScience createMilitaryScienecePb(MilitaryScience militaryScienece) {
		CommonPb.MilitaryScience.Builder builder = CommonPb.MilitaryScience.newBuilder();
		builder.setMilitaryScienceId(militaryScienece.getMilitaryScienceId());
		builder.setLevel(militaryScienece.getLevel());
		builder.setFitTankId(militaryScienece.getFitTankId());
		builder.setFitPos(militaryScienece.getFitPos());
		return builder.build();
	}

	static public CommonPb.MilitaryScienceGrid createMilitaryScieneceGridPb(MilitaryScienceGrid militaryScieneceGrid) {
		CommonPb.MilitaryScienceGrid.Builder builder = CommonPb.MilitaryScienceGrid.newBuilder();
		builder.setTankId(militaryScieneceGrid.getTankId());
		builder.setPos(militaryScieneceGrid.getPos());
		builder.setStatus(militaryScieneceGrid.getStatus());
		builder.setMilitaryScienceId(militaryScieneceGrid.getMilitaryScienceId());
		return builder.build();
	}

	static public CommonPb.MilitaryMaterial createMilitaryMaterialPb(MilitaryMaterial militaryMaterial) {
		CommonPb.MilitaryMaterial.Builder builder = CommonPb.MilitaryMaterial.newBuilder();
		builder.setId(militaryMaterial.getId());
		builder.setCount((int) militaryMaterial.getCount());
		return builder.build();
	}

	static public CommonPb.PartResolve createPartResolvePb(ConfActPartResolve staticActPartResolve) {
		CommonPb.PartResolve.Builder builder = CommonPb.PartResolve.newBuilder();
		builder.setResolveId(staticActPartResolve.getResolveId());
		builder.setCount(staticActPartResolve.getSlug());
		List<List<Integer>> awardList = staticActPartResolve.getAwardList();
		for (List<Integer> e : awardList) {
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			builder.setAward(PbHelper.createAwardPb(type, id, count));
		}
		return builder.build();
	}

	static public CommonPb.TopupGamble createTopupGamblePb(ConfActGamble staticActGamble) {
		CommonPb.TopupGamble.Builder builder = CommonPb.TopupGamble.newBuilder();
		builder.setGambleId(staticActGamble.getGambleId());
		builder.setPrice(staticActGamble.getPrice());
		builder.setTopup(staticActGamble.getTopup());
		List<List<Integer>> awardList = staticActGamble.getAwardList();
		if (awardList != null) {
			for (List<Integer> e : awardList) {
				int type = e.get(0);
				int id = e.get(1);
				int count = e.get(2);
				builder.addAward(PbHelper.createAwardPb(type, id, count));
			}
		}
		return builder.build();
	}

	static public CommonPb.TwoValue createTwoValuePb(int key, long value) {
		CommonPb.TwoValue.Builder builder = CommonPb.TwoValue.newBuilder();
		builder.setV1(key);
		builder.setV2(value);
		return builder.build();
	}

}
