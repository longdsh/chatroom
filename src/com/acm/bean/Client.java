package com.acm.bean;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class Client implements Serializable{
    private String name;
    private Integer info;
    private Socket socket ;
    private String message;

    public Client() {
    }

    public Client(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public Client(String name, Integer info, Socket socket, String message) {
        this.name = name;
        this.info = info;
        this.socket = socket;
        this.message = message;
    }

    public Integer getInfo() {
        return info;
    }

    public Client setInfo(Integer info) {
        this.info = info;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Client setMessage(String message) {
        this.message = message;
        return this;
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
