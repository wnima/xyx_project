package define;

public enum AwardFrom {
	CAIZHENG(2, "CAI ZHENG GUAN") {// 财政官收入

	},
	RED_PACKET(3, "RED PACKET") {// 红包奖励

	},
	ASSEMBLE_MECHA(4, "JI JIA HONG LIU") {// 机甲洪流奖励

	},
	HALF_COST(5, "HALF COST") {// 半价购买

	},
	ACTIVITY_AWARD(6, "ACTIVITY AWARD") {// 活动奖励

	},
	ARENA_SCORE(7, "ARENA SCORE") {// 竞技场积分兑换

	},
	ARENA_RANK(8, "ARENA RANK") {// 竞技场排名奖励

	},
	HUANGBAO_BOX(9, "HUANGBAO BOX") {// 兑换荒宝宝箱

	},
	PART_BOX(10, "PART BOX") {// 配件副本开箱子

	},
	EQUIP_BOX(11, "EQUIP BOX") {// 装备副本开箱子

	},
	COMBAT_BOX(12, "COMBAT BOX") {// 普通副本开箱子

	},
	COMBAT_FIRST(13, "COMBAT FIRST") {// 副本首次过关奖励

	},
	COMBAT_DROP(14, "COMBAT EXPLORE EXTREME DROP") {// 副本掉落

	},
	HERO_DECOMPOSE(15, "HERO DECOMPOSE") {// 将领分解

	},
	LOTTERY_HERO(16, "LOTTERY_HERO") {// 武将招募

	},
	MAIL_ATTACH(17, "MAIL ATTACH") {// 领取邮件附件

	},
	PARTY_SHOP(18, "PARTY SHOP") {// 军团商店购买

	},
	PARTY_COMBAT(19, "PARTY COMBAT") {// 打军团副本

	},
	PARTY_COMBAT_BOX(20, "PARTY COMBAT BOX") {// 军团副本开箱子

	},
	PARTY_WEAL_DAY(21, "PARTY WEAL DAY") {// 军团每日福利

	},
	GIFT_CODE(22, "GIFT CODE") {// 兑换码

	},
	NEWER_GIFT(23, "NEWER GIFT") {// 新手引导礼包

	},
	OL_AWARD(24, "PORT ONLINE AWARD") {// 港口在线奖励

	},
	USE_PROP(25, "USE PROP") {// 使用道具

	},
	SIGN_AWARD(26, "SIGN AWARD") {// 签到奖励

	},
	FIGHT_MINE(27, "FIGHT MINE DROP") {// 世界地图攻打资源点，随机掉落

	},
	WASTE_SMALL(28, "WASTE_SMALL") {// 荒宝小

	},
	WASTE(29, "WASTE") {// 荒宝小

	},
	WASTE_LARGE(30, "WASTE_LARGE") {// 荒宝大

	},
	EXPLORE_SINGLE(31, "EXPLORE_SINGLE") {// 单次箱子

	},
	EXPLORE_THREE(32, "EXPLORE_THREE") {// 三次箱子

	},
	GREEN_SINGLE(33, "GREEN_SINGLE") {// 绿色单抽

	},
	BLUE_SINGLE(34, "BLUE_SINGLE") {// 蓝色单抽

	},
	PURPLE_SINGLE(35, "BLUE_SINGLE") {// 紫色单抽

	},
	PURPLE_NINE(36, "PURPLE_NINE") {// 紫色多抽

	},
	TASK_DAYIY_AWARD(37, "TASK_AWARD") {// 日常活动

	},
	TASK_LIVILY_AWARD(38, "TASK_AWARD") {// 活跃活动

	},
	PAY(39, "PAY") {// 充值

	},
	PART_EVOLVE(40, "PART_EVOLVE") {// 配件进阶

	},
	FLASH_SALE(41, "FLASH_SALE") {// 限时购买

	},
	FLASH_META(42, "FLASH_META") {// 限购材料

	},
	DAY_BUY(43, "DAY_BUY") {// 天天购买

	},
	AMY_REBATE(44, "AMY REBATE") {// 建军节返利

	},
	PAWN(45, "PAWN") {// 极限单兵

	},
	ACT_RANK_AWARD(46, "ACT_RANK_AWARD") {// 极限单兵

	},
	SIGN_LOGIN(47, "SIGN LOGIN") {// 签到登录奖励

	},
	PART_DIAL(48, "PART_DIAL") {// 配件转盘

	},
	COMBAT_COURSE(49, "COMBAT_COURSE") {// 关卡拦截

	},
	PARTY_AMY_PROP(50, "PARTY_AMY_PROP") {// 军团战事福利

	},
	USE_AMY_PROP(51, "USE_AMY_PROP") {// 使用军团福利坦克箱子

	},
	WAR_WIN(52, "WAR WIN RANK") {// 百团混战连胜排名奖励

	},
	WAR_PARTY(53, "WAR IN PARTY") {// 百团混战贡献度奖励

	},
	ACTIVITY_TECH(54, "ACTIVITY TECH") {// 技术革新

	},
	ATTACK_BOSS(55, "ATTACK BOS") {// 挑战boss

	},
	BOSS_HURT(56, "BOSS HURT RANK") {// 世界boss伤害排名奖励

	},
	ACTIVITY_GENERAL(57, "ACTIVITY GENERAL") {// 招募武将

	},
	PARTY_TIP_AWARD(58, "PARTY TIP AWARD") {// 军团tip奖励

	},
	EVERY_DAY_PAY(59, "EVERY_DAY_PAY") {// 每日充值

	},
	VIP_GIFT(60, "VIP_GIFT") {// vip礼包

	},
	CONSUME_DIAL(61, "CONSUME_DIAL") {// 消费转盘

	},
	VACATION(62, "VACATION") {// 度假胜地

	},
	EXCHANGE_PART(63, "EXCHANGE_PART") {// 兑换配件

	},
	EXCHANGE_EQUIP(64, "EXCHANGE_EQUIP") {// 兑换装备

	},
	PART_RESOLVE(65, "PART_RESOLVE") {// 兑换装备

	},
	TOPUP_GAMBLE(66, "TOPUP_GAMBLE") {// 充值下注

	},
	SCORE_AWARD(67, "SCORE AWARD") {// 军事矿区个人积分排名奖励

	},
	PARTY_SCORE_AWARD(68, "PARTY SCORE AWARD") {// 军事矿区军团积分排名奖励

	},
	PAY_TURN_TABLE(69, "PAY_TURN_TABLE") {// 充值转盘

	},
	PRAY_AWARD(70, "PRAY_AWARD") {// 祈福奖励

	},
	ACTIVITY_MINE(71, "ACTIVITY_MINE") {// 活动矿掉落

	},
	ACT_M1A2(72, "ACT_M1A2") {// M1A2掉落

	},
	FRIEND_BLESS(73,"FRIEND_BLESS"){// 好友祝福
	},
	DO_SOME(99, "DO SOME") {// gm 命令

	};
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private AwardFrom(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;
	private String msg;
}
