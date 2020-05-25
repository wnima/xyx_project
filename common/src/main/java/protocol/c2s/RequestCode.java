package protocol.c2s;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import define.AppId;

public enum RequestCode {
	HOST(98, AppId.GATE), // 服务器状态
	PING(99, AppId.GATE), // 心跳包

	// 节点
	CENTER_GATE_FACTOR(1200, AppId.CENTER), // 服务器人数
	CENTER_REGISTER_SERVER(1201, AppId.DONE_HERE), // 注册信息到center服务器
	CENTER_SERVER_PING(30000, AppId.GATE), // 更新地址
	GATE_REMOVE_SERVER(30003, AppId.GATE), // 更新地址

//	// 服务器内部交互
	INTERNAL_LOGIN_RQ(201, AppId.CENTER), //
	INTERNAL_LOGIN_RSP(202, AppId.GATE), //
	INTERNAL_QQ_LOGIN_RQ(203, AppId.CENTER), //
	INTERNAL_QQ_LOGIN_RSP(204, AppId.GATE), //
//	INTERNAL_DEVICE_LOGIN(30024, AppId.CENTER), // 向center请求进行登陆
//	INTERNAL_HALL_LOGIN(30025, AppId.CENTER), // 进入大厅
//	INIERAL_BIND_PHONE(30026, AppId.CENTER), // 绑定手机
//	INIERAL_PHONE_SET_PASS(30027, AppId.CENTER), // 账号注册
//	INIERAL_RETRIEVE_PASS(30028, AppId.CENTER), // 账号注册
//	INTERNAL_PLAYER_RELOGIN_LOGIC(30029, AppId.LOGIC), // 玩家重连
//	INTERNAL_LOGIN_SUCCESS(30030, AppId.GATE), // 登陆成功
//	INTERNAL_ENTER_HALL_SUCCESS(30031, AppId.GATE), // 进入大厅成功
//	INTERNAL_BIND_PHONE_SUCCESS(30032, AppId.GATE), // 綁定手機號成功
//	INTERNAL_PHONE_SET_PASS_SUCCESS(30033, AppId.GATE), // 綁定手機號成功
//	INTERNAL_RETRIEVE_PASS_SUCCESS(30034, AppId.GATE), // 设置密码成功
//	INTERNAL_PLAYER_LOGOUT(30035, AppId.CENTER), // 玩家掉线
//	INTERNAL_CLICK_OUT_PLAYER(30037, AppId.GATE), // 踢出在线玩家
//	INTERNAL_SYN_NOTICE(30038, AppId.GATE), // 通知gate广播公告
//	INTERNAL_SYN_PAOMAO(30039, AppId.GATE), // 通知gate广播公告
//	INTERNAL_PHONE_SET_PASS(30040, AppId.CENTER), // 修改手机密码
//	INTERNAL_RETRIEVE_PASS(30041, AppId.CENTER), // 找回密码
//	INTERNAL_ENTER_HALL_BIND(30042, AppId.GATE), // 通知gate进入大厅成功,客户端替换连接设置用户ID
//	INTERNAL_ENTER_HALL_BIND_SUCC(30043, AppId.CENTER), // 通知center客户端连接准备好了
//	INTERNAL_CLICK_ROBOT(30046, AppId.CENTER), // 清除房间中的机器人
//	INTERNAL_CREATE_RED_ENVELOPE(30047, AppId.CENTER), // 创建红包

	//
	RESPONSE_TO_CLIIENT_CODE(-1, AppId.GATE), // 回复客户端
	ENTER_GAME(100, AppId.CENTER), //
	ACCOUNT_RESET_GAME(101), // 断线重连
	GAME_STOP(199), // 游戏暂停
	LOGIN_REMOVE_SERVER(111), // 服务器退出
	LOGIC_REMOVE_SERVER(112), // 逻辑服退出
	LOG_REMOVE_SERVER(113), // 逻辑服退出

	// 客户端请求登陆
	LOGIN(130, AppId.GATE), // 登陆
	QQ_LOGIN(132), // QQ小游戏登陆

	//
	CENTER_PLAYER_LOGOUT(150), // 玩家退出

	// 奔跑冒险王
	MXW_USER_RQ(301, AppId.CENTER), // 请求用户信息
	MXW_CHAPTER_RQ(303, AppId.CENTER), // 关卡章节信息请求
	MXW_SECTION_START_RQ(305, AppId.CENTER), // 开始关卡
	MXW_SECTION_END_RQ(307, AppId.CENTER), // 关卡结束
	MXW_RANK_RQ(309, AppId.CENTER), // 排行榜信息
	MXW_SHOP_RQ(311, AppId.CENTER), // 商店信息
	MXW_SHOP_BUY_RQ(313, AppId.CENTER), // 商店购买
	MXW_HALL_BOX(315, AppId.CENTER),// 领取大厅宝箱
	MXW_SHOP_USE(317, AppId.CENTER),// 使用商品(角色)
	MXW_LUCK(319, AppId.CENTER),// 使用商品(角色)
	MXW_DETAIL(321, AppId.CENTER),// 关卡详细信息
	MXW_GOT_PROP(323, AppId.CENTER),// 获得道具
	MXW_CONSUME_PROP(325, AppId.CENTER),// 消耗道具
	MXW_GET_PROP_LIST(327, AppId.CENTER),// 道具列表
	MXW_UPDATE_PROP(329, AppId.CENTER),// 更新道具
	MXW_USER_ALLOW(331, AppId.CENTER),// 用户授权
	MXW_GET_SIGN(333, AppId.CENTER),// 签到信息
	MXW_SIGN(335, AppId.CENTER),// 签到
	
	
	// 坦克
	SynChatRq(2901),// 聊天通知
	;

	private final AppId sendTo;

	RequestCode(int value, AppId sendTo) {
		this.sendTo = sendTo;
		this.value = value;
	}

	RequestCode(int value) {
		this.value = value;
		this.sendTo = AppId.DONE_HERE;
	}

	public int getValue() {
		return this.value;
	}

	public AppId getSendTo() {
		return sendTo;
	}

	private int value;

	private static Map<Integer, RequestCode> idCaches = new ConcurrentHashMap<>();

	public static RequestCode getByValue(int value) {
		RequestCode cacheCode = idCaches.get(value);
		if (cacheCode != null) {
			return cacheCode;
		}
		for (RequestCode code : values()) {
			if (code.getValue() == value) {
				idCaches.put(value, code);
				return code;
			}
		}
		idCaches.put(value, RequestCode.RESPONSE_TO_CLIIENT_CODE);
		return RequestCode.RESPONSE_TO_CLIIENT_CODE;
	}
}
