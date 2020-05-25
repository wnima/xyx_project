package db;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.data.DBAction;
import db.data.IModuleData;
import util.ASObject;
import util.LogHelper;
import util.MiscUtil;

public abstract class CacheData {

	private static final Logger logger = LoggerFactory.getLogger(CharData.class);

	protected int dataId = 0;
	protected final Map<DBAction, IModuleData> modules = MiscUtil.newArrayMap();

	public CacheData() {
		// donothing
	}

	public int getDataId() {
		return this.dataId;
	}

	public ASObject getModuleData(DBAction action) {
		IModuleData mod = modules.get(action);
		if (mod == null) {
			return null;
		}
		synchronized (mod) {
			return mod.getData();
		}
	}


	public void updateModuleData(DBAction action, ASObject moduleData) {
		IModuleData mod = modules.get(action);
		if (mod == null) {
			return;
		}
		if (moduleData == null) {
			return;
		}
		synchronized (mod) {
			mod.update(moduleData);
		}
	}
	
	public void deleteModuleData(DBAction action, ASObject moduleData) {
		IModuleData mod = modules.get(action);
		if (mod == null) {
			return;
		}
		if (moduleData == null) {
			return;
		}
		synchronized (mod) {
			mod.delete(moduleData);
		}
	}

	public boolean loadAll() throws SQLException {
		for (Map.Entry<DBAction, IModuleData> modEntry : modules.entrySet()) {
			IModuleData mod = modEntry.getValue();
			mod.load();
		}
		logger.debug(" load player {} success ", dataId);
		return true;
	}

	public void saveAll() throws SQLException {
		for (Map.Entry<DBAction, IModuleData> modEntry : modules.entrySet()) {
			IModuleData mod = modEntry.getValue();
			try {
				synchronized (mod) {
					mod.save();
				}
			} catch (Exception e) {
//				logger.error("", e);
//				logger.error(mod.toString());
				LogHelper.ERROR.error(mod.toString());
				LogHelper.ERROR.error(e.getMessage(), e);
			}
		}
	}


	public DBAction checkSame(CacheData newData) {
		for (Map.Entry<DBAction, IModuleData> modEntry : modules.entrySet()) {
			IModuleData mod = modEntry.getValue();
			boolean ret = mod.checkSame(newData.getModuleData(modEntry.getKey()));
			if (false == ret) {
				return modEntry.getKey();
			}
		}
		return null;
	}

	public abstract void init(int dataId);
}
