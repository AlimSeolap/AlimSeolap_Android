package com.whysly.alimseolap1.views.Activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.interfaces.MainInterface;

public class PrivacyPolicy extends BaseActivity implements MainInterface.View {
    ImageView view1;
    WebView webview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        webview = (WebView) findViewById(R.id.policy_webView);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.loadUrl("https://kr.object.ncloudstorage.com/notification-drawer/html_messages/message14.html");

    }
    //
    //    //https://s3.ap-northeast-2.amazonaws.com/whysly.org/privacy-policy/privacy-policy.html

    public void onPolicyClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow:
                onBackPressed();
                break;
        }
    }

}

