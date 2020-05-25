package define;

public enum CoinEnum {
	CACL_ADD(1, true), // 結算加钱
	CACL_REDUCE(2, false), // 结算扣钱
	CHIP_REDUCE(3, false), // 下注扣钱
	BANK_IN(4, true), // 后台加钱
	BANK_OUT(5, true), // 红包加钱
	CHARGE_ADD(6, true), // 充值加钱
	WITHDRAW_ADD(7, false), // 下分扣款
	BACK_DRAW(8, true), // 拒絕下分，換錢給用戶
	YEB_PROFIT(9,true),//余额宝收益
	BACK_ADD(10,true),//后台加钱
	BACK_REDUCE(11,false), // 后台扣钱
	VIP_MAIL(12,true), // VIP充值邮件
	BACK_MAIL(13,true), // 后台发放邮件
	LOGIC_ADD(14,true),// 游戏逻辑服添加
	ENTER_GAME(15,true),// 进入游戏
	LEAVE_GAME(16,true),// 离开游戏
	ACTIVITY_MAIL(17,true),//活动赠送
	MONTH_CARD(18,true),//月卡
	FRIST_TOPUP_REWARD(19,true),//首充送礼
	AGENT_COIN(20,true),//代理加钱
	;

	private int code;
	private boolean add;

	private CoinEnum(int code, boolean add) {
		this.code = code;
		this.add = add;
	}

	public static boolean isAdd(int code) {
		for (CoinEnum e : values()) {
			if (e.getCode() == code) {
				return e.add;
			}
		}
		return false;
	}

	public static CoinEnum get(int code) {
		for (CoinEnum e : values()) {
			if (e.getCode() == code) {
				return e;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

}
