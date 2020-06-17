package com.example.convo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
            "Content-Type:application/json",
            "Authorization:key=AAAA0cCw9Mg:APA91bHOrSP0GhlPHSQnqFaRGb9bpNJ8z4eCb69jWNfLh4wjYUIiPBVW6UYmvpbiywN04TGZfmLZo4TLLU-y6eUyb5ib3LoNJcBY7vnhGeSwA1m7cWZO7cTol-xuBhDqSi_muXsRn1h3"
    }
    )
    @POST("fcm/send")
    Call<myrespone> sendData(@Body sender body);

}
