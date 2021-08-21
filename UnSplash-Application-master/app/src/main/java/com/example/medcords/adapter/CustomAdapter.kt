package com.example.medcords.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medcords.R
import com.example.medcords.model.Result


class CustomAdapter(val userList: MutableList<Result>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_photos, parent, false)
        return ViewHolder(v)
    }
 
    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])

    }
 
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(photos: Result) {
            val image = itemView.findViewById(R.id.image) as ImageView
            val userImage = itemView.findViewById(R.id.userImage) as ImageView
            Glide.with(itemView.context).load(photos.urls.regular).placeholder(R.drawable.thumbnail).into(image)
            Glide.with(itemView.context).load(photos.user.profile_image.medium).into(userImage)

        }
    }
}
 