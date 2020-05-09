package com.example.convo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class Modelclass extends RecyclerView.Adapter<viewholder> {
    public  int MSG_RIGHT=0;
    public int MSG_LEFT=1;

    List<message> modelclasslist;
    String name;
    String chat,orguid,url;
    Boolean s;

    public Modelclass(List<message> modelclasslist) {
        this.modelclasslist = modelclasslist;
        Log.d("Inside", " Adapter,constructor");
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        int j=0;
        if(viewType==MSG_LEFT) {
           v = LayoutInflater.from(parent.getContext()).inflate(R.layout.l, parent, false);
           j=0;
        }
        else
        {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.m,parent, false);
        j=1;
        }
        Log.d("holder", "View holder created");
        return new viewholder(v,j);

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        name = modelclasslist.get(position).getUid();
        chat = modelclasslist.get(position).getMsgg();
        orguid=modelclasslist.get(position).getOrguid();
        url=modelclasslist.get(position).getUrl();
        viewholder.setdata(name, chat,orguid,url);
        Log.d("holder", "View holder Binded");
    }


    @Override
    public int getItemCount() {
       return  modelclasslist.size();
    }
    @Override
    public int getItemViewType(int position)
    {
        FirebaseAuth f = FirebaseAuth.getInstance();
        if(modelclasslist.get(position).getUid().equals(f.getCurrentUser().getDisplayName()))
        {
            return  MSG_RIGHT;
        }
        else
        {
            return MSG_LEFT;
        }
    }
}

 class viewholder extends RecyclerView.ViewHolder {

     static TextView t1;
     static TextView t2;
     static CardView card;
     static ImageView photo;
       static ConstraintLayout lay;

     public viewholder(@NonNull View itemView,int j) {


         super(itemView);

         t1 = itemView.findViewById(R.id.name);
         t2 = itemView.findViewById(R.id.chat);
         photo=itemView.findViewById(R.id.profile);
         //t3 = itemView.findViewById(R.id.textView);
         card = itemView.findViewById(R.id.cardView);

     }

     static void setdata(String name, String chat,String orguid,String url) {
                  //card.setLayoutParams(t2.getLayoutParams());
//             ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT
//

//             p.setMargins(220, 4, 0, 5);
//             card.setLayoutParams(p);
//             //card.setLayoutParams(t2.getLayoutParams());
//             lay.setBackgroundColor(Color.parseColor("#00BCD4"));
//             //card.setLayoutParams(new ConstraintLayout.LayoutParams(t2.getLayoutParams()));
//             t2.setBackgroundColor(Color.parseColor("#00BCD4"));
//             t2.setTextColor(Color.parseColor("#FFFFFF"));
//             Log.d("Set", "bg");
//             t2.setEms(chat.length());
//             t2.setText(chat);
//             Date d = new Date();
//             //         t3.setText((int)d.getTime());
//             Log.d("setdata", "settingdata");
//         }
//
//                     ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT
//             );
//             //p.setMargins(0, 4, 220, 5);
             //card.setLayoutParams(p);
            // t1.setVisibility(View.VISIBLE);
           //  lay.setBackgroundColor(Color.parseColor("#FFFFFF"));
             //card.setLayoutParams(new ConstraintLayout.LayoutParams(t2.getLayoutParams()));
            // t2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // t2.setTextColor(Color.parseColor("#000000"));
             Log.d("Set", "model data");
          //   Log.d("Set", "model data");

//         if(name.length()>chat.length()) {
//             t1.setText(name);
//             t2.setText(chat);
//             ViewGroup.LayoutParams p1 = t1.getLayoutParams();
//             int w= p1.width;
//             ViewGroup.LayoutParams p2 = t2.getLayoutParams();
//             int h=p2.height;
//             t2.setWidth(w);
//             t2.setHeight(h);
//         }
//         else {
//             t1.setText(name);
//             t2.setText(chat);
//             ViewGroup.LayoutParams p1 = t2.getLayoutParams();
//             int w= p1.width;
//             ViewGroup.LayoutParams p2 = t1.getLayoutParams();
//             int h=p2.height;
//             t1.setWidth(w);
//             t1.setHeight(h);
//         }

             t1.setText(name);
             t2.setText(chat);
             Bitmap b =loadImageFromStorage(MainActivity.PATH);
             photo.setImageBitmap(b);

//             t1.setLayoutParams(t2.getLayoutParams());
//             Date d = new Date();
             Log.d("setdata2", "settingdata");
//        t3.setText((int) d.getTime());

         }
//     public static Drawable LoadImageFromWebOperations(String url) {
//         try {
//             InputStream is = (InputStream) new URL(url).getContent();
//             Drawable d = Drawable.createFromStream(is, "src name");
//             return d;
//         } catch (Exception e) {
//             return null;
//         }


     static public Bitmap loadImageFromStorage(String path)
     {
         Bitmap b=null;
         try {
             File f=new File(path, "profile.jpg");
             b = BitmapFactory.decodeStream(new FileInputStream(f));
         }
         catch (FileNotFoundException e)
         {
             e.printStackTrace();
         }
         return b;
     }

 }
