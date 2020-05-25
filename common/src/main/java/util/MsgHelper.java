package util;

import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;

public class MsgHelper {

	/**
	 * 发送回复消息
	 * 
	 * @param ioSession
	 * @param userId
	 * @param code
	 * @param message
	 */
	public static void sendResponse(ChannelHandlerContext ctx, long playerId, ResponseCode code, MessageLite message) {
		CocoPacket packet = new CocoPacket(code.getValue(), message == null ? null : message.toByteArray(), playerId);
		packet.resetReqIdToRequest();
		ctx.writeAndFlush(packet);
	}

	/**
	 * 发送回复消息
	 * 
	 * @param ctx
	 * @param code
	 * @param message
	 */
	public static void sendResponse(ChannelHandlerContext ctx, ResponseCode code, MessageLite message) {
		CocoPacket rsp = new CocoPacket(code.getValue(), message == null ? null : message.toByteArray());
		rsp.resetReqIdToRequest();
		ctx.writeAndFlush(rsp);
	}

	/**
	 * 发送请求消息
	 * 
	 * @param ctx
	 * @param code
	 * @param message
	 */
	public static void sendRequest(ChannelHandlerContext ctx, RequestCode code, MessageLite message) {
		CocoPacket rsp = new CocoPacket(code.getValue(), message == null ? null : message.toByteArray());
		rsp.resetReqIdToRequest();
		ctx.writeAndFlush(rsp);
	}

	/**
	 * 发送请求消息
	 * 
	 * @param ctx
	 * @param userId
	 * @param code
	 * @param message
	 */
	public static void sendRequest(ChannelHandlerContext ctx, long userId, RequestCode code, MessageLite message) {
		CocoPacket rsp = new CocoPacket(code.getValue(), message == null ? null : message.toByteArray(), userId);
		rsp.resetReqIdToRequest();
		ctx.writeAndFlush(rsp);
	}

	/**
	 * @param code
	 * @param message
	 */
	public static void synToAllPlayer(RequestCode code, MessageLite message) {
		
	}
	
//	public static void sendError(ChannelHandlerContext ctx,long userId,GameError gameError) {
//		
//	}

}
