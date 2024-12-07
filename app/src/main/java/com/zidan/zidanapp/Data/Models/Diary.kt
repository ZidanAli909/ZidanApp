package com.zidan.zidanapp.Data.Models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "diaries")
data class Diary (
    @PrimaryKey(autoGenerate = true) val id : Int,
    val title: String,
    val description: String,
    val media: ByteArray? = null
): Parcelable
