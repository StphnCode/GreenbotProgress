package com.example.greenbot_1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DailyMoodAdapter2 extends FirestoreRecyclerAdapter<MoodTracker, DailyMoodAdapter2.DailyMoodViewHolder> {
    Context context;
    public static final int VIEW_WEARY = 1;
    public static final int VIEW_WORRIED = 2;
    public static final int VIEW_NEUTRAL = 3;
    public static final int VIEW_SLIGHT = 4;
    public static final int VIEW_GRIN = 5;

    public int getMoodRate() {
        return moodRate;
    }

    public void setMoodRate(int moodRate) {
        this.moodRate = moodRate;
    }

    private int moodRate;


    @Override
    protected void onBindViewHolder(@NonNull DailyMoodViewHolder holder, int position, @NonNull MoodTracker model) {
        holder.reset();
        holder.txtDate.setText(model.getDate());
        this.moodRate = model.getMoodRate();

        if(moodRate == VIEW_WEARY){
            holder.moodItem.setBackgroundColor(ContextCompat.getColor(context, R.color.weary_background));
            holder.emoji.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weary_face));
        }else if(moodRate == VIEW_WORRIED){
            holder.moodItem.setBackgroundColor(ContextCompat.getColor(context, R.color.worried_background));
            holder.emoji.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.worried_emoji));
        }else if(moodRate == VIEW_NEUTRAL){
            holder.moodItem.setBackgroundColor(ContextCompat.getColor(context, R.color.neutral_background));
            holder.emoji.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_emoji));
        }else if(moodRate == VIEW_SLIGHT){
            holder.moodItem.setBackgroundColor(ContextCompat.getColor(context, R.color.slightSmile_background));
            holder.emoji.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slightly_smiling_emoji));
        }else if(moodRate == VIEW_GRIN){
            holder.moodItem.setBackgroundColor(ContextCompat.getColor(context, R.color.grin_background));
            holder.emoji.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.grinning_emoji));
        }

    }

    public DailyMoodAdapter2(@NonNull FirestoreRecyclerOptions<MoodTracker> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public DailyMoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailymood_grin_item,parent,false);
        return new DailyMoodViewHolder(view);
    }

    class DailyMoodViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate;
        ImageView moodItem, emoji;

        public DailyMoodViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtMoodDate);
            moodItem = itemView.findViewById(R.id.MoodItem);
            emoji = itemView.findViewById(R.id.imageView5);
        }
        public void reset() {
            moodItem.setBackgroundColor(Color.TRANSPARENT);
            emoji.setImageDrawable(null);
        }

    }


}
