package config.bean;

import config.IConfigBean;
import util.ASObject;

public class ConfSetting implements IConfigBean {

	private int id;

	private String key;

	private int pgNo;

	private ASObject Value;

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getPgNo() {
		return pgNo;
	}

	public void setPgNo(int pgNo) {
		this.pgNo = pgNo;
	}

	public ASObject getValue() {
		return Value;
	}

	public void setValue(ASObject value) {
		Value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id");
		builder.append(":");
		builder.append(id);
		builder.append(",");
		builder.append("key");
		builder.append(":");
		builder.append(key);
		builder.append(",");
		builder.append("pgNo");
		builder.append(":");
		builder.append(pgNo);
		builder.append(",");
		builder.append("Value");
		builder.append(":");
		builder.append(Value);
		return builder.toString();
	}
}