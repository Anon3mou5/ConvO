package com.example.convo;

public class chat {

    String msg,suid,ruid,url,phno;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    int unread;

    public String getMsg() {
        return msg;
    }

    public String getSuid() {
        return suid;
    }

    public String getRuid() {
        return ruid;
    }

    public String getUrl() {
        return url;
    }

    public String getPhno() {
        return phno;
    }



    public chat(String suid, String msg, String ruid, String url, String phno,int unread)
    {
        this.suid=suid;
        this.url=url;
        this.msg=msg;
        this.ruid=ruid;
        this.phno=phno;
        this.unread =unread;
    }




}
