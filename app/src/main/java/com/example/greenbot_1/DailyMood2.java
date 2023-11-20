package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;

public class DailyMood2 extends AppCompatActivity {
    ImageView btnDMBack;
    RecyclerView recyclerView;
    DailyMoodAdapter2 dailyMoodAdapter2;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_daily_mood2);

        btnDMBack = findViewById(R.id.btnDM2Back);
        recyclerView = findViewById(R.id.recyclerViewDM2);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btnDMBack.setOnClickListener((v)->startActivity(new Intent(DailyMood2.this, SelfCare.class)));

        setUpRecyclerView();
    }
    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForMoodTracker().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MoodTracker> option = new FirestoreRecyclerOptions.Builder<MoodTracker>()
                .setQuery(query, MoodTracker.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyMoodAdapter2 = new DailyMoodAdapter2(option, this);

        recyclerView.setAdapter(dailyMoodAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dailyMoodAdapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dailyMoodAdapter2.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dailyMoodAdapter2.notifyDataSetChanged();
    }
}