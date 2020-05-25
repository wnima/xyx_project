package manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;

import config.bean.ConfMail;
import config.provider.ConfMailProvider;
import data.bean.Mail;
import define.MailType;
import domain.Member;
import inject.BeanManager;
import pb.CommonPb.AwardPB;
import pb.CommonPb.Report;
import pb.GamePb.SynMailRq;
import util.DateUtil;

@Singleton
public class MailManager {

	public static MailManager getInst() {
		return BeanManager.getBean(MailManager.class);
	}

	/**
	 * 添加战报邮件,不能包含奖励道具
	 * 
	 * @param player
	 * @param moldId查询MailType中的mold值
	 * @param param没有时则传null
	 * @return
	 */
	public Mail sendReportMail(Player player, Report report, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return null;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_UNREAD, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}

		// if (moldId == MailType.MOLD_ATTACK_PLAYER || moldId ==
		// MailType.MOLD_ATTACK_MINE) {
		// mail.setState(MailType.STATE_READ);
		// }

		mail.setReport(report);

		player.mails.put(mail.getKeyId(), mail);
		tidyMail(player.mails, type, mail.getKeyId(), 50);
		synMailToPlayer(player, mail);
		return mail;
	}

	public void sendArenaReportMail(Player player, Report report, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_UNREAD, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}
		if (moldId == MailType.MOLD_ARENA_3 || moldId == MailType.MOLD_ARENA_4) {
			mail.setState(MailType.STATE_READ);
		}
		mail.setReport(report);

		player.mails.put(mail.getKeyId(), mail);
		tidyMail(player.mails, type, mail.getKeyId(), 20);
		synMailToPlayer(player, mail);
	}

	public void sendWarReportMail(Player player, Report report, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_UNREAD, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}

		mail.setReport(report);

		player.mails.put(mail.getKeyId(), mail);
		tidyMail(player.mails, type, mail.getKeyId(), 20);
		synMailToPlayer(player, mail);
	}

	public Mail createReportMail(Player player, Report report, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return null;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_READ, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}

		mail.setReport(report);

		player.mails.put(mail.getKeyId(), mail);
		tidyMail(player.mails, type, mail.getKeyId(), 50);
		synMailToPlayer(player, mail);
		return mail;
	}

	public void sendNormalMail(Player player, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_UNREAD, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}
		player.mails.put(mail.getKeyId(), mail);
		tidyMail(player.mails, type, mail.getKeyId(), 50);
		synMailToPlayer(player, mail);
	}

	public void sendAttachMail(Player player, List<AwardPB> awards, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(player.maxKey(), type, moldId, MailType.STATE_UNREAD_ITEM, now);
		if (param != null && param.length != 0) {
			mail.setParam(param);
		}

		if (!awards.isEmpty()) {
			mail.setAward(awards);
		}

		player.mails.put(mail.getKeyId(), mail);

		LogHelper.logAttachMail(player.lord, moldId, mail.getKeyId());
		tidyMail(player.mails, type, mail.getKeyId(), 50);
		synMailToPlayer(player, mail);
	}

	public void synMailToPlayer(Player target, Mail mail) {
		if (target != null && target.isLogin) {
			SynMailRq.Builder builder = SynMailRq.newBuilder();
			builder.setShow(PbHelper.createMailShowPb(mail));

			// Base.Builder msg =
			// PbHelper.createSynBase(SynMailRq.EXT_FIELD_NUMBER, SynMailRq.ext,
			// builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	/**
	 * 发送系统模板邮件给帮派
	 * 
	 * @param partyId帮派ID
	 * @param moldId查询MailType中的mold值
	 * @param param没有时则传null
	 * @return
	 */
	public void sendMailToParty(int partyId, int moldId, String... param) {
		List<Member> memberList = PartyDataManager.getInst().getMemberList(partyId);
		Iterator<Member> it = memberList.iterator();
		int now = DateUtil.getSecondTime();
		while (it.hasNext()) {
			Member next = it.next();
			Player player = PlayerDataManager.getInst().getPlayer(next.getLordId());
			if (player == null) {
				continue;
			}
			sendNormalMail(player, moldId, now, param);
		}
	}

	/**
	 * 
	 * @param mails
	 * @param type
	 * @param keyId
	 * @param count
	 */
	public void tidyMail(Map<Integer, Mail> mails, int type, int keyId, int count) {
		if (type == MailType.ARENA_GLOBAL_MAIL) {
			return;
		}

		if (mails.size() < count) {
			return;
		}

		int total = 0;
		Iterator<Mail> it = mails.values().iterator();
		while (it.hasNext()) {
			Mail mail = it.next();
			if (mail.getType() == type) {
				if (mail.getKeyId() < keyId) {
					keyId = mail.getKeyId();
				}
				total++;
			}
		}

		if (total > count) {
			mails.remove(keyId);
		}
	}
}
