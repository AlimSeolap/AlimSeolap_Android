package com.whysly.alimseolap1.views.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.whysly.alimseolap1.R;

public class NotiContentActivity extends BaseActivity  {
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.noti_content);
        webview = (WebView) findViewById(R.id.noti_content_webview);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);

        String url = getIntent().getStringExtra("html_url");
        String new_url = url.replace("\"", "");
        Log.d("html_url", new_url);
        webview.loadUrl(new_url);
        Button good_button = findViewById(R.id.btn_good);
        Button bad_button = findViewById(R.id.btn_bad);
        ImageButton back_button = (ImageButton) findViewById(R.id.back_arrow);
        good_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO:
                // 긍정 평가 반응 -> 서버에 반영
            }
        });

        bad_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO:
                // 부정 평가 반응 -> 서버에 반영
            }
        });

        back_button.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
