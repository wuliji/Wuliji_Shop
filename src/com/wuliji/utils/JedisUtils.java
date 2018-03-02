/**
 * 
 */
package com.wuliji.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>Title: RedisUtils</p>
 * <p>Description: 配置redis连接池</p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月12日 上午11:16:35
 */
public class JedisUtils {
	
	private static JedisPool pool = null;
		
	static{
		
		Properties pro = new Properties();
		InputStream in = JedisUtils.class.getClassLoader().getResourceAsStream("redis.properties");
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//1.配置JedisConfigue
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//设置最大空闲连接数
		jedisPoolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxdle").toString()));
		//设置最小空闲连接数
		jedisPoolConfig.setMinIdle(Integer.parseInt(pro.get("redis.mindle").toString()));
		//设置总的连接数
		jedisPoolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxtotal").toString()));
		
		pool = new JedisPool(jedisPoolConfig, pro.get("redis.url").toString(), 
				Integer.parseInt(pro.get("redis.port").toString()));
	}
	
	/**
	 * 获得连接
	 * @return
	 */
	public static Jedis getJedis(){
		return pool.getResource();
	}
}
