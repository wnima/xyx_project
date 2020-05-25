package com.game.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pb.CommonPb;
import pb.SerializePb.SerActPartyRank;
import pb.SerializePb.SerActPlayerRank;
import pb.SerializePb.SerTwoValue;
import com.game.util.PbHelper;
import com.google.protobuf.InvalidProtocolBufferException;

import data.bean.ActPartyRank;
import data.bean.ActPlayerRank;
import data.bean.Activity;
import data.bean.UsualActivity;
import define.ActivityConst;

public class UsualActivityData extends Activity {

	private int goal;// 全服活动值记录
	private String params;// 不定参数
	private int lastSaveTime;// 最终更新的时间
	private int sortord = ActivityConst.DESC;// 默认排序方式倒序
	private Object object = new Object();

	// 军团排名榜单
	private LinkedList<ActPartyRank> partyRanks = new LinkedList<ActPartyRank>();
	private Map<Integer, Long> partyRankMap = new HashMap<Integer, Long>();

	// 玩家排行榜{类别:榜单}
	private Map<Integer, LinkedList<ActPlayerRank>> ranks = new HashMap<Integer, LinkedList<ActPlayerRank>>();

	public UsualActivityData(ActivityBase activityBase, int begin) {
		super(activityBase, begin);
	}

	public UsualActivityData(UsualActivity usualActivity) throws InvalidProtocolBufferException {
		this.setActivityId(usualActivity.getActivityId());
		this.setBeginTime(usualActivity.getActivityTime());
		this.setEndTime(usualActivity.getRecordTime());
		this.goal = usualActivity.getGoal();
		this.sortord = usualActivity.getSortord();
		this.params = usualActivity.getParams();
		this.setStatusMap(new HashMap<Integer, Integer>());
		this.setStatusList(new ArrayList<Long>());
		dserPlayerRank(usualActivity.getPlayerRank());
		dserPartyRank(usualActivity.getPartyRank());
		dserAddtion(usualActivity.getAddtion());
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public int getSortord() {
		return sortord;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public int getLastSaveTime() {
		return lastSaveTime;
	}

	public void setLastSaveTime(int lastSaveTime) {
		this.lastSaveTime = lastSaveTime;
	}

	public LinkedList<ActPlayerRank> getPlayerRanks(int type) {
		// 如果没有刷新则刷新数据
		LinkedList<ActPlayerRank> playerRanks = ranks.get(type);
		if (playerRanks == null) {
			synchronized (object) {
				playerRanks = new LinkedList<ActPlayerRank>();
				ranks.put(type, playerRanks);
			}
		}
		return playerRanks;
	}

	public Long getPartyScore(int partyId) {
		return this.partyRankMap.get(partyId);
	}

	public LinkedList<ActPartyRank> getPartyRanks() {
		return this.partyRanks;
	}

	/**
	 * 获取玩家排名
	 * 
	 * @param type默认0
	 * @param lordId
	 * @return
	 */
	public ActPlayerRank getPlayerRank(int type, long lordId) {
		LinkedList<ActPlayerRank> playerRanks = getPlayerRanks(type);
		if (playerRanks.size() == 0) {
			return null;
		}
		int rank = 1;
		Iterator<ActPlayerRank> it = playerRanks.iterator();
		while (it.hasNext()) {
			ActPlayerRank next = it.next();
			if (next.getLordId() == lordId) {
				next.setRank(rank);
				return next;
			}
			rank++;
		}
		return null;
	}

	/**
	 * 取军团排名
	 * 
	 * @param type默认0
	 * @param lordId
	 * @return
	 */
	public ActPartyRank getPartyRank(int partyId) {
		if (this.partyRanks.size() == 0) {
			return null;
		}
		int rank = 1;
		Iterator<ActPartyRank> it = this.partyRanks.iterator();
		while (it.hasNext()) {
			ActPartyRank next = it.next();
			if (next.getPartyId() == partyId) {
				next.setRank(rank);
				return next;
			}
			rank++;
		}
		return null;
	}

	public LinkedList<ActPlayerRank> getPlayerRankList(int type, int page) {
		LinkedList<ActPlayerRank> rs = new LinkedList<ActPlayerRank>();
		LinkedList<ActPlayerRank> playerRanks = getPlayerRanks(type);
		if (playerRanks.size() == 0) {
			return rs;
		}
		int[] pages = { page * 20, (page + 1) * 20 };
		Iterator<ActPlayerRank> it = playerRanks.iterator();
		int count = 0;
		while (it.hasNext()) {
			ActPlayerRank next = it.next();
			if (count >= pages[0]) {
				rs.add(next);
			}
			if (++count >= pages[1]) {
				break;
			}
		}
		return rs;
	}

	public boolean isReset(int begin) {
		boolean flag = super.isReset(begin);
		if (flag) {
			this.goal = 0;
			this.partyRanks.clear();
			this.partyRankMap.clear();
			this.params = "";
			Iterator<LinkedList<ActPlayerRank>> it = this.ranks.values().iterator();
			while (it.hasNext()) {
				LinkedList<ActPlayerRank> next = it.next();
				next.clear();
			}
		}
		return flag;
	}

	public byte[] serPlayerRank() {
		SerActPlayerRank.Builder ser = SerActPlayerRank.newBuilder();
		Iterator<LinkedList<ActPlayerRank>> it = this.ranks.values().iterator();
		while (it.hasNext()) {
			LinkedList<ActPlayerRank> playerRanks = it.next();
			for (ActPlayerRank playRank : playerRanks) {
				ser.addActPlayerRank(PbHelper.createActPlayerRank(playRank));
			}
		}
		return ser.build().toByteArray();
	}

	public void dserPlayerRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerActPlayerRank ser = SerActPlayerRank.parseFrom(data);
		List<CommonPb.ActPlayerRank> list = ser.getActPlayerRankList();
		for (CommonPb.ActPlayerRank e : list) {
			long lordId = e.getLordId();
			int type = e.getRankType();
			long value = e.getRankValue();
			ActPlayerRank playRank = new ActPlayerRank(lordId, type, value);
			playRank.setParam(e.getParam());
			LinkedList<ActPlayerRank> playerRanks = ranks.get(type);
			if (playerRanks == null) {
				playerRanks = new LinkedList<ActPlayerRank>();
				ranks.put(type, playerRanks);
			}
			addPlayerRank(playerRanks, lordId, type, value, 0, sortord);
		}
	}

	public byte[] serPartyRank() {
		SerActPartyRank.Builder ser = SerActPartyRank.newBuilder();
		for (ActPartyRank rank : this.partyRanks) {
			ser.addActPartyRank(PbHelper.createActPartyRank(rank));
		}
		return ser.build().toByteArray();
	}

	public void dserPartyRank(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerActPartyRank ser = SerActPartyRank.parseFrom(data);
		List<CommonPb.ActPartyRank> list = ser.getActPartyRankList();
		for (CommonPb.ActPartyRank e : list) {
			ActPartyRank rank = new ActPartyRank();
			rank.setPartyId(e.getPartyId());
			rank.setParam(e.getParam());
			rank.setRankType(e.getRankType());
			rank.setRankValue(e.getRankValue());
			for (Long lordId : e.getLordIdList()) {
				rank.getLordIds().add(lordId);
			}
			partyRanks.add(rank);
			partyRankMap.put(e.getPartyId(), e.getRankValue());
		}
	}

	public byte[] serAddtion() {
		SerTwoValue.Builder ser = SerTwoValue.newBuilder();
		Iterator<Entry<Integer, Long>> it = partyRankMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Long> next = it.next();
			ser.addTwoValue(PbHelper.createTwoValuePb(next.getKey(), next.getValue()));
		}
		return ser.build().toByteArray();
	}

	public void dserAddtion(byte[] data) throws InvalidProtocolBufferException {
		if (data == null) {
			return;
		}
		SerTwoValue ser = SerTwoValue.parseFrom(data);
		List<CommonPb.TwoValue> list = ser.getTwoValueList();
		for (CommonPb.TwoValue e : list) {
			partyRankMap.put(e.getV1(), e.getV2());
		}
	}

	/**
	 * 默认添加
	 * 
	 * @param lordId
	 * @param value
	 * @param maxRank {前十名：则为10}
	 * @param order
	 */
	public void addPlayerRank(long lordId, long value, int maxRank, int order) {
		LinkedList<ActPlayerRank> playerRanks = getPlayerRanks(ActivityConst.TYPE_DEFAULT);
		addPlayerRank(playerRanks, lordId, ActivityConst.TYPE_DEFAULT, value, maxRank, order);
	}

	/**
	 * 
	 * @param lordId
	 * @param type排行榜类型
	 * @param value玩家值
	 * @param maxRank   {前十名：则为10}
	 * @param order
	 */
	public void addPlayerRank(long lordId, int type, long value, int maxRank, int order) {
		LinkedList<ActPlayerRank> playerRanks = getPlayerRanks(type);
		addPlayerRank(playerRanks, lordId, type, value, maxRank, order);
	}

	/**
	 * 
	 * @param lordId
	 * @param type
	 * @param value
	 * @param maxRank最大排名值 {前十名：则为10}
	 * @param order
	 */
	public void addPlayerRank(LinkedList<ActPlayerRank> rankList, Long lordId, int type, Long value, int maxRank, int order) {
		int size = rankList.size();
		if (size == 0) {
			rankList.add(new ActPlayerRank(lordId, type, value));
			return;
		} else if (maxRank != 0 && size >= maxRank) {// 排名已满,则比较最末名
			ActPlayerRank actRank = rankList.getLast();
			if (order == ActivityConst.ASC) {
				if (actRank.getRankValue() < value) {// 升序比最末名大,则不进入排名
					return;
				}
			} else if (order == ActivityConst.DESC) {
				if (actRank.getRankValue() > value) {// 降序比最末名小,则不进入排名
					return;
				}
			}
		}
		boolean flag = false;
		Iterator<ActPlayerRank> it = rankList.iterator();
		while (it.hasNext()) {
			ActPlayerRank next = it.next();
			if (order == ActivityConst.ASC) {
				if (next.getLordId() == lordId && next.getRankValue() > value) {
					next.setRankValue(value);
					flag = true;
					break;
				}
			} else if (order == ActivityConst.DESC) {
				if (next.getLordId() == lordId && next.getRankValue() < value) {
					next.setRankValue(value);
					flag = true;
					break;
				}
			}
		}

		if (!flag) {// 新晋排名玩家
			rankList.add(new ActPlayerRank(lordId, type, value));
		}

		if (order == ActivityConst.ASC) {// 升序排序
			Collections.sort(rankList, new PlayerRankAsc());
		} else if (order == ActivityConst.DESC) {// 降序
			Collections.sort(rankList, new PlayerRankDesc());
		}

		// 将超出排名的最末名删掉
		if (maxRank != 0 && rankList.size() > maxRank) {
			rankList.removeLast();
		}
	}

	/**
	 * 添加排名记录
	 * 
	 * @param partyId
	 * @param value
	 * @param maxRank
	 * @param order
	 */
	public void addPartyRank(int partyId, long value, int maxRank, int order) {
		long rankValue = 0;
		if (this.partyRankMap.containsKey(partyId)) {
			rankValue = this.partyRankMap.get(partyId).longValue();
		}
		rankValue += value;
		this.partyRankMap.put(partyId, rankValue);
		addPartyRank(this.partyRanks, partyId, rankValue, maxRank, order);
	}

	/**
	 * 
	 * @param rankList
	 * @param partyId
	 * @param type
	 * @param value
	 * @param maxRank
	 * @param order
	 */
	public void addPartyRank(LinkedList<ActPartyRank> rankList, int partyId, long value, int maxRank, int order) {
		int size = rankList.size();
		if (size == 0) {
			rankList.add(new ActPartyRank(partyId, 0, value));
			return;
		} else if (maxRank != 0 && size >= maxRank) {// 排名已满,则比较最末名
			ActPartyRank actRank = rankList.getLast();
			if (order == ActivityConst.ASC) {
				if (actRank.getRankValue() < value) {// 升序比最末名大,则不进入排名
					return;
				}
			} else if (order == ActivityConst.DESC) {
				if (actRank.getRankValue() > value) {// 降序比最末名小,则不进入排名
					return;
				}
			}
		}
		boolean flag = false;
		Iterator<ActPartyRank> it = rankList.iterator();
		while (it.hasNext()) {
			ActPartyRank next = it.next();
			int nextPartyId = next.getPartyId();
			long nextRankValue = next.getRankValue();
			if (order == ActivityConst.ASC) {
				if (nextPartyId == partyId && nextRankValue >= value) {
					next.setRankValue(value);
					flag = true;
					break;
				}
			} else if (order == ActivityConst.DESC) {
				if (nextPartyId == partyId && nextRankValue <= value) {
					next.setRankValue(value);
					flag = true;
					break;
				}
			}
		}

		if (!flag) {
			rankList.add(new ActPartyRank(partyId, 0, value));
		}

		if (order == ActivityConst.ASC) {// 升序排序
			Collections.sort(rankList, new PartyRankAsc());
		} else if (order == ActivityConst.DESC) {// 降序
			Collections.sort(rankList, new PartyRankDesc());
		}

		// 将超出排名的最末名删掉
		if (maxRank != 0 && rankList.size() > maxRank) {
			rankList.removeLast();
		}
	}

	public UsualActivity copyData() {
		UsualActivity entity = new UsualActivity();
		entity.setActivityId(this.getActivityId());// 活动ID
		entity.setActivityTime(this.getBeginTime());// 该活动开启时间
		entity.setRecordTime(this.getEndTime());// 记录时间
		entity.setGoal(this.goal);
		entity.setSortord(this.sortord);
		entity.setParams(this.params);
		entity.setPartyRank(serPartyRank());
		entity.setPlayerRank(serPlayerRank());
		entity.setAddtion(serAddtion());
		return entity;
	}
}

class PlayerRankDesc implements Comparator<ActPlayerRank> {
	@Override
	public int compare(ActPlayerRank o1, ActPlayerRank o2) {
		if (o1.getRankValue() < o2.getRankValue()) {
			return 1;
		} else if (o1.getRankValue() > o2.getRankValue()) {
			return -1;
		} else {
			if (o1.getLordId() > o2.getLordId()) {
				return 1;
			} else if (o1.getLordId() < o2.getLordId()) {
				return -1;
			}
		}
		return 0;
	}
}

class PlayerRankAsc implements Comparator<ActPlayerRank> {
	@Override
	public int compare(ActPlayerRank o1, ActPlayerRank o2) {
		if (o1.getRankValue() > o2.getRankValue()) {
			return 1;
		} else if (o1.getRankValue() < o2.getRankValue()) {
			return -1;
		} else {
			if (o1.getLordId() < o2.getLordId()) {
				return -1;
			}
		}
		return 0;
	}
}

class PartyRankDesc implements Comparator<ActPartyRank> {
	@Override
	public int compare(ActPartyRank o1, ActPartyRank o2) {
		if (o1.getRankValue() < o2.getRankValue()) {
			return 1;
		} else if (o1.getRankValue() > o2.getRankValue()) {
			return -1;
		} else {
			if (o1.getPartyId() > o2.getPartyId()) {
				return 1;
			} else if (o1.getPartyId() < o2.getPartyId()) {
				return -1;
			}
		}
		return 0;
	}
}

class PartyRankAsc implements Comparator<ActPartyRank> {
	@Override
	public int compare(ActPartyRank o1, ActPartyRank o2) {
		if (o1.getRankValue() > o2.getRankValue()) {
			return 1;
		} else if (o1.getRankValue() < o2.getRankValue()) {
			return -1;
		} else {
			if (o1.getPartyId() < o2.getPartyId()) {
				return -1;
			}
		}
		return 0;
	}
}