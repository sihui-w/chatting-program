package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;

import java.io.IOException;

//客户端菜单页面
public class QQView {
    private boolean loop = true;
//    接受用户输入
    private String key = "";

    private UserClientService userClientService = new UserClientService();

    private MessageClientService messageClientService = new MessageClientService();

    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
    }

//    显示主菜单
    private void mainMenu() throws IOException, ClassNotFoundException {

        while(loop){
            System.out.println("==============欢迎登录网络通信系统================");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择： ");

            key = Utility.readString(1);

//            根据用户输入，处理不同逻辑
            switch (key){
                case "1":
                    System.out.println("请输入用户号： ");
                    String userId = Utility.readString(50);

                    System.out.println("请输入用户密码： ");
                    String pwd = Utility.readString(50);

//                    到服务端验证用户是否存在,待补充

                    if(userClientService.checkUser(userId,pwd)){
                        System.out.println("==============欢迎" + userId +"================");
//                        二级菜单
                        while(loop){
                            System.out.println("\n======================网络通信系统二级菜单（用户 " + userId + ")============================");
                            System.out.println("\t\t1 显示在线用户列表");
                            System.out.println("\t\t2 群发消息");
                            System.out.println("\t\t3 私聊消息");
                            System.out.println("\t\t4 发送文件");
                            System.out.println("\t\t5 留言离线用户");
                            System.out.println("\t\t6 读取离线消息");
                            System.out.println("\t\t9 退出系统");
                            System.out.println("请输入你的选择： ");

                            key = Utility.readString(1);

                            switch (key){
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入群发消息： ");
                                    String s = Utility.readString(100);
                                    messageClientService.sendMessageToAll(s,userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户号（在线）： ");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content = Utility.readString(100);

//                                    发送消息
                                    messageClientService.sendMessageToOne(content,userId,getterId);

                                    break;
                                case "4":
                                    System.out.println("请输入你想发送的对象（在线用户）： ");
                                    getterId = Utility.readString(50);

                                    System.out.println("请输入发送文件路径（形式 d：\\xx.jpg）");
                                    String src = Utility.readString(100);

                                    System.out.println("请输入接收文件路径（形式 d：\\xx.jpg）");
                                    String dest = Utility.readString(100);

                                    fileClientService.sendFileToOne(src,dest,userId,getterId);

                                    break;

                                case "5":
                                    System.out.println("请输入想聊天的用户号（离线）： ");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    content = Utility.readString(100);

//                                    发送消息
                                    messageClientService.sendMessageToOffline(content,userId,getterId);
                                    break;

                                case "6":
                                    System.out.println("确认读取离线消息：（是/否");
                                    content = Utility.readString(50);

                                    messageClientService.comfirmReadOfflineMes(content,userId);
                                    break;


                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }
                    }else{
                        System.out.println("登陆失败");
                    }
                    break;

                case "9":
                    loop = false;
                    break;
            }


        }
    }
}























































