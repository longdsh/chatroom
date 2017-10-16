package com.acm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        while (true){
            Socket socket = new Socket("localhost",8555);
            //接收到请求开启线程
            new Thread(new ChatServer(socket)).start();
        }
    }
}
