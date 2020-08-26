package com.app.albumsearch.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.albumsearch.utilis.CommonUtilis
import com.app.albumsearch.utilis.Constants
import com.app.albumsearch.utilis.Constants.ARTIST_NAME
import com.app.albumsearch.utilis.Constants.COLLECTION_NAME
import com.app.albumsearch.utilis.Constants.COLLECTION_PRICE
import com.app.albumsearch.utilis.Constants.TRACK_NAME
import com.app.albumsearch.R
import com.app.albumsearch.databinding.ActivitySearchResultBinding
import com.app.albumsearch.databinding.ItemAblumBinding
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.utilis.loadFromUrl
import com.app.albumsearch.viewmodel.StoryViewModel

/**
 * display search result
 */
class SearchResultActivity : BaseActivity() {
    var searchResultBinding: ActivitySearchResultBinding? = null

    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchResultBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_search_result
        )

        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_add_cart -> startActivity(Intent(this, AblumCartActivity::class.java))
            R.id.sort -> showSort()
            else -> {
            }
        }
        return true
    }

    /**
     * show sort option for user
     */
    private fun showSort() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.sort_option))
        val sortOption = arrayOf(COLLECTION_NAME, TRACK_NAME, ARTIST_NAME, COLLECTION_PRICE)
        builder.setItems(sortOption
        ) { dialog, which ->
            when (sortOption[which]) {
                COLLECTION_NAME -> {
                    CommonUtilis.sortByCollectionName(storyViewModel.albumSearchResponse)?.let {
                        storyViewModel.albumSearchResponse.results = it
                    }
                }
                TRACK_NAME -> {
                    CommonUtilis.sortByTrackName(storyViewModel.albumSearchResponse)?.let {
                        storyViewModel.albumSearchResponse.results = it
                    }
                }
                ARTIST_NAME -> {
                    CommonUtilis.sortByArtistName(storyViewModel.albumSearchResponse)?.let {
                        storyViewModel.albumSearchResponse.results = it
                    }
                }
                COLLECTION_PRICE -> {
                    CommonUtilis.sortByCollectionPrice(storyViewModel.albumSearchResponse)?.let {
                        storyViewModel.albumSearchResponse.results = it
                    }
                }
            }
            (searchResultBinding?.albumListView?.adapter as SearchResultListAdapter).submitList(
                storyViewModel.albumSearchResponse.results
            )

            (searchResultBinding?.albumListView?.scrollToPosition(0))
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun init() {
        searchResultBinding?.lifecycleOwner = this

        storyViewModel =
            ViewModelProvider(this, factory).get(StoryViewModel::class.java)

        storyViewModel.albumSearchResponse =
            intent.getSerializableExtra(Constants.KEY_SERACH_RESULT) as AlbumSearchResponse

        initSearchResultListAdapter()

        (searchResultBinding?.albumListView?.adapter as SearchResultListAdapter).submitList(
            storyViewModel.albumSearchResponse.results
        )
    }

    private fun initSearchResultListAdapter() {
        searchResultBinding?.let {
            it.albumListView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            it.albumListView.adapter =
                SearchResultListAdapter(storyViewModel)
        }

    }

    class SearchResultListAdapter(val storyViewModel: StoryViewModel) :
        ListAdapter<Album, SearchResultListAdapter.ViewHolder>(REPO_COMPARATOR) {
        class ViewHolder(val adapterBinding: ItemAblumBinding) : RecyclerView.ViewHolder(
            adapterBinding.root
        ) {

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SearchResultListAdapter.ViewHolder {
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
            holder: SearchResultListAdapter.ViewHolder,
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

                adapterBinding.selectAlbum.setOnCheckedChangeListener { checkBox, isChecked ->
                    if (isChecked) {
                        addAlbumToCart(checkBox.context, album)
                    } else {
                        removeAlbumFromCart(checkBox.context, album)
                    }
                }
            }
        }

        private fun addAlbumToCart(context: Context, album: Album) {
            storyViewModel.addAlbumToCart(context, album)
        }

        private fun removeAlbumFromCart(context: Context, album: Album) {
            storyViewModel.removeAlbumFromCart(context, album)
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