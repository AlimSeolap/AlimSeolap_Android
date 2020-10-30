package com.whysly.alimseolap1.views.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.interfaces.MainInterface;

public class PrivacyPolicy extends BaseActivity implements MainInterface.View {
    ImageView view1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
    }

    public void onEditClick(View view) {
        switch (view.getId()) {
            case R.id.btn_UploadPicture:

                break;
            case R.id.edit_profile_submit_btn:

                //Toast.makeText(this, "모든 정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_arrow:
                onBackPressed();
                Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}

