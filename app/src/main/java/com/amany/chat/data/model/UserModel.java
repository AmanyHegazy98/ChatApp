package com.amany.chat.data.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    private String id;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName("status")
    private String status;
    @SerializedName("search")
    private String search;


    public UserModel() {
    }

    public UserModel(String id, String image, String name, String job, String status, String search) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.job = job;
        this.status = status;
        this.search = search;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}

