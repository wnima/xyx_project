package manager;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import data.bean.SmallId;
import inject.BeanManager;

@Singleton
public class SmallIdManager {

//	private SmallIdDao smallDao;

	private Map<Long, SmallId> smallIdCache = new HashMap<>();

	public static SmallIdManager getInst() {
		return BeanManager.getBean(SmallIdManager.class);
	}

	public void load() {
//		List<SmallId> list = smallDao.load();
//		for (SmallId smallId : list) {
//			smallIdCache.put(smallId.getLordId(), smallId);
//		}
	}

	/**
	 * 判断是否小号 Method: isSmallId @Description: TODO @param lordId @return @return
	 * boolean @throws
	 */
	public boolean isSmallId(long lordId) {
		return lordId == 0 || smallIdCache.containsKey(lordId);
	}
}
