package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfCombat implements IConfigBean{
	private int combatId;
	private int sectionId;
	private int exp;
	private List<List<Integer>> drop;
	private List<List<Integer>> firstAward;
	private List<List<Integer>> form;
	private List<Integer> hero;
	private List<List<Integer>> attr;
	private int preId;

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public List<List<Integer>> getDrop() {
		return drop;
	}

	public void setDrop(List<List<Integer>> drop) {
		this.drop = drop;
	}

	public List<List<Integer>> getFirstAward() {
		return firstAward;
	}

	public void setFirstAward(List<List<Integer>> firstAward) {
		this.firstAward = firstAward;
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

	public int getPreId() {
		return preId;
	}

	public void setPreId(int preId) {
		this.preId = preId;
	}

	public List<Integer> getHero() {
		return hero;
	}

	public void setHero(List<Integer> hero) {
		this.hero = hero;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
