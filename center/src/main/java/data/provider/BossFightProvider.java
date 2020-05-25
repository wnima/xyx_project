package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.BossFight;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class BossFightProvider extends DataProvider<BossFight> {

	private static final Logger logger = LoggerFactory.getLogger(BossFightProvider.class);

	public static BossFightProvider getInst() {
		return BeanManager.getBean(BossFightProvider.class);
	}

	public BossFightProvider() {
		super("");
	}

	@Override
	protected Class<BossFight[]> getClassType() {
		return BossFight[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected BossFight convertDBResult2Bean(ASObject o) {
		BossFight bean = new BossFight();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(BossFight bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
	}

}
