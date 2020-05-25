package com.game.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import pb.CommonPb;
import pb.CommonPb.WarRecord;
import pb.SerializePb.SerDbActivity;
import pb.SerializePb.SerDbTrend;
import pb.SerializePb.SerLiveTask;
import pb.SerializePb.SerPartyApply;
import pb.SerializePb.SerPartyCombat;
import pb.SerializePb.SerProp;
import pb.SerializePb.SerScience;
import pb.SerializePb.SerTwoValue;
import pb.SerializePb.SerWarRecord;
import pb.SerializePb.SerWeal;
import util.DateUtil;

import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.protobuf.InvalidProtocolBufferException;

import data.bean.Activity;
import data.bean.Form;
import data.bean.LiveTask;
import data.bean.Party;
import data.bean.PartyApply;
import data.bean.PartyCombat;
import data.bean.PartyScience;
import data.bean.Prop;
import data.bean.Trend;
import data.bean.Weal;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-9 下午4:05:25
 * @declare
 */

public class PartyData {
	private int partyId;
	private String partyName;
	private String legatusName;
	private int partyLv;
	private int scienceLv;
	private int wealLv;
	private int lively;
	private int build;
	private long fight;
	private int apply;
	private int applyLv;
	private long applyFight;
	private String slogan;
	private String innerSlogan;
	private String jobName1;
	private String jobName2;
	private String jobName3;
	private String jobName4;
	private int refreshTime;
	private int rank;
	private Weal reportMine = new Weal();
	private Map<Integer, PartyScience> sciences = new HashMap<Integer, PartyScience>();
	private Map<Long, PartyApply> applys = new HashMap<Long, PartyApply>();
	private LinkedList<Trend> trends = new LinkedList<Trend>();
	private Map<Integer, PartyCombat> partyCombats = new HashMap<Integer, PartyCombat>();
	private Map<Integer, LiveTask> liveTasks = new HashMap<Integer, LiveTask>();
	private Map<Integer, Activity> activitys = new HashMap<Integer, Activity>();
	private Map<Integer, Prop> amyProps = new HashMap<Integer, Prop>();
	private List<Integer> shopProps = new ArrayList<Integer>();
	private Map<Integer, List<Long>> donates = new HashMap<Integer, List<Long>>();

	private LinkedList<WarRecord> warRecords = new LinkedList<>();
	private int regLv;
	private int warRank;
	private long regFight;
	private int score;

	private long lastSaveTime;

	public int getPartyId() {
		return partyId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setLastSaveTime(long lastSaveTime) {
		this.lastSaveTime = lastSaveTime;
	}

	public long getLastSaveTime() {
		return lastSaveTime;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getLegatusName() {
		return legatusName;
	}

	public void setLegatusName(String legatusName) {
		this.legatusName = legatusName;
	}

	public int getPartyLv() {
		return partyLv;
	}

	public void setPartyLv(int partyLv) {
		this.partyLv = partyLv;
	}

	public int getScienceLv() {
		return scienceLv;
	}

	public void setScienceLv(int scienceLv) {
		this.scienceLv = scienceLv;
	}

	public int getWealLv() {
		return wealLv;
	}

	public void setWealLv(int wealLv) {
		this.wealLv = wealLv;
	}

	public int getLively() {
		return lively;
	}

	public void setLively(int lively) {
		this.lively = lively;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getApply() {
		return apply;
	}

	public void setApply(int apply) {
		this.apply = apply;
	}

	public int getApplyLv() {
		return applyLv;
	}

	public void setApplyLv(int applyLv) {
		this.applyLv = applyLv;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getInnerSlogan() {
		return innerSlogan;
	}

	public void setInnerSlogan(String innerSlogan) {
		this.innerSlogan = innerSlogan;
	}

	public String getJobName1() {
		return jobName1;
	}

	public void setJobName1(String jobName1) {
		this.jobName1 = jobName1;
	}

	public String getJobName2() {
		return jobName2;
	}

	public void setJobName2(String jobName2) {
		this.jobName2 = jobName2;
	}

	public String getJobName3() {
		return jobName3;
	}

	public void setJobName3(String jobName3) {
		this.jobName3 = jobName3;
	}

	public String getJobName4() {
		return jobName4;
	}

	public void setJobName4(String jobName4) {
		this.jobName4 = jobName4;
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Weal getReportMine() {
		return reportMine;
	}

	public void setReportMine(Weal reportMine) {
		this.reportMine = reportMine;
	}

	public Map<Integer, PartyScience> getSciences() {
		return sciences;
	}

	public void setSciences(Map<Integer, PartyScience> sciences) {
		this.sciences = sciences;
	}

	public Map<Long, PartyApply> getApplys() {
		return applys;
	}

	public void setApplys(Map<Long, PartyApply> applys) {
		this.applys = applys;
	}

	public LinkedList<Trend> getTrends() {
		return trends;
	}

	public void setTrends(LinkedList<Trend> trends) {
		this.trends = trends;
	}

	public Map<Integer, PartyCombat> getPartyCombats() {
		return partyCombats;
	}

	public void setPartyCombats(Map<Integer, PartyCombat> partyCombats) {
		this.partyCombats = partyCombats;
	}

	public long getFight() {
		return fight;
	}

	public void setFight(long fight) {
		this.fight = fight;
	}

	public long getApplyFight() {
		return applyFight;
	}

	public void setApplyFight(long applyFight) {
		this.applyFight = applyFight;
	}

	public Map<Integer, Activity> getActivitys() {
		return activitys;
	}

	public void setActivitys(Map<Integer, Activity> activitys) {
		this.activitys = activitys;
	}

	public Map<Integer, Prop> getAmyProps() {
		return amyProps;
	}

	public void setAmyProps(Map<Integer, Prop> amyProps) {
		this.amyProps = amyProps;
	}

	public List<Integer> getShopProps() {
		return shopProps;
	}

	public void setShopProps(List<Integer> shopProps) {
		this.shopProps = shopProps;
	}

	public List<Long> getDonates(int type) {
		return donates.get(type);
	}

	public void putDonates(int key, List<Long> donateList) {
		this.donates.put(key, donateList);
	}

	// public Map<Integer, List<Long>> getDonates() {
	// return donates;
	// }
	//
	// public void setDonates(Map<Integer, List<Long>> donates) {
	// this.donates = donates;
	// }

	/**
	 * 
	 * 
	 * @param stone
	 * @param iron
	 * @param copper
	 * @param silicon
	 * @param oil
	 */
	public void addMine(int stone, int iron, int copper, int silicon, int oil) {
		reportMine.setStone(reportMine.getStone() + stone);
		reportMine.setIron(reportMine.getIron() + iron);
		reportMine.setCopper(reportMine.getCopper() + copper);
		reportMine.setSilicon(reportMine.getSilicon() + silicon);
		reportMine.setOil(reportMine.getOil() + oil);
	}

	public byte[] serMine() {
		SerWeal.Builder ser = SerWeal.newBuilder();
		ser.setWeal(PbHelper.createWealPb(reportMine));
		return ser.build().toByteArray();
	}

	public void dserMine(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerWeal ser = SerWeal.parseFrom(data);
		CommonPb.Weal e = ser.getWeal();
		reportMine.setStone(e.getStone());
		reportMine.setIron(e.getIron());
		reportMine.setCopper(e.getCopper());
		reportMine.setSilicon(e.getSilicon());
		reportMine.setOil(e.getOil());
	}

	public byte[] serScience() {
		SerScience.Builder ser = SerScience.newBuilder();
		Iterator<PartyScience> it = sciences.values().iterator();
		while (it.hasNext()) {
			ser.addScience(PbHelper.createPartySciencePb(it.next()));
		}
		return ser.build().toByteArray();
	}

	public void dserScience(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerScience ser = SerScience.parseFrom(data);
		List<CommonPb.Science> list = ser.getScienceList();
		for (CommonPb.Science e : list) {
			PartyScience science = new PartyScience(e.getScienceId(), e.getScienceLv());
			science.setSchedule(e.getSchedule());
			sciences.put(e.getScienceId(), science);
		}
	}

	public byte[] serActivity() {
		SerDbActivity.Builder ser = SerDbActivity.newBuilder();
		Iterator<Activity> it = activitys.values().iterator();
		while (it.hasNext()) {
			ser.addDbActivity(PbHelper.createDbActivityPb(it.next()));
		}
		return ser.build().toByteArray();
	}

	public byte[] serWarRecord() {
		SerWarRecord.Builder ser = SerWarRecord.newBuilder();
		ser.addAllWarRecord(warRecords);
		return ser.build().toByteArray();
	}

	public void dserActivity(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerDbActivity ser = SerDbActivity.parseFrom(data);
		List<CommonPb.DbActivity> list = ser.getDbActivityList();
		for (CommonPb.DbActivity e : list) {
			Activity activity = new Activity();
			activity.setActivityId(e.getActivityId());
			activity.setBeginTime(e.getBeginTime());
			activity.setEndTime(e.getEndTime());
			activity.setOpen(e.getOpen());
			List<Long> statusList = new ArrayList<Long>();
			for (Long ee : e.getStatusList()) {
				statusList.add(ee);
			}
			activity.setStatusList(statusList);
			List<CommonPb.TwoInt> tlist = e.getTowIntList();
			Map<Integer, Integer> statusMap = new HashMap<Integer, Integer>();
			if (tlist != null) {
				for (CommonPb.TwoInt et : tlist) {
					statusMap.put(et.getV1(), et.getV2());
				}
			}
			activity.setStatusMap(statusMap);
			activitys.put(e.getActivityId(), activity);
		}
	}

	public void dserWarRecord(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerWarRecord ser = SerWarRecord.parseFrom(data);
		warRecords.addAll(ser.getWarRecordList());
	}

	public byte[] serTrend() {
		SerDbTrend.Builder ser = SerDbTrend.newBuilder();
		Iterator<Trend> it = trends.iterator();
		while (it.hasNext()) {
			Trend trend = it.next();
			ser.addDbTrend(PbHelper.createDbTrendPb(trend));
		}
		return ser.build().toByteArray();
	}

	public void dserTrend(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerDbTrend ser = SerDbTrend.parseFrom(data);
		List<CommonPb.DbTrend> list = ser.getDbTrendList();
		if (list != null && list.size() > 0) {
			for (CommonPb.DbTrend e : list) {
				Trend trend = new Trend(e.getTrendId(), e.getTrendTime());
				List<String> paramList = e.getParamList();
				if (paramList != null && paramList.size() > 0) {
					int size = paramList.size();
					String[] param = new String[paramList.size()];
					for (int i = 0; i < size; i++) {
						param[i] = paramList.get(i);
					}
					trend.setParam(param);
				}
				trends.add(trend);
			}
		}
	}

	public byte[] serAmyProps() {
		SerProp.Builder ser = SerProp.newBuilder();
		Iterator<Prop> it = amyProps.values().iterator();
		while (it.hasNext()) {
			Prop prop = it.next();
			ser.addProp(PbHelper.createPropPb(prop));
		}
		return ser.build().toByteArray();
	}

	public void dserAmyProps(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerProp ser = SerProp.parseFrom(data);
		List<CommonPb.Prop> list = ser.getPropList();
		if (list != null && list.size() > 0) {
			for (CommonPb.Prop e : list) {
				Prop prop = new Prop(e.getPropId(), e.getCount());
				amyProps.put(prop.getPropId(), prop);
			}
		}
	}

	public String serShopProps() {
		JSONArray jsonArray = new JSONArray();
		for (Integer shopId : shopProps) {
			jsonArray.add(shopId);
		}
		return jsonArray.toString();
	}

	public void dserShopProps(String shopProp) throws InvalidProtocolBufferException {
		if (shopProp == null || shopProp.equals("") || !shopProp.startsWith("[") || !shopProp.endsWith("]")) {
			shopProps.add(0);
			shopProps.add(0);
			shopProps.add(0);
		} else {
			JSONArray array = JSONArray.parseArray(shopProp);
			for (int i = 0; i < array.size(); i++) {
				shopProps.add(array.getInteger(i));
			}
		}
	}

	public byte[] serDonates() {
		SerTwoValue.Builder ser = SerTwoValue.newBuilder();
		List<Long> hallList = donates.get(1);
		if (hallList != null) {
			for (Long lordId : hallList) {
				ser.addTwoValue(PbHelper.createTwoValuePb(1, lordId));
			}
		}
		List<Long> scienceList = donates.get(2);
		if (scienceList != null) {
			for (Long lordId : scienceList) {
				ser.addTwoValue(PbHelper.createTwoValuePb(2, lordId));
			}
		}
		return ser.build().toByteArray();
	}

	public void dserDonates(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerTwoValue ser = SerTwoValue.parseFrom(data);
		List<CommonPb.TwoValue> list = ser.getTwoValueList();
		if (list != null) {
			for (CommonPb.TwoValue e : list) {
				int key = e.getV1();
				List<Long> keyList = donates.get(key);
				if (keyList == null) {
					keyList = new ArrayList<Long>();
					donates.put(key, keyList);
				}
				keyList.add(e.getV2());
			}
		}
	}

	public byte[] serPartyCombat() {
		SerPartyCombat.Builder ser = SerPartyCombat.newBuilder();
		Iterator<PartyCombat> it = partyCombats.values().iterator();
		while (it.hasNext()) {
			PartyCombat partyCombat = it.next();
			ser.addPartyCombat(PbHelper.createPartyCombatPb(partyCombat));
		}
		return ser.build().toByteArray();
	}

	public void dserPartyCombat(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerPartyCombat ser = SerPartyCombat.parseFrom(data);
		List<CommonPb.PartyCombat> list = ser.getPartyCombatList();
		for (CommonPb.PartyCombat e : list) {
			PartyCombat partyCombat = new PartyCombat();
			partyCombat.setCombatId(e.getCombatId());
			partyCombat.setSchedule(e.getSchedule());
			CommonPb.Form eForm = e.getForm();
			Form form = PbHelper.createForm(eForm);
			partyCombat.setForm(form);
			partyCombats.put(e.getCombatId(), partyCombat);
		}
	}

	public byte[] serLiveTask() {
		SerLiveTask.Builder ser = SerLiveTask.newBuilder();
		Iterator<LiveTask> it = liveTasks.values().iterator();
		while (it.hasNext()) {
			ser.addLiveTask(PbHelper.createLiveTaskPb(it.next()));
		}
		return ser.build().toByteArray();
	}

	public void dserLiveTask(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerLiveTask ser = SerLiveTask.parseFrom(data);
		List<CommonPb.LiveTask> list = ser.getLiveTaskList();
		for (CommonPb.LiveTask e : list) {
			LiveTask liveTask = new LiveTask();
			liveTask.setTaskId(e.getTaskId());
			liveTask.setCount(e.getCount());
			liveTasks.put(liveTask.getTaskId(), liveTask);
		}
	}

	public byte[] serPartyApply() {
		SerPartyApply.Builder ser = SerPartyApply.newBuilder();
		Iterator<PartyApply> it = applys.values().iterator();
		while (it.hasNext()) {
			ser.addDbPartyApply(PbHelper.createDbPartyApplyPb(it.next()));
		}
		return ser.build().toByteArray();
	}

	public void dserPartyApply(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerPartyApply ser = SerPartyApply.parseFrom(data);
		List<CommonPb.DbPartyApply> list = ser.getDbPartyApplyList();
		for (CommonPb.DbPartyApply e : list) {
			PartyApply partyApply = new PartyApply();
			partyApply.setLordId(e.getLordId());
			partyApply.setApplyDate(e.getApplyDate());
			applys.put(e.getLordId(), partyApply);
		}
	}

	public PartyData(Party party) {
		this.partyId = party.getPartyId();
		this.partyName = party.getPartyName();
		this.legatusName = party.getLegatusName();
		this.partyLv = party.getPartyLv();
		this.scienceLv = party.getScienceLv();
		this.wealLv = party.getWealLv();
		this.lively = party.getLively();
		this.build = party.getBuild();
		this.fight = party.getFight();
		this.apply = party.getApply();
		this.applyLv = party.getApplyLv();
		this.applyFight = party.getApplyFight();
		this.slogan = party.getSlogan();
		this.innerSlogan = party.getInnerSlogan();
		this.jobName1 = party.getJobName1();
		this.jobName2 = party.getJobName2();
		this.jobName3 = party.getJobName3();
		this.jobName4 = party.getJobName4();
		this.refreshTime = party.getRefreshTime();
		this.score = party.getScore();

		lastSaveTime = DateUtil.getSecondTime() + RandomHelper.randomInSize(180);

		try {
			dserMine(party.getMine());
			dserScience(party.getScience());
			dserActivity(party.getActivity());
			dserPartyApply(party.getApplyList());
			dserTrend(party.getTrend());
			dserAmyProps(party.getAmyProps());
			dserShopProps(party.getShopProps());
			dserPartyCombat(party.getPartyCombat());
			dserLiveTask(party.getLiveTask());
			dserWarRecord(party.getWarRecord());
			dserDonates(party.getDonates());
			this.regLv = party.getRegLv();
			this.warRank = party.getWarRank();
			this.regFight = party.getRegFight();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Method: copyData
	 * 
	 * @Description: 拷贝数据给保存线程使用
	 * @return
	 * @return Party
	 * @throws
	 */
	public Party copyData() {
		Party party = new Party();
		party.setPartyId(partyId);
		party.setPartyName(new String(partyName));
		party.setLegatusName(new String(legatusName));
		party.setPartyLv(partyLv);
		party.setScienceLv(scienceLv);
		party.setWealLv(wealLv);
		party.setLively(lively);
		party.setBuild(build);
		party.setFight(fight);
		party.setApply(apply);
		party.setApplyLv(applyLv);
		party.setApplyFight(applyFight);
		party.setSlogan(slogan);
		party.setInnerSlogan(innerSlogan);
		party.setJobName1(jobName1);
		party.setJobName2(jobName2);
		party.setJobName3(jobName3);
		party.setJobName4(jobName4);
		party.setRefreshTime(refreshTime);
		party.setMine(serMine());
		party.setScience(serScience());
		party.setActivity(serActivity());
		party.setApplyList(serPartyApply());
		party.setTrend(serTrend());
		party.setAmyProps(serAmyProps());
		party.setShopProps(serShopProps());
		party.setDonates(serDonates());
		party.setPartyCombat(serPartyCombat());
		party.setLiveTask(serLiveTask());
		party.setWarRecord(serWarRecord());
		party.setRegLv(regLv);
		party.setWarRank(warRank);
		party.setRegFight(regFight);
		party.setScore(score);
		return party;
	}

	public Map<Integer, LiveTask> getLiveTasks() {
		return liveTasks;
	}

	public void setLiveTasks(Map<Integer, LiveTask> liveTasks) {
		this.liveTasks = liveTasks;
	}

	public LinkedList<WarRecord> getWarRecords() {
		return warRecords;
	}

	public void setWarRecords(LinkedList<WarRecord> warRecords) {
		this.warRecords = warRecords;
	}

	public int getRegLv() {
		return regLv;
	}

	public void setRegLv(int regLv) {
		this.regLv = regLv;
	}

	public int getWarRank() {
		return warRank;
	}

	public void setWarRank(int warRank) {
		this.warRank = warRank;
	}

	public long getRegFight() {
		return regFight;
	}

	public void setRegFight(long regFight) {
		this.regFight = regFight;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// public Party getSerParty() {
	// party.setMine(serMine());
	// party.setScience(serScience());
	// party.setApplyList(serPartyApply());
	// return party;
	// }
}
