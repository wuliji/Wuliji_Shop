/**
 * 
 */
package com.wuliji.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.Product;
import com.wuliji.utils.DataSourceUtils;

/**
 * <p>Title: AdminDao</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月10日 下午7:08:01
 */
public class AdminDao {
	
	/**
	 * 动态显示商品目录
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		List<Category> query = runner.query(sql, new BeanListHandler<Category>(Category.class));
		return query;
	}
	
	/**
	 * 添加商品到后台
	 * @param product
	 * @throws SQLException 
	 */
	public void saveProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		runner.update(sql, product.getPid(), product.getPname(), product.getMarket_price(),
				product.getShop_price(), product.getPimage(), product.getPdate(), product.getIs_hot(),
				product.getPdesc(), product.getPflag(), product.getCategory().getCid());
		
	}
	
	/**
	 * 后台查询订单数据
	 * @return
	 * @throws SQLException 
	 */
	public List<Order> findAllOrders() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders";
		List<Order> query = runner.query(sql, new BeanListHandler<Order>(Order.class));
		return query;
	}
	
	/**
	 * ajax查询订单详细信息
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal "
				+ "from orderitem i,product p where i.pid=p.pid and i.oid=?";
		List<Map<String, Object>> query = runner.query(sql, new MapListHandler(), oid);
		return query;
	}
	
}
