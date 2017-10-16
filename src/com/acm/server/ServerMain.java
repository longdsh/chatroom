package com.acm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        while (true){
            Socket socket = serverSocket.accept();
            //接收到请求开启线程
            new Thread(new ChatServer(socket)).start();
        }
    }
}
