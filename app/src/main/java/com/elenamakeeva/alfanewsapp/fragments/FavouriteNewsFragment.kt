package com.elenamakeeva.alfanewsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.adapters.NewsAdapter
import com.elenamakeeva.alfanewsapp.model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_favourite_news.*

class FavouriteNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

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
        rv_news_list.layoutManager = LinearLayoutManager(view.context)
        rv_news_list.adapter = newsAdapter

        viewModel.viewStateFavNewsList.observe(viewLifecycleOwner, Observer { favNews ->
            newsAdapter.notifyDataSetChanged()
            newsAdapter.newsList = favNews
            if (favNews.isNullOrEmpty()) tv_emptyList.visibility = View.VISIBLE
        })

        newsAdapter.onNewsClickListener = object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClick(position: Int) {
                val singleNewsFragment = SingleNewsFragment()
                val bundle = Bundle()
                val itemId = newsAdapter.newsList!![position].id
                bundle.putInt("position", position)
                bundle.putInt("idNews", itemId)
                bundle.putBoolean("isFavouriteFragment", true)
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
