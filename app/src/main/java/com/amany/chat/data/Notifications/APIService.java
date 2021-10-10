package com.amany.chat.data.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAFQKo5Rg:APA91bHguAj_EnR7G_xeCdD5eKlw3eXk104N9y6YbyveSt7H06dDmnahzVUKz7SdDzgELW3_GTJxqXVPe-xkI8cg9zg0kRKXblIe-FcTG8rAvOgxtMmHJ-xKTRpJ7Z2PPcSHmVNIPyU8"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAFQKo5Rg:APA91bHguAj_EnR7G_xeCdD5eKlw3eXk104N9y6YbyveSt7H06dDmnahzVUKz7SdDzgELW3_GTJxqXVPe-xkI8cg9zg0kRKXblIe-FcTG8rAvOgxtMmHJ-xKTRpJ7Z2PPcSHmVNIPyU8"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendCall(@Body SenderCall body);

}
