package org.ecloga.qr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.blikoon.qrcodescanner.QrCodeActivity;
import org.ecloga.qrscanner.R;

public class QrActivity extends Activity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final String TAG = "QR Scanner";
    private static final String LIB_PACKAGE = "com.blikoon.qrcodescanner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        new QrPermission(this).requestPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "Could not get a good result");

            if(data == null) {
                return;
            }

            String result = data.getStringExtra(LIB_PACKAGE + ".error_decoding_image");

            if(result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(R.string.caption_error);
                alertDialog.setMessage(getString(R.string.caption_error_text));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            return;
        }

        if(requestCode == REQUEST_CODE_QR_SCAN) {
            if(data == null) {
                return;
            }

            String result = data.getStringExtra(LIB_PACKAGE + ".got_qr_scan_relult");

            Log.d(TAG, "Scan result: " + result);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(R.string.caption_result);
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void scanCode(View view) {
        Intent i = new Intent(this, QrCodeActivity.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
    }
}