package com.news.app

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable


data class RssItem(
    val title: String,
    val link: String,
    val image: String,
    val description: String,
    val pubDate: String
) : Serializable {
    class Deserializer : ResponseDeserializable<Array<RssItem>> {
        override fun deserialize(content: String): Array<RssItem>
                = Gson().fromJson(content, Array<RssItem>::class.java)
    }
}