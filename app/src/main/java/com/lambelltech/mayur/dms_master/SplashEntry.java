package com.lambelltech.mayur.dms_master;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by mayur on 08-01-2017.
 */

public class SplashEntry extends Activity {
    LocalDbHelper mydb;
    public String username;
    public String password;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_entry_activity);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
       mydb = new LocalDbHelper(this);

        /*
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
        /*
                if ( mydb.getSessionStatus() == 0 ) {
                    Intent mainIntent = new Intent(SplashEntry.this, MainActivity.class);
                    SplashEntry.this.startActivity(mainIntent);
                    SplashEntry.this.finish();
                }else{
                    String username = mydb.getUsername();
                    String password = mydb.getPassword();
                    Intent mainIntent = new Intent(SplashEntry.this, MainActivity.class);
                    mainIntent.putExtra("username",username);
                    mainIntent.putExtra("password",password);
                    SplashEntry.this.startActivity(mainIntent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
*/

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    username = mydb.getUsername();
                    password = mydb.getPassword();
                   // Log.i(TAG,username);
                    Intent mainIntent = new Intent(SplashEntry.this, MainActivity.class);
                    if (mydb.getSessionStatus() == 0 ) {
                        Log.i(TAG,"this running if");
                        mainIntent.putExtra("username",username);
                        mainIntent.putExtra("password",password);
                        SplashEntry.this.startActivity(mainIntent);

                    } else {
                        //Toast.makeText(getApplicationContext(),"this is red",Toast.LENGTH_LONG).show();
                        Log.i(TAG,"this running else");
                        mainIntent.putExtra("username", username);
                        mainIntent.putExtra("password", password);
                        //Toast.makeText(getBaseContext(),"this is "+username,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getBaseContext(),"this is "+password,Toast.LENGTH_LONG).show();
                        SplashEntry.this.startActivity(mainIntent);
                    }
                    SplashEntry.this.finish();
                }
            }

        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}