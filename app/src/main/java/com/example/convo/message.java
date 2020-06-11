package com.example.convo;

public class message {

    String uid,msgg,cd,time,orguid,url;
    Boolean s;

    public String getTime() {
        return time;
    }

    public Boolean getS() {
        return s;
    }

    public String getOrguid() {
        return orguid;
    }

    public String getUrl() {
        return url;
    }

    public message(String id, String msg, Boolean b, String orguid, String url,String time)
    {
        uid=id;
        msgg=msg;
        s=b;
        this.time=time;
        this.url=url;
        this.orguid=orguid;
    }

    public message()
    {
    }
    public String getUid() {
        return uid;
    }

    public String getMsgg() {
        return msgg;
    }

}
