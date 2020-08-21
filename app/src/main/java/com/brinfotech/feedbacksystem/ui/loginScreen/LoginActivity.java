package com.brinfotech.feedbacksystem.ui.loginScreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.ActBase;


import butterknife.BindView;

public class LoginActivity extends ActBase implements View.OnClickListener {

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtPwd)
    EditText edtPwd;

    @BindView(R.id.btnLogin)
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnLogin.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {

        if (view == btnLogin) {
            doValidation();
        }
    }

    private void doValidation() {
        String userName = edtUserName.getText().toString().trim();
        String userPassword = edtPwd.getText().toString().trim();
        if (userName.isEmpty()) {
            showToastMessage("Enter UserName");
        } else if (userPassword.isEmpty()) {
            showToastMessage("Enter Password");
        } else {
            callLoginMethod(userName, userPassword);
        }
    }

    private void callLoginMethod(String userName, String userPassword) {


    }
}
