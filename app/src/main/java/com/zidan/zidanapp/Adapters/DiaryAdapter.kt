package com.zidan.zidanapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.zidanapp.Data.Model.Diary
import com.zidan.zidanapp.databinding.RecyclerviewDiaryitemsBinding

class DiaryAdapter : PagingDataAdapter<Diary, DiaryAdapter.DiaryViewHolder>(DIFF_CALLBACK) {

    lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(item: Diary)
    }

    inner class DiaryViewHolder(private val binding: RecyclerviewDiaryitemsBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bindData(diary: Diary) {
                with(binding) {
                    Glide.with(binding.root).load(diary.media).into(imageViewMedia)
                    textViewTitle.text = diary.title
                    textViewDescription.text = diary.description
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = RecyclerviewDiaryitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diary = getItem(position)
        if (diary != null) {
            holder.bindData(diary)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Diary>() {
            override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem == newItem
            }
        }
    }
}