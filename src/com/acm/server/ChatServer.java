package com.acm.server;

import com.acm.bean.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatServer implements Runnable {

    private final static int INTO = 1;//加入群聊
    private final static int EXIT = -1;//退出群聊
    private final static int SAY_TO_ALL = 2;//对所有人说
    private final static int SAY_TO_ONE = 3;//私聊

    private static Map<String, Client> clients = null;
    private Client client = null;

    static {
        clients = new HashMap<String, Client>();
    }


    public void sendToAll(Client client) {

    }

    public void sendToOne(Client client) {

    }

    public void sendInto(Client client) {
        System.out.println("this clientt:"+this.client+"client:"+client+"map:"+clients);
    }

    public void sendExit(Client client) {

    }

    public ObjectInputStream getObjectInputStream() {
        Socket clientSocket = this.client.getSocket();
        System.out.println(this.client);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ois;
    }

    public ObjectOutputStream getObjectOutputStream() {
        Socket clientSocket = this.client.getSocket();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oos;
    }

    public Client getMessage() {
        Client message = null;
        try {
            message = (Client) getObjectInputStream().readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            Client message = getMessage();
            int info = client.getInfo();
            if (info == INTO) {
                sendInto(message);
            } else if (info == SAY_TO_ALL) {
                sendToAll(message);
            } else if (info == SAY_TO_ONE) {
                sendToOne(message);
            } else if (info == EXIT) {
                sendExit(message);
            }

        }


    }

    public ChatServer(Socket clientSocket) {
        //执行登录
        try {
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            this.client = (Client) ois.readObject();
            this.client.setSocket(clientSocket);
            sendInto(this.client);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ChatServer() {
    }

}
