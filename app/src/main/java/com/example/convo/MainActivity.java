package com.example.convo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class MainActivity extends AppCompatActivity {
    List<message> model = new ArrayList<>();
    public int MSG_RIGHT = 0;
    public int MSG_LEFT = 1;
    static  String PATH=null;

    //    List<message> listmsg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Context c = getApplicationContext();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {

            Intent intent = new Intent(c, loginactivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("Loggedin", "Previous user");
        }

        setContentView(R.layout.activity_main);

        Toolbar t = findViewById(R.id.toolbar);
        setSupportActionBar(t);
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

        StatusBarUtil.setTransparent(this);

        final EditText msg = findViewById(R.id.msg);
        FloatingActionButton send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("");
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    String currentuser = u.getDisplayName();
                    DatabaseReference z =FirebaseDatabase.getInstance().getReference("users").child(u.getUid());
                    StorageReference ref = z.child("ref");
                    message me = new message(currentuser, msg.getText().toString(), Boolean.TRUE, u.getUid(),z.child("photo").toString());
                    db.child("chat").push().setValue(me);
                }
                msg.setText("");
            }
        });

//        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user");
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DatabaseReference db4 = FirebaseDatabase.getInstance().getReference().child("user");
//                for(DataSnapshot post: dataSnapshot.getChildren()) {
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    map.put("name", post.child("name").getValue());
//                    map.put("ph", post.child("ph").getValue());
//                    map.put("photo", post.child("photo").getValue());
//                    map.put("email", post.child("email").getValue());
//                    Object url = post.child("photo").getValue();
//                    String uid = post.child("photo").getValue().toString();
//                    db4.push().setValue(map);
//                    try {
//                        // ImageView i = (ImageView)findViewById(R.id.image);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("chat");
        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    // message m = (message)  post.getValue(message.class);
                    String msg = (String) post.child("msgg").getValue();
                   final String uid = (String) post.child("uid").getValue();
                    String orguid = (String) post.child("orguid").getValue();
                    String url=(String) post.child("url").getValue();


                    DocumentReference docRef = db.collection("users").document();
                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        }
                    })

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Object b = document.get("photo");

                                    Log.d("added", "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d("added", "No such document");
                                }
                            } else {
                                Log.d("added", "get failed with ", task.getException());
                            }
                        }
                    });


                    //    Boolean b = (Boolean)post.child("s").getValue();
                    //   Log.d("NAME",""+uid.toString());{
                    message dz = new message(uid, msg, Boolean.TRUE, orguid,url);
                    model.add(dz);
                    //   Log.d("MSG", "" + msg.toString());

                }

                final Modelclass m = new Modelclass(model);
                //final Model m2 = new Model(model);
                RecyclerView recycle = findViewById(R.id.recycle);
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

        ImageView rtrn = findViewById(R.id.rtrn);
        rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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

