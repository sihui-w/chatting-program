package qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //    把线程对象添加至集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }

//    根据userid返回线程

    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }

//    根据userid移除线程

    public static void removeServerConnectClientThread(String userId){
        hm.remove(userId);
    }

//    返回在线用户列表的方法

    public static String getOnlineUser(){

//        遍历存放了线程的hashmap

        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";

        while(iterator.hasNext()){
            onlineUserList += iterator.next().toString()+" ";
        }
        return onlineUserList;



    }




















}
