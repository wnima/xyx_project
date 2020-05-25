package define;

/**
 * @author ChenKui
 * @version 创建时间：2015-10-26 上午10:34:53
 * @declare
 */

public class ActivityConst {

	public static final int CLEAN_DAY = 1;// 每日清理
	public static final int CLEAN_RETAIN = 2;// 保留清理

	public static final int OPEN_CLOSE = -1;// 活动未开启BEGIN之前
	public static final int OPEN_STEP = 0;// BEGIN-END阶段
	public static final int OPEN_AWARD = 1;// END-DISPLAY阶段/BEING-END阶段

	/** 活动领奖状态 **/
	public static final int CAN_NOT_AWARD = 0;// 不可领奖
	public static final int CAN_AWARD = 1;// 可领奖

	public static final int TYPE_DEFAULT = 0;
	public static final int ASC = 0; // 小到大排序
	public static final int DESC = 1; // 大道小排序

	/** 极限单兵前十名 ,勤劳致富前十名 **/
	public static final int RANK_PAWN = 10;
	public static final int RANK_BEE = 30;
	public static final int RANK_PART_DIAL = 10;
	public static final int RANK_TANK_DESTORY = 30;
	public static final int RANK_GENERAL = 30;
	public static final int RANK_CONSUME_DIAL = 30;
	public static final int RANK_FIRE_SHEET = 10;

	/** 活动日志 **/
	public static final int LOG_BUY = 1;
	public static final int LOG_PAY_CONTINUE = 2;
	public static final int LOG_PAY_FOISON = 3;

	public static final int ACT_LEVEL = 1;// 等级礼包
	public static final int ACT_ATTACK = 2;// 雷霆计划
	public static final int ACT_RANK_FIGHT = 3;// 战力排行
	public static final int ACT_RANK_COMBAT = 4;// 关卡排行
	public static final int ACT_RANK_HONOUR = 5;// 荣誉排行
	public static final int ACT_RANK_PARTY_LV = 6;// 军团等级排行
	public static final int ACT_PARTY_DONATE = 7;// 军团募捐
	public static final int ACT_LOT_EQUIP = 8;// 装备探险
	public static final int ACT_LOT_PART = 9;// 配件探险
	public static final int ACT_COLLECT_RESOURCE = 10;// 资源采集
	public static final int ACT_COMBAT = 11;// 激情关卡
	public static final int ACT_RANK_PARTY_FIGHT = 12;// 军团战力排行
	public static final int ACT_DONATE_RE = 13;// 捐献返还
	public static final int ACT_REVELRY = 14;// 全民狂欢
	public static final int ACT_INVEST = 15;// 投资计划
	public static final int ACT_RED_GIFT = 16;// 充值红包
	public static final int ACT_PAY_EVERYDAY = 17;// 每日充值
	public static final int ACT_PAY_FIRST = 18;// 首次充值
	public static final int ACT_QUOTA = 19;// 折扣半价活动
	public static final int ACT_PURPLE_COLL = 20;// 紫装收集
	public static final int ACT_PURPLE_UP = 21;// 紫装升级
	public static final int ACT_CRAZY_ARENA = 22;// 疯狂竞技
	public static final int ACT_CRAZY_HERO = 23;// 疯狂进阶
	public static final int ACT_PART_EVOLVE = 24;// 配件进化
	public static final int ACT_FLASH_SALE = 25;// 限时购买
	public static final int ACT_ENLARGE = 26;// 招兵买将
	public static final int ACT_LOTTEY_EQUIP = 27;// 抽装折扣
	public static final int ACT_COST_GOLD = 28;// 消费有奖
	public static final int ACT_EQUIP_FEED = 29;// 装备补给
	public static final int ACT_CONTU_PAY = 30;// 连续充值
	public static final int ACT_PAY_FOISON = 31;// 充值丰收
	public static final int ACT_DAY_PAY = 32;// 天天充值
	public static final int ACT_DAY_BUY = 33;// 天天限购
	public static final int ACT_FLASH_META = 34;// 限购材料
	public static final int ACT_MONTH_SALE = 35;// 月末限购
	public static final int ACT_GIFT_OL = 36;// 在线时长送礼
	public static final int ACT_MONTH_LOGIN = 37;// 每月登录
	public static final int ACT_ENEMY_SALE = 38;// 敌军兜售
	public static final int ACT_UP_EQUIP_CRIT = 39;// 升装暴击
	public static final int ACT_COMBAT_INTER = 40;// 关卡拦截
	public static final int ACT_RE_FRIST_PAY = 41;// 每天首充返利
	public static final int ACT_GIFT_PAY = 42;// 充值送礼
	public static final int ACT_VIP_GIFT = 43;// VIP礼包
	public static final int ACT_PAY_CONTINUE4 = 44;// 连续充值小
	public static final int ACT_FES_SALE = 45;// 春节限购

	public static final int ACT_PART_SUPPLY = 46;// 配件补给
	public static final int ACT_SCIENCE_MATERIAL = 47;// 科技优惠
	public static final int ACT_FIRE_SHEET = 48;// 火力全开

	/** 活动中心的活动ID **/
	public static final int ACT_MECHA = 101;// 机甲洪流
	public static final int ACT_BEE_ID = 102;// 勤劳致富
	public static final int ACT_AMY_ID = 103;// 建军节
	public static final int ACT_PAWN_ID = 104;// 极限单兵
	public static final int ACT_PROFOTO_ID = 105;// 哈洛克宝藏
	public static final int ACT_PART_DIAL_ID = 106;// 配件转盘
	public static final int ACT_TANK_RAFFLE_ID = 107;// 坦克拉霸
	public static final int ACT_TANK_DESTORY_ID = 108;// 疯狂歼灭
	public static final int ACT_GENERAL_ID = 109;// 招兵买将
	public static final int ACT_TECH_ID = 110;// 技术革新
	public static final int ACT_CONSUME_DIAL_ID = 111;// 消费转盘
	public static final int ACT_VACATIONLAND_ID = 112;// 度假胜地
	public static final int ACT_EQUIP_EXCHANGE_ID = 113;// 装备兑换
	public static final int ACT_PART_EXCHANGE_ID = 114;// 配件兑换
	public static final int ACT_PART_RESOLVE_ID = 115;// 配件分解改造
	public static final int ACT_GAMBLE_ID = 116;// 充值下注
	public static final int ACT_PAY_TURNTABLE_ID = 117;// 充值转盘
	public static final int ACT_SPRING_ID = 118;// 新春狂欢
	public static final int ACT_NEW_RAFFLE_ID = 119;// 新坦克拉霸
	public static final int ACT_M1A2_ID = 120;// M1A2

	/** 特殊活动 **/
	public static final int ACT_EDAY_PAY_ID = 1001;// 每日充值

	public static final int ACT_ONLINE_PORT = 301;// 港口在线奖励
}
