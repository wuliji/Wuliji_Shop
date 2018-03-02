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
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��12��11�� ����7:45:24
 */
public interface AdminServiceImpl {
	/**
	 * ������ƷĿ¼
	 * @return
	 */
	public List<Category> findAllCategory();
	
	/**
	 * �����Ʒ����̨����
	 * @param product
	 */
	public void saveProduct(Product product);
	
	/**
	 * ��̨��ѯ��������
	 * @return
	 */
	public List<Order> findAllOrders();
	
	/**
	 * ajax�鿴������ϸ��Ϣ
	 * @param oid
	 * @return
	 */
	public List<Map<String, Object>> findOrderInfoByOid(String oid);

}
