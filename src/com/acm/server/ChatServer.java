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

    private static Map<String,Client> clients = null;
    private Client client = null;

    static {
         clients = new HashMap<String,Client>();
    }


    public void sendToAll(Client client) {

    }

    public void sendToOne(Client client){

    }
    @Override
    public void run() {



    }

    public ChatServer(Socket clientSocket){
        try {
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            this.client = (Client) ois.readObject();
            this.clients.put(this.client.getName(),this.client);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ChatServer() {
    }

}
