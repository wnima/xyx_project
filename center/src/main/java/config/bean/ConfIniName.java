package config.bean;

import config.IConfigBean;

public class ConfIniName implements IConfigBean{

	private String familyname;
	private String manname;
	private String womanname;
	private String mark;

	public String getFamilyname() {
		return familyname;
	}

	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}

	public String getManname() {
		return manname;
	}

	public void setManname(String manname) {
		this.manname = manname;
	}

	public String getWomanname() {
		return womanname;
	}

	public void setWomanname(String womanname) {
		this.womanname = womanname;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
