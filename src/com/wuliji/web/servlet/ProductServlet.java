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
	 * 商品类型列表
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		
		//先从缓存中查询categoryList 如果有直接返回，无则从数据库查询之后存到缓存中
		//1.获得jedis对象
		Jedis jedis = JedisUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		//2.判断categoryListJson是否为空
		if(categoryListJson == null){
			System.out.println("缓存没有数据，查询数据库");
			//准备分类数据
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);		
			
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	
	/**
	 * 商城首页
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		
		//准备热门商品 --- List<Product>
		List<Product> hotProductList = service.findHotProductList();		
		request.setAttribute("hotProductList", hotProductList);
		
		//准备最新商品 --- List<Product>
		List<Product> newProductList = service.findNewProductList();
		request.setAttribute("newProductList", newProductList);
		
		//准备分类数据
		List<Category> categoryList = service.findAllCategory();
		request.setAttribute("categoryList", categoryList);
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	/**
	 * 商品的详细信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得点击商品的pid
		String pid = request.getParameter("pid");
		//获得当前页
		String currentPage = request.getParameter("currentPage");
		//获得类别cid
		String cid = request.getParameter("cid");
		ProductService service = new ProductService();
		Product product = service.finProductByPid(pid);
		String cname = service.findCnameByProduct(pid);
		
		request.setAttribute("product", product);
		request.setAttribute("cname", cname);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		
		//获得客户端携带的cookie--获得名字是pids的cookie
		String pids= pid;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					//本次访问商品3 则为 3-2-1
					//本次访问商品为2则为2-3-1
					//将pids拆成一个数组
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					//判断集合中是否存在当前的pid
					if(list.contains(pid)){
						list.remove(pid);
					}
					list.addFirst(pid);
					//将list转成字符串
					StringBuffer sb = new StringBuffer();
					for(int i = 0;i < list.size()&&i < 7; i++){
						sb.append(list.get(i));
						sb.append("-");
					}
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}	
		
		//转发之前创建cookie存储点击商品的pid
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}
	
	/**
	 * 某类商品的页面显示
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获得Cid
		String cid = request.getParameter("cid");
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr == null || currentPageStr.equals("")){
			currentPageStr = "1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;
		//2.查询
		ProductService service = new ProductService();
		PageBean pageBean = service.finProductListByCid(cid, currentPage, currentCount);
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		
		//获得客户端携带名字叫pids的cookie
		Cookie[] cookies = request.getCookies();
		//定义集合记录历史商品信息
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
		
		//将历史记录的集合放到request
		request.setAttribute("historyProductList", historyProductList);
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
	
	/**
	 * 将商品添加到购物车
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获得放到购物车的商品的pid
		String pid = request.getParameter("pid");
		//获得该商品的购买数量
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		
		ProductService service = new ProductService();
		//获取product对象
		Product product = service.finProductByPid(pid);
		//计算该类商品总价
		double subTotal = product.getShop_price() * buyNum;
		
		//封装CartItem
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubTotal(subTotal);
		
		//获得购物车
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart == null){
			cart = new Cart();
		}
		
		//购物项放入车中
		Map<String, CartItem> cartItems = cart.getCartItems();
		if(cartItems.containsKey(pid)){
			CartItem cartItem = cartItems.get(pid);
			int num = cartItem.getBuyNum();
			num += buyNum;
			cartItem.setBuyNum(num);
			//修改小计
			subTotal = num * product.getShop_price();
			
		}else{
			cartItems.put(product.getPid(), item);
		}
		cart.setCartItems(cartItems);
		
		//计算总计
		double total = cart.getTotal() + subTotal;
		cart.setTotal(total);
		
		//将购物车放入session域中
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}
	
	/**
	 * 删除单一商品
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获得要删除的item的pid
		String pid = request.getParameter("pid");
		//删除session中的数据
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart != null){
			Map<String, CartItem> cartItems = cart.getCartItems();
			//修改总价
			cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubTotal());
			cartItems.remove(pid);
		}
		
		session.setAttribute("cart", cart);
		
		//跳转购物车页面
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	
	}
	
	/**
	 * 清空购物车
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取session
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
		
	}
	
	/**
	 * 提交订单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//判断用户是否已经登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//没有登录
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		//创建订单
		Order order = new Order();
		
		//获得购物车信息
		Cart cart = (Cart) session.getAttribute("cart");
		//购物车总金额
		double total = 0 ;
		Map<String, CartItem> cartItems = null;
		if(cart != null){
			total = cart.getTotal();
			cartItems = cart.getCartItems();
			for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
				OrderItem item = new OrderItem();
				item.setItemid(CommonsUtils.getUUID());//项目id
				item.setCount(entry.getValue().getBuyNum());//项目数量
				item.setSubtotal(entry.getValue().getSubTotal());//项目金额
				item.setProduct(entry.getValue().getProduct());
				item.setOrder(order);
				//将item添加到订单项集合中
				order.getOrderItems().add(item);
			}
		}
		
		//封装order对象传递给service层		
		String oid = CommonsUtils.getUUID();//1.订单编号
		order.setOid(oid);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		order.setOrdertime(time.toString());//2.订单时间		
		order.setTotal(total);//3.总金额
		order.setState(0);//4.订单状态
		order.setAddress(null);//5.收货地址
		order.setTelephone(null);//6.收货人电话
		order.setUser(user);//订单属于那个用户
		
		//传递数据到service层
		ProductService service = new ProductService();
		service.submitOrder(order);
		
		//页面跳转
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");
		
	}
	
	/**
	 * 确认订单--更新收货人信息+在线支付
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1.更新收货人信息
		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ProductService service = new ProductService();
		service.updateOrder(order);
		//2.在线支付---银行选择
		//只接入一个集成大部分银行的接口---易宝支付
		// 获得 支付必须基本数据
		String orderid = request.getParameter("oid");
		String money = order.getTotal() + "";//支付金额
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
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

		//重定向到第三方支付平台
		response.sendRedirect(url);
		
		
	}
	
	/**
	 * 查询订单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException {
		
		//判断用户是否已经登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//没有登录
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		//查询该用户所有的订单信息
		ProductService service = new ProductService();
		List<Order> orderList = service.findAllOrders(user.getUid());
		//循环所有的订单信息
		if(orderList != null){
			for(Order order : orderList){
				//获得每一个订单的oid
				String oid = order.getOid();
				//查询该订单的所有的订单项---封装商品订单项的商品信息
				List<Map<String, Object>> mapList = service.findAllOrderItemByOid(oid);
				//将mapList转换成List<OrderItem>
				for(Map<String, Object> map : mapList){
					//从map中取出count subtotal 封装到OrderItem中
					OrderItem item = new OrderItem();
					BeanUtils.populate(item, map);
					//从map中取出pimage pname shop_price 封装到Product中
					Product pro = new Product();
					BeanUtils.populate(pro, map);
					//将product封装到OrderItem
					item.setProduct(pro);
					//将orderItem封装到order中的orderItemList中
					order.getOrderItems().add(item);
				}
			}
		}
		
		//将orderList封装到域中
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("order_list.jsp").forward(request, response);;
		
	}
	
}
