package com.wuliji.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.service.AdminService;
import com.wuliji.service.impl.AdminServiceImpl;
import com.wuliji.utils.BeanFactory;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	
	/**
	 * 查找目录动态加载
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//提供一个List<Category> 转成json字符串
		AdminService service = new AdminService();
		List<Category> categoryList = service.findAllCategory();
		
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(json);
	}
	
	/**
	 * 后台获得订单数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得所有订单信息---List<Order>
		AdminService service = new AdminService();
		List<Order> orderList = service.findAllOrders();
		
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
		
	}
	
	/**
	 * ajax获得订单详细信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//模拟延时
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//获得oid
		String oid = request.getParameter("oid");
		
		AdminService service = new AdminService();
		List<Map<String, Object>> map = service.findOrderInfoByOid(oid);
		
		Gson gson = new Gson();
		String json = gson.toJson(map);
		response.setContentType("text/html;charset=utf-8");
		
		
		response.getWriter().write(json);
	}
	

}
