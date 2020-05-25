package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfActAward;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfActAwardProvider extends BaseProvider<ConfActAward> {

	public static ConfActAwardProvider getInst() {
		return BeanManager.getBean(ConfActAwardProvider.class);
	}

	public ConfActAwardProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfActAward[]> getClassType() {
		return ConfActAward[].class;
	}

	@Override
	protected ConfActAward convertDBResult2Bean(ASObject o) {
		ConfActAward conf = new ConfActAward();
		conf.setKeyId(o.getInt("keyId"));
		conf.setActivityId(o.getInt("activityId"));
		conf.setSortId(o.getInt("sortId"));
		conf.setCond(o.getInt("cond"));
		conf.setAwardList(JSON.parseObject(o.getString("awardList"), List.class));
		conf.setDesc(o.getString("desc"));
		conf.setParam(o.getString("param"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfActAward conf) {
		ASObject o = new ASObject();
		o.put("keyId", conf.getId());
		o.put("activityId", conf.getActivityId());
		o.put("sortId", conf.getSortId());
		o.put("cond", conf.getCond());
		o.put("awardList", conf.getAwardList().toString());
		o.put("desc", conf.getDesc());
		o.put("param", conf.getParam());
		return o;
	}

	@Override
	public void init() {
	}

}
