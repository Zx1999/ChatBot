package com.demo.sisyphus.chatRobot.bean;

public class Msg {

    //表名
    public static final String TABLE="Msg";

    //表的各个域名
    public static final String KEY_id="id";
    public static final String KEY_msg="msg";
    public static final String KEY_type="type";

    public int id;
    public String msg;
    public int type;

    public Msg(String msg, int type, int id){
        this.msg = msg;
        this.type = type;
        this.id = id;
    }

    public Msg(String msg, int type){
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }

    public int getId(){
        return id;
    }

}
