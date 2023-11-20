package com.example.greenbot_1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelfCare extends AppCompatActivity {
    ImageView btnLeftArrow, btnBreathingExercise, btnThoughtPad;
    TextView txtRate, txtCurrentDate;
    FirebaseFirestore fStore;

    Slider slider;
    ImageButton btnCheckMood;
    AppCompatButton btnDailyMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_self_care);

        btnLeftArrow = findViewById(R.id.btnLeftArrow);
        btnBreathingExercise = findViewById(R.id.BreathingExercise);
        btnThoughtPad = findViewById(R.id.ThoughtPad);
        txtRate = findViewById(R.id.txtRate);
        slider = findViewById(R.id.slider);
        btnCheckMood = findViewById(R.id.btnCheckMood);
        btnDailyMood = findViewById(R.id.btnDailyMood);


        btnLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelfCare.this, StartConvo.class);
                startActivity(intent);
            }
        });

        btnDailyMood.setOnClickListener((v)->startActivity(new Intent(SelfCare.this, DailyMood2.class)));
        btnBreathingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelfCare.this, BreathingExercise.class);
                startActivity(intent);
            }
        });

        btnThoughtPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelfCare.this, ThoughtPadList.class));
            }
        });

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        txtCurrentDate = findViewById(R.id.currentDate);
        txtCurrentDate.setText(currentDate);

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                int rateValue = Math.round(value);
                txtRate.setText(Integer.toString(rateValue));
            }
        });
        btnCheckMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slider.setEnabled(false);
                submitDailyMood();
            }
        });



    }
    void submitDailyMood(){
        String date = txtCurrentDate.getText().toString();
        int moodRate = Integer.parseInt(txtRate.getText().toString());


        MoodTracker moodTracker = new MoodTracker();
        moodTracker.setDate(date);
        moodTracker.setMoodRate(moodRate);

        saveMoodToFirebase(moodTracker);
    }
    void saveMoodToFirebase(MoodTracker moodTracker){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForMoodTracker().document();

        documentReference.set(moodTracker).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(SelfCare.this, "Saved");

                }else{
                    Utility.showToast(SelfCare.this, "Failed to save");
                }
            }
        });
    }
}