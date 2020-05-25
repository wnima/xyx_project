package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.SmallId;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class SmallIdProvider extends DataProvider<SmallId> {

	private static final Logger logger = LoggerFactory.getLogger(SmallIdProvider.class);

	public static SmallIdProvider getInst() {
		return BeanManager.getBean(SmallIdProvider.class);
	}

	public SmallIdProvider() {
		super("");
	}

	@Override
	protected Class<SmallId[]> getClassType() {
		return SmallId[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected SmallId convertDBResult2Bean(ASObject o) {
		SmallId bean = new SmallId();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(SmallId bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
	}

	/**
	 * 判断是否小号 Method: isSmallId @Description: TODO @param lordId @return @return
	 * boolean @throws
	 */
	public boolean isSmallId(long lordId) {
		return lordId == 0 || getBeanById(lordId) != null;
	}

}
