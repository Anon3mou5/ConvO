package com.example.convo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.convo.MainActivity.getSavedObjectFromPreference;
import static com.example.convo.Singleactivity.refresh;

public class myfirebasemessaging extends FirebaseMessagingService {
    static PendingIntent pendingIntent1,pendingIntent2;
    static String usernumcopy;
    String suid,ruid;
    static int i;

   HashMap<String,String> mode ;
   List<read> model3 = new ArrayList<read>();
   List<chat> model = new ArrayList<chat>();
    @Override

    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    //String msg = remoteMessage.getData().get("send");

        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        //String uid = auth.getUid();
       // if(auth!= null && msg.equals(auth.getUid()))
       // {
            sendNotification(remoteMessage);
          //  dataload();
        //}
    }

    static  List<notifidata> Messages = new ArrayList<notifidata>();

    private void sendNotification(RemoteMessage remoteMessage) {
        String usernum = remoteMessage.getData().get("sender");
        String suid = remoteMessage.getData().get("suid");
        String user = null;
        String icon = remoteMessage.getData().get("profilephoto");
        String msg = remoteMessage.getData().get("msg");
        int isphoto = Integer.parseInt(remoteMessage.getData().get("isphoto"));
        String url = remoteMessage.getData().get("photo");

        if(isphoto==1 && url!=null)
        {
            msg = "📷  Photo";
        }
        contactsfetcher contacts = new contactsfetcher();
        HashMap<String, String> map2;
        Type collectionType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        map2 = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contactnumber", collectionType);
        //map2 = contacts.getContactList(getApplicationContext());
        if (map2 != null) {
                    user = map2.get(usernum);
        }
        if (user == null) {
            user = usernum;
        }
        if (usernumcopy == null) {
            usernumcopy = usernum;
            notifidata d = new notifidata(user, msg);
            Messages.add(d);
        } else {
            if (usernumcopy.equals(usernum)) {
                notifidata d = new notifidata(user, msg);
                Messages.add(d);
            } else {
                Messages.clear();
                notifidata d = new notifidata(user, msg);
                Messages.add(d);
                NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(i);
                i=i+1;
            }
        }

        int icon2 = R.mipmap.logo;
        long when = System.currentTimeMillis();
        //Notification notification2 = new Notification(icon2, "Conv0", when);
        // RemoteViews contentView;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

//////////////////////////////////////intent for dismiss
        Intent intent2 = new Intent(getApplicationContext(), closenotify.class);
        String userid = suid;
        intent2.putExtra("suid", suid);
        intent2.putExtra("number", usernum);
        intent2.putExtra("name", user);
        //  b.putString("userid",userid);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);


        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Conv0";
            String description = "Bridge the need";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);

        }

          notificationbuilder(getApplicationContext(),user,when);
        //////////////////////////////////intent for  acti.cclass

        //       notification2.contentView = contentView;
//        notification2.contentIntent = pendingIntent2;
//        notification2.priority |= Notification.PRIORITY_MAX;
//        notification2.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification2.defaults |= Notification.DEFAULT_LIGHTS; // LED
//        notification2.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
//        notification2.defaults |= Notification.DEFAULT_SOUND;


//        Drawable dr = getResources().getDrawable(R.drawable.photo);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder
//                .setSmallIcon(R.mipmap.logo)
//               .setContent(contentView)
////                .setContentTitle(user)
////               .setContentText(msg)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_ALL)

//                .setLargeIcon(((BitmapDrawable)dr).getBitmap())
        //.addAction(R.id.reply,"reply",pendingIntent1)
        //.addAction(R.id.dismiss,"dismiss",pendingIntent2)
        // .setContentIntent(pendingIntent1).setAutoCancel(true);


//
//            builder.setChannelId("123");
//        }
//        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder
//                .setSmallIcon(R.mipmap.logo)
//            //   .setContent(contentView)
//                .setContentTitle(user)
//               .setContentText(msg)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_ALL).setColor(Color.YELLOW)
//        //        .setLargeIcon(((BitmapDrawable)dr).getBitmap())
//               .addAction(R.id.reply,"Reply",pendingIntent1)
//               .setContentIntent(pendingIntent1).setAutoCancel(true);
//        builder.setChannelId("123");
//    mNotificationManager.notify(1,builder.build());
//    }
    }


       // mNotificationManager.notify(1, notification2);
    //    if(i>j)
          //  j=i;
       // }
// notificationID allows you to update the notification later on.
     //   mNotificationManager.notify(j, builder.build());




  public static void notificationbuilder(Context c,String user,long when)
   {
//
//        contentView = new RemoteViews(getPackageName(), R.layout.notification);
//       contentView.setTextViewText(R.id.title,user);
//       contentView.setTextViewText(R.id.text,msg);
//       contentView.setTextViewText(R.id.time,Long.toString(when));
//       contentView.setOnClickPendingIntent(R.id.reply,pendingIntent1);
//       contentView.setOnClickPendingIntent(R.id.dismiss,pendingIntent2);


       NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);


       RemoteInput remoteinput = new RemoteInput.Builder("replymessage").setLabel("Your Reply...").build();
       NotificationCompat.Action replyActiion = new NotificationCompat.Action.Builder(R.drawable.send,"Reply",pendingIntent2).addRemoteInput(remoteinput).build();
       NotificationCompat.MessagingStyle messaging = new NotificationCompat.MessagingStyle("Me");
       messaging.setConversationTitle(user);


       for(notifidata entry : Messages ) {
           NotificationCompat.MessagingStyle.Message notificationmessage = new NotificationCompat.MessagingStyle.Message(entry.getMsg(), when, entry.getUser());
           messaging.addMessage(notificationmessage);
       }
       Bitmap bitmap = loadImageFromStorage(usernumcopy);
       Notification notifications = new NotificationCompat.Builder(c,"123").setSmallIcon(R.drawable.logo).setStyle(messaging)
             .setAutoCancel(true).
                       setCategory(NotificationCompat.EXTRA_MESSAGES).setContentIntent(pendingIntent1).setLargeIcon(bitmap).
                       setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).addAction(replyActiion).setColor(Color.rgb(237,19,14)).setDefaults(Notification.DEFAULT_ALL).build();
       mNotificationManager.notify(i,notifications);

   }

    public static Bitmap loadImageFromStorage(String path)
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


    public void dataload()
{
    Type collectionType = new TypeToken<HashMap<String, String>>() {
    }.getType();
    mode = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contactnumber", collectionType);
   final FirebaseAuth auth = FirebaseAuth.getInstance();
    final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
    db.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot post : dataSnapshot.getChildren()) {
                String key = post.getKey();
                final DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                bd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datapost) {

                        model3.clear();
                        for (DataSnapshot post : datapost.getChildren()) {
                            String msg = (String) post.child("msg").getValue();
                            suid = (String) post.child("suid").getValue();
                            ruid = (String) post.child("ruid").getValue();
                            String phno = (String) post.child("phno").getValue();
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                            Date date = new Date();
                            String time1 = formatter.format(date);
                            read rd = new read(suid, msg, ruid, phno, time1,null,0,null);
                            model3.add(rd);
                        }
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
                            if (ruid.equals(FirebaseAuth.getInstance().getUid())) {
                                saveObjectToSharedPreference(getApplicationContext(), "preference", suid, model3);
                            } else {
                                saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, model3);
                            }

                        }
                        refresh(getApplicationContext());
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
                            ch = new chat(suid, msg, ruid, "not found", phno,0);
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                db.child(key).removeValue();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();

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