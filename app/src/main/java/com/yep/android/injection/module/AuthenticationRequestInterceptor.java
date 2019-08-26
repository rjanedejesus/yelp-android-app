package com.yep.android.injection.module;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static timber.log.Timber.d;

public class AuthenticationRequestInterceptor implements Interceptor {

    private String token;

    public AuthenticationRequestInterceptor()
    {
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();

        d("Token on Network : " + token);

        Request.Builder requestBuilder = request.newBuilder();
        if(token != null)
            requestBuilder.addHeader("token", token);

        return chain.proceed(requestBuilder.build());
    }

}
