package com.acm.client;


import com.acm.bean.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatClient implements Runnable {

    private static final int intoFail = 0;


    private ClientUi clientUi = null;
    private Socket socket = null;
    private ObjectInputStream ois = null;

    public ChatClient(ClientUi clientUi, Socket socket) {
        this.clientUi = clientUi;
        this.socket = socket;
    }


    private void init() {
        ois = getObjectInputStream(socket);
    }


    private Client getClient() {
        Client client = null;
        try {
            client = (Client) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }


    /**
     * 得到输入流
     *
     * @param socket
     * @return
     */
    public ObjectInputStream getObjectInputStream(Socket socket) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ois;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            init();
            Client client = getClient();
            System.out.println("ChatClient接收消息:"+client);
            if(client.getInfo()!=intoFail){
                System.out.println("登录成功");
            }
        }

    }
}
