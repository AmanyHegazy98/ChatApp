package com.amany.chat.data.model;

import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class ProfileUserModel {




    @PrimaryKey(autoGenerate = true)
    Long AutoId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "job")
    private String job;
    @ColumnInfo(name = "image")
    private ImageView person_photo ;

    @ColumnInfo(name = "image_state")
    private ImageView image_state;

    public ProfileUserModel() {
    }

    public ProfileUserModel(Long autoId, String name, String job, ImageView person_photo, ImageView image_state) {
        AutoId = autoId;
        this.name = name;
        this.job = job;
        this.person_photo = person_photo;
        this.image_state = image_state;
    }

    public Long getAutoId() {
        return AutoId;
    }

    public void setAutoId(Long autoId) {
        AutoId = autoId;
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

    public ImageView getPerson_photo() {
        return person_photo;
    }

    public void setPerson_photo(ImageView person_photo) {
        this.person_photo = person_photo;
    }

    public ImageView getImage_state() {
        return image_state;
    }

    public void setImage_state(ImageView image_state) {
        this.image_state = image_state;
    }
}
