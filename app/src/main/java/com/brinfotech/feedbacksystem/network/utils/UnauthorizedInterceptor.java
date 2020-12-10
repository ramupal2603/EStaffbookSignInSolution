package com.brinfotech.feedbacksystem.network.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.data.UnauthorizedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class UnauthorizedInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            EventBus.getDefault().post(UnauthorizedEvent.instance());
        }
        return response;
    }
}