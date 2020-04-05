package com.elenamakeeva.alfanewsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager

import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.adapters.NewsPagerAdapter
import com.elenamakeeva.alfanewsapp.api.AbstractNews
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.model.NewsViewModel

class SingleNewsFragment : Fragment() {
    private var newsPosition: Int = -1
    private var newsId: Int = -1
    private var isFavouriteFragment = false
    private lateinit var viewModel: NewsViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var itemList: ArrayList<AbstractNews>
    private lateinit var addFavBtn: MenuItem
    private var favouriteNews: FavouriteNews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        itemList = arrayListOf()
        setHasOptionsMenu(true)

        if(bundle != null && !bundle.isEmpty) {
           newsPosition = bundle.getInt("position")
           newsId = bundle.getInt("idNews")
           isFavouriteFragment = bundle.getBoolean("isFavouriteFragment")
        } else parentFragmentManager.beginTransaction().replace(R.id.fragment_container, NewsFragment()).commit()

        activity?.let {
            viewModel = ViewModelProvider(it).get(NewsViewModel::class.java)
            if(!isFavouriteFragment) {
                viewModel.viewState.observe(it, Observer { items ->
                    itemList.addAll(items)
                })
            } else {
                viewModel.viewStateFavNewsList.observe(it, Observer { items ->
                    itemList.addAll(items)
                })
            }
            viewModel.getFavouriteNews(newsId).observe(it, Observer {favNews ->
                favouriteNews = favNews
            })
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_news, container, false)
        viewPager = view.findViewById(R.id.news_viewPager)
        viewPager.adapter = NewsPagerAdapter(itemList)
        viewPager.currentItem = newsPosition
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        addFavBtn = menu.findItem(R.id.add_favourite)
        addFavBtn.isVisible = true
        setFavourite()
        val item = itemList[viewPager.currentItem]

        addFavBtn.setOnMenuItemClickListener {
            viewModel.getFavouriteNews(newsId).observe(viewLifecycleOwner, Observer {favNews ->
                favouriteNews = favNews
            })
            if(favouriteNews == null) {
                viewModel.insertFavouriteNews(FavouriteNews(item.id, item.title, item.link, item.pubDate))
                Toast.makeText(context, getString(R.string.news_add_favourite), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.deleteFavouriteNews(favouriteNews!!)
                Toast.makeText(context, getString(R.string.news_remove_favourite), Toast.LENGTH_SHORT).show()
            }
            setFavourite()
            true
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun setFavourite() {
        viewModel.getFavouriteNews(newsId).observe(viewLifecycleOwner, Observer {favNews ->
            favouriteNews = favNews
            if(favouriteNews == null) addFavBtn.icon = ContextCompat.getDrawable(view!!.context, R.drawable.ic_add_favourite)
            else addFavBtn.icon = ContextCompat.getDrawable(view!!.context, R.drawable.ic_remove_favourite)
        })
    }
}
