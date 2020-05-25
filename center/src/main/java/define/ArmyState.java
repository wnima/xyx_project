package define;

public interface ArmyState {
	// 行军
	final int MARCH = 1;

	// 返回
	final int RETREAT = 2;

	// 采集
	final int COLLECT = 3;

	// 驻军
	final int GUARD = 4;

	// 等待
	final int WAIT = 5;

	// 援助行军
	final int AID = 6;

	// 参与军团战
	final int WAR = 7;
}
