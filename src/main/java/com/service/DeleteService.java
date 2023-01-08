package com.service;

import com.http.HttpServlet;
import com.http.HttpServletRequest;
import com.http.HttpServletResponse;

import java.io.File;

public class DeleteService extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        File file = new File("./user/"+username+".obj");
        if (file.exists()){
            file.delete();
            forward("/myweb/delete_success.html",request,response);
            return;
        }

        forward("/myweb/delete_fail.html",request,response);
    }
}
