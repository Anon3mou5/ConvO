package com.example.convo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Modelclass extends RecyclerView.Adapter<viewholder> {

    List<message> modelclasslist;
    String name;
    String chat;

    public Modelclass(List<message> modelclasslist) {
        this.modelclasslist = modelclasslist;
        Log.d("Inside", " Adapter,constructor");
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.l, parent, false);
        Log.d("holder", "View holder created");
        return new viewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        name = modelclasslist.get(position).getUid();
        chat = modelclasslist.get(position).getMsgg();
        viewholder.setdata(name, chat);
        Log.d("holder", "View holder Binded");
    }


    @Override
    public int getItemCount() {
       return  modelclasslist.size();
    }
}

 class viewholder extends RecyclerView.ViewHolder{

    static TextView t1 ;
    static TextView t2,t3 ;

    public viewholder(@NonNull View itemView) {
        super(itemView);

        t1 = itemView.findViewById(R.id.name);
        t2 = itemView.findViewById(R.id.chat);
        t3 = itemView.findViewById(R.id.textView);
    }
    static void setdata(String name,String chat)
    {
       t1.setText(name);
       t2.setText(chat);
        Log.d("setdata","settingdata");
        Date d = new Date();
        t3.setText((int) d.getTime());

    }
}