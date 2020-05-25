package network.handler.module.tank;
//package com.game.module;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.AwardFrom;
//import com.game.constant.AwardType;
//import com.game.constant.GameError;
//import com.game.constant.MailType;
//import com.game.constant.PartyType;
//import com.game.domain.Member;
//import com.game.domain.Player;
//import com.game.domain.p.Lord;
//import com.game.domain.p.Mail;
//import com.game.manager.GlobalDataManager;
//import com.game.manager.PartyDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.CommonPb;
//import com.game.pb.GamePb.DelMailRq;
//import com.game.pb.GamePb.DelMailRs;
//import com.game.pb.GamePb.GetMailByIdRq;
//import com.game.pb.GamePb.GetMailByIdRs;
//import com.game.pb.GamePb.GetMailListRq;
//import com.game.pb.GamePb.GetMailListRs;
//import com.game.pb.GamePb.RewardMailRq;
//import com.game.pb.GamePb.RewardMailRs;
//import com.game.pb.GamePb.SendMailRq;
//import com.game.pb.GamePb.SendMailRs;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.TimeHelper;
//
///**
// * @author ChenKui
// * @version 创建时间：2015-9-4 下午3:02:55
// * @declare
// */
//@Service
//public class MailService {
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private GlobalDataManager globalDataManager;
//
//	public void getMailListRq(GetMailListRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		GetMailListRs.Builder builder = GetMailListRs.newBuilder();
//		if (req.hasType()) {
//			int type = req.getType();
//			if (type == MailType.ARENA_MAIL) {
//				Iterator<Mail> it = player.mails.values().iterator();
//				while (it.hasNext()) {
//					Mail mail = it.next();
//					if (mail.getType() == type) {
//						builder.addMailShow(PbHelper.createMailShowPb(mail));
//					}
//				}
//			} else if (type == MailType.ARENA_GLOBAL_MAIL) {
//				Iterator<Mail> it = globalDataManager.gameGlobal.getMails().iterator();
//				while (it.hasNext()) {
//					builder.addMailShow(PbHelper.createMailShowPb(it.next()));
//				}
//			}
//		} else {
//			Iterator<Mail> it = player.mails.values().iterator();
//			while (it.hasNext()) {
//				Mail mail = it.next();
//				if (mail.getType() != MailType.ARENA_MAIL && mail.getType() != MailType.ARENA_GLOBAL_MAIL) {
//					builder.addMailShow(PbHelper.createMailShowPb(mail));
//				}
//			}
//		}
//
//		handler.sendMsgToPlayer(GetMailListRs.ext, builder.build());
//	}
//
//	public void getMailById(GetMailByIdRq req, ClientHandler handler) {
//		int keyId = req.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		GetMailByIdRs.Builder builder = GetMailByIdRs.newBuilder();
//
//		Mail mail = null;
//		int type = 0;
//		if (req.hasType()) {
//			type = req.getType();
//		}
//
//		if (type == MailType.ARENA_GLOBAL_MAIL) {
//			Iterator<Mail> it = globalDataManager.gameGlobal.getMails().iterator();
//			Mail e;
//			while (it.hasNext()) {
//				e = it.next();
//				if (e.getKeyId() == keyId) {
//					mail = e;
//					break;
//				}
//			}
//		} else {
//			mail = player.mails.get(keyId);
//		}
//
//		if (mail == null) {
//			handler.sendErrorMsgToPlayer(GameError.MAIL_NOT_EXIST);
//			return;
//		}
//
//		int state = mail.getState();
//		if (state == MailType.STATE_UNREAD) {
//			mail.setState(MailType.STATE_READ);
//		}
//
//		if (state == MailType.STATE_UNREAD_ITEM) {
//			mail.setState(MailType.STATE_READ_ITEM);
//		}
//
//		builder.setMail(PbHelper.createMailPb(mail));
//		handler.sendMsgToPlayer(GetMailByIdRs.ext, builder.build());
//	}
//
//	// public void getMailRq(ClientHandler handler) {
//	// Player player = playerDataManager.getPlayer(handler.getRoleId());
//	// GetMailRs.Builder builder = GetMailRs.newBuilder();
//	// Iterator<Mail> it = player.mails.values().iterator();
//	// while (it.hasNext()) {
//	// builder.addMail(PbHelper.createMailPb(it.next()));
//	// }
//	// handler.sendMsgToPlayer(GetMailRs.ext, builder.build());
//	// }
//
//	public void sendMailRq(SendMailRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null || player.lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord.getLevel() < 15) {
//			handler.sendErrorMsgToPlayer(GameError.NEED_LV_15);
//			return;
//		}
//		CommonPb.Mail mailp = req.getMail();
//		int type = req.getType();
//		String title = mailp.getTitle();
//		String content = mailp.getContont();
//		if (title.length() > 30) {
//			title = title.substring(0, 30);
//		}
//		if (content.length() > 200) {
//			content = content.substring(0, 200);
//		}
//		SendMailRs.Builder builder = SendMailRs.newBuilder();
//		String sendName = lord.getNick();
//		if (type == 0) {
//			if (mailp.getToNameList().size() == 0) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//				return;
//			}
//			Mail myMail = addMail(player, MailType.SEND_MAIL, MailType.STATE_UNREAD, title, content, sendName);
//			myMail.setToName(mailp.getToNameList());
//			for (String nick : mailp.getToNameList()) {
//
//				Player fPlayer = playerDataManager.getPlayer(nick);
//				Mail mail = addMail(fPlayer, MailType.NORMAL_MAIL, MailType.STATE_UNREAD, title, content, sendName);
//				if (mail != null) {
//					List<String> toName = new ArrayList<String>();
//					toName.add(fPlayer.lord.getNick());
//					mail.setToName(toName);
//
//					playerDataManager.synMailToPlayer(fPlayer, mail);
//				}
//
//			}
//			builder.setMail(PbHelper.createMailPb(myMail));
//		} else {
//			Member member = partyDataManager.getMemberById(handler.getRoleId());
//			if (member == null || member.getJob() != PartyType.LEGATUS) {
//				handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
//				return;
//			}
//			int partyId = member.getPartyId();
//			List<Member> memberList = partyDataManager.getMemberList(partyId);
//			if (memberList == null || memberList.size() == 0) {
//				handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
//				return;
//			}
//			Iterator<Member> it = memberList.iterator();
//			while (it.hasNext()) {
//				Member next = it.next();
//				long memberId = next.getLordId();
//				Player playerMember = playerDataManager.getPlayer(memberId);
//				if (playerMember == null) {
//					continue;
//				}
//				Mail partyMail = addMail(playerMember, MailType.NORMAL_MAIL, MailType.STATE_UNREAD, title, content, sendName);
//				List<String> toName = new ArrayList<String>();
//				toName.add(playerMember.lord.getNick());
//				partyMail.setToName(toName);
//
//				playerDataManager.synMailToPlayer(playerMember, partyMail);
//			}
//
//			// playerDataManager.sendAttachMail(player, awards, moldId, now,
//			// param)
//
//			Mail myMail = addMail(player, MailType.SEND_MAIL, MailType.STATE_UNREAD, title, content, sendName);
//			builder.setMail(PbHelper.createMailPb(myMail));
//		}
//		handler.sendMsgToPlayer(SendMailRs.ext, builder.build());
//	}
//
//	public void rewardMailRq(RewardMailRq req, ClientHandler handler) {
//		int keyId = req.getKeyId();
//		RewardMailRs.Builder builder = RewardMailRs.newBuilder();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Mail mail = player.mails.get(keyId);
//		if (mail == null) {
//			handler.sendErrorMsgToPlayer(GameError.MAIL_NOT_EXIST);
//			return;
//		}
//
//		if (mail.getState() != MailType.STATE_READ_ITEM) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		mail.setState(MailType.STATE_NO_ITEM);
//
//		List<CommonPb.Award> awards = mail.getAward();
//		if (awards != null) {
//			for (CommonPb.Award e : awards) {
//				int type = e.getType();
//				int id = e.getId();
//				int count = e.getCount();
//				if (type != AwardType.RED_PACKET) {
//					int itemKeyId = playerDataManager.addAward(player, type, id, count, AwardFrom.MAIL_ATTACH);
//					builder.addAward(PbHelper.createAwardPb(type, id, count, itemKeyId));
//				} else {
//					int gold = playerDataManager.giveRedPacketGold(player, id, count);
//					builder.addAward(PbHelper.createAwardPb(AwardType.GOLD, 0, gold));
//				}
//			}
//		}
//
//		LogHelper.logGetAttachMail(player.lord, mail.getMoldId(), mail.getKeyId());
//
//		handler.sendMsgToPlayer(RewardMailRs.ext, builder.build());
//	}
//
//	// public void readMailRq(ReadMailRq req, ClientHandler handler) {
//	// long keyId = req.getKeyId();
//	// ReadMailRs.Builder builder = ReadMailRs.newBuilder();
//	// Player player = playerDataManager.getPlayer(handler.getRoleId());
//	// Mail mail = player.mails.get(keyId);
//	// if (mail == null) {
//	// handler.sendErrorMsgToPlayer(GameError.MAIL_NOT_EXIST);
//	// return;
//	// }
//	// int state = mail.getState();
//	// if (state == MailType.STATE_UNREAD) {
//	// mail.setState(MailType.STATE_READ);
//	// }
//	// if (state == MailType.STATE_UNREAD_ITEM) {
//	// mail.setState(MailType.STATE_READ_ITEM);
//	// }
//	// handler.sendMsgToPlayer(ReadMailRs.ext, builder.build());
//	// }
//
//	public void delMailRq(DelMailRq req, ClientHandler handler) {
//		long keyId = req.getKeyId();
//		DelMailRs.Builder builder = DelMailRs.newBuilder();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (keyId > 0) {
//			Iterator<Mail> it = player.mails.values().iterator();
//			while (it.hasNext()) {
//				Mail mail = it.next();
//				if (keyId == mail.getKeyId()) {
//					it.remove();
//					break;
//				}
//			}
//		} else {
//			int type = req.getType();
//			Iterator<Mail> it = player.mails.values().iterator();
//			while (it.hasNext()) {
//				Mail mail = it.next();
//				if (mail.getType() == type) {
//					it.remove();
//				}
//			}
//		}
//		handler.sendMsgToPlayer(DelMailRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * @param player
//	 * @param type1玩家邮件2自己发送邮件
//	 * @param state1未读2已读3未读含附件4已读含附件5已读已领附件
//	 * @param title标题
//	 * @param content内容
//	 * @param sendName发送者昵称
//	 * @return
//	 */
//	private Mail addMail(Player player, int type, int state, String title, String content, String sendName) {
//		if (player == null) {
//			return null;
//		}
//		Mail mail = new Mail(player.maxKey(), type, state, TimeHelper.getCurrentSecond(), title, content, sendName);
//		Iterator<Mail> it = player.mails.values().iterator();
//		int count = 0;
//		Mail toDelMail = null;
//		while (it.hasNext()) {
//			Mail next = it.next();
//			if (next.getType() == type) {
//				count++;
//				if (toDelMail == null) {
//					toDelMail = next;
//				} else {
//					if (toDelMail.getTime() > next.getTime()) {
//						toDelMail = next;
//					}
//				}
//			}
//		}
//		if (type == MailType.NORMAL_MAIL && count > 50 && toDelMail != null) {
//			player.mails.remove(toDelMail.getKeyId());
//		}
//		if (type == MailType.SEND_MAIL && count > 20 && toDelMail != null) {
//			player.mails.remove(toDelMail.getKeyId());
//		}
//		player.mails.put(mail.getKeyId(), mail);
//		return mail;
//	}
//}
