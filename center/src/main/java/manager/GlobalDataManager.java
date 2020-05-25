package manager;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.game.domain.GameGlobal;
import com.google.inject.Singleton;
import com.google.protobuf.InvalidProtocolBufferException;

import config.bean.ConfMail;
import config.bean.ConfPartyProp;
import config.provider.ConfMailProvider;
import config.provider.ConfPartyPropProvider;
import data.bean.DbGlobal;
import data.bean.Mail;
import data.provider.GlobalProvider;
import define.MailType;
import inject.BeanManager;
import pb.CommonPb.Report;
import pb.CommonPb.WarRecord;
import util.DateUtil;

@Singleton
public class GlobalDataManager {

	public GameGlobal gameGlobal;

	private int lastGlobalSaveTime = DateUtil.getSecondTime();

	public static GlobalDataManager getInst() {
		return BeanManager.getBean(GlobalDataManager.class);
	}

	@PostConstruct
	public void init() throws InvalidProtocolBufferException {
		initGlobal();
	}

	private void initGlobal() throws InvalidProtocolBufferException {
		gameGlobal = new GameGlobal();
		DbGlobal dbGlobal = GlobalProvider.getInst().getBeanById(0);
		if (dbGlobal == null) {
			dbGlobal = gameGlobal.ser();
			GlobalProvider.getInst().insert(dbGlobal);
			gameGlobal.setGlobalId(dbGlobal.getGlobalId());
		} else {
			gameGlobal.dser(dbGlobal);
		}
	}

	public int getMaxKey() {
		return gameGlobal.maxKey();
	}

	public void addGlobalReportMail(Report report, int moldId, int now, String... param) {
		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(moldId);
		if (staticMail == null) {
			return;
		}
		int type = staticMail.getType();
		Mail mail = new Mail(getMaxKey(), type, moldId, MailType.STATE_READ, now);
		if (param != null) {
			mail.setParam(param);
		}

		mail.setReport(report);

		gameGlobal.getMails().add(mail);
		if (gameGlobal.getMails().size() > 20) {
			gameGlobal.getMails().removeFirst();
		}
	}

	public void addWarRecord(WarRecord warRecord) {
		LinkedList<WarRecord> records = gameGlobal.getWarRecord();
		records.add(warRecord);
		if (records.size() > 20) {
			records.removeFirst();
		}
	}

	public void clearWarRecord() {
		gameGlobal.getWarRecord().clear();
	}

	public LinkedList<WarRecord> getWarRecord() {
		return gameGlobal.getWarRecord();
	}

	public List<Integer> getPartyShop(List<Integer> shopList) {
		int shopTime = gameGlobal.getShopTime();
		int today = DateUtil.getToday();
		int shopTimeDay = shopTime / 10;// 刷新日期
		int shopTimeHour = shopTime - today * 10;// 刷新小时
		boolean flag = false;
		if (shopTimeDay != today) {
			flag = true;
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 12) {
			hour = 0;
		} else if (hour >= 12 && hour < 18) {
			hour = 1;
		} else {
			hour = 2;
		}
		if (shopTimeHour != hour) {
			shopTimeHour = hour;
			flag = true;
		}
		if (flag) {
			shopList.clear();
			gameGlobal.setShopTime(today * 10 + shopTimeHour);
			List<ConfPartyProp> rs = ConfPartyPropProvider.getInst().getAllConfig();
			gameGlobal.getShop().clear();
			for (ConfPartyProp en : rs) {
				gameGlobal.getShop().add(en.getKeyId());
				shopList.add(0);
			}
		}
		return gameGlobal.getShop();
	}
}
