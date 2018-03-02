/**
 * 
 */
package com.wuliji.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.wuliji.dao.ProductDao;
import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.OrderItem;
import com.wuliji.domain.PageBean;
import com.wuliji.domain.Product;
import com.wuliji.utils.DataSourceUtils;

/**
 * <p>Title: ProductService</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月21日 下午7:21:48
 */
public class ProductService {
	
	/**
	 * 获得热门商品
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> findHotProductList() {
		
		ProductDao dao = new ProductDao();
		List<Product> hotProductList = null;
		try {
			hotProductList = dao.findHotProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hotProductList;
		
	}
	
	/**
	 * 获得最新商品
	 * @return
	 */
	public List<Product> findNewProductList() {
		ProductDao dao = new ProductDao();
		List<Product> newProductList = null;
		try {
			newProductList = dao.findNewProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newProductList;
	}
	
	/**
	 * 获得商品种列表
	 * @return
	 */
	public List<Category> findAllCategory() {
		ProductDao dao = new ProductDao();
		List<Category> allCategoryList = null;
		try {
			allCategoryList = dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allCategoryList;
	}
	
	/**
	 * 封装商品分类信息
	 * @param cid
	 * @return
	 */
	public PageBean finProductListByCid(String cid, int currentPage, int currentCount) {
		
		ProductDao dao = new ProductDao();
			
		//封装一个PageBean 返回web层
		PageBean pageBean = new PageBean();		
		
		//1.封装当前页
		pageBean.setCurrentPage(currentPage);
		//2.每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3.总商品条数
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		pageBean.setTotalCount(totalCount);
		//4.封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5.当前页显示的数据
		int index = (currentPage - 1)*currentCount;
		List<Product> list = null;
		try {
			list = dao.findProductByPage(cid, index, currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		return pageBean;
	}
	
	/**
	 * 查询点击商品的信息
	 * @param pid
	 * @return
	 */
	public Product finProductByPid(String pid) {
		ProductDao dao = new ProductDao();
		Product product = null;
		try {
			product = dao.findProductBtPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	
	/**
	 * 根据所点商品返回cname的值
	 * @param pid
	 * @return
	 */
	public String findCnameByProduct(String pid) {
		ProductDao dao = new ProductDao();
		String cname = null;
		try {
			cname = dao.findCnameByProduct(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cname;
	}
	
	/**
	 * 提交订单
	 * @param order
	 */
	public void submitOrder(Order order) {
		
		ProductDao dao = new ProductDao();
		
		try {
			//1.开启事务
			DataSourceUtils.startTransaction();
			
			//2.调用dao存储order表数据的方法
			dao.addOrders(order);
			//3.调用dao存储orderitem表数据的方法
			dao.addOrderItem(order.getOrderItems());
			
			
		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 更新确认订单信息
	 * @param order
	 */
	public void updateOrder(Order order) {
		
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrder(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 付款成功后修改订单状态
	 * @param r6_Order
	 */
	public void updateOrderState(String r6_Order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得指定用户的订单集合
	 * @param uid
	 * @return
	 */
	public List<Order> findAllOrders(String uid) {
		ProductDao dao = new ProductDao();
		List<Order> allOrders = null;
		try {
			allOrders = dao.findAllOrders(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allOrders;
	}
	
	/**
	 * 
	 * @param oid
	 * @return
	 */
	public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> allItemList = null;
		try {
			allItemList = dao.findAllOrderItemByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allItemList;
	}
	
	
	
	
}
