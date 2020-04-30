package com.example.convo;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class signactivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ConstraintLayout l = findViewById(R.id.constraint3);
        Animation a = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.transition);
        l.startAnimation(a);

    }

    @Override
    protected void onStop() {
        super.onStop();
        ConstraintLayout l = findViewById(R.id.constraint3);
                Animation ab = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
                l.startAnimation(ab);
        TextView t = findViewById(R.id.textView);
        t.setVisibility(t.VISIBLE);
    }
}
