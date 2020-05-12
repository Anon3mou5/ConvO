package com.example.convo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsActivity extends AppCompatActivity
{

   final  List<Data> model = new ArrayList<Data>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.contacts_view);
        Toolbar tool = findViewById(R.id.toolbar2);
        setSupportActionBar(tool);

        // StatusBarUtil.setTransparent(this);

//
     Intent z = getIntent();
      //final HashMap<String,String> map2 =(HashMap<String,String>) z.getSerializableExtra("map");
//

//        Thread t4 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                contactsfetcher contacts = new contactsfetcher();
//                final HashMap<String,String> map = contacts.getContactList(getApplicationContext());
//                Log.d("CONTACTS",""+map);
//                Log.d("MAPKEYSET",""+map.keySet());
//                for(String j:map.keySet())
//                {
//                    Log.d("MAP",map.get(j));
//                    Data e = new Data(map.get(j),"not found",0,ContactsActivity.this,"null",j);
//                    //   Log.d("XYZ",""+model.);
//                    model.add(e);
//
//                }
//            }
//        });
//        t4.start();
        contactsfetcher contacts = new contactsfetcher();
        final HashMap<String,String> map = contacts.getContactList(getApplicationContext());
        Log.d("CONTACTS",""+map);
        Log.d("MAPKEYSET",""+map.keySet());
        for(String j:map.keySet())
        {
            Log.d("MAP",map.get(j));
            Data e = new Data(map.get(j),"not found",0,ContactsActivity.this,"null",j);
         //   Log.d("XYZ",""+model.);
            model.add(e);

        }

        Log.d("modeldataplshelp",""+model);
        final contactmodel m = new contactmodel(model);
        //final Model m2 = new Model(model);
        RecyclerView recycle = findViewById(R.id.contactsrecycle);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(lm);
        m.notifyDataSetChanged();
        recycle.setAdapter(m);

        ImageView fab = findViewById(R.id.rtrn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contactsmenu, menu);
        return true;
    }



    ///////////////////////////////////////CHOOSE ITEM
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.refresh:
                model.clear();
                contactsfetcher contacts = new contactsfetcher();
                final HashMap<String,String> map = contacts.getContactList(getApplicationContext());
                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                DocumentReference df = fb.collection("phonelist").document("list");
                HashMap<String,String> phuid = new HashMap<String, String>();
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            for (final String j : map.keySet()) {
                                if (documentSnapshot.getString(j) != null) {
                                    final String userid = documentSnapshot.getString(j);
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference zf = db.collection("users").document(userid);
                                    zf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                Data d = new Data(map.get(j), documentSnapshot.getString("photo"), 1, ContactsActivity.this, userid, j);
                                                model.add(d);
                                            }
                                        }
                                    });
                                            Data d = new Data(map.get(j), "not found", 1, ContactsActivity.this, userid, j);
                                            model.add(d);
                                }
                                else{
                                        Data d = new Data(map.get(j), "not found", 0, ContactsActivity.this, "null", j);
                                        model.add(d);
                                    }
                            }
                            final contactmodel m = new contactmodel(model);
                            //final Model m2 = new Model(model);
                            RecyclerView recycle = findViewById(R.id.contactsrecycle);
                            LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                            lm.setOrientation(LinearLayoutManager.VERTICAL);
                            recycle.setLayoutManager(lm);
                            recycle.setAdapter(m);
                            recycle.getLayoutManager().scrollToPosition(model.size() - 1);
                            m.notifyDataSetChanged();
                            Log.d("Phone",""+documentSnapshot.getData());
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    }
