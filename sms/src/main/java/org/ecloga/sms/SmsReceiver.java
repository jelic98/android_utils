package org.ecloga.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SMS Receiver";

    private Listener listener;
    private String number;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Message received");

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String sender = "";
            StringBuilder body = new StringBuilder();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    sender = smsMessage.getDisplayOriginatingAddress();
                    body.append(smsMessage.getMessageBody());
                }
            } else {
                Bundle smsBundle = intent.getExtras();

                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");

                    if (pdus == null) {
                        Log.e(TAG, "No pdus key");
                        return;
                    }

                    SmsMessage[] messages = new SmsMessage[pdus.length];

                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        body.append(messages[i].getMessageBody());
                    }

                    sender = messages[0].getOriginatingAddress();
                }
            }

            if (listener != null) {
                if (number == null || sender.equals(number)) {
                    listener.onSmsReceived(body.toString());
                }
            }
        }
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onSmsReceived(String message);
    }
}