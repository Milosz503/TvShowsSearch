package com.miloszglowaczewski.tvshowssearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miloszglowaczewski.tvshowssearch.databinding.ItemTvShowBinding


class TvShowsAdapter : ListAdapter<TvShowModel, TvShowsAdapter.ItemViewHolder>(DiffCallback()) {

    class ItemViewHolder(
        private val binding: ItemTvShowBinding
        ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: TvShowModel) {
            binding.title.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTvShowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<TvShowModel>() {
    override fun areItemsTheSame(oldItem: TvShowModel, newItem: TvShowModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TvShowModel, newItem: TvShowModel): Boolean {
        return oldItem == newItem
    }

}