package com.zidan.zidanapp.Data.Room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zidan.zidanapp.Data.Model.Diary
import kotlinx.coroutines.flow.Flow

class DiaryRepo(private val diaryDao: DiaryDao) {
    fun getDiaryList(): Flow<MutableList<Diary>> = diaryDao.getDiaryList()

    suspend fun getDiaryById(id: Int): Diary? = diaryDao.getDiaryById(id)

    suspend fun createDiary(diary: Diary) = diaryDao.createDiary(diary)

    suspend fun deleteDiary(diary: Diary) = diaryDao.deleteDiary(diary)

    fun getDiaryListPaged(): Flow<PagingData<Diary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { diaryDao.getDiaryPagingSource() }
        ).flow
    }
}