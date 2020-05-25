package util;

import net.sf.json.JSONObject;

public class DrawBackEvent extends TimerEvent {

	public DrawBackEvent(long id) {
		super(id, -1, 100, 100);
	}

	public static final String WITH_DRAW_URL = "http://192.168.20.183:9081/player/backMoney";

	@Override
	public void action() {
		Draw draw = PlayerHandlerTest.getInst().getDraw((int) id);
		if (draw == null) {
			return;
		}
		JSONObject json = getDraw(draw);
		String result = HttpUtils.sentPost(WITH_DRAW_URL, json.toString());
		System.out.println(result);
		if (result == null) {
			return;
		}
		JSONObject p = JSONObject.fromObject(result);
		int code = p.getInt("result_code");
		if (code == 200) {
			System.out.println("退款成功");
			draw.setStatus(1);
			PlayerHandlerTest.getInst().putDraw(draw);
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
