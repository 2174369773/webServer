package com.xmc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 */
public class WebServer {

    private ServerSocket serverSocket;

    public WebServer(){
        try {
            System.out.println("服务端正在启动.....");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动成功......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){

        try {
            System.out.println("等待客户端链接");
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端链接成功");
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        WebServer webServer = new WebServer();
        webServer.start();

    }
}
