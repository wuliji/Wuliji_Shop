package com.wuliji.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.wuliji.domain.Cart;
import com.wuliji.domain.CartItem;
import com.wuliji.domain.Category;
import com.wuliji.domain.Order;
import com.wuliji.domain.OrderItem;
import com.wuliji.domain.PageBean;
import com.wuliji.domain.Product;
import com.wuliji.domain.User;
import com.wuliji.service.ProductService;
import com.wuliji.utils.CommonsUtils;
import com.wuliji.utils.JedisUtils;
import com.wuliji.utils.PaymentUtil;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	
	/**
	 * ��Ʒ�����б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		
		//�ȴӻ����в�ѯcategoryList �����ֱ�ӷ��أ���������ݿ��ѯ֮��浽������
		//1.���jedis����
		Jedis jedis = JedisUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		//2.�ж�categoryListJson�Ƿ�Ϊ��
		if(categoryListJson == null){
			System.out.println("����û�����ݣ���ѯ���ݿ�");
			//׼����������
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);		
			
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	
	/**
	 * �̳���ҳ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		
		//׼��������Ʒ --- List<Product>
		List<Product> hotProductList = service.findHotProductList();		
		request.setAttribute("hotProductList", hotProductList);
		
		//׼��������Ʒ --- List<Product>
		List<Product> newProductList = service.findNewProductList();
		request.setAttribute("newProductList", newProductList);
		
		//׼����������
		List<Category> categoryList = service.findAllCategory();
		request.setAttribute("categoryList", categoryList);
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	/**
	 * ��Ʒ����ϸ��Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��õ����Ʒ��pid
		String pid = request.getParameter("pid");
		//��õ�ǰҳ
		String currentPage = request.getParameter("currentPage");
		//������cid
		String cid = request.getParameter("cid");
		ProductService service = new ProductService();
		Product product = service.finProductByPid(pid);
		String cname = service.findCnameByProduct(pid);
		
		request.setAttribute("product", product);
		request.setAttribute("cname", cname);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		
		//��ÿͻ���Я����cookie--���������pids��cookie
		String pids= pid;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					//���η�����Ʒ3 ��Ϊ 3-2-1
					//���η�����ƷΪ2��Ϊ2-3-1
					//��pids���һ������
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					//�жϼ������Ƿ���ڵ�ǰ��pid
					if(list.contains(pid)){
						list.remove(pid);
					}
					list.addFirst(pid);
					//��listת���ַ���
					StringBuffer sb = new StringBuffer();
					for(int i = 0;i < list.size()&&i < 7; i++){
						sb.append(list.get(i));
						sb.append("-");
					}
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}	
		
		//ת��֮ǰ����cookie�洢�����Ʒ��pid
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}
	
	/**
	 * ĳ����Ʒ��ҳ����ʾ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.���Cid
		String cid = request.getParameter("cid");
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr == null || currentPageStr.equals("")){
			currentPageStr = "1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;
		//2.��ѯ
		ProductService service = new ProductService();
		PageBean pageBean = service.finProductListByCid(cid, currentPage, currentCount);
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		
		//��ÿͻ���Я�����ֽ�pids��cookie
		Cookie[] cookies = request.getCookies();
		//���弯�ϼ�¼��ʷ��Ʒ��Ϣ
		List<Product> historyProductList = new ArrayList<Product>();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for(String pid : split){
						Product pro = service.finProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}
		
		//����ʷ��¼�ļ��Ϸŵ�request
		request.setAttribute("historyProductList", historyProductList);
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
	
	/**
	 * ����Ʒ��ӵ����ﳵ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��÷ŵ����ﳵ����Ʒ��pid
		String pid = request.getParameter("pid");
		//��ø���Ʒ�Ĺ�������
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		
		ProductService service = new ProductService();
		//��ȡproduct����
		Product product = service.finProductByPid(pid);
		//���������Ʒ�ܼ�
		double subTotal = product.getShop_price() * buyNum;
		
		//��װCartItem
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubTotal(subTotal);
		
		//��ù��ﳵ
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart == null){
			cart = new Cart();
		}
		
		//��������복��
		Map<String, CartItem> cartItems = cart.getCartItems();
		if(cartItems.containsKey(pid)){
			CartItem cartItem = cartItems.get(pid);
			int num = cartItem.getBuyNum();
			num += buyNum;
			cartItem.setBuyNum(num);
			//�޸�С��
			subTotal = num * product.getShop_price();
			
		}else{
			cartItems.put(product.getPid(), item);
		}
		cart.setCartItems(cartItems);
		
		//�����ܼ�
		double total = cart.getTotal() + subTotal;
		cart.setTotal(total);
		
		//�����ﳵ����session����
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}
	
	/**
	 * ɾ����һ��Ʒ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//���Ҫɾ����item��pid
		String pid = request.getParameter("pid");
		//ɾ��session�е�����
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart != null){
			Map<String, CartItem> cartItems = cart.getCartItems();
			//�޸��ܼ�
			cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubTotal());
			cartItems.remove(pid);
		}
		
		session.setAttribute("cart", cart);
		
		//��ת���ﳵҳ��
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	
	}
	
	/**
	 * ��չ��ﳵ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��ȡsession
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
		
	}
	
	/**
	 * �ύ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//�ж��û��Ƿ��Ѿ���¼
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//û�е�¼
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		//��������
		Order order = new Order();
		
		//��ù��ﳵ��Ϣ
		Cart cart = (Cart) session.getAttribute("cart");
		//���ﳵ�ܽ��
		double total = 0 ;
		Map<String, CartItem> cartItems = null;
		if(cart != null){
			total = cart.getTotal();
			cartItems = cart.getCartItems();
			for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
				OrderItem item = new OrderItem();
				item.setItemid(CommonsUtils.getUUID());//��Ŀid
				item.setCount(entry.getValue().getBuyNum());//��Ŀ����
				item.setSubtotal(entry.getValue().getSubTotal());//��Ŀ���
				item.setProduct(entry.getValue().getProduct());
				item.setOrder(order);
				//��item��ӵ����������
				order.getOrderItems().add(item);
			}
		}
		
		//��װorder���󴫵ݸ�service��		
		String oid = CommonsUtils.getUUID();//1.�������
		order.setOid(oid);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		order.setOrdertime(time.toString());//2.����ʱ��		
		order.setTotal(total);//3.�ܽ��
		order.setState(0);//4.����״̬
		order.setAddress(null);//5.�ջ���ַ
		order.setTelephone(null);//6.�ջ��˵绰
		order.setUser(user);//���������Ǹ��û�
		
		//�������ݵ�service��
		ProductService service = new ProductService();
		service.submitOrder(order);
		
		//ҳ����ת
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");
		
	}
	
	/**
	 * ȷ�϶���--�����ջ�����Ϣ+����֧��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1.�����ջ�����Ϣ
		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ProductService service = new ProductService();
		service.updateOrder(order);
		//2.����֧��---����ѡ��
		//ֻ����һ�����ɴ󲿷����еĽӿ�---�ױ�֧��
		// ��� ֧�������������
		String orderid = request.getParameter("oid");
		String money = order.getTotal() + "";//֧�����
		// ����
		String pd_FrpId = request.getParameter("pd_FrpId");

		// ����֧����˾��Ҫ��Щ����
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// ֧���ɹ��ص���ַ ---- ������֧����˾����ʡ��û�����
		// ������֧�����Է�����ַ
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// ����hmac ��Ҫ��Կ
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		
		
		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
						"&p0_Cmd="+p0_Cmd+
						"&p1_MerId="+p1_MerId+
						"&p2_Order="+p2_Order+
						"&p3_Amt="+p3_Amt+
						"&p4_Cur="+p4_Cur+
						"&p5_Pid="+p5_Pid+
						"&p6_Pcat="+p6_Pcat+
						"&p7_Pdesc="+p7_Pdesc+
						"&p8_Url="+p8_Url+
						"&p9_SAF="+p9_SAF+
						"&pa_MP="+pa_MP+
						"&pr_NeedResponse="+pr_NeedResponse+
						"&hmac="+hmac;

		//�ض��򵽵�����֧��ƽ̨
		response.sendRedirect(url);
		
		
	}
	
	/**
	 * ��ѯ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException {
		
		//�ж��û��Ƿ��Ѿ���¼
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//û�е�¼
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		//��ѯ���û����еĶ�����Ϣ
		ProductService service = new ProductService();
		List<Order> orderList = service.findAllOrders(user.getUid());
		//ѭ�����еĶ�����Ϣ
		if(orderList != null){
			for(Order order : orderList){
				//���ÿһ��������oid
				String oid = order.getOid();
				//��ѯ�ö��������еĶ�����---��װ��Ʒ���������Ʒ��Ϣ
				List<Map<String, Object>> mapList = service.findAllOrderItemByOid(oid);
				//��mapListת����List<OrderItem>
				for(Map<String, Object> map : mapList){
					//��map��ȡ��count subtotal ��װ��OrderItem��
					OrderItem item = new OrderItem();
					BeanUtils.populate(item, map);
					//��map��ȡ��pimage pname shop_price ��װ��Product��
					Product pro = new Product();
					BeanUtils.populate(pro, map);
					//��product��װ��OrderItem
					item.setProduct(pro);
					//��orderItem��װ��order�е�orderItemList��
					order.getOrderItems().add(item);
				}
			}
		}
		
		//��orderList��װ������
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("order_list.jsp").forward(request, response);;
		
	}
	
}
