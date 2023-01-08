package com.xmc;

import com.http.HttpServlet;
import com.service.LoginService;
import com.service.RegService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端要重用的内容
 */
public class ServerContext {

    private static Map<String, HttpServlet> servletMapping = new HashMap<>();
    static {
        initServletMapping();
    }

    private static void initServletMapping(){
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read("./config/servlet.xml");
            Element rootElement = doc.getRootElement();
            List<Element> list = rootElement.elements("servlet");
            for (Element e: list){
                String name = e.attributeValue("name");
                String classPath = e.attributeValue("classpath");
                //通过反射获取对象
                Class aClass = Class.forName(classPath);
                Object o = aClass.newInstance();
                servletMapping.put(name,(HttpServlet) o);
            }
        } catch (DocumentException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet getServlet(String path){
        return servletMapping.get(path);
    }

    public static void main(String[] args) {
        System.out.println(ServerContext.getServlet("/web/login"));
    }
}
