package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Date;

//该对象提供和消息相关的方法
public class MessageClientService {



    public void sendMessageToAll(String content,String senderId) throws IOException {
        //       组装message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TOALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + "对大家说： "+content);

//        发送

        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                .getClientConnectServerThread(senderId).getSocket().getOutputStream());

        oos.writeObject(message);

    }

    public void sendMessageToOne(String content,String senderId, String getterId) throws IOException {
//       组装message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + "对"+ getterId+ "说： "+content);

//        发送

        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                .getClientConnectServerThread(senderId).getSocket().getOutputStream());

        oos.writeObject(message);

    }

    public void sendMessageToOffline(String content,String senderId, String getterId) throws IOException {
        //       组装message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_OFFLINE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + "对上线后的"+ getterId+ "说： "+content);

//        发送

        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                .getClientConnectServerThread(senderId).getSocket().getOutputStream());

        oos.writeObject(message);
    }

    public void comfirmReadOfflineMes(String content,String senderId) throws IOException {
//        组装message
        Message message1 = new Message();
        message1.setMesType(MessageType.MESSAGE_GET_OFFLINE_MES);
        message1.setSender(senderId);
        message1.setContent(content);
        System.out.println(senderId+"的离线消息读取中...");

//        发送
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                .getClientConnectServerThread(senderId).getSocket().getOutputStream());

        oos.writeObject(message1);



    }


}



























