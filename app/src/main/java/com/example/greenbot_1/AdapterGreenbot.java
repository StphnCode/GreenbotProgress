package com.example.greenbot_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterGreenbot extends RecyclerView.Adapter<AdapterGreenbot.MyViewHolder> {
    private List<ChatMessage> messageList;

    public AdapterGreenbot(ArrayList<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogue, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatMessage chatMessage = messageList.get(position);
        String chat = chatMessage.getMessage();
        boolean isUserMessage = chatMessage.isUserMessage();

        if (isUserMessage) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(chat);
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftChatText.setText(chat);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addChatToList(ChatMessage chatMessage) {
        messageList.add(chatMessage);
        notifyItemInserted(messageList.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout leftChatView;
        private LinearLayout rightChatView;
        private TextView leftChatText;
        private TextView rightChatText;

        public MyViewHolder(View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftChatText = itemView.findViewById(R.id.left_chat_text);
            rightChatText = itemView.findViewById(R.id.right_chat_text);
        }
    }
}

