package com.example.smartparkinglot.di

import android.app.Application
import android.content.Context
import com.example.smartparkinglot.MyApplication
import com.example.smartparkinglot.repository.CarParkRepository
import com.example.smartparkinglot.retrofit.RESTClient
import com.example.smartparkinglot.room.UserRoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MyApplication): Context {
        return application
    }

//    @Provides
//    @Singleton
//    fun bindApplication(app: Application): Application {
//        return app
//    }


    @Provides
    @Singleton
    fun provideDatabase(context: Context) : UserRoomDatabase {
        return UserRoomDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideRepository(userRoomDatabase: UserRoomDatabase) : CarParkRepository {
        return CarParkRepository(userRoomDatabase, RESTClient.getApi())
    }

}