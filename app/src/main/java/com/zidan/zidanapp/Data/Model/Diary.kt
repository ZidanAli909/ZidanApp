package com.zidan.zidanapp.Data.Model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "diaries")
data class Diary (
    @PrimaryKey(autoGenerate = true) val id : Int,
    val title: String,
    val description: String,
    val media: ByteArray? = null
): Parcelable
