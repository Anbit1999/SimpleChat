package com.dmt.tuan.simplechat.fragment;

import com.dmt.tuan.simplechat.Notifications.MyResponse;
import com.dmt.tuan.simplechat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAhr-p5SU:APA91bE2nF4ERQ6Bpna984PRHreJQi9_7KB_x5HahPJ2lDgE2emtlKahVvseehqGFzBffaxKq6UqQMtz-nlHtuTFTGCU3Dj5qgVipXzMhcbaGwdb2C9gJl_txPfdQstNYI1Oy3GvBLL0"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
