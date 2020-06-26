package com.example.convo;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.example.convo.Singleactivity.sendNotification;
import static com.example.convo.myfirebasemessaging.Messages;
import static com.example.convo.myfirebasemessaging.notificationbuilder;

public class closenotify extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle remote = RemoteInput.getResultsFromIntent(intent);
        final String user = intent.getStringExtra("name");
        final String phno = intent.getStringExtra("number");
        final  String suid = intent.getStringExtra("suid");
        if(remote!=null) {
            final CharSequence message = remote.getCharSequence("replymessage");
            notifidata d = new notifidata(null, message.toString());
            Messages.add(d);


            long when = System.currentTimeMillis();
            notificationbuilder(context, user, when);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //  mNotificationManager.cancel(0);
//            FirebaseFirestore fb = FirebaseFirestore.getInstance();
//            DocumentReference df = fb.collection("phonelist").document("list");
//            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot.exists()) {
//                        rud = documentSnapshot.get(phno).toString();
            final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Private chats");
            final String text = message.toString();
                     read me = new read(u.getUid(), text, suid, phno, "blah-blah",null,0,null);
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
                                            read m = new read(u.getUid(), text, suid, ph, "blah-",null,0,null);
                                            db1.child(suid).child(u.getUid()).push().setValue(m);
                                        }
                                    }
//                                String ph = documentSnapshot.getString("phno");
//                                read m = new read(u.getUid(), msg.getText().toString(), ruid,ph);
//                                db.child(ruid).child(u.getUid()).push().setValue(m);
                                }
                            }
                        });
                        db1.child(u.getUid()).child(suid).push().setValue(me);
                        //  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        //FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();

                        sendNotification(suid, text, "no photo",context,null,0);

//                    }
//                }
//            });

        }
    }
}

