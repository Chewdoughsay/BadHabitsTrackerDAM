package com.example.badhabitstrackerdam.di

import android.app.Application
import androidx.room.Room
import com.example.badhabitstrackerdam.model.local.AppDatabase
import com.example.badhabitstrackerdam.model.local.HabitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Acest modul trăiește cât trăiește toată aplicația
object AppModule {

    // 1. Învățăm Hilt cum să creeze Baza de Date
    @Provides
    @Singleton // Creăm o singură instanță pentru toată aplicația (eficient)
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "bad_habits_db"
        )
            .fallbackToDestructiveMigration() // Dacă schimbăm structura DB, șterge vechea bază (util la dev)
            .build()
    }

    // 2. Învățăm Hilt cum să ofere DAO-ul
    // Hilt știe deja să facă 'db' din funcția de mai sus!
    @Provides
    @Singleton
    fun provideHabitDao(db: AppDatabase): HabitDao {
        return db.habitDao()
    }

    // NOTĂ: Nu trebuie să facem @Provides pentru Repository,
    // deoarece am folosit @Inject constructor în clasa HabitRepository.
    // Hilt îl detectează automat!
}