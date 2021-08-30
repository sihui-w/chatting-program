package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{

//    该线程持有socket

    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
//        thread要与服务器保持通信，所以loop
        while(true){

            try {
                System.out.println("客户端线程，等待读取从服务器发回的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

//                判断message类型，做相应业务处理

                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String [] onlineUsers = message.getContent().split(" ");

                    System.out.println("\n=========当前在线用户============");

                    for(int i = 0; i< onlineUsers.length; i++){
                        System.out.println("用户： "+onlineUsers[i]);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println("\n"+message.getSender()+"对"
                            +message.getGetter()+"说： "+message.getContent());

                }else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){

                    System.out.println("\n"+ message.getSender() + "对大家说： "+message.getContent());

                } else if(message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES)){
                    System.out.println("\n"+message.getSender()+"对你发来离线消息"+message.getContent());

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){

                    System.out.println("\n"+ message.getSender() + "给" +message.getGetter()+"发文件： "
                            +message.getSrc()+" 到我的电脑目录"+message.getDest());

//                    把message中的字节数组，通过输出流写出到磁盘

                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功");


                } else{
                    System.out.println("wrong type");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}













































