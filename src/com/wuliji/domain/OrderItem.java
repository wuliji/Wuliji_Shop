/**
 * 
 */
package com.wuliji.domain;

/**
 * <p>Title: OrderItem</p>
 * <p>Description: </p>
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��12��4�� ����7:13:58
 */
public class OrderItem {
	
	private String itemid;//�������id
	private int count;//��������Ʒ�Ĺ�������
	private double subtotal;//�������С��
	private Product product;//�������ڲ�����Ʒ
	private Order order;//�ö����������Ķ���
	
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
