package com.example.convo;

public class message {

    String uid,msgg,cd,orguid,url;
    Boolean s;

    public Boolean getS() {
        return s;
    }

    public String getOrguid() {
        return orguid;
    }

    public String getUrl() {
        return url;
    }

    public message(String id, String msg, Boolean b, String orguid, String url)
    {
        uid=id;
        msgg=msg;
        s=b;
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
