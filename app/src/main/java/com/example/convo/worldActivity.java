package com.example.convo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class worldActivity extends AppCompatActivity {

    List<message> model = new ArrayList<>();
    public int MSG_RIGHT = 0;
    public int MSG_LEFT = 1;
    static  String PATH=null;
    static String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar t = findViewById(R.id.toolbar2);
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


        /////////////////////////////////////////////////////////////////////////////////SEND
        final EditText msg = findViewById(R.id.msg);
        FloatingActionButton send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msg.getText().toString())) {

                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference("");
                    final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    final String currentuser = u.getDisplayName();
                    FirebaseFirestore bd = FirebaseFirestore.getInstance();
                    DocumentReference dr = bd.collection("users").document(u.getUid());
                    dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                url = documentSnapshot.getString("photo");

                            }
                        }
                    });
                    if(url==null)
                    {
                        url="not found";
                    }
                    final message me = new message(currentuser, msg.getText().toString(), Boolean.TRUE, u.getUid(), url);
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



        //////////////////////////////////////////////////////////////////////////////////RECIEVE
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
                    //    Boolean b = (Boolean)post.child("s").getValue();
                    //   Log.d("NAME",""+uid.toString());{
                    message dz = new message(uid, msg, Boolean.TRUE, orguid,url);
                    model.add(dz);
                    //   Log.d("MSG", "" + msg.toString());

                }
                final Modelclass m = new Modelclass(model);
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


        /////////////////////RETURN
        ImageView rtrn = findViewById(R.id.rtrn);
        rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //////////////////////CHOOSE MENU
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
                Intent t = new Intent(worldActivity.this,loginactivity.class);
                FirebaseAuth c = FirebaseAuth.getInstance();
                c.signOut();
                startActivity(t);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String dir, String path){
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
