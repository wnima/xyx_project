package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import util.LogHelper;
import util.MiscUtil;
import util.XProperties;

public class DBManager {

	private static final Logger logger = LoggerFactory.getLogger(DBManager.class);

	private static String defaultDatabase = "";
	private static String configDatabase;
	private static XProperties props = null;
	private static Map<String, BoneCP> connectionPoolMap = MiscUtil.newSyncHashMap();

	public static String getDefaultDatabase() {
		return defaultDatabase;
	}

	public static void setDefaultDatabase(String db) {
		defaultDatabase = db;
	}

	public static String getConfigDatabase() {
		return configDatabase == null ? defaultDatabase : configDatabase;
	}

	public static void setConfigDatabase(String configDatabase) {
		DBManager.configDatabase = configDatabase;
	}

	public static Connection getConnection() throws SQLException {
		return getConnection(defaultDatabase);
	}

	public static String getDbPoolStatus() {
		StringBuilder builder = new StringBuilder(64);
		for (Map.Entry<String, BoneCP> e : connectionPoolMap.entrySet()) {
			builder.append(String.format("database %s :", e.getKey()));
			builder.append(String.format("total created connection : %d\n", e.getValue().getTotalCreatedConnections()));
			builder.append(String.format("total free connection : %d\n", e.getValue().getTotalFree()));
			builder.append(String.format("total leased connection : %d\n", e.getValue().getTotalLeased()));
		}
		return builder.toString();
	}

	public static Connection getConnection(String db) throws SQLException {
		BoneCP cp = getBoneCP(db);
		return cp.getConnection();
	}

	private static BoneCP getBoneCP(String db) throws SQLException {
		if (props == null) {
			throw new RuntimeException("DBManager not initialized");
		}
		if (db != null) {
			db = db.trim();
		}
		synchronized (props) {
			if (!connectionPoolMap.containsKey(db)) {
				initConnectionPool(db);
			}
		}
		BoneCP cp = connectionPoolMap.get(db);
		return cp;
	}

	public static Future<Connection> getAsyncConnection(String db) throws SQLException {
		BoneCP cp = getBoneCP(db);
		return cp.getAsyncConnection();
	}

	public static boolean isInitialized() {
		return props != null;
	}

	public static void setProps(XProperties aProps) {
		props = aProps;
	}

	public static void closeAll() {
		for (BoneCP connectionPool : connectionPoolMap.values()) {
			connectionPool.close();
		}
		connectionPoolMap.clear();
	}

	public static void touch() throws SQLException {
		touch(defaultDatabase);
		if (configDatabase != null && !configDatabase.equals("")) {
			touch(configDatabase);
		}
	}

	public static void touch(String db) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection(db);
		} finally {
			close(conn);
		}
	}

	private static String buildKey(String db, String key) {
		return (db == null || db.length() == 0) ? key : (db + "." + key);
	}

	public static XProperties getDatabaseProperties() {
		return getDatabaseProperties(getDefaultDatabase());
	}

	public static XProperties getDatabaseProperties(String db) {
		String prefix = buildKey(db, "");
		XProperties newProps = new XProperties();
		for (Object keyObj : props.keySet()) {
			String key = (String) keyObj;
			if (key.startsWith(prefix)) {
				String value = props.getProperty(key);
				key = key.substring(prefix.length());
				newProps.setProperty(key, value);
			}
		}
		return newProps;
	}

	private static void initConnectionPool(String db) throws SQLException {
		if (connectionPoolMap.containsKey(db)) {
			return;
		}
		XProperties dbProps = getDatabaseProperties(db);
		//
		String driver = dbProps.getProperty("driver");
		String url = dbProps.getProperty("url");
		String user = dbProps.getProperty("user");
		String password = dbProps.getProperty("password");
		boolean closeWatch = dbProps.getBoolean("closeConnectionWatch", false);
		int idleTestMinutes = dbProps.getInteger("idleTestMinutes", 40);
		int idleMaxAge = dbProps.getInteger("idleMaxAgeMinutes", 30);
		int maxConnectionAge = dbProps.getInteger("maxConnectionAgeSeconds", 60 * 60);
		int minConnections = dbProps.getInteger("minConnections", 2);
		int maxConnections = dbProps.getInteger("maxConnections", 10);
		int partition = dbProps.getInteger("partition", 2);

		if (driver == null || driver.isEmpty()) {
			throw new IllegalArgumentException("invalid database: " + db);
		}
		// load driver
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new SQLException("Database driver not found: " + driver, e);
		}
		// init config
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(minConnections);
		config.setMaxConnectionsPerPartition(maxConnections);
		config.setPartitionCount(partition);
		config.setCloseConnectionWatch(closeWatch);
		// idle
		config.setIdleMaxAgeInMinutes(idleMaxAge);
		config.setIdleConnectionTestPeriodInMinutes(idleTestMinutes);
		config.setMaxConnectionAgeInSeconds(maxConnectionAge);
		// init connection pool
		connectionPoolMap.put(db, new BoneCP(config));
	}

	public static void close(PreparedStatement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
	}

	public static void close(Connection conn, PreparedStatement stmt) {
		close(stmt);
		close(conn);
	}

	public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(conn);
	}

	public static void close(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LogHelper.ERROR.error(e.getMessage() + " PreparedStatment close failure", e);
			}
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LogHelper.ERROR.error(e.getMessage() + " Connection close failure", e);
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LogHelper.ERROR.error(e.getMessage() + " ResultSet close failure", e);
			}
		}
	}
}
