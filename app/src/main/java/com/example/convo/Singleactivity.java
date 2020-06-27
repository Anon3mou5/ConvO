package com.example.convo;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.convo.MainActivity.getSavedObjectFromPreference;
import static com.example.convo.MainActivity.mdl;
import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;
import static com.example.convo.MainActivity.saveObjectToSharedPreference;
import static com.example.convo.MainActivity.timings;


public class Singleactivity extends AppCompatActivity {
    static String ruid;
    static String phonenum;
    static  int counter=0;
    static msgmodel m;
    static String phno;
    static int CAMERA_REQUEST=004;
    static int MY_CAMERA_PERMISSION_CODE=003;
    static int PICK_IMAGE_MULTIPLE=004;
    static int PICK_FILEs_MULTIPLE=005;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    //static List<read> mdl3 = new ArrayList<read>();
   // static APIService apiservice;
    boolean notify = false;
    static FirebaseAuth auth;
    static DatabaseReference db;
    ChildEventListener child;
    int status = 1;
    Uri filePath,download;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (child != null && db != null) {
            db.removeEventListener(child);
        }
        DatabaseReference status = FirebaseDatabase.getInstance().getReference("STATUS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status");
        status.setValue(0);
        finish();
        return;
    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (child != null && db != null) {
//            db.removeEventListener(child);
//        }
//            }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (child != null && db != null) {
//            db.addChildEventListener(child);
//        }
//            }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<read> model = new ArrayList<read>();
        //  HashMap<String,String> contacts;
        Intent intent = getIntent();
        File j  =  (File) getIntent().getExtras().get("photo");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(j));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Bitmap bitmap =  intent.getParcelableExtra("bitmap");
        ruid = intent.getStringExtra("uid");
        Log.d("ruid", ruid);
        phno = intent.getStringExtra("phno");
        String name = intent.getStringExtra("name");
        Window window = getWindow();
        window.setNavigationBarColor(getResources().getColor(R.color.pink));
        //   contactsfetcher cf = new contactsfetcher();
        //    contacts= cf.getContactList(getApplicationContext());
        setContentView(R.layout.single_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final EditText msg = findViewById(R.id.msg);
        TextView profilename = findViewById(R.id.profilename);
        final TextView online = findViewById(R.id.online);
        //   String name = contacts.get(phno);
        if (name == null) {
            name = phno;
        }
        CircleImageView profile = findViewById(R.id.profilephoto);
        profile.setImageBitmap(bitmap);
        profilename.setText(name);
        final ImageView fb = findViewById(R.id.send);



        // apiservice = Client.getclient("https://fcm.googleapis.com/").create(APIService.class);
        final Button photo = findViewById(R.id.photo);
        final Button attach = findViewById(R.id.attachment);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sendrotate);
                    fb.startAnimation(anim);
                    String url;
                    final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Private chats");
                    final String text = msg.getText().toString();
                    read me = new read(u.getUid(), msg.getText().toString(), ruid, phno, "blah-blah",null,(int) 0,null,0);
                    FirebaseFirestore bd = FirebaseFirestore.getInstance();
                    CollectionReference cd = bd.collection("phonelist");
                    DocumentReference ref = cd.document("list");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> entity = documentSnapshot.getData();
                                for (Map.Entry<String, Object> ent : entity.entrySet()) {
                                    if (ent.getValue().toString().equals(u.getUid())) {
                                        String ph = ent.getKey().toString();
                                        read m = new read(u.getUid(), text, ruid, ph, "blah-",null,(int)0,null,0);
                                        db1.child(ruid).child(u.getUid()).push().setValue(m);
                                    }
                                }
//                                String ph = documentSnapshot.getString("phno");
//                                read m = new read(u.getUid(), msg.getText().toString(), ruid,ph);
//                                db.child(ruid).child(u.getUid()).push().setValue(m);
                            }
                        }
                    });
                    db1.child(u.getUid()).child(ruid).push().setValue(me);
                    //  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    //FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    sendNotification(ruid, msg.getText().toString(), "no photo", getApplicationContext(),null,(int)0);

                }

                msg.setText("");
    }
});


        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                // TODO Auto-generated method stub
                if (count > 0) {
                    if (counter == 0) {

                        counter = counter + 1;
                        photo.animate().alpha(0).setDuration(100);
                        attach.animate().alpha(0).setDuration(100);
                        photo.setVisibility(View.INVISIBLE);
                        attach.setVisibility(View.INVISIBLE);
                        fb.setVisibility(View.VISIBLE);
                        fb.animate().alpha(1).setDuration(100);

                    }
                    }
                    if (count == 0) {
                        counter=0;
                        fb.animate().alpha(0).setDuration(100);
                        fb.setVisibility(View.INVISIBLE);
                        photo.setVisibility(View.VISIBLE);
                        attach.setVisibility(View.VISIBLE);
                        photo.animate().alpha(1).setDuration(100);
                        attach.animate().alpha(1).setDuration(100);

                    }
                }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input

                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                {
//                   // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                }
//                else
//                {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**The following line is the important one!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                {
//                   // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                }
//                else
//                {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*"); //allows any image file type. Change * to specific extension to limit it
//**The following line is the important one!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_FILEs_MULTIPLE); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
            }
        });

        Type collectionType = new TypeToken<List<read>>() {
        }.getType();
        mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);
        if (mdl == null) {
            mdl = new ArrayList<read>();
        }

        final RecyclerView recycle = findViewById(R.id.singlerecycle);
        m = new msgmodel(mdl);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(lm);
        recycle.setAdapter(m);
        recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
        ((LinearLayoutManager) recycle.getLayoutManager()).setStackFromEnd(true);
        m.notifyDataSetChanged();


//        db.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {
//                // for (DataSnapshot post : dataSnapshot.getChildren()) {
////
//                // message m = (message)  post.getValue(message.class);
//                String msg = (String) post.child("msg").getValue();
//                final String suid = (String) post.child("suid").getValue();
//                String ruid = (String) post.child("ruid").getValue();
//                String phno = (String) post.child("phno").getValue();
//                read rd = new read(suid, msg, ruid, phno);
//                mdl.add(rd);
        //    }
//                        final msgmodel m = new msgmodel(model3);
//                        RecyclerView recycle = findViewById(R.id.singlerecycle);
//                        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                        lm.setOrientation(LinearLayoutManager.VERTICAL);
//                        recycle.setLayoutManager(lm);
//                        recycle.setAdapter(m);


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(ruid);

//        child = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String s) {
//                //mdl3.clear();
//                String ruidd = "";
//                //  for (DataSnapshot post : dataSnapshot.getChildren()) {
//                // message m = (message)  post.getValue(message.class);
//                String msg = (String) post.child("msg").getValue();
//                final String suid = (String) post.child("suid").getValue();
//                String ruid = (String) post.child("ruid").getValue();
//                int isphoto = Integer.parseInt((Long.toString((long) post.child("isphoto").getValue())));
//                if(isphoto==1 && suid==FirebaseAuth.getInstance().getUid())
//                {
//                    return;
//                }
//                ruidd = ruid;
//                String phno = (String) post.child("phno").getValue();
//                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//                Date date = new Date();
//                String time1 = formatter.format(date);
//                read rd = new read(suid, msg, ruidd, phno, time1,null,0,null);
//                mdl.add(rd);
//                ((LinearLayoutManager) recycle.getLayoutManager()).setStackFromEnd(true);
//                recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
//                m.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot post) {
////                for(int i =0 ;i<mdl.size();i++)
////                {
////                    if(mdl.get(i).getMsg().equals(post.child("msg").getValue().toString()))
////                    {
////                        mdl.remove(mdl.get(i));
////                        break;
////                    }
////                }
////                recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
////                m.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//      //  db.addChildEventListener(child);

//        db.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot post : dataSnapshot.getChildren()) {
//
//                            // message m = (message)  post.getValue(message.class);
//                            String msg = (String) post.child("msg").getValue();
//                            final String suid = (String) post.child("suid").getValue();
//                            String ruid = (String) post.child("ruid").getValue();
//                            String phno = (String) post.child("phno").getValue();
//                            read rd = new read(suid, msg, ruid, phno);
//                            mdl.add(rd);
//                        }
////                        final msgmodel m = new msgmodel(model3);
////                        RecyclerView recycle = findViewById(R.id.singlerecycle);
////                        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
////                        lm.setOrientation(LinearLayoutManager.VERTICAL);
////                        recycle.setLayoutManager(lm);
////                        recycle.setAdapter(m);
//                        recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
//                        m.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//
//                });


//        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(u.getUid().toString()).child(ruid.toString());
//        //Toast.makeText(Singleactivity.this,"ruid  "+ ruid,Toast.LENGTH_SHORT).show();
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                model.clear();
//                for (DataSnapshot post : dataSnapshot.getChildren()) {
//                    // message m = (message)  post.getValue(message.class);
//                    String msg = (String) post.child("msg").getValue();
//                    final String suid = (String) post.child("suid").getValue();
//                    String ruid = (String) post.child("ruid").getValue();
//                  //  String url=(String) post.child("url").getValue();
//                    //    Boolean b = (Boolean)post.child("s").getValue();
//                    //   Log.d("NAME",""+uid.toString());{
//                   // message dz = new message(uid, msg, Boolean.TRUE, orguid,url);
//                    read dz = new read(suid,msg,ruid,phno);
//                    model.add(dz);
//                    //   Log.d("MSG", "" + msg.toString());
//                }''


//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        ImageView rtrn = findViewById(R.id.rtrn);
        rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Singleactivity.this, Acti.class);
                startActivity(t);
            }
        });


        Timer timer = new Timer();

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            getstats(ruid,online);
        }
    };
        timer.schedule(timerTask, 0, 2000);
}

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(child!=null && db!=null) {
//            db.removeEventListener(child);
//        }   finish();
//        return;
//    }

    public static void sendNotification(final String ruid, final String msg, final String profilephoto, final Context c,final String photo,final int isphoto) {
        final APIService apiservice = Client.getclient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens").child(ruid);
        Query q = ref.orderByKey().equalTo(ruid);
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference df = fb.collection("phonelist").document("list");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> j = documentSnapshot.getData();

                    for (Map.Entry<String, Object> k : j.entrySet()) {
                        if (k.getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                            phonenum = k.getKey().toString();
                        }
                    }


                }
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String tokens = data.getValue().toString();
                    FirebaseAuth a = FirebaseAuth.getInstance();
                    data2 d = new data2(msg, phonenum, profilephoto, ruid, a.getCurrentUser().getUid(),photo,isphoto);
                    sender send = new sender(d, tokens);
                    apiservice.sendNotification(send).enqueue(new Callback<myrespone>() {
                        @Override
                        public void onResponse(Call<myrespone> call, Response<myrespone> response) {
//                            if (response.code() == 200) {
//                                if (response.body().success != 1) {
                            //Toast.makeText(c, "Failed", Toast.LENGTH_SHORT).show();
                        }
//                            }
//                        }

                        @Override
                        public void onFailure(Call<myrespone> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setview() {
        Type collectionType = new TypeToken<List<read>>() {
        }.getType();
        if (getSavedObjectFromPreference(Singleactivity.this, "preference", ruid, collectionType) != null) {
            List<read> mdl = getSavedObjectFromPreference(getApplicationContext(), "preference", ruid, collectionType);

            m = new msgmodel(mdl);
            m.notifyDataSetChanged();
        }
    }

    public static void refresh(Context c) {
        if (m != null) {
            Type collectionType = new TypeToken<List<read>>() {
            }.getType();
            m.notifyDataSetChanged();
        }
    }

    public void getstats(final String ruid, final TextView online) {


        DatabaseReference db = FirebaseDatabase.getInstance().getReference("STATUS").child(ruid).child(("status"));
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (Integer.parseInt(dataSnapshot.getValue().toString() )== 1){
                    DatabaseReference time = FirebaseDatabase.getInstance().getReference("STATUS").child(ruid).child(("time"));
                    time.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (timings == null) {
                                timings = dataSnapshot.getValue().toString();
                            } else {
                                String newtime = dataSnapshot.getValue().toString();
                                if (newtime.toLowerCase().equals(timings.toLowerCase().toString())) {
                                    DatabaseReference rechange = FirebaseDatabase.getInstance().getReference("STATUS").child(ruid).child(("status"));
                                    rechange.setValue(0);
                                    online.setText("Last seen at :" + timings);
                                    counter=0;
                                } else {
                                    /////////////////setonline
                                    timings=newtime;
//                                    if (counter == 0) {
                                        online.setText("Online");
//                                        counter += 1;
//                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    online.setText("Last seen at :" + timings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               // startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_MULTIPLE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {

                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        filePath = data.getClipData().getItemAt(i).getUri();
                        try {
                            Bitmap photo = null;
                            try {
                                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                            Date date = new Date();
                            String time1 = formatter.format(date);
                            read r = new read(FirebaseAuth.getInstance().getUid(), null, ruid, phno, time1,null,1,filePath.toString(),0);
                            mdl.add(r);
                            m.notifyDataSetChanged();
                            //r = new read(FirebaseAuth.getInstance().getUid(), "l", ruid, phno, time1, "nolink", 1,null);
                            saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, mdl);
                            uploadImage(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                } else if (data.getData() != null) {
                    filePath = data.getData();
                        Bitmap photo = null;
                        try {
                            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Date date = new Date();
                    String time1 = formatter.format(date);
                    read r = new read(FirebaseAuth.getInstance().getUid(), null, ruid, phno, time1, null, 1, filePath.toString(),0);
                    mdl.add(r);
                    m.notifyDataSetChanged();
             //       r = new read(FirebaseAuth.getInstance().getUid(), "", ruid, phno, time1, "nolink", 1,null);
                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, mdl);

                    try {
                        uploadImage(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }
        if (requestCode == PICK_FILEs_MULTIPLE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {

                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        filePath = data.getClipData().getItemAt(i).getUri();
                        try {

                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                            Date date = new Date();
                            String time1 = formatter.format(date);
                            read r = new read(FirebaseAuth.getInstance().getUid(), null, ruid, phno, time1,null,0,filePath.toString(),1);
                            mdl.add(r);
                            m.notifyDataSetChanged();
                            //r = new read(FirebaseAuth.getInstance().getUid(), "l", ruid, phno, time1, "nolink", 1,null);
                            saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, mdl);
                            uploadImage(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                } else if (data.getData() != null) {
                    filePath = data.getData();
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Date date = new Date();
                    String time1 = formatter.format(date);
                    read r = new read(FirebaseAuth.getInstance().getUid(), null, ruid, phno, time1, null, 0, filePath.toString(),1);
                    mdl.add(r);
                    m.notifyDataSetChanged();
                    //       r = new read(FirebaseAuth.getInstance().getUid(), "", ruid, phno, time1, "nolink", 1,null);
                    saveObjectToSharedPreference(getApplicationContext(), "preference", ruid, mdl);

                    try {
                        uploadImage(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }
    }

//
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
//        {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//            Date date = new Date();
//            String time1 = formatter.format(date);
//            read r = new read(FirebaseAuth.getInstance().getUid(),"",ruid,phno,time1,"nolink",1,photo);
//            mdl.add(r);
//            saveObjectToSharedPreference(getApplicationContext(),"preference",ruid,mdl);
//            m.notifyDataSetChanged();
//            try {
//                uploadImage(photo);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("ERROR001",""+e.getStackTrace());
//            }


    private void uploadImage( Uri filePath  ) throws IOException {
        if (filePath != null) {

            // Code for showing progressDialog while uploading

            // Defining the child of storageReference
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/");
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
        Bitmap bmp;
        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
            Log.d("SUCESS", "Data compressed");
            byte[] data = baos.toByteArray();
            ref.putBytes(data)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    Task<Uri> j = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    j.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            download = uri;
                                            Log.d("IMAGE", " " + download.toString());
                                            Toast
                                                    .makeText(Singleactivity.this,
                                                            "Image Uploaded!!",
                                                            Toast.LENGTH_SHORT)
                                                    .show();

                                            final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                            final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Private chats");
                                            read me = new read(u.getUid(), "", ruid, phno, "blah-blah",download.toString(),1,null,0);
                                            FirebaseFirestore bd = FirebaseFirestore.getInstance();
                                            CollectionReference cd = bd.collection("phonelist");
                                            DocumentReference ref = cd.document("list");
                                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        Map<String, Object> entity = documentSnapshot.getData();
                                                        for (Map.Entry<String, Object> ent : entity.entrySet()) {
                                                            if (ent.getValue().toString().equals(u.getUid())) {
                                                                String ph = ent.getKey().toString();
                                                                read m = new read(u.getUid(), "", ruid, ph, "blah-",download.toString(),1,null,0);
                                                                db1.child(ruid).child(u.getUid()).push().setValue(m);
                                                            }
                                                        }
//                                String ph = documentSnapshot.getString("phno");
//                                read m = new read(u.getUid(), msg.getText().toString(), ruid,ph);
//                                db.child(ruid).child(u.getUid()).push().setValue(m);
                                                    }
                                                }
                                            });
                                            db1.child(u.getUid()).child(ruid).push().setValue(me);
                                            //  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                            //FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                            sendNotification(ruid, "📷  Photo", "no photo", getApplicationContext(),null,1);

                                        }
                                    })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    // Error, Image not uploaded

                                                    Toast
                                                            .makeText(Singleactivity.this,
                                                                    "Failed " + e.getMessage(),
                                                                    Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            });


                                    // Progress Listener for loading
                                    // percentage on the dialog box

                                }

                            });

        }
    }
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }


}

