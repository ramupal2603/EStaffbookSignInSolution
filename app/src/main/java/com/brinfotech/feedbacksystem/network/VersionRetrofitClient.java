package com.brinfotech.feedbacksystem.network;

import com.brinfotech.feedbacksystem.network.utils.NetworkConfig;
import com.brinfotech.feedbacksystem.network.utils.UnauthorizedInterceptor;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VersionRetrofitClient {

    public static Retrofit versionRetrofit;

    public void RetrofitClient() {

    }

    public static Retrofit getVersionRetrofit() {

        if (versionRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(NetworkConfig.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
            builder.writeTimeout(NetworkConfig.WRITE_TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(NetworkConfig.READ_TIME_OUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(NetworkConfig.RETRY);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(new UnauthorizedInterceptor());
            builder.addNetworkInterceptor(logging);

            OkHttpClient okHttpClient = builder.build();

            versionRetrofit = new Retrofit.Builder()
                    .baseUrl(WebApiHelper.VERSION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }

        return versionRetrofit;
    }
}
