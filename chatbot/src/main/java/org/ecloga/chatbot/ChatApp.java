package org.ecloga.chatbot;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class ChatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}