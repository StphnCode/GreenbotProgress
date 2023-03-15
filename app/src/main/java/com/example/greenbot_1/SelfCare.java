package com.example.greenbot_1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelfCare extends AppCompatActivity {
    ImageView btnLeftArrow, btnBreathingExercise;
    TextView txtRate;
    Slider slider;
    ImageButton btnCheckMood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_self_care);

        btnLeftArrow = findViewById(R.id.btnLeftArrow);
        btnBreathingExercise = findViewById(R.id.BreathingExercise);
        txtRate = findViewById(R.id.txtRate);
        slider = findViewById(R.id.slider);
        btnCheckMood = findViewById(R.id.btnCheckMood);

        btnLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelfCare.this, StartConvo.class);
                startActivity(intent);
            }
        });
        btnBreathingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelfCare.this, BreathingExercise.class);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView txtCurrentDate = findViewById(R.id.currentDate);
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
            }
        });

    }
}