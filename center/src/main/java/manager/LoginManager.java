package manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import util.DateUtil;
import util.Pair;

@Singleton
public class LoginManager {

	private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

	private Map<Long, Pair<Boolean, Integer>> logoutMap = new HashMap<>();

	public static LoginManager getInst() {
		return BeanManager.getBean(LoginManager.class);
	}

	public void load() {
	}

	public Pair<Boolean, Integer> getLogout(long playerId) {
		return logoutMap.get(playerId);
	}

	public void logout(long playerId) {
		if (logoutMap.containsKey(playerId)) {
			Pair<Boolean, Integer> pair = logoutMap.get(playerId);
			pair.setLeft(true);
			pair.setRight(DateUtil.getSecondTime());
		} else {
			Pair<Boolean, Integer> pair = new Pair<>(true, DateUtil.getSecondTime());
			logoutMap.put(playerId, pair);
		}
	}

	public void login(long playerId) {
		if (logoutMap.containsKey(playerId)) {
			logoutMap.remove(playerId);
		}
	}

}
