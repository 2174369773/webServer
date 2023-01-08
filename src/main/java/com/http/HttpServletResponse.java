package com.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 谢孟呈
 */
public class HttpServletResponse {

    private Socket socket;

    //状态行相关信息
    private int statusCode = 200; //状态代码

    private String statusReason = "OK"; //状态描述

    private File entity;

    //响应头相关信息
    private Map<String, String > headers = new HashMap<>();

    public HttpServletResponse(Socket socket){
        this.socket = socket;

    }

    public void response() throws IOException {

        //发送状态行
        sendStatusLine();

        //发送响应头
        sendHeaders();

        //发送响应正文
        sendContent();

    }

    private void sendStatusLine() throws IOException {
        //1.发送状态行
        String line = "HTTP/1.1 200 OK";
        println(line);
    }

    private void sendHeaders() throws IOException {
//        //2.发送响应头
//        String line = "Content-Type: text/html";
//        println(line);
//        line = "Content-Length: "+entity.length();
//        println(line);
//        println("");//单独发送回车换行，表示传输结束

        //遍历所有header,发送给浏览器
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> e : entries){
            String line = e.getKey()+": "+e.getValue();
            println(line);
        }
        println("");
    }

    private void sendContent() throws IOException {
        if (entity!= null) {
            OutputStream out = socket.getOutputStream();
            //3.发送响应正文
            FileInputStream fis = new FileInputStream(entity);
            int len;
            byte[] data = new byte[1024 * 10];
            while ((len = fis.read(data)) != -1) {
                out.write(data, 0, len);
            }
        }
    }



    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13); //发送回车符
        out.write(10); //发送换行符

    }


    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setEntity(File entity) {
        this.entity = entity;
        String name = entity.getName();
        String ext = name.substring(name.lastIndexOf(".")+1);
       setHeader("Content-Type", HttpContext.getMimeType(ext));
       setHeader("Content-Length",entity.length()+"");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public File getEntity() {
        return entity;
    }

    public void setHeader(String name, String value){
        headers.put(name,value);
    }
}
