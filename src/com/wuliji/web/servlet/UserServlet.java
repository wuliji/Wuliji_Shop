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
	 * ״̬��ļ���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void activeCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ü�����
		String activeCode = request.getParameter("activeCode");
		
		UserService service = new UserService();
		service.active(activeCode);
		
		//��ת����¼ҳ�� �ض���
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
	
	/**
	 * ��֤��У��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡsession�е���֤��
		String checkCode_session = (String) request.getSession().getAttribute("checkcode_session");
		String json = "{\"checkCode_session\":"+checkCode_session+"}";
		//д�뵽json��
		response.getWriter().write(json);
	}
	
	/**
	 * �û����ظ�У��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//����û���
		String username = request.getParameter("username");
		
		UserService service = new UserService();
		boolean isExist = service.checkUsername(username);
		//��usernameд�뵽json������
		String json = "{\"isExist\":"+isExist+"}";
		response.getWriter().write(json);
	}
	
	/**
	 * ��¼
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		//1.����û���������
		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		//2.����һ��ҵ�����ʵ�ּ���
		UserService service = new UserService();
		User user = service.login(userName, passWord);
		//3.ͨ��User�Ƿ�Ϊnull�ж��û����������Ƿ���ȷ
		if(user != null){
			//����ͨ�����ض�����վ����ҳ
			request.getSession().setAttribute("user", user);
			//***************�ж��û��Ƿ�ѡ���Զ���¼*****************
			String autoLogin = request.getParameter("autoLogin");
			System.out.println(autoLogin);
			if("autoLogin".equals(autoLogin)){
				//Ҫ�Զ���¼
				//�����洢�û�����cookie
				Cookie cookie_username = new Cookie("cookie_username",user.getUsername());
				cookie_username.setMaxAge(10*60);
				//�����洢�����cookie
				Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
				cookie_password.setMaxAge(10*60);
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			response.sendRedirect(request.getContextPath() + "/product?method=index");
		}else{
			//�û�����������󣬵��ص�ǰlogin.jsp����ʾ������Ϣ��ʹ��ת��ʵ��
			request.setAttribute("loginInfo", "�û������������");
			request.getRequestDispatcher("/login.jsp").forward(request, response);		
		}
	}
	
	/**
	 * ע��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//������ı���
		request.setCharacterEncoding("UTF-8");	
		
		//1.��֤��У��
		//������������֤��
		String checkCode = request.getParameter("checkCode");
		//�������ͼƬ������
		String checkCode_session = (String) request.getSession().getAttribute("checkcode_session");
		//��������У��
		if(!checkCode_session.equals(checkCode)){
			request.setAttribute("validateInfo", "��֤�����");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return ;
		}
		
		//2.��ñ����ݽ��з�װ
		Map<String, String[]> parameterMap = request.getParameterMap();
		User user = new User();
		try {
			//ָ������ת��������Stringת��Date��
			ConvertUtils.register(new Converter() {
				
				@Override
				public Object convert(Class clazz, Object value) {
					//Stringת�� Date
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
			//ӳ���װ
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
		
		//��user���ݸ�service��
		UserService service = new UserService();
		boolean isRegisterSuccess = service.regist(user);
		
		//�Ƿ�ע��ɹ�
		if(isRegisterSuccess){
			//���ͼ����ʼ�
			String emailMsg = "��ϲ��ע��ɹ���������������ӽ��м����˻�<a href='http://localhost:8080/WulijiShop/user?method=activeCode&activeCode="+ activeCode +"'>"
					+ "http://localhost:8080/WulijiShop/user?method=activeCode&activeCode="+ activeCode+ "</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			//��ת���ɹ�ҳ��
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		}else{
			//��ת��ʧ��ҳ��
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}
	
	/**
	 * �û��˳�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ɾ��session���е�userֵ
		request.getSession().removeAttribute("user");
		//���洢�ڿͻ��˵�cookieɾ����
		Cookie cookie_username = new Cookie("cookie_username","");
		cookie_username.setMaxAge(0);
		//�����洢�����cookie
		Cookie cookie_password = new Cookie("cookie_password","");
		cookie_password.setMaxAge(0);

		response.addCookie(cookie_username);
		response.addCookie(cookie_password);
		response.sendRedirect(request.getContextPath() + "/product?method=index");
	}

}
