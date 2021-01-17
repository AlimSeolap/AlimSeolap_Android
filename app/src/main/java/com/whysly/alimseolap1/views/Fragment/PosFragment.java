package com.whysly.alimseolap1.views.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.Util.Z_value;
import com.whysly.alimseolap1.interfaces.MyService;
import com.whysly.alimseolap1.models.UserKeyword;
import com.whysly.alimseolap1.views.Activity.WebViewActivity;
import com.whysly.alimseolap1.views.SmoothNonSwipeableViewPager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosFragment extends Fragment {
    private SmoothNonSwipeableViewPager viewPager;
    LottieAnimationView animationView;
    LottieAnimationView pop_anim;
    private int currentPosition;
    WebView webview;
    WebSettings settings;
    final public Handler handler1 = new Handler();
    Retrofit retrofit;
    String pass;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.structure_gift, null);

        webview = (WebView) view.findViewById(R.id.webViewmain);
        settings = webview.getSettings();
        //settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);


//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//                                      @Override
//                                      public boolean onTouch(View v, MotionEvent event){
//                                          return true;
//                                      }
//                                  }
//        );

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.empty_noti);
        webview.loadUrl("file:///android_asset/index.html");
        retrofit = new Retrofit.Builder()
                .baseUrl("http://118.67.129.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyService service = retrofit.create(MyService.class);
        SharedPreferences pref = getContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
        Call<List<UserKeyword>> call_user_keyword = service.getUsersKeyword(pref.getString("token", ""));

        call_user_keyword.enqueue(new Callback<List<UserKeyword>>() {

            @Override
            public void onResponse(Call<List<UserKeyword>> call, Response<List<UserKeyword>> response) {
                List<UserKeyword> keywords = response.body();
                HashMap<String, Integer> map = new HashMap<>();
                HashMap<String, Integer> map2 = new HashMap<>();
                //positive z-score 구하기
                for (int i = 0; i < keywords.size(); i++) {
                    map.put(keywords.get(i).getKeyword(), keywords.get(i).getPositive_value_count());
                    System.out.println(keywords.get(i).getPositive_value_count());
                }

                for (int i = 0; i < keywords.size(); i++) {
                    map2.put(keywords.get(i).getKeyword(), keywords.get(i).getNegative_value_count());
                    System.out.println(keywords.get(i).getNegative_value_count());
                }

                Z_value one = new Z_value();
                HashMap<String, Double> p_value = one.getZ_score(map);
                HashMap<String, Double> n_value = one.getZ_score(map2);

                for (int i = 0; i < keywords.size(); i++) {
                    keywords.get(i).setZ_value(p_value.get(keywords.get(i).getKeyword()) - n_value.get(keywords.get(i).getKeyword()));
                    System.out.println("z_value for" + keywords.get(i).getKeyword() + ": " + keywords.get(i).getZ_value());
                }
                //Toast myToast = Toast.makeText(getContext(), p_value.toString(), Toast.LENGTH_SHORT);

                //myToast.show();
                final Comparator<UserKeyword> comp = (k1, k2) -> Double.compare(k1.getZ_value(), k2.getZ_value());
                List<UserKeyword> sortedList = keywords.stream()
                        .sorted(comp)
                        .collect(Collectors.toList());
                try {
                    double a = sortedList.get(sortedList.size() - 1).getZ_value();
                    double b = -sortedList.get(sortedList.size() - 1).getZ_value() + sortedList.get(0).getZ_value();
                    for (int i = 0; i < sortedList.size(); i++) {
                        double c = sortedList.get(i).getZ_value();
                        int d = (int) Math.round((c - a) * (5 / b) + 1);
                        sortedList.get(i).setZ_value(d);
                    }
                } catch (Exception e){

                }
                String sb = "\"word\":\"freq\"";


                if (keywords == null) {
                } else if (keywords.size() < 20) {
                    for (int i = 0; i < keywords.size(); i++) {
                        //sb.append(",\""+ sortedList.get(i).getKeyword() + "\":" + 8);
                        sb = sb + ",\"" + sortedList.get(i).getKeyword() + "\":" + (i + 1);
                        System.out.println("sorted list is: " + sortedList.get(i).getZ_value());
                    }
                } else {
                    for (int i = 0; i < 20; i++) {
                        sb = sb + ",\"" + sortedList.get(i).getKeyword() + "\":" + sortedList.get(i).getFinal_value_count();
                    }
                }
                Log.d("워드클라우드 키워드셋", sb.toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("wordcloud", sb.toString().trim());
                editor.apply();
                Log.d("저장된 키워드", pref.getString("wordcloud", ""));
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                        String defpass = "\"word\":\"freq\",\"알림서랍\":8,\"시각디자인\":8,\"CDO\":12,\"슬기로움\":6,\"안드로이드\":9,\"강민구\":10,\"디자이너\":6";
                        pass = pref.getString("wordcloud", defpass);
                        System.out.println("981217" + pass);
                        System.out.println("981217" + defpass);
                        webview.loadUrl("javascript:makeWordCloud('{" + pass + "}')");
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserKeyword>> call, Throwable t) {

            }
        });









        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent toWebViewActivity = new Intent(getContext(), WebViewActivity.class);
                startActivity(toWebViewActivity);
                return false;
            }
        });






////        webview.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                return (event.getAction() == MotionEvent.ACTION_MOVE);
////            }
////        });
////
////        //string = "{'청머리오리':5,'검은목논병아리':8}";
        webview.setVerticalScrollBarEnabled(false);
       webview.setHorizontalScrollBarEnabled(false);
        //webview.addJavascriptInterface(new AndroidBridge(), "MyTestApp");
       // webview.loadUrl("javascript:function draw(words)");


//
//
//     //   LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(mBroadcastReceiver_remove,
////                new IntentFilter("remove"));
//
////        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(mBroadcastReceiver_intent,
////                new IntentFilter("intent_redirect"));
////        //recyclerView = view.findViewById(R.id.recycler1);
//
//
        return view;
//
    }
    private void init_widgets() {
//
//
    }

    @Override
    public void onResume() {
        super.onResume();

        MyService service = retrofit.create(MyService.class);
        SharedPreferences pref = getContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
        Call<List<UserKeyword>> call_user_keyword = service.getUsersKeyword(pref.getString("token", ""));
        call_user_keyword.enqueue(new Callback<List<UserKeyword>>() {
            @Override
            public void onResponse(Call<List<UserKeyword>> call, Response<List<UserKeyword>> response) {
                List<UserKeyword> keywords = response.body();
                HashMap<String, Integer> map = new HashMap<>();
                HashMap<String, Integer> map2 = new HashMap<>();
                if(keywords.size() == 0) {
                    return;
                }
                //positive z-score 구하기
                for (int i = 0 ; i < keywords.size() ; i++){
                    map.put(keywords.get(i).getKeyword(), keywords.get(i).getPositive_value_count());
                    System.out.println(keywords.get(i).getPositive_value_count());
                }

                for (int i = 0 ; i < keywords.size() ; i++){
                    map2.put(keywords.get(i).getKeyword(), keywords.get(i).getNegative_value_count());
                    System.out.println(keywords.get(i).getNegative_value_count());
                }



                Z_value one = new Z_value();
                HashMap<String, Double> p_value = one.getZ_score(map);
                HashMap<String, Double> n_value = one.getZ_score(map2);


                for (int i = 0 ; i < keywords.size() ; i++){
                    keywords.get(i).setZ_value(p_value.get(keywords.get(i).getKeyword()) - n_value.get(keywords.get(i).getKeyword()));
                    System.out.println("z_value for" + keywords.get(i).getKeyword() + ": " + keywords.get(i).getZ_value());
                }

                final Comparator<UserKeyword> comp = (k1, k2) -> Double.compare(k1.getZ_value(), k2.getZ_value());
                List<UserKeyword> sortedList = keywords.stream()
                        .sorted(comp)
                        .collect(Collectors.toList());
                double a= sortedList.get(sortedList.size() - 1).getZ_value();
                double b= -sortedList.get(sortedList.size() - 1).getZ_value() + sortedList.get(0).getZ_value()  ;
                for (int i = 0; i < sortedList.size(); i++){
                    double c = sortedList.get(i).getZ_value();
                    int d= (int)Math.round((c - a)*(5/b) +1);
                    sortedList.get(i).setZ_value(d);
                }
                String sb = "\"word\":\"freq\"";


                if(keywords == null){
                } else if(keywords.size() < 21) {
                    for (int i = 0; i < keywords.size(); i++) {
                        //sb.append(",\""+ sortedList.get(i).getKeyword() + "\":" + 8);
                        sb = sb + ",\""+ sortedList.get(i).getKeyword() + "\":" + ((int)sortedList.get(i).getZ_value()*2);
                        System.out.println("sorted list is: " + sortedList.get(i).getZ_value());
                    }
                } else  {
                    for (int i = 0; i < 21; i++) {
                        sb = sb + ",\""+ sortedList.get(i).getKeyword() + "\":" + ((int)sortedList.get(i).getZ_value()*2);
                    }
                }
                Log.d("워드클라우드 키워드셋", sb.toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("wordcloud",  sb.toString().trim());
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<UserKeyword>> call, Throwable t) {

            }
        });

        handler1.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                String defpass = "\"word\":\"freq\",\"알림서랍\":8,\"시각디자인\":8,\"CDO\":12,\"슬기로움\":6,\"안드로이드\":9,\"강민구\":10,\"디자이너\":6";
                System.out.println("981217" + pass);
                webview.loadUrl("javascript:makeWordCloud('{" + pass + "}')");

            }
        });

    }

    //
    private void manage_widgets_on_swipe(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }
        currentPosition = pos;
    }



    int i = 0;
    public void onLottieClick(View view) {

        if( i == 0 ) {
            animationView.setSpeed((float) 1.5);
            animationView.playAnimation();
            i = 1;
        }
        else if (i == 1){
            animationView.setSpeed((float) -1.5);
            animationView.playAnimation();
            i = 0;
        }
    }





}
