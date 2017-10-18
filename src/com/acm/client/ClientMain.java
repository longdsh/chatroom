package com.acm.client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2017/10/17.
 */
public class ClientMain {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.2",12345);
        ClientUi clientUi = new ClientUi();
        new Thread(new ChatClient(clientUi,socket)).start();
    }
}
