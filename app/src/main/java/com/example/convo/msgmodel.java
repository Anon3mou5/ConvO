package com.example.convo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.example.convo.MainActivity.getSavedObjectFromPreference;
import static com.example.convo.MainActivity.model3;
import static com.example.convo.MainActivity.saveObjectToSharedPreference;


public class msgmodel extends RecyclerView.Adapter<msgviewholder> {
        public  int MSG_RIGHT=0;
        public int MSG_LEFT=1;
        public  int MSG_LEFT_PHOTO=2;
    public  int MSG_RIGHT_PHOTO=3;
    public  int FILE_LEFT_PHOTO=4;
    public  int FILE_RIGHT_PHOTO=5;

        List<read> modelclasslist;
        String suid,phno;

        String chat,ruid,time,photo,profilephotourl;
        int isphoto,isfile;
        Uri bitmap;
        Boolean s;
        read model;

        public msgmodel(List<read> modelclasslist) {
            this.modelclasslist = modelclasslist;
            Log.d("Inside", " Adapter,constructor");
        }

        @Override
        public msgviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            int j=0;
            if(viewType==MSG_LEFT) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ls, parent, false);
                j=0;
            }
            else if(viewType==MSG_LEFT_PHOTO) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lphotodirect, parent, false);
            }
            else if(viewType==MSG_RIGHT_PHOTO) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mphotodirect, parent, false);
            }
            else if(viewType==FILE_RIGHT_PHOTO) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelayoutright, parent, false);
            }

            else if(viewType==FILE_LEFT_PHOTO) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelayout, parent, false);
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
            photo=modelclasslist.get(position).getPhoto();
            isphoto=modelclasslist.get(position).getIsphoto();
            isfile = modelclasslist.get(position).getIsfile();
            model = modelclasslist.get(position);
            String j = modelclasslist.get(position).getBitmappath();
            if(j!=null) {
                bitmap = Uri.parse(modelclasslist.get(position).getBitmappath());

            }holder.setdata(suid, chat,ruid,phno,model,time,photo,isphoto,bitmap,isfile);
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
            if(modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()) && modelclasslist.get(position).isphoto==1)
            {
                return  MSG_RIGHT_PHOTO;
            }
           else if(modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()))
            {
                return  MSG_RIGHT;
            }
       else if(!modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()) &&  modelclasslist.get(position).isphoto==1)
            {
                return MSG_LEFT_PHOTO;
            }
            else if(!modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()) &&  modelclasslist.get(position).isfile==1)
            {
                return FILE_LEFT_PHOTO;
            }
            else if(modelclasslist.get(position).getSuid().equals(f.getCurrentUser().getUid()) &&  modelclasslist.get(position).isfile==1)
            {
                return FILE_RIGHT_PHOTO;
            }
            else
            {
                return MSG_LEFT;
            }
        }
    }

    class msgviewholder extends RecyclerView.ViewHolder {


         TextView t1;
         TextView t2,mimetype;
         static  double retrive = 0;
         ImageView photosent;
         static int i=0;
         Context c;
         HashMap<String, String> specialmap;
         ImageView photo;
         ConstraintLayout cont;

        public msgviewholder(@NonNull View itemView,int j) {

            super(itemView);

            t1 = itemView.findViewById(R.id.status);
            mimetype=itemView.findViewById(R.id.mimetype);
            cont = itemView.findViewById(R.id.cont);
            c=itemView.getContext();
            photosent=itemView.findViewById(R.id.photosent);
            t2 = itemView.findViewById(R.id.time);

            //t3 = itemView.findViewById(R.id.textView);
        }

        void setdata(final String suid, final String chat, final String ruid, final String phno, final read model, final String time1, final String photo, int isphoto, final Uri bitmap,int isfile) {
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

            if(isfile==1)
            {
                Pattern p = Pattern.compile("/.*$");
                Matcher m = p.matcher(bitmap.toString());
                if(m.find()) {
                    mimetype.setText(m.group(1));
                    t2.setText(time1);
                }
            }
            else {

                if (isphoto == 1) {
                    if (bitmap != null) {
                        try {
                            final Bitmap photobit = MediaStore.Images.Media.getBitmap(itemView.getContext().getContentResolver(), bitmap);
                            ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI\
                                    // Stuff that updates the UI
//                                if (photobit.getHeight() > 300) {
//                                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) photosent.getLayoutParams();
//                                    photosent.setLayoutParams(new FrameLayout.LayoutParams(photobit.getWidth()+200,400 ));
//                                } else {
//                                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) photosent.getLayoutParams();
//                                    photosent.setLayoutParams(new FrameLayout.LayoutParams(photobit.getWidth()+200,220 ));
//                                }
                                    //specialmap = getSavedObjectFromPreference(itemView.getContext(),"imagenum","number",collectionType)
                                    photosent.setImageBitmap(photobit);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        //= getSavedObjectFromPreference(itemView.getContext(),"imagenum","number",collectionType);
                        Toast.makeText(itemView.getContext(), "Unable to retrive", Toast.LENGTH_LONG).show();
                    }

                }
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
                if (isphoto == 0) {
                    t1.setPaintFlags(t1.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    t1.setText(chat);
                    t1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //  Drawable d = ResourcesCompat.getDrawable(c.getResources(),R.drawable.bg2,null);
                            //     cont.setForeground(d);


                            //Drawable d = ResourcesCompat.getDrawable(c.getResources(),R.drawable.bg2,null);
                            //cont.setForeground(d);
//                                              final AlertDialog.Builder builder = new AlertDialog.Builder(c);
//                                              LayoutInflater lf= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                              View v2 = lf.inflate(R.layout.pop2,null);
//                                              final TextView cancel = v2.findViewById(R.id.cancel);
//                                              final TextView b = v2.findViewById(R.id.delete);
//                                              builder.setView(v2);
//                                              final AlertDialog dialog= builder.create();
//                                              dialog.show();
//                                              b.setOnClickListener(new View.OnClickListener() {
//                                                  @Override
//                                                  public void onClick(View v) {
//                                                      dialog.cancel();
//                                                      final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
//                                                      if (u.getUid().equals(suid)) {
//                                                          final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(u.getUid());
//
//                                                          final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Private chats").child(ruid);
//
//                                                          Query qf = db.child(ruid).orderByChild("msg").equalTo(t1.getText().toString());
//                                                          Query qf2 = db2.child(suid).orderByChild("msg").equalTo(t1.getText().toString());
//                                                          qf.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                              @Override
//                                                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                                  for (DataSnapshot d : dataSnapshot.getChildren()) {
////                                                                                          if ((d.child("msg").getValue().toString()).equals(t1.getText())) {
////                                                                                              db.child(d.toString()).removeValue();
////                                                                                              db2.child(d.toString()).removeValue();
//                                                                      d.getRef().removeValue();
//                                                                  }
//                                                              }
//
//                                                              @Override
//                                                              public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                              }
//                                                          });
//                                                          qf2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                              @Override
//                                                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                                  for (DataSnapshot d : dataSnapshot.getChildren()) {
////                                                                                          if ((d.child("msg").getValue().toString()).equals(t1.getText())) {
////                                                                                              db.child(d.toString()).removeValue();
////                                                                                              db2.child(d.toString()).removeValue();
//                                                                      d.getRef().removeValue();
//                                                                  }
//                                                              }
//
//                                                              @Override
//                                                              public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                              }
//                                                          });
//                                                          cancel.setOnClickListener(new View.OnClickListener() {
//                                                              @Override
//                                                              public void onClick(View v) {
//                                                                  dialog.cancel();
//                                                              }
//                                                          });
//
//                                                      }
//                                                  }
//                                              });

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
                                    for (i = mdl.size() - 1; i >= 0; i--) {
                                        read z = mdl.get(i);
                                        if (z.msg.equals(model.msg)) {
                                            mdl.remove(i);
                                            break;
                                        }
                                    }


                                    Type collectionType2 = new TypeToken<List<chat>>() {
                                    }.getType();
                                    List<chat> mdl2;
                                    mdl2 = getSavedObjectFromPreference(c, "preference", "chatmodel", collectionType2);
                                    if (mdl2.size() != 0) {
                                        for (int k = mdl2.size() - 1; k >= 0; k--) {
                                            chat z = mdl2.get(k);
                                            if (z.msg.toLowerCase().equals(chat.toLowerCase())) {
                                                mdl2.remove(k);
                                                FirebaseAuth u = FirebaseAuth.getInstance();
                                                chat zz = new chat(u.getCurrentUser().getUid(), mdl.get(mdl.size() - 2).msg, ruid, "not found", phno, 0);
                                                mdl2.add(zz);
                                                break;
                                            }
                                        }
                                    }
                                    final msgmodel m = new msgmodel(mdl);
                                    saveObjectToSharedPreference(c, "preference", ruid, mdl);
                                    saveObjectToSharedPreference(c, "preference", "chatmodel", mdl2);
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


                }
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
                if (isphoto == 1) {
                    photosent.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Pair pairs[] = new Pair[2];
                                                         pairs[1] = new Pair<View, String>(photosent, "name");
                                                         pairs[0] = new Pair<View, String>(t2, "profile2");
                                                         //   pairs[1]=new Pair<View,String>(emailid,"email");
//                pairs[2]=new Pair<View,String>(emaileditid,"emailedit");
//                pairs[3]=new Pair<View,String>(password,"pass");
//                pairs[4]=new Pair<View,String>(passeditid,"passedit");
                                                         Intent t = new Intent(itemView.getContext(), galleryview.class);
                                                         t.putExtra("sender", phno);
                                                         t.putExtra("when", time1);
                                                         t.putExtra("url", bitmap);
                                                         ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) itemView.getContext(), pairs);
                                                         itemView.getContext().startActivity(t, options.toBundle());


                                                     }
                                                 }
                    );
                }
            }
        }
//     public static Drawable LoadImageFromWebOperations(String url) {
//         try {
//             InputStream is = (InputStream) new URL(url).getContent();
//             Drawable d = Drawable.createFromStream(is, "src name");
//             return d;
//         } catch (Exception e) {
//             return null;
//         }


        static public Bitmap loadImageFromStorage(String subfolder,String path)
        {
            Bitmap b=null;
            if(subfolder==null)
            {
                subfolder="";
            }
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/"+subfolder+"/IMG_"+path+".jpg");
                b = BitmapFactory.decodeStream(new FileInputStream(dir));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            return b;
        }

    }

