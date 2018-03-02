/**
 * 
 */
package com.wuliji.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: Order</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月4日 下午7:16:45
 */
public class Order {
	
	private String oid;//订单的编号
	private String ordertime;//下单时间
	private double total;//总金额
	private int state;//订单的状态（支付状态）
	private String address;//收货地址
	private String name;//收货人姓名
	private String telephone;//收货人电话
	private User user;//该订单属于哪个用户
	List<OrderItem> orderItems = new ArrayList<OrderItem>();//该订单中的订单项
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String addr) {
		this.address = addr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
