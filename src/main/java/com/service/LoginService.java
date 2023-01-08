package com.service;

import com.http.HttpServlet;
import com.http.HttpServletRequest;
import com.http.HttpServletResponse;
import com.pojo.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoginService extends HttpServlet {

    @Override
    public void service( HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        File file = new File("./user/"+username+".obj");
        if (file.exists()){
            try(
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ) {
                User user = (User) objectInputStream.readObject();
                if (password.equals(user.getPassword())){
                    forward("/myweb/login_success.html",request,response);
                    return;
                }

            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }else {
            forward("/myweb/login_fail.html",request,response);
        }

    }
}
