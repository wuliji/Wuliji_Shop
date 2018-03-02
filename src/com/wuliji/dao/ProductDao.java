/**
 * 
 */
package com.wuliji.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.OrderItem;
import com.wuliji.domain.Product;
import com.wuliji.utils.DataSourceUtils;

/**
 * <p>Title: ProductDao</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月21日 下午7:23:58
 */
public class ProductDao {
	
	/**
	 * 获得热门商品
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> findHotProductList() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot = ? limit ?,?";
		List<Product> query = runner.query(sql, new BeanListHandler<Product>(Product.class), 1, 0, 9);	
		return query;
	}
	
	/**
	 * 获得最新商品
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> findNewProductList() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		List<Product> query = runner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);	
		return query;
	}
	
	/**
	 * 获得商品种类列表
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
	 * 查询商品总条数
	 * @return
	 * @throws SQLException 
	 */
	public int getCount(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long query = (Long) runner.query(sql, new ScalarHandler(), cid);
		return query.intValue();
	}

	/**
	 * 查询当前页的商品
	 * @param cid
	 * @param index
	 * @param currentCount
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> findProductByPage(String cid, int index,
			int currentCount) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> list = runner.query(sql, new BeanListHandler<Product>(Product.class), cid, index, currentCount);
		return list;
	}

	/**
	 * 查询点击商品信息
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public Product findProductBtPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		Product query = runner.query(sql, new BeanHandler<Product>(Product.class), pid);
		return query;
	}
	
	/**
	 * 根据所点商品查询该商品的cname
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public String findCnameByProduct(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select category.cname from product,category where product.cid=category.cid and pid=?";
		Object[] query = runner.query(sql, new ArrayHandler(), pid);
		return query[0].toString();
	}
	
	/**
	 * 订单提交---向order表插入数据
	 * @param order
	 * @throws SQLException 
	 */
	public void addOrders(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		runner.update(conn, sql, order.getOid(), order.getOrdertime(), order.getTotal(),
				order.getState(), order.getAddress(), order.getName(), order.getTelephone(),
				order.getUser().getUid());
	}
	
	/**
	 * 订单提交---向orderItems插入数据
	 * @param orderItems
	 * @throws SQLException 
	 */
	public void addOrderItem(List<OrderItem> orderItems) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orderitem values (?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		for(OrderItem item : orderItems){
			runner.update(conn, sql, item.getItemid(), item.getCount(), item.getSubtotal(), 
					item.getProduct().getPid(), item.getOrder().getOid());
		}
		
	}
	
	/**
	 * 确认订单
	 * @param order
	 * @throws SQLException 
	 */
	public void updateOrder(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address=?,name=?,telephone=? where oid=?";
		runner.update(sql, order.getAddress(), order.getName(), order.getTelephone(), order.getOid());
		
	}
	
	/**
	 * 付款成功状态修改
	 * @param r6_Order
	 * @throws SQLException 
	 */
	public void updateOrderState(String r6_Order) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set state=? where oid=?";
		runner.update(sql, 1, r6_Order);
	}

	/**
	 * 查询用户的订单
	 * @param uid
	 * @return
	 * @throws SQLException 
	 */
	public List<Order> findAllOrders(String uid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid=?";
		List<Order> query = runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
		return query;
	}

	/**
	 * 
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderItem i,product p where i.pid=p.pid and i.oid=?";
		List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
		return mapList;
	}
	
	
}
