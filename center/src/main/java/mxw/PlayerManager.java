package mxw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.bean.User;
import data.provider.BagProvider;
import manager.ChapterManager;

//@Singleton
public class PlayerManager {

	private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);

	private Map<Long, UserData> playerMap = new ConcurrentHashMap<>();
	// account,userId
	private Map<String, Long> accounts = new ConcurrentHashMap<>();
	// platId,userId
	private Map<String, Long> plats = new ConcurrentHashMap<>();

	private PlayerManager() {
	}

	// public static PlayerManager getInst() {
	// return BeanManager.getBean(PlayerManager.class);
	// }

	static PlayerManager inst = new PlayerManager();

	public static PlayerManager getInst() {
		return inst;
	}

	public void load() {
	}

	public void loadUser(User user) {
		UserData userData = new UserData();
		userData.setUser(user);
		// 处理序列化数据
		serUserData(userData);
		registerPlayer(userData);
		logger.info("loadUser userId:{}", user.getUserId());
	}

	public boolean registerPlayer(UserData userData) {
		if (playerMap.containsKey(userData.getPlayerId())) {
			return true;
		}
		playerMap.put(userData.getPlayerId(), userData);

		if (userData.getUser().getAccount() != null) {
			accounts.put(userData.getUser().getAccount(), userData.getPlayerId());
		}

		if (userData.getUser().getPlatId() != null) {
			plats.put(userData.getUser().getPlatId(), userData.getPlayerId());
		}

		return true;
	}

	public void removePlayer(UserData player) {
		playerMap.remove(player.getPlayerId());
	}

	public Collection<UserData> getAllPlayers() {
		return new ArrayList<>(playerMap.values());
	}

	public List<UserData> getOnlinePlayers() {
		return playerMap.values().stream().filter(e -> e.isOnline()).collect(Collectors.toList());
	}

	public List<UserData> getCornList() {
		return playerMap.values().stream().filter(e -> e.isOnline() || e.getLogoutCrons() < 3).collect(Collectors.toList());
	}

	public int getOnlineCount() {
		return (int) getOnlinePlayers().stream().filter(e -> e.isOnline()).count();
	}

	public boolean isLoad(long playerId) {
		return playerMap.containsKey(playerId);
	}

	public boolean isOnline(long playerId) {
		if (!playerMap.containsKey(playerId)) {
			return false;
		}
		return playerMap.get(playerId).isOnline();
	}

	public UserData getPlayerByAccount(String account) {
		if (accounts.containsKey(account)) {
			long playerId = accounts.get(account);
			return getPlayerById(playerId);
		}
		return null;
	}

	public UserData getPlayerByPlatId(String platId) {
		if (plats.containsKey(platId)) {
			long playerId = plats.get(platId);
			return getPlayerById(playerId);
		}
		return null;
	}

	public UserData getPlayerById(long playerId) {
		UserData userData = playerMap.get(playerId);
		if (userData == null) {
			logger.info("getPlayerById isNull playerId:{}", playerId);
			return null;
		}
		userData.resetPower();
		return userData;
	}

	public void playerLogout(UserData player, int param1) {
		player.setOnline(false);
	}

	public void serUserData(UserData userData) {
	}

	public void dserUserData(UserData userData) {
	}

	public UserData init(long userId) {
		UserData userData = getPlayerById(userId);
		// 添加角色
		BagProvider.getInst().addProp(userData, 1, 1, 3);
		BagProvider.getInst().addProp(userData, 700, 5, 1);
		ChapterManager.getInst().initChapterFrist(userId, 1);
		return userData;
	}

}
