package com.example.convo;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class read {

    String msg,suid,ruid,phno,time;
    int isphoto;
    String photo;
    String bitmappath;


    public String getMsg() {
        return msg;
    }

    public String getSuid() {
        return suid;
    }

    public String getRuid() {
        return ruid;
    }

    public String getPhno() {
        return phno;
    }

    public String getTime() {
        return time;
    }

    public int getIsphoto() {
        return isphoto;
    }

    public void setIsphoto(int isphoto) {
        this.isphoto = isphoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBitmappath() {
        return bitmappath;
    }

    public void setBitmappath(String bitmap) {
        this.bitmappath = bitmap;
    }

    public read(String suid, String msg, String ruid, String phno, String time, String photo, int isphoto, String bitmap)
    {
        this.suid=suid;
        this.msg=msg;
        this.phno=phno;
        this.ruid=ruid;
        this.time=time;
        this.isphoto=isphoto;
        this.photo=photo;
        this.bitmappath = bitmap;
    }




}
