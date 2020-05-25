package data.bean;

import java.util.Date;

import data.DataBean;

public class Account implements DataBean {
	private int keyId;
	private int accountKey;
	private int serverId;
	private int platNo;
	private String platId;
	private int childNo;
	private int forbid;
	private int whiteName;
	private long lordId;
	private int created;
	private String deviceNo;
	private Date createDate;
	private Date loginDate;
	private int loginDays;
	private int isGm;
	private int isGuider;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
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

	public int getForbid() {
		return forbid;
	}

	public void setForbid(int forbid) {
		this.forbid = forbid;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getWhiteName() {
		return whiteName;
	}

	public void setWhiteName(int whiteName) {
		this.whiteName = whiteName;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public int getLoginDays() {
		return loginDays;
	}

	public void setLoginDays(int loginDays) {
		this.loginDays = loginDays;
	}

	public int getChildNo() {
		return childNo;
	}

	public void setChildNo(int childNo) {
		this.childNo = childNo;
	}

	public int getIsGm() {
		return isGm;
	}

	public void setIsGm(int isGm) {
		this.isGm = isGm;
	}

	public int getIsGuider() {
		return isGuider;
	}

	public void setIsGuider(int isGuider) {
		this.isGuider = isGuider;
	}

	@Override
	public long getId() {
		return this.keyId;
	}

	@Override
	public void setId(long id) {
		this.keyId = (int) id;
	}

}
