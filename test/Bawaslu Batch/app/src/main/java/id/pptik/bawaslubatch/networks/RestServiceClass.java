package id.pptik.bawaslubatch.networks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by github/fiyyanputra on 10/8/2018.
 */

public class RestServiceClass {
    public static final String BASE_URL = "http://bawaslu-ftp.pptik.id:5000/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        /*java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("cache.itb.ac.id", 8080));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();*/

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}