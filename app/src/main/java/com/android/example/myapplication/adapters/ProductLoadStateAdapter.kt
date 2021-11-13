package com.android.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.myapplication.databinding.LoadStateFooterBinding

class ProductLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ProductLoadStateAdapter.Vh>() {

    inner class Vh(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
                retryBtn.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: Vh, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Vh {
        return Vh(
            LoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}