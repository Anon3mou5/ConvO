package com.example.convo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;

public class myservice extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    static  List<notifidata> Messages = new ArrayList<notifidata>();
    Type collectionType = new TypeToken<HashMap<String, String>>() {
    }.getType();
    static  HashMap<String,String> mode ;
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
        Intent it = new Intent(getApplicationContext(),myservice.class);
        sendBroadcast(it);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
                final FirebaseAuth auth = FirebaseAuth.getInstance();
        mode = getSavedObjectFromPreference(getApplicationContext(),"contacts","contactnumber",collectionType);

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
                db.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();
                        final   DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                        final ChildEventListener childevent = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {

                                model3.clear();
                                String ruidd = "";
                                String msg = (String) post.child("msg").getValue();
                                final String suid = (String) post.child("suid").getValue();
                                String ruid = (String) post.child("ruid").getValue();
                                ruidd = ruid;
                                String phno = (String) post.child("phno").getValue();
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                Date date = new Date();
                                String time1=formatter.format(date);
                                read rd = new read(suid, msg, ruid, phno,time1);
                                model3.add(rd);
                                List<read> md;
                                Type collectionType = new TypeToken<List<read>>() {
                                }.getType();
                                if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                    md = getSavedObjectFromPreference(getApplicationContext(), "preference", suid, collectionType);
                                }
                                else
                                {
                                    md = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
                                }
                                if (md != null && md.size() != 0) {
                                    for (int j = 0; j < model3.size(); j++) {
                                        read r = model3.get(j);
                                        md.add(r);
                                    }
                                    if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                        saveObjectToSharedPreference(getApplicationContext(), "preference", suid, md);
                                    }
                                    else
                                    {
                                        saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, md);
                                    }
                                } else {
                                    if(ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                        saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
                                    }
                                    else
                                    {
                                        saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
                                    }

                                }
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
                        bd.addChildEventListener(childevent);

                        Query q = bd.orderByValue().limitToLast(1);
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                model.clear();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    HashMap<String, String> map = (HashMap<String, String>) data.getValue();
                                    final String msg = (String) map.get("msg").toString();
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
                                    ch = new chat(suid, msg, ruid, "not found", phno);
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
                                        notifidata d = new notifidata(mode.get(j.phno),j.msg);
                                        Messages.add(d);
                                        createNotificationChannel(j.phno, j.getMsg(), getApplicationContext(),j.suid);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        db.child(key).removeValue();
                    }
                    @Override
                    public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

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
                .setContentIntent(pendingIntent1).setAutoCancel(true).
                        setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).addAction(replyActiion).setColor(Color.rgb(237,19,14)).setDefaults(Notification.DEFAULT_ALL).build();
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

}