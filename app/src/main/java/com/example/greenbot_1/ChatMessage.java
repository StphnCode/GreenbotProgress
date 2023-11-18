package com.example.greenbot_1;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {
    private String message;
    private boolean isUserMessage;

    public ChatMessage(String message, boolean isUserMessage) {
        this.message = message;
        this.isUserMessage = isUserMessage;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }


    // Implement the Parcelable methods
    protected ChatMessage(Parcel in) {
        message = in.readString();
        isUserMessage = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (isUserMessage ? 1 : 0));
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}

