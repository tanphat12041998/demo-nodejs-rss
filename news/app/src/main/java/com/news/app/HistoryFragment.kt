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
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private val rssAdapter = RssItemAdapter()
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireContext().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        setupListView()
        loadHistory()
    }

    private fun setupListView() {
        list_history.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        list_history.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list_history.adapter = rssAdapter

        rssAdapter.setOnItemClick(object : RssItemClick {
            override fun onClick(item: RssItem) {
                val intent = Intent(requireContext(), NewsActivity::class.java)
                intent.putExtra(NewsActivity.KEY_RSS, item)
                startActivity(intent)
            }
        })
    }

    private fun loadHistory() {
        val gson = Gson()
        val list: ArrayList<RssItem>? = gson.fromJson(
            pref.getString("HISTORY", ""),
            object : TypeToken<List<RssItem?>?>() {}.type
        )
        list?.let {
            rssAdapter.updateItems(it)
        }
    }
}