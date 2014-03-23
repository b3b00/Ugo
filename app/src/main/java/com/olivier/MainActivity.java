package com.olivier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String PHONE_NO = "0660850330";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = (Button) findViewById(R.id.buttonDone);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String msg = pref.getString("msgText","");
                String tel = pref.getString("msgRecipient","0660850330");

                android.util.Log.d("DONE","sending SMS");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(tel, null, msg, null, null);
            }
        });

        Button pref = (Button)findViewById(R.id.prefButton);

        pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Preference.class);
                startActivity(i);
            }
        });




    }


    protected void sendDone(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}
