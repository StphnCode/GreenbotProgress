package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.ClippableRoundedCornerLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    Button btnRegister;
    TextView txtSignIn;
    EditText editEnterName, editEmail, editPassword, editRePassword;
    ProgressBar progressBar;
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://greenbot-1-default-rtdb.firebaseio.com/");
    static int userId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);
        txtSignIn = findViewById(R.id.txtHaveAcc);

        editEnterName = findViewById(R.id.editEnterName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editRePassword = findViewById(R.id.editRePassword);
        progressBar = findViewById(R.id.progressBar);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        String text = "Already have an account? Sign-in";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
        ss.setSpan(fcsGreen, 25,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignIn.setText(ss);




        btnRegister.setOnClickListener(v-> userRegister());



    }
    void userRegister(){
        String userID = String.valueOf(userId);
        String name = editEnterName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String rePassword = editRePassword.getText().toString();


        boolean isValidated = validateData(name, email, password, rePassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(userID, name, email, password);

    }

    void createAccountInFirebase(String userID, String name, String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // creating account is done
                            Utility.showToast(Registration.this, "Successfully registered!");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            sendDataToDB(userID, name, email, password);
                            userId++;
                            startNextActivity();

                        }else{
                            //failed
                            Utility.showToast(Registration.this, task.getException().getLocalizedMessage());

                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }

    void sendDataToDB(String userID, String name, String email, String password){
        dbReference.child("User").child(userID).child("name").setValue(name);
        dbReference.child("User").child(userID).child("id").setValue(userID);
        dbReference.child("User").child(userID).child("email").setValue(email);
        dbReference.child("User").child(userID).child("password").setValue(password);


    }

    boolean validateData(String name, String email, String password, String rePassword){
        // validate the data entered by the user
        if(name.length() == 0){
            editEnterName.requestFocus();
            editEnterName.setError("Required field!");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Email is invalid!");
            return false;
        }
        if(password.length() < 8){
            editPassword.setError("Password must be at least 8 characters long.");
            return false;
        }
        if(!password.equals(rePassword)){
            editRePassword.setError("Password not matched!");
            return false;
        }
        return true;
    }

    private void startNextActivity() {
        Intent intent = new Intent(Registration.this, Birthday.class);
        startActivity(intent);
        finish();

    }
}