package org.ecloga.chatbot;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends Activity implements AIListener {

    private static final String TAG = "Chatbot";
    private static final String ACCESS_TOKEN = "1596796ecdf142eea0c2a3728174ec6b";

    private EditText etMessage;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        etMessage = findViewById(R.id.etMessage);

        RelativeLayout btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();

                if(!message.isEmpty()) {
                    ref.child("chat").push().setValue(new ChatMessage(message, "user"));
                }

                etMessage.setText("");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        AIConfiguration config = new AIConfiguration(ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIService service = AIService.getService(this, config);
        service.setListener(this);

        final AIDataService dataService = new AIDataService(this, config);

        final AIRequest request = new AIRequest();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();

                if (!message.isEmpty()) {
                    ref.child("chat").push().setValue(new ChatMessage(message, "user"));
                    request.setQuery(message);

                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            try {
                                return dataService.request(request);
                            }catch(AIServiceException e) {
                                Log.e(TAG, e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(AIResponse response) {
                            try {
                                String reply = response.getResult().getFulfillment().getSpeech();
                                ref.child("chat").push().setValue(new ChatMessage(reply, "bot"));
                            }catch(NullPointerException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }.execute(request);
                }else {
                    service.startListening();
                }

                etMessage.setText("");
            }
        });

        FirebaseRecyclerAdapter<ChatMessage, ChatRecord> adapter =
                new FirebaseRecyclerAdapter<ChatMessage, ChatRecord>(ChatMessage.class,
                        R.layout.list_chat, ChatRecord.class, ref.child("chat")) {
            @Override
            protected void populateViewHolder(ChatRecord viewHolder, ChatMessage model, int position) {
                if(model.getUser().equals("user")) {
                    viewHolder.rightText.setText(model.getText());
                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.GONE);
                }else {
                    viewHolder.leftText.setText(model.getText());
                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);
                }
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ref.child("chat").push().setValue(new ChatMessage(message, "user"));

        String reply = result.getFulfillment().getSpeech();
        ref.child("chat").push().setValue(new ChatMessage(reply, "bot"));
    }

    @Override
    public void onError(AIError error) {}

    @Override
    public void onAudioLevel(float level) {}

    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}
}