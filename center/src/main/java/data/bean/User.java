package data.bean;

import data.DataBean;

public class User implements DataBean {

	private long userId;
	private String userName;
	private String account;
	private String pwd;
	private int platNo;
	private String platId;
	private String platProtrait;
	private String platName;
	private int protrait;
	private int headFrame;
	private long gold;
	private long coin;
	private int power;
	private int powerTime;
	private int ban;
	private String banMsg;
	private int banTime;
	private int white;
	private String ip;
	private long loginTime;
	private long createTime;
	private String mxwShop;
	
	private int gameType;
	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getPlatNo() {
		return platNo;
	}

	public void setPlatNo(int platNo) {
		this.platNo = platNo;
	}

	public String getPlatId() {
		return platId;
	}

	public void setPlatId(String platId) {
		this.platId = platId;
	}

	public String getPlatProtrait() {
		return platProtrait;
	}

	public void setPlatProtrait(String platProtrait) {
		this.platProtrait = platProtrait;
	}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public int getProtrait() {
		return protrait;
	}

	public void setProtrait(int protrait) {
		this.protrait = protrait;
	}

	public int getHeadFrame() {
		return headFrame;
	}

	public void setHeadFrame(int headFrame) {
		this.headFrame = headFrame;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPowerTime() {
		return powerTime;
	}

	public void setPowerTime(int powerTime) {
		this.powerTime = powerTime;
	}

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getBanMsg() {
		return banMsg;
	}

	public void setBanMsg(String banMsg) {
		this.banMsg = banMsg;
	}

	public int getBanTime() {
		return banTime;
	}

	public void setBanTime(int banTime) {
		this.banTime = banTime;
	}

	public int getWhite() {
		return white;
	}

	public void setWhite(int white) {
		this.white = white;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMxwShop() {
		return mxwShop;
	}

	public void setMxwShop(String mxwShop) {
		this.mxwShop = mxwShop;
	}

	@Override
	public long getId() {
		return userId;
	}

	@Override
	public void setId(long id) {
		this.userId = id;
	}

}
