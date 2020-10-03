package com.example.lenovo.myapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.lenovo.myapp.Auxiliaries.DbVisit;
import com.example.lenovo.myapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    long Delay = 3000;
    DbVisit mDbHelper;
    DatabaseReference mNotification;
    String v = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       /* mDbHelper = new DbVisit(this);
        mNotification = FirebaseDatabase.getInstance().getReference(Constants.NOTIFICATION_DATABASE_PATH_UPLOADS);

        Bundle b = getIntent().getExtras();
        String s = getIntent().getExtras().getString("name");
        Log.e("TAG", s + " ");
        if (s != null) {
            Intent i = new Intent(this, Home_page.class);
            i.putExtra("name", getIntent().getExtras().getString("name"));
            Notifiation n = new Notifiation(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("id"));
            mNotification.child(mNotification.push().getKey()).setValue(n);
            startActivity(i);
            finish();
        } else {
            SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
            String[] projection = {
                    DbVisitContract._ID,
                    DbVisitContract.COLUMN_NAME_TITLE,
            };

            Cursor cursor = db1.query(DbVisitContract.TABLE_NAME, projection, null, null, null, null, null);
            while (cursor.moveToNext()) {
                v = cursor.getString(1);
            }
            cursor.close();*/
        //Log.e("VALUE",v);

        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {
                // Close SplashScreenActivity.class
                finish();
                if (v != null) {
                    Intent myIntent = new Intent(SplashActivity.this,
                            AskForSignin.class);

                    Log.e(">>>", getIntent().getExtras().getString("name") + "");
                    startActivity(myIntent);

                } else {
                    // Start MainActivity.class

                    Intent myIntent = new Intent(SplashActivity.this,
                            SwapScreen.class);
                    myIntent.putExtra("not", getIntent().getExtras());
                    startActivity(myIntent);
                }
            }
        };

        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
}
