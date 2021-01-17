package com.whysly.alimseolap1.views.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.Util.LoginMethod;
import com.whysly.alimseolap1.interfaces.MainInterface;

public class Suggest extends BaseActivity implements MainInterface.View {
    EditText editText;
    EditText editText2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion);
        editText = findViewById(R.id.title_edt);
        editText2 = findViewById(R.id.content_edt);
    }


    public void onSuggestClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow:
                onBackPressed();
                break;
            case R.id.send_btn:
                send(editText.getText().toString(), editText2.getText().toString());
                break;
        }
    }

    public void send(String title, String content) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setPackage("com.google.android.gm");
        email.setType("plain/Text");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"company@whysly.org"});
        email.putExtra(Intent.EXTRA_SUBJECT, "<" + getString(R.string.app_name) + " " + "제안하기" + ">" + title );
        email.putExtra(Intent.EXTRA_TEXT, "유저아이디 (UserID):" + LoginMethod.getUserName() + "\n기기명 (Device):\n안드로이드 OS (Android OS):\n내용 (Content):\n" + content);
        email.setType("message/rfc822");
        startActivity(email);
    }


}

