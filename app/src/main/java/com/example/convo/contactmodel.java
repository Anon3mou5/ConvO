package com.example.convo;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class contactmodel extends RecyclerView.Adapter<contactviewholder> {

private  int Present=1;
    public  int MSG_RIGHT=0;
    public int MSG_LEFT=1;

    List<Data> modelclasslist;
    String name,phno;
    String url,uid;
    int j;
     Activity a;
    public contactmodel(List<Data> modelclasslist) {
        this.modelclasslist = modelclasslist;
        setHasStableIds(true);
        Log.d("Inside", " contactAdapter,constructor");
    }

    @NonNull
    @Override
    public contactviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        int j=0;
        if(viewType==MSG_RIGHT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts, parent, false);
            j=0;
        }
        else
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactsinvisible,parent, false);
            j=1;
        }
        Log.d("holder", "View holder created");
        return new contactviewholder(v,j);

    }

    @Override
    public void onBindViewHolder(@NonNull contactviewholder holder, int position) {
        name = modelclasslist.get(position).getname();
        url=modelclasslist.get(position).geturl();
        uid=modelclasslist.get(position).getUid();
        j=modelclasslist.get(position).getFound();
        phno=modelclasslist.get(position).getPhno();
       holder.setdata(name,url,uid,phno,j);
        Log.d("holder", "View holder Binded");
    }


    @Override
    public int getItemCount() {
        return  modelclasslist.size();
    }

    public void updateList(List<Data> list){
        modelclasslist = list;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position)
    {
//        Pattern pattern;
//        Matcher matcher;
//        final String PASSWORD_PATTERN = "^[6-9](?=.*[0-9]).{9}$";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(modelclasslist.get(position).getname());

        if(modelclasslist.get(position).getFound()==1)
        {
            return  MSG_RIGHT;
        }
        else
        {    return MSG_LEFT;
        }
    }
}

class contactviewholder extends RecyclerView.ViewHolder {





    ////////////////////////////STATIC DATA CREATES DATA BEING REPEATING and only last view being updated

     TextView nameView;
     CircleImageView profile;
     ConstraintLayout lay;
    CardView cd;
    View view;
    Button invite;


    public contactviewholder(@NonNull View itemView,int j) {


        super(itemView);
         view=itemView;
        nameView= itemView.findViewById(R.id.nameView);
        // = itemView.findViewById(R.id.status);
        profile=itemView.findViewById(R.id.profile);
        //t3 = itemView.findViewById(R.id.textView);
       cd =itemView.findViewById(R.id.card);
       invite = itemView.findViewById((R.id.invite));
    }

     void setdata(final String name, final String url, final String uid, final String phno, int found) {
        //card.setLayoutParams(t2.getLayoutParams());
//             ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT
//


         if(found==0)
         {
             invite.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent sendIntent = new Intent();
                     sendIntent.setAction(Intent.ACTION_SEND);
                     sendIntent.putExtra(Intent.EXTRA_TEXT, "Lets,chat on Conv0,a one stop solution for small,secure,fast and cloud messaging.\nMoreover its simple and 6mb only.\nDownload it now at : https://www.dropbox.com/sh/xfnqdwm6seplzdp/AABzJAu9s-KvaIfb642N3IPRa?dl=0 ");
                     sendIntent.setType("text/plain");
                     Intent shareIntent = Intent.createChooser(sendIntent, "Invite through");
                     view.getContext().startActivity(shareIntent);
                 }
             });
         }
        if(found==1) {
            cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair pairs[] = new Pair[2];
                    pairs[0] = new Pair<View, String>(profile, "profile");
                    pairs[1] = new Pair<View, String>(nameView, "name");
                    //   pairs[1]=new Pair<View,String>(emailid,"email");
//                pairs[2]=new Pair<View,String>(emaileditid,"emailedit");
//                pairs[3]=new Pair<View,String>(password,"pass");
//                pairs[4]=new Pair<View,String>(passeditid,"passedit");
                    Intent t = new Intent(view.getContext(), Singleactivity.class);
                    t.putExtra("uid", uid);
                    t.putExtra("name", name);
                    t.putExtra("phno", phno);
                    BitmapDrawable drawable = (BitmapDrawable) profile.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Convo/photos/IMG_"+phno +".jpg");
                    t.putExtra("photo",dir);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);
                    view.getContext().startActivity(t, options.toBundle());
                    ((Activity) view.getContext()).finish();
                }
            });
        }
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

        nameView.setText(name);
         nameView.setPaintFlags(nameView.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        //  Bitmap b =loadImageFromStorage(MainActivity.PATH);
     Thread nw  =new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 final Bitmap bitmap = loadImageFromStorage(phno);
                 if (bitmap == null) {
                     if (url.equals("not found")) {
                     } else {
                         URL url1 = new URL(url);
                         final Bitmap bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                         ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {

                             @Override
                             public void run() {

                                 // Stuff that updates the UI
                                 storeimagetodevice y = new storeimagetodevice();
                                 y.store(bmp,"", phno);
                                 profile.setImageBitmap(bmp);

                             }
                         });
                     }
                 }
                 else
                 {
                     ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {

                         @Override
                         public void run() {

                             // Stuff that updates the UI
                             profile.setImageBitmap(bitmap);

                         }
                     });

                 }
                 }catch(MalformedURLException e){
                     e.printStackTrace();
                 } catch(IOException e){
                     e.printStackTrace();
                 }


         }
     }) ;
     nw.start();
//             t1.setLayoutParams(t2.getLayoutParams());
//             Date d = new Date();
        Log.d("contacts", "setcontacts");
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


 public Bitmap loadImageFromStorage(String path)
    {
        Bitmap b=null;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/IMG_"+path+".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(dir));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

}
