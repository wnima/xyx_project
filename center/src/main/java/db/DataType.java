package db;

public enum DataType {
	PLAYER(1, "p_user"), // 用户
	MXW_CHAPTER(2, "p_max_chapter"), // 关卡信息
	;

	private int id;
	private String tableName;

	private DataType(int id, String tableName) {
		this.id = id;
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public int ID() {
		return id;
	}

}
