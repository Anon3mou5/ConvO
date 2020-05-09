package com.example.convo;

import android.util.Log;

public class Data {

    String name;
    String url;
    public Data(String nm,String url)
    {
        name=nm;
        this.url=url;
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
