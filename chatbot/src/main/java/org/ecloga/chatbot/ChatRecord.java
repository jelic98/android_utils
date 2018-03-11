package org.ecloga.chatbot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ChatRecord extends RecyclerView.ViewHolder  {

    TextView leftText, rightText;

    public ChatRecord(View itemView){
        super(itemView);

        leftText = itemView.findViewById(R.id.leftText);
        rightText = itemView.findViewById(R.id.rightText);
    }
}