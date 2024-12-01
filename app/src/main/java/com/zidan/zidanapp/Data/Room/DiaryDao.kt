package com.zidan.zidanapp.Data.Room

import androidx.room.*
import com.zidan.zidanapp.Data.Model.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary")
    fun getDiaryList(): Flow<MutableList<Diary>>

    @Query("SELECT * FROM diary WHERE id=:id")
    suspend fun getDiaryById(id: Int): Diary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDiary(diary: Diary)

    @Delete
    suspend fun deleteDiary(diary: Diary)
}