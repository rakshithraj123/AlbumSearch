package com.app.albumsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.albumsearch.R
import com.app.albumsearch.databinding.ActivityAblumCartBinding
import com.app.albumsearch.databinding.ItemAblumBinding
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.utilis.loadFromUrl
import com.app.albumsearch.viewmodel.CartViewModel

/**
 * Display album added to cart
 */
class AblumCartActivity : BaseActivity(){
    var albumCarBinding: ActivityAblumCartBinding? = null

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ablum_cart)

        setTitle(getString(R.string.album_cart))
        albumCarBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_ablum_cart
        )

        init()
    }

    private fun init() {
        albumCarBinding?.lifecycleOwner = this

        cartViewModel =
            ViewModelProvider(this, factory).get(CartViewModel::class.java)

        initSearchResultListAdapter()

        cartViewModel.getCartAblum().observe(this, Observer {
                (albumCarBinding?.albumListView?.adapter as AblumCartActivity.CartAlbumListAdapter).submitList(
                    it.albumList.toMutableList()
                )
        })

    }


    private fun initSearchResultListAdapter() {
        albumCarBinding?.let {
            it.albumListView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            it.albumListView.adapter =
                CartAlbumListAdapter()
        }

    }

    class CartAlbumListAdapter() :
        ListAdapter<Album, CartAlbumListAdapter.ViewHolder>(REPO_COMPARATOR)
    {
        class ViewHolder(val adapterBinding: ItemAblumBinding) :  RecyclerView.ViewHolder(
            adapterBinding.root
        ){

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CartAlbumListAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ablum, parent, false)

            var adapterBinding = ItemAblumBinding.inflate(
                LayoutInflater.from(parent.context),
                view as ViewGroup?,
                false
            );

            return ViewHolder(adapterBinding)
        }

        override fun onBindViewHolder(
            holder: CartAlbumListAdapter.ViewHolder,
            position: Int
        ) {
            val album = getItem(position)
            with(holder) {
                adapterBinding.artistName = album.artistName
                adapterBinding.trackName = album.trackName
                adapterBinding.collectioName = album.collectionName
                adapterBinding.collectionPrice = album.collectionPrice.toString()
                adapterBinding.releasedate = album.releaseDate
                adapterBinding.image.loadFromUrl(album.artworkUrl100)

                adapterBinding.selectAlbum.visibility = View.GONE
            }
        }



        companion object {
            private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
                override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean =
                    oldItem.trackName == newItem.trackName

                override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
                    oldItem == newItem
            }
        }

    }
}