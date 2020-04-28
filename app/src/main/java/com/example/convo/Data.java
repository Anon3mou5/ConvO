package com.example.convo;

import android.util.Log;

public class Data {

    String name;
    String chat;
    public Data(String nm,String chat)
    {
        name=nm;
        this.chat=chat;
    }

    public String getname()
    {
        Log.d("Getters called","");
        return name;
    }
    public String getchat()
    {
        return chat;
    }
}
