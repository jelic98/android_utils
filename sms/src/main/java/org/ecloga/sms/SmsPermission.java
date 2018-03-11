package org.ecloga.sms;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class SmsPermission implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int SMS_PERMISSION_CODE = 1;

    private Activity activity;

    public SmsPermission(Activity activity) {
        this.activity = activity;
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void requestPermission() {
        if(shouldAskPermission()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == SMS_PERMISSION_CODE) {
            if(grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            }
        }
    }
}