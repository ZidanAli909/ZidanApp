package com.zidan.zidanapp.Data.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zidan.zidanapp.Data.Model.Diary

@Database(
    entities = [Diary::class],
    version = 1
)
abstract class DiaryDB: RoomDatabase() {
    abstract val diaryDao: DiaryDao

    companion object {
        private const val databaseName = "diary_database"

        @Volatile
        private var databaseInstance: DiaryDB? = null

        fun getDiary(context: Context): DiaryDB {
            synchronized(this) {
                return databaseInstance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDB::class.java,
                    databaseName
                ).build().also {
                    databaseInstance = it
                }
            }
        }
    }
}