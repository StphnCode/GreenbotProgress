package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StartConvo extends AppCompatActivity {

    private static final String CHAT_LIST_STATE_KEY = "chat_list_state";
    private ArrayList<ChatMessage> chatList = new ArrayList<>();
    private AdapterGreenbot adapter = new AdapterGreenbot(chatList);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference conversationRef = firestore.collection("conversations");
    CollectionReference chatbotRepliesRef;

    FirebaseUser currentUser;
    FirebaseFirestore database;
    FirebaseDatabase fDatabase;
    DatabaseReference ref;
    ImageButton btnMenu, btnCall, btnInfo, btnSend;
    RecyclerView rvChatList;
    private String userUID = "";
    private String name = "";
    String currentDateTime = "";
    private Parcelable recyclerViewState;

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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = currentUser.getUid();
        database = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyy - hh:mm a", Locale.getDefault());
        currentDateTime = dateFormat.format(new Date());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.237:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GreenbotAPI apiService = retrofit.create(GreenbotAPI.class);


        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rvChatList.setLayoutManager(llm);

        // Restore the state of the RecyclerView if available
        if (savedInstanceState != null) {
            chatList = savedInstanceState.getParcelableArrayList(CHAT_LIST_STATE_KEY);
        }
        rvChatList.setAdapter(adapter);

        chatbotRepliesRef = firestore.collection("Chatbot Replies");

        // Create a reference to the "Chatbot Replies" collection
        CollectionReference chatbotRepliesRef = firestore.collection("Chatbot Replies");

        // Load chatbot responses only once when the activity is created
        loadChatMessagesFromFirestore();

        chatbotRepliesRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ChatMessage> chatbotResponseList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Retrieve and add chatbot response data to chatbotResponseList
                        ChatbotResponse chatbotResponse = document.toObject(ChatbotResponse.class);
                        chatbotResponseList.add(new ChatMessage(chatbotResponse.getMessage(), false)); // Chatbot response
                    }

                    chatList.addAll(chatbotResponseList);

                    adapter.notifyDataSetChanged();
                    rvChatList.scrollToPosition(chatList.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error loading chatbot responses: " + e);
                });

        ref = fDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String dbUID = dataSnapshot.child("userUID").getValue(String.class);
                    if (userUID.equals(dbUID)) {
                        name = dataSnapshot.child("name").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        rvChatList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerViewState != null) {
                    rvChatList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }

                rvChatList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        rvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });



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

        // Inside `onCreate` method, when adding a new message:
        btnSend.setOnClickListener(v -> {
            if (enterMsg.getText().toString().isEmpty()) {
                Toast.makeText(StartConvo.this, "Please enter a text", Toast.LENGTH_LONG).show();
                return;
            }

            String userMessage = enterMsg.getText().toString();

            // Add the user's message to the chatList and update the UI
            ChatMessage userChatMessage = new ChatMessage(userMessage, true);
            chatList.add(userChatMessage);
            adapter.notifyItemInserted(chatList.size() - 1);
            rvChatList.scrollToPosition(chatList.size() - 1);
            enterMsg.getText().clear();

            //asynchronously add the user's message to Firestore
            addMessageToFirestore(userMessage, true);

            // Call the chatbot API to get the chatbot's response
            Call<ChatResponse> call = apiService.getChatbotResponse(userMessage);
            call.enqueue(callBack);
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CHAT_LIST_STATE_KEY, rvChatList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recyclerViewState = savedInstanceState.getParcelable(CHAT_LIST_STATE_KEY);
    }


    //function to save chatbot response in the "Chatbot Replies" collection
    private void saveChatbotResponseInFirestore(String chatbotResponse) {
        ChatbotResponse chatbotResponseObj = new ChatbotResponse();
        chatbotResponseObj.setUserId("Chatbot");
        chatbotResponseObj.setUserName("Chatbot");
        chatbotResponseObj.setMessage(chatbotResponse);
        chatbotResponseObj.setTimestamp(currentDateTime);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference chatbotRepliesRef = firestore.collection("Chatbot Replies");

        chatbotRepliesRef.add(chatbotResponseObj)
                .addOnSuccessListener(documentReference -> {
                    // Chatbot's response is successfully saved in the "Chatbot Replies" collection
                })
                .addOnFailureListener(e -> {
                    // Handle the error if the chatbot's response couldn't be saved
                    Log.e("FirestoreError", "Error saving chatbot response: " + e);
                });
    }

    private void addMessageToFirestore(String userMessage, boolean isUserMessage) {
        // Generate a unique message ID
        String uniqueMessageId = generateUniqueMessageId();

        // Create a new Conversation object for the user's message
        Conversation userConversation = new Conversation();
        userConversation.setUserId(currentUser.getUid());
        userConversation.setUserName(name);
        userConversation.setMessage(userMessage);
        userConversation.setTimestamp(currentDateTime);
        userConversation.setMessageId(uniqueMessageId);

        if (isUserMessage) {
            userConversation.setSender("User");
        } else {
            userConversation.setSender("Chatbot");
        }

        // Add the user's message to Firestore with the correct timestamp
        conversationRef.add(userConversation)
                .addOnSuccessListener(documentReference -> {
                    // User's message is successfully saved in Firestore
                })
                .addOnFailureListener(e -> {
                    // Handle the error if the user's message couldn't be saved
                    Toast.makeText(StartConvo.this, "Error sending message", Toast.LENGTH_LONG).show();
                });
    }

    private String generateUniqueMessageId() {
        long timestamp = System.currentTimeMillis();
        long randomValue = new Random().nextInt(1000);
        return timestamp + "_" + randomValue;
    }

    private String getChatbotResponses() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatbotResponses", MODE_PRIVATE);
        return sharedPreferences.getString("responses", "");
    }

    private void loadChatMessagesFromFirestore() {
        conversationRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("FirestoreError", "Error loading chat messages: " + e);
                        return;
                    }

                    chatList.clear(); // Clear the existing chat list

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Conversation conversation = document.toObject(Conversation.class);
                        chatList.add(new ChatMessage(conversation.getMessage(), currentUser.getUid().equals(conversation.getUserId())));
                    }

                    // Fetch chatbot responses from "Chatbot Replies" collection
                    chatbotRepliesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshotsChatbot -> {
                                for (QueryDocumentSnapshot documentChatbot : queryDocumentSnapshotsChatbot) {
                                    ChatbotResponse chatbotResponse = documentChatbot.toObject(ChatbotResponse.class);
                                    chatList.add(new ChatMessage(chatbotResponse.getMessage(), false)); // Chatbot response
                                }

                                adapter.notifyDataSetChanged();
                                rvChatList.post(() -> {
                                    rvChatList.scrollToPosition(chatList.size() - 1);
                                });
                            })
                            .addOnFailureListener(eChatbot -> {
                                Log.e("FirestoreError", "Error loading chatbot responses: " + eChatbot);
                            });
                });
    }


    private Callback<ChatResponse> callBack = new Callback<ChatResponse>() {
        @Override
        public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                String chatbotResponse = response.body().chatBotReply;

                // Log the chatbot response
                Log.d("ChatbotResponse", "Chatbot Response: " + chatbotResponse);

                // To save the chatbot's response in Firestore
                saveChatbotResponseInFirestore(chatbotResponse);

                // Update the UI with the chatbot's response
                ChatMessage chatbotChatMessage = new ChatMessage(chatbotResponse, false);
                chatList.add(chatbotChatMessage);
                adapter.notifyItemInserted(chatList.size() - 1);
                rvChatList.scrollToPosition(chatList.size() - 1);
            } else {
                // Handle errors when chatbot response retrieval fails
                Log.e("ChatbotResponseError", "Failed to get chatbot response");
            }
        }

        @Override
        public void onFailure(Call<ChatResponse> call, Throwable t) {
            // Handle Retrofit or network errors
            Log.e("ChatbotError", t.getMessage());
        }
    };
    

}