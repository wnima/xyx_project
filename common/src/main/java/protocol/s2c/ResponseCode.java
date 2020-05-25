package protocol.s2c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ResponseCode {

	PONG(-1), //
	LOGIN_RSP(131), // 登陆成功
	QQ_LOGIN_RSP(133), // 登陆成功

	/*********** 奔跑冒险王 ***********/
	MXW_USER(302), // 用户请求
	MXW_CHAPTER(304), // 关卡信息
	MXW_SECTION_START(306), // 小关卡开始
	MXW_SECTION_END(308), // 小关卡结束
	MXW_RANK(310), // 排行榜
	MXW_SHOP(312), // 商店信息
	MXW_SHOP_BUY(314), // 购买请求
	MXW_HALL_BOX(316), // 章节宝箱
	MXW_SHOP_USE(318), // 使用道具返回
	MXW_LUCK(320), // 幸运转盘
	MXW_SECTION_DETAIL(322), // 关卡详细信息
	MXW_GOT_PROP(324), // 关卡详细信息
	MXW_CONSUME_PROP(326), // 消耗道具
	MXW_PROP_LIST(328), // 消耗道具
	MXW_USER_ALLOW(332), // 消耗道具
	MXW_GET_SIGN(334), // 签到信息
	MXW_SIGN(336), // 签到

	/*********** tank ***********/
	/**** tank-武将 ***/
	HERO_RS(2002), // 我的武将
	HERO_DECOMPOSE_RS(2004), // 武将分解
	HERO_LEVE_LUP_RS(2006), // 武将升级
	HERO_IMPROVE_RS(2008), // 武将升阶
	MULTI_HERO_IMPROVE_RS(2010), // 武将升阶
	LOTTERY_HERO_RS(2012), // 武将升阶

	/**** tank-聊天 ***/
	GET_CHAT_RS(2014), // 聊天信息
	PARTY_RECRUIT_RS(2016), // 聊天信息
	SHARE_REPORT_RS(2018), // 聊天信息
	GetReportRs(2020), // 聊天信息
	SynChatRq(2022), // 聊天信息
	DoChatRs(2024), // 聊天信息
	SearchOlRs(2028), // 聊天信息

	/**** tank-配件 ***/
	GetPartRs(2030), // 配件信息
	GetChipRs(2032), // 配件信息
	CombinePartRs(2034), // 配件信息
	OnPartRs(2036), // 配件信息
	ExplodeChipRs(2038), // 配件信息
	UpPartRs(2040), // 配件信息
	RefitPartRs(2042), // 配件信息
	LockPartRs(2044), // 配件信息
	ExplodePartRs(2046), // 配件信息

	/**** tank-任务 ***/
	GetMajorTaskRs(2050), // 任务
	GetDayiyTaskRs(2052), // 任务
	TaskDaylyResetRs(2054), // 任务
	RefreshDayiyTaskRs(20560), // 任务
	GetLiveTaskRs(2058), // 任务
	TaskAwardRs(2060), // 任务
	TaskLiveAwardRs(2062), // 任务
	AcceptTaskRs(2064), // 任务
	AcceptNoTaskRs(2066), // 任务

	/**** tank-竞技场 ***/
	GetArenaRs(2070), // 竞技场
	InitArenaRs(2072), // 竞技场
	DoArenaRs(2074), // 竞技场
	BuyArenaRs(2076), // 竞技场
	UseScoreRs(2078), // 竞技场
	ArenaAwardRs(2080), // 竞技场
	GetRankRs(2082), // 竞技场
	BuyArenaCdRs(2084), // 竞技场

	/**** tank-建筑 ***/
	GetBuildingRs(2090), // 建筑
	DestroyMillRs(2092), // 建筑
	SpeedQueRs(2094), // 建筑
	CancelQueRs(2096), // 建筑
	UpBuildingRs(2098), // 建筑
	BuyAutoBuildRs(2100), // 建筑
	SetAutoBuildRs(2102), // 建筑

	/**** tank-装备 ***/
	GetEquipRs(2110), // 装备
	SellEquipRs(2112), // 装备
	UpEquipRs(2114), // 装备
	AllEquipRs(2116), // 装备
	OnEquipRs(2118), // 装备
	UpCapacityRs(2120), // 装备

	/**** tank-副本 ***/
	GetCombatRs(2130), // 副本
	DoCombatRs(2132), // 副本
	BuyExploreRs(2134), // 副本
	ResetExtrEprRs(2136), // 副本
	CombatBoxRs(2138), // 副本
	BeginWipeRs(2140), // 副本
	EndWipeRs(2142), // 副本
	GetExtremeRs(2144), // 副本
	ExtremeRecordRs(2146), // 副本

	/**** tank-战斗 ***/
	WarRegRs(2150), // 战斗
	WarCancelRs(2152), // 战斗
	WarMembersRs(2154), // 战斗
	WarPartiesRs(2156), // 战斗
	WarWinAwardRs(2158), // 战斗
	WarReportRs(2160), // 战斗
	WarRankRs(2162), // 战斗
	GetWarFightRs(2164), // 战斗
	WarWinRankRs(2166), // 战斗

	/**** tank-签到 ***/
	GetSignRs(2170), // 签到
	SignRs(2172), // 签到
	EveLoginRs(2174), // 签到
	AcceptEveLoginRs(2176), // 签到

	/**** tank-军队 ***/
	GetTankRs(2180), // 军队
	GetArmyRs(2182), // 军队
	GetFormRs(2184), // 军队
	SetFormRs(2186), // 军队
	RepairRs(2188), // 军队
	BuildTankRs(2190), // 军队
	RefitTankRs(2192), // 军队

	/**** tank-編制 ***/
	GetStaffingRs(2196), // 编制信息

	/**** tank-背包 ***/
	GetPropRs(2200), // 获取背包
	BuyPropRs(2202), // 获取背包
	ComposeSantRs(2204), // 获取背包
	UsePropRs(2206), // 获取背包
	BuildPropRs(2208), // 获取背包

	/**** tank-帮派 ***/
	GetPartyRankRs(2210), // 帮派
	GetPartyLvRankRs(2212), // 帮派
	GetPartyRs(2214), // 帮派
	GetPartyMemberRs(2216), // 帮派
	GetPartyHallRs(2218), // 帮派
	GetPartyScienceRs(2220), // 帮派
	GetPartyWealRs(2222), // 帮派
	GetPartyShopRs(2224), // 帮派
	DonatePartyRs(2226), // 帮派
	UpPartyBuildingRs(2228), // 帮派
	SetPartyJobRs(2230), // 帮派
	BuyPartyShopRs(2232), // 帮派
	WealDayPartyRs(2234), // 帮派
	PartyApplyListRs(2236), // 帮派
	PartyApplyRs(2238), // 帮派
	PartyApplyJudgeRs(2240), // 帮派
	CreatePartyRs(2242), // 帮派
	QuitPartyRs(2244), // 帮派
	DonateScienceRs(2246), // 帮派
	SeachPartyRs(2248), // 帮派
	ApplyListRs(2250), // 帮派
	GetPartyTrendRs(2252), // 帮派
	SloganPartyRs(2254), // 帮派
	UpMemberJobRs(2256), // 帮派
	CleanMemberRs(2258), // 帮派
	SetMemberJobRs(2260), // 帮派
	PartyJobCountRs(2262), // 帮派
	PartyApplyEditRs(2264), // 帮派
	GetPartyCombatRs(2266), // 帮派
	PtcFormRs(2268), // 帮派
	DoPartyCombatRs(2270), // 帮派
	PartyctAwardRs(2272), // 帮派
	GetPartyLiveRankRs(2274), // 帮派
	GetPartyAmyPropsRs(2276), // 帮派
	SendPartyAmyPropRs(2278), // 帮派
	UseAmyPropRs(2280),// 帮派
	;

	ResponseCode(int value) {
		this.value = value;
	}

	private static Map<Integer, ResponseCode> idCaches = new ConcurrentHashMap<>();

	public static ResponseCode getByValue(int value) {
		ResponseCode cacheCode = idCaches.get(value);
		if (cacheCode != null) {
			return cacheCode;
		}
		for (ResponseCode code : values()) {
			if (code.getValue() == value) {
				idCaches.put(value, code);
				return code;
			}
		}
		return null;
	}

	private int value;

	public int getValue() {
		return this.value;
	}
}
