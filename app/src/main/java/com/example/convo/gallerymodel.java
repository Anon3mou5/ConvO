//package com.example.convo;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Paint;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.List;
//
//import static com.example.convo.MainActivity.getSavedObjectFromPreference;
//import static com.example.convo.MainActivity.saveObjectToSharedPreference;
//
//public class gallerymodel extends RecyclerView.Adapter<msgviewholder> {
//    public  int MSG_RIGHT=0;
//    public int MSG_LEFT=1;
//    public  int MSG_LEFT_PHOTO=2;
//    public  int MSG_RIGHT_PHOTO=3;
//
//    List<gallery> modelclasslist;
//    String sender,when,filepath;
//    Uri url;
//    public gallerymodel(List<gallery> modelclasslist) {
//        this.modelclasslist = modelclasslist;
//        Log.d("Inside", " Adapter,constructor");
//    }
//
//    @Override
//    public galleryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v;
//        int j=0;
//            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery,parent, false);
//            j=1;
//        Log.d("holder", "View holder created");
//        return new galleryviewholder(v,j);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull msgviewholder holder, int position) {
//        sender=modelclasslist.get(position).getSender();
//        when=modelclasslist.get(position).getWhen();
//        filepath=modelclasslist.get(position).getUrl();
//        if(filepath!=null) {
//            url = Uri.parse(filepath);
//        }
//        holder.setdata(sender,when,url);
//        Log.d("holder", "View holder Binded");
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return  modelclasslist.size();
//    }
//}
//
//class galleryviewholder extends RecyclerView.ViewHolder {
//
//
//    TextView sender;
//    TextView when;
//    ImageView photosent;
//
//    public galleryviewholder(@NonNull View itemView,int j) {
//
//        super(itemView);
//
//        sender = itemView.findViewById(R.id.sender);
//        when = itemView.findViewById(R.id.when);
//        photosent=itemView.findViewById(R.id.photosent);
//
//
//        //t3 = itemView.findViewById(R.id.textView);
//    }
//
//    void setdata(String sender,String when,Uri bitmap) {
//            if (bitmap != null) {
//                try {
//                    final Bitmap photobit = MediaStore.Images.Media.getBitmap(itemView.getContext().getContentResolver(), bitmap);
//                    ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            photosent.setImageBitmap(photobit);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                //= getSavedObjectFromPreference(itemView.getContext(),"imagenum","number",collectionType);
//                Toast.makeText(itemView.getContext(), "Unable to retrive", Toast.LENGTH_LONG).show();
//            }
//
//
//        }
////             //p.setMargins(0, 4, 220, 5);
//        //card.setLayoutParams(p);
//        // t1.setVisibility(View.VISIBLE);
//        //  lay.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        //card.setLayoutParams(new ConstraintLayout.LayoutParams(t2.getLayoutParams()));
//        // t2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        // t2.setTextColor(Color.parseColor("#000000"));
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(c);
//                    LayoutInflater lf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View v2 = lf.inflate(R.layout.pop2, null);
//                    final TextView cancel = v2.findViewById(R.id.cancel);
//                    final TextView b = v2.findViewById(R.id.delete);
//                    builder.setView(v2);
//                    final AlertDialog dialog = builder.create();
//                    dialog.show();
//                    b.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                            List<read> mdl;
//                            Type collectionType = new TypeToken<List<read>>() {
//                            }.getType();
//                            mdl = getSavedObjectFromPreference(c, "preference", ruid, collectionType);
//                            for (i = mdl.size() - 1; i >= 0; i--) {
//                                read z = mdl.get(i);
//                                if (z.msg.equals(model.msg)) {
//                                    mdl.remove(i);
//                                    break;
//                                }
//                            }
//
//
//                            Type collectionType2 = new TypeToken<List<chat>>() {
//                            }.getType();
//                            List<chat> mdl2;
//                            mdl2 = getSavedObjectFromPreference(c, "preference", "chatmodel", collectionType2);
//                            if (mdl2.size() != 0) {
//                                for (int k = mdl2.size() - 1; k >= 0; k--) {
//                                    chat z = mdl2.get(k);
//                                    if (z.msg.toLowerCase().equals(chat.toLowerCase())) {
//                                        mdl2.remove(k);
//                                        FirebaseAuth u = FirebaseAuth.getInstance();
//                                        chat zz = new chat(u.getCurrentUser().getUid(), mdl.get(mdl.size() - 2).msg, ruid, "not found", phno, 0);
//                                        mdl2.add(zz);
//                                        break;
//                                    }
//                                }
//                            }
//                            final msgmodel m = new msgmodel(mdl);
//                            saveObjectToSharedPreference(c, "preference", ruid, mdl);
//                            saveObjectToSharedPreference(c, "preference", "chatmodel", mdl2);
//                            final RecyclerView recycle = ((Activity) c).findViewById(R.id.singlerecycle);
//                            LinearLayoutManager lm = new LinearLayoutManager(c);
//                            lm.setOrientation(LinearLayoutManager.VERTICAL);
//                            recycle.setLayoutManager(lm);
//                            recycle.setAdapter(m);
//                            recycle.getLayoutManager().scrollToPosition(mdl.size() - 1);
//                            m.notifyDataSetChanged();
//                        }
//                    });
//
//                    cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    return true;
//                }
//
//
//            });
//
//
//        }
//        //  Bitmap b =loadImageFromStorage(MainActivity.PATH);
////            try {
////
////                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
////                photo.setImageBitmap(bitmap);
////            } catch (MalformedURLException e) {
////                e.printStackTrace();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
////             t1.setLayoutParams(t2.getLayoutParams());
////             Date d = new Date();
//        Log.d("setdata2", "settingdata");
////        t3.setText((int) d.getTime());
//
//    }
////     public static Drawable LoadImageFromWebOperations(String url) {
////         try {
////             InputStream is = (InputStream) new URL(url).getContent();
////             Drawable d = Drawable.createFromStream(is, "src name");
////             return d;
////         } catch (Exception e) {
////             return null;
////         }
//
//
//    static public Bitmap loadImageFromStorage(String subfolder,String path)
//    {
//        Bitmap b=null;
//        if(subfolder==null)
//        {
//            subfolder="";
//        }
//        try {
//            File sdCard = Environment.getExternalStorageDirectory();
//            File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/"+subfolder+"/IMG_"+path+".jpg");
//            b = BitmapFactory.decodeStream(new FileInputStream(dir));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        return b;
//    }
//
//}
//
