package com.http;

import java.io.File;

public abstract class HttpServlet {

    public abstract void service(HttpServletRequest request,HttpServletResponse response);

    public void forward(String path,HttpServletRequest request,HttpServletResponse response){

        File file = new File("webapps/"+path);
        response.setEntity(file);

    }
}
