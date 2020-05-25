package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.game.domain.PartyData;
import com.game.domain.Player;
import com.google.inject.Singleton;
import com.google.protobuf.InvalidProtocolBufferException;

import config.bean.ConfPartyProp;
import config.bean.ConfPartyTrend;
import config.provider.ConfPartyLivelyProvider;
import config.provider.ConfPartyPropProvider;
import config.provider.ConfPartyProvider;
import config.provider.ConfPartyTrendProvider;
import data.bean.Grab;
import data.bean.LiveTask;
import data.bean.Lord;
import data.bean.Party;
import data.bean.PartyDonate;
import data.bean.PartyMember;
import data.bean.PartyProp;
import data.bean.PartyRank;
import data.bean.PartyScience;
import data.bean.Prop;
import data.bean.Trend;
import data.bean.Weal;
import data.provider.GlobalProvider;
import data.provider.PartyMemberProvider;
import data.provider.PartyProvider;
import define.PartyType;
import define.WarState;
import domain.Member;
import inject.BeanManager;
import util.DateUtil;

@Singleton
public class PartyDataManager {

	private Map<Integer, PartyData> partyMap = new HashMap<Integer, PartyData>();

	private Map<String, PartyData> partyNameMap = new HashMap<>();

	private List<PartyRank> partyRanks = new ArrayList<PartyRank>();

	private Map<Long, Member> memberMap = new HashMap<Long, Member>();

	private Map<Integer, List<Member>> partyMembers = new HashMap<Integer, List<Member>>();

	// 记录小号的军团成员
	private List<PartyMember> smallIdPartyMembers = new ArrayList<>();
	// 记录有小号的军团
	private Set<Integer> smallIdPartys = new HashSet<Integer>();

	public static PartyDataManager getInst() {
		return BeanManager.getBean(PartyDataManager.class);
	}

	@PostConstruct
	public void init() {
		List<Party> partyList = PartyProvider.getInst().getAllBean();
		List<PartyMember> partyMemberList = PartyMemberProvider.getInst().getAllBean();
		for (PartyMember e : partyMemberList) {
			if (e == null) {
				continue;
			}

			if (SmallIdManager.getInst().isSmallId(e.getLordId())) {
				// 若是小号,记录下来先
				smallIdPartyMembers.add(e);
				smallIdPartys.add(e.getPartyId());
				continue;
			}

			Member member = new Member();
			try {
				member.loadMember(e);
			} catch (InvalidProtocolBufferException e1) {
				e1.printStackTrace();
			}

			memberMap.put(e.getLordId(), member);

			int partyId = e.getPartyId();
			if (partyId != 0) {
				List<Member> list = partyMembers.get(partyId);
				if (list == null) {
					list = new ArrayList<>();
					partyMembers.put(partyId, list);
				}
				list.add(member);
			}
		}

		// 处理小号
		dealSmallIdPartyMember();

		int rank = 1;
		for (Party e : partyList) {
			List<Member> list = partyMembers.get(e.getPartyId());
			// 说明军团都是小号组成,不加入
			if (smallIdPartys.contains(e.getPartyId()) && (list == null || list.size() == 0)) {
				continue;
			}
			PartyData partyData = new PartyData(e);
			partyMap.put(e.getPartyId(), partyData);
			partyNameMap.put(e.getPartyName(), partyData);

			PartyRank partyRank = new PartyRank(e);
			partyRank.setRank(rank++);

			partyRanks.add(partyRank);
		}

	}

	/**
	 * 处理小号 Method: dealSmallIdPartyMember @Description: 判断是否军团长,若不是无所谓<br>
	 * 若是军团长,单人的无所谓。 多人的移交给副军团长 @return void @throws
	 */
	private void dealSmallIdPartyMember() {
		for (PartyMember partyMember : smallIdPartyMembers) {
			// 若是军团长
			if (partyMember.getJob() == PartyType.LEGATUS) {
				// 获取军团人数
				List<Member> list = partyMembers.get(partyMember.getPartyId());
				if (list != null && list.size() > 0) {
					// 移交给军团长自动移交至副军团长中贡献最高的，若没副军团长则移交至贡献最高的成员
					Collections.sort(list, new Comparator<Member>() {
						public int compare(Member o1, Member o2) {
							if (o1.getJob() > o2.getJob()) {
								return 1;
							} else if (o1.getJob() == o2.getJob()) {
								return o1.getDonate() - o2.getDonate();
							} else {
								return -1;
							}
						}
					});

					Member m = list.get(list.size() - 1);
					m.setJob(PartyType.LEGATUS);
				}
			}
		}
	}

	public Map<Long, Member> getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(Map<Long, Member> memberMap) {
		this.memberMap = memberMap;
	}

	public PartyData getParty(int partyId) {
		return partyMap.get(partyId);
	}

	public PartyData getPartyByLordId(long lordId) {
		Member member = memberMap.get(lordId);
		if (member != null) {
			return partyMap.get(member.getPartyId());
		}
		return null;
	}

	public int getPartyId(long lordId) {
		Member member = memberMap.get(lordId);
		if (member != null) {
			return member.getPartyId();
		} else
			return 0;
	}

	public boolean isSameParty(long lordId1, long lordId2) {
		PartyData partyData1 = getPartyByLordId(lordId1);
		PartyData partyData2 = getPartyByLordId(lordId2);
		if (partyData1 != null && partyData2 != null && partyData1.getPartyId() != 0 && partyData1.getPartyId() == partyData2.getPartyId()) {
			return true;
		}
		return false;
	}

	public Member getMemberById(long lordId) {
		return memberMap.get(lordId);
	}

	public Map<Integer, PartyData> getPartyMap() {
		return partyMap;
	}

	public List<Member> getMemberList(int partyId) {
		return partyMembers.get(partyId);
	}

	public int getPartyMemberCount(int partyId) {
		if (partyId == 0) {
			return 0;
		}

		List<Member> members = partyMembers.get(partyId);
		if (members == null) {
			return 0;
		}

		return members.size();
	}

	public int getMemberJobCount(int partyId, int job) {
		List<Member> members = partyMembers.get(partyId);
		Iterator<Member> it = members.iterator();
		int count = 0;
		while (it.hasNext()) {
			Member next = it.next();
			if (next.getJob() == job) {
				count++;
			}
		}
		return count;
	}

	public Map<Integer, PartyScience> getScience(Player player) {
		PartyData partyData = getPartyByLordId(player.roleId);
		if (partyData == null) {
			return null;
		}
		return partyData.getSciences();
	}

	public List<PartyRank> getPartyRank(int page, int type, int level, long fight) {
		List<PartyRank> rs = new ArrayList<PartyRank>();
		int size = partyRanks.size();
		int index = page * 20;
		int end = (page + 1) * 20;
		if (type == 1) {// 请求全部
			for (int i = index; i < end && i < size; i++) {
				PartyRank ee = partyRanks.get(i);
				rs.add(ee);
			}
		} else if (type == 2) {
			int count = 0;
			for (int i = 0; i < size; i++) {
				PartyRank ee = partyRanks.get(i);
				int partyId = ee.getPartyId();
				PartyData partyData = partyMap.get(partyId);
				if (partyData.getApplyLv() <= level && ee.getFight() <= fight) {
					if (count >= index) {
						rs.add(ee);
					}
					count++;
				}
				if (end <= count) {
					break;
				}
			}
		}
		return rs;
	}

	public List<PartyRank> getPartyRanks() {
		return partyRanks;
	}

	public boolean isNameExist(String partyName) {
		return partyNameMap.containsKey(partyName.trim());
	}

	public int createPartyId() {
		return partyMap.size() + 1;
	}

	public void refreshMember(Member member) {
		int today = DateUtil.getToday();
		if (today != member.getRefreshTime()) {
			member.setHallMine(new PartyDonate());
			member.setScienceMine(new PartyDonate());
			member.setWealMine(new Weal());
			member.setRefreshTime(today);
			member.setDayWeal(0);
			member.getCombatIds().clear();
			member.setCombatCount(0);
			member.setActivity(0);
			if (DateUtil.getWeekDay() == 1) {// 每周一清理掉周贡献活跃
				member.setWeekDonate(0);
				member.setWeekAllDonate(0);
			}

			// 更新商品
			if (member.getPartyId() != 0) {
				refreshPartyProp(member);
			}
			member.setRefreshTime(today);
		}
	}

	public void refreshPartyProp(Member member) {
		member.getPartyProps().clear();
		Iterator<ConfPartyProp> it = ConfPartyPropProvider.getInst().getAllConfig().iterator();
		while (it.hasNext()) {
			ConfPartyProp next = it.next();
			if (next.getTreasure() == 1) {
				PartyProp partyProp = new PartyProp(next);
				member.getPartyProps().add(partyProp);
			}
		}
	}

	public PartyData createParty(Lord lord, Member member, String partyName, int apply, int day) {
		int currentDay = DateUtil.getToday();
		Party party = new Party();
		party.setPartyId(createPartyId());
		party.setPartyName(partyName);
		party.setApply(apply);
		party.setPartyLv(1);
		party.setScienceLv(1);
		party.setWealLv(1);
		party.setLegatusName(lord.getNick());
		party.setFight(lord.getFight());
		party.setRefreshTime(day);
		PartyData partyData = new PartyData(party);

		party.setMine(partyData.serMine());
		party.setScience(partyData.serScience());
		party.setTrend(partyData.serTrend());
		party.setAmyProps(partyData.serAmyProps());
		party.setApplyList(partyData.serPartyApply());
		party.setPartyCombat(partyData.serPartyCombat());
		party.setRefreshTime(currentDay);

		partyMap.put(party.getPartyId(), partyData);
		partyNameMap.put(partyName, partyData);

		PartyRank partyRank = new PartyRank(party);
		addPartyRank(partyRank, true);

		enterParty(party.getPartyId(), 1, member);
		member.setJob(PartyType.LEGATUS);

		return partyData;
	}

	public Member createNewMember(Lord lord, int job) {
		Member member = new Member();
		member.setLordId(lord.getLordId());
		member.setJob(job);

		memberMap.put(lord.getLordId(), member);
		return member;
	}

	/**
	 * 参与军团活跃任务
	 * 
	 * @param member
	 * @param taskId
	 * @param count
	 */
	static public void doPartyLivelyTask(PartyData partyData, Member member, int taskId) {
		LiveTask liveTask = partyData.getLiveTasks().get(taskId);
		if (liveTask == null) {
			liveTask = new LiveTask();
			liveTask.setTaskId(taskId);
			partyData.getLiveTasks().put(taskId, liveTask);
		}

		int count = liveTask.getCount() + 1;
		int activity = 0;

		if (taskId == PartyType.TASK_DONATE) {
			if (count > 500) {
				return;
			}
			activity = 1;
		} else if (taskId == PartyType.TASK_COMBAT) {
			if (count > 100) {
				return;
			}
			activity = 2;
		} else if (taskId == PartyType.TASK_BUY_SHOP) {
			if (count > 30) {
				return;
			}
			activity = 5;
		} else if (taskId == PartyType.TASK_ARMY) {
			if (count > 30) {
				return;
			}
			activity = 5;
		} else if (taskId == PartyType.TASK_TEAM) {
			if (count > 50) {
				return;
			}
			activity = 3;
		}

		member.setActivity(member.getActivity() + activity);
		partyData.setLively(partyData.getLively() + activity);
		liveTask.setCount(count);
	}

	/**
	 * 
	 * @param lordId
	 * @return
	 */
	public String getPartyNameByLordId(long lordId) {
		Member member = memberMap.get(lordId);
		if (member == null || member.getPartyId() == 0) {
			return null;
		}
		int partyId = member.getPartyId();
		PartyData partyData = partyMap.get(partyId);
		if (partyData == null) {
			return null;
		}
		return partyData.getPartyName();
	}

	public int enterParty(int partyId, int lv, Member member) {
		List<Member> list = partyMembers.get(partyId);

		if (list == null) {
			list = new ArrayList<>();
			partyMembers.put(partyId, list);
		}

		int count = ConfPartyProvider.getInst().getLvNum(lv);
		if (list.size() >= count) {
			return 2;
		}

		member.enterParty(partyId);
		list.add(member);

		if (member.getPartyProps().isEmpty()) {
			refreshPartyProp(member);
		}

		return 0;
	}

	public void quitParty(int partyId, Member member) {
		member.quitParty();
		List<Member> list = partyMembers.get(partyId);
		if (list != null) {
			Iterator<Member> it = list.iterator();
			while (it.hasNext()) {
				Member e = (Member) it.next();
				if (e.getLordId() == member.getLordId()) {
					it.remove();
				}
			}
		}
	}

	public void addPartyRank(PartyRank partyRank, boolean flag) {
		if (flag) {
			int rank = partyRanks.size();
			partyRank.setRank(rank + 1);
		}
		partyRanks.add(partyRank);
		// rankMap.put(partyRank.getPartyId(), partyRank);
	}

	public boolean subDonate(Member member, int sub) {
		int donate = member.getDonate();
		if (donate < sub) {
			return false;
		}
		member.setDonate(donate - sub);
		return true;
	}

	/**
	 * Depicts:帮派成员采集世界资源,进行累计记录
	 * 
	 * @param lordId
	 * @param iron
	 * @param oil
	 * @param copper
	 * @param silicon
	 * @param stone
	 */
	public void collectMine(long lordId, Grab grab) {
		Member member = memberMap.get(lordId);
		if (member != null && member.getPartyId() != 0) {
			int partyId = member.getPartyId();
			PartyData partyData = partyMap.get(partyId);
			refreshPartyData(partyData);
			Weal reportMine = partyData.getReportMine();
			reportMine.setIron(reportMine.getIron() + grab.rs[0]);
			reportMine.setOil(reportMine.getOil() + grab.rs[1]);
			reportMine.setCopper(reportMine.getCopper() + grab.rs[2]);
			reportMine.setSilicon(reportMine.getSilicon() + grab.rs[3]);
			reportMine.setStone(reportMine.getStone() + grab.rs[4]);
		}
	}

	/**
	 * Function:军团军情、民情添加
	 * 
	 * @param partyId
	 * @param trendId
	 * @param param
	 */
	public boolean addPartyTrend(int partyId, int trendId, String... param) {
		PartyData partyData = partyMap.get(partyId);
		if (partyData != null) {
			ConfPartyTrend staticTrend = ConfPartyTrendProvider.getInst().getConfigById(trendId);
			if (staticTrend == null) {
				return false;
			}
			Trend trend = new Trend(trendId, DateUtil.getSecondTime());
			trend.setTrendId(trendId);
			trend.setParam(param);
			partyData.getTrends().add(trend);

			// 数量超过50
			if (partyData.getTrends().size() > 50) {
				int type = staticTrend.getType();
				Trend temp = null;
				int count = 0;
				Iterator<Trend> it = partyData.getTrends().iterator();
				while (it.hasNext()) {
					Trend next = it.next();
					if (next == null) {
						continue;
					}
					int nextId = next.getTrendId();
					ConfPartyTrend strend = ConfPartyTrendProvider.getInst().getConfigById(nextId);
					if (trend == null) {
						continue;
					}
					if (type == strend.getType()) {
						if (temp == null) {
							temp = next;
						}
						count++;
					}
				}
				if (count > 50 && temp != null) {
					partyData.getTrends().remove(temp);
				}
			}
			return true;
		}
		return false;
	}

	public boolean addPartyTrend(int trendId, Player targe, Player gay, String param) {
		long targeId = targe.roleId;
		PartyData partyData = getPartyByLordId(targeId);
		if (partyData != null) {
			int partyId = partyData.getPartyId();
			long gayId = gay.roleId;
			String lordId1 = String.valueOf(targeId);
			String lordId2 = String.valueOf(gayId);
			String partyName = " ";
			PartyData partyData2 = getPartyByLordId(gayId);
			if (partyData2 != null) {
				partyName = partyData2.getPartyName();
			}
			if (partyName == null) {
				partyName = " ";
			}
			if (param != null) {
				addPartyTrend(partyId, trendId, lordId1, partyName, lordId2, param);
			} else {
				addPartyTrend(partyId, trendId, lordId1, partyName, lordId2);
			}
			return true;
		}
		return false;
	}

	/**
	 * 军团战:战事福利
	 * 
	 * @param partyId
	 * @param props
	 */
	public void addAmyProps(int partyId, List<Prop> props) {
		PartyData partyData = getParty(partyId);
		if (partyData == null || props == null) {
			return;
		}
		Map<Integer, Prop> amyProps = partyData.getAmyProps();
		for (Prop e : props) {
			Prop prop = amyProps.get(e.getPropId());
			if (prop == null) {
				prop = new Prop(e.getPropId(), e.getCount());
				amyProps.put(e.getPropId(), prop);
			} else {
				prop.setCount(prop.getCount() + e.getCount());
			}
		}
	}

	public void refreshPartyData(PartyData partyData) {
		int today = DateUtil.getToday();
		int refreshDate = partyData.getRefreshTime();
		if (refreshDate != today) {

			int pass = DateUtil.until(today, refreshDate);
			for (int i = 0; i < pass; i++) {
				int lively = partyData.getLively();
				lively = ConfPartyLivelyProvider.getInst().costLively(lively);
				partyData.setLively(lively);
			}

			partyData.getPartyCombats().clear();
			partyData.getLiveTasks().clear();
			partyData.setRefreshTime(today);
			if (partyData.getDonates(1) != null) {
				partyData.getDonates(1).clear();
			}
			if (partyData.getDonates(2) != null) {
				partyData.getDonates(2).clear();
			}
			Weal reportMine = partyData.getReportMine();
			if (reportMine != null) {
				reportMine.setCopper(0);
				reportMine.setGold(0);
				reportMine.setIron(0);
				reportMine.setOil(0);
				reportMine.setSilicon(0);
				reportMine.setStone(0);
			}
			List<Integer> shopProps = partyData.getShopProps();
			for (int i = 0; i < shopProps.size(); i++) {
				shopProps.set(i, 0);
			}
		}
	}

	public PartyRank getPartyRankByName(String name) {
		PartyData partyData = partyNameMap.get(name);
		if (partyData != null) {
			return getPartyRank(partyData.getPartyId());
		}

		return null;
	}

	public PartyRank getPartyRank(int partyId) {
		Iterator<PartyRank> it = partyRanks.iterator();
		while (it.hasNext()) {
			PartyRank partyRank = it.next();
			int tempId = partyRank.getPartyId();
			if (partyId == tempId) {
				return partyRank;
			}
		}
		return null;
	}

	public int getRank(int partyId) {
		PartyRank partyRank = getPartyRank(partyId);
		if (partyRank != null) {
			return partyRank.getRank();
		}

		return -1;
	}

	public int getDonateMember(PartyDonate partyDonate, int resourceId) {
		if (resourceId == PartyType.RESOURCE_STONE) {
			return partyDonate.getStone();
		} else if (resourceId == PartyType.RESOURCE_IRON) {
			return partyDonate.getIron();
		} else if (resourceId == PartyType.RESOURCE_SILICON) {
			return partyDonate.getSilicon();
		} else if (resourceId == PartyType.RESOURCE_COPPER) {
			return partyDonate.getCopper();
		} else if (resourceId == PartyType.RESOURCE_OIL) {
			return partyDonate.getOil();
		} else if (resourceId == PartyType.RESOURCE_GOLD) {
			return partyDonate.getGold();
		}
		return 999;
	}

	/**
	 * 判定今天是否是1，3，5，
	 * 
	 * @param member
	 * @return
	 */
	public boolean inWar(Member member) {
		if (DateUtil.getWeekDay() == 1) {
			int state = GlobalProvider.getInst().getBeanById(0).getWarState();
			if ((state >= WarState.REG_STATE && state <= WarState.FIGHT_END) && member.getRegLv() != 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 军团改名 Method: rename @Description: TODO @param partyData @param
	 * partyName @return void @throws
	 */
	public void rename(PartyData partyData, String partyName) {
		// 移除老的名字
		partyNameMap.remove(partyData.getPartyName());
		partyData.setPartyName(partyName);
		partyNameMap.put(partyName, partyData);
	}

	/**
	 * 军团名是否已存在 Method: isExistPartyName @Description: @param
	 * partyName @return @return boolean @throws
	 */
	public boolean isExistPartyName(String partyName) {
		return (partyName != null && partyNameMap.containsKey(partyName.trim()));
	}
}
