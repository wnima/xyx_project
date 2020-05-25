package util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ChannelUtil {

	public static void closeChannel(ChannelHandlerContext ctx, String reason) {
		ctx.close();
	}

	public static void setUserId(ChannelHandlerContext ctx, Integer userId) {
		AttributeKey<Integer> KEY = AttributeKey.valueOf("userId");
		Attribute<Integer> attribute = ctx.channel().attr(KEY);
		attribute.set(userId);
	}

	public static Integer getUserId(ChannelHandlerContext ctx) {
		AttributeKey<Integer> KEY = AttributeKey.valueOf("userId");
		return ctx.channel().attr(KEY).get();
	}

	public static void setId(ChannelHandlerContext ctx, Integer id) {
		AttributeKey<Integer> KEY = AttributeKey.valueOf("ID");
		Attribute<Integer> attribute = ctx.channel().attr(KEY);
		attribute.set(id);
	}

	public static Integer getId(ChannelHandlerContext ctx) {
		AttributeKey<Integer> KEY = AttributeKey.valueOf("ID");
		return ctx.channel().attr(KEY).get();
	}

	/**
	 * IP转成整型
	 * 
	 * @param ip
	 * @return
	 */
	private static Long ip2long(String ip) {
		Long num = 0L;
		if (ip == null) {
			return num;
		}

		try {
			ip = ip.replaceAll("[^0-9\\.]", ""); // 去除字符串前的空字符
			String[] ips = ip.split("\\.");
			if (ips.length == 4) {
				num = Long.parseLong(ips[0], 10) * 256L * 256L * 256L + Long.parseLong(ips[1], 10) * 256L * 256L + Long.parseLong(ips[2], 10) * 256L + Long.parseLong(ips[3], 10);
				num = num >>> 0;
			}
		} catch (NullPointerException ex) {
		}

		return num;
	}
}
