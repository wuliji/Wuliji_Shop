package com.wuliji.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
@SuppressWarnings("all")
public class BaseServlet extends HttpServlet {
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		try {
			//1.��������method������
			String methodName = req.getParameter("method");
			//2.��õ�ǰ�����ʵĶ�����ֽ������
			Class clazz = this.getClass();
			//3.��õ�ǰ�ֽ�������е�ָ���ķ���
			Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			//4.ִ����Ӧ�Ĺ��ܷ���
			method.invoke(this, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
