package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Lord;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class LordProvider extends DataProvider<Lord> {

	private static final Logger logger = LoggerFactory.getLogger(LordProvider.class);

	public static LordProvider getInst() {
		return BeanManager.getBean(LordProvider.class);
	}

	public LordProvider() {
		super("");
	}

	@Override
	protected Class<Lord[]> getClassType() {
		return Lord[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Lord convertDBResult2Bean(ASObject o) {
		Lord bean = new Lord();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Lord bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
		getAllCache().forEach(e -> {
			long lordId = e.getLong("lordId");
			getBeanById(lordId);
		});
	}

}
