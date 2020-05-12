package com.example.convo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class contactsfetcher {

    HashMap<String,String> contacts = new HashMap<String, String>();

    public HashMap<String,String> getContactList(Context c) {
        ContentResolver cr = c.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("NAME", "Name: " + name);
                        contacts.put(trimmer(phoneNo),name);
                        Log.i("PHONE", "Phone Number: " + phoneNo);

                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return contacts;
    }

    public String trimmer(String phno)
    {
        int z=0;
        int k;
        String phone="";
        int j=phno.length();
        for(k=j-1;z<10 & k >= 0 ;k--)
        {
            if(phno.charAt(k)==' ' || phno.charAt(k)=='-'|| phno.charAt(k)=='*'|| phno.charAt(k)=='#')
            {

            }
            else {
                phone = phno.charAt(k) + phone;
                z = z + 1;
            }
        }
        return phone;
    }
    }


