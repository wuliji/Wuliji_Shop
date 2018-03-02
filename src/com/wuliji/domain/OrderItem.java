/**
 * 
 */
package com.wuliji.domain;

/**
 * <p>Title: OrderItem</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月4日 下午7:13:58
 */
public class OrderItem {
	
	private String itemid;//订单项的id
	private int count;//订单项商品的购买数量
	private double subtotal;//订单项的小计
	private Product product;//订单项内部的商品
	private Order order;//该订单项所属的订单
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double sutotal) {
		this.subtotal = sutotal;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
}
