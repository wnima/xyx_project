package define;

public class MailType {

	public static final int NORMAL_MAIL = 1;
	public static final int SEND_MAIL = 2;
	public static final int REPORT_MAIL = 3;
	public static final int SYSTEM_MAIL = 4;
	public static final int ARENA_MAIL = 5;
	public static final int ARENA_GLOBAL_MAIL = 6;

	public static final int STATE_UNREAD = 1;// 未读
	public static final int STATE_READ = 2;// 已读
	public static final int STATE_UNREAD_ITEM = 3;// 含附件未读
	public static final int STATE_READ_ITEM = 4;// 含附件已读
	public static final int STATE_NO_ITEM = 5;// 附件已读已领取

	public static final int MOLD_GUARD = 1;// 驻军:参数{s}
	public static final int MOLD_RETREAT = 2;// 驻军遣返：参数{s}
	public static final int MOLD_HOLD = 3; // 资源占领：参数{s}
	public static final int MOLD_FRIEND_ADD = 4;// 添加好友：参数{s}
	public static final int MOLD_CONCEDE = 5;// 军团长让位：参数{s,s}
	public static final int MOLD_ENTER_PARTY = 6;// 进入军团：参数{s}
	public static final int MOLD_TARGET_GONE = 7;// 原玩家已搬迁：参数{s} 坐标
	// public static final int MOLD_RUINS = 8;// 军团长让位：参数{s,s}
	public static final int MOLD_RUINS = 8;// 废墟提醒:尊敬的指挥官 您的基地被|%s|打成废墟了

	public static final int MOLD_SCOUT_PLAYER = 9;// 侦查玩家：参数{s,s}
	public static final int MOLD_ATTACK_PLAYER = 10;// 攻击玩家：参数{s,s}
	public static final int MOLD_DEFEND = 11;// 遭受攻击：参数{s,s}

	public static final int MOLD_FREE_ATTACK = 16;// 掠夺返回:%s|使用了护罩，保护期内不可侦查或者攻击，你的部队正在返回

	public static final int MOLD_CLEAN_MEMBER = 17;// 清理帮派成员：参数{s}

	public static final int MOLD_SCOUT_MINE = 18;// 侦查矿：参数{s,s}
	public static final int MOLD_ATTACK_MINE = 19;// 攻击矿：参数{s,s}

	public static final int MOLD_AID_GONE = 20;// 驻军遣返:原|%s|的玩家基地已迁往其他地方，您的驻军被直接遣返

	public static final int MOLD_WIPE = 21;// 扫荡奖励：参数{s,s}
	public static final int MOLD_GOLD = 22;// 财政官收入：参数{s,s}
	public static final int MOLD_RED_PACKET = 23;// 土豪的赠礼：参数{s}
	public static final int MOLD_AMY_PROP = 24;// 团长的分配,由于您骁勇善战，军团长|%s|分配您了如下奖励，请继续为军团战斗吧！

	public static final int MOLD_SCOUT_SUCCESS = 25;// 使用成功，你侦查的指挥官|%s|占领中的矿点为：|%s|级|%s|%s
	public static final int MOLD_SCOUT_FAIL = 26;// 使用成功，你查找的指挥官|%s|没有占领中的矿点
	public static final int MOLD_SCOUT = 27;// 使用成功，你侦查的指挥官|%s|在|%s|

	public static final int MOLD_WELCOME_MUZHI = 28;// 欢迎指挥官

	public static final int MOLD_ARENA_1 = 29;// %s挑战%s失败(全服)
	public static final int MOLD_ARENA_2 = 30;// %s挑战%s成功(全服)

	public static final int MOLD_ARENA_3 = 31;// 你挑战%s成功|
	public static final int MOLD_ARENA_4 = 32;// 你挑战%s失败
	public static final int MOLD_ARENA_5 = 33;// %s挑战你成功
	public static final int MOLD_ARENA_6 = 34;// %s挑战你失败

	public static final int MOLD_SYSTEM_1 = 35;// 尊敬的指挥官，您参与了|%s|，现在为您奉上奖励，快快提取吧！
	public static final int MOLD_SYSTEM_2 = 36;// 尊敬的指挥官，很抱歉耽误您宝贵的时间，现在为您奉上补偿，快快提取吧！
	public static final int MOLD_SYSTEM_3 = 37;// 尊敬的指挥官，现在为您补充了物资，快快提取吧！

	public static final int MOLD_FIRST_PAY = 38;// 尊敬的指挥官，这是属于您的首冲豪礼，快快提取吧！
	public static final int MOLD_PAY_DONE = 39; // 尊敬的指挥官，您充值的|%s|金币（实充|%s|金币+赠送|%s|金币）已经到账，请您查收，祝您游戏愉快！

	public static final int MOLD_ACT_1 = 40; // 充值丰收
												// (尊敬的指挥官，您获得了|%s|金币的充值返利和|%s|的水晶奖励，请您查收，祝您游戏愉快！)

	public static final int MOLD_SYSTEM_PUB_1 = 41; // 系统公告

	public static final int MOLD_ACT_2 = 42; // 连续充值(尊敬的指挥官，您获得了|%s|金币的充值返利，请您查收，祝您游戏愉快！)

	public static final int MOLD_SYSTEM_PUB_2 = 43; // 系统公告(带附件)

	public static final int MOLD_PARTY_WAR = 44; // 您个人获得了|%s|连胜的成绩，获得|%s|军团贡献奖励！（军团排名前10奖励，直接发放到军团福利院-战事福利，请关注！）损失坦克：|%s|

	public static final int MOLD_KILL_BOSS = 45; // 您完成了对V3重装坦克的击杀，获得了|%s|奖励

	public static final int MOLD_HURT_BOSS = 46; // 您摧毁了V3重装坦克的炮口，获得了|%s|奖励

	public static final int MOLD_WELCOME_ANFAN = 47;// 欢迎指挥官安峰

	public static final int MOLD_WELCOME_CAOHUA = 48;// 欢迎指挥官草花

	public static final int MOLD_GM_1 = 49;// GM自定义邮件
	public static final int MOLD_GM_2 = 50;// GM自定义系统邮件

	public static final int MOLD_SENIOR_MINE_DEFEND = 51;// 军事矿区遭受攻击
	public static final int MOLD_SENIOR_MINE_SCOUT = 52;// 军事矿区侦察报告
	public static final int MOLD_SENIOR_MINE_ATTACK = 53;// 军事矿区攻击
	
	public static final int MOLD_WELCOME_CAOHUA_YH = 54;//欢迎草花yh
	public static final int MOLD_WELCOME_FANGPIAN = 55;//防骗邮件
	
	public static final int MOLD_PARTY_NAME_CHAGE = 56;// 军团名变更
	
	
}
