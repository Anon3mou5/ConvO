package com.example.convo;

import android.net.Uri;

public class gallery {
    public String sender,when,url;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public gallery(String sender, String when, String url) {
        this.sender = sender;
        this.when = when;
        this.url = url;
    }
}
