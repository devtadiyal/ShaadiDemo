package com.example.medcords.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medcords.R
import com.example.medcords.model.Result
import kotlinx.android.synthetic.main.list_photos.view.*

class PhotoAdapter : PagedListAdapter<Result, PhotoAdapter.UserViewHolder>(USER_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_photos, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.bind(it) }
    }
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userImage = view.userImage
        private val imageView = view.image

        fun bind(user: Result) {
            Glide.with(userImage.context)
                .load(user.user.profile_image.medium)
                .placeholder(R.drawable.thumbnail)
                .into(userImage);
            Glide.with(imageView.context)
                .load(user.urls.regular)
                .placeholder(R.drawable.thumbnail)
                .into(imageView);
        }
    }
    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                newItem == oldItem
        }
    }
}