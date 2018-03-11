package org.ecloga.speech;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class SpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "Speech";
    private static final Locale DEFAULT_LOCALE = Locale.US;

    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText etText;
    private Spinner spnLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        tts = new TextToSpeech(this, this);

        etText = findViewById(R.id.etText);

        spnLocale = findViewById(R.id.spnLocale);
        spnLocale.setAdapter(getLocaleAdapter());
        spnLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLocale();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
            setLocale();
        } else {
            Log.e(TAG, "Initialization failed");
        }
    }

    private void setLocale() {
        int result = tts.setLanguage(getSelectedLocale(DEFAULT_LOCALE));

        btnSpeak.setEnabled(result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED);
    }

    private Locale getSelectedLocale(Locale locale) {
        String name = spnLocale.getSelectedItem().toString();

        Log.e("asd", name);

        try {
            return (Locale) locale.getClass().getField(name).get(locale);
        }catch(Exception e) {
            return DEFAULT_LOCALE;
        }
    }

    private ArrayList<String> getLocales() {
        ArrayList<String> locales = new ArrayList<>();

        Field[] fields = Locale.class.getDeclaredFields();

        for(Field f : fields){
            if(f.getType().equals(Locale.class) && isAllUpper(f.getName())) {
                locales.add(f.getName());
            }
        }

        return locales;
    }

    private ArrayAdapter<String> getLocaleAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, getLocales());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private boolean isAllUpper(String s) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if(c >= 97 && c <= 122) {
                return false;
            }
        }

        return true;
    }

    private void speakOut() {
        tts.speak(etText.getText(), TextToSpeech.QUEUE_FLUSH, null, "speechId");
    }
}