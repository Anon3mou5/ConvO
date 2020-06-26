package com.example.convo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.convo.Singleactivity.refresh;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity {

    static List<Data> model2 = new ArrayList<Data>();
    static List<chat> model = new ArrayList<chat>();
    static List<read> mdl = new ArrayList<read>();
    static  HashMap<String,String> mode ;
    static List<read> model3 = new ArrayList<read>();
    static List<read> model4 = new ArrayList<read>();
    static contactsfetcher contacts;
    static  String timings;
    static HashMap<String,String> map2;
    static int delay=50;
    static HashMap<String ,String> phuid =new HashMap<String, String>();
    chat ch;

    //  List<message> listmsg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.homepage);
        final ConstraintLayout lay = findViewById(R.id.homepage);
        final Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.faded);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }


//        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
//        }
FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {

            Intent intent = new Intent(MainActivity.this, loginactivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            Log.d("Loggedin", "Previous user");
        }

//        Type collectionType = new TypeToken<List<chat>>(){}.getType();
        //   final read zz = getSavedObjectFromPreference(getApplicationContext(), "urnum", auth.getUid(), read.class);


//        Thread k = new Thread(new Runnable() {
//            @Override
//            public void run() {


        Thread k = new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                }
                contacts = new contactsfetcher();
                map2 = contacts.getContactList(MainActivity.this);
                saveObjectToSharedPreference(MainActivity.this, "contacts", "contactnumber", map2);
                List<Data> md = null;
                Type collectionType = new TypeToken<List<Data>>() {
                }.getType();
                md = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contact", collectionType);
                if (md == null) {
                    md = new ArrayList<Data>();
                }
                if (md != null && md.size() == 0) {
                    model2.clear();
                    // final HashMap<String,String> map = contacts.getContactList(ContactsActivity.this);
                    FirebaseFirestore fb = FirebaseFirestore.getInstance();
                    DocumentReference df = fb.collection("phonelist").document("list");
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                for (final String j : map2.keySet()) {
                                    if (documentSnapshot.getString(j) != null) {
                                        final String userid = documentSnapshot.getString(j);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference zf = db.collection("userswithph").document(j);
                                        zf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                     Data d = new Data(map2.get(j), documentSnapshot.getString("photo"), 1, MainActivity.this, userid, j);
                                                     model2.add(0,d);
                                                }
                                            }
                                        });
                                      //  Data d = new Data(map2.get(j), "not found", 1, MainActivity.this, userid, j);
                                        //  phuid.put(j,userid);
                                       // model2.add(0, d);
                                    } else {
                                        Data d = new Data(map2.get(j), "not found", 0, MainActivity.this, "null", j);
                                        model2.add(d);
                                    }
                                }
                            }
//                        }
//                    });
//                    Thread rollback = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<String> az = new ArrayList<String>();
//                            while (model2.size() == 0) {
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                if (model2.size() != 0) {
//                                    break;
//                                }
//                            }
//                            for (int i = 0; i < model2.size(); i++) {
//                                Log.d("PHU", model2.get(i).getPhno());
//                                az.add(model2.get(i).phno);
//                            }
//                            for (String j : map2.keySet()) {
//                                int i;
//                                for (i = 0; i < az.size(); i++) {
//                                    if (j.toLowerCase().equals(az.get(i).toLowerCase().toString())) {
//                                        break;
//                                    }
//                                }
//                                if (i == az.size()) {
//                                    Data e = new Data(map2.get(j), "not found", 0, MainActivity.this, "null", j);
//                                    model2.add(e);
//                                }
//                            }
//                        }
//                    });
                        }
                    });
//                    rollback.start();
                    saveObjectToSharedPreference(MainActivity.this, "contacts", "contact", model2);
                } else {
                    model2 = md;
                    //                Log.d("MAPVALUzzz", "" + map2);
//                model2.clear();
//                FirebaseFirestore fb = FirebaseFirestore.getInstance();
//                DocumentReference df = fb.collection("phonelist").document("list");
//                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            for (final String j : map2.keySet()) {
//                                if (documentSnapshot.getString(j) != null) {
//                                    final String userid = documentSnapshot.getString(j);
//                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                    DocumentReference zf = db.collection("users").document(userid);
//                                    zf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                // Data d = new Data(map.get(j), documentSnapshot.getString("photo"), 1, ContactsActivity.this, userid, j);
//                                                // model.add(d);
//                                            }
//                                        }
//                                    });
//                                    Data d = new Data(map2.get(j), "not found", 1, MainActivity.this, userid, j);
//                                    model2.add(d);
//                                    phuid.put(j, userid);
//                                }
//
//                                //      Log.d("PHU", j);
//                            }
//                        }
//                    }
//                });
//                for (String j : map2.keySet()) {
//                                Data e = new Data(map2.get(j), "not found", 0, MainActivity.this, "null", j);
//                                model2.add(e);
                }
                //                Thread rollback = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> az = new ArrayList<String>();
//                        while (model2.size() == 0) {
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            if(model2.size()!=0)
//                            {
//                                break;
//                            }
//                        }
//                        for (int i = 0; i < model2.size(); i++) {
//                            Log.d("PHU", model2.get(i).getPhno());
//                            az.add(model2.get(i).phno);
//                        }
//                        for (String j : map2.keySet()) {
//                            int i;
//                            for (i = 0; i < az.size(); i++) {
//                                if (j.toLowerCase().equals(az.get(i).toLowerCase().toString())) {
//                                    break;
//                                }
//                            }
//                            if (i == az.size()) {
//                                Data e = new Data(map2.get(j), "not found", 0, MainActivity.this, "null", j);
//                                model2.add(e);
//                            }
//                        }
//                    }
//                });
//                rollback.start();
            }
        });
        k.start();
        k.setPriority(10);

        startService(getApplicationContext());

        Thread timer1 = new Thread(new Runnable() {
            Timer timer = new Timer();
            @Override
            public void run() {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        DatabaseReference status = FirebaseDatabase.getInstance().getReference("STATUS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status");
                        status.setValue(1);
                        DatabaseReference time= FirebaseDatabase.getInstance().getReference("STATUS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("time");
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        String time1 = formatter.format(date);
                        time.setValue(time1);
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            }
            }
        );
                timer1.start();

//        Type collectionType = new TypeToken<HashMap<String, String>>() {
//        }.getType();
//        mode = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contactnumber", collectionType);
//
//        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
//        db.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot post : dataSnapshot.getChildren()) {
//                    String key = post.getKey();
//                    final DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                    bd.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot datapost) {
//
//                            model3.clear();
//                            String ruuid =null ;
//                            for (DataSnapshot post : datapost.getChildren()) {
//                                String msg = (String) post.child("msg").getValue();
//                                suid = (String) post.child("suid").getValue();
//                                ruid = (String) post.child("ruid").getValue();
//                                ruuid =ruid;
//                                String phno = (String) post.child("phno").getValue();
//                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//                                Date date = new Date();
//                                String time1 = formatter.format(date);
//                                read rd = new read(suid, msg, ruid, phno, time1);
//                                model3.add(rd);
//                            List<read> md;
//                            Type collectionType = new TypeToken<List<read>>() {
//                            }.getType();
//                            if (ruuid.equals(FirebaseAuth.getInstance().getUid())) {
//                                md = getSavedObjectFromPreference(getApplicationContext(), "preference", suid, collectionType);
//                            } else {
//                                md = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
//                            }
//                            if (md != null && md.size() != 0) {
//                                for (int j = 0; j < model3.size(); j++) {
//                                    read r = model3.get(j);
//                                    md.add(r);
//                                }
//                                if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
//                                    saveObjectToSharedPreference(getApplicationContext(), "preference", suid, md);
//                                } else {
//                                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, md);
//                                }
//                            } else {
//                                if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
//                                    saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
//                                } else {
//                                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
//                                }
//                            }
//
//                            }
//                            refresh(getApplicationContext());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    Query q = bd.orderByValue().limitToLast(1);
//                    q.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            model.clear();
//                            for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                HashMap<String, String> map = (HashMap<String, String>) data.getValue();
//                                final String msg = (String) map.get("msg").toString();
//                                final String suid = (String) map.get("suid").toString();
//                                final String ruid = (String) map.get("ruid").toString();
//                                Log.d("ruid", "" + ruid);
//                                final String phno = (String) map.get("phno").toString();
//                                FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                                DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
//                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        if (documentSnapshot.exists()) {
//                                            final String photourl;
//                                            photourl = documentSnapshot.getString("photo");
//                                        }
//                                    }
//                                });
//                                chat ch;
//                                ch = new chat(suid, msg, ruid, "not found", phno);
//                                model.add(ch);
//                            }
////
//                            Type collectionType = new TypeToken<List<chat>>() {
//                            }.getType();
//                            List<chat> mdl;
//                            mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", "chatmodel", collectionType);
//                            if (mdl != null && mdl.size() != 0) {
//                                for (int j = 0; j < model.size(); j++) {
//                                    chat r = model.get(j);
//                                    for (int i = mdl.size() - 1; i >= 0; i--) {
//                                        if (mdl.get(i).phno.toLowerCase().equals(r.phno.toLowerCase())) {
//                                            mdl.remove(i);
//                                            break;
//                                        }
//                                    }
//                                    mdl.add(r);
//                                }
//                                saveObjectToSharedPreference(getApplicationContext(), "preference", "chatmodel", mdl);
//                            } else {
//                                saveObjectToSharedPreference(getApplicationContext()
//                                        , "preference", "chatmodel", model);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    db.child(key).removeValue();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

















// final HashMap<String,String> map = contacts.getContactList(ContactsActivity.this);
//startService(getApplicationContext());

//                    if (zz != null) {
//                        if (!zz.phno.toLowerCase().equals(j)) {
//                            model2.add(e);
//                        }
//                        //   Log.d("XYZ",""+model.);
//                    }
//                    else
//                    {

//                }

//            }
//        });
//        k.start();
//        k.setPriority(10);
        //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
   //     startService(getApplicationContext());

        // final Thread z = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
//                db.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
//                        model.clear();
//                        String key = dataSnapshot.getKey();
//                        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                        Query q = bd.orderByValue().limitToLast(1);
//                        q.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    HashMap<String, String> map = (HashMap<String, String>) data.getValue();
//                                    final String msg = (String) map.get("msg").toString();
//                                    final String suid = (String) map.get("suid").toString();
//                                    final String ruid = (String) map.get("ruid").toString();
//                                    Log.d("ruid", "" + ruid);
//                                    final String phno = (String) map.get("phno").toString();
//                                    FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                                    DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
//                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                final String photourl;
//                                                photourl = documentSnapshot.getString("photo");
//
//                                            }
//                                        }
//                                    });
//                                    ch = new chat(suid, msg, ruid, "not found", phno);
//                                    model.add(ch);
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        model.clear();
//                        String key = dataSnapshot.getKey();
//                        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                        Query q = bd.orderByKey().limitToLast(1);
//                        q.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    HashMap<String, String> map = (HashMap<String, String>) data.getValue();
//                                    final String msg = (String) map.get("msg").toString();
//                                    final String suid = (String) map.get("suid").toString();
//                                    final String ruid = (String) map.get("ruid").toString();
//                                    Log.d("ruid", "" + ruid);
//                                    final String phno = (String) map.get("phno").toString();
//                                    FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                                    DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
//                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                final String photourl;
//                                                photourl = documentSnapshot.getString("photo");
//
//                                            }
//                                        }
//                                    });
//                                    ch = new chat(suid, msg, ruid, "not found", phno);
//                                    model.add(ch);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//        z.start();
        final ImageView img = findViewById(R.id.img);
        img.setVisibility(View.VISIBLE);
        img.animate().alpha(1).translationY(-135).setDuration(300);
        Handler handler2 = new Handler(
        );

        handler2.postDelayed(new Runnable() {
            public void run() {
//                while(model2.size()==0)
//                {
//                    if(model2.size()!=0)
//                    {
//                        break;
//                    }
//                    try {
//                        Thread.sleep(200);
//                        delay=delay+200;
//                        Log.d("SLEPT",""+delay);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                img.animate().alpha(0).translationY(135).setDuration(300);
            }
        },3250);

//        TimerTask tim = new TimerTask() {
//            @Override
//            public void run() {
//                synchronized(MainActivity.obj)
//                {
//                    MainActivity.obj.notify();
//                }
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(tim,2000,1000);
//        synchronized(obj)
//        {
//            //this thread waits until i reaches 4
//            try {
//                obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                                           @Override
//                                           public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                               if (!task.isSuccessful()) {
//                                                   Log.d("idk", "getInstanceId failed", task.getException());
//                                                   return;
//                                               }
//
//                                               // Get new Instance ID token
//                                               String token = task.getResult().getToken();
//
//                                               // Log and toast
//                                               String msg = token;
//                                               Log.d("idk", msg);
//                                               Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                           }
//
//                                           public void onNewToken(String token) {
//                                               Log.d("idk", "Refreshed token: " + token);
//
//                                               // If you want to send messages to this application instance or
//                                               // manage this apps subscriptions on the server side, send the
//                                               // Instance ID token to your app server.
//                                               //sendRegistrationToServer(token);
//                                           }
//                                       }
//                );

//      Thread imp = new Thread(new Runnable() {
//          @Override
//          public void run() {
//              FirebaseAuth user = FirebaseAuth.getInstance();
//              DatabaseReference data = FirebaseDatabase.getInstance().getReference("Private chats").child(user.getUid());
//              data.addValueEventListener(new ValueEventListener() {
//                  @Override
//                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                  }
//
//                  @Override
//                  public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                  }
//              });
//          }
//      });


//        Thread work = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//        work.setPriority(2);
//        work.start();

//    }


        setup(lay,a);

        }

    public void startService(Context context) {
       breceiver br = new breceiver();
       Intent it = new Intent(context,myservice.class);
       br.onReceive(context,it);
    }


   void setup(final ConstraintLayout lay,final Animation a)
   {

       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           public void run() {
               Intent in = new Intent(MainActivity.this, Acti.class);
               lay.startAnimation(a);
               startActivity(in);
               overridePendingTransition(0, 0);
               finish();
           }
       }, 3400);

   }

//    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
//        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
//        final Gson gson = new Gson();
//        String serializedObject = gson.toJson(object);
//        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
//        sharedPreferencesEditor.apply();
//        Acti t = new Acti();
//        t.setview();
//        Singleactivity t2 = new
//                Singleactivity();
//        t2.setview();
//    }
//
//    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
//        if (sharedPreferences.contains(preferenceKey)) {
//            final Gson gson = new Gson();
//            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
//        }
//        return null;
//    }


///////////////////////////////////////////MENU
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//       //  Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//
//
    ////////////////////////////////////////////Item SELECTED from TOOLBAR
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.logout:
//                   Intent t = new Intent(MainActivity.this,loginactivity.class);
//                   FirebaseAuth c = FirebaseAuth.getInstance();
//                   c.signOut();
//                   startActivity(t);
//                   return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    ///////////////////////////SAVE_TO_MEMORY
    private String saveToInternalStorage(Bitmap bitmapImage,String dir,String path){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(dir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,path);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            //bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();

    }

    private void createNotificationChannel(String nme,String descr,Context params){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Intent intent = new Intent(params, Acti.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(params, 0, intent, 0);
        Drawable dr = getResources().getDrawable(R.drawable.photo);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(params);
        builder
                .setSmallIcon(R.mipmap.logoz)
                .setContentTitle(nme)
                .setContentText(descr)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_round)
                .setLargeIcon(((BitmapDrawable)dr).getBitmap())
                .setContentIntent(pendingIntent).setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) params.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.decr);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId("123");
        }

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, builder.build());

    }


    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }


}

