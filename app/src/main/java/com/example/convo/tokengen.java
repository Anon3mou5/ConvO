package com.example.convo;

import android.media.session.MediaSession;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;


public class tokengen extends FirebaseMessagingService {
    void updatetoken(String token)
    {
        FirebaseAuth user = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token token1 = new Token(token);
        db.child(user.getUid()).push().setValue(token1);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseAuth user = FirebaseAuth.getInstance();
        if(user!=null)
        {
            String token = FirebaseInstanceId.getInstance().getToken();
            updatetoken(token);
        }
    }
}
