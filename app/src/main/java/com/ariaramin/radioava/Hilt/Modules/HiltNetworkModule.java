package com.ariaramin.radioava.Hilt.Modules;

import com.ariaramin.radioava.MainRepository;
import com.ariaramin.radioava.Retrofit.RequestApi;
import com.ariaramin.radioava.Room.DatabaseDao;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class HiltNetworkModule {

    @Provides
    @Singleton
    Retrofit ProvideRetrofit() {
        String BASE_URL = "https://radiojavan.pythonanywhere.com/";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RequestApi ProvideRequestApi(Retrofit retrofit) {
        return retrofit.create(RequestApi.class);
    }

    @Provides
    @Singleton
    MainRepository ProvideMainRepository(RequestApi requestApi, DatabaseDao databaseDao) {
        return new MainRepository(requestApi, databaseDao);
    }
}
