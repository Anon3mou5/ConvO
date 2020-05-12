package com.example.convo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class msgmodel extends RecyclerView.Adapter<msgviewholder> {
        public  int MSG_RIGHT=0;
        public int MSG_LEFT=1;

        List<read> modelclasslist;
        String suid,phno;
        String chat,ruid;
        Boolean s;

        public msgmodel(List<read> modelclasslist) {
            this.modelclasslist = modelclasslist;
            Log.d("Inside", " Adapter,constructor");
        }

        @NonNull
        @Override
        public msgviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            int j=0;
            if(viewType==MSG_LEFT) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ls, parent, false);
                j=0;
            }
            else
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ms,parent, false);
                j=1;
            }
            Log.d("holder", "View holder created");
            return new msgviewholder(v,j);

        }

        @Override
        public void onBindViewHolder(@NonNull msgviewholder holder, int position) {
            suid = modelclasslist.get(position).getSuid();
            chat = modelclasslist.get(position).getMsg();
            ruid=modelclasslist.get(position).getRuid();
            phno=modelclasslist.get(position).getPhno();
            holder.setdata(suid, chat,ruid,phno);
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
            if(modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()))
            {
                return  MSG_RIGHT;
            }
            else
            {
                return MSG_LEFT;
            }
        }
    }

    class msgviewholder extends RecyclerView.ViewHolder {

         TextView t1;
         TextView t2;
         CardView card;
        ImageView photo;
        ConstraintLayout lay;

        public msgviewholder(@NonNull View itemView,int j) {

            super(itemView);

            t1 = itemView.findViewById(R.id.status);
            //t3 = itemView.findViewById(R.id.textView);
        }

        void setdata(String suid, String chat,String ruid,String phno) {
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

            t1.setText(chat);
            //  Bitmap b =loadImageFromStorage(MainActivity.PATH);
//            try {
//
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
//                photo.setImageBitmap(bitmap);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

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

