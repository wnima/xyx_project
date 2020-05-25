package config.bean;

import config.IConfigBean;

public class ConfLordCommand implements IConfigBean {
	private int commandLv;
	private int book;
	private int prob;
	private int tankCount;
	private int a;
	private float b;

	public int getCommandLv() {
		return commandLv;
	}

	public void setCommandLv(int commandLv) {
		this.commandLv = commandLv;
	}

	public int getBook() {
		return book;
	}

	public void setBook(int book) {
		this.book = book;
	}

	public int getProb() {
		return prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}

	public int getTankCount() {
		return tankCount;
	}

	public void setTankCount(int tankCount) {
		this.tankCount = tankCount;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	@Override
	public int getId() {
		return this.commandLv;
	}

}
