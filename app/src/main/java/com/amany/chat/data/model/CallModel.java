package com.amany.chat.data.model;

import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class CallModel {
    @PrimaryKey(autoGenerate = true)
    Long AutoId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description_call")
    private String message;
    @ColumnInfo(name = "time_of_call")
    private Long time_of_call;
    @ColumnInfo(name = "icon")
    private ImageView icon;
    @ColumnInfo(name = "statue")
    private String statue;
    @ColumnInfo(name = "image_person")
    private ImageView image_person ;
    @ColumnInfo(name = "image_call")
    private ImageView image_call ;

    public CallModel() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime_of_call() {
        return time_of_call;
    }

    public void setTime_of_call(Long time_of_call) {
        this.time_of_call = time_of_call;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public ImageView getImage_person() {
        return image_person;
    }

    public void setImage_person(ImageView image_person) {
        this.image_person = image_person;
    }

    public ImageView getImage_call() {
        return image_call;
    }

    public void setImage_call(ImageView image_call) {
        this.image_call = image_call;
    }
}
