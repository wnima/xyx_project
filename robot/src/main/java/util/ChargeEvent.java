package util;

import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONObject;

public class ChargeEvent extends TimerEvent {

	public static final String CHARGE_URL = "http://192.168.20.183:9081/player/charge";

	public ChargeEvent(long id) {
		super(id, 1000, 100, 100);
	}

	public static int[] charges = { 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000 };

	@Override
	public void action() {
		JSONObject json = new JSONObject();
		json.put("playerId", id);
		int random = new Random().nextInt(charges.length);
		json.put("money", charges[random]);
		json.put("orderId", UUID.randomUUID().toString());
		String result = HttpUtils.sentPost(CHARGE_URL, json.toString());
		System.out.println(result);
	}

}
