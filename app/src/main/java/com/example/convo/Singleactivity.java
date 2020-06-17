package com.example.convo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;
import static com.example.convo.asynch.getSavedObjectFromPreference;

public class Singleactivity extends AppCompatActivity {
    static String ruid;
    static String phonenum;
    static chat ch;
    static List<read> mdl = new ArrayList<read>();
   static  msgmodel m ;
   static List <read> mdl3= new ArrayList<read>();
   static APIService apiservice;
    boolean notify=false;
    static FirebaseAuth auth ;
    static  DatabaseReference db ;
    ChildEventListener child;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    if(child!=null && db!=null) {
        db.removeEventListener(child);
    }   finish();
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       final List<read> model = new ArrayList<read>();
      //  HashMap<String,String> contacts;
        Intent intent = getIntent();
        ruid=intent.getStringExtra("uid");
        Log.d("ruid",ruid);
        final String phno=intent.getStringExtra("phno");
        String name=intent.getStringExtra("name");
     //   contactsfetcher cf = new contactsfetcher();
    //    contacts= cf.getContactList(getApplicationContext());
         setContentView(R.layout.single_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final EditText msg = findViewById(R.id.msg);
        TextView profilename = findViewById(R.id.profilename);
    //   String name = contacts.get(phno);
       if(name==null)
       {
           name=phno;
       }
       profilename.setText(name);
        final ImageView fb = findViewById(R.id.send);



       // apiservice = Client.getclient("https://fcm.googleapis.com/").create(APIService.class);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sendrotate);
                    fb.startAnimation(anim);
                    String url;
                    final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Private chats");
                    final String text = msg.getText().toString();
                    read me = new read(u.getUid(), msg.getText().toString(), ruid, phno, "blah-blah");
                    FirebaseFirestore bd = FirebaseFirestore.getInstance();
                    CollectionReference cd = bd.collection("phonelist");
                    DocumentReference ref = cd.document("list");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> entity = documentSnapshot.getData();
                                for (Map.Entry<String, Object> ent : entity.entrySet()) {
                                    if (ent.getValue().toString().equals(u.getUid())) {
                                        String ph = ent.getKey().toString();
                                        read m = new read(u.getUid(), text, ruid, ph, "blah-");
                                        db1.child(ruid).child(u.getUid()).push().setValue(m);
                                    }
                                }
//                                String ph = documentSnapshot.getString("phno");
//                                read m = new read(u.getUid(), msg.getText().toString(), ruid,ph);
//                                db.child(ruid).child(u.getUid()).push().setValue(m);
                            }
                        }
                    });
                    db1.child(u.getUid()).child(ruid).push().setValue(me);
                    //  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    //FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    sendNotification(ruid, msg.getText().toString(), "no photo",getApplicationContext());

                }

                msg.setText("");
            }
        });



        Type collectionType = new TypeToken<List<read>>() {
        }.getType();
        mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
            if(mdl==null)
{
        mdl = new ArrayList<read>();
}
            final msgmodel m = new msgmodel(mdl);
           final  RecyclerView recycle = findViewById(R.id.singlerecycle);
            LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            recycle.setLayoutManager(lm);
            recycle.setAdapter(m);
            recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
        ((LinearLayoutManager)recycle.getLayoutManager()).setStackFromEnd(true);
            m.notifyDataSetChanged();




//        db.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {
//                // for (DataSnapshot post : dataSnapshot.getChildren()) {
////
//                // message m = (message)  post.getValue(message.class);
//                String msg = (String) post.child("msg").getValue();
//                final String suid = (String) post.child("suid").getValue();
//                String ruid = (String) post.child("ruid").getValue();
//                String phno = (String) post.child("phno").getValue();
//                read rd = new read(suid, msg, ruid, phno);
//                mdl.add(rd);
                //    }
//                        final msgmodel m = new msgmodel(model3);
//                        RecyclerView recycle = findViewById(R.id.singlerecycle);
//                        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                        lm.setOrientation(LinearLayoutManager.VERTICAL);
//                        recycle.setLayoutManager(lm);
//                        recycle.setAdapter(m);



        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(ruid);

        child = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {
                //mdl3.clear();
                String ruidd = "";
                //  for (DataSnapshot post : dataSnapshot.getChildren()) {
                // message m = (message)  post.getValue(message.class);
                String msg = (String) post.child("msg").getValue();
                final String suid = (String) post.child("suid").getValue();
                String ruid = (String) post.child("ruid").getValue();
                ruidd = ruid;
                String phno = (String) post.child("phno").getValue();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String time1=formatter.format(date);
                read rd = new read(suid, msg, ruidd, phno,time1);
                mdl.add(rd);
                recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
                m.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
db.addChildEventListener(child);

//        db.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot post : dataSnapshot.getChildren()) {
//
//                            // message m = (message)  post.getValue(message.class);
//                            String msg = (String) post.child("msg").getValue();
//                            final String suid = (String) post.child("suid").getValue();
//                            String ruid = (String) post.child("ruid").getValue();
//                            String phno = (String) post.child("phno").getValue();
//                            read rd = new read(suid, msg, ruid, phno);
//                            mdl.add(rd);
//                        }
////                        final msgmodel m = new msgmodel(model3);
////                        RecyclerView recycle = findViewById(R.id.singlerecycle);
////                        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
////                        lm.setOrientation(LinearLayoutManager.VERTICAL);
////                        recycle.setLayoutManager(lm);
////                        recycle.setAdapter(m);
//                        recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
//                        m.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//
//                });





//        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(u.getUid().toString()).child(ruid.toString());
//        //Toast.makeText(Singleactivity.this,"ruid  "+ ruid,Toast.LENGTH_SHORT).show();
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                model.clear();
//                for (DataSnapshot post : dataSnapshot.getChildren()) {
//                    // message m = (message)  post.getValue(message.class);
//                    String msg = (String) post.child("msg").getValue();
//                    final String suid = (String) post.child("suid").getValue();
//                    String ruid = (String) post.child("ruid").getValue();
//                  //  String url=(String) post.child("url").getValue();
//                    //    Boolean b = (Boolean)post.child("s").getValue();
//                    //   Log.d("NAME",""+uid.toString());{
//                   // message dz = new message(uid, msg, Boolean.TRUE, orguid,url);
//                    read dz = new read(suid,msg,ruid,phno);
//                    model.add(dz);
//                    //   Log.d("MSG", "" + msg.toString());
//                }''


//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });








        ImageView rtrn = findViewById(R.id.rtrn);
        rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Singleactivity.this,Acti.class);
                startActivity(t);
            }
        });
        }

    public static void sendNotification(final String ruid, final  String msg, final  String photo, final Context c) {
        final APIService apiservice = Client.getclient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens").child(ruid);
        Query q = ref.orderByKey().equalTo(ruid);
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference df = fb.collection("phonelist").document("list");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                              if (documentSnapshot.exists()) {
                                                  Map<String, Object> j = documentSnapshot.getData();

                                                  for (Map.Entry<String, Object> k : j.entrySet()) {
                                                      if (k.getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                                                       phonenum = k.getKey().toString();
                                                      }
                                                  }


                                              }
                                          }
                                      });

         ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String tokens = data.getValue().toString();
                    FirebaseAuth a = FirebaseAuth.getInstance();
                    data2 d = new data2(msg,phonenum,photo,ruid,a.getCurrentUser().getUid());
                    sender send = new sender(d,tokens);
                    apiservice.sendData(send).enqueue(new Callback<myrespone>() {
                        @Override
                        public void onResponse(Call<myrespone> call, Response<myrespone> response) {
//                            if (response.code() == 200) {
//                                if (response.body().success != 1) {
                                    //Toast.makeText(c, "Failed", Toast.LENGTH_SHORT).show();
                                }
//                            }
//                        }

                        @Override
                        public void onFailure(Call<myrespone> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setview() {
            Type collectionType = new TypeToken<List<read>>() {
            }.getType();
            if (getSavedObjectFromPreference(Singleactivity.this, "preference", ruid, collectionType) != null) {
                List<read> mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);

                m = new msgmodel(mdl);
                m.notifyDataSetChanged();
            }
        }

    }

