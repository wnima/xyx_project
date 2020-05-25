package data.bean;

import data.DataBean;

public class Building implements DataBean {
	private long lordId;
	private int ware1;
	private int ware2;
	private int tech;
	private int factory1;
	private int factory2;
	private int refit;
	private int command;
	private int workShop;

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public int getWare1() {
		return ware1;
	}

	public void setWare1(int ware1) {
		this.ware1 = ware1;
	}

	public int getWare2() {
		return ware2;
	}

	public void setWare2(int ware2) {
		this.ware2 = ware2;
	}

	public int getTech() {
		return tech;
	}

	public void setTech(int tech) {
		this.tech = tech;
	}

	public int getFactory1() {
		return factory1;
	}

	public void setFactory1(int factory1) {
		this.factory1 = factory1;
	}

	public int getFactory2() {
		return factory2;
	}

	public void setFactory2(int factory2) {
		this.factory2 = factory2;
	}

	public int getRefit() {
		return refit;
	}

	public void setRefit(int refit) {
		this.refit = refit;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getWorkShop() {
		return workShop;
	}

	public void setWorkShop(int workShop) {
		this.workShop = workShop;
	}

	@Override
	public long getId() {
		return lordId;
	}

	@Override
	public void setId(long id) {
		this.lordId = id;
	}
}
