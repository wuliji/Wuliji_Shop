/**
 * 
 */
package com.wuliji.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.wuliji.dao.AdminDao;
import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.Product;

/**
 * <p>Title: AdminService</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月10日 下午7:06:46
 */
public class AdminService{
	/**
	 * 查找商品目录
	 * @return
	 */
	public List<Category> findAllCategory() {
		AdminDao dao = new AdminDao();
		List<Category> categoryList = null;
		try {
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoryList;
	}
	
	/**
	 * 添加商品到后台数据
	 * @param product
	 */
	public void saveProduct(Product product) {
		AdminDao dao = new AdminDao();
		try {
			dao.saveProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 后台查询订单数据
	 * @return
	 */
	public List<Order> findAllOrders() {
		AdminDao dao = new AdminDao();
		List<Order> ordersList = null;
		try {
			ordersList = dao.findAllOrders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ordersList;
	}
	
	/**
	 * ajax查看订单详细信息
	 * @param oid
	 * @return
	 */
	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		
		AdminDao dao = new AdminDao();
		List<Map<String, Object>> orderList = null;
		try {
			orderList = dao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}


}
