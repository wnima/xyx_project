package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bean.Robot;
import packet.CocoPacket;
import pb.AccountPb.PBBindPhone;
import pb.AccountPb.PBDeviceLogin;
import pb.AccountPb.PBPhoneLogin;
import pb.AccountPb.PBPhoneRegist;
import pb.AccountPb.PBSmsCode;
import pb.HallPb.PbBegin;
import protocol.c2s.RequestCode;
import util.ChannelUtil;

public class AccountManager {

	private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);

	/**
	 * 设备号登录
	 * 
	 * @param robot
	 * @param device
	 */
	public static void deviceLogin(Robot robot, String device) {
		logger.info("deviceLogin id:{} playerId:{} device:{}", ChannelUtil.getId(robot.getCtx()), robot.getPlayerId(), device);
		PBDeviceLogin.Builder pb = PBDeviceLogin.newBuilder();
		pb.setVersion("1.0");
		pb.setDeviceNo(device);
		pb.setPlatNo(12);
		pb.setOs("0");
		pb.setPcMail(200);
		pb.setSgin("8121312312");
		CocoPacket packet = new CocoPacket(RequestCode.DEVICE_LOGIN, pb.build());
		// CocoPacket response = robot.synSendMsg(packet);
		// logger.info("deviceLogin response:{}", response);
		robot.sendMsg(packet);
	}

	/**
	 * 进入大厅
	 * 
	 * @param robot
	 */
	public static void begin(Robot robot) {
		logger.info("deviceLogin id:{} playerId:{} token:{}", robot.getId(), robot.getPlayerId(), robot.getToken());
		PbBegin.Builder pb = PbBegin.newBuilder();
		pb.setRoleId((int) robot.getPlayerId());
		pb.setToken(robot.getToken());
		CocoPacket packet = new CocoPacket(RequestCode.ENTER_HALL, pb.build());
		robot.sendMsg(packet);
	}

	/**
	 * 绑定手机号
	 * 
	 * @param robot
	 */
	public static void bindPhone(Robot robot, String phone) {
		PBBindPhone.Builder pb = PBBindPhone.newBuilder();
		pb.setPhone("15871490051");
		pb.setCode("lzsb");
		pb.setPassword("123456");
		CocoPacket packet = new CocoPacket(RequestCode.BIND_PHONE, pb.build());
		robot.sendMsg(packet);
	}

	/**
	 * 心跳
	 * 
	 * @param robot
	 */
	public static void ping(Robot robot) {
		CocoPacket packet = new CocoPacket(RequestCode.PING, null);
		robot.sendMsg(packet);
	}

	public static void smsCodeRs(Robot robot) {
		PBSmsCode.Builder pb = PBSmsCode.newBuilder();
		pb.setPhone("15871490051");
		CocoPacket packet = new CocoPacket(RequestCode.SMS_CODE, pb.build());
		robot.sendMsg(packet);
	}

	public static void phoneRegister(Robot robot) {
		PBPhoneRegist.Builder pb = PBPhoneRegist.newBuilder();
		pb.setVersion("v0.1");
		pb.setPhone("13405154141");
		pb.setPassword("123456");
		pb.setPlatNo(10);
		pb.setPgNo(101);
		pb.setOs("1");
		pb.setCode("123456");
		pb.setIp("127.0.0.1");
		CocoPacket packet = new CocoPacket(RequestCode.PHONE_REGISTER, pb.build());
		robot.sendMsg(packet);
	}

	public static void phoneLogin(Robot robot) {
		PBPhoneLogin.Builder pb = PBPhoneLogin.newBuilder();
		pb.setVersion("v0.1");
		pb.setPhone("13405154141");
		pb.setPassword("123451");
		pb.setPcMail(10);
		CocoPacket packet = new CocoPacket(RequestCode.PHONE_LOGIN, pb.build());
		robot.sendMsg(packet);
	}

}
