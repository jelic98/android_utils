package org.ecloga.speech;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

public class SpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "Speech";

    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        tts = new TextToSpeech(this, this);

        txtText = findViewById(R.id.etText);

        btnSpeak = findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
    }

    @Override
    public void onDestroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            // todo make dynamic language selection
            int result = tts.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "This language is not supported");
            }else {
                btnSpeak.setEnabled(true);
                speakOut();
            }
        } else {
            Log.e(TAG, "Initialization failed");
        }
    }

    private void speakOut() {
        tts.speak(txtText.getText(), TextToSpeech.QUEUE_FLUSH, null, "speechId");
    }
}