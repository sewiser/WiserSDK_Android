/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Wiser Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NO
 */

package com.wiser.appsdk.sample.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wiser.appsdk.sample.BuildConfig;
import com.wiser.appsdk.sample.R;
import com.wiser.appsdk.sample.main.MainSampleListActivity;
import com.wiser.appsdk.sample.user.resetPassword.UserResetPasswordActivity;
import com.wiser.smart.android.common.utils.ValidatorUtil;
import com.wiser.smart.android.user.api.ILoginCallback;
import com.wiser.smart.android.user.bean.User;
import com.wiser.smart.home.sdk.WiserHomeSdk;

/**
 * User Login Example
 */
public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etAccount, etCountryCode, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_login);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnForget = findViewById(R.id.btnForget);
        btnLogin.setOnClickListener(this);
        btnForget.setOnClickListener(this);

        etAccount = findViewById(R.id.etAccount);
        etCountryCode = findViewById(R.id.etCountryCode);
        etPassword = findViewById(R.id.etPassword);

        etCountryCode.setText(BuildConfig.DEFAULT_COUNTRY_CODE);
        etAccount.setText(BuildConfig.DEFAULT_EMAIL);
        etPassword.setText(BuildConfig.DEFAULT_PASSWORRD);
    }

    @Override
    public void onClick(View v) {
        String strAccount = etAccount.getText().toString();
        String strCountryCode = etCountryCode.getText().toString();
        String strPassword = etPassword.getText().toString();


        if (v.getId() == R.id.btnLogin) {
            ILoginCallback callback = new ILoginCallback() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(UserLoginActivity.this,
                            "Login success",
                            Toast.LENGTH_SHORT).show();

                    startActivity(
                            new Intent(
                                    UserLoginActivity.this,
                                    MainSampleListActivity.class
                            )
                    );

                    finish();
                }

                @Override
                public void onError(String code, String error) {
                    Toast.makeText(UserLoginActivity.this,
                            "code: " + code + "error:" + error,
                            Toast.LENGTH_SHORT).show();
                }
            };
            if (ValidatorUtil.isEmail(strAccount)) {
                WiserHomeSdk.getUserInstance().loginWithEmail(strCountryCode, strAccount, strPassword, callback);
            } else {
                WiserHomeSdk.getUserInstance().loginWithPhonePassword(strCountryCode, strAccount, strPassword, callback);
            }
        } else if (v.getId() == R.id.btnForget) {
            startActivity(new Intent(this, UserResetPasswordActivity.class));
        }
    }
}
