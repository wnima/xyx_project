package config.bean;

import config.IConfigBean;

public class ConfSkill implements IConfigBean{
	private int skillId;
	private int target;
	private int attr;
	private int attrValue;

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getAttr() {
		return attr;
	}

	public void setAttr(int attr) {
		this.attr = attr;
	}

	public int getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(int attrValue) {
		this.attrValue = attrValue;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
