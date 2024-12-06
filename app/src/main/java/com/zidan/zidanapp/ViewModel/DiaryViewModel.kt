package com.zidan.zidanapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zidan.zidanapp.Data.Model.Diary
import com.zidan.zidanapp.Data.Room.DiaryDB
import com.zidan.zidanapp.Data.Room.DiaryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DiaryViewModel(app: Application): AndroidViewModel(app) {
    private val repo: DiaryRepo
    val stateGetDiary = MutableStateFlow<Diary?>(null)

    val diaryPagingData: Flow<PagingData<Diary>> // Paging Data Flow

    init {
        val dao = DiaryDB.getDiary(app).diaryDao
        repo = DiaryRepo(dao)

        diaryPagingData = repo.getDiaryListPaged().cachedIn(viewModelScope) // Initialize paging flow
    }

    fun getDiaryList(): Flow<MutableList<Diary>> = repo.getDiaryList()

    fun createDiary(diary: Diary) {
        viewModelScope.launch {
            repo.createDiary(diary)
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch {
            repo.deleteDiary(diary)
        }
    }

    fun getDiaryById(id: Int) {
        viewModelScope.launch {
            stateGetDiary.value = repo.getDiaryById(id)
        }
    }
}