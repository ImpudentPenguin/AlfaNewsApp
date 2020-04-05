package com.elenamakeeva.alfanewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.elenamakeeva.alfanewsapp.fragments.FavouriteNewsFragment
import com.elenamakeeva.alfanewsapp.fragments.NewsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        title = getString(R.string.news_alfa_bank)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsFragment()).addToBackStack(null).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NewsFragment()).addToBackStack(null).commit()
            }
            R.id.favourite -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavouriteNewsFragment()).addToBackStack(null).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
