package com.brinfotech.feedbacksystem.ui.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.brinfotech.feedbacksystem.MyApplication;
import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.jobQueue.StartUpApplicationJob;
import com.brinfotech.feedbacksystem.ui.loginScreen.LoginActivity;


public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().getMainJobManager().addJobInBackground(new StartUpApplicationJob());

        redirectDashboardActivity();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash_screen;
    }

    private void redirectDashboardActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (isUserLoggedIn()) {
                    redirectBasedOnUserType(getActivity());
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }

    @Override
    public void onClick(View view) {

    }
}
