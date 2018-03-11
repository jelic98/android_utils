package org.ecloga.sms;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.TextView;
import android.widget.Toast;

public class SmsActivity extends Activity {

    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        new SmsPermission(this).requestPermission();

        smsReceiver = new SmsReceiver();
        //smsReceiver.setNumber(BuildConfig.SERVICE_NUMBER);
        smsReceiver.setNumber(null);
        smsReceiver.setListener(new SmsReceiver.Listener() {
            @Override
            public void onSmsReceived(String message) {
                Toast.makeText(SmsActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        TextView tvSender = findViewById(R.id.tvSender);

        String sender = tvSender.getText() + BuildConfig.SERVICE_NUMBER;

        tvSender.setText(sender);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(smsReceiver);

        super.onDestroy();
    }
}