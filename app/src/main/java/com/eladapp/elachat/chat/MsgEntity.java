package com.eladapp.elachat.chat;
public class MsgEntity {
    //分别代表发送和接受消息的类型
    public static final int SEND_MSG=1;
    public static final int RCV_MSG=2;
    //消息内容
    private String content;
    //发送接收
    private int type;
    //接收与发送者
    private String sender;
    private String reciver;
    //消息类型
    private int msgcate;

    public MsgEntity(int type, String content,String sender,String reciver,int msgcate) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.reciver = reciver;
        this.msgcate = msgcate;
    }
    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }
    public String  getReciver() {
        return reciver;
    }
    public int getType() {
        return type;
    }
    public int getMsgcate() {
        return msgcate;
    }
}
