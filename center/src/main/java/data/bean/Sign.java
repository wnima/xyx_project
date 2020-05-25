package data.bean;

import data.DataBean;

public class Sign implements DataBean {

	private long userId;
	private int sign1;
	private int sign2;
	private int sign3;
	private int sign4;
	private int sign5;
	private int sign6;
	private int sign7;

	public Sign() {
	}

	public Sign(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getSign1() {
		return sign1;
	}

	public void setSign1(int sign1) {
		this.sign1 = sign1;
	}

	public int getSign2() {
		return sign2;
	}

	public void setSign2(int sign2) {
		this.sign2 = sign2;
	}

	public int getSign3() {
		return sign3;
	}

	public void setSign3(int sign3) {
		this.sign3 = sign3;
	}

	public int getSign4() {
		return sign4;
	}

	public void setSign4(int sign4) {
		this.sign4 = sign4;
	}

	public int getSign5() {
		return sign5;
	}

	public void setSign5(int sign5) {
		this.sign5 = sign5;
	}

	public int getSign6() {
		return sign6;
	}

	public void setSign6(int sign6) {
		this.sign6 = sign6;
	}

	public int getSign7() {
		return sign7;
	}

	public void setSign7(int sign7) {
		this.sign7 = sign7;
	}

	@Override
	public long getId() {
		return userId;
	}

	@Override
	public void setId(long id) {
		this.userId = id;
	}

}
