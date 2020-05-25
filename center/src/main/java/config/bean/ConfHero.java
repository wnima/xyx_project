package config.bean;

import java.util.List;

import config.IConfigBean;


public class ConfHero implements IConfigBean{
	private int heroId;
	private String heroName;
	private int type;
	private int star;
	private int level;
	private int heroAdditionId;
	private int resolveId;
	private int soul;
	private int canup;
	private List<List<Integer>> meta;
	private List<List<Integer>> attr;
	private int skillId;
	private int skillValue;
	private int tankCount;
	private int order;
	private int compound;
	private int probability;

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHeroAdditionId() {
		return heroAdditionId;
	}

	public void setHeroAdditionId(int heroAdditionId) {
		this.heroAdditionId = heroAdditionId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getSkillValue() {
		return skillValue;
	}

	public void setSkillValue(int skillValue) {
		this.skillValue = skillValue;
	}

	public int getResolveId() {
		return resolveId;
	}

	public void setResolveId(int resolveId) {
		this.resolveId = resolveId;
	}

	public int getSoul() {
		return soul;
	}

	public void setSoul(int soul) {
		this.soul = soul;
	}

	public int getCanup() {
		return canup;
	}

	public void setCanup(int canup) {
		this.canup = canup;
	}

	public List<List<Integer>> getMeta() {
		return meta;
	}

	public void setMeta(List<List<Integer>> meta) {
		this.meta = meta;
	}

	public List<List<Integer>> getAttr() {
		return attr;
	}

	public void setAttr(List<List<Integer>> attr) {
		this.attr = attr;
	}

	public int getTankCount() {
		return tankCount;
	}

	public void setTankCount(int tankCount) {
		this.tankCount = tankCount;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getCompound() {
		return compound;
	}

	public void setCompound(int compound) {
		this.compound = compound;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
