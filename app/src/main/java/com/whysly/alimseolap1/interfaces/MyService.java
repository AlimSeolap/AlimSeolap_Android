package com.whysly.alimseolap1.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.whysly.alimseolap1.models.Age;
import com.whysly.alimseolap1.models.City;
import com.whysly.alimseolap1.models.Message;
import com.whysly.alimseolap1.models.Province;
import com.whysly.alimseolap1.models.State;
import com.whysly.alimseolap1.models.User;
import com.whysly.alimseolap1.models.UserKeyword;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyService {

    @POST("api/noti_data/")
    Call<JsonObject> createPost(@Body JsonObject jsonObject);

    @POST("sign-in")
    Call<JsonObject> postSignIn(@Body JsonObject jsonObject);

    @POST("auth")
    Call<JsonObject> postSignUpSNS(@Body JsonObject jsonObject);

    @POST("messages")
    Call<JsonElement> postGameMessageEval(@Header("Authorization") String value, @Body JsonObject jsonObject);

    @Multipart
    @POST("sign-up")
    Call<User> signMeUp(@PartMap Map<String, RequestBody> m, @Part MultipartBody.Part profile_img);





    @GET("ages")
    Call<List<Age>> getAges();

    @GET("areas/states")
    Call<List<State>> getStates();

    @GET("areas/cities")
    Call<List<City>> getCities(@Query("parent_id") String id);

    @GET("areas/provinces")
    Call<List<Province>> getProvinces(@Query("parent_id") String id);

    @GET("me")
    Call<JsonObject> getMe(@Header("Authorization") String value);

    @GET("messages")
    Call<List<Message>> getMessages(@Header("Authorization") String value);

    @GET("user/messages")
    Call<List<Message>> getUserMessages(@Header("Authorization") String value);

    @GET("users/keywords")
    Call<List<UserKeyword>> getUsersKeyword(@Header("Authorization") String value);

//    @PATCH("me")
//    Call<JsonObject> patchMe(@Header("Authorization") String value, @Body JsonObject jsonObject);




    @Multipart
    @PATCH("me")
    Call<JsonObject> patchMe(@Header("Authorization") String value, @PartMap Map<String, RequestBody> m, @Part MultipartBody.Part profile_img);


    @PATCH("user/messages/{id}/checked")
    Call<JsonObject> patchChecked(@Header("Authorization") String value, @Path("id") String id, @Body JsonObject jsonObject);

    @PATCH("user/messages/{id}/evaluation")
    Call<JsonObject> patchEvaluation(@Header("Authorization") String value , @Path("id") String id, @Body JsonObject jsonObject);

}