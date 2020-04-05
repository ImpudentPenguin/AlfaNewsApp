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
import com.elenamakeeva.alfanewsapp.api.Item
import com.elenamakeeva.alfanewsapp.model.NewsViewModel
import java.lang.NullPointerException

class NewsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewItem = view.findViewById(R.id.rv_news_list)
        activity?.let {
            viewModel.getAllNews().observe(it, Observer<List<Item>> { items ->
                recyclerViewItem.layoutManager = LinearLayoutManager(it)
                recyclerViewItem.adapter = newsAdapter
                newsAdapter.newsList = items

            })
        }
    }

    override fun onStart() {
        super.onStart()
        newsAdapter.onNewsClickListener = object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClick(position: Int) {
                if(newsAdapter.newsList.isNullOrEmpty()) throw NullPointerException()
                val itemId = newsAdapter.newsList!![position].id
                val singleNewsFragment = SingleNewsFragment()
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putInt("idNews", itemId)
                singleNewsFragment.arguments = bundle
                parentFragmentManager
                    .beginTransaction()
                    .hide(this@NewsFragment)
                    .add(R.id.fragment_container, singleNewsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
