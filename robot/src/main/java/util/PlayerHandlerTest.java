package util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

public class PlayerHandlerTest {

	static PlayerHandlerTest handler = new PlayerHandlerTest();

	public static PlayerHandlerTest getInst() {
		return handler;
	}

	protected ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(30);

	private Map<String, Draw> draws = new ConcurrentHashMap<>();// 订单

	public static void main(String[] args) {
		PlayerHandlerTest.getInst().init();
	}

	private void init() {
		// 提现
		DrawEvent drawEvent = new DrawEvent(0, 2303);
		addTimeEvent(drawEvent);
		//
		// 拒绝退款
		DrawBackEvent backEvent = new DrawBackEvent(0);
		addTimeEvent(backEvent);
	}

	public void addTimeEvent(TimerEvent event) {
		ScheduledFuture<?> future = exec.scheduleAtFixedRate(event, event.getDelay(), event.getPeriod(), event.getTimeUnit());
		event.setFuture(future);
	}

	public void shutDown() {
		exec.shutdown();
	}

	public boolean putDraw(Draw draw) {
		if (draws.containsKey(draw.getOrderId())) {
			return false;
		}
		return draws.put(draw.getOrderId(), draw) == null;
	}

	public Draw getDraw(int id) {
		Optional<Draw> opt = draws.values().stream().filter(e -> e.getId() == id && e.getStatus() == 0 && !e.isDeal()).findAny();
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	public List<Draw> getDrawList() {
		return draws.values().stream().filter(e -> e.getStatus() == 0).collect(Collectors.toList());
	}
}