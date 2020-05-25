package com.game.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.game.util.PbHelper;
import com.google.protobuf.InvalidProtocolBufferException;

import data.bean.Account;
import data.bean.Activity;
import data.bean.Army;
import data.bean.Bless;
import data.bean.BuildQue;
import data.bean.Building;
import data.bean.Cash;
import data.bean.Chip;
import data.bean.Combat;
import data.bean.Effect;
import data.bean.Equip;
import data.bean.FailNum;
import data.bean.Form;
import data.bean.Friend;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.LordData;
import data.bean.LotteryEquip;
import data.bean.Mail;
import data.bean.MilitaryMaterial;
import data.bean.MilitaryScience;
import data.bean.MilitaryScienceGrid;
import data.bean.Mill;
import data.bean.Part;
import data.bean.Prop;
import data.bean.PropQue;
import data.bean.RefitQue;
import data.bean.Resource;
import data.bean.Ruins;
import data.bean.Science;
import data.bean.ScienceQue;
import data.bean.Store;
import data.bean.Tank;
import data.bean.TankQue;
import data.bean.Task;
import io.netty.channel.ChannelHandlerContext;
import pb.CommonPb;
import pb.SerializePb.SerData;
import pb.SerializePb.SerData.Builder;
import pb.SerializePb.SerMail;
import util.DateUtil;

public class Player {
	// 角色基础数据
	public Lord lord;

	// 角色账号
	public Account account;

	// 角色坦克列表
	public HashMap<Integer, Tank> tanks = new HashMap<Integer, Tank>();

	// 角色资源数据
	public Resource resource;

	// 角色基地建筑数据
	public Building building;

	// 阵型数据 [1]阵型模板 [2]防守阵型
	public Map<Integer, Form> forms = new HashMap<>();

	// 建筑队列
	public LinkedList<BuildQue> buildQue = new LinkedList<>();

	// 出征部队
	public LinkedList<Army> armys = new LinkedList<>();

	// 失败的次数 [1]操作keyId [2]失败
	public Map<Integer, FailNum> failNums = new HashMap<>();

	// 废墟信息
	public Ruins ruins = new Ruins();

	// 坦克生产队列
	public LinkedList<TankQue> tankQue_1 = new LinkedList<>();

	// 坦克生产队列
	public LinkedList<TankQue> tankQue_2 = new LinkedList<>();

	// 道具数据
	public HashMap<Integer, Prop> props = new HashMap<>();

	// 制造车间生产队列
	public LinkedList<PropQue> propQue = new LinkedList<>();

	// 改装队列
	public LinkedList<RefitQue> refitQue = new LinkedList<>();

	// 角色装备数据
	public HashMap<Integer, HashMap<Integer, Equip>> equips = new HashMap<>();

	// 配件数据
	public HashMap<Integer, HashMap<Integer, Part>> parts = new HashMap<>();

	// 碎片数据
	public HashMap<Integer, Chip> chips = new HashMap<>();

	public HashMap<Integer, Integer> skills = new HashMap<>();

	// 科技馆数据
	public HashMap<Integer, Science> sciences = new HashMap<>();

	// 科技生产队列
	public LinkedList<ScienceQue> scienceQue = new LinkedList<>();

	// 普通副本
	public HashMap<Integer, Combat> combats = new HashMap<>();

	// 探险副本（装备、配件、限时）
	public HashMap<Integer, Combat> explores = new HashMap<>();

	// 章节(宝箱领取状态)
	public HashMap<Integer, Integer> sections = new HashMap<>();

	// 城外工厂
	public HashMap<Integer, Mill> mills = new HashMap<>();

	// 加成效果
	public HashMap<Integer, Effect> effects = new HashMap<>();

	public List<Integer> signs = new ArrayList<Integer>();

	// 军工材料
	public HashMap<Integer, MilitaryMaterial> militaryMaterials = new HashMap<Integer, MilitaryMaterial>();

	// 军工科技 (科技id,科技信息)
	public HashMap<Integer, MilitaryScience> militarySciences = new HashMap<Integer, MilitaryScience>();

	// 军工科技格子状态(tankId,pos,状态)
	public HashMap<Integer, HashMap<Integer, MilitaryScienceGrid>> militaryScienceGrids = new HashMap<Integer, HashMap<Integer, MilitaryScienceGrid>>();

	// 普通副本进度
	public int combatId;

	// 装备副本进度
	public int equipEplrId;

	// 配件副本进度
	public int partEplrId;

	// 军工副本进度
	public int militaryEplrId;

	// 极限副本进度
	public int extrEplrId = 300;

	// 极限历史最高层数
	public int extrMark;

	// 扫荡极限副本开始时间
	public int wipeTime;

	// 限时副本进度
	public int timePrlrId;

	// 签到登录奖励
	public int signLogin;

	// 军事矿区时间(刷新掠夺次数，购买次数)
	public int seniorDay;

	// // 参加军事矿区时间(刷新积分)
	// public int seniorWeek;

	// 军事矿区次数
	public int seniorCount;

	// 军事矿区积分
	public int seniorScore;

	// 军事矿区军团积分奖励是否领取了 0.无资格 1.有资格 2.已领取
	public int seniorAward;

	// 军事矿区掠夺购买次数
	public int seniorBuy;

	// 将领集合
	public HashMap<Integer, Hero> heros = new HashMap<>();

	// 好友列表
	public HashMap<Long, Friend> friends = new HashMap<>();

	// 祝福列表
	public HashMap<Long, Bless> blesses = new HashMap<>();

	// 收藏坐标
	public LinkedList<Store> coords = new LinkedList<>();

	// 邮件列表
	public Map<Integer, Mail> mails = new HashMap<Integer, Mail>();

	// 抽装备信息
	public Map<Integer, LotteryEquip> lotteryEquips = new HashMap<>();

	// 主线,日常,活跃任务
	public Map<Integer, Task> majorTasks = new HashMap<>();
	public List<Task> dayiyTask = new ArrayList<Task>();
	public Map<Integer, Task> liveTask = new HashMap<>();

	// 任务信息
	public Map<Integer, Activity> activitys = new HashMap<>();

	// 兑换(装备,配件)
	public Map<Integer, Cash> cashs = new HashMap<>();

	private int maxKey;

	// 发布军团招募消息的时间
	public int recruitTime = 0;

	// 上次发送聊天时间
	public int chatTime = 0;

	public int maxKey() {
		return ++maxKey;
	}

	public int getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(int maxKey) {
		this.maxKey = maxKey;
	}

	{
		equips.put(0, new HashMap<Integer, Equip>());
		equips.put(1, new HashMap<Integer, Equip>());
		equips.put(2, new HashMap<Integer, Equip>());
		equips.put(3, new HashMap<Integer, Equip>());
		equips.put(4, new HashMap<Integer, Equip>());
		equips.put(5, new HashMap<Integer, Equip>());
		equips.put(6, new HashMap<Integer, Equip>());

		parts.put(0, new HashMap<Integer, Part>());
		parts.put(1, new HashMap<Integer, Part>());
		parts.put(2, new HashMap<Integer, Part>());
		parts.put(3, new HashMap<Integer, Part>());
		parts.put(4, new HashMap<Integer, Part>());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// 角色id，一个物理区上唯一
	public Long roleId;

	public int surface;

	public int lastSaveTime;

	public boolean isLogin = false;

	public boolean immediateSave = false;

	// public boolean connected = false;

	public ChannelHandlerContext ctx;

	public SaveFlag saveFlag = new SaveFlag();

	public Player(Lord lord, int nowTime) {
		this.roleId = lord.getLordId();
		this.lord = lord;
		// int t = 400 - (int) (roleId % 300);
		// lastSaveTime = nowTime + t;
		// LogHelper.ERROR_LOGGER.error("lastSaveTime:" + t + "|" + roleId);

		lastSaveTime = nowTime + 180 + (int) (roleId % 300);
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public void logOut() {
		isLogin = false;
		ctx = null;
		immediateSave = true;

		int now = DateUtil.getSecondTime();
		lord.setOffTime(now);
		lord.setOlTime(onLineTime());
	}

	public void tickOut() {
		if (isLogin) {
			int now = DateUtil.getSecondTime();
			lord.setOffTime(now);
			lord.setOlTime(onLineTime());
		}

		isLogin = false;
		ctx = null;
		immediateSave = true;
	}

	public void logIn() {
		int now = DateUtil.getSecondTime();
		int nowDay = DateUtil.getToday();

//		int lastDay = TimeHelper.getDay(lord.getOnTime());
//		if (nowDay != lastDay) {
//			// 重置每月登录天数
//			int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//			if ((lord.getOlMonth() / 10000) != monthAndDay / 10000) {// 月份不一样
//				lord.setOlMonth(monthAndDay + 1);
//			} else {// 月份一样
//				if (lord.getOlMonth() / 100 != monthAndDay / 100) {
//					lord.setOlMonth(monthAndDay + lord.getOlMonth() % 100 + 1);
//				}
//			}
//
//			int ctTime = lord.getCtTime();
//			if (TimeHelper.getDay(ctTime) != TimeHelper.getDay(now)) {
//				lord.setCtTime(now);
//				lord.setOlAward(0);
//			}
//
//			// 重置前一次的登录时长
//			int offTime = TimeHelper.getDay(lord.getOffTime());
//			if (offTime != nowDay) {
//				lord.setOlTime(0);
//				lord.setOffTime(nowDay);
//			}
//		}

		lord.setOnTime(now);
	}

	/**
	 * 在线时长
	 * 
	 * @return
	 */
	public int onLineTime() {
		int now = DateUtil.getSecondTime();
		int nowDay = DateUtil.getToday();

//		int lastDay = TimeHelper.getDay(lord.getOnTime());
//		if (nowDay != lastDay) {// 登录时间不为当天,则取0点到当前时间
//			int noTime = TimeHelper.getTodayZone(now);
//			int ctDay = TimeHelper.getDay(lord.getCtTime());
//			if (ctDay != nowDay) {
//				lord.setCtTime(noTime);
//				lord.setOlAward(0);
//			}
//			return now - noTime;
//		} else {// 登录时间为当天,则取累积时长
//			int onlineTime = lord.getOlTime() + now - lord.getOnTime();
//			onlineTime = onlineTime > 86400 ? 86400 : onlineTime;
//			return onlineTime;
//		}
		return 0;
	}

	public boolean isActive() {
		if (account == null) {
			// 说明lord存在，但account不存在其不在smallid表中。出现这种情况是因为手动关联了lord产生的多余数据没有处理
			// 将其加入到smallId中即可
			return false;
		}

		return account.getCreated() == 1 && lord.getLevel() > 2;
	}

	public int getSignLogin() {
		return signLogin;
	}

	public void setSignLogin(int signLogin) {
		this.signLogin = signLogin;
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

			mails.put(mail.getKeyId(), mail);
		}
	}

	private byte[] serMail() {
		SerMail.Builder ser = SerMail.newBuilder();
		Iterator<Mail> it = mails.values().iterator();
		while (it.hasNext()) {
			ser.addMail(PbHelper.createMailPb(it.next()));
		}
		return ser.build().toByteArray();
	}

	// /**
	// *
	// * Method: serData
	// *
	// * @Description: 序列化数据
	// * @return
	// * @return Data
	// * @throws
	// */
	// public Data serData() {
	// Data data = new Data();
	// data.setLordId(roleId);
	// data.setTank(serTank());
	// data.setChip(serChip());
	// data.setSkill(serSkill());
	// data.setCombat(serCombat());
	// data.setExplore(serExplore());
	// data.setSection(serSection());
	// data.setMill(serMill());
	// data.setEffect(serEffect());
	// data.setEquip(serEquip());
	// data.setPart(serPart());
	// data.setProp(serProp());
	// data.setForm(serForm());
	// data.setScience(serScience());
	// data.setHero(serHero());
	// data.setFriend(serFriend());
	// data.setBless(serBless());
	// data.setMail(serMail());
	// data.setCoord(serCoord());
	// data.setSign(serSign());
	// data.setMajorTasks(serMajorTask());
	// data.setDayiyTask(serDayiyTask());
	// data.setLiveTask(serLiveTask());
	// data.setLotteryEquip(serLotteryEquip());
	// data.setTankQue1(serTankQue(tankQue_1));
	// data.setTankQue2(serTankQue(tankQue_2));
	// data.setRefitQue(serRefitQue());
	// data.setPropQue(serPropQue());
	// data.setBuildQue(serBuildQue());
	// data.setScienceQue(serScienceQue());
	// data.setArmy(serArmy());
	// data.setActivity(serActivity());
	//
	// data.setMaxKey(maxKey);
	// data.setCombatId(combatId);
	// data.setEquipEplrId(equipEplrId);
	// data.setPartEplrId(partEplrId);
	// data.setExtrEplrId(extrEplrId);
	// data.setExtrMark(extrMark);
	// data.setWipeTime(wipeTime);
	// data.setTimePrlrId(timePrlrId);
	// data.setSignLogin(signLogin);
	// return data;
	// }

	private byte[] serRoleData() {
		SerData.Builder ser = SerData.newBuilder();
		serTank(ser);
		serProp(ser);
		serForm(ser);
		serEquip(ser);
		serPart(ser);
		serChip(ser);
		serSkill(ser);
		serCombat(ser);
		serExplore(ser);
		serSection(ser);
		serMill(ser);
		serScience(ser);
		serHero(ser);
		serFriend(ser);
		serBless(ser);
		serLotteryEquip(ser);
		serTankQue(ser);
		serPropQue(ser);
		serRefitQue(ser);
		serBuildQue(ser);
		serScienceQue(ser);
		serArmy(ser);
		serFailNum(ser);
		serRuins(ser);
		serEffect(ser);
		serSign(ser);
		serMajorTask(ser);
		serDayiyTask(ser);
		serLiveTask(ser);
		serActivity(ser);
		serCash(ser);
		serMilitarySciences(ser);
		serMilitaryScienceGrid(ser);
		serMilitaryMaterials(ser);
		return ser.build().toByteArray();
	}

	/**
	 * Method: serMilitaryMaterials @param ser @return void @throws
	 */
	private void serMilitaryMaterials(Builder ser) {
		Iterator<MilitaryMaterial> it = militaryMaterials.values().iterator();
		while (it.hasNext()) {
			ser.addMilitaryMaterial(PbHelper.createMilitaryMaterialPb(it.next()));
		}
	}

	/**
	 * Method: serMilitaryScienceGrid @Description: 军工科技格子状态 @param ser @return
	 * void @throws
	 */
	private void serMilitaryScienceGrid(Builder ser) {
		Collection<HashMap<Integer, MilitaryScienceGrid>> c = militaryScienceGrids.values();
		for (HashMap<Integer, MilitaryScienceGrid> hashMap : c) {
			Iterator<MilitaryScienceGrid> it = hashMap.values().iterator();
			while (it.hasNext()) {
				ser.addMilitaryScienceGrid(PbHelper.createMilitaryScieneceGridPb(it.next()));
			}
		}
	}

	/**
	 * Method: serMilitarySciences @Description: 军工科技信息 @param ser @return
	 * void @throws
	 */
	private void serMilitarySciences(Builder ser) {
		Iterator<MilitaryScience> it = militarySciences.values().iterator();
		while (it.hasNext()) {
			MilitaryScience next = it.next();
			ser.addMilitaryScience(PbHelper.createMilitaryScienecePb(next));
		}
	}

	private void dserRoleData(SerData ser) {
		dserChip(ser);
		dserSkill(ser);
		dserEquip(ser);
		dserPart(ser);
		dserProp(ser);
		dserForm(ser);
		dserTank(ser);
		dserScience(ser);
		dserActivity(ser);
		dserCombat(ser);
		dserExplore(ser);
		dserSection(ser);
		dserMill(ser);
		dserEffect(ser);
		dserHero(ser);
		dserFriend(ser);
		dserBless(ser);
		dserSign(ser);
		dserMajorTask(ser);
		dserDayiyTask(ser);
		dserLiveTask(ser);
		dserLotteryEquip(ser);
		dserTankQue(ser);
		dserRefitQue(ser);
		dserPropQue(ser);
		dserBuildQue(ser);
		dserScienceQue(ser);
		dserArmy(ser);
		dserFailNum(ser);
		dserCash(ser);
		dserRuins(ser);
		dserMilitarySciences(ser);
		dserMilitaryScienceGrids(ser);
		dserMilitaryMaterials(ser);
	}

	/**
	 * Method: dserMilitaryMaterials @Description: TODO @param ser @return
	 * void @throws
	 */
	private void dserMilitaryMaterials(SerData ser) {
		List<CommonPb.MilitaryMaterial> list = ser.getMilitaryMaterialList();
		for (CommonPb.MilitaryMaterial m : list) {
			militaryMaterials.put(m.getId(), new MilitaryMaterial(m.getId(), m.getCount()));
		}
	}

	/**
	 * Method: dserMilitaryScienceGrids @Description: 解析军工科技格子信息 @param ser @return
	 * void @throws
	 */
	private void dserMilitaryScienceGrids(SerData ser) {
		List<CommonPb.MilitaryScienceGrid> list = ser.getMilitaryScienceGridList();
		for (CommonPb.MilitaryScienceGrid grid : list) {
			HashMap<Integer, MilitaryScienceGrid> map = militaryScienceGrids.get(grid.getTankId());
			if (map == null) {
				map = new HashMap<Integer, MilitaryScienceGrid>();
				militaryScienceGrids.put(grid.getTankId(), map);
			}
			map.put(grid.getPos(), new MilitaryScienceGrid(grid.getTankId(), grid.getPos(), grid.getStatus(), grid.getMilitaryScienceId()));
		}
	}

	/**
	 * Method: dserMilitarySciences @Description: 解析军工科技信息 @return void @throws
	 */
	private void dserMilitarySciences(SerData ser) {
		List<CommonPb.MilitaryScience> list = ser.getMilitaryScienceList();
		for (CommonPb.MilitaryScience tankMilitaryScience : list) {
			militarySciences.put(tankMilitaryScience.getMilitaryScienceId(), new MilitaryScience(tankMilitaryScience.getMilitaryScienceId(), tankMilitaryScience.getLevel(), tankMilitaryScience.getFitTankId(), tankMilitaryScience.getFitPos()));
		}
	}

	public LordData serNewData() {
		LordData dataNew = new LordData();
		dataNew.setLordId(roleId);
		dataNew.setRoleData(serRoleData());
		dataNew.setMail(serMail());

		dataNew.setMaxKey(maxKey);
		dataNew.setCombatId(combatId);
		dataNew.setEquipEplrId(equipEplrId);
		dataNew.setPartEplrId(partEplrId);
		dataNew.setMilitaryEplrId(militaryEplrId);
		dataNew.setExtrEplrId(extrEplrId);
		dataNew.setExtrMark(extrMark);
		dataNew.setWipeTime(wipeTime);
		dataNew.setTimePrlrId(timePrlrId);
		dataNew.setSignLogin(signLogin);
		// dataNew.setSeniorWeek(seniorWeek);
		dataNew.setSeniorDay(seniorDay);
		dataNew.setSeniorCount(seniorCount);
		dataNew.setSeniorScore(seniorScore);
		dataNew.setSeniorAward(seniorAward);
		dataNew.setSeniorBuy(seniorBuy);
		return dataNew;
	}

	public void dserNewData(LordData data) throws InvalidProtocolBufferException {
		roleId = data.getLordId();
		if (data.getRoleData() != null) {
			SerData ser = SerData.parseFrom(data.getRoleData());
			dserRoleData(ser);
		}

		dserMail(data.getMail());
		combatId = data.getCombatId();
		equipEplrId = data.getEquipEplrId();
		partEplrId = data.getPartEplrId();
		militaryEplrId = data.getMilitaryEplrId();
		extrEplrId = data.getExtrEplrId();
		extrMark = data.getExtrMark();
		wipeTime = data.getWipeTime();
		timePrlrId = data.getTimePrlrId();
		signLogin = data.getSignLogin();
		maxKey = data.getMaxKey();
		// seniorWeek = data.getSeniorWeek();
		seniorDay = data.getSeniorDay();
		seniorCount = data.getSeniorCount();
		seniorScore = data.getSeniorScore();
		seniorAward = data.getSeniorAward();
		seniorBuy = data.getSeniorBuy();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void serTank(SerData.Builder ser) {
		Iterator<Tank> it = tanks.values().iterator();
		while (it.hasNext()) {
			ser.addTank(PbHelper.createTankPb(it.next()));
		}
	}

	private void serForm(SerData.Builder ser) {
		Iterator<Form> it = forms.values().iterator();
		while (it.hasNext()) {
			ser.addForm(PbHelper.createFormPb(it.next()));
		}
	}

	private void serChip(SerData.Builder ser) {
		Iterator<Chip> it = chips.values().iterator();
		while (it.hasNext()) {
			ser.addChip(PbHelper.createChipPb(it.next()));
		}
	}

	private void serHero(SerData.Builder ser) {
		Iterator<Hero> it = heros.values().iterator();
		while (it.hasNext()) {
			ser.addHero(PbHelper.createHeroPb(it.next()));
		}
	}

	private void serFriend(SerData.Builder ser) {
		Iterator<Friend> it = friends.values().iterator();
		while (it.hasNext()) {
			ser.addFriend(PbHelper.createDbFriendPb(it.next()));
		}
	}

	private void serBless(SerData.Builder ser) {
		Iterator<Bless> it = blesses.values().iterator();
		while (it.hasNext()) {
			ser.addBless(PbHelper.createDbBlessPb(it.next()));
		}
	}

	private void serSign(SerData.Builder ser) {
		ser.addAllSign(signs);
	}

	private void serMajorTask(SerData.Builder ser) {
		Iterator<Task> it = majorTasks.values().iterator();
		while (it.hasNext()) {
			ser.addMajorTask(PbHelper.createTaskPb(it.next()));
		}
	}

	private void serDayiyTask(SerData.Builder ser) {
		Iterator<Task> it = dayiyTask.iterator();
		while (it.hasNext()) {
			ser.addDayiyTask(PbHelper.createTaskPb(it.next()));
		}
	}

	private void serLiveTask(SerData.Builder ser) {
		Iterator<Task> it = liveTask.values().iterator();
		while (it.hasNext()) {
			ser.addLiveTask(PbHelper.createTaskPb(it.next()));
		}
	}

	private void serLotteryEquip(SerData.Builder ser) {
		Iterator<LotteryEquip> it = lotteryEquips.values().iterator();
		while (it.hasNext()) {
			ser.addLotteryEquip(PbHelper.createDbLotteryEquipPb(it.next()));
		}
	}

	private void serCombat(SerData.Builder ser) {
		Iterator<Combat> it = combats.values().iterator();
		while (it.hasNext()) {
			ser.addCombat(PbHelper.createCombatPb(it.next()));
		}
	}

	private void serExplore(SerData.Builder ser) {
		Iterator<Combat> it = explores.values().iterator();
		while (it.hasNext()) {
			ser.addExplore(PbHelper.createCombatPb(it.next()));
		}
	}

	private void serSection(SerData.Builder ser) {
		for (Map.Entry<Integer, Integer> entry : sections.entrySet()) {
			ser.addSection(PbHelper.createSectionPb(entry.getKey(), entry.getValue()));
		}
	}

	private void serSkill(SerData.Builder ser) {
		for (Map.Entry<Integer, Integer> entry : skills.entrySet()) {
			ser.addSkill(PbHelper.createSkillPb(entry.getKey(), entry.getValue()));
		}
	}

	private void serMill(SerData.Builder ser) {
		Iterator<Mill> it = mills.values().iterator();
		while (it.hasNext()) {
			ser.addMill(PbHelper.createMillPb(it.next()));
		}
	}

	private void serEffect(SerData.Builder ser) {
		Iterator<Effect> it = effects.values().iterator();
		while (it.hasNext()) {
			ser.addEffect(PbHelper.createEffectPb(it.next()));
		}
	}

	private void serEquip(SerData.Builder ser) {
		for (int i = 0; i < 7; i++) {
			Map<Integer, Equip> map = equips.get(i);
			Iterator<Equip> it = map.values().iterator();
			while (it.hasNext()) {
				ser.addEquip(PbHelper.createEquipPb(it.next()));
			}
		}
	}

	private void serPart(SerData.Builder ser) {
		for (int i = 0; i < 5; i++) {
			Map<Integer, Part> map = parts.get(i);
			Iterator<Part> it = map.values().iterator();
			while (it.hasNext()) {
				ser.addPart(PbHelper.createPartPb(it.next()));
			}
		}
	}

	private void serProp(SerData.Builder ser) {
		Iterator<Prop> it = props.values().iterator();
		while (it.hasNext()) {
			ser.addProp(PbHelper.createPropPb(it.next()));
		}
	}

	private void serScience(SerData.Builder ser) {
		Iterator<Science> it = sciences.values().iterator();
		while (it.hasNext()) {
			ser.addScience(PbHelper.createSciencePb(it.next()));
		}
	}

	private void serTankQue(SerData.Builder ser) {
		for (TankQue e : tankQue_1) {
			ser.addTankQue1(PbHelper.createTankQuePb(e));
		}

		for (TankQue e : tankQue_2) {
			ser.addTankQue2(PbHelper.createTankQuePb(e));
		}
	}

	private void serRefitQue(SerData.Builder ser) {
		for (RefitQue e : refitQue) {
			ser.addRefitQue(PbHelper.createRefitQuePb(e));
		}
	}

	private void serBuildQue(SerData.Builder ser) {
		for (BuildQue e : buildQue) {
			ser.addBuildQue(PbHelper.createBuildQuePb(e));
		}
	}

	private void serScienceQue(SerData.Builder ser) {
		for (ScienceQue e : scienceQue) {
			ser.addScienceQue(PbHelper.createScienceQuePb(e));
		}
	}

	private void serPropQue(SerData.Builder ser) {
		for (PropQue e : propQue) {
			ser.addPropQue(PbHelper.createPropQuePb(e));
		}
	}

	private void serArmy(SerData.Builder ser) {
		for (Army e : armys) {
			ser.addArmy(PbHelper.createArmyPb(e));
		}
	}

	private void serFailNum(SerData.Builder ser) {
		Iterator<FailNum> it = failNums.values().iterator();
		while (it.hasNext()) {
			ser.addFailNum(PbHelper.createFailNumPb(it.next()));
		}
	}

	private void serRuins(SerData.Builder ser) {
		ser.setRunis(PbHelper.createRuinsPb(ruins));
	}

	private void serActivity(SerData.Builder ser) {
		Iterator<Activity> it = activitys.values().iterator();
		while (it.hasNext()) {
			Activity next = it.next();
			ser.addActivity(PbHelper.createDbActivityPb(next));
		}
	}

	private void serCash(SerData.Builder ser) {
		Iterator<Cash> it = cashs.values().iterator();
		while (it.hasNext()) {
			Cash next = it.next();
			ser.addCash(PbHelper.createCashPb(next));
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	private void dserTankQue(SerData ser) {
		List<CommonPb.TankQue> list1 = ser.getTankQue1List();
		for (CommonPb.TankQue e : list1) {
			TankQue tankQue = new TankQue(e.getKeyId(), e.getTankId(), e.getCount(), e.getState(), e.getPeriod(), e.getEndTime());
			tankQue_1.add(tankQue);
		}

		List<CommonPb.TankQue> list2 = ser.getTankQue2List();
		for (CommonPb.TankQue e : list2) {
			TankQue tankQue = new TankQue(e.getKeyId(), e.getTankId(), e.getCount(), e.getState(), e.getPeriod(), e.getEndTime());
			tankQue_2.add(tankQue);
		}
	}

	private void dserRefitQue(SerData ser) {
		List<CommonPb.RefitQue> list = ser.getRefitQueList();
		for (CommonPb.RefitQue e : list) {
			RefitQue q = new RefitQue(e.getKeyId(), e.getTankId(), e.getRefitId(), e.getCount(), e.getState(), e.getPeriod(), e.getEndTime());
			refitQue.add(q);
		}
	}

	private void dserBuildQue(SerData ser) {
		List<CommonPb.BuildQue> list = ser.getBuildQueList();
		for (CommonPb.BuildQue e : list) {
			BuildQue q = new BuildQue(e.getKeyId(), e.getBuildingId(), e.getPos(), e.getPeriod(), e.getEndTime());
			buildQue.add(q);
		}
	}

	private void dserPropQue(SerData ser) {
		List<CommonPb.PropQue> list = ser.getPropQueList();
		for (CommonPb.PropQue e : list) {
			PropQue q = new PropQue(e.getKeyId(), e.getPropId(), e.getCount(), e.getState(), e.getPeriod(), e.getEndTime());
			propQue.add(q);
		}
	}

	private void dserArmy(SerData ser) {
		List<CommonPb.Army> list = ser.getArmyList();
		for (CommonPb.Army e : list) {
			Army q = new Army(e);
			armys.add(q);
		}
	}

	private void dserFailNum(SerData ser) {
		List<CommonPb.FailNum> list = ser.getFailNumList();
		for (CommonPb.FailNum e : list) {
			FailNum f = new FailNum(e.getOperType(), e.getNum());
			failNums.put(f.getOperType(), f);
		}
	}

	private void dserRuins(SerData ser) {
		CommonPb.Ruins r = ser.getRunis();
		if (r != null) {
			ruins.setRuins(r.getIsRuins());
			ruins.setLordId(r.getLordId());
			ruins.setAttackerName(r.getAttackerName());
		} else {
			ruins.setRuins(false);
			ruins.setLordId(0);
			ruins.setAttackerName("");
		}
	}

	private void dserCash(SerData ser) {
		List<CommonPb.Cash> list = ser.getCashList();
		for (CommonPb.Cash e : list) {
			Cash q = new Cash(e);
			cashs.put(e.getCashId(), q);
		}
	}

	private void dserScienceQue(SerData ser) {
		List<CommonPb.ScienceQue> list = ser.getScienceQueList();
		for (CommonPb.ScienceQue e : list) {
			ScienceQue que = new ScienceQue(e.getKeyId(), e.getScienceId(), e.getPeriod(), e.getState(), e.getEndTime());
			scienceQue.add(que);
		}
	}

	private void dserChip(SerData ser) {
		List<CommonPb.Chip> chipList = ser.getChipList();
		for (CommonPb.Chip e : chipList) {
			Chip chip = new Chip(e.getChipId(), e.getCount());
			chips.put(chip.getChipId(), chip);
		}
	}

	private void dserHero(SerData ser) {
		List<CommonPb.Hero> list = ser.getHeroList();
		for (CommonPb.Hero e : list) {
			Hero hero = new Hero(e.getKeyId(), e.getHeroId(), e.getCount());
			heros.put(hero.getHeroId(), hero);
		}
	}

	private void dserFriend(SerData ser) {
		List<CommonPb.DbFriend> list = ser.getFriendList();
		for (CommonPb.DbFriend e : list) {
			Friend friend = new Friend(e.getLordId(), e.getBless(), e.getBlessTime());
			friends.put(e.getLordId(), friend);
		}
	}

	private void dserBless(SerData ser) {
		List<CommonPb.DbBless> list = ser.getBlessList();
		for (CommonPb.DbBless e : list) {
			Bless bless = new Bless();
			bless.setLordId(e.getLordId());
			bless.setState(e.getState());
			bless.setBlessTime(e.getBlessTime());
			blesses.put(e.getLordId(), bless);
		}
	}

	private void dserSign(SerData ser) {
		signs.addAll(ser.getSignList());
	}

	private void dserMajorTask(SerData ser) {
		List<CommonPb.Task> list = ser.getMajorTaskList();
		for (CommonPb.Task e : list) {
			Task task = new Task(e.getTaskId());
			task.setAccept(e.getAccept());
			task.setSchedule(e.getSchedule());
			task.setStatus(e.getStatus());
			majorTasks.put(e.getTaskId(), task);
		}
	}

	private void dserDayiyTask(SerData ser) {
		List<CommonPb.Task> list = ser.getDayiyTaskList();
		for (CommonPb.Task e : list) {
			Task task = new Task(e.getTaskId());
			task.setAccept(e.getAccept());
			task.setSchedule(e.getSchedule());
			task.setStatus(e.getStatus());
			dayiyTask.add(task);
		}
	}

	private void dserLiveTask(SerData ser) {
		List<CommonPb.Task> list = ser.getLiveTaskList();
		for (CommonPb.Task e : list) {
			Task task = new Task(e.getTaskId());
			task.setAccept(e.getAccept());
			task.setSchedule(e.getSchedule());
			task.setStatus(e.getStatus());
			liveTask.put(e.getTaskId(), task);
		}
	}

	private void dserLotteryEquip(SerData ser) {
		List<CommonPb.LotteryEquip> list = ser.getLotteryEquipList();
		for (CommonPb.LotteryEquip e : list) {
			LotteryEquip lottery = new LotteryEquip();
			lottery.setLotteryId(e.getLotteryId());
			lottery.setCd(e.getCd());
			lottery.setFreetimes(e.getFreetimes());
			lottery.setTime(e.getTime());
			lottery.setPurple(e.getPurple());
			lotteryEquips.put(e.getLotteryId(), lottery);
		}
	}

	private void dserCombat(SerData ser) {
		List<CommonPb.Combat> list = ser.getCombatList();
		for (CommonPb.Combat e : list) {
			Combat combat = new Combat(e.getCombatId(), e.getStar());
			combats.put(combat.getCombatId(), combat);
		}
	}

	private void dserExplore(SerData ser) {
		List<CommonPb.Combat> list = ser.getExploreList();
		for (CommonPb.Combat e : list) {
			Combat combat = new Combat(e.getCombatId(), e.getStar());
			explores.put(combat.getCombatId(), combat);
		}
	}

	private void dserSection(SerData ser) {
		List<CommonPb.Section> list = ser.getSectionList();
		for (CommonPb.Section e : list) {
			sections.put(e.getSectionId(), e.getBox());
		}
	}

	private void dserSkill(SerData ser) {
		List<CommonPb.Skill> list = ser.getSkillList();
		for (CommonPb.Skill e : list) {
			skills.put(e.getId(), e.getLv());
		}
	}

	private void dserMill(SerData ser) {
		List<CommonPb.Mill> list = ser.getMillList();
		for (CommonPb.Mill e : list) {
			Mill mill = new Mill(e.getPos(), e.getId(), e.getLv());
			mills.put(mill.getPos(), mill);
		}
	}

	private void dserEffect(SerData ser) {
		List<CommonPb.Effect> list = ser.getEffectList();
		for (CommonPb.Effect e : list) {
			Effect effect = new Effect(e.getId(), e.getEndTime());
			effects.put(effect.getEffectId(), effect);
		}
	}

	private void dserEquip(SerData ser) {
		List<CommonPb.Equip> equipList = ser.getEquipList();
		for (CommonPb.Equip e : equipList) {
			Equip equip = new Equip(e.getKeyId(), e.getEquipId(), e.getLv(), e.getExp(), e.getPos());
			equips.get(equip.getPos()).put(equip.getKeyId(), equip);
		}
	}

	private void dserPart(SerData ser) {
		List<CommonPb.Part> partList = ser.getPartList();
		for (CommonPb.Part e : partList) {
			boolean locked = false;
			if (e.hasField(CommonPb.Mail.getDescriptor().findFieldByName("locked"))) {
				locked = e.getLocked();
			}
			Part part = new Part(e.getKeyId(), e.getPartId(), e.getUpLv(), e.getRefitLv(), e.getPos(), locked);
			parts.get(part.getPos()).put(part.getKeyId(), part);
		}
	}

	private void dserProp(SerData ser) {
		List<CommonPb.Prop> propList = ser.getPropList();
		for (CommonPb.Prop e : propList) {
			Prop prop = new Prop(e.getPropId(), e.getCount());
			props.put(prop.getPropId(), prop);
		}
	}

	private void dserForm(SerData ser) {
		List<CommonPb.Form> formList = ser.getFormList();
		for (CommonPb.Form form : formList) {
			CommonPb.TwoInt p1 = form.getP1();
			CommonPb.TwoInt p2 = form.getP2();
			CommonPb.TwoInt p3 = form.getP3();
			CommonPb.TwoInt p4 = form.getP4();
			CommonPb.TwoInt p5 = form.getP5();
			CommonPb.TwoInt p6 = form.getP6();
			Form e = new Form();
			e.setType(form.getType());
			e.setCommander(form.getCommander());
			e.p[0] = p1.getV1();
			e.c[0] = p1.getV2();

			e.p[1] = p2.getV1();
			e.c[1] = p2.getV2();

			e.p[2] = p3.getV1();
			e.c[2] = p3.getV2();

			e.p[3] = p4.getV1();
			e.c[3] = p4.getV2();

			e.p[4] = p5.getV1();
			e.c[4] = p5.getV2();

			e.p[5] = p6.getV1();
			e.c[5] = p6.getV2();

			forms.put(e.getType(), e);
		}
	}

	private void dserTank(SerData ser) {
		List<CommonPb.Tank> tankList = ser.getTankList();
		for (CommonPb.Tank tank : tankList) {
			tanks.put(tank.getTankId(), new Tank(tank.getTankId(), tank.getCount(), tank.getRest()));
		}
	}

	private void dserScience(SerData ser) {
		List<CommonPb.Science> scienceList = ser.getScienceList();
		for (CommonPb.Science e : scienceList) {
			Science science = new Science(e.getScienceId(), e.getScienceLv());
			sciences.put(science.getScienceId(), science);
		}
	}

	private void dserActivity(SerData ser) {
		List<CommonPb.DbActivity> activityList = ser.getActivityList();
		for (CommonPb.DbActivity e : activityList) {
			Activity activity = new Activity();
			activity.setActivityId(e.getActivityId());
			activity.setBeginTime(e.getBeginTime());
			activity.setEndTime(e.getEndTime());
			activity.setOpen(e.getOpen());
			List<Long> statusList = new ArrayList<Long>();
			if (e.getStatusList() != null) {
				for (Long status : e.getStatusList()) {
					statusList.add(status);
				}
			}
			activity.setStatusList(statusList);
			Map<Integer, Integer> statusMap = new HashMap<Integer, Integer>();
			if (e.getTowIntList() != null) {
				for (CommonPb.TwoInt towInt : e.getTowIntList()) {
					statusMap.put(towInt.getV1(), towInt.getV2());
				}
			}
			activity.setStatusMap(statusMap);
			activitys.put(e.getActivityId(), activity);
		}
	}
}
