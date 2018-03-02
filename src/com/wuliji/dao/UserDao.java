/**
 * 
 */
package com.wuliji.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.wuliji.domain.User;
import com.wuliji.utils.DataSourceUtils;

/**
 * <p>Title: UserDao</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月17日 下午4:51:07
 */
public class UserDao {
	
	/**
	 * 注册
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public int regist(User user) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		int update = runner.update(sql, user.getUid(),user.getUsername(),user.getPassword(),
				user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),
				user.getSex(),user.getState(),user.getCode());
		return update;
	}
	
	/**
	 * 激活
	 * @param activeCode
	 * @throws SQLException 
	 */
	public void active(String activeCode) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update user set state=? where code=?";
		runner.update(sql, 1, activeCode);
	}
	
	/**
	 * 校验用户名是否存在
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	public Long checkUsername(String username) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from user where username=?";
		Long query = (Long) runner.query(sql, new ScalarHandler(), username);
		return query;
	}
	
	/**
	 * 登录
	 * @param userName
	 * @param passWord
	 * @return
	 * @throws SQLException 
	 */
	public User login(String userName, String passWord) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user where username=? and password=?";
		User user = runner.query(sql, new BeanHandler<User>(User.class), userName, passWord);
		return user;
	}

}
