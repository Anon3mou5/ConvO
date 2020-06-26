package com.example.convo;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaeger.library.StatusBarUtil;

public class loginactivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate);
        StatusBarUtil.setTransparent(this);
        final ImageView logo = findViewById(R.id.img);


        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);

        }

        //logoz.animate().translationY(500).setDuration(100);
        Animation b = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.transition);

        logo.animate().translationY(-600).translationX(-123).setDuration(600).setStartDelay(400).scaleX((float)0.9).scaleY((float)0.9);

        Animation a = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fading);

        final EditText passeditid = findViewById(R.id.passedit);
        final EditText emaileditid = findViewById(R.id.emailedit);
        ConstraintLayout l = findViewById(R.id.constraint);

        final ConstraintLayout l1 = findViewById(R.id.constraint2);

        l.startAnimation(a);

        Button sign = findViewById(R.id.button3);
        Button login= findViewById(R.id.button);
        final Activity activity = loginactivity.this;
        final TextView emailid = findViewById(R.id.email);
        final TextView password = findViewById(R.id.pass);
        final Intent signup = new Intent(this,signactivity.class);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        Button forgot = findViewById(R.id.forgotpassword);
        forgot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(emaileditid.getText().toString()).matches()) {
                    Toast.makeText(loginactivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "If your email was registered then,Please check your mail", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emaileditid.getText().toString());
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pair pairs[] = new  Pair[2];
                pairs[0]=new Pair<View,String>(logo,"logoz");
                pairs[1]=new Pair<View,String>(l1,"constraint");
                //   pairs[1]=new Pair<View,String>(emailid,"email");
//                pairs[2]=new Pair<View,String>(emaileditid,"emailedit");
//                pairs[3]=new Pair<View,String>(password,"pass");
//                pairs[4]=new Pair<View,String>(passeditid,"passedit");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity ,pairs);
                startActivity(signup,options.toBundle());
//                ConstraintLayout l = findViewById(R.id.constraint3);
//                Animation ab = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
//                l.startAnimation(ab);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Patterns.EMAIL_ADDRESS.matcher(emaileditid.getText().toString()).matches()) {
                    Toast.makeText(loginactivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else
                    {
                   auth.signInWithEmailAndPassword(emaileditid.getText().toString(), passeditid.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {


                               NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                   CharSequence name = getString(R.string.app_name);
                                   String description = getString(R.string.decr);
                                   int importance = NotificationManager.IMPORTANCE_HIGH;
                                   NotificationChannel channel = new NotificationChannel("123", name, importance);
                                   channel.setDescription(description);
                                   // Register the channel with the system; you can't change the importance
                                   // or other notification behaviors after this
                                   mNotificationManager.createNotificationChannel(channel);

                               }
                               NotificationCompat.MessagingStyle messaging = new NotificationCompat.MessagingStyle("Me");
                               messaging.setConversationTitle("Welcome Back User");


                     long  when =System.currentTimeMillis();
                                   NotificationCompat.MessagingStyle.Message notificationmessage = new NotificationCompat.MessagingStyle.Message("Where had you been all these time?",when,"Welcome Back User");
                                   messaging.addMessage(notificationmessage);

                               Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                               intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 0);

                               Notification notifications = new NotificationCompat.Builder(getApplicationContext(),"123").setSmallIcon(R.drawable.logo).setStyle(messaging)
                                       .setContentIntent(pendingIntent1).setAutoCancel(true).
                                               setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).setColor(Color.rgb(237,19,14)).setDefaults(Notification.DEFAULT_ALL).build();
                               mNotificationManager.notify(10,notifications);




                               FirebaseAuth usr = FirebaseAuth.getInstance();
                               String token = FirebaseInstanceId.getInstance().getToken();
                               DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Tokens");
                               Token token1 = new Token(token);
                               database.child(usr.getUid()).setValue(token1);
                               Toast.makeText(loginactivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                               Intent intent = new Intent(loginactivity.this, MainActivity.class);
                               startActivity(intent);
                               finish();
                           } else {
                               Toast.makeText(loginactivity.this, "Invalid Account or Password", Toast.LENGTH_LONG).show();
                           }
                       }
                    }
                   );
                }
            }
        });




                        /*.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Toast.makeText(loginactivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(loginactivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                );*/
//                }
//                { task ->
//                    if(task.isSuccessful) {
//                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(loginactivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish()
//    @Override
//    public void finish() {
//        super.finish();
//        ConstraintLayout l = findViewById(R.id.constraint3);
//        Animation ab = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
//        l.startAnimation(ab);
//
//
//    }
            }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Intent t = new Intent(this, exitclass.class);
                    startActivity(t);
                    finish();
                }                    // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
        }
    }
}
