/**
 * 
 */
package com.wuliji.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

/**
 * <p>Title: BeanFactory</p>
 * <p>Description: </p>
 * <p>Company: 乌力吉</p> 
 * @author 乌力吉
 * @date 2017年12月11日 下午7:53:47
 */
public class BeanFactory {
	
	/**
	 * 返回一个对象
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		
		//生产对象---根据清单---配置文件---每一个bean对象的生产的细节配到配置文件
		//使用dom4j解析xml文件
		
		try {
			//1.创建解析器
			SAXReader reader = new SAXReader();
			//2.解析文档---src下的bean.xml
			String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
			Document doc = reader.read(path);
			//3.获得元素
			Element element = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			String className = element.attributeValue("class");
			//4.利用反射创建对象
			Class clazz = Class.forName(className);
			Object newInstance = clazz.newInstance();
			return newInstance;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
