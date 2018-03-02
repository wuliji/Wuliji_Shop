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
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��11��21�� ����7:21:48
 */
public class ProductService {
	
	/**
	 * ���������Ʒ
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
	 * ���������Ʒ
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
	 * �����Ʒ���б�
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
	 * ��װ��Ʒ������Ϣ
	 * @param cid
	 * @return
	 */
	public PageBean finProductListByCid(String cid, int currentPage, int currentCount) {
		
		ProductDao dao = new ProductDao();
			
		//��װһ��PageBean ����web��
		PageBean pageBean = new PageBean();		
		
		//1.��װ��ǰҳ
		pageBean.setCurrentPage(currentPage);
		//2.ÿҳ��ʾ������
		pageBean.setCurrentCount(currentCount);
		//3.����Ʒ����
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		pageBean.setTotalCount(totalCount);
		//4.��װ��ҳ��
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5.��ǰҳ��ʾ������
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
	 * ��ѯ�����Ʒ����Ϣ
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
	 * ����������Ʒ����cname��ֵ
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
	 * �ύ����
	 * @param order
	 */
	public void submitOrder(Order order) {
		
		ProductDao dao = new ProductDao();
		
		try {
			//1.��������
			DataSourceUtils.startTransaction();
			
			//2.����dao�洢order�����ݵķ���
			dao.addOrders(order);
			//3.����dao�洢orderitem�����ݵķ���
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
	 * ����ȷ�϶�����Ϣ
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
	 * ����ɹ����޸Ķ���״̬
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
	 * ���ָ���û��Ķ�������
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
