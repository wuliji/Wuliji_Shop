/**
 * 
 */
package com.wuliji.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: Cart</p>
 * <p>Description: </p>
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��11��29�� ����8:05:18
 */
public class Cart {
	
	/**
	 * ���ﳵ�洢�Ĺ������Ϊ��Ʒ��id
	 */
	private Map<String, CartItem> cartItems = new HashMap<String, CartItem>();
	
	/**
	 * ���ﳵ��Ʒ���ܼƷ���
	 */
	private double total;

	public Map<String, CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<String, CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
