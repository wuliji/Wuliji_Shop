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
 * <p>Company: ������</p> 
 * @author ������
 * @date 2017��12��11�� ����7:53:47
 */
public class BeanFactory {
	
	/**
	 * ����һ������
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		
		//��������---�����嵥---�����ļ�---ÿһ��bean�����������ϸ���䵽�����ļ�
		//ʹ��dom4j����xml�ļ�
		
		try {
			//1.����������
			SAXReader reader = new SAXReader();
			//2.�����ĵ�---src�µ�bean.xml
			String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
			Document doc = reader.read(path);
			//3.���Ԫ��
			Element element = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			String className = element.attributeValue("class");
			//4.���÷��䴴������
			Class clazz = Class.forName(className);
			Object newInstance = clazz.newInstance();
			return newInstance;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
