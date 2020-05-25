package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.DbExtreme;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ExtremeProvider extends DataProvider<DbExtreme> {

	private static final Logger logger = LoggerFactory.getLogger(ExtremeProvider.class);

	public static ExtremeProvider getInst() {
		return BeanManager.getBean(ExtremeProvider.class);
	}

	public ExtremeProvider() {
		super("");
	}

	@Override
	protected Class<DbExtreme[]> getClassType() {
		return DbExtreme[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected DbExtreme convertDBResult2Bean(ASObject o) {
		DbExtreme bean = new DbExtreme();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(DbExtreme bean) {
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
