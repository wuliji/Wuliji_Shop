/**
 * 
 */
package com.wuliji.service;

import java.sql.SQLException;

import com.wuliji.dao.UserDao;
import com.wuliji.domain.User;

/**
 * <p>Title: UserService</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月17日 下午4:49:30
 */
public class UserService {
	
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	public boolean regist(User user) {
		
		UserDao dao = new UserDao();
		int isRegisterSuccess = 0;
		try {
			isRegisterSuccess = dao.regist(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isRegisterSuccess > 0? true:false;
	}

	/**
	 * 激活码的验证
	 * @param activeCode
	 */
	public void active(String activeCode) {
		UserDao dao = new UserDao();
		try {
			dao.active(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 校验用户名是否存在
	 * @param username
	 */
	public boolean checkUsername(String username) {
		
		UserDao dao = new UserDao();
		Long isExist = null;
		try {
			isExist = dao.checkUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist > 0? true:false;
	}
	
	/**
	 * 登录校验
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public User login(String userName, String passWord) {
		
		UserDao dao = new UserDao();
		User login = null;
		try {
			login = dao.login(userName, passWord);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return login;
	}

}
