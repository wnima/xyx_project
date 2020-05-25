package data.bean;

import data.DataBean;

public class ChapterBox implements DataBean {

	private long id;
	private long userId;
	private int chapterId;
	private int box1;
	private int box2;
	private int box3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getBox1() {
		return box1;
	}

	public void setBox1(int box1) {
		this.box1 = box1;
	}

	public int getBox2() {
		return box2;
	}

	public void setBox2(int box2) {
		this.box2 = box2;
	}

	public int getBox3() {
		return box3;
	}

	public void setBox3(int box3) {
		this.box3 = box3;
	}

}
