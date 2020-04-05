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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_news.*
import java.lang.NullPointerException

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsAdapter = NewsAdapter()
        disposable = CompositeDisposable()
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
        rv_news_list.layoutManager = LinearLayoutManager(view.context)
        rv_news_list.adapter = newsAdapter
        newsAdapter.notifyDataSetChanged()
        swipe_layout.setOnRefreshListener {
            disposable.clear()
            disposable.addAll(viewModel.onLoadNews())
            swipe_layout.isRefreshing = false
        }
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            newsAdapter.newsList = it
            newsAdapter.notifyDataSetChanged()
        })

        newsAdapter.onNewsClickListener = object : NewsAdapter.OnNewsClickListener {
            override fun onNewsClick(position: Int) {
                if(newsAdapter.newsList.isNullOrEmpty()) throw NullPointerException()
                val itemId = newsAdapter.newsList!![position].id
                val singleNewsFragment = SingleNewsFragment()
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putInt("idNews", itemId)
                bundle.putBoolean("isFavouriteFragment", false)
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

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
