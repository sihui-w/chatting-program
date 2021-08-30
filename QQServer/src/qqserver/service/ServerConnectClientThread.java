package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//该线程的对象和某个客户端保持通信
public class ServerConnectClientThread extends Thread{

    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }


    @Override
    public void run() {
        while (true) {

            try {
                System.out.println("保持和客户端通信，" + userId + "读取数据");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();


//                根据message类型，执行相关业务逻辑

                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
//                    客户端请求用户列表 1.获取用户列表；2.用message对象发出3.用输出流发出

                    System.out.println(message.getSender() + " 请求在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();

                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
//                    根据message得到getter id，获得对应线程
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(message.getGetter());


//                    得到对应输出流，发送message

                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(
                                serverConnectClientThread.getSocket().getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        oos.writeObject(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)) {
//                    遍历管理线程的集合，得到所有线程的socket
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
//                        取出在线用户id
                        String onlineUserId = iterator.next().toString();

                        if (!onlineUserId.equals(message.getSender())) {
//                            转发
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);

                        }
                    }


                } else if (message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES)) {
                    ManageOfflineClientMessage.addOfflineMes(message.getGetter(), message);

                } else if (message.getMesType().equals(MessageType.MESSAGE_GET_OFFLINE_MES)) {

                    Message message3 = ManageOfflineClientMessage.getOfflineMes(message.getSender());

                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());


                    oos.writeObject(message3);

                    ManageOfflineClientMessage.removeOfflineMes(message.getSender());

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
//                    1.得到线程
//                    2.得到socket
//                    3.得到输出流
//                    4.转发
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());

                    oos.writeObject(message);


                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + "退出");

//                    删除客户端对应线程

                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }




}
