package com.example.convo;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class storeimagetodevice {

    public URI store(Bitmap bitmap, String subfolder, String phno)
    {
    FileOutputStream outStream = null;
    if(subfolder==null)
    {
        subfolder="";
    }

// Write to SD Card
try {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath(), "Convo/photos/"+subfolder);
        dir.mkdirs();

        String fileName = String.format("%s.jpg", "IMG_"+phno );
        File outFile = new File(dir, fileName);
        if(outFile.exists())
        {
            outFile.delete();
        }
        outFile.createNewFile();
        outStream = new FileOutputStream(outFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();

        Log.d("PHOTO", "onPictureTaken - wrote to " + outFile.getAbsolutePath());
         return outFile.toURI();
    //  refreshGallery(outFile);
    } catch (
    FileNotFoundException e) {
        e.printStackTrace();
    } catch (
    IOException e) {
        e.printStackTrace();
    } finally {
}

        return null;
    }
}
