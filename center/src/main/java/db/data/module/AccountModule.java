package db.data.module;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DBUtil;
import database.DataQueryHandler;
import db.data.IModuleData;
import util.ASObject;

public class AccountModule implements IModuleData {

	private class AccountData {
		public int accountId = 0;
		public int lianyunUid = 0;
		public String userName = null;
		public int charId = 0;
		public String charName = null;
	}

	private static final Logger logger = LoggerFactory.getLogger(AccountModule.class);
	private final int char_id;
	private boolean loaded = false;
	private AccountData accountData = new AccountData();

	public AccountModule(int char_id) {
		this.char_id = char_id;
	}

	@Override
	public void load() throws SQLException {
		logger.info("AccountModule load");
		DataQueryHandler query = new DataQueryHandler() {
			@Override
			public void onResult(PreparedStatement ps) throws SQLException {
				ResultSet rs = ps.getResultSet();
				if (rs.next()) {
					accountData.accountId = rs.getInt("account_id");
					accountData.userName = rs.getString("account_name");
					accountData.charId = rs.getInt("char_id");
					accountData.charName = rs.getString("char_name");
					accountData.lianyunUid = rs.getInt("lianyun_uid");
					loaded = true;
				} else {
					loaded = false;
					logger.info("LoadAccountFailure charId = " + char_id);
				}
			}
		};
		String sql = "SELECT a.id AS account_id, a.name AS account_name, c.id AS char_id, c.name AS char_name " + ", a.lianyunUid as lianyun_uid FROM `accounts` a, `characters` c WHERE a.id = c.accountid AND c.id = ?";
		DBUtil.execute(sql, new Object[] { char_id }, query);
	}

	@Override
	public void save() throws SQLException {
		// do nothing
	}

	@Override
	public void update(ASObject moduleData) {
		// do nothing
	}

	@Override
	public ASObject getData() {
		if (loaded == false) {
			return null;
		}
		ASObject ret = new ASObject();
		ret.put("accountId", accountData.accountId);
		ret.put("userName", accountData.userName);
		ret.put("charId", accountData.charId);
		ret.put("charName", accountData.charName);
		ret.put("lianyunUid", accountData.lianyunUid);
		return ret;
	}

	@Override
	public boolean checkSame(ASObject newData) {
		return true;
	}

	@Override
	public String toString() {
		return "AccountModule{" + "char_id=" + char_id + ", loaded=" + loaded + ", accountData=" + accountData + '}';
	}

	@Override
	public void delete(ASObject moduleData) {
		// TODO Auto-generated method stub

	}
}
