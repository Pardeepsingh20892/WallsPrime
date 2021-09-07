package com.wallsprime.wallpapers.adapters.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallsprime.wallpapers.R
import com.wallsprime.wallpapers.data.common.UnsplashPhoto
import com.wallsprime.wallpapers.databinding.SingleItemImageFullScreenBinding


class CategoryDetailViewAdapter(
    private val onFavouriteClick: (UnsplashPhoto) -> Unit,
    private val getItemUrl: (itemUrl: String,code: Int) -> Unit,
    private val itemPosition: (itemPosition: Int) -> Unit,
    private val userProfile: (profile: String) -> Unit

) : PagingDataAdapter<UnsplashPhoto, CategoryDetailViewAdapter.ViewHolder>(
    CategoryComparator()
) {


    class ViewHolder(private val binding: SingleItemImageFullScreenBinding, private val onFavouriteClick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(getItemUrl: ( item:String, code:Int) -> Unit,item: UnsplashPhoto?){

            var sp = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            var url: String = when(sp.getString("load_quality","")){
                "regular" -> item?.urls?.regular!!
                "full" -> item?.urls?.full!!
                "small" -> item?.urls?.small!!
                else  -> item?.urls?.regular!!
            }

            var itemUrl: String = when(sp.getString("download_quality","")){
                "regular" -> item?.urls?.regular!!
                "full" -> item?.urls?.full!!
                "small" -> item?.urls?.small!!
                else  -> item?.urls?.full!!
            }


            binding.apply {


                Glide.with(itemView.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_load_full)
                    .into(imageViewSingleItemFullScreen)


                favhome.setImageResource(
                    when {
                        item.favourite -> R.drawable.ic_baseline_favorite_24
                        else -> R.drawable.ic_baseline_favorite_border_24_toolbar
                    }
                )

                downloadHome.setOnClickListener {
                    getItemUrl(itemUrl,1)

                }

                setHome.setOnClickListener {
                    getItemUrl(itemUrl,2)

                }

                shareHome.setOnClickListener {
                    getItemUrl(itemUrl,3)

                }


            }

        }




          init {
              binding.favhome.setOnClickListener {
                  val positionFav = bindingAdapterPosition
                  if (positionFav != RecyclerView.NO_POSITION) {
                      onFavouriteClick(positionFav)
                  }
              }
          }




    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            SingleItemImageFullScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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


        itemPosition(position)

        val item = getItem(position)
        viewHolder.bind(getItemUrl ={itemUrl,code ->
            getItemUrl(itemUrl,code)
        },item)

        userProfile(item!!.user.attributionUrl)
    }
}