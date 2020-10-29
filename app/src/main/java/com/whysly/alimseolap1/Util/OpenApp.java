package com.whysly.alimseolap1.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonObject;
import com.whysly.alimseolap1.interfaces.MyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class OpenApp {

    public static void openApp(Context context, String redirecting_url, long server_noti_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.67.129.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences pref = context.getSharedPreferences("data", MODE_PRIVATE);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("https://" +redirecting_url);
        intent.setData(uri);
        context.startActivity(intent);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("checked", "true");
        MyService service = retrofit.create(MyService.class);
        Call<JsonObject> call_patchChecked = service.patchChecked(pref.getString("token", ""), String.valueOf(server_noti_id), jsonObject);
        call_patchChecked.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("알림확인패치성공", response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("알림확인패치오류", t.toString());
            }
        });
    }

}
