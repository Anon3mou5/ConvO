package com.example.convo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity {


    List<chat> model = new ArrayList<chat>();
    public int MSG_RIGHT = 0;
    public int MSG_LEFT = 1;
    static  String PATH=null;
    static chat ch;

    //  List<message> listmsg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  StatusBarUtil.setTransparent(this);
        setContentView(R.layout.startactivity);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final Context c = getApplicationContext();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(c, loginactivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("Loggedin", "Previous user");
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }

        setContentView(R.layout.startactivity);
//       final Thread k = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                contactsfetcher contacts = new contactsfetcher();
//                map2 = contacts.getContactList(MainActivity.this);
//                for(String j:map2.keySet())
//                {
//                    Log.d("MAP",map2.get(j));
//                    Data e = new Data(map2.get(j),"not found",0,MainActivity.this,"null",j);
//                    //   Log.d("XYZ",""+model.);
//                    model2.add(e);
//
//                }
//                Log.d("MAPVALUzzz",""+map2);
//            }
//        });
//        k.start();
        Toolbar t = findViewById(R.id.maintoolbar);
        setSupportActionBar(t);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
        db.addChildEventListener(new ChildEventListener() {
                                     @Override
                                     public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                                         model.clear();
                                         String key = dataSnapshot.getKey();
                                         DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                                         Query q = bd.orderByValue().limitToLast(1);
                                         q.addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                 for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                     HashMap<String, String> map = (HashMap<String, String>) data.getValue();
                                                     final String msg = (String) map.get("msg").toString();
                                                     final String suid = (String) map.get("suid").toString();
                                                     final String ruid = (String) map.get("ruid").toString();
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
                                                     model.add(ch);
                                                 }
                                                 final chatmodel m = new chatmodel(model);
                                                 RecyclerView recycle = findViewById(R.id.chatrecycle);
                                                 LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                                                 lm.setOrientation(LinearLayoutManager.VERTICAL);
                                                 recycle.setLayoutManager(lm);
                                                 recycle.setAdapter(m);
                                                 recycle.getLayoutManager().scrollToPosition(model.size() - 1);
                                                 m.notifyDataSetChanged();
                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                             }
                                         });
                                     }


                                     @Override
                                     public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                         model.clear();
                                         String key = dataSnapshot.getKey();
                                         DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                                         Query q = bd.orderByKey().limitToLast(1);
                                         q.addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                 for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                     HashMap<String, String> map = (HashMap<String, String>) data.getValue();
                                                     final String msg = (String) map.get("msg").toString();
                                                     final String suid = (String) map.get("suid").toString();
                                                     final String ruid = (String) map.get("ruid").toString();
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
                                                     model.add(ch);
                                                 }
                                                 final chatmodel m = new chatmodel(model);
                                                 RecyclerView recycle = findViewById(R.id.chatrecycle);
                                                 LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                                                 lm.setOrientation(LinearLayoutManager.VERTICAL);
                                                 recycle.setLayoutManager(lm);
                                                 recycle.setAdapter(m);
                                                 recycle.getLayoutManager().scrollToPosition(model.size() - 1);
                                                 m.notifyDataSetChanged();
                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                             }
                                         });
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
//        Log.d("Data added","aalll");
        BottomNavigationView v =findViewById(R.id.bottom_navigation);
      v.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem menuItem){
                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                switch (id) {
                    case R.id.page_2: {

                        Intent t = new Intent(MainActivity.this, worldActivity.class);
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
                Intent t = new Intent(MainActivity.this,ContactsActivity.class);
                startActivity(t);
                overridePendingTransition(R.anim.down_to_up,R.anim.up_to_down);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    ///////////////////////////////////////CHOOSE ITEM
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                Intent t = new Intent(MainActivity.this,loginactivity.class);
                FirebaseAuth c = FirebaseAuth.getInstance();
                c.signOut();
                startActivity(t);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



///////////////////////////////////////////MENU
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//       //  Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//
//
    ////////////////////////////////////////////Item SELECTED from TOOLBAR
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.logout:
//                   Intent t = new Intent(MainActivity.this,loginactivity.class);
//                   FirebaseAuth c = FirebaseAuth.getInstance();
//                   c.signOut();
//                   startActivity(t);
//                   return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    ///////////////////////////SAVE_TO_MEMORY
    private String saveToInternalStorage(Bitmap bitmapImage,String dir,String path){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(dir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,path);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            //bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}

