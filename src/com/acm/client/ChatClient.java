package com.acm.client;

import com.acm.bean.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.2",12345);
       while(true) {
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            Client client = new Client();
            client.setInfo(1).setMessage("login").setName(name);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(client);
        }
    }
}
