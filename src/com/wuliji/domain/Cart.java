/**
 * 
 */
package com.wuliji.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: Cart</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月29日 下午8:05:18
 */
public class Cart {
	
	/**
	 * 购物车存储的购物项，键为商品的id
	 */
	private Map<String, CartItem> cartItems = new HashMap<String, CartItem>();
	
	/**
	 * 购物车商品的总计费用
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
