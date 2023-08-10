package com.brinfotech.feedbacksystem.baseClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.customClasses.ProgressLoader;
import com.brinfotech.feedbacksystem.data.UnauthorizedEvent;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.DateTimeUtils;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.interfaces.OnSignOutReasonSelected;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.brinfotech.feedbacksystem.ui.Utils;
import com.brinfotech.feedbacksystem.ui.adminView.adminDashboard.AdminDashboardActivity;
import com.brinfotech.feedbacksystem.ui.changePasswordView.ChangePasswordActivity;
import com.brinfotech.feedbacksystem.ui.fireMarshalView.dashboard.FireMarshalDashboardActivity;
import com.brinfotech.feedbacksystem.ui.fireMarshalView.fireEvacuation.FireMarshalEvacuationActivity;
import com.brinfotech.feedbacksystem.ui.loginScreen.LoginActivity;
import com.brinfotech.feedbacksystem.ui.managerView.managerDashboard.ManageDashboardActivity;
import com.brinfotech.feedbacksystem.ui.managerView.managerFireEvacuation.FireEvacuationActivity;
import com.brinfotech.feedbacksystem.ui.managerView.managerStaffView.StaffReportActivity;
import com.brinfotech.feedbacksystem.ui.staffView.dashboard.StaffDashboardActivity;
import com.brinfotech.feedbacksystem.ui.staffView.thankYouPage.ThankYouScreen;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Unbinder unbinder = null;
    @Nullable
    @BindView(R.id.txtBack)
    ImageView imgBack;
    @Nullable
    @BindView(R.id.txtTime)
    TextView txtTime;
    CountDownTimer newTimer;
    private ProgressLoader loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        unbinder = ButterKnife.bind(this);
        Utils.hideKeyBoard(getActivity());

        showTime();

    }

    private void showTime() {
        newTimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (txtTime != null) {
                    txtTime.setText(String.format("Time: %s", DateTimeUtils.getCurrentTime(getContext())));
                }
            }

            public void onFinish() {

            }
        };
        newTimer.start();
    }

    @Optional
    @OnClick(R.id.txtBack)
    void onBackArrowPressed() {
        getActivity().finish();
    }


    protected abstract int getLayoutResource();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

        if (newTimer != null) {
            newTimer.cancel();
        }
    }

    public void showToastMessage(String errorMessage) {

        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = null;
        if (toastLayout != null) {
            toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(15);
        }

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

    public void openStaffDashboard(BaseActivity activity) {
        Intent intent = new Intent(getActivity(), StaffDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        activity.finish();
    }

    public void openManagerDashboard(BaseActivity activity) {
        Intent intent = new Intent(getActivity(), ManageDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        activity.finish();
    }

    public void openFireMarshalDashboard(BaseActivity activity) {
        Intent intent = new Intent(getActivity(), FireMarshalDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        activity.finish();
    }

    public void openAdminView(BaseActivity activity) {
        Intent intent = new Intent(getActivity(), AdminDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        activity.finish();
    }

    public void openStaffReportActivity() {
        Intent intent = new Intent(getActivity(), StaffReportActivity.class);
        startActivity(intent);
    }

    public void openFireEvacuationActivity() {
        Intent intent = new Intent(getActivity(), FireEvacuationActivity.class);
        startActivity(intent);
    }

    public void openFireMarshalEvacuationActivity() {
        Intent intent = new Intent(getActivity(), FireMarshalEvacuationActivity.class);
        startActivity(intent);
    }

    public void openThankYouActivity(String status) {
        Intent intent = new Intent(getActivity(), ThankYouScreen.class);
        intent.putExtra(ConstantClass.EXTRAA_FORM_DATA, status);
        startActivity(intent);
    }

    public void redirectBasedOnUserType(BaseActivity activity) {
        String userType = Prefs.getString(PreferenceKeys.USER_TYPE, "0");
        Log.e("FCM TOKEN", "" + getFCMRefreshedToken());
        if (userType.equals(WebApiHelper.USER_TYPE_STAFF)) {
            openStaffDashboard(activity);
        } else if (userType.equals(WebApiHelper.USER_TYPE_MANAGER)) {
            openManagerDashboard(activity);
        } else if (userType.equals(WebApiHelper.USER_TYPE_FIRE_MARSHAL)) {
            openFireMarshalDashboard(activity);
        } else if (userType.equals(WebApiHelper.USER_TYPE_ADMIN)) {
            openAdminView(activity);
        }
    }

    public void redirectToChangePasswordScreen(BaseActivity activity) {
        openChangedPasswordScreen(activity);
    }

    private void openChangedPasswordScreen(BaseActivity activity) {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        startActivityForResult(intent, ConstantClass.REQUEST_CODE_CHANGE_PASSWORD);

    }


    public void showErrorMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.something_went_wrong));
    }

    public void showLoginFailedMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.login_failed));
    }

    public void showNoNetworkMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.no_internet_connection));
    }

    public boolean isUserLoggedIn() {
        return Prefs.getBoolean(PreferenceKeys.USER_LOGGED_IN, false);
    }

    public Bitmap generateQRCode(String userId) {
        if (!userId.isEmpty()) {
            Bitmap qrCodeBitmap = null;
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(userId, BarcodeFormat.QR_CODE, 250, 250);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeBitmap = bitmap;

            } catch (WriterException e) {
                e.printStackTrace();
            }
            return qrCodeBitmap;
        }

        return null;

    }

    public String getFCMRefreshedToken() {
        String fcmToken = "";
        fcmToken = Prefs.getString(PreferenceKeys.FCM_TOKEN, "");

        if (fcmToken.isEmpty()) {
            fcmToken = FirebaseInstanceId.getInstance().getToken();
        }

        return fcmToken;
    }

    @Subscribe
    public final void onUnauthorizedEvent(UnauthorizedEvent e) {
        handleUnauthorizedEvent();
    }

    protected void handleUnauthorizedEvent() {
        Prefs.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    public void showSignOutOptionsDialog(OnSignOutReasonSelected signOutReasonSelected) {
        final String[] selectedReason = {"0"};

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTitle("Reason For Signing Out");
        builder.setSingleChoiceItems(new String[]{"Break Time", "Lunch Break", "Signing out for today"}, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                selectedReason[0] = String.valueOf(index);
            }
        });
        builder.addButton("Submit", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signOutReasonSelected.OnSignOutReasonListener(selectedReason[0]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}

