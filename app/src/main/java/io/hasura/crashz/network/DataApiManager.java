package io.hasura.crashz.network;

import android.content.Context;
import android.provider.ContactsContract;

import java.util.concurrent.TimeUnit;

import io.hasura.crashz.interceptor.AddCookiesInterceptor;
import io.hasura.crashz.interceptor.ReceiveCookiesInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class DataApiManager {

    private static ApiInterface apiInterface;
    private Context context;

    public DataApiManager(Context context) {
        this.context = context;
    }

    private void createApiInterface(){

        HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new AddCookiesInterceptor(context)) // VERY VERY IMPORTANT
                .addInterceptor(new ReceiveCookiesInterceptor(context))
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkURL.DATA_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApiInterface(){

        if(apiInterface==null){
            createApiInterface();
        }
        return apiInterface;

    }
}
