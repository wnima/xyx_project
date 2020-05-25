package define;

public interface SysChatId {
	final int RECRUIT_1 = 101;
	final int RECRUIT_2 = 102;
	final int RECRUIT_3 = 103;
	final int RECRUIT_4 = 104;

	// |%s|在竞技场大发神威，取得了|%s|连胜！我也要打
	final int ARENA_1 = 105;

	// 恭喜|%s|统率提升到|%s|级
	final int COMMAND_UP = 106;

	// |%s|在抽装备中获得了|%s| Lv.1
	final int GET_EQUIP = 107;

	// |%s|在竞技场中将|%s|拉下马，大家快去击败他
	final int ARENA_2 = 108;

	// 恭喜|%s|军衔提升到|%s|
	final int RANKS_UP = 109;

	// 恭喜|%s|声望提升到|%s|
	final int FAME_UP = 110;

	// 限时副本开启了，请大家前去获取宝藏吧！我要去
	final int LIMIT_COMBAT = 111;

	// 恭喜|%s|攻打资源点获得了|%s|
	final int ATTACK_MINE = 112;

	// [["恭喜",0],["",0],["组装了",0],["",0],["我也要组装!",1]]
	final int ASSEMBER_TANK = 113;

	// [["恭喜",0],["",0],["将",0],["",0],["升到了",0],["",0]]
	final int PURPLE_EQUIP = 114;

	// [["恭喜",0],["",0],["将",0],["",0],["强化到了",0],["",0]]
	final int PART_UPGRADE = 115;

	// [["恭喜",0],["",0],["在探宝中获得了",0],["",0],["我也要去探宝!",1]]
	final int EXPLORE_PROP = 116;

	// [["恭喜",0],["",0],["招募了将领",0],["",0],["我也要去招募将领!",1]]
	final int LOTTERY_HERO = 117;

	// [["恭喜",0],["",0],["进阶获得了将领",0],["",0],["我也要去进阶将领!",1]]
	final int IMPROVE_HERO = 118;

	// [["恭喜",0],["",0],["把将领",0],["",0],["升级到了",0],["",0],["我也要去升级将领!",1]]
	final int UPGRADE_HERO = 119;

	// [["恭喜",0],["",0],["很幸运获得了",0],["",0],["我也要去玩!",1]]
	final int LUCK_ROUND = 120;
	
	// [["恭喜",0],["",0],["打开了哈洛克的宝藏获得了",0],["",0],["我也要去玩!",1]]
	final int HALOKE_TREASURE = 121;

	// [["恭喜",0],["",0],["将",0],["",0],["改造到了",0],["",0]]
	final int PART_REFIT = 122;

	// [["恭喜",0],["",0],["在探宝中获得了",0],["",0],["我也要去探宝!",1]]
	final int EXPLORE_TANK = 123;

	// [["欢迎",0],["",0],["加入军团！",0]]
	final int JOIN_PARTY = 124;

	// [["",0],["军团发出招募告示：",0],["",0],["去看看",1]]
	final int RECRUIT_5 = 125;

	// [["",0],["退出了军团",0]]
	final int QUIT_PARTY = 126;

	// [["",0],["被任命为军团长",0]]
	final int PARTY_LEADER = 127;

	// [["",0],["被任命为副军团长",0]]
	final int PARTY_VICE_LEADER = 128;

	// [["",0],["被任命为",0],["",0]]
	final int PARTY_JOB = 129;

	// [["",0],["被",0],["",0],["踢出了军团",0]]
	final int PARTY_TICK = 130;

	// [["",0],["消灭了试炼场的",0],["",0],["前往领奖!",1]]
	final int PARTY_COMBAT = 131;

	// [["",0],["被",0],["",0],["淘汰,取得百团混战第",0],["",0],["名",0]]
	final int PARTY_WAR = 132;

	// [["恭喜",0],["",0],["获得了百团混战第一名,本次军团战结束",0]]
	final int PARTY_CHAMPION = 133;

	// [["",0],["的",0],["",0],["在百团混战中,光芒大盛,取得了5连胜!",0]]
	final int PARTY_WAR_WIN = 134;
	
	// [["V3重装坦克被",0],["",0],["击败,他获得了丰厚的奖励",0]]
	final int BOSS_KILL = 135;
	
	// [["V3重装坦克炮口被",0],["",0],["摧毁,他获得了丰厚的奖励",0]]
	final int BOSS_HURT = 136;
	
	// [["恭喜",0],["",0],["打开了银鹰将领宝箱获得了将领鲷哥!",0]]
	final int HERO_BOX = 139;
	
	// 
	final int SYS_HORN = 146;
	
	// [["本军团改名为",0],["",0]]
	final int PARTY_NAME_CHANGE = 152;
	
	// [["",0],["使用了道具",0],["",0],["获得了珍贵物品",0],["",0]]
	final int OPEN_BOX = 153;
	
	// [["恭喜",0],["",0],["从每日充值中开启宝箱，获得",0],["",1],["我要参加!",1]]
	final int EVERY_DAY_CHARGE = 154;
	
	// [["恭喜",0],["",0],["很幸运获得了",0],["",1],["我也要玩!",1]]
	final int JOIN_ACTIVITY = 155;
	
	// [["恭喜,0",["",0],["成为VIP",0],["",0],["获得了大量特权!我要VIP",1]]
	final int BECOME_VIP = 147;
	
	// [["惊天爆料,某人挑战大神威严,胜利而归,点击查看最新战况",1]]
	final int Challenge_GOD_WIN = 156;
	
	// [["号外号外,某人挑战大神威严,铩羽而归,点击查看最新战况",1]]
	final int Challenge_GOD_FAIL = 157;
	
}
