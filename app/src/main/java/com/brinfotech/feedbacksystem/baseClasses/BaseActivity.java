package com.brinfotech.feedbacksystem.baseClasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.customClasses.ProgressLoader;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.ui.Utils;
import com.brinfotech.feedbacksystem.ui.loginScreen.LoginActivity;
import com.brinfotech.feedbacksystem.ui.staffView.dashboard.StaffDashboardActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Unbinder unbinder = null;


    private ProgressLoader loader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        unbinder = ButterKnife.bind(this);
        Utils.hideKeyBoard(getActivity());
    }


    protected abstract int getLayoutResource();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void showToastMessage(String errorMessage) {

        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(15);
        toast.show();
    }

    public void printLogMessage(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public void showProgressBar() {

        //Check if Activity is null then close activity.
        if (getActivity() == null) {
            return;
        } else {
            //If loader instance is null then re-create object.
            if (loader == null) {
                loader = new ProgressLoader(getActivity());
            }

            //If progress bar is not showing then show progress bar.
            if (!loader.isShowing()) {
                loader.show();
            }
        }

    }

    public void hideProgressBar() {

        if (loader != null && loader.isShowing()) {
            loader.dismiss();
        }
    }

    public void showAlertDialog(Context context, String message) {

        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(message);
            builder1.setTitle(context.getResources().getString(R.string.app_name));
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {

        }

    }

    public Context getContext() {
        return BaseActivity.this;
    }

    public BaseActivity getActivity() {
        return BaseActivity.this;
    }

    public void openStaffDashboard() {
        Intent intent = new Intent(getActivity(), StaffDashboardActivity.class);
        startActivity(intent);
    }

    public void startActivityAfterSeconds() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                openStaffDashboard();
            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }

}

