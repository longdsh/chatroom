package com.acm.server;

import com.acm.bean.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class ChatServer implements Runnable {

    private static List<Client> clients = null;
    private Socket clientSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    static {
        clients = new ArrayList<Client>();
    }


    public void sendToAll(String message) {
        for(Client client:clients){
            Socket socket = client.getSocket();
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                ois = new ObjectInputStream(clientSocket.getInputStream());
                Client client = (Client) ois.readObject();
                client.setSocket(clientSocket);
                clients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public ChatServer(Socket clientSocket) {

        this.clientSocket = clientSocket;

    }

    public ChatServer() {
    }

}
