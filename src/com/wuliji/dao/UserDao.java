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
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��11��17�� ����4:51:07
 */
public class UserDao {
	
	/**
	 * ע��
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
	 * ����
	 * @param activeCode
	 * @throws SQLException 
	 */
	public void active(String activeCode) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update user set state=? where code=?";
		runner.update(sql, 1, activeCode);
	}
	
	/**
	 * У���û����Ƿ����
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
	 * ��¼
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
