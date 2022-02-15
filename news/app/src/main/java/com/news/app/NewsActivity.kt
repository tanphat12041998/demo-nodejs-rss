package com.news.app

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news.*
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.content.SharedPreferences
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.google.gson.Gson

class NewsActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private var rss: RssItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setSupportActionBar(toolbar)
        pref = getSharedPreferences("PREF", Context.MODE_PRIVATE)

        rss = intent.getSerializableExtra(KEY_RSS) as RssItem

        rss?.let {
            Log.d("NewsActivity", it.link)

            toolbar.title = it.title
            toolbar.setNavigationOnClickListener {
                finish()
            }

            webview.loadUrl(it.link)
        }

        saveHistory()
    }

    private fun saveHistory() {
        rss?.let { item ->
            val gson = Gson()
            var list: ArrayList<RssItem>? = gson.fromJson(
                pref.getString("HISTORY", ""),
                object : TypeToken<List<RssItem?>?>() {}.type
            )
            list?.let { ls ->
                val isExists = ls.find { it.link ==  item.link} != null
                if(!isExists) {
                    ls.add(item)
                }
            } ?: kotlin.run{
                list = arrayListOf()
                list!!.add(item)
            }
            val json = gson.toJson(list)
            val editor = pref.edit()
            editor.remove("HISTORY").apply()
            editor.putString("HISTORY", json)
            editor.apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_news, menu)
        if(isExistsInFav()) {
            menu?.get(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        } else {
            menu?.get(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var list = getFavorites()
        when(item.itemId) {
            R.id.fav -> {
                if(isExistsInFav()) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
                    rss?.let { itemRss ->
                        list?.let { ls ->
                            val isExists = ls.find { it.link ==  itemRss.link} != null
                            if(isExists) {
                                ls.remove(itemRss)
                            }
                        }
                        val json = Gson().toJson(list)
                        val editor = pref.edit()
                        editor.remove("FAVORITE").apply()
                        editor.putString("FAVORITE", json)
                        editor.apply()
                    }
                } else {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
                    rss?.let { itemRss ->
                        list?.let { ls ->
                            val isExists = ls.find { it.link ==  itemRss.link} != null
                            if(!isExists) {
                                ls.add(itemRss)
                            }
                        } ?: kotlin.run{
                            list = arrayListOf()
                            list!!.add(itemRss)
                        }
                        val json = Gson().toJson(list)
                        val editor = pref.edit()
                        editor.remove("FAVORITE").apply()
                        editor.putString("FAVORITE", json)
                        editor.apply()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFavorites() : ArrayList<RssItem>? = Gson().fromJson(
        pref.getString("FAVORITE", ""),
        object : TypeToken<List<RssItem?>?>() {}.type
    )

    private fun isExistsInFav() : Boolean {
        val list = getFavorites()
        return list?.let { ls ->
            rss?.let { item ->
                 ls.find { it.link ==  item.link} != null
            } ?: false
        } ?: false
    }

    companion object {
        const val KEY_RSS = "KEY_RSS"
    }
}