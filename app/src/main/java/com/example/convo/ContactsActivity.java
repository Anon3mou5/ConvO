package com.example.convo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.example.convo.MainActivity.model2;
import static com.example.convo.MainActivity.map2;
import static com.example.convo.MainActivity.phuid;
import static com.example.convo.MainActivity.saveObjectToSharedPreference;
//import static com.example.convo.MainActivity.contacts;

public class ContactsActivity extends AppCompatActivity {



   // final List<Data> model = new ArrayList<Data>();

    final contactmodel m = new contactmodel(model2);


//    @Override
//    public void onBackPressed() {
//        final TextView startachat = findViewById(R.id.startachat);
//        final TextView numberofchat = findViewById(R.id.numberofcontacts);
//
//        final FloatingActionButton button4 = findViewById(R.id.button4);
//
//        final EditText searchField = findViewById(R.id.searchview);
//        searchField.animate().alpha(0).setDuration(235).setStartDelay(0);
//        searchField.setVisibility(View.INVISIBLE);
//        numberofchat.animate().translationX(100).alpha(0).setDuration(250).setStartDelay(0);
//        startachat.animate().translationX(100).alpha(1).setDuration(250).setStartDelay(0);
//        button4.animate().translationX(680).setDuration(250).setStartDelay(0);
//        startachat.animate().translationX(100).alpha(1).setDuration(250).setStartDelay(0);
//
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Thread tz = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                map = contacts.getContactList(ContactsActivity.this);
//            }
//        });
//        tz.start();

        setContentView(R.layout.contacts_view);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar tool = findViewById(R.id.toolbar2);
        setSupportActionBar(tool);
//        Thread ti = new Thread(new Runnable() {
//            @Override
//            public void run() {
//               // Log.d("CONTACTS", "" + map);
//              //  Log.d("MAPKEYSET", "" + map.keySet());
//                for (String j : map.keySet()) {
//                    Log.d("MAP", map.get(j));
//                    Data e = new Data(map.get(j), "not found", 0, ContactsActivity.this, "null", j);
//                    //   Log.d("XYZ",""+model.);
//                    model.add(e);
//
//                }
//
//                Log.d("modeldataplshelp", "" + model);
//            }
//        });


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
//        Log.d("CONTACTS", "" + map);
//        Log.d("MAPKEYSET", "" + map.keySet());
//        for (String j : map.keySet()) {
//            Log.d("MAP", map.get(j));
//            Data e = new Data(map.get(j), "not found", 0, ContactsActivity.this, "null", j);
//            //   Log.d("XYZ",""+model.);
//            model.add(e);
//
//        }
//
//        Log.d("modeldataplshelp", "" + model);

        //final Model m2 = new Model(model);
        final RecyclerView recycle = findViewById(R.id.contactsrecycle);
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

        final TextView startachat = findViewById(R.id.startachat);
        final TextView numberofchat = findViewById(R.id.numberofcontacts);

        final FloatingActionButton button4 = findViewById(R.id.button4);

        final ImageView button5 = findViewById(R.id.button5);

        final EditText searchField = findViewById(R.id.searchview);

        TextView numberoffilm = findViewById(R.id.numberofcontacts);
        numberoffilm.setText(model2.size() + " Contacts");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4.animate().translationX(-680).setDuration(250).setStartDelay(0);
                startachat.animate().translationX(-100).alpha(0).setDuration(250).setStartDelay(0);
                button5.setVisibility(View.VISIBLE);
                numberofchat.animate().translationX(-100).alpha(0).setDuration(250).setStartDelay(0);
                Animation vz = AnimationUtils.loadAnimation(ContactsActivity.this,R.anim.serachbartool);
               button5.startAnimation(vz);
                searchField.setVisibility(View.VISIBLE);
                searchField.animate().alpha(1).setDuration(235).setStartDelay(0);
                searchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // filter your list from your input
                         filter(s.toString());
                        //you can use runnable postDelayed like 500 ms to delay search text
                    }
                });
            }

            void filter(String text) {
                final List<Data> temp = new ArrayList();
                for (final HashMap.Entry<String, String> d : map2.entrySet()) {


                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getValue().toLowerCase().contains(text.toLowerCase())) {
                        if(phuid.get(d.getKey())!=null){
                            Data e1 = new Data(d.getValue(), "not found", 1, ContactsActivity.this, phuid.get(d.getKey()), d.getKey());
                            temp.add(e1);
                        } else{
                            Data e = new Data(d.getValue(), "not found", 0, ContactsActivity.this, " ", d.getKey());
                            temp.add(e);
                        }

                        m.updateList(temp);
                    }
                }   //update recyclerview

            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation vz = AnimationUtils.loadAnimation(ContactsActivity.this,R.anim.ulta);
                button5.startAnimation(vz);
                button5.setVisibility(View.INVISIBLE);
                searchField.animate().alpha(0).setDuration(235).setStartDelay(0);
                searchField.setVisibility(View.INVISIBLE);
                numberofchat.animate().translationX(100).alpha(1).setDuration(250).setStartDelay(0);
                startachat.animate().translationX(100).alpha(1).setDuration(250).setStartDelay(0);
                button4.animate().translationX(40).setDuration(250).setStartDelay(0);



            }
        });
    }

     public void contactsadd() {
        List<String> az = new ArrayList<String>();
        while (model2.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (model2.size() != 0) {
                break;
            }
        }
        for (int i = 0; i < model2.size(); i++) {
            Log.d("PHU", model2.get(i).getPhno());
            az.add(model2.get(i).phno);
        }
        for (String j : map2.keySet()) {
            int i;
            for (i = 0; i < az.size(); i++) {
                if (j.toLowerCase().equals(az.get(i).toLowerCase().toString())) {
                    break;
                }
            }
            if (i == az.size()) {
                Data e = new Data(map2.get(j), "not found", 0, ContactsActivity.this, "null", j);
                model2.add(e);
            }
        }
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
                model2.clear();
               // final HashMap<String,String> map = contacts.getContactList(ContactsActivity.this);
                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                DocumentReference df = fb.collection("phonelist").document("list");
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            for (final String j : map2.keySet()) {
                                if (documentSnapshot.getString(j) != null) {
                                    final String userid = documentSnapshot.getString(j);
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference zf = db.collection("users").document(userid);
                                    zf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                // Data d = new Data(map.get(j), documentSnapshot.getString("photo"), 1, ContactsActivity.this, userid, j);
                                                // model.add(d);
                                            }
                                        }
                                    });
                                    Data d = new Data(map2.get(j), "not found", 1, ContactsActivity.this, userid, j);
                                    //  phuid.put(j,userid);
                                    model2.add(d);
                                }
//                                else{
//                                        Data d = new Data(map.get(j), "not found", 0, ContactsActivity.this, "null", j);
//                                        model.add(d);
//                                    }
                            }

                        }
contactsadd();
                    }
                });
                           Collections.sort(model2, new sortbyfound());
                            RecyclerView recycle = findViewById(R.id.contactsrecycle);
                            LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                            lm.setOrientation(LinearLayoutManager.VERTICAL);
                            recycle.setLayoutManager(lm);
                            recycle.setAdapter(m);
                            m.notifyDataSetChanged();
                            saveObjectToSharedPreference(ContactsActivity.this,"contacts","contact",model2);
                //            Log.d("Phone",""+documentSnapshot.getData());

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    }
    class sortbyfound implements Comparator<Data>
    {
        public int compare(Data a,Data b)
        {
            return a.getFound()-b.getFound();
        }
    }
