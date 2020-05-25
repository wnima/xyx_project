package com.game.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;

import pb.CommonPb;
import pb.CommonPb.SeniorPartyScore;
import pb.CommonPb.SeniorScore;
import pb.CommonPb.WarRecord;
import pb.SerializePb.SerMail;
import pb.SerializePb.SerOneLong;
import pb.SerializePb.SerSeniorPartyScore;
import pb.SerializePb.SerSeniorScore;
import pb.SerializePb.SerWarRecord;
import com.game.util.PbHelper;
import com.google.protobuf.InvalidProtocolBufferException;

import data.bean.DbGlobal;
import data.bean.Mail;

public class GameGlobal {
	private int globalId;
	private int maxKey;
	private LinkedList<Mail> mails = new LinkedList<Mail>();

	private int warTime;
	private LinkedList<WarRecord> warRecord = new LinkedList<>();

	/**
	 * 百团混战状态 1.报名中 2.准备阶段 3.战斗中 4.结束 5.取消
	 */
	private int warState;
	private List<Long> winRank = new ArrayList<>();
	private Set<Long> getWinRank = new HashSet<>();

	private int bossTime;
	private int bossLv;
	private int bossWhich;
	private int bossHp;
	private int bossState;
	private List<Long> hurtRank = new ArrayList<>();
	private Set<Long> getHurtRank = new HashSet<>();
	private String bossKiller;
	private List<Integer> shop = new ArrayList<Integer>();
	private int shopTime;

	private LinkedList<SeniorScoreRank> scoreRank = new LinkedList<>();
	private LinkedList<SeniorPartyScoreRank> scorePartyRank = new LinkedList<>();

	private int seniorWeek;

	/**
	 * 军事矿区状态
	 */
	private int seniorState;

	public List<Long> getWinRank() {
		return winRank;
	}

	public void setWinRank(List<Long> winRank) {
		this.winRank = winRank;
	}

	public int getWarTime() {
		return warTime;
	}

	public void setWarTime(int warTime) {
		this.warTime = warTime;
	}

	public int getWarState() {
		return warState;
	}

	public void setWarState(int warState) {
		this.warState = warState;
	}

	public int getGlobalId() {
		return globalId;
	}

	public void setGlobalId(int globalId) {
		this.globalId = globalId;
	}

	public int getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(int maxKey) {
		this.maxKey = maxKey;
	}

	public LinkedList<Mail> getMails() {
		return mails;
	}

	public void setMails(LinkedList<Mail> mails) {
		this.mails = mails;
	}

	public int getBossLv() {
		return bossLv;
	}

	public void setBossLv(int bossLv) {
		this.bossLv = bossLv;
	}

	public int getBossWhich() {
		return bossWhich;
	}

	public void setBossWhich(int bossWhich) {
		this.bossWhich = bossWhich;
	}

	public int getBossHp() {
		return bossHp;
	}

	public void setBossHp(int bossHp) {
		this.bossHp = bossHp;
	}

	public List<Long> getHurtRank() {
		return hurtRank;
	}

	public void setHurtRank(List<Long> hurtRank) {
		this.hurtRank = hurtRank;
	}

	public Set<Long> getGetHurtRank() {
		return getHurtRank;
	}

	public void setGetHurtRank(Set<Long> getHurtRank) {
		this.getHurtRank = getHurtRank;
	}

	public List<Integer> getShop() {
		return shop;
	}

	public void setShop(List<Integer> shop) {
		this.shop = shop;
	}

	public int getShopTime() {
		return shopTime;
	}

	public void setShopTime(int shopTime) {
		this.shopTime = shopTime;
	}

	public LinkedList<SeniorScoreRank> getScoreRank() {
		return scoreRank;
	}

	public void setScoreRank(LinkedList<SeniorScoreRank> scoreRank) {
		this.scoreRank = scoreRank;
	}

	public LinkedList<SeniorPartyScoreRank> getScorePartyRank() {
		return scorePartyRank;
	}

	public void setScorePartyRank(LinkedList<SeniorPartyScoreRank> scorePartyRank) {
		this.scorePartyRank = scorePartyRank;
	}

	public int getSeniorWeek() {
		return seniorWeek;
	}

	public void setSeniorWeek(int seniorWeek) {
		this.seniorWeek = seniorWeek;
	}

	public DbGlobal ser() {
		DbGlobal dbGlobal = new DbGlobal();
		dbGlobal.setGlobalId(globalId);
		dbGlobal.setMaxKey(maxKey);
		dbGlobal.setWarTime(warTime);
		dbGlobal.setWarState(warState);
		dbGlobal.setMails(serMail());
		dbGlobal.setWarRecord(serWarRecord());
		dbGlobal.setGetWinRank(serGetWinRank());
		dbGlobal.setWinRank(serWinRank());
		dbGlobal.setShop(serShop());

		dbGlobal.setBossTime(bossTime);
		dbGlobal.setBossLv(bossLv);
		dbGlobal.setBossWhich(bossWhich);
		dbGlobal.setBossHp(bossHp);
		dbGlobal.setHurtRank(serHurtRank());
		dbGlobal.setGetHurtRank(serGetHurtRank());
		dbGlobal.setBossState(bossState);
		dbGlobal.setBossKiller(bossKiller);
		dbGlobal.setShopTime(shopTime);
		dbGlobal.setSeniorWeek(seniorWeek);
		dbGlobal.setScoreRank(serScoreRank());
		dbGlobal.setScorePartyRank(serScorePartyRank());
		dbGlobal.setSeniorState(seniorState);
		return dbGlobal;
	}

	public void dser(DbGlobal dbGlobal) throws InvalidProtocolBufferException {
		globalId = dbGlobal.getGlobalId();
		maxKey = dbGlobal.getMaxKey();
		warTime = dbGlobal.getWarTime();
		warState = dbGlobal.getWarState();
		dserMail(dbGlobal.getMails());
		dserWarRecord(dbGlobal.getWarRecord());
		dserGetWinRank(dbGlobal.getGetWinRank());
		dserWinRank(dbGlobal.getWinRank());
		dserShop(dbGlobal.getShop());

		bossTime = dbGlobal.getBossTime();
		bossLv = dbGlobal.getBossLv();
		bossWhich = dbGlobal.getBossWhich();
		bossHp = dbGlobal.getBossHp();
		bossState = dbGlobal.getBossState();
		dserHurtRank(dbGlobal.getHurtRank());
		dserGetHurtRank(dbGlobal.getGetHurtRank());
		bossKiller = dbGlobal.getBossKiller();
		shopTime = dbGlobal.getShopTime();
		dserScoreRank(dbGlobal.getScoreRank());
		dserScorePartyRank(dbGlobal.getScorePartyRank());
		seniorWeek = dbGlobal.getSeniorWeek();
		seniorState = dbGlobal.getSeniorState();
	}

	private byte[] serMail() {
		SerMail.Builder ser = SerMail.newBuilder();
		Iterator<Mail> it = mails.iterator();
		while (it.hasNext()) {
			ser.addMail(PbHelper.createMailPb(it.next()));
		}
		return ser.build().toByteArray();
	}

	private byte[] serWarRecord() {
		SerWarRecord.Builder ser = SerWarRecord.newBuilder();
		ser.addAllWarRecord(warRecord);
		return ser.build().toByteArray();
	}

	private byte[] serGetWinRank() {
		SerOneLong.Builder ser = SerOneLong.newBuilder();
		ser.addAllV(getWinRank);
		return ser.build().toByteArray();
	}

	private byte[] serWinRank() {
		SerOneLong.Builder ser = SerOneLong.newBuilder();
		ser.addAllV(winRank);
		return ser.build().toByteArray();
	}

	private String serShop() {
		JSONArray array = new JSONArray();
		for (Integer v : shop) {
			array.add(v);
		}
		return array.toString();
	}

	private byte[] serHurtRank() {
		SerOneLong.Builder ser = SerOneLong.newBuilder();
		ser.addAllV(hurtRank);
		return ser.build().toByteArray();
	}

	private byte[] serGetHurtRank() {
		SerOneLong.Builder ser = SerOneLong.newBuilder();
		ser.addAllV(getHurtRank);
		return ser.build().toByteArray();
	}

	private byte[] serScoreRank() {
		SerSeniorScore.Builder ser = SerSeniorScore.newBuilder();
		for (SeniorScoreRank one : scoreRank) {
			ser.addSeniorScore(one.ser());
		}
		return ser.build().toByteArray();
	}

	private byte[] serScorePartyRank() {
		SerSeniorPartyScore.Builder ser = SerSeniorPartyScore.newBuilder();
		for (SeniorPartyScoreRank one : scorePartyRank) {
			ser.addSeniorPartyScore(one.ser());
		}

		return ser.build().toByteArray();
	}

	private void dserWarRecord(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerWarRecord ser = SerWarRecord.parseFrom(data);
		warRecord.addAll(ser.getWarRecordList());
	}

	private void dserGetWinRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerOneLong ser = SerOneLong.parseFrom(data);
		getWinRank.addAll(ser.getVList());
	}

	private void dserWinRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerOneLong ser = SerOneLong.parseFrom(data);
		winRank.addAll(ser.getVList());
	}

	private void dserShop(String data) throws InvalidProtocolBufferException {
		if (data == null || data.equals("")) {
			return;
		}
		JSONArray json = JSONArray.parseArray(data);
		for (int i = 0; i < json.size(); i++) {
			shop.add(json.getInteger(i));
		}
	}

	private void dserHurtRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerOneLong ser = SerOneLong.parseFrom(data);
		hurtRank.addAll(ser.getVList());
	}

	private void dserGetHurtRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerOneLong ser = SerOneLong.parseFrom(data);
		getHurtRank.addAll(ser.getVList());
	}

	private void dserScoreRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerSeniorScore ser = SerSeniorScore.parseFrom(data);
		for (SeniorScore one : ser.getSeniorScoreList()) {
			scoreRank.add(new SeniorScoreRank(one));
		}

	}

	private void dserScorePartyRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}

		SerSeniorPartyScore ser = SerSeniorPartyScore.parseFrom(data);
		for (SeniorPartyScore one : ser.getSeniorPartyScoreList()) {
			scorePartyRank.add(new SeniorPartyScoreRank(one));
		}
	}

	private void dserMail(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerMail serMail = SerMail.parseFrom(data);
		List<CommonPb.Mail> list = serMail.getMailList();
		for (CommonPb.Mail e : list) {
			Mail mail = new Mail();
			mail.setKeyId(e.getKeyId());
			if (e.hasField(CommonPb.Mail.getDescriptor().findFieldByName("title"))) {
				mail.setTitle(e.getTitle());
			}

			if (e.hasField(CommonPb.Mail.getDescriptor().findFieldByName("contont"))) {
				mail.setContont(e.getContont());
			}

			if (e.hasField(CommonPb.Mail.getDescriptor().findFieldByName("sendName"))) {
				mail.setSendName(e.getSendName());
			}

			if (e.hasField(CommonPb.Mail.getDescriptor().findFieldByName("moldId"))) {
				mail.setMoldId(e.getMoldId());
			}

			mail.setState(e.getState());
			mail.setTime(e.getTime());
			mail.setType(e.getType());

			mail.setToName(e.getToNameList());

			mail.setAward(e.getAwardList());

			List<String> paramList = e.getParamList();
			if (paramList != null && paramList.size() > 0) {
				String[] param = new String[paramList.size()];
				for (int i = 0; i < param.length; i++) {
					param[i] = paramList.get(i);
				}
				mail.setParam(param);
			}
			if (e.hasReport()) {
				mail.setReport(e.getReport());
			}

			mails.add(mail);
		}
	}

	public int maxKey() {
		return ++maxKey;
	}

	public LinkedList<WarRecord> getWarRecord() {
		return warRecord;
	}

	public void setWarRecord(LinkedList<WarRecord> warRecord) {
		this.warRecord = warRecord;
	}

	public Set<Long> getGetWinRank() {
		return getWinRank;
	}

	public void setGetWinRank(Set<Long> getWinRank) {
		this.getWinRank = getWinRank;
	}

	public int getBossTime() {
		return bossTime;
	}

	public void setBossTime(int bossTime) {
		this.bossTime = bossTime;
	}

	public int getBossState() {
		return bossState;
	}

	public void setBossState(int bossState) {
		this.bossState = bossState;
	}

	public String getBossKiller() {
		return bossKiller;
	}

	public void setBossKiller(String bossKiller) {
		this.bossKiller = bossKiller;
	}

	public int getSeniorState() {
		return seniorState;
	}

	public void setSeniorState(int seniorState) {
		this.seniorState = seniorState;
	}
}
