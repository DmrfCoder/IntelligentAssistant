package com.example.dmrf.intelligentassistant.Bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by DMRF on 2017/8/7.
 */

public class ChatMessage extends BmobObject {
    private String name;
    private boolean isNew;
    private String msg;
    private Type type;
    private Date date;
    private Integer num;

    public boolean getisNew() {
        return isNew;
    }

    public void setisNew(boolean aNew) {
        isNew = aNew;
    }

    public ChatMessage(String msg, Type type, Date date) {
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public ChatMessage() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public enum Type {
        INCOMING, OUTCOMING;
    }
}
