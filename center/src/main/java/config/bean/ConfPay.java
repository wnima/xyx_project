package config.bean;

import config.IConfigBean;

public class ConfPay implements IConfigBean{
	private int payId;
	private int topup;
	private int extraGold;

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public int getTopup() {
		return topup;
	}

	public void setTopup(int topup) {
		this.topup = topup;
	}

	public int getExtraGold() {
		return extraGold;
	}

	public void setExtraGold(int extraGold) {
		this.extraGold = extraGold;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
