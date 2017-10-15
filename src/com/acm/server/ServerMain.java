package com.acm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ServerMain {
    public static void main(String[] args) {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(8555);
                Socket clientSocket = serverSocket.accept();
                new Thread(new ChatServer(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
