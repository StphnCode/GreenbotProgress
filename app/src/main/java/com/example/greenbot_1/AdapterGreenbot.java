package com.example.greenbot_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterGreenbot extends RecyclerView.Adapter<AdapterGreenbot.MyViewHolder> {

    private ArrayList<String> list = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogue, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String chat = list.get(position);

        if (position % 2 == 0) { // user chat
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(chat);
            holder.leftChatView.setVisibility(View.GONE);
        } else { // chatbot response
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftChatText.setText(chat);
            holder.rightChatView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addChatToList(String chat) {
        list.add(chat);
        notifyDataSetChanged();
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
