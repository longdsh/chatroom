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



    @Override
    public void run() {
        /**
         * 接收消息
         */
        System.out.println("ClientUi:"+this.clientUi);
        while (clientUi.getFlag()) {
            Client client = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                client = (Client) ois.readObject();
            } catch (Exception e) {
                clientUi.setFlag(false);
                //e.printStackTrace();
            }
            System.out.println("ClientUi:"+this.clientUi);
            System.out.println("ChatClient接收消息:"+client);
            /*if(client.getInfo()!=INTO_FAIL){
                //System.out.println("登录成功");
            }*/
            if(client.getInfo()==INTO_FAIL){
                clientUi.getShowInfo().append("用户已存在\n");
            }else if(client.getInfo()==INTO_SUCCESS){
                clientUi.getShowInfo().append("登录成功\n");
                clientUi.getLineText().setText("在线人数："+client.getMsg().get("online").toString());
                clientUi.getUserNameBtn().setEnabled(false);
                clientUi.getSayToAllBtn().setEnabled(true);
                clientUi.getSayToOneBtn().setEnabled(true);
                clientUi.getExitBtn().setEnabled(true);
            }else if(client.getInfo()==SAY_TO_ALL){
                clientUi.getShowInfo().append((String) client.getMsg().get("message")+"\n");
                clientUi.getLineText().setText( "在线人数："+client.getMsg().get("online").toString());
            }else if(client.getInfo()==SAY_TO_ONE){
                clientUi.getShowInfo().append((String) client.getMsg().get("message")+"\n");
            }
            //光标移到末行
            clientUi.getShowInfo().setCaretPosition(clientUi.getShowInfo().getText().length());
        }

    }
}
