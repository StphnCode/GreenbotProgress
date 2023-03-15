package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class BreathingExercise extends AppCompatActivity {
    ImageView btnBrBack, btnPlayVid;
    VideoView squareBreathingVid;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_breathing_exercise);

        btnBrBack = findViewById(R.id.btnBrBack);
        btnPlayVid = findViewById(R.id.btnPlayVid);
        btnBrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreathingExercise.this, SelfCare.class);
                startActivity(intent);
            }
        });

        squareBreathingVid = findViewById(R.id.squareBreathingVid);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.square_breathing;
        uri = Uri.parse(videoPath);
        squareBreathingVid.setVideoURI(uri);


        btnPlayVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squareBreathingVid.start();
            }
        });



    }
}