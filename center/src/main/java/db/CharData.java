
package db;

import java.util.Arrays;

import db.data.DBAction;
import db.data.module.CommonModule;

public class CharData extends CacheData {

	public CharData() {
		// donothing
	}

	@Override
	public void init(int dataId) {
		this.dataId = dataId;
		modules.put(DBAction.PLAYER, new CommonModule(this.dataId, DataType.PLAYER.getTableName(), "userId", Arrays.asList("userId"), false));
	}

}
