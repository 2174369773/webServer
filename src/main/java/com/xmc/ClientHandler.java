package com.xmc;

import com.http.HttpContext;
import com.http.HttpServlet;
import com.http.HttpServletRequest;
import com.http.HttpServletResponse;
import com.service.LoginService;
import com.service.RegService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * 线程处理类
 */
public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpServletRequest httpServletRequest = new HttpServletRequest(socket);
            HttpServletResponse httpServletResponse = new HttpServletResponse(socket);

            //处理请求
            String uri = httpServletRequest.getRequestURI();
            HttpServlet servlet = ServerContext.getServlet(uri);
            if (servlet != null){
                servlet.service(httpServletRequest,httpServletResponse);
            }else {
                File file = new File("webapps" + uri);
                //发送响应
                if (file.isFile() && file.exists()) {
                    httpServletResponse.setEntity(file);
                } else {
                    File file1 = new File("webapps/root/404.html");
                    httpServletResponse.setStatusCode(404);
                    httpServletResponse.setStatusReason("NOT FOUND");
                    httpServletResponse.setEntity(file1);
                }

            }
            httpServletResponse.response();
            System.out.println("响应完毕!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {
            System.out.println("空请求");
        } finally {
            try {
                //http一问一答后断开链接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
