package  com.app.albumsearch.repository.storage

/** Constants for specific storage types */
const val STORE_ALBUM = 1
const val STORE_CART = 2

/** File names for specific storage types */
const val ALBUM_FILE_NAME = "album.txt"
const val CART_FILE_NAME = "cart.txt"

/** Returns the storage file name based on the storage type */
fun getFileNameFor(type: Int): String {
    return when (type) {
        STORE_ALBUM -> ALBUM_FILE_NAME
        STORE_CART -> CART_FILE_NAME
        else -> ""
    }
}