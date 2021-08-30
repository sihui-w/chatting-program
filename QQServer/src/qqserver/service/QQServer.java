package qqserver.service;
//监听端口，等待客户端连接，保持通讯

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class QQServer {
    private ServerSocket ss = null;

//    创建一个集合，存放可登陆的用户
    public static HashMap<String,User>validUsers = new HashMap<>();



    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
        validUsers.put("500",new User("500","123456"));
        validUsers.put("600",new User("600","123456"));

    }

//    验证用户

    private boolean checkUser(String userId,String passwd){
        User user = validUsers.get(userId);
        if(user == null){
            return false;
        }
        if(!user.getPasswd().equals(passwd)){
            return false;
        }
        return true;
    }




    public QQServer(){
        try {
            System.out.println("服务端在8888端口监听...");
//            启动推送新闻的进程
            new Thread(new SendNewsToAllService()).start();

            ss = new ServerSocket(8888);
            
            while (true){
                Socket socket = ss.accept();
                
//                得到输入流

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Message message = new Message();
                
//                验证
                if(checkUser(u.getUserId(),u.getPasswd())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);

//                    回复message
                    oos.writeObject(message);

//                    创建持有socket的线程，和客户端保持通信
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());

                    serverConnectClientThread.start();

//                    把线程放进集合管理

                    ManageClientThreads.addClientThread(u.getUserId(),serverConnectClientThread);


                }else{
                    System.out.println("用户 id= "+u.getUserId()+" pwd="+u.getPasswd()+"验证失败");

                    message.setMesType((MessageType.MESSAGE_LOGIN_FAIL));
                    oos.writeObject(message);
                    socket.close();

                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
