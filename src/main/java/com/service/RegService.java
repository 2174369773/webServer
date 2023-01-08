package com.service;

import com.http.HttpServlet;
import com.http.HttpServletRequest;
import com.http.HttpServletResponse;
import com.pojo.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RegService extends HttpServlet {

    @Override
    public void service(HttpServletRequest request,HttpServletResponse response){
        //解析请求获取信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String age = request.getParameter("age");
        //保存信息
        User user = new User(username,password,nickname,Integer.parseInt(age));

        try(
            FileOutputStream outputStream = new FileOutputStream("" +
                    "user/"+username+".obj");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ){
            objectOutputStream.writeObject(user);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        //响应成功页面
       forward("/myweb/reg_success.html",request,response);

    }
}
