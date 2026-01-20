package com.example.badhabitstrackerdam.di

import android.app.Application
import androidx.room.Room
import com.example.badhabitstrackerdam.model.local.AppDatabase
import com.example.badhabitstrackerdam.model.local.CheckInDao
import com.example.badhabitstrackerdam.model.local.HabitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "bad_habits_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(db: AppDatabase): HabitDao {
        return db.habitDao()
    }

    @Provides
    @Singleton
    fun provideCheckInDao(db: AppDatabase): CheckInDao {
        return db.checkInDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder()
            .baseUrl("https://zenquotes.io/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideZenQuotesApi(retrofit: retrofit2.Retrofit): com.example.badhabitstrackerdam.model.remote.ZenQuotesApi {
        return retrofit.create(com.example.badhabitstrackerdam.model.remote.ZenQuotesApi::class.java)
    }
}