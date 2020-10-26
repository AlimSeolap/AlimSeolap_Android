package com.whysly.alimseolap1.views.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonObject;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.interfaces.MyService;
import com.whysly.alimseolap1.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.30.1.18:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyService service = retrofit.create(MyService.class);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
        if (pref.getString("token", "").equals("")) {
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            intent.putExtra("from", "loading");
            startLoading(intent);
        }else {
            Call<JsonObject> call_userinfo = service.getMe(pref.getString("token", ""));
            call_userinfo.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    System.out.println("등록성공");
                    Log.d("postSignUpSNS", response.toString());
                    Log.d("postSignUpSNS", response.body().toString());
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        if (object.getString("finished").equals("false")) {
                            Intent intent = new Intent(LoadingActivity.this, EditMyProfile.class);
                            intent.putExtra("from", "loading");

                            startLoading(intent);
                        } else {
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            intent.putExtra("from", "loading");
                            startLoading(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("postSignUpSNS", t.getMessage());
                    Log.d("postSignUpSNS", t.toString());
                }
            });
        }

    }
    private void startLoading(Intent intent) {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 3000);
    }



}
