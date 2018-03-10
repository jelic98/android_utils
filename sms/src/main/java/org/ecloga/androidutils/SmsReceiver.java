package org.ecloga.androidutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SMS Receiver";

    private Listener listener;
    private String serviceProviderNumber;

    SmsReceiver(String serviceProviderNumber) {
        this.serviceProviderNumber = serviceProviderNumber;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
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

                if(smsBundle != null) {
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

            if (sender.equals(serviceProviderNumber) && listener != null) {
                listener.onSmsReceived(body.toString());
            }
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSmsReceived(String text);
    }
}