package com.amany.chat.data.Notifications;

public class SenderCall {
    private CallData data;
    private String to;

    public SenderCall() {
    }

    public SenderCall(CallData data, String to) {
        this.data = data;
        this.to = to;
    }

    public CallData getData() {
        return data;
    }

    public void setData(CallData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
