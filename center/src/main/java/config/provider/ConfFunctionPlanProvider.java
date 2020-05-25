package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfFunctionPlan;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfFunctionPlanProvider extends BaseProvider<ConfFunctionPlan> {

	public static ConfFunctionPlanProvider getInst() {
		return BeanManager.getBean(ConfFunctionPlanProvider.class);
	}

	public ConfFunctionPlanProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfFunctionPlan[]> getClassType() {
		return ConfFunctionPlan[].class;
	}

	@Override
	protected ConfFunctionPlan convertDBResult2Bean(ASObject o) {
		ConfFunctionPlan conf = new ConfFunctionPlan();
		conf.setKeyId(o.getInt("keyId"));
		conf.setFunId(o.getInt("funId"));
		conf.setFunname(o.getString("funname"));
		conf.setDayiy(o.getInt("dayiy"));
		conf.setRules(o.getString("rules"));
		conf.setWhole(o.getInt("whole"));
		conf.setServerList(JSON.parseObject(o.getString("serverList"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfFunctionPlan conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
