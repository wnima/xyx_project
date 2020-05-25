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
