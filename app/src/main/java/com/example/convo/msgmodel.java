package com.example.convo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.convo.asynch.getSavedObjectFromPreference;
import static com.example.convo.asynch.saveObjectToSharedPreference;

public class msgmodel extends RecyclerView.Adapter<msgviewholder> {
        public  int MSG_RIGHT=0;
        public int MSG_LEFT=1;

        List<read> modelclasslist;
        String suid,phno;
        String chat,ruid,time;
        Boolean s;
        read model;

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
            time=modelclasslist.get(position).getTime();
            model = modelclasslist.get(position);
            holder.setdata(suid, chat,ruid,phno,model,time);
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
         static int i=0;
         Context c;
        ImageView photo;
        ConstraintLayout cont;

        public msgviewholder(@NonNull View itemView,int j) {

            super(itemView);

            t1 = itemView.findViewById(R.id.status);
            cont = itemView.findViewById(R.id.cont);
            c=itemView.getContext();
            t2 = itemView.findViewById(R.id.time);

            //t3 = itemView.findViewById(R.id.textView);
        }

        void setdata(final String suid, final String chat, final String ruid, final String phno, final read model,String time1) {
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
            t2.setText(time1);
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


            t1.setOnLongClickListener(new View.OnLongClickListener() {
                                          @Override
                                          public boolean onLongClick(View v) {

                                              //  Drawable d = ResourcesCompat.getDrawable(c.getResources(),R.drawable.bg2,null);
                                              //     cont.setForeground(d);
                                              final AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                              LayoutInflater lf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                              View v2 = lf.inflate(R.layout.pop2, null);
                                              final TextView cancel = v2.findViewById(R.id.cancel);
                                              final TextView b = v2.findViewById(R.id.delete);
                                              builder.setView(v2);
                                              final AlertDialog dialog = builder.create();
                                              dialog.show();
                                              b.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      dialog.cancel();
                                                      List<read> mdl;
                                                      Type collectionType = new TypeToken<List<read>>() {
                                                      }.getType();
                                                      mdl = getSavedObjectFromPreference(c, "preference", ruid, collectionType);
                                                      for (i=mdl.size()-1;i>=0;i--) {
                                                          read z=mdl.get(i);
                                                          if (z.msg.equals(model.msg)) {
                                                              mdl.remove(i);
                                                              break;
                                                          }
                                                      }


                                                      Type collectionType2 = new TypeToken<List<chat>>() {
                                                      }.getType();
                                                      List<chat>mdl2;
                                                      mdl2 = getSavedObjectFromPreference(c, "preference", "chatmodel", collectionType2);
                                                      if(mdl2.size()!=0) {
                                                          for(int k= mdl2.size() - 1; k >= 0; k--) {
                                                              chat z = mdl2.get(k);
                                                              if (z.msg.toLowerCase().equals(chat.toLowerCase())) {
                                                                  mdl2.remove(k);
                                                                  FirebaseAuth u = FirebaseAuth.getInstance();
                                                                  chat zz = new chat(u.getCurrentUser().getUid(), mdl.get(mdl.size() - 2).msg, ruid, "not found", phno);
                                                                  mdl2.add(zz);
                                                                  break;
                                                              }
                                                          }
                                                      }
                                                      final msgmodel m = new msgmodel(mdl);
                                                      saveObjectToSharedPreference(c,"preference",ruid,mdl);
                                                      saveObjectToSharedPreference(c,"preference","chatmodel",mdl2);
                                                      final RecyclerView recycle = ((Activity) c).findViewById(R.id.singlerecycle);
                                                      LinearLayoutManager lm = new LinearLayoutManager(c);
                                                      lm.setOrientation(LinearLayoutManager.VERTICAL);
                                                      recycle.setLayoutManager(lm);
                                                      recycle.setAdapter(m);
                                                      recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
                                                      m.notifyDataSetChanged();
                                                  }
                                              });

                                              cancel.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      dialog.cancel();
                                                  }
                                              });
                                              return true;
                                          }


                                              });




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

