package network.handler.module.mxw;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import actor.CenterActorManager;
import config.bean.ConfChapter;
import config.bean.ConfSetting;
import config.bean.ConfShop;
import config.bean.ConfSign;
import config.provider.ConfChapterProvider;
import config.provider.ConfSettingProvider;
import config.provider.ConfShopProvider;
import config.provider.ConfSignProvider;
import data.bean.Bag;
import data.bean.Chapter;
import data.bean.ChapterBox;
import data.bean.Sign;
import data.provider.BagProvider;
import data.provider.ChapterBoxProvider;
import data.provider.ChapterProvider;
import data.provider.SignProvider;
import define.GameType;
import define.MxwPropId;
import define.ShopEnum;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.ChapterManager;
import mxw.PlayerManager;
import mxw.RankManager;
import mxw.UserData;
import mxw.UserRank;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.MxwPb.ChapterBoxReq;
import pb.MxwPb.ChapterBoxRsp;
import pb.MxwPb.ChapterReq;
import pb.MxwPb.ChapterRsp;
import pb.MxwPb.ConsumePropReq;
import pb.MxwPb.ConsumePropRsp;
import pb.MxwPb.GetPropListReq;
import pb.MxwPb.GetPropListRsp;
import pb.MxwPb.GetSignReq;
import pb.MxwPb.GetSignRsp;
import pb.MxwPb.GotPropReq;
import pb.MxwPb.GotPropRsp;
import pb.MxwPb.PropPB;
import pb.MxwPb.RankReq;
import pb.MxwPb.RankRsp;
import pb.MxwPb.SectionDetailReq;
import pb.MxwPb.SectionDetailRsp;
import pb.MxwPb.SectionEndReq;
import pb.MxwPb.SectionEndRsp;
import pb.MxwPb.SectionRankPB;
import pb.MxwPb.SectionStartReq;
import pb.MxwPb.SectionStartRsp;
import pb.MxwPb.ShopBuyReq;
import pb.MxwPb.ShopBuyRsp;
import pb.MxwPb.ShopReq;
import pb.MxwPb.ShopRsp;
import pb.MxwPb.ShopUseReq;
import pb.MxwPb.ShopUseRsp;
import pb.MxwPb.SignReq;
import pb.MxwPb.SignRsp;
import pb.MxwPb.UpdateProp;
import pb.MxwPb.UserAllowReq;
import pb.MxwPb.UserAllowRsp;
import pb.MxwPb.UserProps;
import pb.MxwPb.UserProps.Builder;
import pb.MxwPb.UserReq;
import pb.MxwPb.UserRsp;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;
import util.PBHelper;

@Singleton
public class GameMxwModule implements IModuleMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(GameMxwModule.class);

	public static GameMxwModule getInst() {
		return BeanManager.getBean(GameMxwModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
		handler.registerAction(RequestCode.MXW_USER_RQ.getValue(), this::actionUse, UserReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_CHAPTER_RQ.getValue(), this::actionChapter, ChapterReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SECTION_START_RQ.getValue(), this::actionSectionStart, SectionStartReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SECTION_END_RQ.getValue(), this::actionSectionEnd, SectionEndReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_RANK_RQ.getValue(), this::actionRank, RankReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SHOP_RQ.getValue(), this::actionShop, ShopReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SHOP_BUY_RQ.getValue(), this::actionShopBuy, ShopBuyReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_HALL_BOX.getValue(), this::actionHallBox, ChapterBoxReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SHOP_USE.getValue(), this::actionShopUse, ShopUseReq.getDefaultInstance());
		// handler.registerAction(RequestCode.MXW_LUCK.getValue(),
		// this::actionLuck, LuckReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_DETAIL.getValue(), this::actionSectionDetail, SectionDetailReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_GOT_PROP.getValue(), this::actionGotPropReq, GotPropReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_CONSUME_PROP.getValue(), this::actionConsumePropReq, ConsumePropReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_GET_PROP_LIST.getValue(), this::actionGetPropListReq, GetPropListReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_UPDATE_PROP.getValue(), this::actionUpdataReq, UpdateProp.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_USER_ALLOW.getValue(), this::actionUserAllowReq, UserAllowReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_GET_SIGN.getValue(), this::actionGetSignReq, GetSignReq.getDefaultInstance());
		handler.registerAction(RequestCode.MXW_SIGN.getValue(), this::actionSignReq, SignReq.getDefaultInstance());
	}

	private void actionUse(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		UserReq req = msg.get();
		logger.info("actionUse playerId:{} game:{}", playerId, player.getGame());
		UserRsp.Builder builder = UserRsp.newBuilder();
		builder.setUserId(playerId);
		builder.setUserName(player.getUser().getUserName() == null ? "" : player.getUser().getUserName());
		builder.setPortrait(BagProvider.getInst().getPortrait(playerId));
		builder.setHeadFrame(player.getUser().getHeadFrame());
		builder.setGold(player.getUser().getGold());
		builder.setCoin(player.getUser().getCoin());
		builder.setPower(player.getUser().getPower());
		builder.setChapter(ChapterManager.getInst().getChapterId(playerId));
		ConfSetting setting = ConfSettingProvider.getInst().get("max_chapter");
		builder.setMaxChapter(setting.getValue().getInt("chapterId"));
		Map<Integer, Bag> props = BagProvider.getInst().getPropList(playerId);
		for (Entry<Integer, Bag> item : props.entrySet()) {
			Builder itemBuilder = UserProps.newBuilder().setPropsId(item.getKey()).setPropsNum(item.getValue().getPropNum());
			builder.addWeapon(itemBuilder);
		}

		// 血瓶
		if (!props.containsKey(MxwPropId.BLOOD_BOX)) {
			Builder itemBuilder = UserProps.newBuilder().setPropsId(MxwPropId.BLOOD_BOX).setPropsNum(0);
			builder.addWeapon(itemBuilder);
		}

		// 武器包
		if (!props.containsKey(MxwPropId.WEAPON_BAG)) {
			Builder itemBuilder = UserProps.newBuilder().setPropsId(MxwPropId.WEAPON_BAG).setPropsNum(0);
			builder.addWeapon(itemBuilder);
		}

		logger.info("UserRsp playerId:{} chapter:{}", playerId, ChapterManager.getInst().getChapterId(playerId));

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_USER, builder.build());
	}

	private void actionChapter(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		ChapterReq req = msg.get();
		logger.info("actionChapter playerId:{} game:{} chapter:{}", playerId, player.getGame(), req.getChapter());

		int chapterId = req.getChapter();

		ChapterRsp.Builder builder = ChapterRsp.newBuilder();
		builder.setChapter(req.getChapter());

		if (player.getGame() == 2) {
			builder.setChapter(0);
			logger.info("actionChapter playerId:{} game:{} chatper:{}", playerId, player.getGame(), 0);
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_CHAPTER, builder.build());
			return;
		}

		/**** 宝箱信息 ****/
		ChapterBox chapBox = ChapterBoxProvider.getInst().getById(playerId, chapterId);
		if (chapBox == null) {
			builder.addBoxOpen(false);
			builder.addBoxOpen(false);
			builder.addBoxOpen(false);
		} else {
			builder.addBoxOpen(chapBox.getBox1() == 1);
			builder.addBoxOpen(chapBox.getBox2() == 1);
			builder.addBoxOpen(chapBox.getBox3() == 1);
		}

		/**** 关卡信息 ****/
		List<ConfChapter> sections = ConfChapterProvider.getInst().getChapter(player.getUser().getGameType(), req.getChapter());
		sections.forEach(e -> {
			Chapter rank = ChapterProvider.getInst().getRecord(playerId, e.getId());
			if (rank != null) {
				builder.addSections(PBHelper.createSectionPB(e.getSectionId(), rank.getPassTime() > 0, rank.getStarLv()));
			} else {
				builder.addSections(PBHelper.createSectionPB(e.getSectionId(), false, 0));
			}
		});
		logger.info("actionChapter playerId:{} game:{} chatper:{}", playerId, player.getGame(), chapterId);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_CHAPTER, builder.build());
	}

	private void actionSectionStart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SectionStartReq req = msg.get();
		logger.info("actionSectionStart playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		if (player.getGame() == 1) {
			ConfChapter conf = ConfChapterProvider.getInst().getSection(player.getUser().getGameType(), req.getChapter(), req.getSection());
			if (conf == null) {
				logger.info("actionSectionStart conf isNull");
				return;
			}
			player.setCombatId(conf.getId());
		} else {
			player.setCombatId(65);
		}

		if (player.getUser().getPower() < 1) {
			logger.info("power not enough");
			return;
		}

		player.reducePower(1);// 扣除1点体力
		SectionStartRsp.Builder builder = SectionStartRsp.newBuilder();
		builder.setGold(player.getUser().getGold());
		builder.setCoin(player.getUser().getCoin());
		builder.setRoldId(BagProvider.getInst().getPortrait(playerId));
		logger.info("actionSectionRsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SECTION_START, builder.build());
	}

	private void actionSectionEnd(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SectionEndReq req = msg.get();
		logger.info("actionSectionEnd playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		if (player.getGame() == 2) {// 无尽模式
			SectionEndRsp.Builder builder = SectionEndRsp.newBuilder();
			builder.setPassTime(req.getPassTime());
			builder.setEnterNextPass(1);
			logger.info("actionSectionEnd:{}", builder.build());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SECTION_END, builder.build());
			return;
		}

		ConfChapter conf = ConfChapterProvider.getInst().getConfigById(player.getCombatId());
		if (conf == null) {
			logger.info("关卡信息错误 combatId:{}", player.getCombatId());
			return;
		}

		int starLv = req.getStarLv();
		starLv = starLv > 3 ? 3 : starLv;
		long passTime = req.getPassTime();

		int enterNext = 2;
		Chapter chapter = ChapterProvider.getInst().getRecord(playerId, conf.getId());
		if (chapter == null) {
			logger.info("关卡记录不存在 combatId:{}", conf.getId());
		}

		if (passTime > 0 && player.getGame() == GameType.MXW.getVal()) {// 冒險王通关
			ChapterProvider.getInst().updateRecord(playerId, conf.getId(), starLv, passTime, DateUtil.getSecondTime());
			if (conf.getSectionId() != 8) {// 直接新关卡
				enterNext = 1;
				ChapterProvider.getInst().addChapter(playerId, conf.getId() + 1, 0, 0, 0);
			}

			// 检测是否通关了本章节,如果通关了,则开启下一章节
			boolean isOpen = ChapterManager.getInst().isPassChapter(playerId, conf.getChapterId(), conf.getSectionId());
			if (isOpen) {
				enterNext = 1;
				ConfChapter newChapter = ConfChapterProvider.getInst().getSection(player.getGame(), conf.getChapterId() + 1, 1);
				if (newChapter != null) {
					ChapterProvider.getInst().addChapter(playerId, newChapter.getId(), 0, 0, 0);
					logger.info("添加新的章节playerId:{} combatId:{}", playerId, newChapter.getId());
				}
			}
		} else if (passTime > 0 && player.getGame() == GameType.RUN.getVal()) {
			chapter = ChapterProvider.getInst().addChapter(playerId, conf.getId(), starLv, passTime, DateUtil.getSecondTime());
		}

		SectionEndRsp.Builder builder = SectionEndRsp.newBuilder();
		builder.setPassTime(chapter.getPassTime());
		builder.setEnterNextPass(enterNext);

		logger.info("actionSectionEnd:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SECTION_END, builder.build());
	}

	private void actionRank(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		RankReq req = msg.get();
		logger.info("actionRank playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		CenterActorManager.getRankActor().put(() -> {
			RankRsp.Builder builder = RankRsp.newBuilder();
			List<UserRank> rankList = RankManager.getInst().getRankList(player.getUser().getGameType());

			int i = 1;
			int rank = 0;
			int chapterId = 0;
			int sectionId = 0;

			switch (GameType.getByVal(player.getUser().getGameType())) {
			case MXW:
				int starLv = 0;
				for (UserRank e : rankList) {
					UserData user = PlayerManager.getInst().getPlayerById(e.getUserId());
//					if (user.getUser().getPlatProtrait() == null) {// 未授权则直接跳过
//						continue;
//					}
					ConfChapter conf = ConfChapterProvider.getInst().getConfigById(e.getCombatId());
					if (conf == null) {
						continue;
					}
					if (i > 50) {// 不能超过50个
						break;
					}
					if (user != null) {
						if (e.getUserId() == playerId) {
							rank = i;
							chapterId = conf.getChapterId();
							sectionId = conf.getSectionId();
							starLv = e.getStarLv();
						}
						String userName = user.getUser().getUserName() == null ? "" : user.getUser().getUserName();
						String protrait = user.getUser().getPlatProtrait() == null ? "" : user.getUser().getPlatProtrait();
						builder.addRanks(PBHelper.crateRankPB(i++, userName, protrait, conf.getChapterId(), conf.getSectionId(), e.getStarLv()));
					}
				}

				if (rank == 0) {
					Optional<UserRank> opt = rankList.stream().filter(e -> e.getUserId() == playerId).findFirst();
					if (opt.isPresent()) {
						ConfChapter conf = ConfChapterProvider.getInst().getConfigById(opt.get().getCombatId());
						chapterId = conf.getChapterId();
						sectionId = conf.getSectionId();
						starLv = opt.get().getStarLv();
						rank = rankList.indexOf(opt.get()) + 1;
					}
				}

				builder.setMyRank(rank);
				builder.setMyName(player.getUser().getUserName() == null ? "" : player.getUser().getUserName());
				builder.setMyPortrait(player.getUser().getPlatProtrait() == null ? "" : player.getUser().getPlatProtrait());
				builder.setMyProcess(chapterId + "-" + sectionId);
				builder.setMyValue(starLv);

				logger.info("actionRank rsp:{}", builder.build());
				MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_RANK, builder.build());
				break;
			case RUN:
				int distance = 0;
				for (UserRank e : rankList) {
					UserData user = PlayerManager.getInst().getPlayerById(e.getUserId());
//					if (user.getUser().getPlatProtrait() == null) {// 未授权则直接跳过
//						continue;
//					}
					ConfChapter conf = ConfChapterProvider.getInst().getConfigById(e.getCombatId());
					if (conf == null) {
						continue;
					}
					if (i > 50) {// 不能超过50个
						break;
					}
					if (user != null) {
						if (e.getUserId() == playerId) {
							rank = i;
							chapterId = conf.getChapterId();
							sectionId = conf.getSectionId();
							distance = (int) e.getPassTime();
						}
						String userName = user.getUser().getUserName() == null ? "" : user.getUser().getUserName();
						String protrait = user.getUser().getPlatProtrait() == null ? "" : user.getUser().getPlatProtrait();
						builder.addRanks(PBHelper.crateRankPB(i++, userName, protrait, conf.getChapterId(), conf.getSectionId(), (int) e.getPassTime()));
					}
				}
				if (rank == 0) {
					Optional<UserRank> opt = rankList.stream().filter(e -> e.getUserId() == playerId).findFirst();
					if (opt.isPresent()) {
						ConfChapter conf = ConfChapterProvider.getInst().getConfigById(opt.get().getCombatId());
						chapterId = conf.getChapterId();
						sectionId = conf.getSectionId();
						distance = (int) opt.get().getPassTime();
						rank = rankList.indexOf(opt.get()) + 1;
					}
				}

				builder.setMyRank(rank);
				builder.setMyName(player.getUser().getUserName() == null ? "" : player.getUser().getUserName());
				builder.setMyPortrait(player.getUser().getPlatProtrait() == null ? "" : player.getUser().getPlatProtrait());
				builder.setMyProcess(chapterId + "-" + sectionId);
				builder.setMyValue(distance);

				logger.info("actionRank rsp:{}", builder.build());
				MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_RANK, builder.build());
				break;

			default:
				break;
			}
			return null;
		});
	}

	private void actionShop(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ShopReq req = msg.get();
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		logger.info("actionShop playerId:{} req:{}", playerId, req);
		ShopRsp.Builder builder = ShopRsp.newBuilder();
		for (ConfShop e : ConfShopProvider.getInst().getAllConfig()) {
			if (e.getType() != 1) {
				continue;
			}
			Bag bag = BagProvider.getInst().getById(playerId, e.getId());
			if (bag != null) {
				builder.addShops(PBHelper.crateShopPB(e.getId(), e.getItemId(), e.getItemNum(), e.getGold(), e.getCoin(), bag.getState()));
				continue;
			}
			builder.addShops(PBHelper.crateShopPB(e.getId(), e.getItemId(), e.getItemNum(), e.getGold(), e.getCoin(), ShopEnum.UN_HAD.getValue()));
		}
		logger.info("actionShop rsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP, builder.build());
	}

	private void actionShopBuy(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ShopBuyReq req = msg.get();
		logger.info("actionShopBuy playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		ShopBuyRsp.Builder builder = ShopBuyRsp.newBuilder();
		builder.setShopId(req.getShopId());

		// 道具不存在
		if (!ConfShopProvider.getInst().isContain(req.getShopId())) {
			builder.setState(0);
			builder.setGold(player.getUser().getGold());
			builder.setCoin(player.getUser().getCoin());
			builder.setTip("道具不存在");
			builder.setCoinEnough(true);
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP_BUY, builder.build());
			return;
		}

		// 金钱不足,直接给客户端,广告
		ConfShop conf = ConfShopProvider.getInst().getConfigById(req.getShopId());
		if (player.getUser().getGold() < conf.getGold() || player.getUser().getCoin() < conf.getCoin()) {
			builder.setState(0);
			builder.setCoinEnough(false);
			builder.setTip("金币不足");
			builder.setGold(player.getUser().getGold());
			builder.setCoin(player.getUser().getCoin());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP_BUY, builder.build());
			return;
		}

		int propId = req.getShopId();
		int propNum = req.getNum();
		Bag bag = BagProvider.getInst().getById(playerId, propId);
		if (bag != null) {
			builder.setState(0);
			builder.setCoinEnough(false);
			builder.setTip("已拥有");
			builder.setGold(player.getUser().getGold());
			builder.setCoin(player.getUser().getCoin());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP_BUY, builder.build());
			return;
		}

		player.getUser().setGold(player.getUser().getGold() - conf.getGold());
		player.getUser().setCoin(player.getUser().getCoin() - conf.getCoin());

		// 购买角色成功
		BagProvider.getInst().addProp(player, propId, propNum, ShopEnum.HAD.getValue());

		builder.setState(1);
		builder.setCoinEnough(true);
		builder.setGold(player.getUser().getGold());
		builder.setCoin(player.getUser().getCoin());
		logger.info("actionShopBuy rsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP_BUY, builder.build());
	}

	private void actionHallBox(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ChapterBoxReq req = msg.get();
		logger.info("actionHallBox playerId:{} req:{}", playerId, req);
		int chapterId = req.getChapterId();

		ChapterBoxRsp.Builder builder = ChapterBoxRsp.newBuilder();

		UserData player = PlayerManager.getInst().getPlayerById(playerId);

		ChapterBox box = ChapterBoxProvider.getInst().getById(playerId, chapterId);
		if (box == null) {
			box = ChapterBoxProvider.getInst().add(playerId, chapterId, 0, 0, 0);
		}

		int box1 = req.getBoxId() == 1 ? 1 : -1;
		int box2 = req.getBoxId() == 2 ? 1 : -1;
		int box3 = req.getBoxId() == 3 ? 1 : -1;

		long gold = 0;
		long coin = 0;
		if (req.getBoxId() == 1 && box.getBox1() == 0) {
			coin = 5;
			ChapterBoxProvider.getInst().update(playerId, chapterId, box1, box2, box3);
		}
		if (req.getBoxId() == 2 && box.getBox2() == 0) {
			coin = 5;
			ChapterBoxProvider.getInst().update(playerId, chapterId, box1, box2, box3);
		}
		if (req.getBoxId() == 3 && box.getBox3() == 0) {
			coin = 5;
			ChapterBoxProvider.getInst().update(playerId, chapterId, box1, box2, box3);
		}

		player.getUser().setCoin(player.getUser().getCoin() + coin);
		builder.setGold(gold);
		builder.setCoin(coin);
		builder.addBoxes(box.getBox1() == 1);
		builder.addBoxes(box.getBox2() == 1);
		builder.addBoxes(box.getBox3() == 1);

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_HALL_BOX, builder.build());
	}

	private void actionShopUse(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ShopUseReq req = msg.get();

		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		logger.info("actionShopUse playerId:{} req:{}", playerId, req);

		int propId = req.getShopId();

		ShopUseRsp.Builder builder = ShopUseRsp.newBuilder();

		Map<Integer, Bag> props = BagProvider.getInst().getByType(playerId, 1);
		if (props.containsKey(propId)) {
			props.forEach((e, f) -> {
				if (e == propId) {
					f.setState(ShopEnum.USEING.getValue());
					player.getUser().setProtrait(req.getShopId());
				} else {
					f.setState(ShopEnum.HAD.getValue());
				}
			});
		}

		for (ConfShop e : ConfShopProvider.getInst().getConfigList(e -> e.getType() == 1)) {
			Bag bag = props.get(e.getId());
			if (bag != null) {
				builder.addShops(PBHelper.crateShopPB(e.getId(), e.getItemId(), e.getItemNum(), e.getGold(), e.getCoin(), bag.getState()));
				continue;
			}
			builder.addShops(PBHelper.crateShopPB(e.getId(), e.getItemId(), e.getItemNum(), e.getGold(), e.getCoin(), ShopEnum.UN_HAD.getValue()));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SHOP_USE, builder.build());
	}

	// private void actionLuck(ChannelHandlerContext ctx, CocoPacket packet,
	// AbstractHandlers.MessageHolder<MessageLite> msg) {
	// long playerId = packet.getPlayerId();
	// LuckReq req = msg.get();
	// logger.info("actionLuck req:{}");
	// UserData player = PlayerManager.getInst().getPlayerById(playerId);
	// if (player == null) {
	// return;
	// }
	//
	// // 添加奖励
	//// int num = req.getProNum();
	//// if (req.getPropId() == 1) {
	//// player.setWeapon(player.getWeapon() + num);
	//// } else if (req.getPropId() == 2) {
	//// player.setBloodBox(player.getBloodBox() + num);
	//// } else if (req.getPropId() == 3) {
	//// player.setFrame(player.getFrame() + num);
	//// }
	//
	// LuckRsq.Builder builder = LuckRsq.newBuilder();
	// MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_LUCK,
	// builder.build());
	// }

	private void actionSectionDetail(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SectionDetailReq req = msg.get();
		logger.info("sectionDetail playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		ConfChapter confChapter = ConfChapterProvider.getInst().getSection(player.getUser().getGameType(), req.getChapter(), req.getSection());
		if (confChapter == null) {
			logger.info("关卡信息错误", req);
			return;
		}

		SectionDetailRsp.Builder builder = SectionDetailRsp.newBuilder();
		builder.setChapter(req.getChapter());
		builder.setSection(req.getSection());
		builder.setGold(player.getUser().getGold());
		builder.setCoin(player.getUser().getCoin());

		builder.setWeapon(BagProvider.getInst().getPropNum(playerId, MxwPropId.WEAPON_BAG));
		builder.setBloodBox(BagProvider.getInst().getPropNum(playerId, MxwPropId.BLOOD_BOX));
		builder.setFrame(BagProvider.getInst().getPropNum(playerId, MxwPropId.FRAME));

		Chapter chapter = ChapterProvider.getInst().getRecord(playerId, confChapter.getId());
		builder.setPassStarLv(chapter == null ? 0 : chapter.getStarLv());
		builder.setPassTopTime(chapter == null ? 0 : chapter.getPassTime());

		int state = SignProvider.getInst().getState(playerId);
		builder.setShowSign(state == 2);

		// 该关卡的排行
		List<Chapter> topList = ChapterProvider.getInst().getRankList(confChapter.getId());
		for (Chapter e : topList) {
			SectionRankPB.Builder rb = SectionRankPB.newBuilder();
			UserData target = PlayerManager.getInst().getPlayerById(e.getUserId());
			rb.setNick(target.getUser().getUserName() == null ? "" : target.getUser().getUserName());
			rb.setPortrait(target.getUser().getPlatProtrait() == null ? "" : target.getUser().getPlatProtrait());
			rb.setPassTime(e.getPassTime());
			rb.setRankId(1);
			builder.addRanks(rb.build());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_SECTION_DETAIL, builder.build());
	}

	private void actionGotPropReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GotPropReq req = msg.get();
		logger.info("actionGotPropReq playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		int propId = req.getPropId();
		int propNum = req.getPropNum();

		GotPropRsp.Builder builder = GotPropRsp.newBuilder();
		builder.setPropId(propId);
		Bag bag = BagProvider.getInst().addProp(player, propId, propNum, 1);
		builder.setPropNum(bag.getPropNum());
		builder.setPropAdd(propNum);
		builder.setIsshowUi(req.getIsshowUi());
		logger.info("actionGotPropRsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_GOT_PROP, builder.build());
	}

	private void actionConsumePropReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConsumePropReq req = msg.get();
		logger.info("actionConsumePropReq playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}
		int propId = req.getPropId();
		int propNum = req.getPropNum();

		ConsumePropRsp.Builder builder = ConsumePropRsp.newBuilder();
		builder.setPropId(propId);
		boolean success = BagProvider.getInst().subProp(player, propId, propNum);
		builder.setSuccess(success);

		if (success) {
			if (propId == MxwPropId.WEAPON_BAG) {
				int roleId = ChapterManager.getInst().getChapterId(playerId);
				if (roleId == 1) {
					BagProvider.getInst().addProp(player, MxwPropId.FEI_BIAO, 20, 1);
				} else {
					BagProvider.getInst().addProp(player, MxwPropId.MO_ZHANG, 20, 1);
				}
			}
		}

		logger.info("ConsumePropRsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_CONSUME_PROP, builder.build());
	}

	private void actionGetPropListReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GetPropListReq req = msg.get();
		logger.info("actionGetPropListReq playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		GetPropListRsp.Builder builder = GetPropListRsp.newBuilder();
		List<ConfShop> confList = ConfShopProvider.getInst().getConfigList(e -> e.getType() == 2);
		for (ConfShop e : confList) {
			PropPB.Builder b = PropPB.newBuilder();
			b.setItemId(e.getId());
			Bag bag = BagProvider.getInst().getById(playerId, e.getId());
			if (bag == null) {
				b.setItemNum(0);
			} else {
				b.setItemNum(bag.getPropNum());
			}
			builder.addProps(b.build());
		}

		// logger.info("actionGetPropListRsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_PROP_LIST, builder.build());
	}

	private void actionUpdataReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UpdateProp req = msg.get();
		logger.info("actionUpdataReq playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		Map<Integer, Integer> items = req.getItemsMap();
		items.forEach((e, f) -> {
			BagProvider.getInst().setPropNum(player, e, f);
		});

		GetPropListRsp.Builder builder = GetPropListRsp.newBuilder();
		List<ConfShop> confList = ConfShopProvider.getInst().getConfigList(e -> e.getType() == 2);
		for (ConfShop e : confList) {
			PropPB.Builder b = PropPB.newBuilder();
			b.setItemId(e.getId());
			Bag bag = BagProvider.getInst().getById(playerId, e.getId());
			if (bag == null) {
				b.setItemNum(0);
			} else {
				b.setItemNum(bag.getPropNum());
			}
			builder.addProps(b.build());
		}
		logger.info("actionUpdata rsp:{}", builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_PROP_LIST, builder.build());
	}

	private void actionUserAllowReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		UserAllowReq req = msg.get();

		logger.info("actionUserAllowReq playerId:{} req:{}", playerId, req);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		player.getUser().setUserName(req.getUserName());
		player.getUser().setPlatProtrait(req.getAvatar());

		UserAllowRsp.Builder builder = UserAllowRsp.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_USER_ALLOW, builder.build());
	}

	private void actionGetSignReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();

		logger.info("actionGetSignReq playerId:{}", playerId);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		Sign sign = SignProvider.getInst().getSign(playerId);
		GetSignRsp.Builder builder = GetSignRsp.newBuilder();

		// 获取当前应该签到的ID
		int signId = SignProvider.getInst().getSignId(playerId);
		if (signId == -1) {
			return;
		}
		for (ConfSign e : ConfSignProvider.getInst().getAllConfig()) {
			int itemId = e.getAwardList().get(0).get(0);
			int itemNum = e.getAwardList().get(0).get(1);
			if (signId == e.getId()) {
				int state = SignProvider.getInst().getState(sign, signId);
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), state, itemId, itemNum));
			} else if (signId < e.getId()) {// 待签
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), 1, itemId, itemNum));
			} else {// 已签到
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), 3, itemId, itemNum));
			}
		}
		logger.info("actionGetSignRsp:{}", builder.build());

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_GET_SIGN, builder.build());
	}

	private void actionSignReq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();

		logger.info("actionGetSignReq playerId:{}", playerId);
		UserData player = PlayerManager.getInst().getPlayerById(playerId);
		if (player == null) {
			return;
		}

		Sign sign = SignProvider.getInst().getSign(playerId);
		SignRsp.Builder builder = SignRsp.newBuilder();
		int signId = SignProvider.getInst().getSignId(playerId);
		if (signId == -1) {
			return;
		}

		ConfSign conf = ConfSignProvider.getInst().getConfigById(signId);
		int propId = conf.getAwardList().get(0).get(0);
		int propNum = conf.getAwardList().get(0).get(1);

		boolean isSign = SignProvider.getInst().sign(playerId, signId, DateUtil.getToday());
		if (isSign) {
			Bag bag = BagProvider.getInst().addProp(player, propId, propNum, 1);
			GotPropRsp.Builder awardShow = GotPropRsp.newBuilder();
			awardShow.setPropId(propId);
			awardShow.setPropNum(bag.getPropNum());
			awardShow.setPropAdd(propNum);
			awardShow.setIsshowUi(true);
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_GOT_PROP, awardShow.build());
		}

		for (ConfSign e : ConfSignProvider.getInst().getAllConfig()) {
			int itemId = e.getAwardList().get(0).get(0);
			int itemNum = e.getAwardList().get(0).get(1);
			if (signId == e.getId()) {
				int state = SignProvider.getInst().getState(sign, signId);
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), state, itemId, itemNum));
			} else if (signId < e.getId()) {// 待签
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), 1, itemId, itemNum));
			} else {// 已签到
				builder.addSigns(PBHelper.crateSignPB(e.getSignDay(), 3, itemId, itemNum));
			}
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.MXW_GET_SIGN, builder.build());
	}
}
