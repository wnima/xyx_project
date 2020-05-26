package config.bean;

import config.IConfigBean;

public class ConfGameType implements IConfigBean{

	private int id;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
