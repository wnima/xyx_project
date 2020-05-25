package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfArenaAward;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfArenaAwardProvider extends BaseProvider<ConfArenaAward> {

	public static ConfArenaAwardProvider getInst() {
		return BeanManager.getBean(ConfArenaAwardProvider.class);
	}

	public ConfArenaAwardProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfArenaAward[]> getClassType() {
		return ConfArenaAward[].class;
	}

	@Override
	protected ConfArenaAward convertDBResult2Bean(ASObject o) {
		ConfArenaAward conf = new ConfArenaAward();
		conf.setKeyId(o.getInt("keyId"));
		conf.setBeginRank(o.getInt("beginRank"));
		conf.setEndRank(o.getInt("endRank"));
		conf.setAward(JSON.parseObject(o.getString("award"), List.class));
		conf.setScore(o.getInt("score"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfArenaAward conf) {
		ASObject o = new ASObject();
		o.put("keyId", conf.getKeyId());
		o.put("beginRank", conf.getBeginRank());
		o.put("endRank", conf.getEndRank());
		o.put("award", conf.getAward().toString());
		o.put("score", conf.getScore());
		return o;
	}

	@Override
	public void init() {
	}

}
