package db.data;

import java.sql.SQLException;

import util.ASObject;

public interface IModuleData {

	void load() throws SQLException;

	void save() throws SQLException;

	void update(ASObject moduleData);

	void delete(ASObject moduleData);

	ASObject getData();

	boolean checkSame(ASObject newData);

}
