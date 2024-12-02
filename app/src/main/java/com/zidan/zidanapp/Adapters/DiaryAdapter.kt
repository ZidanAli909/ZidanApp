package com.zidan.zidanapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.zidanapp.Data.Model.Diary
import com.zidan.zidanapp.databinding.RecyclerviewDiaryitemsBinding

class DiaryAdapter(private val diaryList: MutableList<Diary>)
    : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

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
        return DiaryViewHolder(RecyclerviewDiaryitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bindData(diaryList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(diaryList[position])
        }
    }
}