package com.example.convo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Singleactivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       final List<read> model = new ArrayList<read>();
      //  HashMap<String,String> contacts;
        Intent intent = getIntent();
        final String ruid=intent.getStringExtra("uid");
        Log.d("ruid",ruid);
        final String phno=intent.getStringExtra("phno");
        String name=intent.getStringExtra("name");
     //   contactsfetcher cf = new contactsfetcher();
    //    contacts= cf.getContactList(getApplicationContext());
         setContentView(R.layout.single_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final EditText msg = findViewById(R.id.msg);
        TextView profilename = findViewById(R.id.profilename);
    //   String name = contacts.get(phno);
       if(name==null)
       {
           name=phno;
       }
       profilename.setText(name);
        final ImageView fb = findViewById(R.id.send);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sendrotate);
                    fb.startAnimation(anim);
                    String url;
                    final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats");
                    final String text = msg.getText().toString();
                    read me = new read(u.getUid(), msg.getText().toString(), ruid,phno);
                    FirebaseFirestore bd = FirebaseFirestore.getInstance();
                    CollectionReference cd = bd.collection("phonelist");
                    DocumentReference ref = cd.document("list");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                Map<String,Object> entity = documentSnapshot.getData();
                                for(Map.Entry<String,Object> ent : entity.entrySet())
                                {
                                    if(ent.getValue().toString().equals(u.getUid()))
                                    {
                                        String ph = ent.getKey().toString();
                                        read m = new read(u.getUid(), text, ruid,ph);
                                        db.child(ruid).child(u.getUid()).push().setValue(m);
                                    }
                                }
//                                String ph = documentSnapshot.getString("phno");
//                                read m = new read(u.getUid(), msg.getText().toString(), ruid,ph);
//                                db.child(ruid).child(u.getUid()).push().setValue(m);
                            }
                        }
                    });
                    db.child(u.getUid()).child(ruid).push().setValue(me);
                }
                msg.setText("");
                        }
                    });
         final  FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(u.getUid().toString()).child(ruid.toString());
        //Toast.makeText(Singleactivity.this,"ruid  "+ ruid,Toast.LENGTH_SHORT).show();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    // message m = (message)  post.getValue(message.class);
                    String msg = (String) post.child("msg").getValue();
                    final String suid = (String) post.child("suid").getValue();
                    String ruid = (String) post.child("ruid").getValue();
                  //  String url=(String) post.child("url").getValue();
                    //    Boolean b = (Boolean)post.child("s").getValue();
                    //   Log.d("NAME",""+uid.toString());{
                   // message dz = new message(uid, msg, Boolean.TRUE, orguid,url);
                    read dz = new read(suid,msg,ruid,phno);
                    model.add(dz);
                    //   Log.d("MSG", "" + msg.toString());
                }
                final msgmodel m = new msgmodel(model);
                RecyclerView recycle = findViewById(R.id.singlerecycle);
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
                Intent t = new Intent(Singleactivity.this,Acti.class);
                startActivity(t);
            }
        });

        }

    }

