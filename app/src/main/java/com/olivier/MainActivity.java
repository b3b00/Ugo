package com.olivier;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.*;

import com.olivier.button.CircleButton;

import java.util.UUID;

public class MainActivity extends Activity implements ActionBar.TabListener {

    private static final String PHONE_NO = "0660850330";

    private static final String SENT      = "SMS_SENT";

    private static final String DELIVERED = "SMS_DELIVERED";

    protected void testNotif(String status, String message, String phone ) {
        Notification notif = new NotificationCompat.Builder(getBaseContext())
                .setContentInfo(message + " : " +phone)
                .setContentTitle(status)
                .setSmallIcon(R.drawable.ic_launcher).build();
        NotificationManager notifMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifID = UUID.randomUUID().hashCode();

        notifMan.notify(notifID,notif);


    }


    protected void sendSMS(CircleButton button, final String msgKey) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        button.setAlpha(0.5f);

        vibrator.vibrate(100);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final String msg = pref.getString(msgKey, "");
        final String tel = pref.getString("msgRecipient", "0660850330");

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
                        message = getResources().getString(R.string.sent);
                        MainActivity.this.testNotif(message,msg, tel);
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


                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                /*TextView txt = (TextView)findViewById(R.id.txtInfo);
                txt.setText(message);*/
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
                String delivered = getResources().getString(R.string.delivered);
                String toastMessage = delivered+" "+ (pdu != null ? "["+pdu+"]":"");
                MainActivity.this.testNotif(toastMessage,msg, tel);

                Toast.makeText(getBaseContext(),toastMessage,Toast.LENGTH_LONG).show();
                /*TextView txt = (TextView)findViewById(R.id.txtInfo);
                txt.setText(toastMessage);*/

            }
        }, new IntentFilter(DELIVERED));




        smsManager.sendTextMessage(tel, null, msg, sentPI, deliveredPI);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        CircleButton buttonDone = (CircleButton) findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMS((CircleButton)v, "msgDone");
            }
        });

        CircleButton buttonOutButNotDone = (CircleButton) findViewById(R.id.buttonOutButNotDone);

        buttonOutButNotDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {     sendSMS((CircleButton)v, "msgOutButNotDone");        }
        });

        CircleButton buttonNotOut = (CircleButton) findViewById(R.id.buttonNotOut);

        buttonNotOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMS((CircleButton)v, "msgNotOut");
            }
        });

        //Button pref = (Button)findViewById(R.id.prefButton);
        // TODO


/*
        pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Preference.class);
                startActivity(i);
            }
        });
*/



    }


    private void initComponents() {
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Intent i = new Intent(MainActivity.this, Preference.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }
}
