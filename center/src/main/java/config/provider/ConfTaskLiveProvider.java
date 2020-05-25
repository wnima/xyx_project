package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfTaskLive;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfTaskLiveProvider extends BaseProvider<ConfTaskLive> {

	public static ConfTaskLiveProvider getInst() {
		return BeanManager.getBean(ConfTaskLiveProvider.class);
	}

	public ConfTaskLiveProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfTaskLive[]> getClassType() {
		return ConfTaskLive[].class;
	}

	@Override
	protected ConfTaskLive convertDBResult2Bean(ASObject o) {
		ConfTaskLive conf = new ConfTaskLive();
		conf.setId(o.getInt("id"));
		conf.setLive(o.getInt("live"));
		conf.setAwardList(JSON.parseObject(o.getString("awardList"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfTaskLive conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {

	}

	public ConfTaskLive getTaskLive(int liveAd, int totalLive) {
		for (ConfTaskLive e : getAllConfig()) {
			if (e.getLive() > liveAd && e.getLive() <= totalLive) {
				return e;
			}
		}
		return null;
	}
}
