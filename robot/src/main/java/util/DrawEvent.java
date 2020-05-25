package util;

import java.util.UUID;

import net.sf.json.JSONObject;

public class DrawEvent extends TimerEvent {

	private long userId;

	public DrawEvent(long id, long userId) {
		super(id, 1, 100, 100);
		this.userId = userId;
	}

	public static final String WITH_DRAW_URL = "http://192.168.20.183:9081/player/withdraw";

	public static int[] DRAW = { 100000 };

	@Override
	public void action() {
		Draw draw = new Draw();
		draw.setId((int) id);
		draw.setUserId(userId);
		draw.setMoney(100000);
		draw.setOrderId(UUID.randomUUID().toString());

		JSONObject json = getDraw(draw);

		String result = HttpUtils.sentPost(WITH_DRAW_URL, json.toString());
		System.out.println(result);
		if (result != null) {
			JSONObject p = JSONObject.fromObject(result);
			int code = p.getInt("result_code");
			if (code == 200) {
				System.out.println("提现成功");
				PlayerHandlerTest.getInst().putDraw(draw);
			}
		}
	}

	public JSONObject getDraw(Draw draw) {
		JSONObject json = new JSONObject();
		json.put("money", draw.getMoney());
		json.put("playerId", draw.getUserId());
		json.put("orderId", draw.getOrderId());
		return json;
	}
}
