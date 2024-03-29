package com.whysly.alimseolap1.views.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.JsonObject;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.interfaces.MyService;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class SettingsFragment extends Fragment {
    FirebaseRemoteConfig firebaseRemoteConfig;
    private static final String LATEST_VERSION_KEY = "latest_version";
    public String latest_version;
    TextView version_stat;
    Button update_button;
    Button version_check;
    SharedPreferences pref;
    TextView username;

    public void Oncreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void versionCheck() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        displayWelcomeMessage();
    }

    private void displayWelcomeMessage(){
        //받아온 데이터 중 "latest_version" 라는 이름의 매개변수 값을 가져온다.
        latest_version = firebaseRemoteConfig.getString(LATEST_VERSION_KEY);
        SharedPreferences pref = getContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("latestVersionInfo", latest_version);
        editor.apply();
        Toast.makeText(getContext(), latest_version,
                Toast.LENGTH_SHORT).show();
        Log.i(TAG, "파이어베이스에서 받아온 최신 버전 정보 : "+latest_version);
        if(!MyAppVersion().equals(latest_version)) {
            version_stat.setText("업데이트 해주세요.(" + MyAppVersion() + ") 최신버전: " + latest_version);
            version_check.setVisibility(View.GONE);
            update_button.setVisibility(View.VISIBLE);
        } else {
            version_stat.setText("앱이 최신버전 입니다." + latest_version);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, null);
        update_button = view.findViewById(R.id.update_button);
        version_check = view.findViewById(R.id.version_check);
        CircleImageView ivImage = view.findViewById(R.id.profile_pic2);
        version_stat = view.findViewById(R.id.version_status);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        latest_version = firebaseRemoteConfig.getString(LATEST_VERSION_KEY);
        System.out.println("981217" + latest_version);
        pref = getContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
        String version = pref.getString("latestVersionInfo", "");
        username = view.findViewById(R.id.username_display);
        version_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versionCheck();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getContext().getPackageName()));
                startActivity(intent);
            }
        });
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.67.129.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyService service = retrofit.create(MyService.class);
        Call<JsonObject> call_userinfo = service.getMe(pref.getString("token", ""));
        call_userinfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("등록성공");
                Log.d("postSignUpSNS", response.toString());
                Log.d("postSignUpSNS", response.body().toString());
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    username.setText(object.getString("nickname"));
                    SharedPreferences.Editor editor = pref.edit();
                    if (object.getString("profile_img").length() > 10) {
                        Glide.with(getContext()).load(object.getString("profile_img"))
                                .centerCrop()
                                //.placeholder(R.drawable.alimi_sample)
                                .error(R.drawable.alimi_sample)
                                .into(ivImage);
                        editor.putString("profilepic_path",  object.getString("profile_img"));
                        editor.apply();
                        Log.d("파일11경로", object.getString("profile_img"));

                    } else {
                        Glide.with(getContext()).load(pref.getString("profilepic_path",""))
                                .centerCrop()
                                //.placeholder(R.drawable.alimi_sample)
                                .error(R.drawable.alimi_sample)
                                .into(ivImage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("등록실패");
                Toast.makeText(getContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                Log.d("postSignUpSNS", t.getMessage());
                Log.d("postSignUpSNS", t.toString());
            }
        });

//        File file = new File(pref.getString("profilepic_path",pref.getString("profilepicurl", "")));
//        Log.d("pfpic_url", pref.getString("profilepic_path",pref.getString("profilepicurl", "")));
//        Uri imageUri = Uri.fromFile(file);

        // Glide로 이미지 표시하기
        // String imageUrl = pref.getString("profilepic_path",pref.getString("profilepicurl", ""));
        // Glide로 이미지 표시하기
        Log.d("profilepic_path", pref.getString("profilepic_path",""));
//        Glide.with(getContext()).load(pref.getString("profilepic_path",""))
//                .centerCrop()
//                //.placeholder(R.drawable.alimi_sample)
//                //.error(R.drawable.alimi_sample)
//                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                .into(ivImage)
//        ;


        return view;
    }

    private String MyAppVersion(){

        PackageInfo pi = null;
        try {
            pi = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pi.versionName;
        return version;
    }

}
