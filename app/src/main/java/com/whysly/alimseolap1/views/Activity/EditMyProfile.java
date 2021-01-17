package com.whysly.alimseolap1.views.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.asksira.dropdownview.DropDownView;
import com.asksira.dropdownview.OnDropDownSelectionListener;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.interfaces.MainInterface;
import com.whysly.alimseolap1.interfaces.MyService;
import com.whysly.alimseolap1.models.Age;
import com.whysly.alimseolap1.models.City;
import com.whysly.alimseolap1.models.Province;
import com.whysly.alimseolap1.models.State;
import com.whysly.alimseolap1.models.User;
import com.whysly.alimseolap1.views.Games.Introduce;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMyProfile extends BaseActivity implements MainInterface.View{
    DropDownView dropDownView;
    DropDownView dropDownView2;
    DropDownView area1;
    DropDownView area2;
    DropDownView area3;
    List<String> yourFilterList;
    List<String> yourFilterList2;
    List<String> listarea1;
    List<String> listarea2;
    List<String> listarea3;
    List<State> data_states;
    List<City> data_cities;
    List<Province> data_provinces;
    JSONObject object;
    SharedPreferences pref;
    TextView area_name_1;
    TextView area_name_2;
    TextView area_name_3;
    TextView agerange1;
    TextView agerange2;
    TextView email;
    TextView nickname;
    Button malebtn;
    Button femalebtn;
    TextView username;
    MyService service;
    Map<String, Integer> province_code;
    String age_code;
    String area_code;
    List<Age> ageList;
    Boolean ageIsSelected = false;
    Boolean areaIsSelected = false;
    Boolean gameReady = false;
    LottieAnimationView loading_dots;
    private static final String CAPTURE_PATH = "/Profile_Alimi";
    private static final int PICK_FROM_CAMERA = 0;

    private static final int PICK_FROM_ALBUM = 1;

    private static final int CROP_FROM_IMAGE = 2;



    private Uri mImageCaptureUri;

    private ImageView iv_UserPhoto;

    private int id_view;

    private String absoultePath;
    CircleImageView ivImage;

    HashMap<String,String> map1 = new HashMap<String,String>();
    HashMap<String,String> map2 = new HashMap<String,String>();
    int from = 0;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_profile);
        malebtn = findViewById(R.id.male);
        femalebtn = findViewById(R.id.female);
        agerange1 = findViewById(R.id.agerange1);
        agerange2 = findViewById(R.id.agerange2);
        area_name_1 = findViewById(R.id.area1);
        area_name_2 = findViewById(R.id.area2);
        area_name_3 = findViewById(R.id.area3);
        loading_dots = findViewById(R.id.loading_dots);
        loading_dots.setVisibility(View.INVISIBLE);

        email = findViewById(R.id.editTextEmail);
        nickname = findViewById(R.id.nickname_edit);
        intent = getIntent();
        intent.getStringExtra("game");



        yourFilterList = new ArrayList<>();
        yourFilterList2 = new ArrayList<>();
        listarea1 = new ArrayList<>();
        listarea2 = new ArrayList<>();
        listarea3 = new ArrayList<>();
        yourFilterList.add("초등학교 저학년");
        yourFilterList.add("초등학교 고학년");
        yourFilterList.add("중학생");
        yourFilterList.add("10대 후반");
        yourFilterList.add("20대");
        yourFilterList.add("30대");
        yourFilterList.add("40대");
        yourFilterList.add("50대");
        yourFilterList.add("60대");
        yourFilterList.add("70대 이상");
        yourFilterList2.add("초반");
        yourFilterList2.add("중반");
        yourFilterList2.add("후반");
        username = findViewById(R.id.username);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.67.129.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pref = getSharedPreferences("data", MODE_PRIVATE);
        service = retrofit.create(MyService.class);
        Intent intent = getIntent();
        String email_text = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        from = intent.getIntExtra("from",0);
        ivImage = findViewById(R.id.profile_pic2);
        // Glide로 이미지 표시하기
        // String imageUrl = pref.getString("profilepic_path",pref.getString("profilepicurl", ""));
        Glide.with(getApplicationContext()).load(pref.getString("profilepic_path",""))
                .centerCrop()
                //.placeholder(R.drawable.alimi_sample)
                .error(R.drawable.alimi_sample)
                .into(ivImage);

        if (from == 1) {
            this.email.setText(email_text);
            this.email.setEnabled(false);
        } else {

            Call<JsonObject> call_userinfo = service.getMe(pref.getString("token", ""));
            call_userinfo.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    System.out.println("등록성공");
                    Log.d("postSignUpSNS", response.toString());
                    Log.d("postSignUpSNS", response.body().toString());
                    try {
                        object = new JSONObject(response.body().toString());
                        username.setText(object.getString("nickname"));
                        nickname.setText(object.getString("nickname"));
                        email.setText(object.getString("email"));
                        email.setEnabled(false);
                        age_code = String.valueOf(object.getInt("age_id"));
                        area_code = String.valueOf(object.getInt("area_id"));
                        String gen = object.getString("gender");
                        String age = object.getJSONObject("age").get("age").toString();
                        JSONObject area = object.getJSONObject("area");
                        if(age_code.equals(1)) {
                            ageIsSelected = false;
                            areaIsSelected = false;
                        } else {
                            ageIsSelected = true;
                            areaIsSelected = true;
                        }
                        area_name_1.setText(area.getString("state"));
                        area_name_2.setText(area.getString("city"));
                        area_name_3.setText(area.getString("province"));
                        agerange1.setText(age);
                        Log.d("나이", age);
                        SharedPreferences.Editor editor = pref.edit();
                        if (object.getString("profile_img").length() > 10) {
                            Glide.with(getApplicationContext()).load(object.getString("profile_img"))
                                    .centerCrop()
                                    //.placeholder(R.drawable.alimi_sample)
                                    .error(R.drawable.alimi_sample)
                                    .into(ivImage);
                            editor.putString("profilepic_path", object.getString("profile_img"));
                            editor.apply();
                            Log.d("파일경로", pref.getString("profilepic_path", ""));

                        }
                        if(gen.equals("M")){
                            malebtn.setSelected(true);
                            malebtn.setTextColor(Color.parseColor("#FFFFFF"));
                            femalebtn.setSelected(false);
                            femalebtn.setTextColor(Color.parseColor("#F2B41B"));
                        }else if (gen.equals("F")){
                            malebtn.setSelected(false);
                            malebtn.setTextColor(Color.parseColor("#FFFFFF"));
                            femalebtn.setSelected(true);
                            femalebtn.setTextColor(Color.parseColor("#F2B41B"));
                        }else if (gen.equals("1")){
                            malebtn.setSelected(false);
                            malebtn.setTextColor(Color.parseColor("#F2B41B"));
                            femalebtn.setSelected(false);
                            femalebtn.setTextColor(Color.parseColor("#F2B41B"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println("등록실패");
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    Log.d("postSignUpSNS", t.getMessage());
                    Log.d("postSignUpSNS", t.toString());
                }
            });

        }

        Call<List<Age>> call_age = service.getAges();
        call_age.enqueue(new Callback<List<Age>>() {
            @Override
            public void onResponse(Call<List<Age>> call, Response<List<Age>> response) {
                ageList = response.body();
                for(int i = 0; i < ageList.size(); i ++){
                    map1.put( ageList.get(i).getAge(), String.valueOf(ageList.get(i).getId()));
                }
                //Log.d("EditProfile_age", age.toString());
            }

            @Override
            public void onFailure(Call<List<Age>> call, Throwable t) {

            }


        });



        Call<List<State>> call = service.getStates();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                data_states = response.body();
                for (int i = 0; i < data_states.size(); i++) {
                    listarea1.add(data_states.get(i).getState());
                }
                Log.d("TEST", data_states.toString());

            }
            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
            }
        });
        //listarea1.add()
        dropDownView = findViewById(R.id.agerange1dropdown);
        dropDownView2 = findViewById(R.id.agerange2dropdown);
        area1 = findViewById(R.id.area1dropdown);
        area2 = findViewById(R.id.area2dropdown);
        area3 = findViewById(R.id.area3dropdown);
        dropDownView2.setDropDownListItem(yourFilterList2);
        dropDownView.setDropDownListItem(yourFilterList);
        area1.setDropDownListItem(listarea1);
        //area2.setDropDownListItem(listarea1);
        //area1.setDropDownListItem();
        dropDownView.setOnSelectionListener(new OnDropDownSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                if (position > 3 && position < 9) {
                    dropDownView2.setVisibility(View.VISIBLE);
                    ageIsSelected = false;
                    dropDownView2.expand(true);
                    agerange1.setText(dropDownView.getFilterTextView().getText());
                } else {
                    dropDownView2.setVisibility(View.INVISIBLE);
                    agerange1.setText(dropDownView.getFilterTextView().getText());
                    age_code = map1.get(agerange1.getText().toString());
                    Log.d("age_code" , agerange1.getText().toString() + age_code);
                    ageIsSelected = true;
                }

                //Do something with the selected position
            }
        });
        dropDownView2.setOnSelectionListener(new OnDropDownSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                agerange1.setText(dropDownView.getFilterTextView().getText() + " " +dropDownView2.getFilterTextView().getText());
                //Do something with the selected position
                ageIsSelected = true;
                age_code = map1.get(agerange1.getText().toString());
                System.out.println(age_code + map1.toString());
            }
        });
        area1.setOnSelectionListener(new OnDropDownSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                area3.setVisibility(view.INVISIBLE);
                listarea2.removeAll(listarea2);
                area2.setPressed(false);
                area2.setSelected(false);
                area2.dispatchSetSelected(false);
                area2.refreshDrawableState();
                area_name_2.setText(null);
                area_name_3.setText(null);
                area_name_1.setText(listarea1.get(position));
                areaIsSelected = false;
                if (area2.getSelectingPosition() != -1) {
                    area2.setPressed(false);
                    area2.setPlaceholderText("시/군/구");
                }
                Call<List<City>> call_cities = service.getCities(String.valueOf(data_states.get(position).getId()));
                call_cities.enqueue(new Callback<List<City>>() {
                    @Override
                    public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                                Log.d("TEST", response.body().toString());
                                data_cities = response.body();
                                Log.d("TEST", data_cities.toString());
                                for (int n = 0; n < data_cities.size(); n++) {
                                    listarea2.add(data_cities.get(n).getCity());
                                }
                                Log.d("TEST", listarea2.toString());
                                area2.setDropDownListItem(listarea2);
                                area2.setVisibility(View.VISIBLE);
                                area2.expand(true);
                    }
                    @Override
                    public void onFailure(Call<List<City>> call, Throwable t) {
                        Log.d("TEST", t.getMessage());
                    }
                });
                //Do something with the selected position
            }
        });


        area2.setOnSelectionListener(new OnDropDownSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                area_name_2.setText(listarea2.get(position));
                area_name_3.setText(null);
                area3.setSelected(false);
                areaIsSelected = false;
                Call<List<Province>> call_province = service.getProvinces(String.valueOf(data_cities.get(position).getId()));
                call_province.enqueue(new Callback<List<Province>>() {
                    @Override
                    public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                        Log.d("TEST", response.body().toString());
                        data_provinces = response.body();
                        Log.d("TEST", data_provinces.toString());
                        listarea3.removeAll(listarea3);
                        province_code =new HashMap<String, Integer>();;
                        for (int n = 0; n < data_provinces.size(); n++) {
                            map2.put(data_provinces.get(n).getProvince(), String.valueOf(data_provinces.get(n).getId()));
                            listarea3.add(data_provinces.get(n).getProvince());
                        }
                        for (Map.Entry<String, String> entry : map2.entrySet()) {
                            System.out.println("[Key]:" + entry.getKey() + " [Value]:" + entry.getValue());
                        }
                        Log.d("TEST", listarea2.toString());
                        area3.setDropDownListItem(listarea3);
                        area3.setVisibility(View.VISIBLE);
                        area3.expand(true);
                    }
                    @Override
                    public void onFailure(Call<List<Province>> call, Throwable t) {
                        Log.d("TEST", t.getMessage());
                    }
                });
                //Do something with the selected position
            }
        });
        area3.setOnSelectionListener(new OnDropDownSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                area_name_3.setText(listarea3.get(position));
                area_code = map2.get(area_name_3.getText().toString());
                areaIsSelected = true;
            }
        });
        malebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                malebtn.setSelected(true);
                malebtn.setTextColor(Color.parseColor("#FFFFFF"));
                femalebtn.setSelected(false);
                femalebtn.setTextColor(Color.parseColor("#F2B41B"));
            }
        });
        femalebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femalebtn.setSelected(true);
                femalebtn.setTextColor(Color.parseColor("#FFFFFF"));
                malebtn.setSelected(false);
                malebtn.setTextColor(Color.parseColor("#F2B41B"));

            }
        });
//        ivImage = findViewById(R.id.profile_pic2);
//        // Glide로 이미지 표시하기
//       // String imageUrl = pref.getString("profilepic_path",pref.getString("profilepicurl", ""));
//            Glide.with(getApplicationContext()).load(pref.getString("profilepic_path",""))
//                    .centerCrop()
//                    .placeholder(R.drawable.alimi_sample)
//                    //.error(R.drawable.alimi_sample)
//                    .into(ivImage)
//            ;


    }

    public void onEditClick(View view) {
        switch (view.getId()) {
            case R.id.btn_UploadPicture:
                onPicClick();
                break ;
            case R.id.edit_profile_submit_btn :
                if(agerange2.getVisibility() != view.VISIBLE || area_name_3.getVisibility() != view.VISIBLE
                        || email.getVisibility() != view.VISIBLE || nickname.getVisibility() != view.VISIBLE)
                {
                }
                progressON();
                saveAllandPatch();

                //Toast.makeText(this, "모든 정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                break ;
            case R.id.back_arrow :
                onBackPressed();
                Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
                break ;
        }
    }

    public void saveAllandPatch(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("agerange1", agerange1.getText().toString());
        editor.putString("agerange2",agerange2.getText().toString());
        editor.putString("area_name_1",area_name_1.getText().toString());
        editor.putString("area_name_2",area_name_2.getText().toString());
        editor.putString("area_name_3",area_name_3.getText().toString());
        editor.putString("username", username.getText().toString());
        if (malebtn.isSelected()) {
            editor.putString("gender","M");
        }
        if (femalebtn.isSelected()) {
            editor.putString("gender","F");
        }
        else  {
            editor.putString("gender","1");
        }
        editor.putString("email", email.getText().toString());
        editor.apply();

        if (!areaIsSelected){
            Toast.makeText(getApplicationContext(), "지역을 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        } else if(!ageIsSelected){
            Toast.makeText(getApplicationContext(), "나이를 입력해 주세요.", Toast.LENGTH_LONG).show();
        } else if((malebtn.isSelected() || femalebtn.isSelected()) == false){
            Toast.makeText(getApplicationContext(), "성별을 입력해 주세요.", Toast.LENGTH_LONG).show();
        } else if(email.getText().length() == 0 ){
            Toast.makeText(getApplicationContext(), "이메일 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
        } else if(nickname.length() == 0){
            Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_LONG).show();
        } else {
            loading_dots.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            File file = new File(pref.getString("new_profilepic_path", ""));
            RequestBody nickname = RequestBody.create(MediaType.parse("text/plain"),
                    this.nickname.getText().toString());
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),
                    "M");
            System.out.println(age_code + "헤헤" + area_code);
//            age_code.get(agerange1 +" "+ agerange2).toString()
            RequestBody age = RequestBody.create(MediaType.parse("text/plain"), age_code);
            RequestBody area = RequestBody.create(MediaType.parse("text/plain"), area_code);
            //RequestBody profile_img = RequestBody.create(MediaType.parse("multipart/form-data"),file);
//            RequestBody password = RequestBody.create(MediaType.parse("text/plain"),
//                    intent.getStringExtra("password"));
            JsonObject jsonObject = new JsonObject();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part profile_img = MultipartBody.Part.createFormData("profile_img", file.getName(), requestFile);
            //MultipartBody.Part filePart = MultipartBody.Part.createFormData("profile_img", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            if(pref.getString("new_profilepic_path", "").equals("")){
                profile_img = null;
            }
            Map<String, RequestBody> map = new HashMap<>();
            map.put("gender", gender);
            map.put("nickname", nickname);

//            map.put("password", password);
            //@Part MultipartBody.Part filePart

            if (intent.getStringExtra("from").equals("1")){
                Log.d("내정보수정", "1");
                map.put("age_id", age);
                map.put("area_id", area);


                RequestBody email = RequestBody.create(MediaType.parse("text/plain"),
                        intent.getStringExtra("email"));

                map.put("email", email);

                RequestBody confirm_password = RequestBody.create(MediaType.parse("text/plain"),
                        intent.getStringExtra("password"));
                map.put("confirm_password", confirm_password);

                RequestBody password = RequestBody.create(MediaType.parse("text/plain"),
                        intent.getStringExtra("password"));

                map.put("password", password);
                Call<User> call_signMeUp = service.signMeUp( map, profile_img);
                call_signMeUp.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("회원가입", response.toString());
                        Log.d("회원가입", response.body().toString());
                        Log.d("내정보수정", "2");
                        editor.apply();
                        signInAfterUp(intent.getStringExtra("email"), intent.getStringExtra("password"));
                        progressON();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("내정보수정", "3");
                        System.out.println("등록실패");
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

            } else {
                map.put("age", age);
                map.put("area", area);
                Call<JsonObject> call_patchMe = service.patchMe(pref.getString("token", ""), map, profile_img);
                        call_patchMe.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.d("4" , response.body().toString());

                                try {
                                    JSONObject object = new JSONObject(response.body().toString());
                                    if (!object.getBoolean("finished")) {
                                        game();
                                    } else {
                                        complete();

                                    }

                                }catch (Exception e) {
                                    complete();
                                }
                        // 첫 로그인(회원가입의 경우)
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.d("내정보수정", "5");
                                System.out.println("등록실패");
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                }
            }
        }

        public void signInAfterUp(String id, String password) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://118.67.129.104/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MyService service = retrofit.create(MyService.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", id);
            jsonObject.addProperty("password", password);
            Call<JsonObject> call_signIn = service.postSignIn(jsonObject);
            call_signIn.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token",object.getString("token"));
                        editor.putString("uid", object.getString("id"));
                        editor.putString("login_method", "default");
                        editor.apply();
                        game();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });



        }

    public void complete(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                loading_dots.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "정보가 저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
        if (this.intent.equals("false")) {
            Intent intent = new Intent(this, Introduce.class);
            startActivity(intent);
            finish();
        }
    }

    public void game(){
        Intent intent = new Intent(getApplicationContext(), Introduce.class);
        startActivity(intent);
        finish();
        loading_dots.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "정보가 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onPicClick() {


            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    doTakePhotoAction();

                }

            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    doTakeAlbumAction();

                }

            };


            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }

            };

            new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("취소", cancelListener)
                    .setNegativeButton("앨범선택", albumListener)
                    .show();
    }



    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기

    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // 임시로 사용할 파일의 경로를 생성

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));


        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기

    {

        // 앨범 호출

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        if(resultCode != RESULT_OK)

            return;


        switch(requestCode)

        {

            case PICK_FROM_ALBUM:

            {

                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.

                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();

                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());

            }


            case PICK_FROM_CAMERA:

            {

                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.

                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");

                intent.setDataAndType(mImageCaptureUri, "image/*");


                // CROP할 이미지를 200*200 크기로 저장

                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기

                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기

                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율

                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율

                intent.putExtra("scale", true);

                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_IMAGE); // CROP_FROM_CAMERA case문 이동

                break;

            }


            case CROP_FROM_IMAGE: {

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.

                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에

                // 임시 파일을 삭제합니다.

                if (resultCode != RESULT_OK) {

                    return;

                }


                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+

                        "/Profile_Alimi/"+System.currentTimeMillis()+".jpg";

                if(extras != null)

                {

                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                    //iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌



                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.

                    absoultePath = filePath;



                    break;


                }

                // 임시 파일 삭제

                File f = new File(mImageCaptureUri.getPath());

                if(f.exists())

                {

                    f.delete();

                }

            }





        }

    }


    private void storeCropImage(Bitmap bitmap, String filePath) {

        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH;

        File directory_SmartWheel = new File(dirPath);


        if(!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)

            directory_SmartWheel.mkdir();

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("new_profilepic_path", filePath);

        editor.apply();
        File copyFile = new File(filePath);
        Log.d("path", copyFile.getAbsolutePath() + pref.getString("profilepic_path", ""));


        BufferedOutputStream out = null;


        try {


            copyFile.createNewFile();

            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,

                    Uri.fromFile(copyFile)));

            MediaScannerConnection.scanFile( getApplicationContext(),

                    new String[]{copyFile.getAbsolutePath()},

                    null,

                    new MediaScannerConnection.OnScanCompletedListener(){

                        @Override

                        public void onScanCompleted(String path, Uri uri) {

                            Log.v("File scan", "file:" + path + "was scanned seccessfully");

                        }

                    });



            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        Log.d("file path", filePath);
        Uri imageUri = Uri.fromFile(copyFile);
        Glide.with(getApplicationContext()).load(imageUri)
                .centerCrop()
                //.placeholder(R.drawable.alimi_sample)
                //.error(R.drawable.alimi_sample)
                .into(ivImage);


    }
}
