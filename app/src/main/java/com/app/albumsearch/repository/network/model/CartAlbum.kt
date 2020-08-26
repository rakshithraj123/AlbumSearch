package com.app.albumsearch.repository.network.model

import java.io.Serializable

class CartAlbum : Serializable {
    var albumList = mutableSetOf<Album>()
}