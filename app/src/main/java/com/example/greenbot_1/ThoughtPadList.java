package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ThoughtPadList extends AppCompatActivity {
    AppCompatButton btnAddEntry;
    RecyclerView recyclerView;
    ImageView imgEmoji, btnTPLback;
    JournalAdapter journalAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_thought_pad_list);

        btnAddEntry = findViewById(R.id.btnAddEntry);
        recyclerView = findViewById(R.id.recyclerView);
        btnTPLback = findViewById(R.id.btnTPLBack);

        btnAddEntry.setOnClickListener((v) -> startActivity(new Intent(ThoughtPadList.this, ThoughtPad.class)));
        btnTPLback.setOnClickListener((v) -> startActivity(new Intent(ThoughtPadList.this, SelfCare.class)));

        setUpRecyclerView();


    }

    void setUpRecyclerView(){

        Query query = Utility.getCollectionReferenceForJournal().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Journal> options = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(query, Journal.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalAdapter = new JournalAdapter(options, this);
        recyclerView.setAdapter(journalAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        journalAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        journalAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        journalAdapter.notifyDataSetChanged();
    }
}