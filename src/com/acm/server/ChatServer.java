package com.acm.server;

import com.acm.bean.Client;
import com.acm.util.StreamUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatServer implements Runnable {

    private final static int INTO = 1;//加入群聊
    private final static int EXIT = -1;//退出群聊
    private final static int SAY_TO_ALL = 2;//对所有人说
    private final static int SAY_TO_ONE = 3;//私聊


    private Socket clientSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oosToSelf = null;
    private ObjectOutputStream oosToOther = null;
    private static Map<String, Socket> clients = null;

    static {
        clients = new HashMap<String, Socket>();
    }

    /**
     * 群发
     *
     * @param client
     */
    public void sendToAll(Client client) {
        for (Map.Entry<String, Socket> entry : clients.entrySet()) {
            if (entry.getKey() != client.getName()) {//消息不发给自己
                //拿到socket
                oosToOther = StreamUtil.getObjectOutputStream(entry.getValue());
                try {
                    oosToOther.writeObject(client);
                } catch (IOException e) {
                    clients.remove(entry.getKey());//用户不正当退出
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 私发消息
     *
     * @param client
     */
    public void sendToOne(Client client) {
        String toName = (String) client.getMsg().get("toName");

        if (clients.containsKey(toName)) {
            oosToOther = StreamUtil.getObjectOutputStream(clients.get(toName));
            try {
                oosToOther.writeObject(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            client.addMsg("message", "用户不存在");
            try {
                oosToSelf.writeObject(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行登录 并通知所有人
     *
     * @param client
     */
    public void sendInto(Client client) {
        // System.out.println("this clientt:"+this.client+"client:"+client+"map:"+clients);
        String name = client.getName();
        if (clients.containsKey(name)) {
            client.setInfo(0);
            client.addMsg("message", "用户已存在");
            try {
                oosToSelf.writeObject(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(client.getName() + ":" + client.getMsg().get("message"));
            clients.put(client.getName(), this.clientSocket);//将客户socket放入map
            client.setInfo(1);
            client = client
                    .addMsg("message", "登录成功")
                    .addMsg("online", clients.size());//在线人数
            try {
                oosToSelf.writeObject(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.addMsg("message", name + ",加入群聊");
            sendToAll(client);
        }


    }

    /**
     * 退出并告诉所有人此用户退出
     *
     * @param client
     */
    public void sendExit(Client client) {
        clients.remove(client.getName());
        client.addMsg("message", client.getName() + "退出聊天室");
        sendToAll(client);

    }


    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            init();//每次初始到输入输出流 否则抛出异常
            Client client = getClient();
            int info = client.getInfo();//判断为哪种消息
            if (info == INTO) {
                sendInto(client);
            } else if (info == SAY_TO_ALL) {
                sendToAll(client);
            } else if (info == SAY_TO_ONE) {
                sendToOne(client);
            } else if (info == EXIT) {
                flag = false;
                sendExit(client);
            }

        }


    }


    /**
     * 通过输入流得到消息内容
     *
     * @return
     */
    public Client getClient() {
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
     * 初始化输入输出流
     */
    public void init() {
        oosToSelf = StreamUtil.getObjectOutputStream(this.clientSocket);
        ois = StreamUtil.getObjectInputStream(this.clientSocket);
    }

    public ChatServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ChatServer() {
    }

}
