/**
 * 
 */
package com.wuliji.domain;

/**
 * <p>Title: CartItem</p>
 * <p>Description: </p>
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��11��29�� ����8:04:10
 */
public class CartItem {
	
	private Product product;
	private int buyNum;
	private double subtotal;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public double getSubTotal() {
		return subtotal;
	}
	public void setSubTotal(double subTotal) {
		this.subtotal = subTotal;
	}
	
}
