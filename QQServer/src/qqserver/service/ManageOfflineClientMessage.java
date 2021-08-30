package qqserver.service;

import qqcommon.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ManageOfflineClientMessage {

    //    创建一个集合，存放离线消息
    public static HashMap<String, Message> offLineDb = new HashMap<>();

    public static HashMap<String, Message> getOffLineDb() {
        return offLineDb;
    }

//    把离线消息放进集合

    public static void addOfflineMes(String getterId, Message message) {
        offLineDb.put(getterId, message);
    }



//    根据getterId返回消息
    public static int getMesLength() {

        return offLineDb.size();
    }

    public static Message getOfflineMes(String getterId) {


        return offLineDb.get(getterId);

    }


//    根据getterId移除消息

    public static void removeOfflineMes(String getterId) {
        offLineDb.remove(getterId);
    }


}
