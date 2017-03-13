package com.almajedgarage.almajedgarage;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mg on 12/4/2016.
 */
 public class GlobCar {


    //public static String base_url = "http://glowingsoft.com/almajedgarage/";
    public static String base_url = "http://almajed-garage.com/androidapp/";
    public static String search = "search.php";
    public static String add_car = "add_car.php";
    public static String all_cars = "all_car.php";
    public  static String delete_car = "delete_car.php";
    public static String update_car = "update_car.php";
    public static String admin = "admin.php";
    public static String MYPREFERENCES = "GARAGE";
    private static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    public  static String USER_ID = "user_id";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String LOGEDIN = "logedin";
    public static String TYPE = "type";

   private GlobCar(){

    }

    public static void setPreferencesCar(Context context,String user_id, String username, String password, String user_type , boolean logedIn){
        initializeSharedPreferences(context);
        editor.putString(USER_ID,user_id);
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.putBoolean(LOGEDIN,logedIn);
        editor.putString(TYPE,user_type);
        editor.commit();
    }

    private static void initializeSharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE);
        editor = context.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE).edit();
    }
    public static boolean isLoggedIn(Context context){
        initializeSharedPreferences(context);
        boolean isloggedin = sharedPreferences.getBoolean(LOGEDIN,false);
       return isloggedin;
    }
    public static boolean logOut(Context context){
        initializeSharedPreferences(context);
        editor.putString(USER_ID,"");
        editor.putString(USERNAME,"");
        editor.putString(PASSWORD,"");
        editor.putBoolean(LOGEDIN,false);
        editor.putString(TYPE,"");
        editor.commit();
        return true;
    }


    public static String adminType(Context context) {
        initializeSharedPreferences(context);
        return sharedPreferences.getString(TYPE,"2");
    }
    public static String adminUsername(Context context) {
        initializeSharedPreferences(context);
        return sharedPreferences.getString(USERNAME,"");
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        } else {
            Toast.makeText(context,"Network Problem",Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }
    public static void userToString(Context context){
        initializeSharedPreferences(context);
        Log.d("USER","id"+sharedPreferences.getString(USER_ID,"")+" username: "+sharedPreferences.getString(USERNAME,"")+" type:"+sharedPreferences.getString(TYPE,""));
    }

    public static String adminUserId(Context context) {
        initializeSharedPreferences(context);
        return sharedPreferences.getString(USER_ID,"");
    }

}


