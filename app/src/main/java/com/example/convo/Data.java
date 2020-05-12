package com.example.convo;

import android.app.Activity;
import android.util.Log;

public class Data {

    String name,status,phno;
    int found;
    String url,uid;
    Activity a;
    public int getFound() {
        return found;
    }
    public String getStatus() {
        return status;
    }

    public Activity geta() {
        return a;
    }

    public String getUid() {
        return uid;
    }

    public String getPhno() {
        return phno;
    }

    public Data(String nm, String url, int found, Activity a, String uid, String phno)
    {
        name=nm;
        this.url=url;
        this.phno=phno;
        this.found=found;
        this.a=a;
        this.uid=uid;
    }

    public String getname()
    {
        Log.d("Getters called","");
        return name;
    }
    public String geturl()
    {
        return url;
    }
}
