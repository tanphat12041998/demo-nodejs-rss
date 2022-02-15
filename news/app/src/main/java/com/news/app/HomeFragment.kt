package com.news.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val rssAdapter = RssItemAdapter()
    private val URL = "http://192.168.1.3:1998/fetchNews"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListView()
        fetchRss()
    }

    private fun setupListView() {
        list_home.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        list_home.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list_home.adapter = rssAdapter

        rssAdapter.setOnItemClick(object : RssItemClick {
            override fun onClick(item: RssItem) {
                val intent = Intent(requireContext(), NewsActivity::class.java)
                intent.putExtra(NewsActivity.KEY_RSS, item)
                startActivity(intent)
            }
        })
    }

    private fun fetchRss() {
        Fuel.get(URL).responseObject(RssItem.Deserializer()) { _, _, result ->
            Handler(Looper.getMainLooper()).post {
                result.component1()?.let {
                    rssAdapter.updateItems(it.toList())
                } ?: kotlin.run {
                    Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}