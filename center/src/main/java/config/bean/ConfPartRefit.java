package config.bean;

import config.IConfigBean;

public class ConfPartRefit implements IConfigBean{
	private int quality;
	private int lv;
	private int fitting;
	private int plan;
	private int mineral;
	private int tool;
	private int fittingExplode;
	private int planExplode;
	private int mineralExplode;
	private int toolExplode;

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getFitting() {
		return fitting;
	}

	public void setFitting(int fitting) {
		this.fitting = fitting;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getMineral() {
		return mineral;
	}

	public void setMineral(int mineral) {
		this.mineral = mineral;
	}

	public int getTool() {
		return tool;
	}

	public void setTool(int tool) {
		this.tool = tool;
	}

	public int getFittingExplode() {
		return fittingExplode;
	}

	public void setFittingExplode(int fittingExplode) {
		this.fittingExplode = fittingExplode;
	}

	public int getPlanExplode() {
		return planExplode;
	}

	public void setPlanExplode(int planExplode) {
		this.planExplode = planExplode;
	}

	public int getMineralExplode() {
		return mineralExplode;
	}

	public void setMineralExplode(int mineralExplode) {
		this.mineralExplode = mineralExplode;
	}

	public int getToolExplode() {
		return toolExplode;
	}

	public void setToolExplode(int toolExplode) {
		this.toolExplode = toolExplode;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
