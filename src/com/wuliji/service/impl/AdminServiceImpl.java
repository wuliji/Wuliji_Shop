/**
 * 
 */
package com.wuliji.service.impl;

import java.util.List;
import java.util.Map;

import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.Product;

/**
 * <p>Title: AdminServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月11日 下午7:45:24
 */
public interface AdminServiceImpl {
	/**
	 * 查找商品目录
	 * @return
	 */
	public List<Category> findAllCategory();
	
	/**
	 * 添加商品到后台数据
	 * @param product
	 */
	public void saveProduct(Product product);
	
	/**
	 * 后台查询订单数据
	 * @return
	 */
	public List<Order> findAllOrders();
	
	/**
	 * ajax查看订单详细信息
	 * @param oid
	 * @return
	 */
	public List<Map<String, Object>> findOrderInfoByOid(String oid);

}
