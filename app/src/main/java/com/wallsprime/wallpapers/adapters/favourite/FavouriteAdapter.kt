package com.wallsprime.wallpapers.adapters.favourite




import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.databinding.SingleItemImageBinding
import com.wallsprime.wallpapers.ui.main.MainFragmentDirections

class FavouriteAdapter(
    private val onFavouriteClick: (UnsplashPhoto) -> Unit
) : ListAdapter<UnsplashPhoto, FavouriteAdapter.ViewHolder>(PhotoComparator()) {


    class ViewHolder(private val binding: SingleItemImageBinding, private val onFavouriteClick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {



        fun bind( position: Int,item: UnsplashPhoto?) {

            binding.apply {

                val url: String? = item?.urls?.small
                Glide.with(itemView.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_load_icon)
                    .into(imageViewSingleItemImage)


                imageViewFav.setImageResource(
                    when {
                        item!!.favourite -> R.drawable.ic_baseline_favorite_24
                        else -> R.drawable.ic_baseline_favorite_border_24_toolbar
                    }
                )

                imageViewSingleItemImage.setOnClickListener {
                    val direction =
                        MainFragmentDirections.actionFavouriteFragmentToDetailViewFavouriteFragment(position)
                    it.findNavController().navigate(direction)

                }

            }
        }

       init {
           binding.imageViewFav.setOnClickListener {
               val positionFav = bindingAdapterPosition
               if (positionFav != RecyclerView.NO_POSITION) {
                   onFavouriteClick(positionFav)
               }
           }
       }



    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            SingleItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding,

            onFavouriteClick = { position ->
                val photoItem = getItem(position)
                if (photoItem != null) {
                    onFavouriteClick(photoItem)
                }
            }

        )

    }



    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(position,item)

    }


}