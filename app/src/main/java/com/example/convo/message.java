package com.example.convo;

public class message {

    String uid,msgg;


    public message(String id, String msg)
    {
        uid=id;
        msgg=msg;
    }
    public String getUid() {
        return uid;
    }

    public String getMsgg() {
        return msgg;
    }

}
