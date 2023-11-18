package com.example.greenbot_1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        // Check for internet connection
        if (NetworkUtil.isNetworkAvailable(this)) {
            // There is an internet connection
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, GetStarted.class));
                }
            }, 3000);
        } else {
            // No internet connection
            // Display a message to the user (you can use a dialog, toast, or a custom view)
            // Here's a simple example using a toast message:
            Toast.makeText(this, "No internet connection. Please connect to the internet and try again.", Toast.LENGTH_LONG).show();
        }
    }
}
