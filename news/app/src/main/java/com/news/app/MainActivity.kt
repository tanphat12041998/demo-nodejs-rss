package com.news.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        navigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.home -> loadFragment(HomeFragment())
                R.id.history -> loadFragment(HistoryFragment())
                R.id.favorite -> loadFragment(FavoriteFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}