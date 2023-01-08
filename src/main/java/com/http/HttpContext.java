package com.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpContext {

    private static Map<String ,String> mimeMapping = new HashMap<>();

    static {

        initMimeMapping();

    }

    private static void initMimeMapping(){

//        mimeMapping.put("html","text/html");
//        mimeMapping.put("css","text/css");
//        mimeMapping.put("js","application/javascript");
//        mimeMapping.put("png","image/png");
//        mimeMapping.put("jpg","image/jpg");
//        mimeMapping.put("gif","image/gif");
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read("./config/web.xml");
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements("mime-mapping");
            for (Element e: elements) {
                String key = e.elementText("extension");
                String value = e.elementText("mime-type");
                mimeMapping.put(key, value);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        String mp3 = getMimeType("mp3");
        System.out.println(mp3);

    }
}
