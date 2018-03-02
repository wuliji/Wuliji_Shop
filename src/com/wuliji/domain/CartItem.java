/**
 * 
 */
package com.wuliji.domain;

/**
 * <p>Title: CartItem</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年11月29日 下午8:04:10
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
