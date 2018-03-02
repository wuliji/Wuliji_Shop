/**
 * 
 */
package com.wuliji.utils;

import java.util.UUID;

/**
 * <p>Title: CommonsUtils</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月17日 下午4:44:54
 */
public class CommonsUtils {
	
	/**
	 * 生成UUID方法
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
}
