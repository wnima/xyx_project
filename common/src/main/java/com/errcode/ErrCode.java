package com.errcode;

/**
 * Created by Administrator on 2017/11/29.
 */
public enum ErrCode {
	ERR_SUCCESS(0, "成功"),
	ERR_PLAYER_NONEXISTENT(1, "该玩家不存在"),
	ERR_ROOM_NONEXISTENT(2, "房间不存在"),
	ERR_ROOMTYPE_NONEXISTENT(3, "房间类型不存在"),
	ERR_DESKINFO(4, "桌子信息错误"),
	ERR_DESKINFO_PLAYERS(5, "桌子人数达到上限"),
	ERR_REPEAT_OPT(6, "重复操作"),
	ERR_NOT_OPT(7, "不能进行该操作"),
	ERR_DESK_PLAYER_ALL(8, "桌子人数已满"),
	SMALL_CHIP_ERROR(9, "铃铛下注错误"),
	NOT_MONEY(10, "金额不足"),
	NOT_QUIT(11, "不能退出房间"),
	SMALL_SEL_ERROR(12, "铃铛选择错误"),
	CHIP_ERROR(13, "下注错误"),
	CONF_CARDS_NUM(14, "牌数不正确"),
	CONF_CARDS(15, "配置的牌错误"),
	CONF_CARDS_NO_ONLY(16, "牌已经存在"),
	CONF_PARAM_NUM(17, "配置参数不正确"),
	ERR_RENEWAD(18, "续压失败"),
	ENTER_GAME_ERROR(19, "进入游戏失败"),
	NO_CHANGE_DESK(20, "不能换桌"),
	NO_OPT(21, "不能操作"),
	NO_MYSELF(22, "不能跟自己比牌"),
	GAME_STOP(23, "当前游戏维护中"),
	PLAYER_CHIP(24, "玩家下注限制"),
	KICK_PLAYER(25, "与服务器断开连接"),
	PLAYER_AGAIN_LOGIC(26, "玩家被挤下线"),
	LOOKED(27, "玩家已看牌"),
	MJ_CLOSE(28, "麻将房间解散"),
	BEEN_XUTOU(29, "请不要重复续投"),
	XUTOU_CHIP(30, "续投重码不正确"),
	NEED_TOPUP(31, "余额不足,请前往充值"),
	IN_FORZEN(32,"冰冻效果中"),
	IN_CD(33,"CD正在冷却中"),
	RESULT_CONTROL(34,"只有下注阶段可调")
	;

	private int key;
	private String value;

	ErrCode(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
}
