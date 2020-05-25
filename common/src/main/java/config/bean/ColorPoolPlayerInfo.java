package config.bean;

import define.EColorPool;

/**
 * Created by Administrator on 2017/12/5.
 */
public class ColorPoolPlayerInfo {

	private long playerId = 0;

	private String name = "";

	private EColorPool eColorPool;

	private long winMoney = 0;

	private long time = 0;

	private int image = 0;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EColorPool geteColorPool() {
		return eColorPool;
	}

	public void seteColorPoll(EColorPool eColorPool) {
		this.eColorPool = eColorPool;
	}

	public long getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(long winMoney) {
		this.winMoney = winMoney;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}
}
