package com.example.convo;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signactivity extends AppCompatActivity {
    //    final EditText name = findViewById(R.id.nameedit);
    Uri filePath;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");
 static  public  Uri downloadUrl;
 static public StorageReference str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);

        ImageView btn_choose_photo = findViewById(R.id.photo);
        final EditText ph = findViewById(R.id.phedit);
        final EditText email = findViewById(R.id.emailedit);

        final EditText name = findViewById(R.id.nameedit);


        // TextView emailindication = findViewById(R.id.emailindication);

        final EditText pass = findViewById(R.id.passedit);

        final TextView indication = findViewById(R.id.passindication);

        final EditText confirmpass = findViewById(R.id.confirmpassedit);

        ConstraintLayout l = findViewById(R.id.constraint3);

        Button signin = findViewById(R.id.signin);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition);
        l.startAnimation(a);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indication.setVisibility(TextView.VISIBLE);
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
                            }
                            else {
                                if (pass.getText().toString().equals(confirmpass.getText().toString())) {

                                 final   Thread t2 = new Thread(new Runnable() {
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
                                                                                        uploadImage();
                                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                        CollectionReference cd = db.collection("users");
                                                                                        CollectionReference z = db.collection("phonelist");
                                                                                        HashMap<String, String> map = new HashMap<>();
                                                                                        HashMap<String,String>  phuid=new HashMap<>();
                                                                                        map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                        map.put("name", name.getText().toString());
                                                                                        map.put("ph", ph.getText().toString());
                                                                                        phuid.put(ph.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                        if(downloadUrl==null)
                                                                                        {
                                                                                            map.put("photo","not found");
                                                                                        }
                                                                                        else {
                                                                                            map.put("photo", downloadUrl.toString());
                                                                                        }map.put("email",email.getText().toString());

                                                                                        cd.document(user.getUid()).set(map);
                                                                                        z.document("list").set(phuid, SetOptions.merge());
                                                                                        user.updateProfile(profileUpdates)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Log.d("username", "User profile updated.");

                                                                                                            Intent recycle = new Intent(signactivity.this, MainActivity.class);
                                                                                        startActivity(recycle);
                                                                                        finish();
                                                                                    }                               Intent recycle = new Intent(signactivity.this, MainActivity.class);
                                                                                                        startActivity(recycle);
                                                                                                        finish();                                                      }
                                                                            });
                                                        Toast.makeText(signactivity.this, "Account Created Succesfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(signactivity.this, "Account Creation UnSuccesfull", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                    });
                                    t2.start();

                                         final AlertDialog.Builder builder = new AlertDialog.Builder(signactivity.this);
                                            View v2 = getLayoutInflater().inflate(R.layout.popup,null);
                                            builder.setView(v2);
                                            final AlertDialog dialog= builder.create();
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
                                                    if(t2.isAlive()==true)
                                                    {
                                                    }else
                                                    {
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
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,16}$";
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
        ImageView btn_choose_photo = findViewById(R.id.photo);

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
                btn_choose_photo.setImageBitmap(bitmap);;
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading

            // Defining the child of storageReference

            final StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + FirebaseAuth.getInstance().getUid());
            Log.d("SUCESS","Inside upload()");

            // adding listeners on upload
            // or failure of image
            Bitmap bmp;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                Log.d("SUCESS","Data compressed");
                byte[] data = baos.toByteArray();
                ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(signactivity.this,"success",Toast.LENGTH_SHORT).show();
                    }
                    });
                str=ref;
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl=uri;
                        Toast.makeText(signactivity.this,"url: "+uri.toString(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure","idk why");
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("Cancelled","idk why");
                    }
                });
            }catch(Exception e)
            {
                Toast.makeText(signactivity.this,"Unsuccessfull compress",Toast.LENGTH_SHORT).show();
            }

        }
    }
}

