package com.amany.chat.data.model;

import com.google.gson.annotations.SerializedName;

public class PeopleModel {


    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName( "image")
    private String person_photo ;
    @SerializedName("online")
    private String online;
    @SerializedName("image_active")
    private int image_active;

    public PeopleModel() {
    }

    public PeopleModel( String name, String job, String person_photo, String online, int image_active) {

        this.name = name;
        this.job = job;
        this.person_photo = person_photo;
        this.online = online;
        this.image_active = image_active;
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

    public String getPerson_photo() {
        return person_photo;
    }

    public void setPerson_photo(String person_photo) {
        this.person_photo = person_photo;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public int getImage_active() {
        return image_active;
    }

    public void setImage_active(int image_active) {
        this.image_active = image_active;
    }
}
