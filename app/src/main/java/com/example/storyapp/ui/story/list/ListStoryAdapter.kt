package com.example.storyapp.ui.story.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.local.entity.StoryItem
import com.example.storyapp.databinding.StoryLayoutBinding

class ListStoryAdapter(
    private val listener: (StoryItem) -> Unit
) : PagingDataAdapter<StoryItem, ListStoryAdapter.ListStoryViewHolder>(DIFF_CALLBACK) {

    inner class ListStoryViewHolder(binding: StoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ivItemPhoto
        private val name = binding.tvItemName
        private val desc = binding.storyDescriptionTv

        fun bind(item: StoryItem) {
            Glide.with(itemView).load(item.photoUrl).into(image)
            name.text = item.name
            desc.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val binding = StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener { listener(item) }
        }

    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryItem,
                newItem: StoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}