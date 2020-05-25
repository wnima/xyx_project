package config.provider;

import java.util.Iterator;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyLively;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyLivelyProvider extends BaseProvider<ConfPartyLively> {

	public static ConfPartyLivelyProvider getInst() {
		return BeanManager.getBean(ConfPartyLivelyProvider.class);
	}

	public ConfPartyLivelyProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyLively[]> getClassType() {
		return ConfPartyLively[].class;
	}

	@Override
	protected ConfPartyLively convertDBResult2Bean(ASObject o) {
		ConfPartyLively conf = new ConfPartyLively();
		conf.setLivelyLv(o.getInt("livelyLv"));
		conf.setLivelyExp(o.getInt("livelyExp"));
		conf.setCostLively(o.getInt("costLively"));
		conf.setResource(o.getInt("resource"));
		conf.setScience(o.getInt("science"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyLively conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public int getPartyLiveBuild(int lively) {
		ConfPartyLively maxLive = getMaxLive();
		if (maxLive != null && maxLive.getLivelyExp() <= lively) {
			return maxLive.getScience();
		}

		int size = getAllConfig().size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = getConfigById(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}
		if (entity != null) {
			return entity.getScience();
		}
		return 1;
	}

	public ConfPartyLively getMaxLive() {
		ConfPartyLively max = null;
		Iterator<ConfPartyLively> it = getAllConfig().iterator();
		while (it.hasNext()) {
			ConfPartyLively next = it.next();
			if (max == null) {
				max = next;
			} else {
				if (next.getLivelyExp() > max.getLivelyExp()) {
					max = next;
				}
			}
		}
		return max;
	}

	public int costLively(int lively) {
		ConfPartyLively maxLive = getMaxLive();
		if (maxLive != null && maxLive.getLivelyExp() <= lively) {
			lively = lively - maxLive.getCostLively();
			lively = lively < 0 ? 0 : lively;
			return lively;
		}

		int size = getAllConfig().size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = getConfigById(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}
		if (entity != null) {
			lively = lively - entity.getCostLively();
			lively = lively < 0 ? 0 : lively;
		}
		return lively;
	}

	public int getPartyLiveResource(int lively) {
		int size = getAllConfig().size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = getConfigById(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}

		if (entity != null) {
			return entity.getResource();
		} else {
			ConfPartyLively ee = getConfigById(size);
			if (ee.getLivelyExp() <= lively) {
				return ee.getResource();
			}
		}
		return 0;
	}

}
