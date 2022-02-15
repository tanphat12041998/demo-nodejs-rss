package com.news.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private val rssAdapter = RssItemAdapter()
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireContext().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        setupListView()
        loadFavorite()
    }

    private fun setupListView() {
        list_favorite.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        list_favorite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list_favorite.adapter = rssAdapter

        rssAdapter.setOnItemClick(object : RssItemClick {
            override fun onClick(item: RssItem) {
                val intent = Intent(requireContext(), NewsActivity::class.java)
                intent.putExtra(NewsActivity.KEY_RSS, item)
                startActivity(intent)
            }
        })
    }

    private fun loadFavorite() {
        val gson = Gson()
        val list: ArrayList<RssItem>? = gson.fromJson(
            pref.getString("FAVORITE", ""),
            object : TypeToken<List<RssItem?>?>() {}.type
        )
        list?.let {
            rssAdapter.updateItems(it)
        }
    }
}