package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;

import io.grpc.okhttp.internal.Util;

public class ThoughtPad extends AppCompatActivity {
    ImageView btnTPback;
    TextView journalDate;
    EditText journalContent;
    AppCompatButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_thought_pad);

        btnTPback = findViewById(R.id.btnTPBack);
        journalDate = findViewById(R.id.journalDate);
        journalContent = findViewById(R.id.journalContent);
        btnSubmit = findViewById(R.id.btnSubmit);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        journalDate.setText(currentDate);

        btnTPback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThoughtPad.this, ThoughtPadList.class));
            }
        });

        btnSubmit.setOnClickListener((v)-> submitJournal());

    }

    void submitJournal(){
        String date = journalDate.getText().toString();
        String content = journalContent.getText().toString();

        if(content == null || content.isEmpty()){
            journalContent.setError("Content cannot be empty");
            return;
        }
        Journal journal = new Journal();
        journal.setDate(date);
        journal.setContent(content);
        journal.setTimestamp(Timestamp.now());

        saveJournalToFirebase(journal);
    }

    void saveJournalToFirebase(Journal journal){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForJournal().document();
        documentReference.set(journal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(ThoughtPad.this, "Submitted Successfully");
                    finish();
                }else{
                    Utility.showToast(ThoughtPad.this, "Failed to Submit");
                }
            }
        });

    }
}