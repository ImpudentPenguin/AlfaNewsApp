package com.elenamakeeva.alfanewsapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.adapters.NewsAdapter
import com.elenamakeeva.alfanewsapp.model.NewsViewModel

class FavouriteNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerViewItem: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsAdapter = NewsAdapter()

        activity?.let {
            viewModel = ViewModelProvider(it).get(NewsViewModel::class.java)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewItem = view.findViewById(R.id.rv_news_list)
        activity?.let {
            viewModel.getAllFavouriteNews().observe(it, Observer { favNews ->
                recyclerViewItem.layoutManager = LinearLayoutManager(it)
                recyclerViewItem.adapter = newsAdapter
                Log.i("infoAlfa",favNews.isNullOrEmpty().toString())
                newsAdapter.newsList = favNews
            })
        }
    }

    override fun onStart() {
        super.onStart()
        newsAdapter.onNewsClickListener = object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClick(position: Int) {
                val singleNewsFragment = SingleNewsFragment()
                val bundle = Bundle()
                bundle.putInt("position", position)
                singleNewsFragment.arguments = bundle
                parentFragmentManager
                    .beginTransaction()
                    .hide(this@FavouriteNewsFragment)
                    .add(R.id.fragment_container, singleNewsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
