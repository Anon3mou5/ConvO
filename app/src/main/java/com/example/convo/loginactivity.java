package com.example.convo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginactivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate);
        final ImageView logo = findViewById(R.id.img);
        //logo.animate().translationY(500).setDuration(100);
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

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pair pairs[] = new  Pair[2];
                pairs[0]=new Pair<View,String>(logo,"logo");
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

                auth.signInWithEmailAndPassword(emaileditid.getText().toString(), passeditid.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginactivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(loginactivity.this, Acti.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(loginactivity.this, "UnSuccessfull LogIn", Toast.LENGTH_LONG).show();
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
            }
        });
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
        }
