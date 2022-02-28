package com.ariaramin.radioava.Hilt.Modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ActivityComponent.class)
public class HiltAppModule {

    @Provides
    ConnectivityManager ProvideConnectivityManager(@ApplicationContext Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    NetworkRequest ProvideNetworkRequest() {
        return new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
    }

    @Provides
    SharedPreferenceManager ProvideSharedPreferencesManager(@ApplicationContext Context context) {
        return new SharedPreferenceManager(context);
    }
}
