package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfMine;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfMineProvider extends BaseProvider<ConfMine> {

	public static ConfMineProvider getInst() {
		return BeanManager.getBean(ConfMineProvider.class);
	}

	public ConfMineProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfMine[]> getClassType() {
		return ConfMine[].class;
	}

	@Override
	protected ConfMine convertDBResult2Bean(ASObject o) {
		ConfMine conf = new ConfMine();
		conf.setPos(o.getInt("pos"));
		conf.setType(o.getInt("type"));
		conf.setLv(o.getInt("lv"));
		conf.setDropOne(JSON.parseObject(o.getString("dropOne"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfMine conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
