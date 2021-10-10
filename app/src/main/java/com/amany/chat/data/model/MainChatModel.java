package com.amany.chat.data.model;

import com.google.gson.annotations.SerializedName;

public class MainChatModel {
    @SerializedName("sender")
    private String sender ;
    @SerializedName("receiver")
    private String receiver;
    @SerializedName("message")
    private String message;
    @SerializedName("isSeen")
    private String isSeen;
    @SerializedName("time")
    private Long time;
    @SerializedName("upload")
    private String upload;
    @SerializedName("file")
    private String file;
    @SerializedName("fileName")
    private String fileName;




    public MainChatModel() {
    }

    public MainChatModel(String sender, String receiver, String message, String isSeen, Long time, String upload, String file, String fileName) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
        this.time = time;
        this.upload = upload;
        this.file = file;
        this.fileName = fileName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
