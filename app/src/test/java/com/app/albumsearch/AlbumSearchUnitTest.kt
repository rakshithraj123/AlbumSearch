package com.app.albumsearch

import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.utilis.CommonUtilis
import org.junit.Assert
import org.junit.Test

class AlbumSearchUnitTest {
    @Test
    fun testRemoveDuplicateAlbum_vaild() {
        var results  = mutableListOf<Album>()

        results.add(Album(trackName = "track1"))
        results.add(Album(trackName = "track1"))
        results.add(Album(trackName = "track1"))
        CommonUtilis.removeDuplicateAlbum(AlbumSearchResponse(results.size,results)).let {
            Assert.assertEquals(1, it?.size)
        }
    }

    @Test
    fun testRemoveDuplicateAlbum_invaild() {
        var results  = mutableListOf<Album>()

        results.add(Album(trackName = "track1"))
        results.add(Album(trackName = "track2"))
        results.add(Album(trackName = "track3"))
        CommonUtilis.removeDuplicateAlbum(AlbumSearchResponse(results.size,results)).let {
            Assert.assertEquals(3, it?.size)
        }
    }

    @Test
    fun testSortByReleaseDate() {
        var results  = mutableListOf<Album>()

        results.add(Album(releaseDate = "2008-06-02T12:00:00Z"))
        results.add(Album(releaseDate = "2010-11-22T12:00:00Z"))
        results.add(Album(releaseDate = "2002-10-29T08:00:00Z"))

        CommonUtilis.sortByReleaseDate(AlbumSearchResponse(results.size,results)).let {

            Assert.assertEquals("2010-11-22T12:00:00Z", it?.get(0)?.releaseDate)
            Assert.assertEquals("2008-06-02T12:00:00Z", it?.get(1)?.releaseDate)
            Assert.assertEquals("2002-10-29T08:00:00Z", it?.get(2)?.releaseDate)
        }
    }

    @Test
    fun testSortByTrackName() {
        var results  = mutableListOf<Album>()

        results.add(Album(trackName = "Bill"))
        results.add(Album(trackName = "Jhon"))
        results.add(Album(trackName = "Adem"))

        CommonUtilis.sortByTrackName(AlbumSearchResponse(results.size,results)).let {

           Assert.assertEquals("Jhon", it?.get(0)?.trackName)
            Assert.assertEquals("Bill", it?.get(1)?.trackName)
            Assert.assertEquals("Adem", it?.get(2)?.trackName)
        }
    }

    @Test
    fun testSortByArtistName() {
        var results  = mutableListOf<Album>()

        results.add(Album(artistName = "artistName2"))
        results.add(Album(artistName = "artistName1"))
        results.add(Album(artistName = "artistName3"))

        CommonUtilis.sortByArtistName(AlbumSearchResponse(results.size,results)).let {

            Assert.assertEquals("artistName3", it?.get(0)?.artistName)
            Assert.assertEquals("artistName2", it?.get(1)?.artistName)
            Assert.assertEquals("artistName1", it?.get(2)?.artistName)
        }
    }

    @Test
    fun testSortByCollectionPrice() {
        var results  = mutableListOf<Album>()

        results.add(Album(collectionPrice = 25.8))
        results.add(Album(collectionPrice = 13.9))
        results.add(Album(collectionPrice = 50.5))

        CommonUtilis.sortByCollectionPrice(AlbumSearchResponse(results.size,results)).let {

            Assert.assertEquals(50.5, it?.get(0)?.collectionPrice)
            Assert.assertEquals(25.8, it?.get(1)?.collectionPrice)
            Assert.assertEquals( 13.9, it?.get(2)?.collectionPrice)
        }
    }

}

