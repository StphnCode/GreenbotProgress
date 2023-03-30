package com.example.greenbot_1;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GreenbotAPI {
    @FormUrlEncoded
    @POST("chat")
    Call<ChatResponse> chatWithTheBit(@Field("chatInput") String message);
}

class ChatResponse {
    public String chatBotReply;

    public ChatResponse(String chatBotReply) {
        this.chatBotReply = chatBotReply;
    }
}
