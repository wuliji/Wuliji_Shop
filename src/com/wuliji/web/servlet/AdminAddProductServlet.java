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
		
		//�ռ��������ݲ���װһ��Productʵ�� ���ϴ�ͼƬ���������������
		
		Product product = new Product();
		//�ռ����ݵ�����
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//���������ļ����
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//�����ļ��ϴ����Ķ���
			ServletFileUpload upload = new ServletFileUpload(factory);
			//����request����ļ���Զ��󼯺�
			List<FileItem> parseRequest = upload.parseRequest(request);
			for(FileItem item : parseRequest){
				//�ж��Ƿ�����ͨ����
				boolean formField = item.isFormField();
				if(formField){
					//��ͨ���� ��ñ������� ��װ��Productʵ����
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
					
				}else{
					//�ļ��ϴ��� ����ļ����� ����ļ�����
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
			//��product�����װδ��װ������
			product.setPid(CommonsUtils.getUUID());
			product.setPflag(0);
			product.setPdate(new Date(System.currentTimeMillis()));
			Category category = new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			
			//��product���ݸ�service��
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
