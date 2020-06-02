package com.example.convo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static com.example.convo.MainActivity.model;
import static com.example.convo.MainActivity.model3;

public class asynch extends AsyncTask<Context, Void, Void> {
    static chat ch;
    @Override
    protected Void doInBackground(final Context... params) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid());
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                AsyncTask a = new AsyncTask<Context,Void,Void>() {
                    @Override
                    protected Void doInBackground(final Context... params) {

                        model.clear();
                        final String key = dataSnapshot.getKey();
                        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
                        Query qz = bd.orderByValue();
                        qz.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                model3.clear();
                                String ruidd = "";
                                for (DataSnapshot post : dataSnapshot.getChildren()) {

                                    // message m = (message)  post.getValue(message.class);
                                    String msg = (String) post.child("msg").getValue();
                                    final String suid = (String) post.child("suid").getValue();
                                    String ruid = (String) post.child("ruid").getValue();
                                    ruidd = ruid;
                                    String phno = (String) post.child("phno").getValue();
                                    read rd = new read(suid, msg, ruid, phno);
                                    model3.add(rd);

                                }
                                List<read> md;
                                Type collectionType = new TypeToken<List<chat>>(){}.getType();
                                if ((md=getSavedObjectFromPreference(params[0], "preference", ruidd, collectionType)) != null) {
                                    for (int j = 0; j < model3.size(); j++) {
                                        read r = model3.get(j);
                                        md.add(r);
                                    }
                                    saveObjectToSharedPreference(params[0], "preference", ruidd, md);


                                } else {
                                    saveObjectToSharedPreference(params[0], "preference", ruidd, model3);
                                }


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
//                                final chatmodel m = new chatmodel(model);
//                                RecyclerView recycle = findViewById(R.id.chatrecycle);
//                                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//                                lm.setOrientation(LinearLayoutManager.VERTICAL);
//                                recycle.setLayoutManager(lm);
//                                recycle.setAdapter(m);
//                                recycle.getLayoutManager().scrollToPosition(model.size() - 1);
//                                m.notifyDataSetChanged();
                                Type collectionType = new TypeToken<List<chat>>(){}.getType();

                                if (getSavedObjectFromPreference(params[0], "preference", "chatmodel", collectionType) != null) {
                                    List<chat> mdl = getSavedObjectFromPreference(params[0], "preference", "chatmodel", collectionType);

                                    for (int j = 0; j < model.size(); j++) {
                                        chat r = model.get(j);
                                        for (int i = mdl.size() - 1; i >= 0; i--) {
                                            if (mdl.get(i).phno.toLowerCase().equals(r.phno.toLowerCase())) {
                                                mdl.remove(i);
                                                mdl.add(r);
                                                break;
                                            }
                                        }
                                        if (mdl.get(mdl.size() - 1).phno.toLowerCase().equals(r.phno.toLowerCase())) {
                                            // mdl.add(r);
                                        } else {
                                            mdl.add(r);
                                        }
                                    }
                                    saveObjectToSharedPreference(params[0], "preference", "chatmodel", mdl);

                                } else {
                                    saveObjectToSharedPreference(params[0], "preference", "chatmodel", model);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        db.child(key).removeValue();


                        return null;
                    }
                };
                a.execute(params);
                           }


            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
//                AsyncTask ab = new AsyncTask<Context,Void,Void>() {
//                    @Override
//                    protected Void doInBackground( final Context... params) {

//                        model.clear();
//                        String key = dataSnapshot.getKey();
//                        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Private chats").child(auth.getCurrentUser().getUid()).child(key);
//                        Query q = bd.orderByKey().limitToLast(1);
//                        Query qz = bd.orderByValue();
//                        qz.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                model3.clear();
//                                String ruidd = "";
//                                for (DataSnapshot post : dataSnapshot.getChildren()) {
//
//                                    // message m = (message)  post.getValue(message.class);
//                                    String msg = (String) post.child("msg").getValue();
//                                    final String suid = (String) post.child("suid").getValue();
//                                    String ruid = (String) post.child("ruid").getValue();
//                                    ruidd = ruid;
//                                    String phno = (String) post.child("phno").getValue();
//                                    read rd = new read(suid, msg, ruid, phno);
//                                    model3.add(rd);
//
//                                }
//                                List<read> mdl;
//                                Type collectionType = new TypeToken<List<chat>>(){}.getType();
//                                if((mdl=getSavedObjectFromPreference(params[0],"preference",ruidd,collectionType))!=null)
//                                {
//
//                                    for(int j = 0; j<model3.size();j++)
//                                    {
//                                        read r = model3.get(j);
//                                        mdl.add(r);
//                                    }
//                                    saveObjectToSharedPreference(params[0], "preference", ruidd, mdl);
//
//
//                                }
//                                else {
//                                    saveObjectToSharedPreference(params[0], "preference", ruidd, model3);
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//
//                        });
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
////                                final chatmodel m = new chatmodel(model);
////                                RecyclerView recycle = findViewById(R.id.chatrecycle);
////                                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
////                                lm.setOrientation(LinearLayoutManager.VERTICAL);
////                                recycle.setLayoutManager(lm);
////                                recycle.setAdapter(m);
////                                recycle.getLayoutManager().scrollToPosition(model.size() - 1);
////                                m.notifyDataSetChanged();
//                                Type collectionType = new TypeToken<List<chat>>(){}.getType();
//
//                                if (getSavedObjectFromPreference(params[0], "preference", "chatmodel", collectionType) != null) {
//                                    List<chat> mdl = getSavedObjectFromPreference(params[0], "preference", "chatmodel", collectionType);
//                                    for (int j = 0; j < model.size(); j++) {
//                                        chat r = model.get(j);
//                                        mdl.add(r);
//                                    }
//                                    saveObjectToSharedPreference(params[0], "preference", "chatmodel", mdl);
//
//                                } else {
//                                    saveObjectToSharedPreference(params[0], "preference", "chatmodel", model);
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//
//                        });
//                        db.child(key).removeValue();
//
//                        return null;
//                    }
//                };
//
//                ab.execute(params);

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
        return null;
    }
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();

    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }


}
