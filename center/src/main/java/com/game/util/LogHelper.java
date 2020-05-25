package com.game.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.game.domain.Player;

import data.bean.Account;
import data.bean.Army;
import data.bean.Lord;
import data.bean.RefitQue;
import data.bean.Tank;
import data.bean.TankQue;
import define.AwardFrom;
import define.GoldCost;
import define.ItemFrom;
import util.DateUtil;

public class LogHelper {
	public static Logger FLOW_LOGGER = Logger.getLogger("FLOW");
	public static Logger ERROR_LOGGER = Logger.getLogger("ERROR");
	public static Logger CHANNEL_LOGGER = Logger.getLogger("CHANNEL");
	public static Logger PUBLIC_LOGGER = Logger.getLogger("PUBLIC");
	public static Logger GAME_LOGGER = Logger.getLogger("GAME");
	public static Logger MESSAGE_LOGGER = Logger.getLogger("MESSAGE");
	public static Logger HAUST_LOGGER = Logger.getLogger("HAUST");
	public static Logger SAVE_LOGGER = LogManager.getLogger("SAVE");
	public static Logger GM_LOGGER = LogManager.getLogger("GM");
	public static Logger WAR_LOGGER = LogManager.getLogger("WAR");
	public static Logger BOSS_LOGGER = LogManager.getLogger("BOSS");
	public static Logger ACTIVITY_LOGGER = LogManager.getLogger("ACTIVITY");
	public static Logger CHAT_LOGGER = LogManager.getLogger("CHAT");
	public static Logger PAY_LOGGER = LogManager.getLogger("PAY");
	public static Logger START_LOGGER = LogManager.getLogger("START");
	public static Logger PROP_LOGGER = LogManager.getLogger("PROP");
	public static Logger GOLD_LOGGER = LogManager.getLogger("GOLD");
	public static Logger HERO_LOGGER = LogManager.getLogger("HERO");
	public static Logger GAME_WARN_LOGGER = LogManager.getLogger("GAME_WARN");
	public static Logger GAME_DATA_LOGGER = LogManager.getLogger("GAME_DATA");
	public static Logger GAME_HUN = LogManager.getLogger("GAME_HUN");

	static public void logGoldCost(Lord lord, int cost, GoldCost goldCost) {
		GAME_LOGGER.error("goldCost|" + lord.getLordId() + "|" + lord.getNick() + "|" + goldCost.getCode() + "|" + cost);
	}

	static public void logGoldGive(Lord lord, int give, AwardFrom awardFrom) {
		GAME_LOGGER.error("goldGive|" + lord.getLordId() + "|" + lord.getNick() + "|" + awardFrom.getCode() + "|" + give);
	}

	static public void logItem(Lord lord, ItemFrom itemFrom, int serverId, int type, int id, int count, String param) {
		GAME_LOGGER.error("itemAdd|" + lord.getLordId() + "|" + serverId + "|" + itemFrom.getMsg() + "|" + type + "|" + id + "|" + count + "|" + param);
	}

	static public void logAttachMail(Lord lord, int mailId, int keyId) {
		GAME_LOGGER.error("attachMail|" + lord.getLordId() + "|" + lord.getNick() + "|" + mailId + "|" + keyId);
	}

	static public void logGetAttachMail(Lord lord, int mailId, int keyId) {
		GAME_LOGGER.error("awardMail|" + lord.getLordId() + "|" + lord.getNick() + "|" + mailId + "|" + keyId);
	}

	static public void logDelMail(Lord lord, int mailId, int keyId) {
		GAME_LOGGER.error("delMail|" + lord.getLordId() + "|" + lord.getNick() + "|" + mailId + "|" + keyId);
	}

	static public void logBuildTank(Lord lord, TankQue tankQue) {
		GAME_LOGGER.error("buildTank|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankQue.getTankId() + "|" + tankQue.getCount() + "|"
				+ tankQue.getKeyId() + "|" + tankQue.getPeriod());
	}

	static public void logCompleteTank(Lord lord, TankQue tankQue, Tank tank) {
		GAME_LOGGER.error("completeTank|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankQue.getTankId() + "|" + tankQue.getCount() + "|"
				+ tank.getCount() + "|" + tankQue.getKeyId());
	}

	static public void logRefitTank(Lord lord, RefitQue tankQue) {
		GAME_LOGGER.error("refitTank|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankQue.getTankId() + "|" + tankQue.getCount() + "|"
				+ tankQue.getKeyId() + "|" + tankQue.getPeriod());
	}

	static public void logMilitaryRefitTank(Lord lord, int tankId, int count) {
		GAME_LOGGER.error("militaryRefitTank|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankId + "|" + count);
	}
	
	static public void logUpMilitaryScience(Lord lord, int scienceId , int level) {
		GAME_LOGGER.error("UpMilitaryScience|" + lord.getLordId() + "|" + lord.getNick() + "|" + scienceId + "|" + level);
	}
	
	static public void logUnLockMilitaryGrid(Lord lord, int tankId ,int pos) {
		GAME_LOGGER.error("logUnLockMilitaryGrid|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankId + "|" + pos);
	}

	static public void logCompleteTank(Lord lord, RefitQue tankQue, Tank tank) {
		GAME_LOGGER.error("completeRefit|" + lord.getLordId() + "|" + lord.getNick() + "|" + tankQue.getTankId() + "|" + tankQue.getCount() + "|"
				+ tank.getCount() + "|" + tankQue.getKeyId());
	}

	static public void logAttack(Lord lord, int pos, Army army) {
		GAME_LOGGER.error("attack|" + lord.getLordId() + "|" + lord.getNick() + "|" + pos + "|" + army.getKeyId() + "|" + army.getPeriod());
	}

	static public void logProp(Lord lord, int propId, int count) {
		GAME_LOGGER.error("useProp|" + lord.getLordId() + "|" + lord.getNick() + "|" + propId + "|" + count);
	}

	// static public void logAddProp(Lord lord, int propId, int count) {
	// GAME_LOGGER.error("addProp|" + lord.getLordId() + "|" + lord.getNick() +
	// "|" + propId + "|" + count);
	// }

	static public void logLogin(Player player) {
		Lord lord = player.lord;
		int accountKey = 0;
		int serverId = 0;
		String deviceNo = "";
		int platNo = 0;
		String platId = "";
		String createDate = "";
		if (player.account != null) {
			Account account = player.account;
			accountKey = account.getAccountKey();
			serverId = account.getServerId();
			deviceNo = account.getDeviceNo();
			platNo = account.getPlatNo();
			platId = account.getPlatId();
			if (account.getCreateDate() != null) {
//				createDate = DateHelper.formatDateTime(account.getCreateDate(), DateHelper.format1);
			}
		}
		GAME_LOGGER.error("login|" + lord.getLordId() + "|" + lord.getNick() + "|" + lord.getVip() + "|" + lord.getTopup() + "|" + lord.getProsMax() + "|"
				+ lord.getGold() + "|" + lord.getGoldCost() + "|" + lord.getGoldGive() + "|" + accountKey + "|" + serverId + "|" + lord.getLevel() + "|"
				+ platNo + "|" + platId + "|" + deviceNo + "|" + createDate);
	}

	static public void logActivity(Lord lord, int activityId, int costGold, int type, int id, int count, int serverId) {
		ACTIVITY_LOGGER.error(lord.getLordId() + "|" + activityId + "|" + costGold + "|" + type + "|" + id + "|" + count + "|" + serverId + "|"
				+ DateUtil.getToday());
	}

	static public void logPay(Lord lord, Account account, int serverId, String orderId, String serialId, int amount, String payTime) {
		PAY_LOGGER.error(serverId + "|" + lord.getLordId() + "|" + account.getPlatNo() + "|" + account.getPlatId() + "|" + orderId + "|" + serialId + "|"
				+ amount + "|" + payTime);
	}

	static public void logRegister(Account account) {
		if (account == null) {
			return;
		}
		String createDate = "";
		if (account.getCreateDate() != null) {
//			createDate = DateHelper.formatDateTime(account.getCreateDate(), DateHelper.format1);
		}
		GAME_LOGGER.error("register|" + account.getLordId() + "|" + account.getServerId() + "|" + account.getPlatNo() + "|" + account.getPlatId() + "|"
				+ account.getDeviceNo() + "|" + account.getCreated() + "|" + createDate);
	}
	// static public void logCostProduct(long lordId, int serverId, String
	// platNo, String platId, String deviceNo, String ip, int createRole) {
	// GAME_LOGGER.error("product|" + lordId + "|" + serverId + "|" + platNo +
	// "|" + platId + "|" + deviceNo + "|" + ip + "|" + createRole);
	// }

	// static public void logLogin(Lord lord, Account account, int serverId,
	// String platNo, String serialId, String createTime, String ip, int vip,
	// int level) {
	// GAME_LOGGER.error("Login|" + lord.getLordId() + "|" + serverId + "|" +
	// platNo + "|" + serialId + "|");
	// }

	// static public void logLogin(Lord lord, Account account, int serverId,
	// String platNo, String serialId, String createTime, String ip, int vip,
	// int level) {
	// GAME_LOGGER.error("Login|" + lord.getLordId() + "|" + serverId + "|" +
	// platNo + "|" + serialId + "|");
	// }
}
