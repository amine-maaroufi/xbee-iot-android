package iot.iotxbee.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;



/**
 * Created by Mohamed ali on 22/06/2016.
 */
public class DataManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "data_Pref";



    // server ip (make variable public to access from outside)
    public static final String KEY_IP = "ip";

    // device token (make variable public to access from outside)
    public static final String KEY_TOKEN = "token";


    // Constructor
    public DataManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createdata(String ip,String TOKEN){

        // Storing ID in pref
        editor.putString(KEY_IP, ip);


        // Storing TOKEN in pref
        editor.putString(KEY_TOKEN, TOKEN);

        // commit changes
        editor.commit();
    }




    /**
     * Get stored  data
     * */
    public HashMap<String, String> getDetails(){
        HashMap<String, String> data = new HashMap<String, String>();
        //  IP server
         data.put(KEY_IP, pref.getString(KEY_IP, null));

        // token device
        data.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

        // return user
        return data;
    }


    public  void clearSession(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

}
