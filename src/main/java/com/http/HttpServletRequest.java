package com.http;

import com.xmc.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {

    private Socket socket;
    private String method; //保存请求方式
    private String uri; //请求路径
    private String protocol; //协议

    private String requestURI; //uri左侧部分，"？"左侧部分
    private String queryString; //"?"右侧部分

    private Map<String,String> parameters = new HashMap<>(); //存每一组参数


    Map<String, String > header = new HashMap<>();

    private byte[] contentData;

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;

        //解析请求行
        parseRequestLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();

    }

    private void parseRequestLine() throws IOException, EmptyRequestException {

        String line = readLine();
        if (line.isEmpty()){
            throw new EmptyRequestException("空请求异常!");
        }
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        parseURI(); //进一步解析uri
        protocol = data[2];
//        System.out.println("请求方式："+method+" "+"抽象路径:"+ uri+" 协议版本:"+protocol);

    }

    /**
     * 解析uri,带参数
     */
    private void parseURI(){
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if (data.length > 1 ) {
            queryString = data[1];
            //将参数按照"&"拆分
            parseParameters(queryString);
        }
    }

    /**
     * 解析参数 例如: name1=value1&name2=value2.......
     * @param line
     */
    private void parseParameters(String line){
        String[] paras = line.split("&");
        for (String p : paras) {
            //将每组按照"="拆
            String[] split = p.split("=");
            //第一项为key
            String key = split[0];
            String value = null;
            if (split.length > 1) {
                value = split[1];
            }
            parameters.put(key, value);
        }


    }

    private void parseHeaders() throws IOException {
        while(true){
            String line = readLine();
            if (line.isEmpty()){
                break;
            }
            String[] data = line.split(":\\s");
            header.put(data[0],data[1]);
        }

    }

    private void parseContent(){
        //通过判断请求头是否包含:Content-Length
        String length = header.get("Content-Length");
        if (length != null){
            int len = Integer.parseInt(length);
            contentData = new byte[len];

            //读取正文内容
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                inputStream.read(contentData);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            String contentType = header.get("Content-Type");
            if ("application/x-www-form-urlencoded".equals(contentType)){
                String line = new String(contentData);
                parseParameters(line);
            }
        }

    }

    public String readLine() throws IOException {
        InputStream inputStream = socket.getInputStream();
        int d;
        char cur = 'a';
        char pre = 'a';
        StringBuffer buffer = new StringBuffer();
        while ((d = inputStream.read()) != -1){
            cur = (char) d;
            if (pre == 13 && cur == 10){
                break;
            }
            buffer.append(cur);
            pre = cur;
        }
        return  buffer.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return header.get(name);
    }

    //进一步解析uri部分

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {

        String value =  parameters.get(name);
        if (value != null){
            //将中文的 %xxxxx还原,http协议不支持直接传输中文
            try {
                value = URLDecoder.decode(value,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
