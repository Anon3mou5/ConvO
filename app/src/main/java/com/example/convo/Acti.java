package com.example.convo;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;
import static com.example.convo.asynch.getSavedObjectFromPreference;


public class Acti extends AppCompatActivity {

    public int MSG_RIGHT = 0;
    static List<chat> mdl2 = new ArrayList<chat>();
    public int MSG_LEFT = 1;
    static String PATH = null;
    static chat ch;
    static contactsfetcher contacts;
    chatmodel m;
    RecyclerView recycle;

    //  List<message> listmsg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setTransparent(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final Context c = getApplicationContext();

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        setContentView(R.layout.startactivity);
        Toolbar t = findViewById(R.id.maintoolbar);
        setSupportActionBar(t);




        Type collectionType = new TypeToken<List<chat>>(){}.getType();
        mdl2 = getSavedObjectFromPreference(getApplicationContext(), "preference", "chatmodel", collectionType);
      if(mdl2==null)
      {
          mdl2=new ArrayList<chat>();
      }
            m = new chatmodel(mdl2);
            recycle = findViewById(R.id.chatrecycle);
            LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            recycle.setLayoutManager(lm);
           // recycle.getLayoutManager().scrollToPosition(mdl2.size() - 1);
            m.notifyDataSetChanged();
            recycle.setAdapter(m);





        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                Query q = bd.orderByValue().limitToLast(1);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            HashMap<String, String> map = (HashMap<String, String>) data.getValue();
                            final String msg = (String) map.get("msg");
                            final String suid = (String) map.get("suid");
                            final String ruid = (String) map.get("ruid");
                            Log.d("ruid", "" + ruid);
                            final String phno = (String) map.get("phno").toString();
                            FirebaseFirestore ref = FirebaseFirestore.getInstance();
                            DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        final String photourl;
                                        photourl = documentSnapshot.getString("photo");

                                    }
                                }
                            });
                            ch = new chat(suid, msg, ruid, "not found", phno);

                           if(mdl2.size()!=0)
                           {
                               for(int i = mdl2.size()-1;i>=0;i--)
                               {
                                   if(mdl2.get(i).phno.equals(ch.phno))
                                   {
                                       mdl2.remove(i);
                                       break;
                                   }
                               }
                           }
//                            if(mdl2.size()==0)
//                            {
//                                mdl2.add(ch);
//                            }
                                mdl2.add(ch);
                        }
//                        final chatmodel m = new chatmodel(model);
//                        RecyclerView recycle = findViewById(R.id.chatrecycle);
//                        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                        lm.setOrientation(LinearLayoutManager.VERTICAL);
//                        recycle.setLayoutManager(lm);
//                        recycle.setAdapter(m);
                          m.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
//                        String key = dataSnapshot.getKey();
//                        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                        Query q = bd.orderByKey().limitToLast(1);
//                        q.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    HashMap<String, String> map = (HashMap<String, String>) data.getValue();
//                                    final String msg = (String) map.get("msg").toString();
//                                    final String suid = (String) map.get("suid").toString();
//                                    final String ruid = (String) map.get("ruid").toString();
//                                    Log.d("ruid", "" + ruid);
//                                    final String phno = (String) map.get("phno").toString();
//                                    FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                                    DocumentReference df = ref.collection("users").document(ruid.substring(1, ruid.length() - 1));
//                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                final String photourl;
//                                                photourl = documentSnapshot.getString("photo");
//
//                                            }
//                                        }
//                                    });
//                                    ch = new chat(suid, msg, ruid, "not found", phno);
//                                    model.add(ch);
//                                }
//                                final chatmodel m = new chatmodel(model);
//                                RecyclerView recycle = findViewById(R.id.chatrecycle);
//                                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                                lm.setOrientation(LinearLayoutManager.VERTICAL);
//                                recycle.setLayoutManager(lm);
//                                recycle.setAdapter(m);
//                                recycle.getLayoutManager().scrollToPosition(model.size() - 1);
//                                m.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });














//Thread getter = new Thread(new Runnable() {
//    @Override
//    public void run() {
//
//
//
//    }
//});
//
//
//       getter.start();
//





//            if (auth.getCurrentUser() == null) {
//                Intent intent = new Intent(c, loginactivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Log.d("Loggedin", "Previous user");
//            }
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
//                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//            }
//            final Thread k = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
//                    }
//                    contacts = new contactsfetcher();
//                    map2 = contacts.getContactList(MainActivity.this);
//                    for(String j:map2.keySet())
//                    {
//                        // Log.d("MAP",map2.get(j));
//                        Data e = new Data(map2.get(j),"not found",0,MainActivity.this,"null",j);
//                        //   Log.d("XYZ",""+model.);
//                        model2.add(e);
//
//                    }
//                    // Log.d("MAPVALUzzz",""+map2);
//                }
//            });
//            k.start();
//        TimerTask tim = new TimerTask() {
//            @Override
//            public void run() {
//                synchronized(MainActivity.obj)
//                {
//                    MainActivity.obj.notify();
//                }
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(tim,2000,1000);
//        synchronized(obj)
//        {
//            //this thread waits until i reaches 4
//            try {
//                obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }



//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                model.clear();
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(d.getKey());
//                    Query q = bd.orderByKey().limitToLast(1);
//                    q.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            final String msg = (String) dataSnapshot.child("msg").getValue();
//                            final String suid = (String) dataSnapshot.child("suid").getValue();
//                            final String ruid = (String) dataSnapshot.child("ruid").getValue();
//                            Log.d("ruid", ruid);
//                            final String phno = (String) dataSnapshot.child("phno").getValue();
//                            FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                            DocumentReference df = ref.collection("users").document(ruid);
//                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        String photourl;
//                                        photourl = documentSnapshot.getString("photo");
//                                        chat c = new chat(suid, msg, ruid, photourl, phno);
//                                        model.add(c);
//                                    }
//                                }
//                            });
//                        }
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                final chatmodel m = new chatmodel(model);
//                RecyclerView recycle = findViewById(R.id.chatrecycle);
//                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                lm.setOrientation(LinearLayoutManager.VERTICAL);
//                recycle.setLayoutManager(lm);
//                recycle.setAdapter(m);
//                recycle.getLayoutManager().scrollToPosition(model.size() - 1);
//                m.notifyDataSetChanged();            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                model.clear();
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(d.getKey());
//                    Query q = bd.orderByKey().limitToLast(1);
//                    q.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            final String msg = (String) dataSnapshot.child("msg").getValue();
//                            final String suid = (String) dataSnapshot.child("suid").getValue();
//                            final String ruid = (String) dataSnapshot.child("ruid").getValue();
//                            Log.d("ruid", ruid);
//                            final String phno = (String) dataSnapshot.child("phno").getValue();
//                            FirebaseFirestore ref = FirebaseFirestore.getInstance();
//                            DocumentReference df = ref.collection("users").document(ruid);
//                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        String photourl;
//                                        photourl = documentSnapshot.getString("photo");
//                                        chat c = new chat(suid, msg, ruid, photourl, phno);
//                                        model.add(c);
//                                    }
//                                }
//                            });
//                        }
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                final chatmodel m = new chatmodel(model);
//                RecyclerView recycle = findViewById(R.id.chatrecycle);
//                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                lm.setOrientation(LinearLayoutManager.VERTICAL);
//                recycle.setLayoutManager(lm);
//                recycle.setAdapter(m);
//                recycle.getLayoutManager().scrollToPosition(model.size() - 1);
//                m.notifyDataSetChanged();            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        } );

//        Toolbar t = findViewById(R.id.tool);
//        setActionBar(t);


//        model.add(new Data("Suhas","Hello"));
//        model.add(new Data("Sandesh","Hi"));
//        model.add(new Data("Supreeth","Nmasthe"));
//        model.add(new Data("Vineeth","Gm"));
//        model.add(new Data("viraj","Hel0"));
//        model.add(new Data("Vishjesh","sd"));
//        model.add(new Data("Sumith","tbh"));
//        model.add(new Data("Sambith","smh"));
//        model.add(new Data("Suhas","Hello"));
//        model.add(new Data("Sandesh","Hi"));
//        model.add(new Data("Supreeth","Nmasthe"));
//        model.add(new Data("Vineeth","Gm"));
//        model.add(new Data("viraj","Hel0"));
//        model.add(new Data("Vishjesh","sd"));
//        model.add(new Data("Sumith","tbh"));
//        model.add(new Data("Sambith","smh"));
//        model.add(new Data("Suhas","Hello"));
//        model.add(new Data("Sandesh","Hi"));
//        model.add(new Data("Supreeth","Nmasthe"));
//        model.add(new Data("Vineeth","Gm"));
//        model.add(new Data("viraj","Hel0"));
//        model.add(new Data("Vishjesh","sd"));
//        model.add(new Data("Sumith","tbh"));
//        model.add(new Data("Sambith","smh"));

        // Log.d("MODELLING",model2.toString());
//        Log.d("Data added","aalll");

        Log.d("RUNNING",""+isMyServiceRunning(myservice.class));

        BottomNavigationView v = findViewById(R.id.bottom_navigation);
        v.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                                  @Override
                                                  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                                      int id = menuItem.getItemId();
                                                      menuItem.setChecked(true);
                                                      switch (id) {
                                                          case R.id.page_2: {

                                                              Intent t = new Intent(Acti.this, worldActivity.class);
                                                              startActivity(t);
                                                              overridePendingTransition(R.anim.down_to_up, R.anim.up_to_down);
                                                          }
                                                          default:
                                                              return true;
                                                      }
                                                  }
                                              }
        );


        FloatingActionButton fab = findViewById(R.id.startchat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Acti.this, ContactsActivity.class);
                startActivity(t);
                overridePendingTransition(R.anim.down_to_up, R.anim.up_to_down);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public void setview()
    {
        List<chat> mdl;
        Type collectionType = new TypeToken<List<chat>>(){}.getType();
        if ((mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", "chatmodel", collectionType)) != null) {
             m = new chatmodel(mdl);
            m.notifyDataSetChanged();
        }

    }

    ///////////////////////////////////////CHOOSE ITEM
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                Intent t = new Intent(getApplicationContext(), loginactivity.class);
                FirebaseAuth c = FirebaseAuth.getInstance();
                c.signOut();
                startActivity(t);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


}


