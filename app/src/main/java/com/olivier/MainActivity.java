package com.olivier;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String PHONE_NO = "0660850330";

    private static final String SENT      = "SMS_SENT";

    private static final String DELIVERED = "SMS_DELIVERED";

    protected void sendSMS(Button button, String msgKey) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        button.setAlpha(0.5f);

        vibrator.vibrate(100);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String msg = pref.getString(msgKey, "");
        String tel = pref.getString("msgRecipient", "0660850330");

        android.util.Log.d("DONE", "sending SMS");
        SmsManager smsManager = SmsManager.getDefault();



        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                int resultCode = getResultCode();
                // TODO
                String message = "";
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                        message = "SMS sent";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Generic failure";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "No service";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                }
                Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
                TextView txt = (TextView)findViewById(R.id.txtInfo);
                txt.setText(message);
            }
        }, new IntentFilter(SENT));


        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                // TODO
                String pdu=intent.getStringExtra("pdu");
                android.util.Log.i("UGO","SMS delivery intent : pdu=["+pdu+"]");
                String message = "SMS delivered "+ (pdu != null ? "["+pdu+"]":"");
                Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();
                TextView txt = (TextView)findViewById(R.id.txtInfo);
                txt.setText(message);

            }
        }, new IntentFilter(DELIVERED));




        smsManager.sendTextMessage(tel, null, msg, sentPI, deliveredPI);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonDone = (Button) findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMS((Button)v, "msgDone");
            }
        });

        Button buttonOutButNotDone = (Button) findViewById(R.id.buttonOutButNotDone);

        buttonOutButNotDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {     sendSMS((Button)v, "msgOutButNotDone");        }
        });

        Button buttonNotOut = (Button) findViewById(R.id.buttonNotOut);

        buttonNotOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMS((Button)v, "msgNotOut");
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}