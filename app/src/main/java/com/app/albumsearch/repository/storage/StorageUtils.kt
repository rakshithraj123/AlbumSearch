package  com.app.albumsearch.repository.storage

import android.content.Context
import android.text.TextUtils
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.repository.network.model.CartAlbum

import java.io.*


enum class TYPE {
    WRITE, READ
}

/**
 * Utility class for data storage
 */
object StorageUtils {

    /** Writes the data to storage based on the Type */
    @Synchronized
    private fun writeData(context: Context?, type: Int, data: Any): Any? {
        return readWriteData(context, type, data, TYPE.WRITE) ?: false
    }

    /** Reads the data from storage based on the Type */
    @Synchronized
    private fun readData(context: Context?, type: Int): Any? {
        return readWriteData(context, type, null, TYPE.READ)
    }

    /** Performs storage read or write operation based on the Type */
    @Synchronized
    private fun readWriteData(
        context: Context?,
        type: Int,
        data: Any?,
        operationType: TYPE?
    ): Any? {
        return when (operationType) {
            TYPE.WRITE -> writeDataToFile(context, type, data)
            TYPE.READ -> readDataFromFile(context, type)
            else -> null
        }
    }

    /** Writes the data to file based on the Type */
    @Synchronized
    private fun writeDataToFile(context: Context?, type: Int, data: Any?): Any? {
        var result = false
        context?.let {
            val fileName = getFileNameFor(type)
            try {
                result = write(context, fileName, data)
            } catch (fileNotFoundException: FileNotFoundException) {
                fileNotFoundException.printStackTrace()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
        return result
    }

    /** Reads the data from file based on the Type */
    @Synchronized
    private fun readDataFromFile(context: Context?, type: Int): Any? {
        var data: Any? = null
        context?.let {
            val fileName: String = getFileNameFor(type)
            var fileInputStream: FileInputStream? = null

            if (context != null && !TextUtils.isEmpty(fileName)) {
                try {
                    fileInputStream = context.openFileInput(fileName)
                    val inputStream = ObjectInputStream(fileInputStream)
                    data = inputStream.readObject() as Any?
                    inputStream.close()
                    fileInputStream.close()
                } catch (fileNotFoundException: FileNotFoundException) {
                    fileNotFoundException.printStackTrace()
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                } catch (classNotFoundException: ClassNotFoundException) {
                    classNotFoundException.printStackTrace()
                }
            }
            return data
        } ?: return data
    }

    @Synchronized
    private fun write(context: Context?, fileName: String, data: Any?): Boolean {
        var result = false
        val fileOutputStream = context?.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream?.let {
            data?.let {
                synchronized(it) {
                    val outputStream = ObjectOutputStream(fileOutputStream)
                    outputStream.writeObject(data)
                    outputStream.close()
                    fileOutputStream.close()
                    result = true
                }
            }
        }
        return result
    }

    /** Stores the Album data */
    internal fun storeAblum(context: Context, data: AlbumSearchResponse): Boolean {
        if(data.results?.size == 0){
            return false
        }else{
            return writeData(context, STORE_ALBUM, data) as Boolean
        }
    }

    /** Gets the Album data from storage */
    internal fun getAlbum(context: Context): AlbumSearchResponse {
        return readData(context, STORE_ALBUM) as AlbumSearchResponse
    }

    internal fun addAlbumToCart(context: Context, album: Album): Boolean  {
        val cartAlbum = getCartAlbum(context)
        cartAlbum.albumList.add(album)
        return writeData(context, STORE_CART, cartAlbum) as Boolean
    }

    internal fun removeAlbumFromCart(context: Context, album: Album): Boolean  {
        val cartAlbum = getCartAlbum(context)
        cartAlbum.albumList.remove(album)
        return writeData(context, STORE_CART, cartAlbum) as Boolean
    }

    internal fun getCartAlbum(context: Context) : CartAlbum {
        return readData(context, STORE_CART)?.let {
            it as CartAlbum
        }?:CartAlbum()
    }
}