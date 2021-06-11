package com.brinfotech.feedbacksystem.ui.staffView.thankYouPage;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;

public class ThankYouScreen extends BaseActivity {

    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.txtThanksMessage)
    TextView txtThanksMessage;

    String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();

        redirectToDashboard();
    }

    private void redirectToDashboard() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (isUserLoggedIn()) {
                    redirectBasedOnUserType(getActivity());
                } else {
                    finish();
                }

            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }

    private void getIntentData() {
        status = getIntent().getStringExtra(ConstantClass.EXTRAA_FORM_DATA);

        txtUserName.setText(Prefs.getString(PreferenceKeys.USER_NAME, ""));

        if (status.equals(WebApiHelper.STATUS_SIGNED_IN)) {
            txtThanksMessage.setText(R.string.signed_in_thanks_msg);
            playSignedInVoice();
        } else if (status.equals(WebApiHelper.STATUS_SIGNED_OUT)) {
            txtThanksMessage.setText(R.string.signed_out_thanks_msg);
            playSignedOutVoice();
        }
    }

    private void playSignedOutVoice() {
        MediaPlayer mPlayer = MediaPlayer.create(ThankYouScreen.this, R.raw.fivesquidjamilsigningout);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mPlayer.start();
    }

    private void playSignedInVoice() {
        MediaPlayer mPlayer = MediaPlayer.create(ThankYouScreen.this, R.raw.fivesquidjamilsigningin);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mPlayer.start();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_thank_you;
    }

    @Override
    public void onClick(View view) {

    }
}
