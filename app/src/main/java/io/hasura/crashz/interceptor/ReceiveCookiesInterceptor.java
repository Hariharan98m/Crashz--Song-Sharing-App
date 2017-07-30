package io.hasura.crashz.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by HARIHARAN on 29-06-2017.
 */

public class ReceiveCookiesInterceptor implements Interceptor {
    private Context context;
    public ReceiveCookiesInterceptor(Context context) {
        this.context = context;
    } // AddCookiesInterceptor()
    @Override
    public Response intercept(Chain chain) throws IOException {
        //Get the response Object from the interceptor chain
        Response originalResponse = chain.proceed(chain.request());
        //If the response header Set-cookie is present, then

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).
                    getStringSet("REF_COOKIES", new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("REF_COOKIES", cookies).apply();
            memes.commit();
        }

        return originalResponse;
    }
}
