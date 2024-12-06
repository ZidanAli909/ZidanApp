package com.zidan.zidanapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zidan.zidanapp.databinding.RecyclerviewLoadingitemsBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onBindViewHolder(
        holder: LoadingStateAdapter.LoadingStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateAdapter.LoadingStateViewHolder {
        val binding = RecyclerviewLoadingitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    class LoadingStateViewHolder(private val binding: RecyclerviewLoadingitemsBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    textViewMessage.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState is LoadState.Error
                textViewMessage.isVisible = loadState is LoadState.Error
            }
        }

        }

}