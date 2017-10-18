package com.acm.com.acm.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/10/18.
 */
public class StreamUtil {

    /**
     * 得到输出流
     *
     * @param socket
     * @return
     */
    public static ObjectOutputStream getObjectOutputStream(Socket socket) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oos;
    }

    /**
     * 得到输入流
     *
     * @param socket
     * @return
     */
    public static ObjectInputStream getObjectInputStream(Socket socket) {
        // System.out.println(this.client);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ois;
    }
}
