package data.provider;

import java.util.Map;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.User;
import inject.BeanManager;
import mxw.PlayerManager;
import util.ASObject;

@Singleton
public class UserProvider extends DataProvider<User> {

	public static UserProvider getInst() {
		return BeanManager.getBean(UserProvider.class);
	}

	public UserProvider() {
		super("p_user");
	}

	@Override
	protected Class<User[]> getClassType() {
		return User[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected User convertDBResult2Bean(ASObject o) {
		User bean = new User();
		bean.setUserId(o.getInt("userId"));
		bean.setUserName(o.getString("userName"));
		bean.setAccount(o.getString("account"));
		bean.setPwd(o.getString("pwd"));
		bean.setPlatNo(o.getInt("platNo"));
		bean.setPlatId(o.getString("platId"));
		bean.setPlatProtrait(o.getString("platProtrait"));
		bean.setPlatName(o.getString("platName"));
		bean.setProtrait(o.getInt("protrait"));
		bean.setHeadFrame(o.getInt("headFrame"));
		bean.setGold(o.getLong("gold"));
		bean.setCoin(o.getLong("coin"));
		bean.setPower(o.getInt("power"));
		bean.setPowerTime(o.getInt("powerTime"));
		bean.setBan(o.getInt("ban"));
		bean.setBanMsg(o.getString("banMsg"));
		bean.setBanTime(o.getInt("banTime"));
		bean.setWhite(o.getInt("white"));
		bean.setIp(o.getString("ip"));
		bean.setCreateTime(o.getInt("createTime"));
		bean.setLoginTime(o.getInt("loginTime"));
		bean.setMxwShop(o.getString("mxwShop"));
		bean.setGameType(o.getInt("gameType"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(User bean) {
		ASObject o = new ASObject();
		o.put("userId", bean.getUserId());
		o.put("userName", bean.getUserName());
		o.put("account", bean.getAccount());
		o.put("pwd", bean.getPwd());
		o.put("platNo", bean.getPlatNo());
		o.put("platId", bean.getPlatId());
		o.put("platProtrait", bean.getPlatProtrait());
		o.put("platName", bean.getPlatName());
		o.put("protrait", bean.getProtrait());
		o.put("headFrame", bean.getHeadFrame());
		o.put("gold", bean.getGold());
		o.put("coin", bean.getCoin());
		o.put("power", bean.getPower());
		o.put("powerTime", bean.getPowerTime());
		o.put("ban", bean.getBan());
		o.put("banMsg", bean.getBanMsg());
		o.put("banTime", bean.getBanTime());
		o.put("white", bean.getWhite());
		o.put("ip", bean.getIp());
		o.put("createTime", bean.getCreateTime());
		o.put("loginTime", bean.getLoginTime());
		o.put("mxwShop", bean.getMxwShop());
		o.put("gameType", bean.getGameType());
		return o;
	}

	@Override
	public void init() {
		for (User e : getAllBean()) {
			PlayerManager.getInst().loadUser(e);
		}
	}

	@Override
	protected String getIdKey() {
		return "userId";
	}

	@Override
	protected void cronTask() {// 定时更新在线玩家
		PlayerManager.getInst().getCornList().forEach(e -> {
			udp(e.getUser());
			if (e.isLogout()) {// 记录离线更新次数
				e.setLogoutCrons(e.getLogoutCrons() + 1);
			}
		});
	}

}
