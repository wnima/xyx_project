package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.bossFight.domain.Boss;
import com.google.inject.Singleton;

import data.DataProvider;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class BossProvider extends DataProvider<Boss> {

	private static final Logger logger = LoggerFactory.getLogger(BossProvider.class);

	public static BossProvider getInst() {
		return BeanManager.getBean(BossProvider.class);
	}

	public BossProvider() {
		super("");
	}

	@Override
	protected Class<Boss[]> getClassType() {
		return Boss[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Boss convertDBResult2Bean(ASObject o) {
		Boss bean = new Boss();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Boss bean) {
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
