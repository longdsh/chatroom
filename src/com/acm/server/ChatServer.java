package com.acm.server;

import com.acm.bean.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatServer implements Runnable {

    /**
     * 向同一个对象输入输出流不要重复初始化2次 如
     * <p>
     * ois  = getObjectInputStream(this.clientSocket);一次后
     * 之后再 ois  = getObjectInputStream(this.clientSocket);会抛异常
     */
    private final static int INTO_FAIL = 0;//加入群聊失败
    private final static int INTO_SUCCESS = 1;//加入群聊
    private final static int EXIT = -1;//退出群聊
    private final static int SAY_TO_ALL = 2;//对所有人说
    private final static int SAY_TO_ONE = 3;//私聊
    private String name ;
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
            if (!entry.getKey().equals(client.getName())) {//消息不发给自己
                //拿到socket
                System.out.println("群发消息to：" + entry.getKey());
                try {
                    client.addMsg("online",clients.size());
                    oosToOther = new ObjectOutputStream(entry.getValue().getOutputStream());
                    oosToOther.writeObject(client);
                } catch (IOException e) {
                    System.out.println("用户不正当退出");
                    clients.remove(entry.getKey());//用户不正当退出
                    //e.printStackTrace();
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
            try {
                oosToOther = new ObjectOutputStream(clients.get(toName).getOutputStream());
                oosToOther.writeObject(client);
            } catch (IOException e) {
                clients.remove(toName);//用户不正当退出
                //e.printStackTrace();
            }
        } else {
            client.addMsg("message", "用户不存在");
            try {
                oosToSelf = new ObjectOutputStream(this.clientSocket.getOutputStream());
                oosToSelf.writeObject(client);
            } catch (IOException e) {
               // e.printStackTrace();
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
            client.setInfo(INTO_FAIL);
            client.addMsg("message", "用户已存在");
            try {
                oosToSelf = new ObjectOutputStream(this.clientSocket.getOutputStream());
                oosToSelf.writeObject(client);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        } else {
            clients.put(client.getName(), this.clientSocket);//将客户socket放入map
            client.setInfo(INTO_SUCCESS);
            client = client
                    .addMsg("message", "登录成功")
                    .addMsg("online", clients.size());//在线人数
            try {
                oosToSelf = new ObjectOutputStream(this.clientSocket.getOutputStream());
                oosToSelf.writeObject(client);
            } catch (IOException e) {
               // e.printStackTrace();
            }
            client.setInfo(SAY_TO_ALL);
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
            Client client = null;
            int info = 5;
            try {
                ois = new ObjectInputStream(this.clientSocket.getInputStream());
                client = (Client) ois.readObject();
                info = client.getInfo();//判断为哪种消息
            } catch (Exception e) {
                flag = false;
            }
            if (info == INTO_SUCCESS) {
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


    public ChatServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ChatServer() {
    }

    /**
     * 算了 少个文件
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        while (true){
            Socket socket = serverSocket.accept();
            //接收到请求开启线程
            new Thread(new ChatServer(socket)).start();
        }
    }

}
