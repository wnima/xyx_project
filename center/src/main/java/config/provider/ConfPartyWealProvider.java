package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyWeal;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyWealProvider extends BaseProvider<ConfPartyWeal> {

	public static ConfPartyWealProvider getInst() {
		return BeanManager.getBean(ConfPartyWealProvider.class);
	}

	public ConfPartyWealProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyWeal[]> getClassType() {
		return ConfPartyWeal[].class;
	}

	@Override
	protected ConfPartyWeal convertDBResult2Bean(ASObject o) {
		ConfPartyWeal conf = new ConfPartyWeal();
		conf.setWealLv(o.getInt("wealLv"));
		conf.setWealList(JSON.parseObject(o.getString("wealList"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyWeal conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
