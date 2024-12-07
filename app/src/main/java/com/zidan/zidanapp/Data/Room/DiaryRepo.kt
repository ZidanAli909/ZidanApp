package com.zidan.zidanapp.Data.Room

import androidx.paging.DataSource
import com.zidan.zidanapp.Data.Models.Diary
import kotlinx.coroutines.flow.Flow

class DiaryRepo(private val diaryDao: DiaryDao) {
    fun getDiaryList(): Flow<MutableList<Diary>> = diaryDao.getDiaryList()

    fun getDiaryListPaged(): DataSource.Factory<Int, Diary> = diaryDao.getDiaryListPaged()

    suspend fun getDiaryById(id: Int): Diary? = diaryDao.getDiaryById(id)

    suspend fun createDiary(diary: Diary) = diaryDao.createDiary(diary)

    suspend fun deleteDiary(diary: Diary) = diaryDao.deleteDiary(diary)
}