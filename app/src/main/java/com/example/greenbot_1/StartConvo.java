package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StartConvo extends AppCompatActivity {

    private AdapterGreenbot adapterGreenbot = new AdapterGreenbot();
    List<String> list;

    RecyclerView recyclerView;
    EditText messageEditText;

    ImageButton btnMenu, btnCall, btnInfo, btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_convo);

        btnMenu = findViewById(R.id.menuBtn);
        btnCall = findViewById(R.id.callBtn);
        btnInfo = findViewById(R.id.infoBtn);
        btnSend = findViewById(R.id.sendBtn);
        EditText enterMsg = findViewById(R.id.enterMsg);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.254.106:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GreenbotAPI apiService = retrofit.create(GreenbotAPI.class);


        RecyclerView rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setAdapter(adapterGreenbot);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rvChatList.setLayoutManager(llm);


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, Menu.class);
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, SOS_UI.class);
                startActivity(intent);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, SelfCare.class);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener((v)->{
            if(enterMsg.getText().toString().isEmpty()) {
                Toast.makeText(StartConvo.this, "Please enter a text", Toast.LENGTH_LONG).show();
                return;
            }

            String message = enterMsg.getText().toString();
            adapterGreenbot.addChatToList(message);
            rvChatList.smoothScrollToPosition(adapterGreenbot.getItemCount());


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    apiService.chatWithTheBit(message).enqueue(callBack);
                }
            };

            new Thread(runnable).start();

            enterMsg.getText().clear();
        });
    }

    private Callback<ChatResponse> callBack = new Callback<ChatResponse>() {
        @Override
        public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                adapterGreenbot.addChatToList((response.body().chatBotReply));
                Log.d("ChatbotResponse", response.body().chatBotReply); // add this line to log the chatbot response
            } else {
                Toast.makeText(StartConvo.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ChatResponse> call, Throwable t) {
            Toast.makeText(StartConvo.this, "Something went wrong", Toast.LENGTH_LONG).show();
            Log.e("ChatbotError", t.getMessage()); // add this line to log the error message
        }
    };
}