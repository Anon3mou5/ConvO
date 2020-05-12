package com.example.convo;

public class chat {

    String msg,suid,ruid,url,phno;

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

    public chat(String suid, String msg, String ruid, String url, String phno)
    {
        this.suid=suid;
        this.url=url;
        this.msg=msg;
        this.ruid=ruid;
        this.phno=phno;
    }




}
