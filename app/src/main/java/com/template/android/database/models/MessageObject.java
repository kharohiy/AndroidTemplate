package com.template.android.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageObject {

    @SerializedName("id")
    private long id;
    @SerializedName("message")
    private String message;

    // Local Elements
    @Expose(serialize = false, deserialize = false)
    private long createdUnix;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatedUnix() {
        return createdUnix;
    }

    public void setCreatedUnix(long createdUnix) {
        this.createdUnix = createdUnix;
    }
}
