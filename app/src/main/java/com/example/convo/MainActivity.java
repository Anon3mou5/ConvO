package com.example.convo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Data>model= new ArrayList<>();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycle = findViewById(R.id.recycle);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(lm);

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
        recycle.setAdapter(m);
        m.notifyDataSetChanged();
        mAuth = FirebaseAuth.getInstance();

    }
}
