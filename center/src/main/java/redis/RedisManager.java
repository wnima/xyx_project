package redis;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import util.LogHelper;

@Singleton
public class RedisManager {
	private static final Logger logger = LoggerFactory.getLogger(RedisManager.class);

	public static RedisManager getInst() {
		return BeanManager.getBean(RedisManager.class);
	}

	private JedisPool pool;

	public void init(String host, int port, String password) {
//		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxTotal(10);
//		if (password != null && password.length() > 0) {
//			pool = new JedisPool(config, host, port, 5000, password);
//		} else {
//			pool = new JedisPool(config, host, port, 5000);
//		}
	}

	public void setMapFieldPhp(String key, String field, String fieldValue) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hset(key, field, fieldValue);
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
//			logger.error("", e);
		} finally {
			jedis.close();
		}
	}

	public List<Object> setMapValue(String key, Map<String, String> map) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Transaction tx = jedis.multi();
			map.entrySet().forEach(e -> tx.hset(key, e.getKey(), e.getValue()));
			return tx.exec();
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
//			logger.error("", e);
			return null;
		} finally {
			jedis.close();
		}
	}

	public void hSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hset(key, value, value);
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
//			logger.error("", e);
		} finally {
			jedis.close();
		}
	}

	public String setValue(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
//			logger.error("", e);
			return null;
		} finally {
			jedis.close();
		}
	}

}
