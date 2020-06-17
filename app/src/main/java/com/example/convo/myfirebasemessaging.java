package com.example.convo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.convo.MainActivity.getSavedObjectFromPreference;

public class myfirebasemessaging extends FirebaseMessagingService {
    static PendingIntent pendingIntent1,pendingIntent2;
    static String usernumcopy;
    static int i;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    //String msg = remoteMessage.getData().get("send");
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        //String uid = auth.getUid();
       // if(auth!= null && msg.equals(auth.getUid()))
       // {
            sendNotification(remoteMessage);
        //}
    }

    static  List<notifidata> Messages = new ArrayList<notifidata>();

    private void sendNotification(RemoteMessage remoteMessage) {
 String usernum=remoteMessage.getData().get("sender");
 String suid = remoteMessage.getData().get("suid");
 String user = null;
        String icon = remoteMessage.getData().get("photo");
        String msg = remoteMessage.getData().get("msg");
        contactsfetcher contacts = new contactsfetcher();
        HashMap<String,String> map2;
        Type collectionType = new TypeToken<HashMap<String,String>>() {
        }.getType();
        map2 = getSavedObjectFromPreference(getApplicationContext(), "contacts", "contactnumber", collectionType);
       //map2 = contacts.getContactList(getApplicationContext());
        if(map2!=null) {
            for (String j : map2.keySet()) {
                if (j.toLowerCase().equals(usernum.toLowerCase())) {
                    user = map2.get(j);
                    break;
                }
            }
        }
     if(user==null)
     {
         user=usernum;
     }
        if(usernumcopy==null)
        {
            usernumcopy = usernum;
            notifidata d = new notifidata(user,msg);
            Messages.add(d);
        }
        else
        {
            if(usernumcopy.equals(usernum))
            {
                notifidata d = new notifidata(user,msg);
                Messages.add(d);
            }
            else
            {
                i+=1;
            }
        }

        int icon2 = R.mipmap.logo;
        long when = System.currentTimeMillis();
      //Notification notification2 = new Notification(icon2, "Conv0", when);
       // RemoteViews contentView;

        Intent intent = new Intent(getApplicationContext(), Acti.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

//////////////////////////////////////intent for dismiss
        Intent intent2 = new Intent(getApplicationContext(), closenotify.class);
        String userid=suid;
        intent2.putExtra("suid",suid);
        intent2.putExtra("number",usernum);
        intent2.putExtra("name",user);
      //  b.putString("userid",userid);
       // intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.decr);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

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

       // mNotificationManager.notify(1, notification2);
    //    if(i>j)
          //  j=i;
       // }
// notificationID allows you to update the notification later on.
     //   mNotificationManager.notify(j, builder.build());




    }
  public static void notificationbuilder(Context c,String user,long when)
   {


       NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);


//
//        contentView = new RemoteViews(getPackageName(), R.layout.notification);
//       contentView.setTextViewText(R.id.title,user);
//       contentView.setTextViewText(R.id.text,msg);
//       contentView.setTextViewText(R.id.time,Long.toString(when));
//       contentView.setOnClickPendingIntent(R.id.reply,pendingIntent1);
//       contentView.setOnClickPendingIntent(R.id.dismiss,pendingIntent2);




       RemoteInput remoteinput = new RemoteInput.Builder("replymessage").setLabel("Your Reply...").build();
       NotificationCompat.Action replyActiion = new NotificationCompat.Action.Builder(R.drawable.send,"Reply",pendingIntent2).addRemoteInput(remoteinput).build();
       NotificationCompat.MessagingStyle messaging = new NotificationCompat.MessagingStyle("Me");
       messaging.setConversationTitle(user);


       for(notifidata entry : Messages ) {
           NotificationCompat.MessagingStyle.Message notificationmessage = new NotificationCompat.MessagingStyle.Message(entry.getMsg(), when, entry.getUser());
           messaging.addMessage(notificationmessage);
       }

       Notification notifications = new NotificationCompat.Builder(c,"123").setSmallIcon(R.drawable.logo).setStyle(messaging)
               .setContentIntent(pendingIntent1).setAutoCancel(true).
                       setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).addAction(replyActiion).setColor(Color.rgb(17,139,234)).setDefaults(Notification.DEFAULT_ALL).build();
       mNotificationManager.notify(0,notifications);
   }
}
