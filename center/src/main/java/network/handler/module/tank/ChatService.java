package network.handler.module.tank;

import java.util.List;

import com.game.domain.PartyData;
import com.game.domain.Player;
import com.game.util.ChatHelper;
import com.game.util.EmojiHelper;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import chat.Chat;
import chat.ManChat;
import chat.ManShare;
import chat.SystemChat;
import data.bean.Lord;
import data.bean.Mail;
import data.bean.Man;
import define.PartyType;
import define.SysChatId;
import domain.Member;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.ChatDataManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import network.ServerManager;
import packet.CocoPacket;
import pb.CommonPb;
import pb.CommonPb.ChatPB;
import pb.CommonPb.Report;
import pb.CommonPb.TankData;
import pb.GamePb.DoChatRq;
import pb.GamePb.DoChatRs;
import pb.GamePb.GetChatRs;
import pb.GamePb.GetReportRq;
import pb.GamePb.GetReportRs;
import pb.GamePb.PartyRecruitRs;
import pb.GamePb.SearchOlRq;
import pb.GamePb.SearchOlRs;
import pb.GamePb.ShareReportRq;
import pb.GamePb.ShareReportRs;
import pb.GamePb.SynChatRq;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class ChatService implements IModuleMessageHandler {
	public static ChatService getInst() {
		return BeanManager.getBean(ChatService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	public void getChat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		GetChatRs.Builder builder = GetChatRs.newBuilder();
		List<ChatPB> list = ChatDataManager.getInst().getWorldChat();
		for (ChatPB e : list) {
			builder.addChat(e);
		}

		Member member = PartyDataManager.getInst().getMemberById(player.roleId);
		if (member != null) {
			int partyId = member.getPartyId();
			if (partyId != 0) {
				List<ChatPB> partyChats = ChatDataManager.getInst().getPartyChat(partyId);
				if (partyChats != null) {
					for (ChatPB e : partyChats) {
						builder.addChat(e);
					}
				}
			}
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GET_CHAT_RS, builder.build());
	}

	/**
	 * 
	 * Method: createManChat
	 * 
	 * @Description: 普通聊天 @param player @param msg @return @return Chat @throws
	 */
	public Chat createManChat(Player player, String msg) {
		ManChat manChat = new ManChat();
		manChat.setPlayer(player);
		manChat.setTime(DateUtil.getSecondTime());
		manChat.setMsg(msg);
		return manChat;
	}

	public Chat createSysChat(int sysId, String... param) {
		SystemChat systemChat = new SystemChat();
		systemChat.setSysId(sysId);
		systemChat.setTime(DateUtil.getSecondTime());
		systemChat.setParam(param);
		return systemChat;
	}

	private Chat createRecruitChat(Player player, int sysId, String... param) {
		ManShare manShare = new ManShare();
		manShare.setPlayer(player);
		manShare.setTime(DateUtil.getSecondTime());
		manShare.setParam(param);
		manShare.setSysId(sysId);
		return manShare;
	}

	private Chat createReportShare(Player player, int chatId, int reportKey, String... param) {
		ManShare manShare = new ManShare();
		manShare.setPlayer(player);
		manShare.setTime(DateUtil.getSecondTime());
		manShare.setId(chatId);
		manShare.setParam(param);
		manShare.setReport(reportKey);
		return manShare;
	}

	private Chat createTankShare(Player player, TankData tankData) {
		ManShare manShare = new ManShare();
		manShare.setPlayer(player);
		manShare.setTime(DateUtil.getSecondTime());
		manShare.setTankData(tankData);
		return manShare;
	}

	private Chat createHeroShare(Player player, int heroId) {
		ManShare manShare = new ManShare();
		manShare.setPlayer(player);
		manShare.setTime(DateUtil.getSecondTime());
		manShare.setHeroId(heroId);
		return manShare;
	}

	// public void sendSysHorn(String c) {
	// sendHornChat(createSysChat(SysChatId.SYS_HORN, c), 1);
	// }

	/**
	 * 
	 * Method: partyRecruit
	 * 
	 * @Description: 发布军团招募消息 @param handler @return void @throws
	 */
	public void partyRecruit(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		int now = DateUtil.getSecondTime();
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (now - player.recruitTime < 3600) {
//			handler.sendErrorMsgToPlayer(GameError.PARTY_RECRUIT_CD);
			return;
		}

		Member member = PartyDataManager.getInst().getMemberById(player.roleId);
		if (member == null || member.getPartyId() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		if (member.getJob() != PartyType.LEGATUS) {
//			handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
			return;
		}

		PartyData partyData = PartyDataManager.getInst().getParty(member.getPartyId());
		if (partyData == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
			return;
		}

		Chat chat = null;
		if (partyData.getSlogan() != null) {
			chat = createRecruitChat(player, SysChatId.RECRUIT_5, partyData.getPartyName(), partyData.getSlogan());
		} else if (partyData.getApplyLv() > 0 && partyData.getApplyFight() == 0) {
			chat = createRecruitChat(player, SysChatId.RECRUIT_1, partyData.getPartyName());
		} else if (partyData.getApplyLv() == 0 && partyData.getApplyFight() > 0) {
			chat = createRecruitChat(player, SysChatId.RECRUIT_2, partyData.getPartyName());
		} else if (partyData.getApplyLv() == 0 && partyData.getApplyFight() == 0) {
			chat = createRecruitChat(player, SysChatId.RECRUIT_3, partyData.getPartyName());
		} else if (partyData.getApplyLv() > 0 && partyData.getApplyFight() > 0) {
			chat = createRecruitChat(player, SysChatId.RECRUIT_4, partyData.getPartyName());
		}

		sendWorldChat(chat);
		player.recruitTime = now;
		PartyRecruitRs.Builder builder = PartyRecruitRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.PARTY_RECRUIT_RS, builder.build());
	}

	/**
	 * 分享挑战战力top5 Method: shareChallengeTop5 @Description: TODO @return void @throws
	 */
	public void shareChallengeFightRankTop5(Player player, Player guardPlayer, Mail mail, int result) {
//		if (rankDataManager.isFightRankTop5(guardPlayer.lord.getLordId())) {
//			if (result == 1) {
//				sendWorldChat(createSysChat(SysChatId.Challenge_GOD_WIN, player.lord.getNick(), "" + mail.getKeyId()));
//			} else if (result == 2) {
//				sendWorldChat(createSysChat(SysChatId.Challenge_GOD_FAIL, player.lord.getNick(), "" + mail.getKeyId()));
//			}
//		}
	}

	public void shareChat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		ShareReportRq req = msg.get();
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		int now = DateUtil.getSecondTime();
		if (now - player.chatTime < 5) {
//			handler.sendErrorMsgToPlayer(GameError.CHAT_CD);
			return;
		}

		int reportKey = 0;
		int channel = req.getChannel();

		Chat chat = null;
		if (req.hasField(ShareReportRq.getDescriptor().findFieldByName("reportKey"))) {
//			if (req.hasReportKey()) {
			reportKey = req.getReportKey();
			Mail mail = player.mails.get(reportKey);
			if (mail == null) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			Report report = mail.getReport();
			if (report == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_REPORT);
				return;
			}

			chat = createReportShare(player, mail.getMoldId(), reportKey, mail.getParam());
		} else if (req.hasField(ShareReportRq.getDescriptor().findFieldByName("tankData"))) {
			CommonPb.TankData tankData = req.getTankData();
			chat = createTankShare(player, tankData);
		} else if (req.hasField(ShareReportRq.getDescriptor().findFieldByName("heroId"))) {
			chat = createHeroShare(player, req.getHeroId());
		}

		if (channel == 1) {// 世界
			sendWorldChat(chat);
		} else {// 军团
			Member member = PartyDataManager.getInst().getMemberById(player.roleId);
			if (member == null || member.getPartyId() == 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
				return;
			}

			sendPartyChat(chat, member.getPartyId());
		}

		player.chatTime = now;

		ShareReportRs.Builder builder = ShareReportRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SHARE_REPORT_RS, builder.build());
	}

	public void getReport(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GetReportRq req = msg.get();
		String nick = req.getName();
		int reportKey = req.getReportKey();
		Player target = PlayerDataManager.getInst().getPlayer(nick);
		if (target == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		GetReportRs.Builder builder = GetReportRs.newBuilder();
		Mail mail = target.mails.get(reportKey);
		if (mail == null || mail.getReport() == null) {
			builder.setState(2);
		} else {
			builder.setState(1);
			builder.setReport(mail.getReport());
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetReportRs, builder.build());
	}

	/**
	 * 
	 * Method: sendWorldChat
	 * 
	 * @Description: 发送世界消息 @param chat @return void @throws
	 */
	public void sendWorldChat(Chat chat) {
		ChatPB b = ChatDataManager.getInst().addWorldChat(chat);
		SynChatRq.Builder builder = SynChatRq.newBuilder();
		builder.setChat(b);
		MsgHelper.synToAllPlayer(RequestCode.SynChatRq, builder.build());
	}

	/**
	 * 
	 * Method: sendHornChat
	 * 
	 * @Description: 发送喇叭消息 @param chat @param style @return void @throws
	 */
	public void sendHornChat(Chat chat, int style) {
		ChatPB b = ChatDataManager.getInst().addHornChat(chat, style);
		SynChatRq.Builder builder = SynChatRq.newBuilder();
		builder.setChat(b);
		MsgHelper.synToAllPlayer(RequestCode.SynChatRq, builder.build());
	}

	public boolean sendPrivateChat(Chat chat, long lordId) {
		Player player = PlayerDataManager.getInst().getPlayer(lordId);
		if (player != null && player.isLogin) {
			ChatPB b = ChatDataManager.getInst().createPrivateChat(chat);

			SynChatRq.Builder builder = SynChatRq.newBuilder();
			builder.setChat(b);

			ServerManager.getInst().getAllGate().forEach(e -> {
				MsgHelper.sendRequest(e.getIoSession(), lordId, RequestCode.SynChatRq, builder.build());
			});

			return true;
		} else {
			return false;
		}
	}

	public void sendPartyChat(Chat chat, int partyId) {
		ChatPB b = ChatDataManager.getInst().addPartyChat(chat, partyId);
		SynChatRq.Builder builder = SynChatRq.newBuilder();
		builder.setChat(b);

		List<Member> list = PartyDataManager.getInst().getMemberList(partyId);
		Player player = null;
		for (Member member : list) {
			player = PlayerDataManager.getInst().getPlayer(member.getLordId());
			if (player != null && player.isLogin) {

				ServerManager.getInst().getAllGate().forEach(e -> {
					MsgHelper.sendRequest(e.getIoSession(), member.getLordId(), RequestCode.SynChatRq, builder.build());
				});
			}
		}
	}

	// public void sendGmChat(Chat chat) {
	//
	// }

	public void doChat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		DoChatRq req = msg.get();
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
			LogHelper.ERROR_LOGGER.error("dochat nul!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + playerId);
		}

		if (player.lord.getSilence() > 0) {
//			handler.sendErrorMsgToPlayer(GameError.CHAT_SILENCE);
			return;
		}

		int now = DateUtil.getToday();
		if (now - player.chatTime < 5) {
//			handler.sendErrorMsgToPlayer(GameError.CHAT_CD);
			return;
		}

		int channel = req.getChannel();
		int shareType = 0;

		List<String> msgList = req.getMsgList();

		if (req.hasField(DoChatRq.getDescriptor().findFieldByName("shareType"))) {
			shareType = req.getShareType();
		}

		if (shareType == 0) {// 聊天
			if (msgList.isEmpty()) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			String content = msgList.get(0);
			if (content.length() > 40) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_CHAT_LENTH);
				return;
			}

			content = EmojiHelper.filterEmoji(content);

			if (ChatHelper.isCorrect(content)) {
				content = "*******";
			}

			Chat chat = createManChat(player, content);

			if (channel == 1) {// 世界频道
				LogHelper.CHAT_LOGGER.trace("wordChat|" + player.lord.getNick() + "|" + player.roleId + "|" + content);
				sendWorldChat(chat);
			} else if (channel == 2) {// 军团
				Member member = PartyDataManager.getInst().getMemberById(player.roleId);
				if (member == null || member.getPartyId() == 0) {
//					handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
					return;
				}

				LogHelper.CHAT_LOGGER.trace("partyChat|" + player.lord.getNick() + "|" + player.roleId + "|" + content);
				sendPartyChat(chat, member.getPartyId());
			} else if (channel == 3) {// 客服

			} else if (channel == 4) {// 私聊
				long target = 0;
				if (!req.hasField(DoChatRq.getDescriptor().findFieldByName("target"))) {
//					handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
					return;
				}

				target = req.getTarget();
				if (!sendPrivateChat(chat, target)) {
//					handler.sendErrorMsgToPlayer(GameError.TARGET_NOT_ONLINE);
					return;
				}

				LogHelper.CHAT_LOGGER.trace("privateChat|" + player.lord.getNick() + "|" + player.roleId + "|" + content);
			}
		}

		player.chatTime = now;
		DoChatRs.Builder builder = DoChatRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoChatRs, builder.build());
		return;
	}

	public void searchOl(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		SearchOlRq req = msg.get();
		long playerId = packet.getPlayerId();
		SearchOlRs.Builder builder = SearchOlRs.newBuilder();
		Player target = PlayerDataManager.getInst().getOnlinePlayer(req.getName());
		if (target != null) {
			Lord lord = target.lord;
			Man man = new Man(lord.getLordId(), lord.getSex(), lord.getNick(), lord.getPortrait(), lord.getLevel());
			man.setRanks(lord.getRanks());
			man.setFight(lord.getFight());
			man.setPros(lord.getPros());
			man.setProsMax(lord.getProsMax());
			man.setPartyName(PartyDataManager.getInst().getPartyNameByLordId(lord.getLordId()));
			builder.setMan(PbHelper.createManPb(man));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SearchOlRs, builder.build());
	}

}
