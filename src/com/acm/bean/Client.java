package com.acm.bean;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lusufei1996 on 2017/10/15.
 */
public class Client implements Serializable{
    private String name;
    private Integer info;
    private Map<String,Object> msg = new HashMap<String,Object>();

    public Client() {
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getInfo() {
        return info;
    }

    public Client setInfo(Integer info) {
        this.info = info;
        return this;
    }


    public Client addMsg(String key,Object value){
        msg.put(key,value);
        return this;
    }

    public Map<String, Object> getMsg() {
        return msg;
    }

    public Client setMsg(Map<String, Object> msg) {
        this.msg = msg;
        return this;
    }

    public Client(String name, Integer info) {
        this.name = name;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", info=" + info +
                ", msg=" + msg +
                '}';
    }
}
