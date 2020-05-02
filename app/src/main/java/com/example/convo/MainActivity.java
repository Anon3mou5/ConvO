package com.example.convo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Data>model= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.authenticate);

        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(this,loginactivity.class);
            startActivity(intent);
        }else{

        }

        RecyclerView recycle = findViewById(R.id.recycle);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
//        recycle.setLayoutManager(lm);

        model.add(new Data("Suhas","Hello"));
        model.add(new Data("Sandesh","Hi"));
        model.add(new Data("Supreeth","Nmasthe"));
        model.add(new Data("Vineeth","Gm"));
        model.add(new Data("viraj","Hel0"));
        model.add(new Data("Vishjesh","sd"));
        model.add(new Data("Sumith","tbh"));
        model.add(new Data("Sambith","smh"));
        model.add(new Data("Suhas","Hello"));
        model.add(new Data("Sandesh","Hi"));
        model.add(new Data("Supreeth","Nmasthe"));
        model.add(new Data("Vineeth","Gm"));
        model.add(new Data("viraj","Hel0"));
        model.add(new Data("Vishjesh","sd"));
        model.add(new Data("Sumith","tbh"));
        model.add(new Data("Sambith","smh"));
        model.add(new Data("Suhas","Hello"));
        model.add(new Data("Sandesh","Hi"));
        model.add(new Data("Supreeth","Nmasthe"));
        model.add(new Data("Vineeth","Gm"));
        model.add(new Data("viraj","Hel0"));
        model.add(new Data("Vishjesh","sd"));
        model.add(new Data("Sumith","tbh"));
        model.add(new Data("Sambith","smh"));
        Log.d("Data added","aalll");

        Modelclass m = new Modelclass(model);
//        recycle.setAdapter(m);
        StatusBarUtil.setTransparent(this);
        m.notifyDataSetChanged();
        final ImageView logo = findViewById(R.id.imageView);
        //logo.animate().translationY(500).setDuration(100);
        Animation b = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.transition);
        logo.animate().translationY(-600).translationX(-100).setDuration(600).setStartDelay(400).scaleX((float)0.8).scaleY((float)0.8);
        Animation a = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fading);
        final EditText passeditid = findViewById(R.id.passedit);
        final EditText emaileditid = findViewById(R.id.emailedit);
        ConstraintLayout l = findViewById(R.id.constraint);
        final ConstraintLayout l1 = findViewById(R.id.constraint2);
        l.startAnimation(a);
        Button login = findViewById(R.id.button3);
        final Activity activity = MainActivity.this;

        final TextView emailid = findViewById(R.id.email);
        final TextView password = findViewById(R.id.pass);
      final  Intent signup = new Intent(this,signactivity.class);

        login.setOnClickListener(new View.OnClickListener() {
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
    }
}
