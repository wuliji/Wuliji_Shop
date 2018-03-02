package com.wuliji.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.wuliji.domain.Category;
import com.wuliji.domain.Product;
import com.wuliji.service.AdminService;
import com.wuliji.utils.CommonsUtils;

/**
 * Servlet implementation class AdminAddProductServlet
 */
public class AdminAddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAddProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//收集表单的数据并封装一个Product实体 将上传图片存贷服务器磁盘上
		
		Product product = new Product();
		//收集数据的容器
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//创建磁盘文件项工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//创建文件上传核心对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			//解析request获得文件项对对象集合
			List<FileItem> parseRequest = upload.parseRequest(request);
			for(FileItem item : parseRequest){
				//判断是否是普通表单项
				boolean formField = item.isFormField();
				if(formField){
					//普通表单项 获得表单的数据 封装到Product实体中
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
					
				}else{
					//文件上传项 获得文件名称 获得文件内容
					String fileName = item.getName();
					String path = this.getServletContext().getRealPath("upload");
					InputStream is = item.getInputStream();
					OutputStream os = new FileOutputStream(path + "/" + fileName);
					IOUtils.copy(is, os);
					is.close();
					os.close();
					item.delete();
					
					map.put("pimage", "upload/" + fileName);
				}
			}
			BeanUtils.populate(product, map);
			//对product对象封装未封装的数据
			product.setPid(CommonsUtils.getUUID());
			product.setPflag(0);
			product.setPdate(new Date(System.currentTimeMillis()));
			Category category = new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			
			//将product传递给service层
			AdminService service = new AdminService();
			service.saveProduct(product);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
