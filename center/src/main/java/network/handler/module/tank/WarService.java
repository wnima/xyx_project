package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.game.domain.PartyData;
import com.game.domain.Player;
import com.game.fight.FightLogic;
import com.game.fight.domain.Fighter;
import com.game.fight.domain.Force;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.game.warFight.domain.WarMember;
import com.game.warFight.domain.WarParty;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfHero;
import config.provider.ConfHeroProvider;
import config.provider.ConfTankProvider;
import config.provider.ConfWarAwardProvider;
import data.bean.Army;
import data.bean.Form;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Prop;
import data.bean.WarLog;
import define.ArmyState;
import define.AwardFrom;
import define.AwardType;
import define.EffectType;
import define.FirstActType;
import define.MailType;
import define.PartyType;
import define.WarState;
import domain.Member;
import domain.WarFight;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.GlobalDataManager;
import manager.HeroManager;
import manager.MailManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import manager.WarDataManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb;
import pb.CommonPb.RptAtkWar;
import pb.CommonPb.WarRecord;
import pb.CommonPb.WarRecordPerson;
import pb.GamePb.GetWarFightRq;
import pb.GamePb.GetWarFightRs;
import pb.GamePb.SynWarRecordRq;
import pb.GamePb.SynWarStateRq;
import pb.GamePb.WarCancelRs;
import pb.GamePb.WarMembersRs;
import pb.GamePb.WarPartiesRq;
import pb.GamePb.WarPartiesRs;
import pb.GamePb.WarRankRq;
import pb.GamePb.WarRankRs;
import pb.GamePb.WarRegRq;
import pb.GamePb.WarRegRs;
import pb.GamePb.WarReportRq;
import pb.GamePb.WarReportRs;
import pb.GamePb.WarWinAwardRs;
import pb.GamePb.WarWinRankRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;
import util.WarHelper;

@Singleton
public class WarService implements IModuleMessageHandler {

	public static WarService getInst() {
		return BeanManager.getBean(WarService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: warReg
	 * 
	 * @Description: 百团混战报名 @param handler @return void @throws
	 */
	public void warReg(WarRegRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		// if (!inRegTime()) {
		// handler.sendErrorMsgToPlayer(GameError.OUT_REG_TIME);
		// return;
		// }

		// if (!TimeHelper.inWarRegTime()) {
		// handler.sendErrorMsgToPlayer(GameError.OUT_REG_TIME);
		// return;
		// }

		if (!WarDataManager.getInst().inRegTime()) {
//			handler.sendErrorMsgToPlayer(GameError.OUT_REG_TIME);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			LogHelper.ERROR_LOGGER.error("attack nul!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + playerId);
//			handler.sendErrorMsgToPlayer(GameError.SERVER_EXCEPTION);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(player.roleId);
		// int partyId = PartyDataManager.getInst().getPartyId(player.roleId);
		if (member == null || member.getPartyId() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int enterTime = member.getEnterTime();
		if (enterTime == DateUtil.getToday()) {
//			handler.sendErrorMsgToPlayer(GameError.IN_PARTY_TIME);
			return;
		}

		for (Army army : player.armys) {
			if (army.getState() == ArmyState.WAR) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_REG);
				return;
			}
		}

		Form attackForm = PbHelper.createForm(req.getForm());
		ConfHero staticHero = null;
		Hero hero = null;
		if (attackForm.getCommander() > 0) {
			hero = player.heros.get(attackForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);
		if (!PlayerDataManager.getInst().checkAndSubTank(player, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		if (hero != null) {
			hero.setCount(hero.getCount() - 1);
		}

		int fight = FightService.getInst().calcFormFight(player, attackForm);

		int marchTime = 60 * 60;
		int now = DateUtil.getSecondTime();
		Army army = new Army(player.maxKey(), 0, ArmyState.WAR, attackForm, marchTime, now + marchTime);
		player.armys.add(army);

		WarMember warMember = WarDataManager.getInst().createWarMember(player, member, attackForm, fight);
		WarDataManager.getInst().warReg(warMember);

		WarRegRs.Builder builder = WarRegRs.newBuilder();
		builder.setFight(fight);
		builder.setArmy(PbHelper.createArmyPb(army));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarRegRs, builder.build());
	}

	private void retreatEnd(Player player, Army army) {
		// 部队返回
		int[] p = army.getForm().p;
		int[] c = army.getForm().c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0 && c[i] > 0) {
				PlayerDataManager.getInst().addTank(player, p[i], c[i]);
			}
		}
		// 将领返回
		int heroId = army.getForm().getCommander();
		if (heroId > 0) {
			HeroManager.getInst().addHero(player, heroId, 1);
		}
	}

	/**
	 * 
	 * Method: warCancel
	 * 
	 * @Description: 百团混战取消报名 @param handler @return void @throws
	 */
	public void warCancel(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		// if (!TimeHelper.inWarRegTime()) {
		// handler.sendErrorMsgToPlayer(GameError.OUT_REG_TIME);
		// return;
		// }

		if (!WarDataManager.getInst().inRegTime()) {
//			handler.sendErrorMsgToPlayer(GameError.OUT_REG_TIME);
			return;
		}

		int partyId = PartyDataManager.getInst().getPartyId(playerId);
		if (partyId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Iterator<Army> it = player.armys.iterator();
		while (it.hasNext()) {
			Army army = (Army) it.next();
			if (army.getState() == ArmyState.WAR) {
				retreatEnd(player, army);
				WarDataManager.getInst().warUnReg(partyId, playerId);
				it.remove();
				WarCancelRs.Builder builder = WarCancelRs.newBuilder();
				MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarCancelRs, builder.build());
				return;
			}
		}

//		handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
	}

	/**
	 * 
	 * Method: warMembers
	 * 
	 * @Description: 获取军团成员报名列表 @param handler @return void @throws
	 */
	public void warMembers(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int partyId = PartyDataManager.getInst().getPartyId(playerId);
		if (partyId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		WarMembersRs.Builder builder = WarMembersRs.newBuilder();
		WarParty warParty = WarDataManager.getInst().getWarParty(partyId);
		if (warParty != null) {
			Iterator<WarMember> it = warParty.getMembers().values().iterator();
			while (it.hasNext()) {
				WarMember warMember = (WarMember) it.next();
				builder.addMemberReg(PbHelper.createWarRegPb(warMember));
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarMembersRs, builder.build());
	}

	/**
	 * 
	 * Method: warParties
	 * 
	 * @Description: 获取参战军团列表 @param handler @return void @throws
	 */
	public void warParties(WarPartiesRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int partyId = PartyDataManager.getInst().getPartyId(playerId);
		if (partyId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int page = req.getPage();
		int begin = page * 20;
		int end = begin + 20;

		WarPartiesRs.Builder builder = WarPartiesRs.newBuilder();
		Map<Integer, Long> parties = WarDataManager.getInst().getPartyFightMap();
		int index = 0;
		if (parties != null) {
			for (Map.Entry<Integer, Long> entry : parties.entrySet()) {
				if (index >= end) {
					break;
				}

				if (index >= begin) {
					// PartyData partyData =
					// PartyDataManager.getInst().getParty(entry.getKey());
					WarParty warParty = WarDataManager.getInst().getWarParty(entry.getKey());
					if (warParty != null) {
						PartyData partyData = warParty.getPartyData();
						if (partyData == null) {
//							handler.sendErrorMsgToPlayer(GameError.SERVER_EXCEPTION);
							return;
						}

						// LogHelper.ERROR_LOGGER.error("partyName:" +
						// partyData.getPartyName());
						// LogHelper.ERROR_LOGGER.error("regLv:" +
						// partyData.getRegLv());
						// LogHelper.ERROR_LOGGER.error("count:" +
						// warParty.getMembers().size());
						// LogHelper.ERROR_LOGGER.error("fight:" +
						// entry.getValue());

						builder.addPartyReg(PbHelper.createPartyRegPb(partyData.getPartyName(), partyData.getRegLv(), warParty.getMembers().size(), entry.getValue()));
					}
				}
				index++;
			}
		}

		builder.setTotal(parties.size());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarPartiesRs, builder.build());
	}

	/**
	 * 
	 * Method: winAward
	 * 
	 * @Description: 百团混战领取个人连胜排行奖励 @param handler @return void @throws
	 */
	public void winAward(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		int partyId = PartyDataManager.getInst().getPartyId(player.roleId);
		if (partyId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (GlobalDataManager.getInst().gameGlobal.getWarState() != WarState.WAR_END) {
//			handler.sendErrorMsgToPlayer(GameError.WAR_PROCESS);
			return;
		}

		if (WarDataManager.getInst().hadGetWinRankAward(player.roleId)) {
//			handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_BOX);
			return;
		}

		int rank = WarDataManager.getInst().getWinRank(player.roleId);
		if (rank < 1 || rank > 10) {
//			handler.sendErrorMsgToPlayer(GameError.NOT_ON_RANK);
			return;
		}

		WarDataManager.getInst().setWinRankAward(player.roleId);

		WarWinAwardRs.Builder builder = WarWinAwardRs.newBuilder();

		List<List<Integer>> awards = ConfWarAwardProvider.getInst().getWinAward(rank);
		builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, awards, AwardFrom.WAR_WIN));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarWinAwardRs, builder.build());
	}

	/**
	 * 
	 * Method: warReport
	 * 
	 * @Description: 获取百团混战战况 @param req @param handler @return void @throws
	 */
	public void warReport(WarReportRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int type = req.getType();
		WarReportRs.Builder builder = WarReportRs.newBuilder();

		if (type == 1) {// 全服战况
			LinkedList<WarRecord> list = GlobalDataManager.getInst().getWarRecord();
			if (list != null) {
				builder.addAllRecord(list);
			}
		} else if (type == 2) {// 军团战况
			LinkedList<WarRecord> list = WarDataManager.getInst().getPartyWarRecord(member.getPartyId());
			if (list != null) {
				builder.addAllRecord(list);
			}
		} else if (type == 3) {// 个人战况
			// Player player = PlayerDataManager.getInst().getPlayer(roleId);
			LinkedList<WarRecordPerson> list = member.getWarRecords();

			if (list != null) {
				for (WarRecordPerson warRecordPerson : list) {
					builder.addRecord(warRecordPerson.getRecord());
				}
			}
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarReportRs, builder.build());
	}

	/**
	 * 
	 * Method: warRank
	 * 
	 * @Description: 获取军团排名列表 @param req @param handler @return void @throws
	 */
	public void warRank(WarRankRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int partyId = PartyDataManager.getInst().getPartyId(playerId);
		if (partyId == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int page = req.getPage();
		int begin = page * 20;
		int end = begin + 20;

		Map<Integer, WarParty> rankMap = WarDataManager.getInst().getRankMap();
		WarRankRs.Builder builder = WarRankRs.newBuilder();
		int index = 0;

		for (Map.Entry<Integer, WarParty> entry : rankMap.entrySet()) {
			if (index >= end) {
				break;
			}

			if (index >= begin) {
				builder.addWarRank(PbHelper.createWarRankPb(entry.getValue()));
			}
			index++;
		}

		WarParty selfParty = WarDataManager.getInst().getWarParty(partyId);
		if (selfParty != null) {
			builder.setSelfParty(PbHelper.createWarRankPb(selfParty));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarRankRs, builder.build());
	}

	/**
	 * 
	 * Method: getWarFight
	 * 
	 * @Description: 获取战报 @param req @param handler @return void @throws
	 */
	public void getWarFight(GetWarFightRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int index = req.getIndex();
		GetWarFightRs.Builder builder = GetWarFightRs.newBuilder();

		int loop = 0;
		for (WarRecordPerson record : member.getWarRecords()) {
			if (loop == index) {
				builder.setRpt(record.getRpt());
				break;
			}

			loop++;
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetWarFightRs, builder.build());
	}

	/**
	 * 
	 * Method: warWinRank
	 * 
	 * @Description: 百团混战连胜排名列表 @param handler @return void @throws
	 */
	public void warWinRank(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int partyId = PartyDataManager.getInst().getPartyId(playerId);

		LinkedList<WarMember> list = WarDataManager.getInst().getWinRankList();
		WarWinRankRs.Builder builder = WarWinRankRs.newBuilder();
		int rank = 0;
		for (WarMember warMember : list) {
			rank++;
			builder.addWinRank(PbHelper.createWarWinRankPb(warMember, rank));
		}

		int winCount = 0;
		long fight = 0;
		WarParty selfParty = WarDataManager.getInst().getWarParty(partyId);
		if (selfParty != null) {
			WarMember warMember = selfParty.getMember(playerId);
			if (warMember != null) {
				Member member = warMember.getMember();
				winCount = member.getWinCount();
				fight = member.getRegFight();
			}
		}

		builder.setWinCount(winCount);
		builder.setFight(fight);
		boolean canGet = false;
		if (GlobalDataManager.getInst().gameGlobal.getWarState() == WarState.WAR_END) {
			int myRank = WarDataManager.getInst().getWinRank(playerId);
			if (myRank > 0 && myRank < 11) {
				if (!WarDataManager.getInst().hadGetWinRankAward(playerId)) {
					canGet = true;
				}
			}
		}

		builder.setCanGet(canGet);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WarWinRankRs, builder.build());
	}

	public void subForceToForm(Fighter fighter, Form form) {
		int[] c = form.c;
		for (int i = 0; i < c.length; i++) {
			Force force = fighter.forces[i];
			if (force != null) {
				form.c[i] = force.count;
			}
		}
	}

	// private Map<Integer, RptTank> haustArmyTank(Fighter fighter, Form form) {
	// Map<Integer, RptTank> map = new HashMap<Integer, RptTank>();
	// int killed = 0;
	// int tankId = 0;
	// for (Force force : fighter.forces) {
	// if (force != null) {
	// killed = force.killed;
	// if (killed > 0) {
	// tankId = force.staticTank.getTankId();
	// RptTank rptTank = map.get(tankId);
	// if (rptTank != null) {
	// rptTank.setCount(rptTank.getCount() + killed);
	// } else {
	// rptTank = new RptTank(tankId, killed);
	// map.put(tankId, rptTank);
	// }
	// }
	// }
	// }
	//
	// subForceToForm(fighter, form);
	// return map;
	// }

	private CommonPb.RptMan createRptMan(Player player, int hero) {
		CommonPb.RptMan.Builder builder = CommonPb.RptMan.newBuilder();
		Lord lord = player.lord;
		builder.setName(lord.getNick());
		builder.setHero(hero);
		return builder.build();
	}

	public boolean fightWarMember(WarMember a, WarMember d, RptAtkWar.Builder rptAtkWar) {

		Fighter attacker = FightService.getInst().createFighter(a.getPlayer(), a.getInstForm(), 3);
		Fighter defencer = FightService.getInst().createFighter(d.getPlayer(), d.getInstForm(), 3);

		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_2, true);
		fightLogic.packForm(a.getInstForm(), d.getInstForm());
		fightLogic.fight();

		CommonPb.Record record = fightLogic.generateRecord();
		subForceToForm(attacker, a.getInstForm());
		subForceToForm(defencer, d.getInstForm());

		int result = fightLogic.getWinState();

		// RptAtkWar.Builder rptAtkWar = RptAtkWar.newBuilder();
		rptAtkWar.setFirst(fightLogic.attackerIsFirst());
		rptAtkWar.setAttacker(createRptMan(a.getPlayer(), a.getInstForm().getCommander()));
		rptAtkWar.setDefencer(createRptMan(d.getPlayer(), d.getInstForm().getCommander()));
		rptAtkWar.setRecord(record);

		if (result == 1) {// 攻方胜利
			rptAtkWar.setResult(true);
			return true;
		} else {
			rptAtkWar.setResult(false);
			return false;
		}
	}

	public void sendWarAward() {
		Map<Integer, WarParty> rankMap = WarDataManager.getInst().getRankMap();
		int partyId;
		PartyData partyData;
		for (int i = 1; i < 11; i++) {
			WarParty warParty = rankMap.get(i);
			List<Prop> props = new ArrayList<>();
			List<List<Integer>> awards = ConfWarAwardProvider.getInst().getRankAward(i);

			for (List<Integer> prop : awards) {
				props.add(new Prop(prop.get(1), prop.get(2)));
			}

			partyData = warParty.getPartyData();
			partyId = partyData.getPartyId();
			PartyDataManager.getInst().addAmyProps(partyId, props);
			LogHelper.WAR_LOGGER.trace(partyData.getPartyName() + " lv:" + partyData.getRegLv() + " get rank:" + i);

			if (i == 1) {
				championBuff(warParty);
				PartyDataManager.getInst().addPartyTrend(partyId, 15, "");
			}

			PartyDataManager.getInst().addPartyTrend(partyId, 14, String.valueOf(i));
		}
	}

	private void membersAward() {
		int now = DateUtil.getSecondTime();
		Map<Integer, WarParty> parties = WarDataManager.getInst().getParties();
		Iterator<WarParty> it = parties.values().iterator();
		while (it.hasNext()) {
			WarParty warParty = (WarParty) it.next();
			Iterator<WarMember> iterator = warParty.getMembers().values().iterator();
			while (iterator.hasNext()) {
				WarMember warMember = (WarMember) iterator.next();
				addScoreAndBackArmy(warMember, now);
			}
		}
	}

	private void cancelWar() {
		// int now = DateUtil.getSecondTime();
		Map<Integer, WarParty> parties = WarDataManager.getInst().getParties();
		Iterator<WarParty> it = parties.values().iterator();
		while (it.hasNext()) {
			WarParty warParty = (WarParty) it.next();
			Iterator<WarMember> iterator = warParty.getMembers().values().iterator();
			while (iterator.hasNext()) {
				WarMember warMember = (WarMember) iterator.next();
				WarDataManager.getInst().cancelArmy(warMember);
			}
		}

		WarDataManager.getInst().cancelWarFight();
	}

	private void endWar() {
		WarParty warParty = WarDataManager.getInst().getRankMap().get(1);
		WarRecord out = PbHelper.createWarWinPb(warParty.getPartyData().getPartyName(), 1, DateUtil.getSecondTime());
		synWarRecord(out);
		WarDataManager.getInst().addWorldAndPartyRecord(warParty, out);

		for (WarMember warMember : WarDataManager.getInst().getWinRankList()) {
			GlobalDataManager.getInst().gameGlobal.getWinRank().add(warMember.getPlayer().roleId);
		}
	}

	private void championBuff(WarParty warParty) {
		Iterator<Member> it = PartyDataManager.getInst().getMemberList(warParty.getPartyData().getPartyId()).iterator();
		while (it.hasNext()) {
			Member member = (Member) it.next();
			Player player = PlayerDataManager.getInst().getPlayer(member.getLordId());
			if (player != null) {
				PlayerDataManager.getInst().addEffect(player, EffectType.WAR_CHAMPION, 18 * 3600);
			}
		}
	}

	final private static int[] WIN_COUNT_SCORE = { 0, 3, 7, 10, 23, 40, 60, 84, 111, 146 };

	private int rankScore(int winCount) {
		int score = 0;
		if (winCount >= 10) {
			score = 180;
		} else {
			score = WIN_COUNT_SCORE[winCount];
		}

		return score;
	}

	private void addScoreAndBackArmy(WarMember warMember, int now) {
		Player target = warMember.getPlayer();

		Form regForm = warMember.getForm();
		Form insForm = warMember.getInstForm();

		// 永久损失的坦克
		int[] c = new int[6];
		for (int i = 0; i < c.length; i++) {
			c[i] = (regForm.c[i] - insForm.c[i]) / 100;
		}

		int score = 90 + rankScore(warMember.getMember().getWinCount());
		String mailContent = "";
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 0) {
				score += ConfTankProvider.getInst().getConfigById(regForm.p[i]).getWarScore() * c[i];
				mailContent += (regForm.p[i] + "|" + c[i] + "&");
			}

			if (regForm.p[i] > 0) {
				PlayerDataManager.getInst().addTank(target, regForm.p[i], regForm.c[i] - c[i]);
			}
		}

		if (regForm.getCommander() > 0) {
			HeroManager.getInst().addHero(target, regForm.getCommander(), 1);
		}

		Iterator<Army> it = target.armys.iterator();
		while (it.hasNext()) {
			Army army = (Army) it.next();
			if (army.getState() == ArmyState.WAR) {
				it.remove();
				break;
			}
		}

		PartyDataManager.doPartyLivelyTask(warMember.getWarParty().getPartyData(), warMember.getMember(), PartyType.TASK_TEAM);

		PlayerDataManager.getInst().addAward(target, AwardType.CONTRIBUTION, 0, score, AwardFrom.WAR_PARTY);
		MailManager.getInst().sendNormalMail(target, MailType.MOLD_PARTY_WAR, now, String.valueOf(warMember.getMember().getWinCount()), String.valueOf(score), mailContent);
	}

	public void warTimerLogic() {
		if (WarHelper.isWarDay()) {
			int nowDay = DateUtil.getToday();
			WarLog warLog = WarDataManager.getInst().getWarLog();
			if (warLog == null || nowDay != warLog.getWarTime()) {
				warLog = new WarLog();
				warLog.setWarTime(nowDay);
				WarDataManager.getInst().setWarLog(warLog);
				WarDataManager.getInst().flushWarLog();
			}

			WarFight warFight = WarDataManager.getInst().getWarFight();
			if (WarHelper.isWarBeginReg()) {// 开始报名
				if (warFight == null || warFight.getFightDay() != nowDay) {
					warFight = new WarFight(nowDay);
					WarDataManager.getInst().setWarFight(warFight);
					warFight.setState(WarState.REG_STATE);
				}
			} else if (WarHelper.isWarBeginEnd()) {// 报名结束
				if (warFight.getState() == WarState.REG_STATE) {
					warFight.setState(WarState.PREPAIR_STATE);
					synWarState(WarState.PREPAIR_STATE);
				}
			} else if (WarHelper.isWarBeginFight()) {// 开战时间
				if (warFight.getState() == WarState.PREPAIR_STATE) {
					if (WarDataManager.getInst().getParties().size() < 10) {
						cancelWar();
						warFight.setState(WarState.CANCEL_STATE);
						synWarState(WarState.CANCEL_STATE);
						LogHelper.WAR_LOGGER.error("reg member count not enough!");
					} else {
						warFight.prepairForFight();
						warFight.setState(WarState.FIGHT_STATE);
						synWarState(WarState.FIGHT_STATE);
					}
				}
			}

			if (warFight != null) {
				int state = warFight.getState();
				if (state == WarState.FIGHT_STATE) {
					if (warFight.round()) {
						warFight.setState(WarState.FIGHT_END);
					}
				} else if (state == WarState.FIGHT_END) {
					sendWarAward();
					membersAward();
					endWar();
					warFight.setState(WarState.WAR_END);
					synWarState(WarState.WAR_END);
				}
			}
		}
	}

	public void synWarRecord(WarRecord record) {
		SynWarRecordRq.Builder builder = SynWarRecordRq.newBuilder();
		builder.setRecord(record);
		SynWarRecordRq req = builder.build();

		Map<Integer, WarParty> parties = WarDataManager.getInst().getParties();
		Iterator<WarParty> it = parties.values().iterator();
		while (it.hasNext()) {
			WarParty warParty = (WarParty) it.next();
			Iterator<WarMember> iterator = warParty.getMembers().values().iterator();
			while (iterator.hasNext()) {
				WarMember warMember = (WarMember) iterator.next();
				PlayerDataManager.getInst().synWarRecordToPlayer(warMember.getPlayer(), req);
			}
		}
	}

	public void synWarState(int state) {
		SynWarStateRq.Builder builder = SynWarStateRq.newBuilder();
		builder.setState(state);
		SynWarStateRq req = builder.build();

		Map<Integer, WarParty> parties = WarDataManager.getInst().getParties();
		Iterator<WarParty> it = parties.values().iterator();
		while (it.hasNext()) {
			WarParty warParty = (WarParty) it.next();
			Iterator<WarMember> iterator = warParty.getMembers().values().iterator();
			while (iterator.hasNext()) {
				WarMember warMember = (WarMember) iterator.next();
				PlayerDataManager.getInst().synWarStateToPlayer(warMember.getPlayer(), req);
			}
		}
	}

}
