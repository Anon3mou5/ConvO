package com.example.convo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import android.os.Binder;
import android.util.Log;

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

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.convo.MainActivity.mdl;
import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;
import static com.example.convo.Acti.refresh;

public class myservice extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    static  List<notifidata> Messages = new ArrayList<notifidata>();
    static  HashMap<String,String> mode ;
    String ruid,suid,msg;
    static  List<chat> mdl2 ;
    HashMap<String, Long> unread = new HashMap<String, Long>();
    public class LocalBinder extends Binder {
        public myservice getService() {
            return myservice .this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Intent it = new Intent(getApplicationContext(),myservice.class);
      //  sendBroadcast(it);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//                final FirebaseAuth auth = FirebaseAuth.getInstance();
//        mode = getSavedObjectFromPreference(getApplicationContext(),"contacts","contactnumber",collectionType);
//        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
//                db.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
//                        String key = dataSnapshot.getKey();
//                        final   DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                        final ChildEventListener childevent = new ChildEventListener() {
//                            @Override
//                            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {
//
//                                model3.clear();
//                                String ruidd = "";
//                                String msg = (String) post.child("msg").getValue();
//                                final String suid = (String) post.child("suid").getValue();
//                                String ruid = (String) post.child("ruid").getValue();
//                                ruidd = ruid;
//                                String phno = (String) post.child("phno").getValue();
//                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//                                Date date = new Date();
//                                String time1=formatter.format(date);
//                                read rd = new read(suid, msg, ruid, phno,time1);
//                                model3.add(rd);
//                                List<read> md;
//                                Type collectionType = new TypeToken<List<read>>() {
//                                }.getType();
//                                if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
//                                    md = getSavedObjectFromPreference(getApplicationContext(), "preference", suid, collectionType);
//                                }
//                                else
//                                {
//                                    md = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
//                                }
//                                if (md != null && md.size() != 0) {
//                                    for (int j = 0; j < model3.size(); j++) {
//                                        read r = model3.get(j);
//                                        md.add(r);
//                                    }
//                                    if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
//                                        saveObjectToSharedPreference(getApplicationContext(), "preference", suid, md);
//                                    }
//                                    else
//                                    {
//                                        saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, md);
//                                    }
//                                } else {
//                                    if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
//                                        saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
//                                    }
//                                    else
//                                    {
//                                        saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
//                                    }
//
//                                }
//                            }
//
//                            @Override
//                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                            }
//
//                            @Override
//                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        };


        Type collectionType1 = new TypeToken<List<chat>>() {
        }.getType();

        mdl2 = getSavedObjectFromPreference(getApplicationContext(), "preference", "chatmodel", collectionType1);
        if(mdl2==null)
        {
            mdl2 = new ArrayList<chat>();
        }
        else
        {
            for(int i=0;i<mdl2.size()-1;i++)
            {
                unread.put(mdl2.get(i).getRuid(),(long) mdl2.get(i).getUnread());
            }
        }
        Type collectionType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        mode = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contactnumber", collectionType);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
//        db.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot post : dataSnapshot.getChildren()) {
//                    String key = post.getKey();
        db.addChildEventListener(new ChildEventListener() {
                                                         @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                       final String key = dataSnapshot.getKey();
                    final DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);

                                                             bd.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                     if (unread.get(key)!= null) {
                                                                         unread.put(key, dataSnapshot.getChildrenCount() + unread.get(key));
                                                                     }
                                                                     else
                                                                     {
                                                                         unread.put(key, dataSnapshot.getChildrenCount() );
                                                                     }
                                                                 }

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                 }
                                                             });

                    Query q = bd.orderByValue().limitToLast(1);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            model.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                HashMap<String, String> map = (HashMap<String, String>) data.getValue();
                                String msg = (String) map.get("msg").toString();
                                String url = (String) map.get("photo");
                                long isphoto =(long) data.child("isphoto").getValue();
                                if(isphoto==1 && url!=null)
                                {
                                    msg = "📷  Photo";
                                }

                                final String suid = (String) map.get("suid").toString();
                                final String ruid = (String) map.get("ruid").toString();
                                Log.d("ruid", "" + ruid);
                                final String phno = (String) map.get("phno").toString();
                                FirebaseFirestore ref = FirebaseFirestore.getInstance();
                                DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            final String photourl;
                                            photourl = documentSnapshot.getString("photo");

                                        }
                                    }
                                });
                                chat ch;
                                if(!suid.equals(auth.getUid())) {
                                    ch = new chat(suid, msg, ruid, "not found", phno, Integer.valueOf(String.valueOf(unread.get(suid))));
                                }
                                else
                                {
                                    ch = new chat(suid, msg, ruid, "not found", phno, 0);
                                }
                                model.add(ch);
                            }
//
                            Type collectionType = new TypeToken<List<chat>>() {
                            }.getType();

                            List<chat> mdl;
                            mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", "chatmodel", collectionType);
                            if (mdl != null && mdl.size() != 0) {
                                for (int j = 0; j < model.size(); j++) {
                                    chat r = model.get(j);
                                    for (int i = mdl.size() - 1; i >= 0; i--) {
                                        if (mdl.get(i).phno.toLowerCase().equals(r.phno.toLowerCase())) {
                                            mdl.remove(i);
                                            break;
                                        }
                                    }
                                    mdl.add(r);
                                }
                                saveObjectToSharedPreference(getApplicationContext(), "preference", "chatmodel", mdl);
                            } else {
                                saveObjectToSharedPreference(getApplicationContext()
                                        , "preference", "chatmodel", model);
                            }
                            for (chat j : model) {
                                if (!j.suid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                  //  notifidata d = new notifidata(mode.get(j.phno), j.msg);
                                   // Messages.add(d);
                                    //createNotificationChannel(j.phno, j.getMsg(), getApplicationContext(), j.suid);
                                }
                            }
refresh(getApplicationContext());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    bd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datapost) {

                            model3.clear();
                            for (DataSnapshot post : datapost.getChildren()) {
                                msg = (String) post.child("msg").getValue();
                                suid = (String) post.child("suid").getValue();
                                ruid = (String) post.child("ruid").getValue();
                                final String phno = (String) post.child("phno").getValue();
                                final String url = (String) post.child("photo").getValue();
                                int isphoto = Integer.parseInt((Long.toString((long) post.child("isphoto").getValue())));
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                Date date = new Date();
                                final String time1 = formatter.format(date);
                                if(isphoto==1 && url!=null && suid.equals(FirebaseAuth.getInstance().getUid()))
                                {

                                }
                                else {
                                    if (isphoto == 1 && url != null) {
                                        msg = "📷  Photo";
                                        if (!suid.equals(FirebaseAuth.getInstance().getUid())) {
                                            Thread nw = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        URL url1 = new URL(url);
                                                        final Bitmap bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                                                        Map<String, String> specialmap;
                                                        Uri Filepath;
                                                        specialmap = loadMap("imagenum", "number");
                                                        if (specialmap == null || specialmap.size()==0) {
                                                            specialmap = new HashMap<String, String>();
                                                            specialmap.put((phno), Double.toString(1));
                                                            saveMap(specialmap, "imagenum", "number");
                                                            storeimagetodevice y = new storeimagetodevice();
                                                            y.store(bmp, phno, Double.toString(0));
                                                            Filepath = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Convo/photos/" + phno + "/IMG_" + Double.toString(0) + ".jpg"));
                                                        } else {
                                                            storeimagetodevice y = new storeimagetodevice();
                                                            Double val = Double.valueOf(specialmap.get(phno));
                                                            y.store(bmp, phno, val.toString());
                                                            Filepath = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Convo/photos/" + phno + "/IMG_" + Double.toString(val) + ".jpg"));
                                                            specialmap.remove(phno);
                                                            val = val + 1;
                                                            specialmap.put((phno), val.toString());
                                                            saveMap(specialmap, "imagenum", "number");
                                                        }
                                                        read photodata = new read(suid, msg, ruid, phno, time1, null, 1, Filepath.toString());
                                                        model3.add(photodata);
                                                        List<read> md;
                                                        Type collectionType = new TypeToken<List<read>>() {
                                                        }.getType();
                                                        if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                                            md = getSavedObjectFromPreference(getApplicationContext(), "preference", suid, collectionType);
                                                        } else {
                                                            md = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
                                                        }
                                                        if (md != null && md.size() != 0) {
                                                            for (int j = 0; j < model3.size(); j++) {
                                                                read r = model3.get(j);
                                                                md.add(r);
                                                            }
                                                            if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                                                saveObjectToSharedPreference(getApplicationContext(), "preference", suid, md);
                                                            } else {
                                                                saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, md);
                                                            }
                                                        } else {
                                                            if (model3.size() != 0) {
                                                                if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                                                    saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
                                                                } else {
                                                                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
                                                                }
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            );
                                            nw.start();
                                        }
                                    } else {
                                        read rd = new read(suid, msg, ruid, phno, time1, url, 0, null);
                                        model3.add(rd);
                                        List<read> md;
                                        Type collectionType = new TypeToken<List<read>>() {
                                        }.getType();
                                        if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                            md = getSavedObjectFromPreference(getApplicationContext(), "preference", suid, collectionType);
                                        } else {
                                            md = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
                                        }
                                        if (md != null && md.size() != 0) {
                                            for (int j = 0; j < model3.size(); j++) {
                                                read r = model3.get(j);
                                                md.add(r);
                                            }
                                            if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                                saveObjectToSharedPreference(getApplicationContext(), "preference", suid, md);
                                            } else {
                                                saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, md);
                                            }
                                        } else {
                                            if (model3.size() != 0) {
                                                if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                                    saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
                                                } else {
                                                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
                                                }
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//db.child(key).removeValue();
                db.child(key).removeValue();

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
        });

// write your code to post content on server

        return android.app.Service.START_STICKY;
    }

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();

    }

    private void createNotificationChannel(String nme,String descr,Context params,String suid){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Intent intent = new Intent(params, Acti.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(params, 0, intent, 0);
        Drawable dr = getResources().getDrawable(R.drawable.photo);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(params);
//        builder
//                .setSmallIcon(R.mipmap.logoz)
//                .setContentTitle(nme)
//                .setContentText(descr)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setSmallIcon(R.mipmap.ic_round)
//                .setLargeIcon(((BitmapDrawable)dr).getBitmap())
//                .setContentIntent(pendingIntent).setAutoCancel(true);

        Intent intent1 = new Intent(getApplicationContext(), Acti.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

//////////////////////////////////////intent for dismiss
        Long when = System.currentTimeMillis();

        NotificationManager mNotificationManager = (NotificationManager) params.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent2 = new Intent(getApplicationContext(), closenotify.class);
        String userid = suid;

        intent2.putExtra("suid", suid);
        intent2.putExtra("number", nme);
        intent2.putExtra("name", mode.get(nme));
        //  b.putString("userid",userid);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);

        RemoteInput remoteinput = new RemoteInput.Builder("replymessage").setLabel("Your Reply...").build();
        NotificationCompat.Action replyActiion = new NotificationCompat.Action.Builder(R.drawable.send,"Reply",pendingIntent2).addRemoteInput(remoteinput).build();
        NotificationCompat.MessagingStyle messaging = new NotificationCompat.MessagingStyle("Me");
        messaging.setConversationTitle(mode.get(nme));


        for(notifidata entry : Messages ) {
            NotificationCompat.MessagingStyle.Message notificationmessage = new NotificationCompat.MessagingStyle.Message(entry.getMsg(), when, entry.getUser());
            messaging.addMessage(notificationmessage);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.decr);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
        //    builder.setChannelId("123");
        }
        Notification notifications = new NotificationCompat.Builder(params,"123").setSmallIcon(R.drawable.logo).setStyle(messaging)
                .setContentIntent(pendingIntent1).setAutoCancel(true).setCategory(NotificationCompat.CATEGORY_MESSAGE).
                        setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).addAction(replyActiion).setColor(Color.rgb(0,62,214)).setDefaults(Notification.DEFAULT_ALL).build();
        mNotificationManager.notify(0,notifications);

// notificationID allows you to update the notification later on.
    //    mNotificationManager.notify(0, builder.build());

    }


    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    static public Bitmap loadImageFromStorage(String subfolder, String path)
    {
        Bitmap b=null;
        if(subfolder==null)
        {
            subfolder="";
        }
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/"+subfolder+"/"+path+".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(dir));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    private void saveMap(Map<String,String> inputMap,String prefname,String objname){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences(prefname, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(objname).commit();
            editor.putString(objname, jsonString);
            editor.commit();
        }
    }

    private Map<String,String> loadMap(String prefname,String objname){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences(prefname, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(objname, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key,value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

}
