package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.game.domain.PartyData;
import com.game.domain.Player;
import com.game.fight.FightLogic;
import com.game.fight.domain.Fighter;
import com.game.fight.domain.Force;
import com.game.util.CompareParty;
import com.game.util.EmojiHelper;
import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfHero;
import config.bean.ConfPartyBuildLevel;
import config.bean.ConfPartyCombat;
import config.bean.ConfPartyContribute;
import config.bean.ConfPartyProp;
import config.bean.ConfPartyScience;
import config.bean.ConfPartyTrend;
import config.bean.ConfPartyWeal;
import config.bean.ConfProp;
import config.provider.ConfAwardProvider;
import config.provider.ConfHeroProvider;
import config.provider.ConfPartyBuildLevelProvider;
import config.provider.ConfPartyCombatProvider;
import config.provider.ConfPartyContributeProvider;
import config.provider.ConfPartyLivelyProvider;
import config.provider.ConfPartyPropProvider;
import config.provider.ConfPartyProvider;
import config.provider.ConfPartyScienceProvider;
import config.provider.ConfPartyTrendProvider;
import config.provider.ConfPartyWealProvider;
import config.provider.ConfPropProvider;
import data.bean.Form;
import data.bean.Hero;
import data.bean.LiveTask;
import data.bean.Lord;
import data.bean.Man;
import data.bean.PartyApply;
import data.bean.PartyCombat;
import data.bean.PartyDonate;
import data.bean.PartyProp;
import data.bean.PartyRank;
import data.bean.PartyScience;
import data.bean.Prop;
import data.bean.Resource;
import data.bean.Trend;
import data.bean.TrendParam;
import data.bean.Weal;
import define.AwardFrom;
import define.AwardType;
import define.BuildingId;
import define.FirstActType;
import define.GoldCost;
import define.MailType;
import define.PartyType;
import define.SysChatId;
import define.TaskType;
import domain.Member;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.GlobalDataManager;
import manager.MailManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.ActivityPb.GetPartyLvRankRq;
import pb.ActivityPb.GetPartyLvRankRs;
import pb.CommonPb;
import pb.CommonPb.AwardPB;
import pb.GamePb.ApplyListRs;
import pb.GamePb.BuyPartyShopRq;
import pb.GamePb.BuyPartyShopRs;
import pb.GamePb.CannlyApplyRq;
import pb.GamePb.CleanMemberRq;
import pb.GamePb.CleanMemberRs;
import pb.GamePb.ConcedeJobRq;
import pb.GamePb.CreatePartyRq;
import pb.GamePb.CreatePartyRs;
import pb.GamePb.DoPartyCombatRq;
import pb.GamePb.DoPartyCombatRs;
import pb.GamePb.DonatePartyRq;
import pb.GamePb.DonatePartyRs;
import pb.GamePb.DonateScienceRq;
import pb.GamePb.DonateScienceRs;
import pb.GamePb.GetPartyAmyPropsRs;
import pb.GamePb.GetPartyCombatRs;
import pb.GamePb.GetPartyHallRs;
import pb.GamePb.GetPartyLiveRankRs;
import pb.GamePb.GetPartyMemberRs;
import pb.GamePb.GetPartyRankRq;
import pb.GamePb.GetPartyRankRs;
import pb.GamePb.GetPartyRq;
import pb.GamePb.GetPartyRs;
import pb.GamePb.GetPartyScienceRs;
import pb.GamePb.GetPartyShopRs;
import pb.GamePb.GetPartyTrendRq;
import pb.GamePb.GetPartyTrendRs;
import pb.GamePb.GetPartyWealRs;
import pb.GamePb.PartyApplyEditRq;
import pb.GamePb.PartyApplyEditRs;
import pb.GamePb.PartyApplyJudgeRq;
import pb.GamePb.PartyApplyJudgeRs;
import pb.GamePb.PartyApplyListRs;
import pb.GamePb.PartyApplyRq;
import pb.GamePb.PartyApplyRs;
import pb.GamePb.PartyJobCountRs;
import pb.GamePb.PartyctAwardRq;
import pb.GamePb.PartyctAwardRs;
import pb.GamePb.PtcFormRs;
import pb.GamePb.QuitPartyRs;
import pb.GamePb.SeachPartyRq;
import pb.GamePb.SeachPartyRs;
import pb.GamePb.SendPartyAmyPropRq;
import pb.GamePb.SendPartyAmyPropRs;
import pb.GamePb.SetMemberJobRq;
import pb.GamePb.SetMemberJobRs;
import pb.GamePb.SetPartyJobRq;
import pb.GamePb.SetPartyJobRs;
import pb.GamePb.SloganPartyRq;
import pb.GamePb.SloganPartyRs;
import pb.GamePb.UpMemberJobRq;
import pb.GamePb.UpMemberJobRs;
import pb.GamePb.UpPartyBuildingRq;
import pb.GamePb.UpPartyBuildingRs;
import pb.GamePb.UseAmyPropRq;
import pb.GamePb.UseAmyPropRs;
import pb.GamePb.WealDayPartyRq;
import pb.GamePb.WealDayPartyRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-9 下午1:53:39
 * @declare
 */
@Singleton
public class PartyService implements IModuleMessageHandler {

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	public static PartyService getInst() {
		return BeanManager.getBean(PartyService.class);
	}

	public long getResource(int resourceId, Resource resource, Lord lord) {
		if (resourceId == PartyType.RESOURCE_STONE) {
			return resource.getStone();
		} else if (resourceId == PartyType.RESOURCE_IRON) {
			return resource.getIron();
		} else if (resourceId == PartyType.RESOURCE_SILICON) {
			return resource.getSilicon();
		} else if (resourceId == PartyType.RESOURCE_COPPER) {
			return resource.getCopper();
		} else if (resourceId == PartyType.RESOURCE_OIL) {
			return resource.getOil();
		} else if (resourceId == PartyType.RESOURCE_GOLD) {
			return lord.getGold();
		}
		return -1;
	}

	/**
	 * Function:军团排名信息
	 * 
	 * @param req
	 * @param handler
	 */
	public void getPartyRank(GetPartyRankRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int page = req.getPage();
		int type = req.getType();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		GetPartyRankRs.Builder builder = GetPartyRankRs.newBuilder();

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() > 0) {
			int partyId = member.getPartyId();
			PartyRank partyRank = PartyDataManager.getInst().getPartyRank(partyId);
			PartyData partyData = PartyDataManager.getInst().getParty(partyId);
			int count = PartyDataManager.getInst().getPartyMemberCount(partyId);
			if (partyRank != null) {
				builder.setParty(PbHelper.createPartyRankPb(partyRank, partyData, count));
			}
		}

		List<PartyRank> partyRankList = PartyDataManager.getInst().getPartyRank(page, type, lord.getLevel(), lord.getFight());
		for (PartyRank e : partyRankList) {
			int partyId = e.getPartyId();
			PartyData partyData = PartyDataManager.getInst().getParty(partyId);
			int count = PartyDataManager.getInst().getPartyMemberCount(partyId);
			builder.addPartyRank(PbHelper.createPartyRankPb(e, partyData, count));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyRankRs, builder.build());
	}

	/**
	 * Function:军团等级排名信息
	 * 
	 * @param req
	 * @param handler
	 */
	public void getPartyLvRankRq(GetPartyLvRankRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int page = req.getPage();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Lord lord = player.lord;
		if (lord == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		GetPartyLvRankRs.Builder builder = GetPartyLvRankRs.newBuilder();
		PartyData party = PartyDataManager.getInst().getPartyByLordId(lord.getLordId());
//		if (party != null) {
//			PartyLvRank partyLvRank = activityDataManager.getPartyLvRank(party.getPartyId());
//			if (partyLvRank != null) {
//				builder.setParty(PbHelper.createPartyLvRankPb(partyLvRank));
//			}
//		}
//
//		List<PartyLvRank> partyLvRankList = activityDataManager.getPartyLvRankList(page);
//		for (PartyLvRank e : partyLvRankList) {
//			int partyId = e.getPartyId();
//			if (partyId == 0) {
//				continue;
//			}
//			builder.addPartyLvRank(PbHelper.createPartyLvRankPb(e));
//		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyLvRankRs, builder.build());
	}

	/**
	 * Function:军团信息
	 * 
	 * @param req
	 * @param handler
	 */
	public void getParty(GetPartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.lord.getLevel() < 10) {
//			//handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null) {
			member = PartyDataManager.getInst().createNewMember(player.lord, PartyType.COMMON);
		}

		int partyId = req.getPartyId();
		if (partyId == 0) {
			partyId = member.getPartyId();
		}

		GetPartyRs.Builder builder = GetPartyRs.newBuilder();
		if (partyId != 0) {
			PartyDataManager.getInst().refreshMember(member);
			PartyData partyData = PartyDataManager.getInst().getParty(partyId);
			int count = PartyDataManager.getInst().getPartyMemberCount(partyId);
			int rank = PartyDataManager.getInst().getRank(partyId);
			builder.setParty(PbHelper.createPartyPb(partyData, count, rank));
		}

		if (member.getPartyId() != 0) {
			builder.setDonate(member.getDonate());
			builder.setJob(member.getJob());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyRs, builder.build());
	}

	/**
	 * Function：军团成员
	 * 
	 * @param handler
	 */
	public void getPartyMember(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		GetPartyMemberRs.Builder builder = GetPartyMemberRs.newBuilder();
		List<Member> list = PartyDataManager.getInst().getMemberList(partyId);
		for (Member e : list) {
			Player player = PlayerDataManager.getInst().getPlayer(e.getLordId());
			if (player == null) {
				continue;
			}
			Lord lord = player.lord;
			if (lord == null) {
				continue;
			}

			int online = 0;
			if (!player.isLogin) {
				online = player.lord.getOffTime();
			}
			builder.addPartyMember(PbHelper.createPartyMemberPb(e, lord, online));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyMemberRs, builder.build());
	}

	/**
	 * 大厅详细信息
	 * 
	 * @param handler
	 */
	public void getPartyHallRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshMember(member);
		GetPartyHallRs.Builder builder = GetPartyHallRs.newBuilder();
		builder.setPartyDonate(PbHelper.createPartyDonatePb(member.getHallMine()));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyHallRs, builder.build());
	}

	/**
	 * Function：帮派科技信息
	 * 
	 * @param handler
	 */
	public void getPartyScienceRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		GetPartyScienceRs.Builder builder = GetPartyScienceRs.newBuilder();
		if (member == null || member.getPartyId() == 0) {
//			handler.sendMsgToPlayer(GetPartyScienceRs.ext, builder.build());
			return;
		}

		int partyId = member.getPartyId();
		PartyDataManager.getInst().refreshMember(member);
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		builder.setPartyDonate(PbHelper.createPartyDonatePb(member.getScienceMine()));
		List<ConfPartyScience> initPartyScieceList = ConfPartyScienceProvider.getInst().getInitScience();
		for (ConfPartyScience e : initPartyScieceList) {
			int scienceId = e.getScienceId();
			PartyScience science = null;
			if (!partyData.getSciences().containsKey(scienceId)) {
				science = new PartyScience(scienceId, 0);
			} else {
				science = partyData.getSciences().get(scienceId);
			}
			if (science == null) {
				continue;
			}
			builder.addScience(PbHelper.createPartySciencePb(science));
		}
		Iterator<PartyScience> it = partyData.getSciences().values().iterator();
		while (it.hasNext()) {
			builder.addScience(PbHelper.createPartySciencePb(it.next()));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyScienceRs, builder.build());
	}

	/**
	 * Function：军团福利院信息
	 * 
	 * @param handler
	 */
	public void getPartyWealRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshMember(member);
		int partyId = member.getPartyId();

		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		PartyDataManager.getInst().refreshPartyData(partyData);

		GetPartyWealRs.Builder builder = GetPartyWealRs.newBuilder();
		builder.setEverWeal(member.getDayWeal());

		builder.setLive(partyData.getLively());

		int source = ConfPartyLivelyProvider.getInst().getPartyLiveResource(partyData.getLively());
		Weal resource = new Weal();
		// 军团总资源
		Weal mine = partyData.getReportMine();

		// 可领取总福利
		long iron = mine.getIron() * source / 1000;
		long oil = mine.getOil() * source / 1000;
		long copper = mine.getCopper() * source / 1000;
		long silicon = mine.getSilicon() * source / 1000;
		long stone = mine.getStone() * source / 1000;

		// 剩余领取福利
		Weal wealMine = member.getWealMine();
		iron = iron - wealMine.getIron() < 0 ? 0 : iron - wealMine.getIron();
		oil = oil - wealMine.getOil() < 0 ? 0 : oil - wealMine.getOil();
		copper = copper - wealMine.getCopper() < 0 ? 0 : copper - wealMine.getCopper();
		silicon = silicon - wealMine.getSilicon() < 0 ? 0 : silicon - wealMine.getSilicon();
		stone = stone - wealMine.getStone() < 0 ? 0 : stone - wealMine.getStone();
		resource.setIron(iron);
		resource.setOil(oil);
		resource.setCopper(copper);
		resource.setSilicon(silicon);
		resource.setStone(stone);

		builder.setResource(PbHelper.createWealPb(resource));

		builder.setGetResource(PbHelper.createWealPb(member.getWealMine()));
		Iterator<LiveTask> it = partyData.getLiveTasks().values().iterator();
		while (it.hasNext()) {
			builder.addLiveTask(PbHelper.createLiveTaskPb(it.next()));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyWealRs, builder.build());
	}

	/**
	 * Function:军团道具商店
	 * 
	 * @param handler
	 */
	public void getPartyShopRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyData partyData = PartyDataManager.getInst().getParty(member.getPartyId());
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshMember(member);
		PartyDataManager.getInst().refreshPartyData(partyData);

		GetPartyShopRs.Builder builder = GetPartyShopRs.newBuilder();
		Iterator<PartyProp> it = member.getPartyProps().iterator();
		while (it.hasNext()) {
			PartyProp next = it.next();
			ConfPartyProp staticProp = ConfPartyPropProvider.getInst().getConfigById(next.getKeyId());
			if (staticProp == null || staticProp.getTreasure() != 1) {
				continue;
			}
			builder.addPartyProp(PbHelper.createPartyPropPb(next));
		}

		// 军团珍品
		List<Integer> shopProps = partyData.getShopProps();
		List<Integer> globalShop = GlobalDataManager.getInst().getPartyShop(shopProps);
		for (int i = 0; i < globalShop.size(); i++) {
			int keyId = globalShop.get(i);
			int count = 0;
			if (shopProps.size() < i + 1) {
				shopProps.add(0);
			} else {
				count = shopProps.get(i);
			}
			builder.addPartyProp(PbHelper.createPartyPropPb(keyId, count));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyShopRs, builder.build());
	}

	/**
	 * 大厅捐献
	 * 
	 * @param req
	 * @param handler
	 */
	public void donatePartyRq(DonatePartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshMember(member);

		long roleId = playerId;

		Player player = PlayerDataManager.getInst().getPlayer(roleId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Resource resource = player.resource;
		Lord lord = player.lord;
		int resourceId = req.getResouceId();
		if (resourceId < 1 || resourceId > 6) {
//			//handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		PartyDonate partyDonate = member.getHallMine();
		int count = PartyDataManager.getInst().getDonateMember(partyDonate, resourceId);
		ConfPartyContribute staContribute = ConfPartyContributeProvider.getInst().getStaticContribute(resourceId, count + 1);
		if (staContribute == null) {
//			//handler.sendErrorMsgToPlayer(GameError.DONATE_COUNT);
			return;
		}
		long hadResource = getResource(resourceId, resource, lord);
//
//		float discount = activityDataManager.discountDonate(resourceId);
//		int price = (int) (discount * staContribute.getPrice() / 100f);

		int price = staContribute.getPrice();

		if (hadResource < price) {
//			//handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
			return;
		}

		DonatePartyRs.Builder builder = DonatePartyRs.newBuilder();
		if (resourceId == PartyType.RESOURCE_STONE) {
			partyDonate.setStone(count + 1);
			PlayerDataManager.getInst().modifyStone(resource, -staContribute.getPrice());
			builder.setStone(resource.getStone());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 0);
		} else if (resourceId == PartyType.RESOURCE_IRON) {
			partyDonate.setIron(count + 1);
			PlayerDataManager.getInst().modifyIron(resource, -staContribute.getPrice());
			builder.setIron(resource.getIron());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 0);
		} else if (resourceId == PartyType.RESOURCE_SILICON) {
			partyDonate.setSilicon(count + 1);
			PlayerDataManager.getInst().modifySilicon(resource, -staContribute.getPrice());
			builder.setSilicon(resource.getSilicon());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 0);
		} else if (resourceId == PartyType.RESOURCE_COPPER) {
			partyDonate.setCopper(count + 1);
			PlayerDataManager.getInst().modifyCopper(resource, -staContribute.getPrice());
			builder.setCopper(resource.getCopper());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 0);
		} else if (resourceId == PartyType.RESOURCE_OIL) {
			partyDonate.setOil(count + 1);
			PlayerDataManager.getInst().modifyOil(resource, -staContribute.getPrice());
			builder.setOil(resource.getOil());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 0);
		} else if (resourceId == PartyType.RESOURCE_GOLD) {
			partyDonate.setGold(count + 1);
			PlayerDataManager.getInst().subGold(player.lord, price, GoldCost.PARTY_DONATE_HALL);
			builder.setGold(player.lord.getGold());
//			activityDataManager.updActivity(player, ActivityConst.ACT_PARTY_DONATE, 1, 1);
		}

		PartyDataManager.doPartyLivelyTask(partyData, member, PartyType.TASK_DONATE);

		TaskManager.getInst().updTask(player, TaskType.COND_PARTY_DONATE, 1, null);

		// 添加建设度
		int build = staContribute.getBuild();
//		build = activityDataManager.fireSheet(player, partyId, build);
		builder.setIsBuild(false);

		List<Long> donates = partyData.getDonates(1);
		if (donates == null) {
			partyData.setBuild(partyData.getBuild() + build);
			builder.setIsBuild(true);
			donates = new ArrayList<Long>();
			donates.add(roleId);
			partyData.putDonates(1, donates);
		} else {
			int index = donates.indexOf(roleId);
			if (index == -1) {
				int lvNum = ConfPartyProvider.getInst().getLvNum(partyData.getPartyLv());
				if (donates.size() < lvNum + 6) {
					partyData.setBuild(partyData.getBuild() + build);
					builder.setIsBuild(true);
					donates.add(roleId);
				}
			} else {
				partyData.setBuild(partyData.getBuild() + build);
				builder.setIsBuild(true);
			}
		}

		member.setDonate(member.getDonate() + build);
		member.setWeekDonate(member.getWeekDonate() + build);
		member.setWeekAllDonate(member.getWeekAllDonate() + build);

//		activityDataManager.updatePartyLvRank(partyData);

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DonatePartyRs, builder.build());

	}

	/**
	 * Function:军团建筑:大厅，科技馆，福利院升级
	 * 
	 * @param req
	 * @param handler
	 */
	public void upPartyBuildingRq(UpPartyBuildingRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int buildingId = req.getBuildingId();
		if (buildingId != 1 && buildingId != 2 && buildingId != 3) {
//			//handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (member.getJob() != PartyType.LEGATUS) {
//			//handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int buildingLv = 0;
		ConfPartyBuildLevel buildLevel = null;
		if (buildingId == PartyType.HALL_ID) {
			buildingLv = partyData.getPartyLv() + 1;
		} else if (buildingId == PartyType.SCIENCE_ID) {
			if (partyData.getScienceLv() >= partyData.getPartyLv()) {// 不能超过军团大厅科技等级
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_LV_ERROR);
				return;
			}
			buildingLv = partyData.getScienceLv() + 1;
		} else if (buildingId == PartyType.WEAL_ID) {
			if (partyData.getWealLv() >= partyData.getPartyLv()) {// 不能超过军团大厅科技等级
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_LV_ERROR);
				return;
			}
			buildingLv = partyData.getWealLv() + 1;
		}

		buildLevel = ConfPartyBuildLevelProvider.getInst().getBuildLevel(buildingId, buildingLv);
		UpPartyBuildingRs.Builder builder = UpPartyBuildingRs.newBuilder();
		if (buildLevel != null && partyData.getBuild() >= buildLevel.getNeedExp()) {
			partyData.setBuild(partyData.getBuild() - buildLevel.getNeedExp());
			if (buildingId == PartyType.HALL_ID) {
				partyData.setPartyLv(buildingLv);
				PartyDataManager.getInst().addPartyTrend(partyId, 7, String.valueOf(buildingLv));
			} else if (buildingId == PartyType.SCIENCE_ID) {
				partyData.setScienceLv(buildingLv);
				PartyDataManager.getInst().addPartyTrend(partyId, 8, String.valueOf(buildingLv));
			} else if (buildingId == PartyType.WEAL_ID) {
				partyData.setWealLv(buildingLv);
				PartyDataManager.getInst().addPartyTrend(partyId, 9, String.valueOf(buildingLv));
			} else {
				buildingLv -= 1;
			}
		}

//		activityDataManager.updatePartyLvRank(partyData);

		builder.setBuildingId(buildingId);
		builder.setBuildingLv(buildingLv);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpPartyBuildingRs, builder.build());
	}

	/**
	 * 自定义职位
	 * 
	 * @param req
	 * @param handler
	 */
	public void setPartyJobRq(SetPartyJobRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		String jobName1 = req.getJobName1();
		String jobName2 = req.getJobName2();
		String jobName3 = req.getJobName3();
		String jobName4 = req.getJobName4();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (member.getJob() != PartyType.LEGATUS) {
//			//handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (jobName1 == null || jobName1.length() > 4) {
//			//handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (jobName2 == null || jobName2.length() > 4) {
//			//handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (jobName3 == null || jobName3.length() > 4) {
//			//handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (jobName4 == null || jobName4.length() > 4) {
//			//handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		jobName1 = EmojiHelper.filterEmoji(jobName1);
		jobName2 = EmojiHelper.filterEmoji(jobName2);
		jobName3 = EmojiHelper.filterEmoji(jobName3);
		jobName4 = EmojiHelper.filterEmoji(jobName4);

		partyData.setJobName1(jobName1);
		partyData.setJobName2(jobName2);
		partyData.setJobName3(jobName3);
		partyData.setJobName4(jobName4);
		SetPartyJobRs.Builder builder = SetPartyJobRs.newBuilder();

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SetPartyJobRs, builder.build());
	}

	/**
	 * 贡献兑换军团道具
	 * 
	 * @param req
	 * @param handler
	 */
	public void buyPartyShopRq(BuyPartyShopRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int keyId = req.getKeyId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		ConfPartyProp staticProp = ConfPartyPropProvider.getInst().getConfigById(keyId);
		if (staticProp == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		PartyDataManager.getInst().refreshPartyData(partyData);
		PartyDataManager.getInst().refreshMember(member);

		int itemType = staticProp.getItemType();
		int itemId = staticProp.getItemId();
		int itemCount = staticProp.getItemNum();
		int treasure = staticProp.getTreasure();
		int contribute = staticProp.getContribute();

		PartyProp partyProp = null;
		BuyPartyShopRs.Builder builder = BuyPartyShopRs.newBuilder();
		if (treasure == 1) {
			Iterator<PartyProp> it = member.getPartyProps().iterator();
			while (it.hasNext()) {
				PartyProp next = it.next();
				if (next.getKeyId() == keyId && next.getCount() < staticProp.getCount()) {
					partyProp = next;
				}
			}
			if (partyProp == null) {
				// handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
				return;
			}
			if (!PartyDataManager.getInst().subDonate(member, contribute)) {
				// handler.sendErrorMsgToPlayer(GameError.DONATE_NOT_ENOUGH);
				return;
			}
			partyProp.setCount(partyProp.getCount() + 1);
		} else {
			List<Integer> shopProps = partyData.getShopProps();
			List<Integer> globalShop = GlobalDataManager.getInst().getPartyShop(shopProps);
			int buyCount = 0;
			for (int i = 0; i < globalShop.size(); i++) {
				int golKeyId = globalShop.get(i);
				int count = partyData.getShopProps().get(i);
				if (golKeyId == keyId && count < staticProp.getCount()) {
					buyCount = count + 1;

					if (!PartyDataManager.getInst().subDonate(member, contribute)) {
						// handler.sendErrorMsgToPlayer(GameError.DONATE_NOT_ENOUGH);
						return;
					}

					partyData.getShopProps().set(i, buyCount);
					break;
				}
			}
			if (buyCount == 0) {
				// handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
				return;
			}
		}

		int awardKeyId = PlayerDataManager.getInst().addAward(player, itemType, itemId, itemCount, AwardFrom.PARTY_SHOP);
		builder.addAward(PbHelper.createAwardPb(itemType, itemId, staticProp.getItemNum(), awardKeyId));

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyPartyShopRs, builder.build());

		PartyDataManager.doPartyLivelyTask(partyData, member, PartyType.TASK_BUY_SHOP);
		TaskManager.getInst().updTask(player, TaskType.COND_PARTY_PROP, 1, null);
	}

	public void wealDayPartyRq(WealDayPartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int type = req.getType();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int enterTime = member.getEnterTime();
		if (enterTime == DateUtil.getToday()) {
//			//handler.sendErrorMsgToPlayer(GameError.TIME_NOT_ENOUGH);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		WealDayPartyRs.Builder builder = WealDayPartyRs.newBuilder();
		if (type == 1) {
			if (member.getDayWeal() != 0) {
//				//handler.sendErrorMsgToPlayer(GameError.WEAL_GOT);
				return;
			}

			ConfPartyWeal staticPartyWeal = ConfPartyWealProvider.getInst().getConfigById(partyData.getWealLv());
			if (staticPartyWeal == null) {
//				//handler.sendErrorMsgToPlayer(GameError.WEAL_NOT_EXIST);
				return;
			}
			List<List<Integer>> list = staticPartyWeal.getWealList();
			for (List<Integer> e : list) {
				int itemType = e.get(0);
				int itemId = e.get(1);
				int itemCount = e.get(2);
				int keydId = PlayerDataManager.getInst().addAward(player, itemType, itemId, itemCount, AwardFrom.PARTY_WEAL_DAY);
				builder.addAward(PbHelper.createAwardPb(itemType, itemId, itemCount, keydId));
			}
			member.setDayWeal(1);
		} else if (type == 2) {
			PartyDataManager.getInst().refreshPartyData(partyData);
			int source = ConfPartyLivelyProvider.getInst().getPartyLiveResource(partyData.getLively());
			// 军团总资源
			Weal mine = partyData.getReportMine();

			// 可领取总福利
			long iron = mine.getIron() * source / 1000;
			long oil = mine.getOil() * source / 1000;
			long copper = mine.getCopper() * source / 1000;
			long silicon = mine.getSilicon() * source / 1000;
			long stone = mine.getStone() * source / 1000;

			// 剩余领取福利
			Weal wealMine = member.getWealMine();
			iron = iron - wealMine.getIron() < 0 ? 0 : iron - wealMine.getIron();
			oil = oil - wealMine.getOil() < 0 ? 0 : oil - wealMine.getOil();
			copper = copper - wealMine.getCopper() < 0 ? 0 : copper - wealMine.getCopper();
			silicon = silicon - wealMine.getSilicon() < 0 ? 0 : silicon - wealMine.getSilicon();
			stone = stone - wealMine.getStone() < 0 ? 0 : stone - wealMine.getStone();

			// 记录领取福利
			wealMine.setIron(wealMine.getIron() + iron);
			wealMine.setOil(wealMine.getOil() + oil);
			wealMine.setCopper(wealMine.getCopper() + copper);
			wealMine.setSilicon(wealMine.getSilicon() + silicon);
			wealMine.setStone(wealMine.getStone() + stone);

			Resource resource = player.resource;
			PlayerDataManager.getInst().modifyIron(resource, iron);
			PlayerDataManager.getInst().modifyOil(resource, oil);
			PlayerDataManager.getInst().modifySilicon(resource, silicon);
			PlayerDataManager.getInst().modifyCopper(resource, copper);
			PlayerDataManager.getInst().modifyStone(resource, stone);
			builder.setIron(resource.getIron());
			builder.setOil(resource.getOil());
			builder.setCopper(resource.getCopper());
			builder.setSilicon(resource.getSilicon());
			builder.setStone(resource.getStone());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.WealDayPartyRs, builder.build());
	}

	public void partyApplyListRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		if (member.getJob() == PartyType.COMMON) {
//			//handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}
		Player p = PlayerDataManager.getInst().getPlayer(playerId);
		if (p == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		if (p.lord.getLevel() < 10) {
//			//handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}
		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		PartyApplyListRs.Builder builder = PartyApplyListRs.newBuilder();
		Iterator<PartyApply> it = partyData.getApplys().values().iterator();
		while (it.hasNext()) {
			PartyApply partyApply = it.next();
			long lordId = partyApply.getLordId();
			Player player = PlayerDataManager.getInst().getPlayer(lordId);
			if (player == null) {
				continue;
			}
			builder.addPartyApply(PbHelper.createPartyApplyPb(player.lord, partyApply));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyApplyListRs, builder.build());
	}

	/**
	 * 玩家申请入军团
	 * 
	 * @param req
	 * @param handler
	 */
	public void partyApplyRq(PartyApplyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null) {
			member = PartyDataManager.getInst().createNewMember(player.lord, PartyType.COMMON);
		}
		if (member.getPartyId() != 0) {
//			//handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}
		int partyId = req.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);

		Lord lord = player.lord;
		if (partyData.getApplyFight() > lord.getFight()) {
//			//handler.sendErrorMsgToPlayer(GameError.FIGHT_NOT_ENOUGH);
			return;
		}

		if (partyData.getApplyLv() > lord.getLevel() || lord.getLevel() < 10) {
//			//handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		int memberCount = PartyDataManager.getInst().getPartyMemberCount(partyId);
		int limitNum = ConfPartyProvider.getInst().getLvNum(partyData.getPartyLv());
		if (limitNum <= memberCount) {
//			//handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
			return;
		}

		if (partyData.getApply() == PartyType.APPLY) {
			Map<Long, PartyApply> applys = partyData.getApplys();
			if (applys.size() >= 20) {
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_APPLY_FULL);
				return;
			}
			if (applys.containsKey(playerId)) {
//				//handler.sendErrorMsgToPlayer(GameError.HAD_APPLY);
				return;
			}

			int today = DateUtil.getSecondTime();
			PartyApply partyApply = new PartyApply();
			partyApply.setLordId(playerId);
			partyApply.setApplyDate(today);
			applys.put(playerId, partyApply);

			String applyList = member.getApplyList();
			if (applyList == null || applyList.startsWith("null")) {
				member.setApplyList("|" + partyId + "|");
			} else {
				member.setApplyList(applyList + partyId + "|");
			}
			PartyDataManager.getInst().addPartyTrend(partyId, 1, String.valueOf(playerId));
			// 异步通知军团长,副军团
			PlayerDataManager.getInst().synApplyPartyToPlayer(partyId, applys.size());
		} else {
			int partyLv = partyData.getPartyLv();
			String applyList = member.getApplyList();
			int flag = PartyDataManager.getInst().enterParty(partyId, partyLv, member);
			if (flag == 1) {
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
				return;
			} else if (flag == 2) {
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_MEMBER_FULL);
				return;
			}
			if (lord != null && lord.getPartyTipAward() == 0) {
				lord.setPartyTipAward(1);
			}
			resetApply(playerId, applyList, 0);// 该玩家的申请全取消

//			if (mineDataManager.getSeniorState() == SeniorState.END_STATE) {
//				player.seniorAward = 1;
//			}

			MailManager.getInst().sendNormalMail(player, MailType.MOLD_ENTER_PARTY, DateUtil.getSecondTime(), partyData.getPartyName());
			ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.JOIN_PARTY, player.lord.getNick()), partyId);
		}
		PartyApplyRs.Builder builder = PartyApplyRs.newBuilder();

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyApplyRs, builder.build());
	}

	/**
	 * Function：军团申请审批
	 * 
	 * @param req
	 * @param handler
	 */
	public void partyApplyJudgeRq(PartyApplyJudgeRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int judge = req.getJudge();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
//			//handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (member.getJob() < PartyType.LEGATUS_CP) {
//			//handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (judge == 1 || judge == 2) {
			long lordId = req.getLordId();
			PartyApply applyer = partyData.getApplys().get(lordId);
			if (applyer == null) {
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
				return;
			}
			Member memberApplyer = PartyDataManager.getInst().getMemberById(lordId);
			if (memberApplyer == null || memberApplyer.getPartyId() != 0) {
//				//handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
				return;
			}
			Player playerApply = PlayerDataManager.getInst().getPlayer(lordId);
			if (playerApply == null) {
//				//handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
				return;
			}

			if (judge == 1) {// 通过
				String applyList = memberApplyer.getApplyList();
				int count = PartyDataManager.getInst().enterParty(partyId, partyData.getPartyLv(), memberApplyer);
				if (count == 0) {
					PartyDataManager.getInst().addPartyTrend(partyId, 1, String.valueOf(lordId));
					MailManager.getInst().sendNormalMail(playerApply, MailType.MOLD_ENTER_PARTY, DateUtil.getSecondTime(), partyData.getPartyName());

					PlayerDataManager.getInst().synPartyAcceptToPlayer(playerApply, partyId, 1);
					resetApply(lordId, applyList, 0);// 该玩家的申请全取消

					Lord lord = playerApply.lord;

					if (lord != null && lord.getPartyTipAward() == 0) {
						lord.setPartyTipAward(1);
					}

//					if (mineDataManager.getSeniorState() == SeniorState.END_STATE) {
//						playerApply.seniorAward = 1;
//					}

					ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.JOIN_PARTY, playerApply.lord.getNick()), partyId);
				} else {
					// handler.sendErrorMsgToPlayer(GameError.PARTY_MEMBER_FULL);
					return;
				}
			} else if (judge == 2) {// 拒绝
				resetApply(lordId, memberApplyer.getApplyList(), partyId);// 该玩家的申请取消
				memberApplyer.getApplyList().replace("|" + partyId, "");
				PlayerDataManager.getInst().synPartyAcceptToPlayer(playerApply, partyId, 0);
			}
		} else if (judge == 3) {// 清空玩家申请列表
			Iterator<PartyApply> it = partyData.getApplys().values().iterator();
			while (it.hasNext()) {
				PartyApply next = it.next();
				it.remove();
				Member applyMember = PartyDataManager.getInst().getMemberById(next.getLordId());
				if (applyMember == null) {
					continue;
				}
				applyMember.getApplyList().replace("|" + partyId, "");
			}
		}
		PartyApplyJudgeRs.Builder builder = PartyApplyJudgeRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyApplyJudgeRs, builder.build());
	}

	/**
	 * Function:创建军团
	 * 
	 * @param req
	 * @param handler
	 */
	public void createPartyRq(CreatePartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null) {
			member = PartyDataManager.getInst().createNewMember(player.lord, PartyType.COMMON);
		}

		if (member.getPartyId() != 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		String partyName = req.getPartyName();

		if (PartyDataManager.getInst().isExistPartyName(partyName)) {
			// handler.sendErrorMsgToPlayer(GameError.SAME_PARTY_NAME);
			return;
		}

		PartyDataManager.getInst().refreshMember(member);

		int apply = req.getApplyType();
		int createType = req.getType();
		if (apply != 1 && apply != 2) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		if (createType != 1 && createType != 2) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		if (partyName == null || partyName.isEmpty() || partyName.length() >= 12 || partyName.length() < 2) {
			// handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (EmojiHelper.containsEmoji(partyName)) {
			// handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
			return;
		}

		boolean flag = PartyDataManager.getInst().isNameExist(partyName);
		if (flag) {
			// handler.sendErrorMsgToPlayer(GameError.SAME_PARTY_NAME);
			return;
		}

		if (player.lord.getLevel() < 10) {
			// handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}
		int today = DateUtil.getToday();
		Lord lord = player.lord;
		Resource resource = player.resource;
		if (createType == 1) {
			int cost = 50;
			if (lord.getGold() < cost) {
				// handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.PARTY_CREATE);
		} else if (createType == 2) {
			int cost = 300;
			if (resource.getStone() < cost || resource.getIron() < cost || resource.getSilicon() < cost || resource.getCopper() < cost || resource.getOil() < cost) {
				// handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
				return;
			}
			PlayerDataManager.getInst().modifyStone(resource, -cost);
			PlayerDataManager.getInst().modifyCopper(resource, -cost);
			PlayerDataManager.getInst().modifyIron(resource, -cost);
			PlayerDataManager.getInst().modifySilicon(resource, -cost);
			PlayerDataManager.getInst().modifyOil(resource, -cost);
		}
		if (lord != null && lord.getPartyTipAward() == 0) {
			lord.setPartyTipAward(1);
		}

		PartyData partyData = PartyDataManager.getInst().createParty(lord, member, partyName, apply, today);
		CreatePartyRs.Builder builder = CreatePartyRs.newBuilder();
		int rank = PartyDataManager.getInst().getRank(partyData.getPartyId());
		builder.setParty(PbHelper.createPartyPb(partyData, 1, rank));
		builder.setStone(resource.getSilicon());
		builder.setIron(resource.getIron());
		builder.setSilicon(resource.getSilicon());
		builder.setCopper(resource.getCopper());
		builder.setOil(resource.getOil());
		builder.setGold(lord.getGold());

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CreatePartyRs, builder.build());

	}

	/**
	 * Function:退出军团
	 * 
	 * @param handler
	 */
	public void quitPartyRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		int partyId = member.getPartyId();
		int count = PartyDataManager.getInst().getPartyMemberCount(partyId);
		if (count <= 1) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (member.getJob() == PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (PartyDataManager.getInst().inWar(member)) {
			// handler.sendErrorMsgToPlayer(GameError.IN_WAR);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		// PartyData party = PartyDataManager.getInst().getPartyByLordId(player.roleId);
		// if (party != null) {
		// party.setScore(party.getScore() - player.seniorScore);
		// mineDataManager.setPartyScoreRank(party);
		// }

		PartyDataManager.getInst().quitParty(partyId, member);

		PartyDataManager.getInst().addPartyTrend(partyId, 2, String.valueOf(playerId));

		QuitPartyRs.Builder builder = QuitPartyRs.newBuilder();

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.QuitPartyRs, builder.build());

//		worldService.retreatAllGuard(player);

		ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.QUIT_PARTY, player.lord.getNick()), partyId);

	}

	/**
	 * Function:捐献
	 * 
	 * @param req
	 * @param handler
	 */
	public void donateScience(DonateScienceRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int scienceId = req.getScienceId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshPartyData(partyData);
		PartyDataManager.getInst().refreshMember(member);
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Resource resource = player.resource;
		Lord lord = player.lord;
		int resourceId = req.getResouceId();
		if (resourceId < 1 || resourceId > 6) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		PartyDonate partyDonate = member.getScienceMine();
		int count = PartyDataManager.getInst().getDonateMember(partyDonate, resourceId);
		ConfPartyContribute staContribute = ConfPartyContributeProvider.getInst().getStaticContribute(resourceId, count + 1);
		if (staContribute == null) {
			// handler.sendErrorMsgToPlayer(GameError.DONATE_COUNT);
			return;
		}

		long hadResource = getResource(resourceId, resource, lord);
		int price = (int) (1 * staContribute.getPrice() / 100f);

		if (hadResource < price) {
			// handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
			return;
		}

		// 添加科技技能
		Map<Integer, PartyScience> scienceMap = partyData.getSciences();
		PartyScience science = scienceMap.get(scienceId);
		if (science == null) {
			science = new PartyScience();
			science.setScienceId(scienceId);
			science.setScienceLv(0);
			science.setSchedule(0);
			scienceMap.put(scienceId, science);
		}

		int build = staContribute.getBuild();
		ConfPartyScience staticScience = ConfPartyScienceProvider.getInst().getPartyScience(scienceId, science.getScienceLv() + 1);

		// 科技等级
		int scienceLv = partyData.getScienceLv();
		if (scienceLv < science.getScienceLv()) {
			// handler.sendErrorMsgToPlayer(GameError.SCIENCE_LV_ERROR);
			return;
		}

		int maxSchedule = staticScience.getSchedule();
		if (scienceLv == science.getScienceLv() && science.getSchedule() >= maxSchedule) {
			// handler.sendErrorMsgToPlayer(GameError.SCIENCE_LV_ERROR);
			return;
		}

		int addBuild = 0;

		if (staticScience != null) {
			if (partyDonate.getCopper() == 0 && partyDonate.getGold() == 0 && partyDonate.getIron() == 0 && partyDonate.getOil() == 0 && partyDonate.getSilicon() == 0 && partyDonate.getStone() == 0) {

				build = staContribute.getBuild() * ConfPartyLivelyProvider.getInst().getPartyLiveBuild(partyData.getLively());// 每日
				// 基础*首次系数
			}
			if (staContribute.getBuild() != build) {// 有首次
				build += addBuild;
			} else {
				build = addBuild;
			}
			int schedule = science.getSchedule() + build;
			if (scienceLv == science.getScienceLv()) {
				schedule = schedule > maxSchedule ? maxSchedule : schedule;
			}

			List<Long> donates = partyData.getDonates(2);
			if (donates == null) {
				donates = new ArrayList<Long>();
				donates.add(playerId);
				partyData.putDonates(2, donates);
				science.setSchedule(schedule);
			} else {
				int index = donates.indexOf(playerId);
				if (index == -1) {
					int lvNum = ConfPartyProvider.getInst().getLvNum(partyData.getPartyLv());
					if (donates.size() < lvNum + 6) {
						donates.add(playerId);
						science.setSchedule(schedule);
					}
				} else {
					science.setSchedule(schedule);
				}
			}

			if (scienceLv > science.getScienceLv() && science.getSchedule() >= maxSchedule) {
				science.setScienceLv(science.getScienceLv() + 1);
				schedule = science.getSchedule() - maxSchedule;
				schedule = schedule < 0 ? 0 : schedule;
				science.setSchedule(schedule);
			}
		}

		DonateScienceRs.Builder builder = DonateScienceRs.newBuilder();
		builder.setScience(PbHelper.createPartySciencePb(science));
		if (resourceId == PartyType.RESOURCE_STONE) {
			partyDonate.setStone(count + 1);
			PlayerDataManager.getInst().modifyStone(resource, -staContribute.getPrice());
			builder.setStone(resource.getStone());
		} else if (resourceId == PartyType.RESOURCE_IRON) {
			partyDonate.setIron(count + 1);
			PlayerDataManager.getInst().modifyIron(resource, -staContribute.getPrice());
			builder.setIron(resource.getIron());
		} else if (resourceId == PartyType.RESOURCE_SILICON) {
			partyDonate.setSilicon(count + 1);
			PlayerDataManager.getInst().modifySilicon(resource, -staContribute.getPrice());
			builder.setSilicon(resource.getSilicon());
		} else if (resourceId == PartyType.RESOURCE_COPPER) {
			partyDonate.setCopper(count + 1);
			PlayerDataManager.getInst().modifyCopper(resource, -staContribute.getPrice());
			builder.setCopper(resource.getCopper());
		} else if (resourceId == PartyType.RESOURCE_OIL) {
			partyDonate.setOil(count + 1);
			PlayerDataManager.getInst().modifyOil(resource, -staContribute.getPrice());
			builder.setOil(resource.getOil());
		} else if (resourceId == PartyType.RESOURCE_GOLD) {
			partyDonate.setGold(count + 1);
			PlayerDataManager.getInst().subGold(player.lord, price, GoldCost.PARTY_DONATE_SCIENCE);
			builder.setGold(player.lord.getGold());
		}

		PartyDataManager.doPartyLivelyTask(partyData, member, PartyType.TASK_DONATE);

		TaskManager.getInst().updTask(player, TaskType.COND_PARTY_DONATE, 1, null);

		member.setDonate(member.getDonate() + addBuild);
		member.setWeekDonate(member.getWeekDonate() + addBuild);
		member.setWeekAllDonate(member.getWeekAllDonate() + count);

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DonateScienceRs, builder.build());
	}

	public void seachParty(SeachPartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		String partyName = req.getPartyName();
		PartyRank partyRank = PartyDataManager.getInst().getPartyRankByName(partyName);
		if (partyRank == null) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_NOT_EXIST);
			return;
		}

		SeachPartyRs.Builder builder = SeachPartyRs.newBuilder();
		PartyData partyData = PartyDataManager.getInst().getParty(partyRank.getPartyId());
		int count = PartyDataManager.getInst().getPartyMemberCount(partyRank.getPartyId());
		builder.setPartyRank(PbHelper.createPartyRankPb(partyRank, partyData, count));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SeachPartyRs, builder.build());
	}

	public void applyList(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		ApplyListRs.Builder builder = ApplyListRs.newBuilder();
		if (member.getPartyId() != 0) {
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.ApplyListRs, builder.build());
			return;
		}

		if (member.getApplyList() != null) {
			String applyList = new String(member.getApplyList());
			String[] applyIds = applyList.split("\\|");
			for (String e : applyIds) {
				if (e == null || e.equals("") || e.startsWith("null") || e.endsWith("null")) {
					continue;
				}
				builder.addPartyId(Integer.valueOf(e));
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ApplyListRs, builder.build());
	}

	/**
	 * Function:取消申请
	 * 
	 * @param req
	 * @param handler
	 */
	public void cannlyApplyRq(CannlyApplyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int partyId = req.getPartyId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		if (member.getPartyId() != 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_NOT_EXIST);
			return;
		}
		ApplyListRs.Builder builder = ApplyListRs.newBuilder();
		String applyList = member.getApplyList();
		member.setApplyList(applyList.replace(partyId + "|", ""));
		Iterator<PartyApply> it = partyData.getApplys().values().iterator();
		while (it.hasNext()) {
			if (it.next().getLordId() == playerId) {
				it.remove();
				break;
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ApplyListRs, builder.build());
		// 异步通知军团长,副军团
		Map<Long, PartyApply> applys = partyData.getApplys();
		if (applys != null) {
			PlayerDataManager.getInst().synApplyPartyToPlayer(partyId, applys.size());
		}
	}

	/**
	 * Function：获取军团动态
	 * 
	 * @param req
	 * @param handler
	 */
	public void getPartyTrendRq(GetPartyTrendRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int page = req.getPage();
		int type = req.getType();
		long roleId = playerId;
		Member member = PartyDataManager.getInst().getMemberById(roleId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_NOT_EXIST);
			return;
		}

		GetPartyTrendRs.Builder builder = GetPartyTrendRs.newBuilder();
		List<Trend> trendList = partyData.getTrends();
		if (trendList == null || trendList.size() <= 0) {
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyTrendRs, builder.build());
			return;
		}

		int count = 0;
		int pages[] = { page * 20, (page + 1) * 20 };
		ListIterator<Trend> it = trendList.listIterator(trendList.size());
		while (it.hasPrevious()) {
			Trend trend = it.previous();
			int trendId = trend.getTrendId();
			ConfPartyTrend staticTrend = ConfPartyTrendProvider.getInst().getConfigById(trendId);
			if (staticTrend == null || staticTrend.getType() != type) {
				continue;
			}
			if (count >= pages[0]) {
				List<TrendParam> manList = new ArrayList<TrendParam>();
				List<TrendParam> trendParamList = getTrendParam(partyData, trend);
				if (trendParamList == null) {
					continue;
				}
				for (TrendParam e : trendParamList) {
					TrendParam trendParam = new TrendParam();
					trendParam.setContent(e.getContent());
					if (e.getMan() != null) {
						trendParam.setMan(e.getMan());
					}
					manList.add(trendParam);
				}
				builder.addTrend(PbHelper.createTrendPb(trend, manList));
			}
			count++;
			if (count >= pages[1]) {
				break;
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyTrendRs, builder.build());
	}

	/**
	 * Function:设置公告
	 * 
	 * @param req
	 * @param handler
	 */
	public void sloganParty(SloganPartyRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int type = req.getType();
		String slogan = req.getSlogan();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}
		if (member.getJob() < PartyType.LEGATUS_CP) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		if (slogan == null || slogan.length() >= 30) {
			// handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		slogan = EmojiHelper.filterEmoji(slogan);

		// if (EmojiHelper.containsEmoji(slogan)) {
		// //handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
		// return;
		// }

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (type == 1) {
			partyData.setInnerSlogan(slogan);
		} else {
			partyData.setSlogan(slogan);
		}

		SloganPartyRs.Builder builder = SloganPartyRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SloganPartyRs, builder.build());
	}

	/**
	 * Function：军团成员主动升职，抢军团长，抢副团长
	 * 
	 * @param req
	 * @param handler
	 */
	public void upMemberJob(UpMemberJobRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int job = req.getJob();
		if (job != PartyType.LEGATUS && job != PartyType.LEGATUS_CP) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		boolean flag = upJob(partyData, member, job);
		if (!flag) {
			// handler.sendErrorMsgToPlayer(GameError.UP_JOB_FAIL);
			return;
		}

		UpMemberJobRs.Builder builder = UpMemberJobRs.newBuilder();
		builder.setJob(member.getJob());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UpMemberJobRs, builder.build());
	}

	/**
	 * Function：对帮主和副帮主职位变动需要调用此方法
	 * 
	 * @param partyData
	 * @param member
	 * @param job
	 * @return
	 */
	private boolean upJob(PartyData partyData, Member member, int job) {
		List<Member> memberList = PartyDataManager.getInst().getMemberList(member.getPartyId());
		if (memberList == null || memberList.size() == 0) {
			return false;
		}

		int flag = 0;
		Iterator<Member> it = memberList.iterator();
		Date now = new Date();
		while (it.hasNext()) {
			Member next = it.next();
			if (next.getJob() == job) {
				Player player = PlayerDataManager.getInst().getPlayer(next.getLordId());
				Date loginDate = DateUtil.parseDate(player.lord.getOnTime());
				int dayiy = DateUtil.dayiy(loginDate, now);
				if (dayiy < 16) {
					flag++;
				} else {// 如果有帮主或副帮主15天未登陆,则降为普通帮众
					next.setJob(PartyType.COMMON);
				}
			}
		}

		if (job == PartyType.LEGATUS && flag == 0) {
			Player player = PlayerDataManager.getInst().getPlayer(member.getLordId());
			partyData.setLegatusName(player.lord.getNick());
			member.setJob(PartyType.LEGATUS);
			ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_LEADER, player.lord.getNick()), partyData.getPartyId());
			return true;
		} else if (job == PartyType.LEGATUS_CP && flag < 2) {
			member.setJob(PartyType.LEGATUS_CP);
			Player player = PlayerDataManager.getInst().getPlayer(member.getLordId());
			ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_VICE_LEADER, player.lord.getNick()), partyData.getPartyId());
			return true;
		}

		return false;
	}

	/**
	 * Function：清理,踢出帮派成员
	 * 
	 * @param req
	 * @param handler
	 */
	public void cleanMemberRq(CleanMemberRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		long lordId = req.getLordId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (member.getJob() < PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		int partyId = member.getPartyId();
		Member cleanMember = PartyDataManager.getInst().getMemberById(lordId);
		if (cleanMember == null || cleanMember.getPartyId() != partyId) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int count = PartyDataManager.getInst().getPartyMemberCount(partyId);
		if (count <= 1) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (PartyDataManager.getInst().inWar(cleanMember)) {
			// handler.sendErrorMsgToPlayer(GameError.IN_WAR);
			return;
		}

		Player cleanPlayer = PlayerDataManager.getInst().getPlayer(cleanMember.getLordId());
		// PartyData party =
		// PartyDataManager.getInst().getPartyByLordId(cleanPlayer.roleId);
		// if (party != null) {
		// party.setScore(party.getScore() - cleanPlayer.seniorScore);
		// mineDataManager.setPartyScoreRank(party);
		// }

		PartyDataManager.getInst().quitParty(partyId, cleanMember);

		String lordId1 = String.valueOf(lordId);
		String lordIdJ = String.valueOf(playerId);
		PartyDataManager.getInst().addPartyTrend(partyId, 3, lordId1, lordIdJ);

		MailManager.getInst().sendNormalMail(cleanPlayer, MailType.MOLD_CLEAN_MEMBER, DateUtil.getSecondTime(), player.lord.getNick());
		PlayerDataManager.getInst().synPartyOutToPlayer(cleanPlayer, partyId);

		CleanMemberRs.Builder builder = CleanMemberRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CleanMemberRs, builder.build());

//		worldService.retreatAllGuard(cleanPlayer);

		ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_TICK, cleanPlayer.lord.getNick(), player.lord.getNick()), partyId);
	}

	/**
	 * Function：军团长让位
	 * 
	 * @param req
	 * @param handler
	 */
	public void concedeJobRq(ConcedeJobRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		long lordId = req.getLordId();
		Player mplayer = PlayerDataManager.getInst().getPlayer(lordId);
		if (mplayer == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (member.getJob() != PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int partyId = member.getPartyId();
		Member concedeMember = PartyDataManager.getInst().getMemberById(lordId);
		if (concedeMember == null || concedeMember.getPartyId() != partyId) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_NOT_EXIST);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(concedeMember.getLordId());
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		String legatusName = player.lord.getNick();
		member.setJob(PartyType.COMMON);
		concedeMember.setJob(PartyType.LEGATUS);
		partyData.setLegatusName(legatusName);

		MailManager.getInst().sendMailToParty(partyId, MailType.MOLD_CONCEDE, mplayer.lord.getNick(), legatusName);

		CleanMemberRs.Builder builder = CleanMemberRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CleanMemberRs, builder.build());
		ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_LEADER, player.lord.getNick()), partyId);
	}

	/**
	 * 判断军团名是否存在 Method: isExistPartyName @Description: TODO @param
	 * partyName @return @return boolean @throws
	 */
	public boolean isExistPartyName(String partyName) {
		return PartyDataManager.getInst().isExistPartyName(partyName);
	}

	/**
	 * Method: 军团改名 @Description: TODO @param player @param param @return
	 * void @throws
	 */
	public void rename(int partyId, String partyName, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {

		// 改名
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		PartyDataManager.getInst().rename(partyData, partyName);

		// 发送邮件
		MailManager.getInst().sendMailToParty(partyId, MailType.MOLD_PARTY_NAME_CHAGE, partyName);

		// 发送帮派聊天信息
		ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_NAME_CHANGE, partyName), partyId);
	}

	/**
	 * Function：任命职位
	 * 
	 * @param req
	 * @param handler
	 */
	public void setMemberJobRq(SetMemberJobRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		long lordId = req.getLordId();
		int job = req.getJob();
		if (job != PartyType.COMMON && job != PartyType.LEGATUS_CP && job != PartyType.JOB1 && job != PartyType.JOB2 && job != PartyType.JOB3 && job != PartyType.JOB4) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}

		if (member.getJob() != PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		int partyId = member.getPartyId();
		Member setMember = PartyDataManager.getInst().getMemberById(lordId);
		if (setMember == null || setMember.getPartyId() != partyId) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		if (job == PartyType.LEGATUS_CP) {
			int count = PartyDataManager.getInst().getMemberJobCount(partyId, job);
			if (count < 2) {
				setMember.setJob(job);
				PartyDataManager.getInst().addPartyTrend(partyId, 6, String.valueOf(lordId));

				Player player = PlayerDataManager.getInst().getPlayer(lordId);
				ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_VICE_LEADER, player.lord.getNick()), setMember.getPartyId());
			}
		} else if (job == PartyType.JOB1 || job == PartyType.JOB2 || job == PartyType.JOB3 || job == PartyType.JOB4) {
			int count = PartyDataManager.getInst().getMemberJobCount(partyId, job);
			if (count < 3) {
				setMember.setJob(job);
				PartyDataManager.getInst().addPartyTrend(partyId, 4, String.valueOf(lordId), String.valueOf(job));
				Player player = PlayerDataManager.getInst().getPlayer(lordId);
				ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_JOB, player.lord.getNick(), String.valueOf(job)), setMember.getPartyId());
			}
		} else {
			setMember.setJob(job);
			PartyDataManager.getInst().addPartyTrend(partyId, 4, String.valueOf(lordId), String.valueOf(job));
		}
		SetMemberJobRs.Builder builder = SetMemberJobRs.newBuilder();
		builder.setJob(setMember.getJob());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SetMemberJobRs, builder.build());
	}

	/**
	 * Function：军团职位情况
	 * 
	 * @param handler
	 */
	public void partyJobCount(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}
		int partyId = member.getPartyId();
		int cpcount = PartyDataManager.getInst().getMemberJobCount(partyId, PartyType.LEGATUS_CP);
		int countJob1 = PartyDataManager.getInst().getMemberJobCount(partyId, PartyType.JOB1);
		int countJob2 = PartyDataManager.getInst().getMemberJobCount(partyId, PartyType.JOB2);
		int countJob3 = PartyDataManager.getInst().getMemberJobCount(partyId, PartyType.JOB3);
		int countJob4 = PartyDataManager.getInst().getMemberJobCount(partyId, PartyType.JOB4);
		PartyJobCountRs.Builder builder = PartyJobCountRs.newBuilder();
		builder.setJob1(countJob1);
		builder.setJob2(countJob2);
		builder.setJob3(countJob3);
		builder.setJob4(countJob4);
		builder.setCpLegatus(cpcount);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyJobCountRs, builder.build());
	}

	/**
	 * Function：军团申请条件编辑
	 * 
	 * @param handler
	 */
	public void partyApplyEditRq(PartyApplyEditRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int applyType = req.getApplyType();
		int applyLv = req.getApplyLv();
		long fight = req.getFight();
		String slogan = req.getSlogan();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member != null && member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
			return;
		}
		if (member.getJob() != PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		if (slogan == null || slogan.length() >= 80) {
			// handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		// if (EmojiHelper.containsEmoji(slogan)) {
		// //handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
		// return;
		// }

		slogan = EmojiHelper.filterEmoji(slogan);

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		partyData.setApply(applyType);
		partyData.setApplyLv(applyLv);
		partyData.setApplyFight(fight);
		partyData.setSlogan(slogan);
		PartyApplyEditRs.Builder builder = PartyApplyEditRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyApplyEditRs, builder.build());
	}

	/**
	 * Function: 获取军团副本信息
	 * 
	 * @param handler
	 */
	public void getPartyCombat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		PartyDataManager.getInst().refreshPartyData(partyData);
		PartyDataManager.getInst().refreshMember(member);

		GetPartyCombatRs.Builder builder = GetPartyCombatRs.newBuilder();
		Map<Integer, PartyCombat> pctMap = partyData.getPartyCombats();
		Iterator<PartyCombat> it = pctMap.values().iterator();
		while (it.hasNext()) {
			PartyCombat partyCombat = (PartyCombat) it.next();
			builder.addPartyCombat(PbHelper.createPartyCombatInfoPb(partyCombat));
		}

		int count = 5 - member.getCombatCount();
		count = count < 0 ? 0 : count;

		builder.setCount(count);
		builder.addAllGetAward(member.getCombatIds());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyCombatRs, builder.build());
		return;
	}

	public void ptcForm(int combatId, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		ConfPartyCombat staticPartyCombat = ConfPartyCombatProvider.getInst().getConfigById(combatId);
		if (staticPartyCombat == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		PartyDataManager.getInst().refreshPartyData(partyData);
		PartyDataManager.getInst().refreshMember(member);

		PtcFormRs.Builder builder = PtcFormRs.newBuilder();

		Map<Integer, PartyCombat> pctMap = partyData.getPartyCombats();
		PartyCombat partyCombat = pctMap.get(combatId);
		if (partyCombat == null) {
			partyCombat = createPartyCombat(staticPartyCombat);
			pctMap.put(combatId, partyCombat);
		} else {
			if (partyCombat.getSchedule() >= 100) {
				builder.setState(1);
				MsgHelper.sendResponse(ctx, playerId, ResponseCode.PtcFormRs, builder.build());
				return;
			}
		}

		builder.setState(0);
		builder.setForm(PbHelper.createFormPb(partyCombat.getForm()));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PtcFormRs, builder.build());
	}

	private PartyCombat createPartyCombat(ConfPartyCombat staticPartyCombat) {
		PartyCombat partyCombat = new PartyCombat(staticPartyCombat.getCombatId(), 0);
		partyCombat.setForm(PbHelper.createForm(staticPartyCombat.getForm()));
		return partyCombat;
	}

	private void haustNpcTank(Form form, Fighter fighter) {
		int killed = 0;
		for (int i = 0; i < fighter.forces.length; i++) {
			Force force = fighter.forces[i];
			if (force != null) {
				killed = force.killed;
				if (killed > 0) {
					form.c[i] = form.c[i] - killed;
				}
			}
		}
	}

	private int calcSchedule(Form form, ConfPartyCombat staticPartyCombat) {
		int cur = form.c[0] + form.c[1] + form.c[2] + form.c[3] + form.c[4] + form.c[5];
		int total = staticPartyCombat.getTotalTank();
		return (int) ((total - cur) * 100.0f / total);
	}

	/**
	 * Function： 打军团副本
	 * 
	 * @param req
	 * @param handler
	 */
	public void doPartyCombat(DoPartyCombatRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (member.getCombatCount() >= 5) {
			// handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int combatId = req.getCombatId();
		ConfPartyCombat staticPct = ConfPartyCombatProvider.getInst().getConfigById(combatId);
		if (staticPct == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		Map<Integer, PartyCombat> pctMap = partyData.getPartyCombats();
		PartyCombat partyCombat = pctMap.get(combatId);
		if (partyCombat == null) {
			// handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (partyCombat.getSchedule() >= 100) {
			// handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS);
			return;
		}

		CommonPb.Form form = req.getForm();
		Form attackForm = PbHelper.createForm(form);
		ConfHero staticHero = null;
		if (attackForm.getCommander() > 0) {
			Hero hero = player.heros.get(attackForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
				// handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
				// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
				// handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);

		if (!PlayerDataManager.getInst().checkTank(player, attackForm, maxTankCount)) {
			// handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		member.setCombatCount(member.getCombatCount() + 1);

		Fighter attacker = FightService.getInst().createFighter(player, attackForm, 1);
		Fighter npc = FightService.getInst().createFighter(partyCombat.getForm(), staticPct);

		FightLogic fightLogic = new FightLogic(attacker, npc, FirstActType.ATTACKER, true);
		fightLogic.packForm(attackForm, partyCombat.getForm());

		fightLogic.fight();

		haustNpcTank(partyCombat.getForm(), npc);

		DoPartyCombatRs.Builder builder = DoPartyCombatRs.newBuilder();
		CommonPb.Record record = fightLogic.generateRecord();
		int result = -1;
		if (fightLogic.getWinState() == 1) {
			result = fightLogic.estimateStar();
		}

		builder.setResult(result);
		builder.setRecord(record);

		int originSchedule = partyCombat.getSchedule();
		if (result > 0) {
			partyCombat.setSchedule(100);
			List<List<Integer>> awardList = staticPct.getLastAward();
			for (List<Integer> e : awardList) {
				int type = e.get(0);
				int id = e.get(1);
				int count = e.get(2);
				int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.PARTY_COMBAT);
				if (type == AwardType.PARTY_BUILD) {
					builder.setBuild(count);
				} else {
					builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
				}
			}
			PartyDataManager.getInst().addPartyTrend(partyId, 10, String.valueOf(playerId), staticPct.getName());
		} else {
			int schedule = calcSchedule(partyCombat.getForm(), staticPct);
			if (schedule == 100) {
				schedule = 99;
			}
			partyCombat.setSchedule(schedule);
		}

		List<List<Integer>> awardList = staticPct.getDrop();
		for (List<Integer> e : awardList) {
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.PARTY_COMBAT);
			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
		}

		float factor = (originSchedule - partyCombat.getSchedule()) / 100.0f;
		if (factor < 0.1f) {
			factor = 0.1f;
		}

		int exp = (int) (staticPct.getExp() * FightService.getInst().effectExpAdd(player) * factor);
		PlayerDataManager.getInst().addExp(player.lord, exp);

		TaskManager.getInst().updTask(player, TaskType.COND_PARTY_COMBAT, 1, null);
		PartyDataManager.doPartyLivelyTask(partyData, member, PartyType.TASK_COMBAT);

		builder.setExp(exp);
		builder.setPartyCombat(PbHelper.createPartyCombatPb(partyCombat));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoPartyCombatRs, builder.build());
		if (result > 0) {
			ChatService.getInst().sendPartyChat(ChatService.getInst().createSysChat(SysChatId.PARTY_COMBAT, player.lord.getNick(), String.valueOf(combatId)), partyId);
		}
	}

	/**
	 * Function：军团副本奖励箱子领取
	 * 
	 * @param req
	 * @param handler
	 */
	public void partyctAward(PartyctAwardRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int partyId = member.getPartyId();
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int combatId = req.getCombatId();
		Map<Integer, PartyCombat> pctMap = partyData.getPartyCombats();
		PartyCombat partyCombat = pctMap.get(combatId);
		if (partyCombat == null || partyCombat.getSchedule() != 100) {
			// handler.sendErrorMsgToPlayer(GameError.PARTY_COMBAT_EXIST);
			return;
		}

		Set<Integer> combatIds = member.getCombatIds();
		if (combatIds.contains(combatId)) {
			// handler.sendErrorMsgToPlayer(GameError.AWARD_HAD_GOT);
			return;
		}

		PartyctAwardRs.Builder builder = PartyctAwardRs.newBuilder();
		ConfPartyCombat staticPct = ConfPartyCombatProvider.getInst().getConfigById(combatId);
		if (staticPct == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		int contribute = staticPct.getContribute();
		if (contribute > 0) {
			if (contribute > member.getDonate()) {
				// handler.sendErrorMsgToPlayer(GameError.DONATE_NOT_ENOUGH);
				return;
			}
			member.setDonate(member.getDonate() - contribute);
		}

		combatIds.add(combatId);

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<List<Integer>> box = staticPct.getBox();
		int[] seeds = { 0, 0 };
		for (List<Integer> e : box) {
			if (e.size() < 4) {
				continue;
			}
			seeds[0] += e.get(3);
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (List<Integer> e : box) {
			if (e.size() < 4) {
				continue;
			}
			seeds[1] += e.get(3);
			if (seeds[0] <= seeds[1]) {
				int type = e.get(0);
				int id = e.get(1);
				int count = e.get(2);
				int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.PARTY_COMBAT_BOX);
				builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
				break;
			}
		}

		TaskManager.getInst().updTask(player, TaskType.COND_PARTY_BOX, 1, null);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PartyctAwardRs, builder.build());
	}

	/**
	 * Function：軍團活躍排名
	 * 
	 * @param handler
	 */
	public void getPartyLiveRank(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		int partyId = member.getPartyId();
		if (partyId == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		GetPartyLiveRankRs.Builder builder = GetPartyLiveRankRs.newBuilder();
		List<Member> memberList = PartyDataManager.getInst().getMemberList(partyId);
		Iterator<Member> it = memberList.iterator();
		int today = DateUtil.getToday();
		while (it.hasNext()) {
			Member next = it.next();
			long lordId = next.getLordId();
			Player player = PlayerDataManager.getInst().getPlayer(lordId);
			if (player == null) {
				continue;
			}
			Lord lord = player.lord;
			if (lord == null) {
				continue;
			}
			int live = next.getActivity();
			if (today != next.getRefreshTime()) {
				live = 0;
			}
			builder.addPartyLive(PbHelper.createPartyLivePb(lord, next.getJob(), live));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyLiveRankRs, builder.build());
	}

	/**
	 * Function：军团战事福利列表
	 * 
	 * @param handler
	 */
	public void getPartyAmyPropsRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		int partyId = member.getPartyId();
		if (partyId == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}
		GetPartyAmyPropsRs.Builder builder = GetPartyAmyPropsRs.newBuilder();

		Map<Integer, Prop> amyProps = partyData.getAmyProps();
		Iterator<Prop> it = amyProps.values().iterator();

		while (it.hasNext()) {
			Prop next = it.next();
			if (next.getCount() <= 0) {
				continue;
			}
			builder.addProp(PbHelper.createPropPb(next));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPartyAmyPropsRs, builder.build());
	}

	/**
	 * Function：发放军团战事福利
	 * 
	 * @param handler
	 */
	public void sendPartyAmyPropRq(SendPartyAmyPropRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Member member = PartyDataManager.getInst().getMemberById(playerId);
		if (member == null || member.getPartyId() == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		int partyId = member.getPartyId();
		if (partyId == 0) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		int job = member.getJob();
		if (job != PartyType.LEGATUS) {
			// handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		PartyData partyData = PartyDataManager.getInst().getParty(partyId);
		if (partyData == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		List<Long> idList = req.getSendIdList();
		CommonPb.Prop sendProp = req.getProp();
		int sendCount = idList.size();
		if (sendCount == 0 || sendProp == null || sendProp.getCount() <= 0) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		Map<Integer, Prop> amyProps = partyData.getAmyProps();

		int propId = sendProp.getPropId();
		int propCount = sendProp.getCount();

		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Prop prop = amyProps.get(propId);
		if (prop == null || propCount * sendCount > prop.getCount()) {
			// handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}

		SendPartyAmyPropRs.Builder builder = SendPartyAmyPropRs.newBuilder();

		List<AwardPB> awardList = new ArrayList<AwardPB>();
		awardList.add(PbHelper.createAwardPb(AwardType.PROP, propId, propCount));

		String propsName = staticProp.getPropName() + "*" + propCount;
		sendCount = 0;
		for (Long lordId : idList) {
			Member partyMember = PartyDataManager.getInst().getMemberById(lordId.longValue());
			if (partyMember == null || partyMember.getPartyId() != partyId) {
				continue;
			}
			Player role = PlayerDataManager.getInst().getPlayer(lordId.longValue());
			if (role == null) {
				continue;
			}
			sendCount += propCount;

			PartyDataManager.getInst().addPartyTrend(partyId, 11, propsName, String.valueOf(lordId.longValue()));
			MailManager.getInst().sendAttachMail(role, awardList, MailType.MOLD_AMY_PROP, DateUtil.getSecondTime(), staticProp.getPropName());
		}

		int count = prop.getCount() - sendCount < 0 ? 0 : prop.getCount() - sendCount;
		prop.setCount(count);
		builder.setProp(PbHelper.createPropPb(prop));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SendPartyAmyPropRs, builder.build());
	}

	public void useAmyPropRq(UseAmyPropRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			// handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		CommonPb.Prop sendProp = req.getProp();

		int propId = sendProp.getPropId();
		int propCount = sendProp.getCount();

		Prop prop = player.props.get(propId);
		if (prop == null || propCount > prop.getCount()) {
			// handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}
		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
			// handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticProp.getEffectType() != 7) {
			// handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		// 消耗箱子
		int count = prop.getCount() - propCount < 0 ? 0 : prop.getCount() - propCount;
		prop.setCount(count);

		// 取最大战车工厂等级
		int lv = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_1, player.building);
		int lv1 = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_2, player.building);
		lv = lv > lv1 ? lv : lv1;

		// 通过战车工厂等级获取掉落ID,进行掉落
		int awardId = 0;
		List<List<Integer>> effectValue = staticProp.getEffectValue();
		for (List<Integer> e : effectValue) {
			int beginLv = e.get(0);
			int endLv = e.get(1);
			if (lv >= beginLv && lv <= endLv) {
				awardId = e.get(2);
				break;
			}
		}
		List<List<Integer>> awardList = ConfAwardProvider.getInst().getAwards(awardId);
		UseAmyPropRs.Builder builder = UseAmyPropRs.newBuilder();

		for (List<Integer> e : awardList) {
			if (e.size() < 3) {
				continue;
			}
			int type = e.get(0);
			int id = e.get(1);
			int itemCount = e.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, itemCount, AwardFrom.USE_AMY_PROP);
			builder.addAward(PbHelper.createAwardPb(type, id, itemCount, keyId));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UseAmyPropRs, builder.build());
	}

	private Man createMan(long lordId) {
		Player player = PlayerDataManager.getInst().getPlayer(lordId);
		if (player == null) {
			return new Man();
		}
		Lord lord = player.lord;
		if (lord == null) {
			return new Man();
		}
		Man man = new Man(lord.getLordId(), lord.getSex(), lord.getNick(), lord.getPortrait(), lord.getLevel());
		man.setPos(lord.getPos());
		man.setRanks(lord.getRanks());
		man.setFight(lord.getFight());
		man.setVip(lord.getVip());
		man.setHonour(lord.getHonour());
		man.setPros(lord.getPros());
		man.setProsMax(lord.getProsMax());
		man.setPartyName(PartyDataManager.getInst().getPartyNameByLordId(lord.getLordId()));
		return man;
	}

	public List<TrendParam> getTrendParam(PartyData partyData, Trend trend) {
		List<TrendParam> list = new ArrayList<TrendParam>();
		int trendId = trend.getTrendId();
		switch (trendId) {
		case 1:// 成员加入{s}
		case 2: {// 成员退出{s}
			TrendParam param = new TrendParam();
			long lordId = Long.valueOf(trend.getParam()[0]);
			Man man = createMan(lordId);
			param.setMan(createMan(lordId));
			param.setContent(man.getNick());
			list.add(param);
			break;
		}
		case 3:// 踢出军团{s,s}
		case 4: {// 被任命职位{s,s}
			long lordId1 = Long.valueOf(trend.getParam()[0]);
			TrendParam param = new TrendParam();
			Man man1 = createMan(lordId1);
			param.setMan(man1);
			param.setContent(man1.getNick());
			list.add(param);

			int job = Integer.valueOf(trend.getParam()[1]);
			TrendParam param2 = new TrendParam();
			if (job == PartyType.JOB1) {
				param2.setContent(partyData.getJobName1());
			} else if (job == PartyType.JOB2) {
				param2.setContent(partyData.getJobName2());
			} else if (job == PartyType.JOB3) {
				param2.setContent(partyData.getJobName3());
			} else if (job == PartyType.JOB4) {
				param2.setContent(partyData.getJobName4());
			} else if (job == PartyType.COMMON) {
				param2.setContent("成员");
			}

			list.add(param2);
			break;
		}
		case 5:// 被任命军团长{s}
		case 6: {// 被任命副军团长{s}
			TrendParam param = new TrendParam();
			long lordId = Long.valueOf(trend.getParam()[0]);
			Man man = createMan(lordId);
			param.setMan(man);
			param.setContent(man.getNick());
			list.add(param);
			break;
		}
		case 7:// 军团科技大厅等级提升{s}
		case 8:// 军团等级提升{s}
		case 9: // 福利院等级提升{s}
		case 14:// 百团大战,本次百团混战获得第|%s|名,奖励已发放到福利院！
		case 15:// 本次百团混战获得第1名,已激活军团采集加速BUFF
		{
			TrendParam param = new TrendParam();
			param.setContent(trend.getParam()[0]);
			list.add(param);
			break;
		}
		case 10: {// 消灭军团副本关卡{s,s}
			TrendParam param = new TrendParam();
			long lordId = Long.valueOf(trend.getParam()[0]);
			Man man = createMan(lordId);
			param.setMan(man);
			param.setContent(man.getNick());
			list.add(param);

			TrendParam param2 = new TrendParam();
			param2.setContent(trend.getParam()[1]);
			list.add(param2);
			break;
		}
		case 11: {// 福利分配{s,s}
			TrendParam param = new TrendParam();
			param.setContent(trend.getParam()[0]);
			list.add(param);

			long lordId2 = Long.valueOf(trend.getParam()[1]);
			TrendParam param2 = new TrendParam();
			Man man2 = createMan(lordId2);
			param2.setMan(man2);
			param2.setContent(man2.getNick());
			list.add(param2);
			break;
		}
		case 12: {// 防守成功{s,s,s}
			if (trend.getParam().length != 3) {// 错误数据则不发送
				return null;
			}
			long lordId1 = Long.valueOf(trend.getParam()[0]);
			TrendParam param = new TrendParam();
			Man man1 = createMan(lordId1);
			param.setMan(man1);
			param.setContent(man1.getNick());
			list.add(param);

			TrendParam param2 = new TrendParam();
			param2.setContent(trend.getParam()[1]);
			list.add(param2);

			long lordId3 = Long.valueOf(trend.getParam()[2]);
			TrendParam param3 = new TrendParam();
			Man man3 = createMan(lordId3);
			param3.setMan(man3);
			param3.setContent(man3.getNick());
			list.add(param3);
			break;
		}
		case 13: {// 防守成功{s,s,s,s}
			if (trend.getParam().length != 4) {
				return null;
			}
			long lordId1 = Long.valueOf(trend.getParam()[0]);
			TrendParam param = new TrendParam();
			Man man1 = createMan(lordId1);
			param.setMan(man1);
			param.setContent(man1.getNick());
			list.add(param);

			TrendParam param2 = new TrendParam();
			param2.setContent(trend.getParam()[1]);
			list.add(param2);

			long lordId3 = Long.valueOf(trend.getParam()[2]);
			TrendParam param3 = new TrendParam();
			Man man3 = createMan(lordId3);
			param3.setMan(man3);
			param3.setContent(man3.getNick());
			list.add(param3);

			TrendParam param4 = new TrendParam();
			param4.setContent(trend.getParam()[3]);
			list.add(param4);
			break;
		}
		default:
			break;
		}
		return list;
	}

	/**
	 * 计算帮派战斗力，排名
	 */
	public void partyTimerLogic() {
		List<PartyRank> partyRanks = PartyDataManager.getInst().getPartyRanks();
		Collections.sort(partyRanks, new CompareParty());
		Iterator<PartyRank> it = partyRanks.iterator();
		int rank = 1;
		while (it.hasNext()) {
			PartyRank next = it.next();
			next.setRank(rank++);
		}
	}

	// public long getPartyFight(int partyId) {
	// int fight = 0;
	// List<Member> memberList = PartyDataManager.getInst().getMemberList(partyId);
	// if (memberList == null) {
	// return fight;
	// }
	// Iterator<Member> it = memberList.iterator();
	// while (it.hasNext()) {
	// long playerId = it.next().getLordId();
	// Player player = PlayerDataManager.getInst().getPlayer(playerId);
	// if (player != null) {
	// fight += player.lord.getFight();
	// }
	// }
	// return fight;
	// }

	public long calcPartyFight(PartyData partyData) {
		List<Member> list = PartyDataManager.getInst().getMemberList(partyData.getPartyId());
		if (list == null) {
			return 0;
		}
		long fight = 0;
		Member member;
		Player player;
		for (int i = 0; i < list.size(); i++) {
			member = list.get(i);
			player = PlayerDataManager.getInst().getPlayer(member.getLordId());
			if (player == null) {
				continue;
			}
			Lord lord = player.lord;
			if (lord == null) {
				continue;
			}
			fight += player.lord.getFight();
		}
		return fight;
	}

	/**
	 * 
	 * @param lordId
	 * @param applyList
	 * @param partyId0时全取消
	 */
	public void resetApply(long lordId, String applyList, int partyId) {
		try {
			String[] applyIds = applyList.split("\\|");
			for (String applyPartyId : applyIds) {
				if (applyPartyId.equals("")) {
					continue;
				}
				int applyId = Integer.parseInt(applyPartyId);
				if (partyId == 0 || applyId == partyId) {
					PartyData partyData = PartyDataManager.getInst().getParty(applyId);
					if (partyData == null) {
						continue;
					}
					Map<Long, PartyApply> applyMap = partyData.getApplys();
					if (applyMap != null && applyMap.containsKey(lordId)) {
						applyMap.remove(lordId);
					}
				}
			}
		} catch (Exception e) {
		}
	}

}
