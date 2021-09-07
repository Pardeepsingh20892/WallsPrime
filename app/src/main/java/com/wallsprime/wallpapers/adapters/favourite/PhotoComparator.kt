package com.wallsprime.wallpapers.adapters.favourite

import androidx.recyclerview.widget.DiffUtil
import com.wallsprime.wallpapers.data.common.UnsplashPhoto

class PhotoComparator : DiffUtil.ItemCallback<UnsplashPhoto>() {

    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
        oldItem == newItem
}