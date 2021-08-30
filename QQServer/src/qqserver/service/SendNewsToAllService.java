package qqserver.service;


import qqcommon.Message;
import qqcommon.MessageType;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendNewsToAllService implements Runnable{
    @Override
    public void run() {

//        用while保持推送
        while (true) {
            System.out.println("请输入要推送的新闻（输入exit表示取消推送： ");
            String news = Utility.readString(100);

            if("exit".equals(news)){
                break;
            }

//        把新闻包到message对象中
            Message message = new Message();
            message.setSender("服务器");
            message.setMesType(MessageType.MESSAGE_TOALL_MES);

            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器发送新闻： " + news);

//        遍历所有线程，得到socket，再输出流发送
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineUserId = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                    oos.writeObject(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }

    }
}



















































