package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;



public class Birthday extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button nxtButton;

    int selectedYear, selectedMonth, selectedDay, currentYear, currentMonth, currentDay, age;
   // DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://greenbot-1-default-rtdb.firebaseio.com/");
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth mFirebaseAuth;
    Member member;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_birthday);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        nxtButton = findViewById(R.id.nxtButton);
        member = new Member();
        ref = database.getInstance().getReference().child("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();


        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = calculateAge(currentYear, currentDay, selectedYear, selectedDay);
                if(age < 17 || age >= 2023){
                    Utility.showToast(Birthday.this, "Age must be greater than or equal 17");
                    return;

                }
                long id = maxId;
                ref.child(String.valueOf(id)).child("age").setValue(age);
                member.setAge(age);
                String userUID = mFirebaseAuth.getCurrentUser().getUid();
                ref.child(String.valueOf(id)).child("userUID").setValue(userUID);
                member.setUserUID(userUID);
                openPrivacy();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void openPrivacy(){
        Intent intent = new Intent(Birthday.this, Privacy.class);
        startActivity(intent);
    }


    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        currentDay = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(currentDay, currentMonth, currentYear);
    }


    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                selectedYear = year;
                selectedMonth = month;
                selectedDay = day;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEPT";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    int calculateAge(int currentYear, int currentDay, int selectedYear, int selectedDay){
        int age = currentYear - selectedYear;
        if(currentDay < selectedDay){
            age--;
        }
        return age;
    }
}