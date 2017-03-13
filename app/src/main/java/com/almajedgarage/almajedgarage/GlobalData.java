package com.almajedgarage.almajedgarage;

import android.content.Context;
import android.util.Log;

/**
 * Created by mg on 12/3/2016.
 */
public class GlobalData {
    Context context;

    public GlobalData(Context applicationContext) {
        this.context = applicationContext;
    }

    public void startActivity(){

    }

    public void startActivityFinish(Context applicationContext, Class<MainActivity> mainActivityClass){

    }
    public void testToast(){

    }
    public void testLog(String TAG,String message){
        Log.d(TAG,message);
    }

    public void checkNetwork(){

    }


}
