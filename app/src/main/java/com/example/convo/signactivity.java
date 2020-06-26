package com.example.convo;

import android.animation.Animator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaeger.library.StatusBarUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.convo.MainActivity.saveObjectToSharedPreference;


public class signactivity extends AppCompatActivity {
    //    final EditText name = findViewById(R.id.nameedit);
    Uri filePath;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");
    static public Uri downloadUrl;
    static Uri download;
    ;
    static public StorageReference str;
    EditText name = null;
    EditText ph = null;
    EditText email = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConstraintLayout l = findViewById(R.id.constraint3);
        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition2);
        l.startAnimation(a);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        StatusBarUtil.setTransparent(this);
        ImageView btn_choose_photo = findViewById(R.id.photo);
        ph = findViewById(R.id.phedit);
        email = findViewById(R.id.emailedit);

        name = findViewById(R.id.nameedit);


        // TextView emailindication = findViewById(R.id.emailindication);

        final EditText pass = findViewById(R.id.passedit);

        final TextView indication = findViewById(R.id.passindication);

        final EditText confirmpass = findViewById(R.id.confirmpassedit);


        Button signin = findViewById(R.id.signin);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        ConstraintLayout l = findViewById(R.id.constraint3);

        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition);
        l.startAnimation(a);

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0) {
                    indication.setVisibility(View.INVISIBLE);
                } else {
                    indication.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().length() == 0) {
                    Toast.makeText(signactivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (isPhValidMethod(ph.getText().toString()) == false) {
                        Toast.makeText(signactivity.this, "Invalid Phone no format", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                            Toast.makeText(signactivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                        } else {
                            if (isPasswordValidMethod(pass.getText().toString()) == false) {
                                Toast.makeText(signactivity.this, "Invalid format", Toast.LENGTH_SHORT).show();
                            } else {
                                if (pass.getText().toString().equals(confirmpass.getText().toString())) {

                                    final Thread t2 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(name.getText().toString())
                                                                .build();
                                                        try {
                                                            uploadImage();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        FirebaseAuth usr = FirebaseAuth.getInstance();
                                                        String token = FirebaseInstanceId.getInstance().getToken();
                                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Tokens");
                                                        Token token1 = new Token(token);
                                                        database.child(usr.getUid()).push().setValue(token1);
                                                        user.updateProfile(profileUpdates)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.d("username", "User profile updated.");

                                                                            Intent recycle = new Intent(signactivity.this, MainActivity.class);
                                                                            startActivity(recycle);
                                                                            finish();
                                                                        }
                                                                        Intent recycle = new Intent(signactivity.this, MainActivity.class);
                                                                        startActivity(recycle);
                                                                        finish();
                                                                    }
                                                                });
                                                        read zz = new read(user.getUid(), name.getText().toString(), user.getUid(), ph.getText().toString(), "blah",null,0,null);
                                                        saveObjectToSharedPreference(getApplicationContext(), "urnum", user.getUid(), zz);

                                                        Toast.makeText(signactivity.this, "Account Created Succesfully", Toast.LENGTH_SHORT).show();

                                                        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            CharSequence name = getString(R.string.app_name);
                                                            String description = getString(R.string.decr);
                                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                                            NotificationChannel channel = new NotificationChannel("123", name, importance);
                                                            channel.setDescription(description);
                                                            // Register the channel with the system; you can't change the importance
                                                            // or other notification behaviors after this
                                                            mNotificationManager.createNotificationChannel(channel);

                                                        }
                                                        NotificationCompat.MessagingStyle messaging = new NotificationCompat.MessagingStyle("Me");
                                                        messaging.setConversationTitle("Welcome User");


                                                        long when = System.currentTimeMillis();
                                                        NotificationCompat.MessagingStyle.Message notificationmessage = new NotificationCompat.MessagingStyle.Message("Dont forget to Enable Floating Notification", when, "Welcome User");
                                                        messaging.addMessage(notificationmessage);

                                                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 0);

                                                        Notification notifications = new NotificationCompat.Builder(getApplicationContext(), "123").setSmallIcon(R.drawable.logo).setStyle(messaging)
                                                                .setContentIntent(pendingIntent1).setAutoCancel(true).
                                                                        setPriority(Notification.PRIORITY_MAX).setOnlyAlertOnce(true).setColor(Color.rgb(237, 19, 14)).setDefaults(Notification.DEFAULT_ALL).build();
                                                        mNotificationManager.notify(10, notifications);

                                                    } else {
                                                        Toast.makeText(signactivity.this, "Account Creation UnSuccesfull", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                    });
                                    t2.start();

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(signactivity.this);
                                    View v2 = getLayoutInflater().inflate(R.layout.popup, null);
                                    builder.setView(v2);
                                    final AlertDialog dialog = builder.create();
                                    dialog.show();

                                    final LottieAnimationView j = v2.findViewById(R.id.sunny);
                                    j.addAnimatorListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {
                                            if (t2.isAlive() == true) {
                                            } else {
                                                j.cancelAnimation();
                                                dialog.cancel();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(signactivity.this, "Password is not same", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    }
                }
            }
        });

        // Replace with id of your button.
        btn_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        123);
            }
        });

    }

    // Validate password
    public boolean isPasswordValidMethod(String pas) {
        boolean isValid = false;

        // ^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
        // ^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$

        String expression = "^((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})$";
        CharSequence inputStr = pas;

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,17}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pas);

        return matcher.matches();
    }

    public boolean isPhValidMethod(String ph) {
        boolean isValid = false;

        // ^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
        // ^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^[6-9](?=.*[0-9]).{9}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(ph);

        return matcher.matches();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        FloatingActionButton btn_choose_photo = findViewById(R.id.photo);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == 123
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                btn_choose_photo.setImageBitmap(bitmap);
                ;
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    private void uploadImage() throws IOException {
        if (filePath != null) {

            // Code for showing progressDialog while uploading

            // Defining the child of storageReference

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
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                                            downloadUrl = download;
                                            Toast.makeText(getApplicationContext(), "Sucessfullly uploaded : " + download.toString(), Toast.LENGTH_LONG);
                                            Log.d("PHOTO", " " + download.toString());
                                            Toast
                                                    .makeText(signactivity.this,
                                                            "Image Uploaded!!",
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            CollectionReference cd = db.collection("userswithph");
                                            CollectionReference z = db.collection("phonelist");
                                            HashMap<String, String> map = new HashMap<>();
                                            HashMap<String, String> phuid = new HashMap<>();
                                            map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            map.put("name", name.getText().toString());
                                            map.put("ph", ph.getText().toString());
                                            phuid.put(ph.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            map.put("photo", download.toString());
                                            map.put("email", email.getText().toString());

                                            cd.document(ph.getText().toString()).set(map);
                                            z.document("list").set(phuid, SetOptions.merge());

                                        }
                                    })


                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    // Error, Image not uploaded

                                                    Toast
                                                            .makeText(signactivity.this,
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
}

