package com.zidan.zidanapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.zidanapp.Data.Models.Diary
import com.zidan.zidanapp.databinding.RecyclerviewDiaryitemsBinding

class DiaryAdapter : PagedListAdapter<Diary, DiaryAdapter.DiaryViewHolder>(DIFF_CALLBACK) {

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
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewDiaryitemsBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diary: Diary? = getItem(position)
        if (diary != null) {
            holder.bindData(diary)
            holder.itemView.setOnClickListener {
                if (::onItemClickListener.isInitialized) onItemClickListener.onItemClick(diary)
            }
        } else {
            holder.itemView.setOnClickListener(null)
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