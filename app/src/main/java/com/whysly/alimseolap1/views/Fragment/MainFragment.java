package com.whysly.alimseolap1.views.Fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.JsonObject;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.Util.Z_value;
import com.whysly.alimseolap1.interfaces.MyService;
import com.whysly.alimseolap1.models.UserKeyword;
import com.whysly.alimseolap1.views.Activity.MainActivity;
import com.whysly.alimseolap1.views.Activity.MainViewModel;
import com.whysly.alimseolap1.views.Activity.NotiContentActivity;
import com.whysly.alimseolap1.views.Adapters.RecyclerViewAdapter;
import com.whysly.alimseolap1.views.Adapters.RecyclerViewEmptySupport;
import com.whysly.alimseolap1.views.Adapters.SmooliderAdapter;
import com.whysly.alimseolap1.views.SmoothNonSwipeableViewPager;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    private SmoothNonSwipeableViewPager viewPager;
    LottieAnimationView animationView;
    LottieAnimationView pop_anim;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerViewEmptySupport recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainViewModel model;
    private int currentPosition;
    int user_id;
    WebView webview;
    WebSettings settings;
    final public Handler handler1 = new Handler();
    SharedPreferences pref;
    Retrofit retrofit;
    String pass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_fragment2, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.wordcloud_toolbar);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.empty_noti);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshWebView();
                viewPager.setAdapter( new SmooliderAdapter(activity.getSupportFragmentManager(), 2));
                pullToRefresh.setRefreshing(false);
            }
        });
        pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        View viewToolbar = getActivity().getLayoutInflater().inflate(R.layout.custom_toolbar_all, null);
        activity.getSupportActionBar().setCustomView(viewToolbar, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext());
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, long server_id) {
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://118.67.129.104/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MyService service = retrofit.create(MyService.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("checked", "true");
                Call<JsonObject> call_patchChecked = service.patchChecked(pref.getString("token", ""), String.valueOf(server_id), jsonObject);
                Log.d("알림확인패치", String.valueOf(server_id));
                call_patchChecked.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("token" , pref.getString("token", ""));
                        Log.d("알림확인패치성공", response.toString());
                        JsonObject jsonObject1 = response.body();
                        String html_url = jsonObject1.get("html_url").toString();
                        Intent intent = new Intent(getContext(), NotiContentActivity.class);
                        intent.putExtra("html_url", html_url);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "존재하지 않거나 삭제된 게시물 입니다.", Toast.LENGTH_SHORT);
                        Log.d("알림확인패치오류", t.toString());
                    }
                });
            }
        }) ;
        //recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_noti));
//        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
//        if (animator instanceof SimpleItemAnimator) {
//            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
//        }

        model = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(MainViewModel.class);
        model.getDefaultNotifications().observe(getViewLifecycleOwner(), entities -> {
            recyclerViewAdapter.setEntities(entities);
            recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());
        });

        if (recyclerViewAdapter.getItemCount() == 0) {
            lottieAnimationView.setVisibility(View.VISIBLE);
        }
        else lottieAnimationView.setVisibility(View.INVISIBLE);

        Log.d("MainFragment", "뷰생성됩.");

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

//        webview = (WebView) view.findViewById(R.id.webViewmain);
//        settings = webview.getSettings();
//        //settings.setLoadsImagesAutomatically(true);
//        settings.setJavaScriptEnabled(true);
//
        viewPager = (SmoothNonSwipeableViewPager) view.findViewById(R.id.smoolider);
        manage_widgets_on_swipe(0);
        viewPager.setAdapter( new SmooliderAdapter(activity.getSupportFragmentManager(), 2));
//
//        LottieAnimationView lottieAnimationView = view.findViewById(R.id.empty_noti);
//        webview.loadUrl("file:///android_asset/index.html");
        retrofit = new Retrofit.Builder()
                .baseUrl("http://118.67.129.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


//        MyService service = retrofit.create(MyService.class);
//        SharedPreferences pref = getContext().getSharedPreferences("data", Activity.MODE_PRIVATE);
//        Call<List<UserKeyword>> call_user_keyword = service.getUsersKeyword(pref.getString("token", ""));
//        call_user_keyword.enqueue(new Callback<List<UserKeyword>>() {
//
//            @Override
//            public void onResponse(Call<List<UserKeyword>> call, Response<List<UserKeyword>> response) {
//                List<UserKeyword> keywords = response.body();
//                HashMap<String, Integer> map = new HashMap<>();
//                HashMap<String, Integer> map2 = new HashMap<>();
//                //positive z-score 구하기
//                for (int i = 0 ; i < keywords.size() ; i++){
//                    map.put(keywords.get(i).getKeyword(), keywords.get(i).getPositive_value_count());
//                    System.out.println(keywords.get(i).getPositive_value_count());
//                }
//
//                for (int i = 0 ; i < keywords.size() ; i++){
//                    map2.put(keywords.get(i).getKeyword(), keywords.get(i).getNegative_value_count());
//                    System.out.println(keywords.get(i).getNegative_value_count());
//                }
//
//                Z_value one = new Z_value();
//                HashMap<String, Double> p_value = one.getZ_score(map);
//                HashMap<String, Double> n_value = one.getZ_score(map2);
//
//                for (int i = 0 ; i < keywords.size() ; i++){
//                    keywords.get(i).setZ_value(p_value.get(keywords.get(i).getKeyword()) - n_value.get(keywords.get(i).getKeyword()));
//                    System.out.println("z_value for" + keywords.get(i).getKeyword() + ": " + keywords.get(i).getZ_value());
//                }
//                Toast myToast = Toast.makeText(getContext(), p_value.toString(), Toast.LENGTH_SHORT);
//
//                myToast.show();
//                final Comparator<UserKeyword> comp = (k1, k2) -> Double.compare( k1.getZ_value(), k2.getZ_value());
//                List<UserKeyword> sortedList = keywords.stream()
//                        .sorted(comp)
//                        .collect(Collectors.toList());
//                String sb = "\"word\":\"freq\"";
//
//
//                if(keywords == null){
//                } else if(keywords.size() < 20) {
//                    for (int i = 0; i < keywords.size(); i++) {
//                        //sb.append(",\""+ sortedList.get(i).getKeyword() + "\":" + 8);
//                        sb = sb + ",\""+ sortedList.get(i).getKeyword() + "\":" + (i+1);
//                        System.out.println("sorted list is: " + sortedList.get(i).getZ_value());
//                    }
//                } else  {
//                    for (int i = 0; i < 20; i++) {
//                        sb = sb + ",\""+ sortedList.get(i).getKeyword() + "\":" + sortedList.get(i).getFinal_value_count();
//                    }
//                }
//                Log.d("워드클라우드 키워드셋", sb.toString());
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("wordcloud",  sb.toString().trim());
//                editor.apply();
//                Log.d("저장된 키워드", pref.getString("wordcloud",""));
//                handler1.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        SharedPreferences pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
//                        String defpass = "\"word\":\"freq\",\"알림서랍\":8,\"시각디자인\":8,\"CDO\":12,\"슬기로움\":6,\"안드로이드\":9,\"강민구\":10,\"디자이너\":6";
//                        pass = pref.getString("wordcloud", defpass);
//                        System.out.println("981217" + pass);
//                        System.out.println("981217" + defpass);
//                        webview.loadUrl("javascript:makeWordCloud('{" + pass + "}')");
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<List<UserKeyword>> call, Throwable t) {
//
//            }
//        });
//
//
//
//
//
//
//
//       webview.setOnLongClickListener(new View.OnLongClickListener() {
//           @Override
//           public boolean onLongClick(View view) {
//               Intent toWebViewActivity = new Intent(getContext(), WebViewActivity.class);
//               startActivity(toWebViewActivity);
//               return false;
//           }
//       });
//
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(final int position) {
                // handle operations with current page
                manage_widgets_on_swipe(position);

            }
        });
//
//
//
//
////        webview.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                return (event.getAction() == MotionEvent.ACTION_MOVE);
////            }
////        });
////
////        //string = "{'청머리오리':5,'검은목논병아리':8}";
//        webview.setVerticalScrollBarEnabled(false);
//        webview.setHorizontalScrollBarEnabled(false);
//        //webview.addJavascriptInterface(new AndroidBridge(), "MyTestApp");
//       // webview.loadUrl("javascript:function draw(words)");
//
        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
        animationView.setOnClickListener(this::onLottieClick);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % 2);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//
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
//    private void init_widgets() {
//
//
//    }
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

    final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            Log.d("현우", "onSwiped실행");
            System.out.println(direction);

            int noti_position = viewHolder.getAdapterPosition();
            if(recyclerViewAdapter.getItemCount() == 0) {
                recyclerView.setVisibility(View.VISIBLE);
            } else {

            }

            String noti_id_string = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.noti_id)).getText().toString();
            System.out.println(noti_id_string);

            int noti_id = 0;
            try {
                noti_id = Integer.parseInt(noti_id_string);
            }
            catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }


            System.out.println(noti_id);
            String evaluate = "none";

            //스와이프 방향에 따라 DB에서  this_user_real_evaluation 값을 지정해줌줌

            if (direction == 8) {
                Log.d("향", "onSwiped: 오른쪽");
                viewHolder.getItemId();
                evaluate = "true";
                int user_eval = 1;
                final int id = noti_id;

                recyclerViewAdapter.removeItemView(noti_position, pref.getString("uid", ""));
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model.updateRealEvaluation(id,1, pref.getString("token",""));
                        //Do something after 100ms
                        System.out.println("981217" + id);
                    }
                }, 1000);

            } else if (direction == 4) {
                Log.d("방향", "onSwiped: 왼쪽");
                viewHolder.getItemId();
                evaluate = "false";
                int user_eval = -1;
                //model.updateRealEvaluation(noti_id , -1);
                recyclerViewAdapter.removeItemView(noti_position, pref.getString("uid", ""));
                final int id = noti_id;

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model.updateRealEvaluation(id,-1, pref.getString("token",""));
                        System.out.println("981217" + id);
                        //Do something after 100ms
                    }
                }, 1000);
            }

            // 스와이프 하여 제거하면 밑의 코드가 실행되면서 스와이프 된 뷰홀더의 위치 값을 통해 어댑터에서 아이템이 지워졌다고 노티파이 해줌.
            Log.d("준영", "noti_idx1: " + viewHolder.getAdapterPosition());
            Log.d("준영", "noti_idx2: " + noti_position);

            System.out.println(noti_position);
            String notitext = "foo";

            // 스와이프와 동시에 스와이프 방향과 스와이프된 뷰홀더의 모든 내용을 서버로 전송
            String notititle = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.notititle)).getText().toString();
            notitext = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.notitext)).getText().toString();
//            String noti_sub_text = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.extra_sub_text)).getText().toString();
            String app_name = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.app_name)).getText().toString();
//            String package_name = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.packge_name)).getText().toString();
            String category = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.noti_category)).getText().toString();

            System.out.println(app_name);
            System.out.println(notitext);

            String noti_date1 = ((TextView) recyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.noti_date)).getText().toString();
            Log.d("준영", "noti_date1 : " + noti_date1 );
            Date time = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
            String noti_date2 = format1.format(time);
            System.out.println(noti_position);
            //notititle = model.getNotificationDao().loadNotification(noti_position + 1).title;
            //String category = model.getNotificationDao().loadNotification(noti_position).category;



            System.out.println(user_id + notititle);

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://13.125.130.16/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d("현우", "Retrofit 빌드 성공");

//            NotificationDatabase db = NotificationDatabase.getNotificationDatabase(getContext());
//            user_id = db.notificationDao().loadNotification(noti_id).user_id;
//            notititle = db.notificationDao().loadNotification(noti_id).title;

            MyService service = retrofit.create(MyService.class);
            //json 객체 생성하여 값을 넣어줌
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("app_name", app_name);
            jsonObject.addProperty("package_name", "X");
            jsonObject.addProperty("title", notititle);
            jsonObject.addProperty("content", notitext);
            jsonObject.addProperty("subContent", category);
            jsonObject.addProperty("noti_date", noti_date2);
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("user_value", evaluate);

            System.out.println(app_name + " / " + notititle + " / " + notitext + " / " + noti_date1 + " / " + evaluate + " / " + user_id );




            Call<JsonObject> call = service.createPost(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    System.out.println("알림데이터 전송성공");
                    Log.d("현우", response.toString());
                    Log.d("현우", retrofit.toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println("알림데이터 전송실패");
                    Log.d("현우", t.toString());


                }
            });
        }
    };
//



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
                        .sorted(comp.reversed())
                        .collect(Collectors.toList());
                System.out.println("sorted list comp: " + sortedList.get(0).getZ_value()+ "/" + sortedList.get(1).getZ_value() +"/" + sortedList.get(2).getZ_value());
                double a= sortedList.get(sortedList.size() - 1).getZ_value();
                double b= -sortedList.get(sortedList.size() - 1).getZ_value() + sortedList.get(0).getZ_value()  ;
                for (int i = 0; i < sortedList.size(); i++){
                    double c = sortedList.get(i).getZ_value();
                    int d= (int)Math.round((c - a)*(5/b));
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


        //webview.reload();

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

    public void onLottieClick2(View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void RefreshWebView() {
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
                Toast myToast = Toast.makeText(getContext(), n_value.toString(), Toast.LENGTH_SHORT);

                myToast.show();
                final Comparator<UserKeyword> comp = (k1, k2) -> Double.compare(k1.getZ_value(), k2.getZ_value());
                List<UserKeyword> sortedList = keywords.stream()
                        .sorted(comp.reversed())
                        .collect(Collectors.toList());
                System.out.println("sorted list comp: " + sortedList.get(0).getZ_value()+ "/" + sortedList.get(1).getZ_value() +"/" + sortedList.get(2).getZ_value());
                double a= sortedList.get(sortedList.size() - 1).getZ_value();
                double b= -sortedList.get(sortedList.size() - 1).getZ_value() + sortedList.get(0).getZ_value()  ;
                for (int i = 0; i < sortedList.size(); i++){
                    double c = sortedList.get(i).getZ_value();
                    int d= (int)Math.round((c - a)*(5/b));
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
    }


}
