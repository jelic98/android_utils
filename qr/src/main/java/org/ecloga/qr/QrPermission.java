package org.ecloga.qr;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaActionSound;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class QrPermission implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int QR_PERMISSION_CODE = 1;

    private Activity activity;

    public QrPermission(Activity activity) {
        this.activity = activity;
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void requestPermission() {
        if(shouldAskPermission()) {
            ActivityCompat.requestPermissions(activity, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.VIBRATE
            }, QR_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == QR_PERMISSION_CODE) {
            if(grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            }
        }
    }
}