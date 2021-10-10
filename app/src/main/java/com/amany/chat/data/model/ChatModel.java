package com.amany.chat.data.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class ChatModel {

    @SerializedName("id")
    private String id;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "data ")
    private Long data ;


    public ChatModel() {
    }

    public ChatModel(String id, String image, String name, String job, String message, Long data) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.job = job;
        this.message = message;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }
}
