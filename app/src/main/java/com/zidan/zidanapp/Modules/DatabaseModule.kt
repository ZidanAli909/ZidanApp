package com.zidan.zidanapp.Modules

import android.content.Context
import androidx.room.Room
import com.zidan.zidanapp.Data.Room.DiaryDB
import com.zidan.zidanapp.Data.Room.DiaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DiaryDB {
        return Room.databaseBuilder(
            context,
            DiaryDB::class.java,
            "diary_database" // Replace with your actual database name
        ).build()
    }

    @Provides
    fun provideDiaryDao(database: DiaryDB): DiaryDao {
        return database.diaryDao
    }
}