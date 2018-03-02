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
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��12��4�� ����7:16:45
 */
public class Order {
	
	private String oid;//�����ı��
	private String ordertime;//�µ�ʱ��
	private double total;//�ܽ��
	private int state;//������״̬��֧��״̬��
	private String address;//�ջ���ַ
	private String name;//�ջ�������
	private String telephone;//�ջ��˵绰
	private User user;//�ö��������ĸ��û�
	List<OrderItem> orderItems = new ArrayList<OrderItem>();//�ö����еĶ�����
	
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
