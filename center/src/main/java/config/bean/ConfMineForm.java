package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfMineForm implements IConfigBean {
	private int keyId;
	private int lv;
	private List<List<Integer>> form;
	private List<List<Integer>> attr;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public List<List<Integer>> getForm() {
		return form;
	}

	public void setForm(List<List<Integer>> form) {
		this.form = form;
	}

	public List<List<Integer>> getAttr() {
		return attr;
	}

	public void setAttr(List<List<Integer>> attr) {
		this.attr = attr;
	}

	@Override
	public int getId() {
		return 0;
	}

}
