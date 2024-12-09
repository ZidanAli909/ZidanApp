package com.zidan.zidanapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zidan.zidanapp.Data.Models.Diary
import com.zidan.zidanapp.Data.Room.DiaryDB
import com.zidan.zidanapp.Data.Room.DiaryDao
import com.zidan.zidanapp.Data.Room.DiaryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    app: Application,
    private val dao: DiaryDao
) : AndroidViewModel(app) {
    // WARNING! AndroidViewModel hanya muat satu parameter saja (app).
    // Paging 3 susah untuk Dao-exclusive :(

    private val repo: DiaryRepo
    val stateGetDiary = MutableStateFlow<Diary?>(null)
    val diaryPagedList: LiveData<PagedList<Diary>>

    init {
        val dao = DiaryDB.getDiary(app).diaryDao
        repo = DiaryRepo(dao)
        diaryPagedList = LivePagedListBuilder(
                dao.getDiaryListPaged(),
        PagedList.Config.Builder()
            .setPageSize(4)
            .setEnablePlaceholders(true)
            .build()
        ).build()
    }

    fun getDiaryList(): Flow<MutableList<Diary>> = repo.getDiaryList()

    fun createDiary(diary: Diary) {
        viewModelScope.launch {
            repo.createDiary(diary)
            invalidatePagedList()
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch {
            repo.deleteDiary(diary)
            invalidatePagedList()
        }
    }

    fun getDiaryById(id: Int) {
        viewModelScope.launch {
            stateGetDiary.value = repo.getDiaryById(id)
        }
    }

    private fun invalidatePagedList() {
        diaryPagedList.value?.dataSource?.invalidate()
    }
}