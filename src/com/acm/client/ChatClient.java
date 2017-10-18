package com.acm.client;


import com.acm.bean.Client;
import com.acm.util.StreamUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatClient implements Runnable {

    private static final int intoFail = 0;

    private ClientUi clientUi = null;
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    public ChatClient(ClientUi clientUi, Socket socket) {
        this.clientUi = clientUi;
        this.socket = socket;
    }

    private void init() {
        oos = StreamUtil.getObjectOutputStream(socket);
        ois = StreamUtil.getObjectInputStream(socket);
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

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            init();
            Client client = getClient();
            if(client.getInfo()!=intoFail){
                System.out.println("登录成功");
            }

        }

    }
}
