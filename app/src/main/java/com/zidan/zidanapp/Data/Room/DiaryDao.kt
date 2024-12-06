package com.zidan.zidanapp.Data.Room

import androidx.paging.PagingSource
import androidx.room.*
import com.zidan.zidanapp.Data.Model.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diaries")
    fun getDiaryList(): Flow<MutableList<Diary>>

    @Query("SELECT * FROM diaries")
    fun getDiaryPagingSource(): PagingSource<Int, Diary>

    @Query("SELECT * FROM diaries WHERE id=:id")
    suspend fun getDiaryById(id: Int): Diary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDiary(diary: Diary)

    @Delete
    suspend fun deleteDiary(diary: Diary)
}