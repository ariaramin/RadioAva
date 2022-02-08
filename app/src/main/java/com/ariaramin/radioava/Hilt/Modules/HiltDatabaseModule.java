package com.ariaramin.radioava.Hilt.Modules;

import android.content.Context;

import com.ariaramin.radioava.Room.AppDatabase;
import com.ariaramin.radioava.Room.DatabaseDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HiltDatabaseModule {

    @Provides
    @Singleton
    AppDatabase ProvideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    DatabaseDao ProvideDatabaseDao(AppDatabase appDatabase) {
        return appDatabase.databaseDao();
    }
}
