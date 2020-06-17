package com.example.convo;

public class sender {
    public data2 data;
    public String to;

    public data2 getData() {
        return data;
    }

    public void setData(data2 data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public sender(data2 data, String to) {
        this.data = data;
        this.to = to;
    }
}
