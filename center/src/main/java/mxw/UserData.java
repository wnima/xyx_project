package mxw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.bean.User;
import util.DateUtil;

public class UserData {

	private static final Logger logger = LoggerFactory.getLogger(UserData.class);

	private User user;
	private int combatId;// 当前关卡ID
	private boolean online = false;// 是否在线
	private boolean logout = false;// 掉线或者退出标志
	private int logoutCrons = 0;

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public long getPlayerId() {
		return user.getUserId();
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isLogout() {
		return logout;
	}

	public void setLogout(boolean logout) {
		this.logout = logout;
	}

	public int getLogoutCrons() {
		return logoutCrons;
	}

	public void setLogoutCrons(int logoutCrons) {
		this.logoutCrons = logoutCrons;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void init(User user) {
		this.user = user;
	}

	public void addPower(int add) {
		resetPower();
		int power = user.getPower();
		user.setPower(power + add);
		if (power + add >= 20) {
			user.setPowerTime(DateUtil.getSecondTime());
		}
	}

	public void reducePower(int num) {
		if (num < 0) {
			return;
		}
		resetPower();
		int power = user.getPower();
		user.setPower(power - num);
		if (power == 20) {// 如果是满的，则开始纪录时间
			user.setPowerTime(DateUtil.getSecondTime());
		}
	}

	public void resetPower() {
		int power = user.getPower();

		int powerTime = user.getPower();
		int second = DateUtil.getSecondTime();

		// 新增power
		int add = (second - powerTime) / 300;
		if (add > 0) {
			if (add + power >= 20) {
				user.setPower(20);
				user.setPowerTime(second);
			} else {
				user.setPower(add + power);
				user.setPowerTime(powerTime + add * 300);
			}
		}
	}

}
