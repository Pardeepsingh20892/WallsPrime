package com.wallsprime.wallpapers.adapters.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.data.explore.UnsplashExplore
import com.wallsprime.wallpapers.databinding.SingleItemExploreFragmentBinding
import com.wallsprime.wallpapers.ui.main.MainFragmentDirections


class ExploreAdapter(
    private val categoryData:List<UnsplashExplore>,
    private val getPositionId: (positionId:String)-> Unit
    ) :  RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {



    class ExploreViewHolder(private val binding: SingleItemExploreFragmentBinding, private val categoryData:List<UnsplashExplore>): RecyclerView.ViewHolder(binding.root) {


        fun bind( position: Int, getPositionId: (positionId:String)-> Unit) {


            binding.apply {

            val url: String = categoryData[position].imageUrl

            Glide.with(itemView.context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_load_icon)
                .into(imageViewSingleItemCollectionFragment)

                imageViewSingleItemCollectionFragment.setOnClickListener {
                getPositionId(categoryData[position].id)
                val direction = MainFragmentDirections.actionExploreFragmentToCategoryFragmentExploreFragment(categoryData[position].id,categoryData[position].name)
                it.findNavController().navigate(direction)

            }


            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding =
            SingleItemExploreFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ExploreViewHolder(binding,categoryData)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(position,getPositionId = {
            getPositionId(it)
        })


    }







    override fun getItemCount(): Int {
       return categoryData.size
    }


}