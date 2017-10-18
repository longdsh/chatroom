package com.acm.test;

import com.acm.bean.Client;
import com.acm.util.StreamUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Administrator on 2017/10/18.
 */
public class ServerTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       // StreamUtil streamUtil = new StreamUtil();
        Socket socket = new Socket("127.0.0.2",12345);
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("输出消息类型");
            Integer info = sc.nextInt();
             sc.nextLine();
            System.out.println("姓名");
            String name = sc.nextLine();
            System.out.println("输入message");
            String message = sc.nextLine();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos =new ObjectOutputStream(socket.getOutputStream());
            Client client = new Client(name,info);
            client.addMsg("message",message);

            oos.writeObject(client);

            client = (Client) ois.readObject();
            System.out.println(client);
        }
    }
}
