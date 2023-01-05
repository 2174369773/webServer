package com.xmc;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
            System.out.println("读取到请求行:"+ buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
