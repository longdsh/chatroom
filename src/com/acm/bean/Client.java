package com.acm.bean;

import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class Client {
    private String name;
    private Socket socket ;

    public Client() {
    }

    public Client(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }
}
