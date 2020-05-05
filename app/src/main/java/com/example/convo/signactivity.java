package com.example.convo;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signactivity extends AppCompatActivity {
//    final EditText name = findViewById(R.id.nameedit);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);


        final  EditText email = findViewById(R.id.emailedit);

        // TextView emailindication = findViewById(R.id.emailindication);

        final EditText pass = findViewById(R.id.passedit);

        final TextView indication = findViewById(R.id.passindication);

        final EditText confirmpass = findViewById(R.id.confirmpassedit);

        ConstraintLayout l = findViewById(R.id.constraint3);

        Button signin = findViewById(R.id.signin);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition);
        l.startAnimation(a);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indication.setVisibility(TextView.VISIBLE);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().length()==0)
                {
                    Toast.makeText(signactivity.this,"Email cannot be empty",Toast.LENGTH_SHORT).show();
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    Toast.makeText(signactivity.this,"Invalid email format",Toast.LENGTH_SHORT).show();
                }

                if(isPasswordValidMethod(pass.getText().toString())==false) {
                    Toast.makeText(signactivity.this, "Invalid format", Toast.LENGTH_SHORT).show();
                }
                    if (pass.getText().toString() == confirmpass.getText().toString()) {

                        auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(signactivity.this, "Account Created Succesfully", Toast.LENGTH_SHORT).show();

                                    auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Intent recycle = new Intent(signactivity.this, MainActivity.class);
                                                startActivity(recycle);
                                                finish();
                                            } else {
                                                Toast.makeText(signactivity.this, "UnSuccessfull LogIn", Toast.LENGTH_LONG).show();
                                                Intent j = new Intent(signactivity.this, loginactivity.class);
                                                startActivity(j);
                                                finish();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(signactivity.this, "Account Creation UnSuccesfull", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }






                    }
                });
            }

        // Validate password
         public boolean isPasswordValidMethod(String pas)
        {
            boolean isValid = false;

            // ^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
            // ^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$

            String expression = "^((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})$";
            CharSequence inputStr = pas;

            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,16}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(pas);

            return matcher.matches();
        }


}

