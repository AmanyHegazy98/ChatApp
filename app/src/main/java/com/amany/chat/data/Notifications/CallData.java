package com.amany.chat.data.Notifications;

public class CallData {
    private String imageview;
    private String namecallfrom;
    private String sentto;

    public CallData() {
    }

    public CallData(String imageview, String namecallfFrom, String sentto) {
        this.imageview = imageview;
        this.namecallfrom = namecallfFrom;
        this.sentto = sentto;
    }

    public String getImageview() {
        return imageview;
    }

    public void setImageview(String imageview) {
        this.imageview = imageview;
    }

    public String getNamecallfFrom() {
        return namecallfrom;
    }

    public void setNamecallfFrom(String namecallfFrom) {
        this.namecallfrom = namecallfFrom;
    }

    public String getSentto() {
        return sentto;
    }

    public void setSentto(String sentto) {
        this.sentto = sentto;
    }
}
