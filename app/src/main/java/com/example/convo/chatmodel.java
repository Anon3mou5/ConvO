package com.example.convo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.convo.MainActivity.getSavedObjectFromPreference;
import static com.example.convo.MainActivity.map2;
import static com.example.convo.MainActivity.mode;
import static com.example.convo.MainActivity.saveObjectToSharedPreference;

public class chatmodel extends RecyclerView.Adapter<chatviewholder> {
    public int MSG_RIGHT = 0;
    public int MSG_LEFT = 1;

    public List<chat> modelclasslisting;
    String suid,phno;
    String chat, ruid, url;
    int unread;
    Boolean s;

    public chatmodel(List<chat> modelclasslist) {
        this.modelclasslisting = modelclasslist;
        setHasStableIds(true);
        Log.d("Inside", " Adapter,constructor");
    }

    @NonNull
    @Override
    public chatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        int j = 0;
        if (viewType == MSG_LEFT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newmessage, parent, false);
            j=0;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lastmessage, parent, false);
        } Log.d("holder", "View holder created");
            return new chatviewholder(v, j);

    }

    @Override
    public void onBindViewHolder(@NonNull chatviewholder holder, int position) {
        suid = modelclasslisting.get(position).getSuid();
        chat = modelclasslisting.get(position).getMsg();
        ruid = modelclasslisting.get(position).getRuid();
        url = modelclasslisting.get(position).getUrl();
        phno=modelclasslisting.get(position).getPhno();
        unread=modelclasslisting.get(position).getUnread();
        try {
            holder.setdata(suid, chat, ruid, url,phno,unread,modelclasslisting.get(position));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("holder", "View holder Binded");
    }


    @Override
    public int getItemCount() {
        return modelclasslisting.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    public int getItemViewType(int position)
    {
        FirebaseAuth f = FirebaseAuth.getInstance();
        if(modelclasslisting.get(position).getUnread()==0)
        {
            return  MSG_RIGHT;
        }
        else
        {
            return MSG_LEFT;
        }
    }
}

class chatviewholder extends RecyclerView.ViewHolder {

     TextView t1;
     TextView t2,count;
     CardView card;
     CircleImageView profile;
     View view;
     String name=null;

    public chatviewholder(@NonNull View itemView,int j) {

        super(itemView);

        t1 = itemView.findViewById(R.id.naam);
        t2= itemView.findViewById(R.id.status);
        count = itemView.findViewById(R.id.counter);
        profile=itemView.findViewById(R.id.profile);
        view = itemView;
        card = itemView.findViewById(R.id.card);

        //t3 = itemView.findViewById(R.id.textView);
    }

    void setdata(final String suid, final String chat,final String ruid,final String url,final String phno,final int unread,final chat model) throws InterruptedException {
        //card.setLayoutParams(t2.getLayoutParams());
//             ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                     ConstraintLayout.LayoutParams.WRAP_CONTENT
//

        t1.setPaintFlags(t1.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        t2.setPaintFlags(t1.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));


        if(unread!=0)
        {
            count.setText(unread+"");
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
//        FirebaseFirestore fb = FirebaseFirestore.getInstance();
//        DocumentReference df = fb.collection("users").document(ruid);
//        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists())
//                {
//                    String phone
//                }
//            }
//        })
while(map2==null)
{
    Thread.currentThread().sleep(500);
}

         name=map2.get(phno);
         if(name==null)
         {
             name=phno;
         }
          t2.setText(chat);
          t1.setText(name);

        //  Bitmap b =loadImageFromStorage(MainActivity.PATH);
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
//                photo.setImageBitmap(bitmap);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(unread!=0) {
                        count.setVisibility(View.INVISIBLE);
                        model.setUnread(0);
                        Type collectionType1 = new TypeToken<List<chat>>() {
                        }.getType();
                        List<chat> z = getSavedObjectFromPreference(itemView.getContext(),"preference","chatmodel",collectionType1);
                        for(int i=0;i<z.size();i++)
{
    if(z.get(i).msg.equals(chat) && z.get(i).getRuid().equals(ruid))
    {
        z.get(i).setUnread(0);
    break;
    }
    saveObjectToSharedPreference(itemView.getContext(),"preference","chatmodel",collectionType1);
}
                    }
                    Pair pairs[] = new  Pair[2];
                    pairs[0]=new Pair<View,String>(profile,"profile");
                    pairs[1]=new Pair<View,String>(t1,"name");
                    //   pairs[1]=new Pair<View,String>(emailid,"email");
//                pairs[2]=new Pair<View,String>(emaileditid,"emailedit");
//                pairs[3]=new Pair<View,String>(password,"pass");
//                pairs[4]=new Pair<View,String>(passeditid,"passedit");
                    Intent t = new Intent(view.getContext(),Singleactivity.class);
                    if(ruid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()))
                    {
                        t.putExtra("uid",suid);
                    }
                    else {
                        t.putExtra("uid", ruid);

                    }t.putExtra("name",name);
                    t.putExtra("phno",phno);
                    BitmapDrawable drawable = (BitmapDrawable) profile.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Convo/photos/IMG_"+phno +".jpg");
                    t.putExtra("photo",dir);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( (Activity) view.getContext(),pairs);
                    view.getContext().startActivity(t,options.toBundle());
                }
            });

//             t1.setLayoutParams(t2.getLayoutParams());
//             Date d = new Date();
        Log.d("setdata2", "settingdata");
//        t3.setText((int) d.getTime());

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
                                    y.store(bmp,"",phno);
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
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/"+path+".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(dir));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

}

