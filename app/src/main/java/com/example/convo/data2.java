package com.example.convo;

public class data2 {
     String msg;
     String sender;
    String profilephoto,photo;
    String ruid;
    int isphoto;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    String suid;

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public int getIsphoto() {
        return isphoto;
    }

    public void setIsphoto(int isphoto) {
        this.isphoto = isphoto;
    }

    public data2(String msg, String sender, String profilephoto, String ruid, String suid, String photo,int isphoto) {
        this.msg=msg;
        this.sender=sender;
        this.profilephoto=profilephoto;
        this.ruid = ruid;
        this.suid=suid;
        this.isphoto = isphoto;
        this.photo = photo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public  String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




}
