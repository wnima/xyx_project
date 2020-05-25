package database;

import java.math.BigInteger;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import util.ASObject;
import util.MiscUtil;

public class DataQueryResult extends DataQueryHandler {
	private List<ASObject> data = MiscUtil.newArrayList();

	@Override
	public void onResult(PreparedStatement ps) throws SQLException {
		this.data.clear();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			ASObject obj = new ASObject();
			int cnt = meta.getColumnCount();
			for (int i = 0; i < cnt; ++i) {
				String name = meta.getColumnLabel(i + 1);
				Object value = rs.getObject(name);
				if (value instanceof Blob) {
					value = MiscUtil.getBlobBytes((Blob) value);
				} else if (value instanceof Timestamp) {
					value = ((Timestamp) value).getTime();
				} else if (value instanceof BigInteger) {
					value = ((BigInteger) value).longValue();
				}
				obj.put(name, value);
			}
			this.data.add(obj);
		}
	}

	public void fillResult(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			ASObject obj = new ASObject();
			int cnt = meta.getColumnCount();
			for (int i = 0; i < cnt; ++i) {
				String name = meta.getColumnLabel(i + 1);
				Object value = rs.getObject(name);
				if (value instanceof Blob) {
					value = MiscUtil.getBlobBytes((Blob) value);
				} else if (value instanceof Timestamp) {
					value = ((Timestamp) value).getTime();
				} else if (value instanceof BigInteger) {
					value = ((BigInteger) value).longValue();
				}
				obj.put(name, value);
			}
			this.data.add(obj);
		}
	}

	public static List<ASObject> load(String tableName, String[] fields, Map<String, Object> where) {
		DataQueryResult result = new DataQueryResult();
		DBUtil.executeQuery(tableName, fields, where, result);
		return result.getData();
	}

	public static List<ASObject> load(String tableName, Map<String, Object> where) {
		DataQueryResult result = new DataQueryResult();
		DBUtil.executeQuery(tableName, where, result);
		return result.getData();
	}

	public static List<ASObject> load(String db, String tableName, String[] fields, Map<String, Object> where) {
		DataQueryResult result = new DataQueryResult();
		DBUtil.executeQuery(db, tableName, fields, where, result);
		return result.getData();
	}

	public static List<ASObject> load(String db, String tableName, Map<String, Object> where) {
		DataQueryResult result = new DataQueryResult();
		DBUtil.executeQuery(db, tableName, null, where, result);
		return result.getData();
	}

	public static List<ASObject> load(String sql) {
		DataQueryResult result = new DataQueryResult();
		ResultSet resultSet = DBUtil.executeQuery(sql);
		try {
			result.fillResult(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result.getData();
	}

	public List<ASObject> getData() {
		return this.data;
	}
}
