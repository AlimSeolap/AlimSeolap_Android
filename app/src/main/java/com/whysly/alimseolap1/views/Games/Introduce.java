package com.whysly.alimseolap1.views.Games;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.views.Activity.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Introduce extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ScrollView scrollView = findViewById(R.id.introduce_scroll_view);
        LinearLayout linearLayout = findViewById(R.id.introduce_linear_layout);
        LinearLayout linearLayout2 = findViewById(R.id.introduce_linear_layout2);
        Button introduce_accept = findViewById(R.id.introduce_accept_btn);
        TextView a1 = findViewById(R.id.a1);
        CircleImageView b1 = findViewById(R.id.b1);
        View b2 = findViewById(R.id.b2);
        CircleImageView c1 = findViewById(R.id.c1);
        TextView c2 = findViewById(R.id.c2);
        TextView c3 = findViewById(R.id.c3);
        ImageView introduce = findViewById(R.id.introduce_text_image);


        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getSize(size); // or getSize(size)
        int width = size.x;
        int height_pixel = size.y;
        float density = getBaseContext().getResources().getDisplayMetrics().density;
        int dpWidth  = (int)((float)width/density);
        int dpHeight = (int)((float)height_pixel/density);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height =  height_pixel;
        linearLayout.setLayoutParams(params);
        linearLayout2.requestLayout();
        ViewGroup.LayoutParams params2 = linearLayout2.getLayoutParams();
        params2.height =  height_pixel;
        linearLayout2.setLayoutParams(params2);
        linearLayout2.requestLayout();




        //System.out.println(linearLayout.getHeight());
        System.out.println("기기사이즈" + width +"/" + height_pixel+"/" + density+"/" + dpHeight+"/" + linearLayout.getHeight());
        //TextView describe = findViewById(R.id.describe);
        TextView tv = findViewById(R.id.text60sec);
//        String string = "시간은 ‘50초’ 드릴게요.";
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.popup_without_transition);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.popup);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.popup);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.popup);
        Animation animation5 = AnimationUtils.loadAnimation(this, R.anim.popup);
        a1.startAnimation(animation1);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()

            {
                b1.startAnimation(animation2);
                b1.setVisibility(View.VISIBLE);
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1500);// 0.5초 정도 딜레이를 준 후 시작

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()

            {
                b2.startAnimation(animation3);
                b2.setVisibility(View.VISIBLE);
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1500);// 0.5초 정도 딜레이를 준 후 시작



        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()

            {
                c1.startAnimation(animation4);
                c1.setVisibility(View.VISIBLE);
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 4500);// 0.5초 정도 딜레이를 준 후 시작

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                c2.startAnimation(animation5);
                c3.startAnimation(animation5);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                //여기에 딜레이 후 시작할 작업들을 입력

            }
        }, 4500);// 0.5초 정도 딜레이를 준 후 시작


        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        float a = 46*density;
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY",  scrollView.getBottom() + (int) a + 6);
                        objectAnimator.setDuration(1000);
                        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        objectAnimator.start();
                        //scrollView.getBottom()
                    }
                });
                //replace this line to scroll up or down
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //scrollView.smoothScrollTo(0, scrollView.getBottom());

            }
        }, 6500);


//        int color = Color.parseColor("#F5B517");
//
//        int size = 80;

//        SpannableStringBuilder builder = new SpannableStringBuilder(string);
//
//        builder.setSpan(new ForegroundColorSpan(color), 4, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new AbsoluteSizeSpan(size), 4, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv.append(builder);


        introduce_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainGame.class);
                startActivity(intent);
                finish();
            }
        });



    }



}
