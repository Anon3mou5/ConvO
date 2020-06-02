package com.example.convo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class read {

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

    public read(String suid, String msg, String ruid, String phno)
    {
        this.suid=suid;
        this.msg=msg;
        this.ruid=ruid;
        this.phno=phno;
    }




}
