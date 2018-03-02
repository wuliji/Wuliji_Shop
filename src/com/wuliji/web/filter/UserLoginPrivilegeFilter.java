/**
 * 
 */
package com.wuliji.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wuliji.domain.User;

/**
 * <p>Title: UserLoginPrivilegeFilter</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月7日 下午7:57:23
 */
public class UserLoginPrivilegeFilter implements Filter{

	@Override
	public void destroy() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		
		//检验用户是否登录
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//没有登录
			rep.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}
		
		chain.doFilter(req, rep);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO 自动生成的方法存根
		
	}

}
