package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfMine implements IConfigBean{
	private int pos;
	private int type;
	private int lv;
	private List<List<Integer>> dropOne;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public List<List<Integer>> getDropOne() {
		return dropOne;
	}

	public void setDropOne(List<List<Integer>> dropOne) {
		this.dropOne = dropOne;
	}

	@Override
	public int getId() {
		return 0;
	}

}
