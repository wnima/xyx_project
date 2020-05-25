package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.LordData;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class LordDataProvider extends DataProvider<LordData> {

	private static final Logger logger = LoggerFactory.getLogger(LordDataProvider.class);

	public static LordDataProvider getInst() {
		return BeanManager.getBean(LordDataProvider.class);
	}

	public LordDataProvider() {
		super("");
	}

	@Override
	protected Class<LordData[]> getClassType() {
		return LordData[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected LordData convertDBResult2Bean(ASObject o) {
		LordData bean = new LordData();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(LordData bean) {
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
