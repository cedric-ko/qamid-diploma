package ru.edu.qamid.ui.data;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

// Этот класс - перехватчик обращений к сети для работы Network Idling
public class IdlingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        NetworkIdlingResource.increment();
        try {
            return chain.proceed(chain.request());
        } finally {
            NetworkIdlingResource.decrement();
        }
    }
}