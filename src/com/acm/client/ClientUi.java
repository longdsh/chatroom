package com.acm.client;

import com.acm.bean.Client;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/10/17.
 */
public class ClientUi extends JFrame{
    private final static int INTO_FAIL = 0;//加入群聊失败
    private final static int INTO_SUCCESS = 1;//加入群聊
    private final static int EXIT = -1;//退出群聊
    private final static int SAY_TO_ALL = 2;//对所有人说
    private final static int SAY_TO_ONE = 3;//私聊
    private Socket socket = null;
    private Boolean flag = false;
    ObjectOutputStream ois = null;
    Client client = null;
    /**
     * 连接框
     */
    private JPanel line =null;
    private JLabel lineLable = null;
    private JTextField lineText = null;
    private JButton lineBtn = null;
    private JLabel userNameLable = null;
    private JTextField userNameText = null;
    private JButton userNameBtn = null;
    /**
     * 群聊框
     */
    private JPanel sayToAll = null;
    private JLabel sayToAllLable = null;
    private JTextField sayToAllText =null;
    private JButton sayToAllBtn = null;
    /**
     * 私聊
     */
    private JPanel sayToOnePanel = null;
    private JPanel sayToOne = null;
    private JLabel sayToOneNameLable = null;
    private JTextField sayToOneName = null;
    private JLabel sayToOneLable = null;
    private JTextField sayToOneText = null;
    private JButton sayToOneBtn = null;

    /**
     * 显示信息
     */
    private JScrollPane showScroll = null;
    private JTextArea showInfo = null;
    /**
     * 退出
     */
    private JButton ExitBtn = null;

    ClientUi clientUi = null;

    public ClientUi(){
        /**
         * 连接
         */
        line = new JPanel();
        line.setBounds(0,0,1000,50);
        lineLable = new JLabel("输入服务器地址");
        lineText = new JTextField(20);
        lineBtn = new JButton("连接");
        userNameLable = new JLabel("输入用户名");
        userNameText = new JTextField(10);
        userNameBtn = new JButton("登录");
        userNameBtn.setEnabled(false);
        line.add(lineLable);
        line.add(lineText);
        line.add(lineBtn);
        line.add(userNameLable);
        line.add(userNameText);
        line.add(userNameBtn);
        /**
         * 私聊
         */
        sayToOne = new JPanel();
        sayToOne.setBounds(10,100,1000,40);
        sayToOneNameLable = new JLabel("输入要私聊人ID");
        sayToOneName = new JTextField(20);
        sayToOneLable = new JLabel("'输入信息");
        sayToOneText = new JTextField(20);
        sayToOneBtn = new JButton("私发消息");
        sayToOneBtn.setEnabled(false);
        sayToOne.add(sayToOneNameLable);
        sayToOne.add(sayToOneName);
        sayToOne.add(sayToOneLable);
        sayToOne.add(sayToOneText);
        sayToOne.add(sayToOneBtn);
        /**
         * 群发消息
         */
        sayToAll = new JPanel();
        sayToAll.setBounds(10,150,1000,50);
        sayToAllLable = new JLabel("输入要群发的消息");
        sayToAllText = new JTextField(20);
        sayToAllBtn = new JButton("群发消息");
        sayToAllBtn.setEnabled(false);
        sayToAll.add(sayToAllLable);
        sayToAll.add(sayToAllText);
        sayToAll.add(sayToAllBtn);
        /**
         * 显示消息
         */

        showInfo = new JTextArea();
        showInfo.setBounds(100,300,600,300);
        showInfo.setLineWrap(true);
        showInfo.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(showInfo);
        scrollPane.setBounds(100,200,600,300);

        ExitBtn = new JButton("退出");
        ExitBtn.setBounds(600,500,100,50);
        ExitBtn.setEnabled(false);


        this.add(line,BorderLayout.NORTH);
        this.add(sayToOne,BorderLayout.WEST);
        this.add(sayToAll,BorderLayout.CENTER);
        this.add(scrollPane,BorderLayout.SOUTH);
        this.add(ExitBtn,BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        this.setBounds(10,10,1000,700);
        this.setVisible(true);
        this.clientUi = this;
       // this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bind();
    }

    public void bind(){

        lineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = lineText.getText().trim();
                try {
                    socket = new Socket(ip,12345);
                    flag = true;
                    new Thread(new ChatClient(clientUi,socket)).start();
                    userNameBtn.setEnabled(true);
                    showInfo.append("已连接\n");
                    lineBtn.setEnabled(false);
                } catch (IOException e1) {
                    //e1.printStackTrace();
                    lineText.setText("ip地址错误");
                }
            }
        });

        userNameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("userNameBtn click");
                try {
                    ois = new ObjectOutputStream(socket.getOutputStream());
                    String name = userNameText.getText().trim();
                    client = new Client(name,INTO_SUCCESS);
                    ois.writeObject(client);
                } catch (IOException e1) {
                    flag = false;
                }
            }
        });

        sayToAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = sayToAllText.getText().trim();
                System.out.println("sayToAllBtn click");

                try {
                    ois = new ObjectOutputStream(socket.getOutputStream());
                    client.setInfo(SAY_TO_ALL);
                    client.addMsg("message",client.getName()+":"+message);
                    ois.writeObject(client);
                    showInfo.append("发送成功\n");//正常来说应该是服务器返回消息才设置此处偷懒
                } catch (IOException e1) {
                    flag = false;
                }

            }
        });

        sayToOneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("sayToOneBtn click");
                showInfo.append("发送成功\n");//正常来说应该是服务器返回消息才设置此处偷懒
                try {
                    String message = sayToOneText.getText().trim();
                    String toName = sayToOneName.getText().trim();
                    ois = new ObjectOutputStream(socket.getOutputStream());
                    client.setInfo(SAY_TO_ONE);
                    client.addMsg("message",client.getName()+" 悄悄话:"+message).addMsg("toName",toName);
                    ois.writeObject(client);
                } catch (IOException e1) {
                    flag = false;
                }
            }
        });

        ExitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = false;
                try {
                    ois = new ObjectOutputStream(socket.getOutputStream());
                    client.setInfo(EXIT);
                    ois.writeObject(client);
                    //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } catch (IOException e1) {
                    //e1.printStackTrace();
                }
            }
        });
    }



    public JTextArea getShowInfo() {
        return showInfo;
    }


    public Boolean getFlag() {
        return flag;
    }

    public ClientUi setFlag(Boolean flag) {
        this.flag = flag;
        return this;
    }

    public JTextField getLineText() {
        return lineText;
    }

    public ClientUi(Socket socket){
        this.socket = socket;
    }


    public JButton getLineBtn() {
        return lineBtn;
    }

    public JButton getUserNameBtn() {
        return userNameBtn;
    }

    public JButton getSayToAllBtn() {
        return sayToAllBtn;
    }

    public JButton getSayToOneBtn() {
        return sayToOneBtn;
    }

    public JButton getExitBtn() {
        return ExitBtn;
    }

    @Override
    public String toString() {
        return "ClientUi{" +
                "flag=" + flag +
                '}';
    }

    public static void main(String[] args) throws IOException {
        //Socket socket = new Socket("127.0.0.2",12345);
        new ClientUi();
    }
}
