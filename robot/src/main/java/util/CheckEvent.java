package util;

import java.util.List;

public class CheckEvent extends TimerEvent {

	public CheckEvent(long id) {
		super(id, -1, 1000, 1000);
	}

	@Override
	public void action() {
		List<Draw> list = PlayerHandlerTest.getInst().getDrawList();
		System.out.println("未执行完毕" + list.size());
	}

}
