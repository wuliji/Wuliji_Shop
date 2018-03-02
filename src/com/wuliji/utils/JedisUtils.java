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
 * <p>Description: ����redis���ӳ�</p>
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��11��12�� ����11:16:35
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
		
		//1.����JedisConfigue
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//����������������
		jedisPoolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxdle").toString()));
		//������С����������
		jedisPoolConfig.setMinIdle(Integer.parseInt(pro.get("redis.mindle").toString()));
		//�����ܵ�������
		jedisPoolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxtotal").toString()));
		
		pool = new JedisPool(jedisPoolConfig, pro.get("redis.url").toString(), 
				Integer.parseInt(pro.get("redis.port").toString()));
	}
	
	/**
	 * �������
	 * @return
	 */
	public static Jedis getJedis(){
		return pool.getResource();
	}
}
