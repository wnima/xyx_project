//package network.handler.module.tank;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSONArray;
//import com.game.domain.Player;
//import com.game.domain.Role;
//import com.game.domain.s.StaticActAward;
//import com.game.domain.s.StaticAwards;
//import com.game.domain.s.StaticLordCommand;
//import com.game.domain.s.StaticLordRank;
//import com.game.domain.s.ConfVip;
//import com.game.message.handler.DealType;
//import com.game.pb.BasePb.Base;
//import com.game.server.GameServer;
//import com.game.server.ICommand;
//import com.game.util.AccountHelper;
//import com.game.util.DateHelper;
//import com.game.util.EmojiHelper;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.TimeHelper;
//import com.google.inject.Singleton;
//import com.google.protobuf.MessageLite;
//
//import config.bean.ConfProp;
//import config.provider.ConfVipProvider;
//import data.bean.Account;
//import data.bean.Arena;
//import data.bean.Award;
//import data.bean.BossFight;
//import data.bean.Effect;
//import data.bean.Equip;
//import data.bean.FailNum;
//import data.bean.Lord;
//import data.bean.Prop;
//import data.bean.Resource;
//import define.AwardFrom;
//import define.AwardType;
//import define.EffectType;
//import define.GameError;
//import define.GoldCost;
//import define.HeroId;
//import define.MailType;
//import define.OperType;
//import define.PropId;
//import define.SysChatId;
//import domain.Member;
//import inject.BeanManager;
//import io.netty.channel.ChannelHandlerContext;
//import manager.PlayerDataManager;
//import network.AbstractHandlers;
//import network.IModuleMessageHandler;
//import network.handler.ClientHandler;
//import packet.CocoPacket;
//import pb.ActivityPb.DoPartyTipAwardRs;
//import pb.ActivityPb.GiftCodeRs;
//import pb.ActivityPb.OlAwardRs;
//import pb.CommonPb;
//import pb.GamePb.BeginGameRq;
//import pb.GamePb.BeginGameRs;
//import pb.GamePb.BuyBuildRs;
//import pb.GamePb.BuyFameRs;
//import pb.GamePb.BuyPowerRs;
//import pb.GamePb.BuyProsRs;
//import pb.GamePb.ClickFameRs;
//import pb.GamePb.CreateRoleRq;
//import pb.GamePb.CreateRoleRs;
//import pb.GamePb.DoneGuideRs;
//import pb.GamePb.GetEffectRs;
//import pb.GamePb.GetGuideGiftRs;
//import pb.GamePb.GetLordRs;
//import pb.GamePb.GetResourceRs;
//import pb.GamePb.GetSkillRs;
//import pb.GamePb.GetTimeRs;
//import pb.GamePb.ResetSkillRs;
//import pb.GamePb.RoleLoginRs;
//import pb.GamePb.SetDataRq;
//import pb.GamePb.SetDataRs;
//import pb.GamePb.SetPortraitRs;
//import pb.GamePb.UpCommandRs;
//import pb.GamePb.UpRankRs;
//import pb.GamePb.UpSkillRs;
//import pb.InnerPb.UseGiftCodeRq;
//import pb.InnerPb.UseGiftCodeRs;
//import pb.InnerPb.VerifyRq;
//import pb.InnerPb.VerifyRs;
//import util.WarHelper;
//
//@Singleton
//public class PlayerModule implements IModuleMessageHandler {
//
//	private static Logger logger = LoggerFactory.getLogger(PlayerModule.class);
//
//	public static PlayerModule getInst() {
//		return BeanManager.getBean(PlayerModule.class);
//	}
//
//	@Override
//	public void registerModuleHandler(AbstractHandlers handler) {
//		handler.registerAction(2001, this::beginGame, BeginGameRq.getDefaultInstance());
//		handler.registerAction(2002, this::roleLogin);
//		handler.registerAction(2003, this::verifyRs, VerifyRs.getDefaultInstance());
//		handler.registerAction(2004, this::useGiftCodeRs, UseGiftCodeRs.getDefaultInstance());
//	}
//
//	/**
//	 * 
//	 * Method: beginGame
//	 * 
//	 * @Description: 客户端发过来的登陆验证请求，这里转发给账号服务器做验证 @param req @return void @throws
//	 */
//	public void beginGame(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
//		BeginGameRq req = msg.get();
//		int keyId = req.getKeyId();
//		String token = req.getToken();
//		int serverId = req.getServerId();
//		String curVersion = req.getCurVersion();
//		String deviceNo = req.getDeviceNo();
//
//		VerifyRq.Builder builder = VerifyRq.newBuilder();
//		builder.setKeyId(keyId);
//		builder.setServerId(serverId);
//		builder.setToken(token);
//		builder.setCurVersion(curVersion);
//		builder.setDeviceNo(deviceNo);
////		builder.setChannelId(handler.getChannelId());
//
////		Base.Builder baseBuilder = PbHelper.createRqBase(VerifyRq.EXT_FIELD_NUMBER, null, VerifyRq.ext, builder.build());
////		handler.sendMsgToPublic(baseBuilder);
////		MsgHelper.sendResponse(ctx, userId, code, message);
//	}
//
//	/**
//	 * 
//	 * Method: roleLogin
//	 * 
//	 * @Description: TODO @param handler @return void @throws
//	 */
//	public void roleLogin(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
////		public void roleLogin(ClientHandler handler) {
//		RoleLoginRs.Builder builder = RoleLoginRs.newBuilder();
//		builder.setState(1);
//
//		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
//
//		if (player.account.getCreated() != 1) {
////			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
//		}
//
//		ChannelHandlerContext preCtx = player.ctx;
//		player.ctx = handler.getCtx();
//
//		if (player.isLogin) {
//			if (preCtx != null) {
//				preCtx.close();
//			}
//		} else {
//			player.setLogin(true);
//			player.logIn();
//			PlayerDataManager.getInst().addOnline(player);
//		}
//
//		// 封测送vip
//		// Calendar now = Calendar.getInstance();
//		// int day = now.get(Calendar.DAY_OF_MONTH);
//		// int vip = player.lord.getVip();
//		// if (vip < day - 4) {
//		// player.lord.setVip(day - 4);
//		// }
//		if (WarHelper.isWarOpen()) {
//			builder.setWar(1);
//		}
//
//		if (WarHelper.isBossOpen()) {
//			builder.setBoss(1);
//		}
//
//		if (WarHelper.isStaffingOpen()) {
//			builder.setStaffing(1);
//		}
//
//		handler.sendMsgToPlayer(RoleLoginRs.ext, builder.build());
//
//		LogHelper.logLogin(player);
//	}
//
//	/**
//	 * 
//	 * Method: verifyRs
//	 * 
//	 * @Description: 账号服务器的验证返回处理 @param req @return void @throws
//	 */
//	public void verifyRs(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
////	public void verifyRs(VerifyRs req, ServerHandler handler, ChannelHandlerContext playerCtx) {
//		VerifyRs req = msg.get();
//		int platNo = req.getPlatNo();
//		int childNo = req.getChildNo();
//		String platId = req.getPlatId();
//		int keyId = req.getKeyId();
//		int serverId = req.getServerId();
//		// String curVersion = req.getCurVersion();
//		String deviceNo = req.getDeviceNo();
//
//		BeginGameRs.Builder builder = BeginGameRs.newBuilder();
//		Date now = new Date();
//		Account account = PlayerDataManager.getInst().getAccount(serverId, keyId);
//		if (account == null) {
//			account = new Account();
//			account.setServerId(serverId);
//			account.setAccountKey(keyId);
//			account.setPlatId(platId);
//			account.setPlatNo(platNo);
//			account.setChildNo(childNo);
//			account.setDeviceNo(deviceNo);
//			account.setLoginDays(1);
//			account.setCreateDate(new Date());
//			account.setLoginDate(new Date());
//
//			PlayerDataManager.getInst().createPlayer(account);
//			LogHelper.logRegister(account);
//			// player.connected = true;
//		} else {
//			Account dbAccount = accountDao.selectAccountByKeyId(account.getKeyId());
//			if (dbAccount != null) {
//				// 若是小号，走创建流程,但不创建account
//				if (smallIdManager.isSmallId(dbAccount.getLordId())) {
//					PlayerDataManager.getInst().createPlayerAfterCutSmallId(account);
//				} else {
//
//					account.setIsGm(dbAccount.getIsGm());
//					account.setIsGuider(dbAccount.getIsGuider());
//					account.setWhiteName(dbAccount.getWhiteName());
//					// account.setForbid(dbAccount.getForbid());
//					account.setLordId(dbAccount.getLordId());
//				}
//
//				Date loginDate = account.getLoginDate();
//				if (!DateHelper.isSameDate(now, loginDate)) {
//					account.setLoginDays(account.getLoginDays() + 1);
//				}
//			}
//
//			account.setDeviceNo(deviceNo);
//			account.setChildNo(childNo);
//			account.setLoginDate(now);
//			PlayerDataManager.getInst().recordLogin(account);
//
//			// Player player = PlayerDataManager.getInst().getPlayer(account.getLordId());
//			// if (player != null) {
//			// player.connected = true;
//			// }
//
//		}
//
//		if (AccountHelper.isForbid(account)) {
//			builder.setState(3);
//			builder.setTime(TimeHelper.getCurrentSecond());
//			Base.Builder baseBuilder = handler.createRsBase(GameError.OK, BeginGameRs.ext, builder.build());
//			GameServer.getInstance().sendMsgToPlayer(playerCtx, baseBuilder);
//			return;
//		}
//
//		// if (serverSetting.forbidByWhiteName(account)) {
//		// response.put("state", 4);
//		// return GameError.OK;
//		// }
//
//		if (account.getCreated() == 1) {// 角色已创建
//			builder.setState(2);
//		} else {
//			builder.setState(1);
//			builder.addAllName(generateNames());
//		}
//
//		GameServer.getInstance().registerRoleChannel(playerCtx, account.getLordId());
//
//		// builder.setSd(initSession);
//		builder.setTime(TimeHelper.getCurrentSecond());
//		Base.Builder baseBuilder = handler.createRsBase(GameError.OK, BeginGameRs.ext, builder.build());
//		GameServer.getInstance().sendMsgToPlayer(playerCtx, baseBuilder);
//	}
//
//	/**
//	 * 
//	 * Method: useGiftCodeRs
//	 * 
//	 * @Description: 使用兑换码 @param req @param handler @return void @throws
//	 */
//	public void useGiftCodeRs(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
////		public void useGiftCodeRs(final UseGiftCodeRs req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				// TODO Auto-generated method stub
//
//				long roleId = req.getLordId();
//				String award = req.getAward();
//
//				Player player = PlayerDataManager.getInst().getPlayer(roleId);
//				ChannelHandlerContext ctx = player.ctx;
//
//				GiftCodeRs.Builder builder = GiftCodeRs.newBuilder();
//				builder.setState(req.getState());
//
//				int state = req.getState();
//				if (state != 0) {
//					if (player.isLogin && ctx != null) {
//						Base.Builder baseBuilder = handler.createRsBase(GameError.OK, GiftCodeRs.ext, builder.build());
//						GameServer.getInstance().sendMsgToPlayer(ctx, baseBuilder);
//					}
//					return;
//				}
//
//				try {
//					JSONArray arrays = JSONArray.parseArray(award);
//					for (int i = 0; i < arrays.size(); i++) {
//						JSONArray array = arrays.getJSONArray(i);
//						if (array.size() != 3)
//							continue;
//						int type = array.getInteger(0);
//						int id = array.getInteger(1);
//						int count = array.getInteger(2);
//						int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.GIFT_CODE);
//						builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//
//				if (player.isLogin && ctx != null) {
//					Base.Builder baseBuilder = handler.createRsBase(GameError.OK, GiftCodeRs.ext, builder.build());
//					GameServer.getInstance().sendMsgToPlayer(ctx, baseBuilder);
//				}
//			}
//		}, DealType.MAIN);
//
//	}
//
//	/**
//	 * 
//	 * Method: createRole
//	 * 
//	 * @Description: 玩家创建角色 @param req @param ctx @return void @throws
//	 */
//	public void createRole(CreateRoleRq req, ClientHandler handler) {
//		GameError err;
//		int state;
//		CreateRoleRs.Builder builder = CreateRoleRs.newBuilder();
//		Player newPlayer = PlayerDataManager.getInst().getNewPlayer(playerId);
//		if (newPlayer == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
//		}
//
//		if (newPlayer.account.getCreated() == 1) {
//			err = GameError.ALREADY_CREATE;
//			builder.setNick(newPlayer.lord.getNick());
//			builder.setPortrait(newPlayer.lord.getPortrait());
//			state = 3;
//		} else {
//			String nick = req.getNick();
//			int portrait = req.getPortrait();
//			int sex = req.getSex();
//
//			if (nick == null || nick.isEmpty() || nick.length() >= 12) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//				return;
//			}
//
//			if (EmojiHelper.containsEmoji(nick)) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
//				return;
//			}
//
//			if (PlayerDataManager.getInst().takeNick(nick)) {
//				// newPlayer.account.setNick(nick);
//				newPlayer.account.setCreated(1);
//				newPlayer.account.setCreateDate(new Date());
//				newPlayer.lord.setPortrait(portrait);
//				newPlayer.lord.setNick(nick);
//				newPlayer.lord.setSex(sex);
//
//				if (PlayerDataManager.getInst().createFullPlayer(newPlayer)) {
//					err = GameError.OK;
//					state = 1;
//					// PlayerDataManager.getInst().removeNewPlayer(newPlayer.roleId);
//					// PlayerDataManager.getInst().addPlayer(newPlayer);
//					changeNewPlayer(newPlayer.roleId, handler);
//					Account account = newPlayer.account;
//					LogHelper.logRegister(account);
//					return;
//				} else {
//					newPlayer.account.setCreated(0);
//					handler.sendErrorMsgToPlayer(GameError.SERVER_EXCEPTION);
//					LogHelper.ERROR_LOGGER.error("createFullPlayer {" + newPlayer.roleId + "} error");
//					return;
//				}
//			} else {
//				err = GameError.SAME_NICK;
//				state = 2;
//			}
//		}
//
//		builder.setState(state);
//		handler.sendMsgToPlayer(err, CreateRoleRs.ext, builder.build());
//	}
//
//	private void changeNewPlayer(final long roleId, final ClientHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				// TODO Auto-generated method stub
//				Player newPlayer = PlayerDataManager.getInst().getNewPlayer(roleId);
//				if (newPlayer == null) {
//					LogHelper.ERROR_LOGGER.error("changeNewPlayer {" + roleId + "} error");
//					return;
//				}
//
//				PlayerDataManager.getInst().removeNewPlayer(roleId);
//				PlayerDataManager.getInst().addPlayer(newPlayer);
//
//				CreateRoleRs.Builder builder = CreateRoleRs.newBuilder();
//				builder.setState(1);
//
//				handler.sendMsgToPlayer(GameError.OK, CreateRoleRs.ext, builder.build());
//			}
//		}, DealType.MAIN);
//
//	}
//
//	/**
//	 * 
//	 * Method: getLord
//	 * 
//	 * @Description: 客户端请求玩家数据 @param roleId @param ctx @return void @throws
//	 */
//	public void getLord(ClientHandler handler) {
//		Long roleId = playerId;
//		Player player = PlayerDataManager.getInst().getPlayer(roleId);
//		if (player != null) {
//			Lord lord = PlayerDataManager.getInst().getPlayer(roleId).lord;
//			CombatService.cleanExplore(lord);
//
//			restoreProsAndPower(player, TimeHelper.getCurrentSecond());
//			GetLordRs.Builder builder = GetLordRs.newBuilder();
//			builder.setLordId(lord.getLordId());
//			builder.setNick(lord.getNick());
//			builder.setPortrait(lord.getPortrait());
//			builder.setLevel(lord.getLevel());
//			builder.setExp(lord.getExp());
//			builder.setVip(lord.getVip());
//			builder.setPos(lord.getPos());
//			builder.setGold(lord.getGold());
//			builder.setRanks(lord.getRanks());
//			builder.setCommand(lord.getCommand());
//			builder.setFame(lord.getFame());
//			builder.setFameLv(lord.getFameLv());
//			builder.setHonour(lord.getHonour());
//			builder.setPros(lord.getPros());
//			builder.setProsMax(lord.getProsMax());
//			builder.setProsTime(PlayerDataManager.getInst().leftBackProsTime(lord));
//			builder.setPower(lord.getPower());
//			builder.setPowerTime(PlayerDataManager.getInst().leftBackPowerTime(lord));
//			builder.setNewState(lord.getNewState());
//			builder.setFight(lord.getFight());
//			builder.setEquip(lord.getEquip());
//			builder.setFitting(lord.getFitting());
//			builder.setMetal(lord.getMetal());
//			builder.setPlan(lord.getPlan());
//			builder.setMineral(lord.getMineral());
//			builder.setTool(lord.getTool());
//			builder.setDraw(lord.getDraw());
//
//			builder.setEquipEplr(lord.getEquipEplr());
//			builder.setPartEplr(lord.getPartEplr());
//			builder.setMilitaryEplr(lord.getMilitaryEplr());
//			builder.setExtrEplr(lord.getExtrEplr());
//			builder.setTimeEplr(lord.getTimeEplr());
//			builder.setEquipBuy(lord.getEquipBuy());
//			builder.setPartBuy(lord.getPartBuy());
//			builder.setMilitaryBuy(lord.getMilitaryBuy());
//			builder.setExtrReset(lord.getExtrReset());
//
//			builder.setHuangbao(lord.getHuangbao());
//			builder.setCreateRoleTime(player.account.getCreateDate().getTime());
//
//			int nowDay = TimeHelper.getCurrentDay();
//			if (nowDay != lord.getFameTime1()) {
//				builder.setClickFame(true);
//			} else {
//				builder.setClickFame(false);
//			}
//
//			if (nowDay != lord.getFameTime2()) {
//				builder.setBuyFame(true);
//			} else {
//				builder.setBuyFame(false);
//			}
//
//			builder.setSex(lord.getSex());
//
//			refreshBuyPower(lord);
//			builder.setBuyPower(lord.getBuyPower());
//			builder.setNewerGift(lord.getNewerGift());
//			builder.setBuildCount(lord.getBuildCount());
//
//			builder.setOlTime(player.onLineTime());
//
//			builder.setCtTime(lord.getCtTime());
//			builder.setOlAward(lord.getOlAward());
//			builder.setGm(player.account.getIsGm());
//			builder.setGuider(player.account.getIsGuider());
//			builder.setTopup(lord.getTopup());
//			builder.setPartyTipAward(lord.getPartyTipAward());
//			builder.setStaffing(lord.getStaffing());
//			builder.setStaffingLv(lord.getStaffingLv());
//			builder.setStaffingExp(lord.getStaffingExp());
//
//			builder.setRuins(PbHelper.createRuinsPb(player.ruins));
//			handler.sendMsgToPlayer(GetLordRs.ext, builder.build());
//		}
//	}
//
//	/**
//	 * 
//	 * Method: getGuideGift
//	 * 
//	 * @Description: 领取新手引导礼包 @throws
//	 */
//	public void getGuideGift(ClientHandler handler) {
//		Long roleId = playerId;
//		Player player = PlayerDataManager.getInst().getPlayer(roleId);
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		if (lord.getNewerGift() == 1) {
//			handler.sendErrorMsgToPlayer(GameError.GIFT_HAD_GOT);
//			return;
//		}
//		StaticAwards staticAward = staticAwardsDataMgr.getAwardById(38);
//		List<List<Integer>> awardList = staticAward.getAwardList();
//		GetGuideGiftRs.Builder builder = GetGuideGiftRs.newBuilder();
//		for (List<Integer> ee : awardList) {
//			if (ee.size() < 3) {
//				continue;
//			}
//			int type = ee.get(0);
//			int id = ee.get(1);
//			int count = ee.get(2);
//			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.NEWER_GIFT);
//			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//		}
//		lord.setNewerGift(1);
//		handler.sendMsgToPlayer(GetGuideGiftRs.ext, builder.build());
//	}
//
//	private void refreshBuyPower(Lord lord) {
//		int nowDay = TimeHelper.getCurrentDay();
//		if (lord.getBuyPowerTime() != nowDay) {
//			lord.setBuyPower(0);
//			lord.setBuyPowerTime(nowDay);
//		}
//	}
//
//	/**
//	 * 
//	 * Method: buyPower
//	 * 
//	 * @Description: 购买体力 @param handler @return void @throws
//	 */
//	public void buyPower(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//		refreshBuyPower(lord);
//
//		int buyCount = lord.getBuyPower();
//		ConfVip staticVip = ConfVipProvider.getInst().getStaticVip(lord.getVip());
//
//		if (buyCount >= staticVip.getBuyPower()) {
//			handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
//			return;
//		}
//
//		int cost = (buyCount < 12) ? (buyCount + 1) * 5 : 120;
//		if (lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_POWER);
//		PlayerDataManager.getInst().addPower(lord, 5);
//		lord.setBuyPower(buyCount + 1);
//
//		BuyPowerRs.Builder builder = BuyPowerRs.newBuilder();
//		builder.setGold(lord.getGold());
//		builder.setPower(lord.getPower());
//		handler.sendMsgToPlayer(BuyPowerRs.ext, builder.build());
//		return;
//	}
//
//	/**
//	 * 
//	 * Method: upRank
//	 * 
//	 * @Description: 升级军衔 @param handler @return void @throws
//	 */
//	public void upRank(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//		if (lord.getRanks() >= 17) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_RANKS);
//			return;
//		}
//
//		StaticLordRank staticLordRank = staticLordDataMgr.getStaticLordRank(lord.getRanks() + 1);
//		if (staticLordRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		if (lord.getLevel() < staticLordRank.getLordLv()) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		if (player.resource.getStone() < staticLordRank.getStoneCost()) {
//			handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().modifyStone(player.resource, -staticLordRank.getStoneCost());
//		lord.setRanks(lord.getRanks() + 1);
//
//		lord.setStaffing(PlayerDataManager.getInst().calcStaffing(player));
//
//		UpRankRs.Builder builder = UpRankRs.newBuilder();
//		builder.setStone(player.resource.getStone());
//		handler.sendMsgToPlayer(UpRankRs.ext, builder.build());
//
//		if (lord.getRanks() >= 7) {
//			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.RANKS_UP, player.lord.getNick(), staticLordRank.getName()));
//		}
//
//		PlayerDataManager.getInst().synStaffingToPlayer(player);
//	}
//
//	/**
//	 * 
//	 * Method: upCommand
//	 * 
//	 * @Description: 提升统帅等级 @param useGold @param handler @return void @throws
//	 */
//	public void upCommand(boolean useGold, ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//		StaticLordCommand staticLordCommand = staticLordDataMgr.getStaticCommandLv(lord.getCommand() + 1);
//		if (staticLordCommand == null) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_COMMAND);
//			return;
//		}
//
//		int bookCount = staticLordCommand.getBook();
//		UpCommandRs.Builder builder = UpCommandRs.newBuilder();
//
//		if (useGold) {
//			ConfProp book = staticPropDataMgr.getStaticProp(PropId.COMMAND_BOOK);
//			int cost = book.getPrice() * bookCount;
//			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//
//			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.UP_COMMAND);
//			builder.setGold(lord.getGold());
//		} else {
//
//			Prop prop = player.props.get(PropId.COMMAND_BOOK);
//			if (prop == null || prop.getCount() < bookCount) {
//				handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//				return;
//			}
//
//			PlayerDataManager.getInst().subProp(prop, bookCount);
//			builder.setBook(prop.getCount());
//		}
//
//		int probAdd = 0;
//		if (player.heros.containsKey(HeroId.TONG_SHUAI_GUAN)) {
//			probAdd = staticHeroDataMgr.getStaticHero(HeroId.TONG_SHUAI_GUAN).getSkillValue() * 10;
//		}
//
//		int[] revelry = activityDataManager.revelry();
//
//		FailNum f = PlayerDataManager.getInst().getFailNumByOperType(player, OperType.upCommand);
//		if (RandomHelper.isHitRangeIn1000((int) ((calcuLordCommandProb(f.getNum(), staticLordCommand.getA(), staticLordCommand.getB()) + probAdd) * ((1000 + revelry[0]) / 1000.0f)))) {
//			lord.setCommand(lord.getCommand() + 1);
//			builder.setSuccess(true);
//			// 更新失败次数为0
//			f.setNum(0);
//
//			if (lord.getCommand() >= 15) {
//				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.COMMAND_UP, player.lord.getNick(), String.valueOf(lord.getCommand())));
//			}
//
//		} else {
//			// 更新失败次数+1
//			builder.setSuccess(false);
//			f.setNum(f.getNum() + 1);
//		}
//
//		handler.sendMsgToPlayer(UpCommandRs.ext, builder.build());
//	}
//
//	/**
//	 * 失败次数计入升级统率概率 y=x/(x + ab) x = 失败次数 + 1 a,b 为配表值
//	 * 
//	 * @param failNum
//	 * @return
//	 */
//	private float calcuLordCommandProb(int failNum, int a, float b) {
//		int x = failNum + 1;
//		return x * 1000 / (x + a * b);
//	}
//
//	/**
//	 * 
//	 * Method: buyPros
//	 * 
//	 * @Description: 购买繁荣度 @param handler @return void @throws
//	 */
//	public void buyPros(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//		if (PlayerDataManager.getInst().fullPros(lord)) {
//			handler.sendErrorMsgToPlayer(GameError.FULL_PROSP);
//			return;
//		}
//
//		int cost = 0;
//		int tempPros = 0; // 恢复后的繁荣度
//		// 若是非废墟,繁荣度必须买满
//		if (!PlayerDataManager.getInst().isRuins(player)) {
//			cost = (int) Math.ceil((lord.getProsMax() - lord.getPros()) / 50.0);
//			tempPros = lord.getProsMax();
//		} else {
//			// 废墟必须买到脱离废墟(繁荣度最大值低于600恢复满,繁荣度大于等于600时恢复至600)
//			if (lord.getProsMax() < 600) {
//				cost = (int) Math.ceil((lord.getProsMax() - lord.getPros()) / 25.0);
//				tempPros = lord.getProsMax();
//			} else {
//				cost = (int) Math.ceil((600 - lord.getPros()) / 25.0);
//				tempPros = 600;
//			}
//		}
//
//		if (lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_PROSP);
//		lord.setPros(tempPros);
//		PlayerDataManager.getInst().outOfRuins(player);
//
//		BuyProsRs.Builder builder = BuyProsRs.newBuilder();
//		builder.setGold(lord.getGold());
//		handler.sendMsgToPlayer(BuyProsRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: buyFame
//	 * 
//	 * @Description: 授勋 @param type @param handler @return void @throws
//	 */
//	public void buyFame(int type, ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//
//		int now = TimeHelper.getCurrentDay();
//		if (lord.getFameTime2() == now) {
//			handler.sendErrorMsgToPlayer(GameError.ALREADY_FAME);
//			return;
//		}
//
//		int add = 0;
//		BuyFameRs.Builder builder = BuyFameRs.newBuilder();
//		if (type == 1) {
//			if (player.resource.getStone() < 1000) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
//				return;
//			}
//			PlayerDataManager.getInst().modifyStone(player.resource, -1000);
//			builder.setStone(player.resource.getStone());
//			add = 10;
//		} else if (type == 2) {
//			if (lord.getGold() < 10) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			PlayerDataManager.getInst().subGold(lord, 10, GoldCost.BUY_FAME);
//			builder.setGold(lord.getGold());
//			add = 100;
//		} else if (type == 3) {
//			if (lord.getGold() < 40) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			PlayerDataManager.getInst().subGold(lord, 40, GoldCost.BUY_FAME);
//			builder.setGold(lord.getGold());
//			add = 400;
//		} else {
//			if (lord.getGold() < 100) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			PlayerDataManager.getInst().subGold(lord, 100, GoldCost.BUY_FAME);
//			builder.setGold(lord.getGold());
//			add = 1200;
//		}
//
//		PlayerDataManager.getInst().addFame(lord, add);
//		lord.setFameTime2(now);
//		builder.setFame(lord.getFame());
//		builder.setFameLv(lord.getFameLv());
//		handler.sendMsgToPlayer(BuyFameRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: getSkill
//	 * 
//	 * @Description: 获取技能数据 @param handler @return void @throws
//	 */
//
//	public void getSkill(ClientHandler handler) {
//		GetSkillRs.Builder builder = GetSkillRs.newBuilder();
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		for (Map.Entry<Integer, Integer> entry : player.skills.entrySet()) {
//			builder.addSkill(PbHelper.createSkillPb(entry.getKey(), entry.getValue()));
//		}
//		handler.sendMsgToPlayer(GetSkillRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: upSkill
//	 * 
//	 * @Description: 升级技能 @param skillId @param handler @return void @throws
//	 */
//	public void upSkill(int skillId, ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Integer lv = player.skills.get(skillId);
//		if (lv == null) {
//			lv = 0;
//		}
//
//		int upLv = lv + 1;
//		int bookCount = upLv;
//		int lordLv = upLv;
//
//		if (player.lord.getLevel() < lordLv) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		Prop book = player.props.get(PropId.SKILL_BOOK);
//		if (book == null || book.getCount() < bookCount) {
//			handler.sendErrorMsgToPlayer(GameError.BOOK_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().subProp(book, bookCount);
//		player.skills.put(skillId, upLv);
//
//		UpSkillRs.Builder builder = UpSkillRs.newBuilder();
//		builder.setLv(upLv);
//		builder.setBookCount(book.getCount());
//		handler.sendMsgToPlayer(UpSkillRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: resetSkill
//	 * 
//	 * @Description: 重置技能 @param handler @return void @throws
//	 */
//	public void resetSkill(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		if (player.lord.getGold() < 58) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().subGold(player.lord, 58, GoldCost.RESET_SKILL);
//
//		int bookCount = 0;
//		Iterator<Integer> it = player.skills.values().iterator();
//		while (it.hasNext()) {
//			Integer lv = (Integer) it.next();
//			bookCount += ((1 + lv) * lv / 2);
//		}
//
//		Prop prop = PlayerDataManager.getInst().addProp(player, PropId.SKILL_BOOK, bookCount);
//		player.skills.clear();
//
//		ResetSkillRs.Builder builder = ResetSkillRs.newBuilder();
//		builder.setGold(player.lord.getGold());
//		builder.setBook(prop.getCount());
//		handler.sendMsgToPlayer(ResetSkillRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: clickFame
//	 * 
//	 * @Description: 领取军衔声望 @param handler @return void @throws
//	 */
//	public void clickFame(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Lord lord = player.lord;
//		int now = TimeHelper.getCurrentDay();
//		if (lord.getFameTime1() == now) {
//			handler.sendErrorMsgToPlayer(GameError.ALREADY_RANK_FAME);
//			return;
//		}
//
//		StaticLordRank staticLordRank = staticLordDataMgr.getStaticLordRank(lord.getRanks());
//		if (staticLordRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		lord.setFameTime1(now);
//
//		PlayerDataManager.getInst().addFame(lord, staticLordRank.getFame());
//		ClickFameRs.Builder builder = ClickFameRs.newBuilder();
//		builder.setFameLv(lord.getFameLv());
//		builder.setFame(lord.getFame());
//		handler.sendMsgToPlayer(ClickFameRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: getTime
//	 * 
//	 * @Description: 客户端获取服务器时间 @param ctx @return void @throws
//	 */
//	public void getTime(ClientHandler handler) {
//		GetTimeRs.Builder builder = GetTimeRs.newBuilder();
//		builder.setTime(TimeHelper.getCurrentSecond());
//		builder.setOpenPay(serverSetting.isOpenPay());
//
//		handler.sendMsgToPlayer(GetTimeRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: getResource
//	 * 
//	 * @Description: 客户端获取资源数据 @param handler @return void @throws
//	 */
//	public void getResource(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		Resource resource = player.resource;
//		GetResourceRs.Builder builder = GetResourceRs.newBuilder();
//		builder.setIron(resource.getIron());
//		builder.setOil(resource.getOil());
//		builder.setCopper(resource.getCopper());
//		builder.setSilicon(resource.getSilicon());
//		builder.setStone(resource.getStone());
//
//		handler.sendMsgToPlayer(GetResourceRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: getEffect
//	 * 
//	 * @Description: 获取特殊效果加成 @param handler @return void @throws
//	 */
//	public void getEffect(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		GetEffectRs.Builder builder = GetEffectRs.newBuilder();
//		Iterator<Effect> it = player.effects.values().iterator();
//		while (it.hasNext()) {
//			builder.addEffect(PbHelper.createEffectPb(it.next()));
//		}
//		handler.sendMsgToPlayer(GetEffectRs.ext, builder.build());
//	}
//
//	public void doneGuide(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		DoneGuideRs.Builder builder = DoneGuideRs.newBuilder();
//		if (player.lord.getPos() == -1 && player.lord.getLevel() > 2) {
//			PlayerDataManager.getInst().addEffect(player, EffectType.ATTACK_FREE, 14400);
//			worldDataManager.addNewPlayer(player);
//			rankDataManager.setHonour(player.lord);
//			builder.setPos(player.lord.getPos());
//		}
//
//		handler.sendMsgToPlayer(DoneGuideRs.ext, builder.build());
//	}
//
//	private Equip getEquip(Player player, int keyId) {
//		for (int i = 0; i < 7; i++) {
//			Map<Integer, Equip> map = player.equips.get(i);
//			Equip equip = map.get(keyId);
//			if (equip != null) {
//				return equip;
//			}
//		}
//
//		return null;
//	}
//
//	public void setData(SetDataRq req, ClientHandler handler) {
//		int type = req.getType();
//		long value = req.getValue();
//
//		if (value < 0) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
//		}
//
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		if (type == 1) {
//			player.lord.setFight(value);
//			rankDataManager.setFight(player.lord);
//		} else if (type == 2) {
//			player.lord.setStars((int) value);
//			rankDataManager.setStars(player.lord);
//		} else if (type == 3) {
//			// rankDataManager.setHonour(player.lord);
//		} else if (type == 4) {
//			int keyId = (int) value;
//			Equip equip = getEquip(player, keyId);
//			if (equip == null || equip.getEquipId() / 100 != 1) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
//				return;
//			}
//			rankDataManager.setAttack(player.lord, equip);
//		} else if (type == 5) {
//			int keyId = (int) value;
//			Equip equip = getEquip(player, keyId);
//			if (equip == null || equip.getEquipId() / 100 != 5) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
//				return;
//			}
//
//			rankDataManager.setCrit(player.lord, equip);
//		} else if (type == 6) {
//			int keyId = (int) value;
//			Equip equip = getEquip(player, keyId);
//			if (equip == null || equip.getEquipId() / 100 != 4) {
//				handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
//				return;
//			}
//
//			rankDataManager.setDodge(player.lord, equip);
//		}
//
//		SetDataRs.Builder builder = SetDataRs.newBuilder();
//		handler.sendMsgToPlayer(SetDataRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: restoreProsAndPower
//	 * 
//	 * @Description: 恢复体力和繁荣度 @param player @param now @return void @throws
//	 */
//	private void restoreProsAndPower(Player player, int now) {
//		Lord lord = player.lord;
//		if (!PlayerDataManager.getInst().fullPower(lord)) {
//			PlayerDataManager.getInst().backPower(lord, now);
//		}
//
//		if (!PlayerDataManager.getInst().fullPros(lord)) {
//			PlayerDataManager.getInst().backPros(player, now);
//		}
//	}
//
//	/**
//	 * 
//	 * Method: checkEffectEnd
//	 * 
//	 * @Description: 检查效果加成 @param player @param now @return void @throws
//	 */
//	private void checkEffectEnd(Player player, int now) {
//		Iterator<Effect> it = player.effects.values().iterator();
//		while (it.hasNext()) {
//			Effect effect = (Effect) it.next();
//			if (effect.getEndTime() <= now) {
//				PlayerDataManager.getInst().vaildEffect(player, effect.getEffectId(), -1);
//				it.remove();
//				if (effect.getEffectId() == EffectType.MARCH_SPEED || effect.getEffectId() == EffectType.MARCH_SPEED_SUPER) {
//					worldService.recalcArmyMarch(player);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: restoreDataTimerLogic
//	 * 
//	 * @Description: 恢复能量和繁荣度的定时器逻辑 @return void @throws
//	 */
//	public void restoreDataTimerLogic() {
//		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//
//		while (iterator.hasNext()) {
//			Player player = iterator.next();
//
//			if (player.isActive()) {
//				restoreProsAndPower(player, now);
//				if (player.wipeTime != 0) {
//					combatService.checkWipe(player, now);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: effectTimerLogic
//	 * 
//	 * @Description: 判断加成效果是否结束的定时器逻辑 @return void @throws
//	 */
//	public void effectTimerLogic() {
//		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//		int goldTime;
//		while (iterator.hasNext()) {
//			Player player = iterator.next();
//			if (player.isActive()) {
//				checkEffectEnd(player, now);
//				if (player.heros.containsKey(HeroId.CAI_ZHENG_GUAN)) {
//					goldTime = player.lord.getGoldTime();
//					if (goldTime <= now) {
//						// PlayerDataManager.getInst().addGold(player.lord, 30,
//						// GoldGive.CAIZHENG);
//
//						player.lord.setGoldTime(now + TimeHelper.DAY_S);
//						List<CommonPb.Award> awardList = new ArrayList<CommonPb.Award>();
//						awardList.add(PbHelper.createAwardPb(AwardType.GOLD, 0, 30));
//						PlayerDataManager.getInst().sendAttachMail(player, awardList, MailType.MOLD_GOLD, now, "30");
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: saveTimerLogic
//	 * 
//	 * @Description: 定时保存玩家数据 @return void @throws
//	 */
//	public void saveTimerLogic() {
//		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//		int saveCount = 0;
//		int lv = 1;
//		while (iterator.hasNext()) {
//			Player player = iterator.next();
//			if (player.immediateSave || (now - player.lastSaveTime) >= 300) {
//				try {
//					if (saveCount >= 500) {
//						break;
//					}
//
//					saveCount++;
//					player.lastSaveTime = now;
//					Arena arena = null;
//					Member member = null;
//					BossFight bossFight = null;
//
//					lv = player.lord.getLevel();
//					if (lv >= 10) {
//						member = partyDataManager.getMemberById(player.roleId);
//					}
//
//					if (lv >= 15) {
//						arena = arenaDataManager.getArena(player.roleId);
//					}
//
//					if (lv >= 30) {
//						bossFight = bossDataManager.getBossFight(player.roleId);
//					}
//
//					GameServer.getInstance().savePlayerServer.saveData(new Role(player, arena, member, bossFight));
//					if (player.immediateSave) {
//						player.immediateSave = false;
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					LogHelper.ERROR_LOGGER.error("save player {" + player.roleId + "} data error", e);
//				}
//			}
//		}
//
//		if (saveCount != 0) {
//			LogHelper.SAVE_LOGGER.trace("save player count:" + saveCount);
//		}
//	}
//
//	// /**
//	// *
//	// * Method: loadPlayerData
//	// *
//	// * @Description: 客户端请求重新重数据库加载数据
//	// * @param handler
//	// * @return void
//	// * @throws
//	// */
//	// public void loadPlayerData(ClientHandler handler) {
//	// boolean success =
//	// PlayerDataManager.getInst().loadPlayerFromDb(playerId);
//	// LoadDataRs.Builder builder = LoadDataRs.newBuilder();
//	// builder.setSuccess(success);
//	//
//	// handler.sendMsgToPlayer(LoadDataRs.ext, builder.build());
//	// }
//
//	private List<String> generateNames() {
//		List<String> names = new ArrayList<String>();
//		while (names.size() < 3) {
//			String name = staticIniDataMgr.getManNick();
//			if (PlayerDataManager.getInst().canUseName(name)) {
//				names.add(name);
//			}
//		}
//
//		while (names.size() < 6) {
//			String name = staticIniDataMgr.getWomanNick();
//			if (PlayerDataManager.getInst().canUseName(name)) {
//				names.add(name);
//			}
//		}
//
//		return names;
//	}
//
//	public List<String> getAvailabelNames() {
//		return generateNames();
//	}
//
//	public void setPortrait(int portrait, ClientHandler handler) {
//		int pendent = portrait / 100;
//		int base = portrait - pendent * 100;
//
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		if (pendent > 10 || base > 10) {
//			if (player.lord.getVip() < 5) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
//				return;
//			}
//		}
//
//		player.lord.setPortrait(portrait);
//		SetPortraitRs.Builder builder = SetPortraitRs.newBuilder();
//		handler.sendMsgToPlayer(SetPortraitRs.ext, builder.build());
//	}
//
//	private static final int[] BUY_BUILD_COST = { 68, 108, 198, 368, 688 };
//
//	public void buyBuild(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		int count = 0;
//		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
//		if (staticVip != null) {
//			count = staticVip.getBuildQue();
//		}
//
//		int buildCount = player.lord.getBuildCount();
//		if (buildCount >= count) {
//			handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
//			return;
//		}
//
//		buildCount += 1;
//
//		if (buildCount > BUY_BUILD_COST.length) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		int cost = BUY_BUILD_COST[buildCount - 1];
//		if (player.lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		PlayerDataManager.getInst().subGold(player.lord, cost, GoldCost.BUY_BUILD);
//
//		player.lord.setBuildCount(buildCount);
//		BuyBuildRs.Builder builder = BuyBuildRs.newBuilder();
//		builder.setGold(player.lord.getGold());
//		handler.sendMsgToPlayer(BuyBuildRs.ext, builder.build());
//	}
//
//	public void giftCode(String code, ClientHandler handler) {
//		if (code.length() != 12) {
//			handler.sendErrorMsgToPlayer(GameError.GIFT_CODE_LENTH);
//			return;
//		}
//
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//
//		UseGiftCodeRq.Builder builder = UseGiftCodeRq.newBuilder();
//		builder.setCode(code);
//		builder.setLordId(player.roleId);
//		builder.setServerId(player.account.getServerId());
//		builder.setPlatNo(player.account.getPlatNo());
//
//		Base.Builder baseBuilder = PbHelper.createRqBase(UseGiftCodeRq.EXT_FIELD_NUMBER, 0L, UseGiftCodeRq.ext, builder.build());
//		handler.sendMsgToPublic(baseBuilder);
//	}
//
//	public void olAward(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//
//		int ctTime = player.lord.getCtTime();
//		int lastCtDay = TimeHelper.getDay(ctTime);
//
//		int now = TimeHelper.getCurrentSecond();
//		int nowDay = TimeHelper.getDay(now);
//		if (lastCtDay != nowDay) {// 跨零点在线在线的玩家
//			int todayZone = TimeHelper.getTodayZone(now);
//			player.lord.setCtTime(todayZone);
//			player.lord.setOlAward(0);
//		}
//
//		int id = player.lord.getOlAward();
//		int keyId = 0;
//		switch (id) {
//		case 0:
//			keyId = 1001;
//			break;
//		case 1:
//			keyId = 1002;
//			break;
//		case 2:
//			keyId = 1003;
//			break;
//		case 3:
//			keyId = 1004;
//			break;
//		case 4:
//			keyId = 1005;
//			break;
//		case 5:
//			keyId = 1006;
//			break;
//		case 6:
//			keyId = 1007;
//			break;
//		default:
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		StaticActAward staticActAward = staticActivityDataMgr.getActAward(keyId);
//		if (staticActAward == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		if (now - player.lord.getCtTime() < staticActAward.getCond()) {
//			handler.sendErrorMsgToPlayer(GameError.OL_NOT_ENOUGH);
//			return;
//		}
//
//		player.lord.setOlAward(id + 1);
//		player.lord.setCtTime(now);
//		List<Award> awards = PlayerDataManager.getInst().addAwardsBackPb(player, staticActAward.getAwardList(), AwardFrom.OL_AWARD);
//
//		OlAwardRs.Builder builder = OlAwardRs.newBuilder();
//		builder.setId(player.lord.getOlAward());
//		builder.addAllAward(awards);
//		handler.sendMsgToPlayer(OlAwardRs.ext, builder.build());
//	}
//
//	public void doPartyTipAwardRq(ClientHandler handler) {
//		Player player = PlayerDataManager.getInst().getPlayer(playerId);
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		if (lord.getPartyTipAward() == 2) {// 奖励已领取
//			handler.sendErrorMsgToPlayer(GameError.AWARD_HAD_GOT);
//			return;
//		}
//		lord.setPartyTipAward(2);
//		DoPartyTipAwardRs.Builder builder = DoPartyTipAwardRs.newBuilder();
//		int keyId = PlayerDataManager.getInst().addAward(player, AwardType.PROP, 56, 5, AwardFrom.PARTY_TIP_AWARD);
//		builder.addAward(PbHelper.createAwardPb(AwardType.PROP, 56, 5, keyId));
//		handler.sendMsgToPlayer(DoPartyTipAwardRs.ext, builder.build());
//	}
//
//}
