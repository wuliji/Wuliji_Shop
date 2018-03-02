package com.wuliji.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.wuliji.domain.User;
import com.wuliji.service.UserService;
import com.wuliji.utils.CommonsUtils;
import com.wuliji.utils.MailUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	
	/**
	 * 状态码的激活
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void activeCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得激活码
		String activeCode = request.getParameter("activeCode");
		
		UserService service = new UserService();
		service.active(activeCode);
		
		//跳转到登录页面 重定向
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
	
	/**
	 * 验证码校验
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取session中的验证码
		String checkCode_session = (String) request.getSession().getAttribute("checkcode_session");
		String json = "{\"checkCode_session\":"+checkCode_session+"}";
		//写入到json中
		response.getWriter().write(json);
	}
	
	/**
	 * 用户名重复校验
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得用户名
		String username = request.getParameter("username");
		
		UserService service = new UserService();
		boolean isExist = service.checkUsername(username);
		//将username写入到json数据中
		String json = "{\"isExist\":"+isExist+"}";
		response.getWriter().write(json);
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		//1.获得用户名和密码
		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		//2.调用一个业务操作实现检验
		UserService service = new UserService();
		User user = service.login(userName, passWord);
		//3.通过User是否为null判断用户名和密码是否正确
		if(user != null){
			//检验通过，重定向到网站的首页
			request.getSession().setAttribute("user", user);
			//***************判断用户是否勾选了自动登录*****************
			String autoLogin = request.getParameter("autoLogin");
			System.out.println(autoLogin);
			if("autoLogin".equals(autoLogin)){
				//要自动登录
				//创建存储用户名的cookie
				Cookie cookie_username = new Cookie("cookie_username",user.getUsername());
				cookie_username.setMaxAge(10*60);
				//创建存储密码的cookie
				Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
				cookie_password.setMaxAge(10*60);
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			response.sendRedirect(request.getContextPath() + "/product?method=index");
		}else{
			//用户名或密码错误，调回当前login.jsp，提示错误信息，使用转发实现
			request.setAttribute("loginInfo", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);		
		}
	}
	
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//解决中文编码
		request.setCharacterEncoding("UTF-8");	
		
		//1.验证码校验
		//获得面输入的验证码
		String checkCode = request.getParameter("checkCode");
		//获得生成图片的文字
		String checkCode_session = (String) request.getSession().getAttribute("checkcode_session");
		//进行文字校验
		if(!checkCode_session.equals(checkCode)){
			request.setAttribute("validateInfo", "验证码错误");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return ;
		}
		
		//2.获得表单数据进行封装
		Map<String, String[]> parameterMap = request.getParameterMap();
		User user = new User();
		try {
			//指定类型转换器（将String转成Date）
			ConvertUtils.register(new Converter() {
				
				@Override
				public Object convert(Class clazz, Object value) {
					//String转成 Date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = null;
					try {
						date = format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return date;
				}
			}, Date.class);
			//映射封装
			BeanUtils.populate(user, parameterMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//private String uid;
		user.setUid(CommonsUtils.getUUID());
		//private String telephone;
		user.setTelephone(null);
		//private int state;
		user.setState(0);
		//private String code;
		String activeCode = CommonsUtils.getUUID();
		user.setCode(activeCode);
		
		//将user传递给service层
		UserService service = new UserService();
		boolean isRegisterSuccess = service.regist(user);
		
		//是否注册成功
		if(isRegisterSuccess){
			//发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击下面的链接进行激活账户<a href='http://localhost:8080/WulijiShop/user?method=activeCode&activeCode="+ activeCode +"'>"
					+ "http://localhost:8080/WulijiShop/user?method=activeCode&activeCode="+ activeCode+ "</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			//跳转到成功页面
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		}else{
			//跳转到失败页面
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}
	
	/**
	 * 用户退出
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//删除session域中的user值
		request.getSession().removeAttribute("user");
		//将存储在客户端的cookie删除掉
		Cookie cookie_username = new Cookie("cookie_username","");
		cookie_username.setMaxAge(0);
		//创建存储密码的cookie
		Cookie cookie_password = new Cookie("cookie_password","");
		cookie_password.setMaxAge(0);

		response.addCookie(cookie_username);
		response.addCookie(cookie_password);
		response.sendRedirect(request.getContextPath() + "/product?method=index");
	}

}
