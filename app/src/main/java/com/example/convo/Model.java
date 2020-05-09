//package com.example.convo;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Build;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.cardview.widget.CardView;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.sql.Time;
//import java.util.Date;
//import java.util.List;
//
//public class Model extends RecyclerView.Adapter<viewholder2> {
//
//    List<message> modelclasslist;
//    String name;
//    String chat;
//    Boolean s;
//
//    public Model(List<message> modelclasslist) {
//        this.modelclasslist = modelclasslist;
//        Log.d("Inside", " Adapter,constructor");
//    }
//
//    @NonNull
//    @Override
//    public viewholder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.m, parent, false);
//        Log.d("holder", "View holder created");
//        return new viewholder2(v);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull viewholder2 holder, int position) {
//        name = modelclasslist.get(position).getUid();
//        chat = modelclasslist.get(position).getMsgg();
//        s=modelclasslist.get(position).getS();
//        viewholder2.setdata(name, chat,s);
//        Log.d("holder", "View holder Binded");
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return  modelclasslist.size();
//    }
//}
//
//class viewholder2 extends RecyclerView.ViewHolder {
//
//    static TextView t1;
//    static TextView t2;
//    static CardView card;
//    static ConstraintLayout lay;
//
//    public viewholder2(@NonNull View itemView) {
//        super(itemView);
//
//        t2 = itemView.findViewById(R.id.chat2);
//        lay = itemView.findViewById(R.id.lay2);
//        //t3 = itemView.findViewById(R.id.textView);
//        }
//
//    static void setdata(String name, String chat, Boolean z) {
//
//        if(z==Boolean.TRUE)
//        {
//            Log.d("Set", "model data");
//        //    t2.setEms(chat.length());
//            t2.setText(chat);
//            Date d = new Date();
//            Log.d("setdata2", "settingdata");
////        t3.setText((int) d.getTime());
//
//        }
//    }
//}
