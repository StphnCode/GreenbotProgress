package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button btnSignIn;
    TextView txtSignUp;
    EditText editEmailLogin, editPasswordLogin;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignIn);
        txtSignUp = findViewById(R.id.txtDontHaveAcc);
        editEmailLogin = findViewById(R.id.editEmailLogin);
        editPasswordLogin = findViewById(R.id.editPasswordLogin);
        progressBar = findViewById(R.id.progressBar2);


        String text = "Don't have an account? Sign-up";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
        ss.setSpan(fcsGreen, 23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignUp.setText(ss);



        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();

            }
        });

        btnSignIn.setOnClickListener((v)-> loginUser());


    }
    void loginUser(){
        String email = editEmailLogin.getText().toString();
        String password = editPasswordLogin.getText().toString();


        boolean isValidated = validateData(email, password);
        if(checkEmpty(editEmailLogin)){
            editEmailLogin.setError("Required Field!");
        }
        if(checkEmpty(editPasswordLogin)){
            editPasswordLogin.setError("Required Field!");
        }
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email, password);

    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to convo
                        startActivity(new Intent(Login.this, StartConvo.class));
                        finish();

                    }else{
                        Utility.showToast(Login.this, "Please verify your email.");
                    }
                }else{
                    Utility.showToast(Login.this, task.getException().getLocalizedMessage());
                }

            }
        });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            txtSignUp.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            txtSignUp.setVisibility(View.VISIBLE);
        }
    }
    boolean checkEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean validateData(String email, String password){
        // validate the data entered by the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmailLogin.setError("Email is invalid!");
            return false;
        }
        return true;
    }
}