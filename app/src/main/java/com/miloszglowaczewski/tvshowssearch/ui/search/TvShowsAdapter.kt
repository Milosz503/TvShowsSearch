package com.miloszglowaczewski.tvshowssearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miloszglowaczewski.tvshowssearch.databinding.ItemTvShowBinding


class TvShowsAdapter : ListAdapter<TvShowModel, TvShowsAdapter.ItemViewHolder>(DiffCallback()) {

    class ItemViewHolder(
        private val binding: ItemTvShowBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: TvShowModel) {
            binding.title.text = item.title
            binding.genres.text = item.genres.joinToString(", ")
            Glide
                .with(binding.root)
                .load(item.poster)
                .centerCrop()
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(binding.posterImage);
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