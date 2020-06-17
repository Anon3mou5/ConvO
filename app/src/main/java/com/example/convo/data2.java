package com.example.convo;

public class data2 {
     String msg;
     String sender;
    String photo;
    String ruid;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    String suid;

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public data2(String msg, String sender, String photo, String ruid,String suid) {
        this.msg=msg;
        this.sender=sender;
        this.photo=photo;
        this.ruid = ruid;
        this.suid=suid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public  String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




}
