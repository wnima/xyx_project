package config.bean;

import config.IConfigBean;


public class ConfCost implements IConfigBean{

	private int costId;
	private int count;
	private int type;
	private int price;

	public int getCostId() {
		return costId;
	}

	public void setCostId(int costId) {
		this.costId = costId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
