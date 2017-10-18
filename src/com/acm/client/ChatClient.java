package com.acm.client;


import com.acm.bean.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatClient implements Runnable {

    private final static int INTO_FAIL = 0;//加入群聊失败
    private final static int INTO_SUCCESS = 1;//加入群聊
    private final static int EXIT = -1;//退出群聊
    private final static int SAY_TO_ALL = 2;//对所有人说
    private final static int SAY_TO_ONE = 3;//私聊
    private ClientUi clientUi = null;
    private Socket socket = null;
    private ObjectInputStream ois = null;

    public ChatClient(ClientUi clientUi, Socket socket) {
        this.clientUi = clientUi;
        this.socket = socket;
    }

    public ChatClient() {

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
            if(client.getInfo()!=INTO_FAIL){
                System.out.println("登录成功");
            }
        }

    }
}
