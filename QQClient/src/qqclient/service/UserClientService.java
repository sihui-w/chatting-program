package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//完成用户验证、注册等功能
public class UserClientService {

    private User u = new User();
    private Socket socket;

    public boolean checkUser(String userId,String pwd){
        boolean b= false;
//        创建User对象

        u.setUserId((userId));
        u.setPasswd(pwd);

//    连接到服务器，发送u对象

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 8888);
//发送
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

//            读取从服务器回复的message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){



//                创建线程，和服务器保持通讯

                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);

//                启动线程
                clientConnectServerThread.start();

//                把线程放入集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);

                b = true;


            }else{

                socket.close();

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return b;

    }

//    向服务器端请求在线用户列表
    public void onlineFriendList() throws IOException {

//        发送请求好友的message对象
        Message message = new Message();

        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());

//        通过输出流对象发出（先得到线程，再得到socket）
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.
                getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());

        oos.writeObject(message);

    }

//    退出客户端方法，给服务端发送退出的messa

    public void logout() throws IOException {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);

        message.setSender(u.getUserId());

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        System.out.println(u.getUserId()+"退出系统");
        System.exit(0);

    }








}





























































