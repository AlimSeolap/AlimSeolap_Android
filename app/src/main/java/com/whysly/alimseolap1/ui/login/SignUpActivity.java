package com.whysly.alimseolap1.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.whysly.alimseolap1.R;
import com.whysly.alimseolap1.views.Activity.EditMyProfile;
import com.whysly.alimseolap1.views.Activity.MainActivity;

public class SignUpActivity extends AppCompatActivity  {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient ;
    FirebaseUser currentUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText passwordConfirmText = findViewById(R.id.confirm_password);
        final Button next = findViewById(R.id.next);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        // Build a GoogleSignInClient with the options specified by gso.

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                next.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getConfirm_passwordError() != null) {
                    passwordConfirmText.setError(getString(loginFormState.getConfirm_passwordError()));
                }

            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                //loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.signUpDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), passwordConfirmText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordConfirmText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });


        passwordConfirmText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                //얘들은 검증안하고 로그인 시킴 ㅋㅋ
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                next(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

               // loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent return_intent = new Intent(this, LoginActivity.class);
        startActivity(return_intent);
        finish();
    }

    public void onSignUpClick(View view) {
        switch (view.getId()) {
            case R.id.return_to_sign_in:
                Intent return_intent = new Intent(this, LoginActivity.class);
                startActivity(return_intent);
                finish();
                break ;
        }
    }


    private void updateUiWithUser(LoggedInUserView model) {
        if (model == null) {

        } else {
            String welcome = getString(R.string.welcome) + model.getDisplayName();
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            Intent login_success = new Intent(this, MainActivity.class);
            startActivity(login_success);
        }

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString + "로그인에 실패하였습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
    }

    private void next(String email, String password) {
        Intent intent = new Intent (SignUpActivity.this, EditMyProfile.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("from", "signup");
        startActivity(intent);
        finish();
        // [START create_user_with_email]
        // [END create_user_with_email]
    }

}