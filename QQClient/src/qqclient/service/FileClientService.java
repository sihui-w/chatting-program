package qqclient.service;

// 完成文件传输服务

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.*;

public class FileClientService {

    public void sendFileToOne(String src,String dest,String senderId,String getterId) throws IOException {
//        读取src文件
//        1.构建message对象
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //2.创建文件输入流
        FileInputStream fileInputStream = null;
        byte [] fileBytes = new byte[(int)new File(src).length()];
        fileInputStream = new FileInputStream(src);
        fileInputStream.read(fileBytes);

//        3.将文件对应的字节数组设置到message

        message.setFileBytes(fileBytes);

        if(fileInputStream != null){
            fileInputStream.close();
        }

//        提示信息
        System.out.println("\n" + senderId + "给" + getterId + "发送文件： "
                +src+ "到对方的电脑目录： " +dest);

//        发送
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                .getClientConnectServerThread(senderId).getSocket().getOutputStream());

        oos.writeObject(message);



    }

}































