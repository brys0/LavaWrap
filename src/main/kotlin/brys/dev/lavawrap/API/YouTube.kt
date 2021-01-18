package brys.dev.lavawrap.API

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import java.lang.Exception

/**
 *  [Object] that contains all needed YouTube functions to query and retrieve a YouTube URL
 */
object YouTube {
    private val temp: YouTube? = YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null).setApplicationName("Kyro").build()
    private val search = temp?.search()!!
    /**
     * A YouTube query function That Queries [YouTube] and returns a [String] of a YouTube video url / urls
     */
    fun query(args: String?, amount: Int, auth: String): MutableList<String>? {
        when (args.isNullOrEmpty()) {
            true -> throw Exception("A null string was provided!")
        }
        try {
            val results = search.list("id,snippet")
                .setQ(args)
                .setMaxResults(amount.toLong())
                .setType("video")
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                .setKey(auth)
                .execute()
                .items
            if (results.isNotEmpty()) {
                val videoList: MutableList<String> = ArrayList()
                for (i in 0 until results.size) {
                    val ids = results[i]
                    videoList.add(i, "https://www.youtube.com/watch?v=${ids.id.videoId}")
                }
                return videoList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun rawQuery(args: String?, amount: Int, auth: String): MutableList<SearchResult>? {
        when (args.isNullOrEmpty()) {
            true -> throw Exception("A null string was provided!")
        }
        try {
            val results = search.list("id,snippet")
                .setQ(args)
                .setMaxResults(amount.toLong())
                .setType("video")
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                .setKey(auth)
                .execute()
                .items
            if (results.isNotEmpty()) {
                val videoList: MutableList<SearchResult> = ArrayList()
                for (i in 0 until results.size) {
                    val ids = results[i]
                    videoList.add(i, ids)
                }
                return videoList
            }
        } catch (e: Exception) {
            println("Something malfunctioned in YouTube Package")
        }
        return null
    }
    fun parseTitle(id: String, auth: String): String? {
        val titles =  temp?.videos()?.list("id,snippet")
            ?.setId(id)
            ?.setKey(auth)
            ?.execute()
            ?.items
        return titles?.get(0)?.snippet?.title
    }
}