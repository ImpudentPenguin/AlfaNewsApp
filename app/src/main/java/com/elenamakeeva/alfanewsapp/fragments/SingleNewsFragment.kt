package com.elenamakeeva.alfanewsapp.fragments

import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager

import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.adapters.NewsPagerAdapter
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item
import com.elenamakeeva.alfanewsapp.model.NewsViewModel
import kotlinx.android.synthetic.main.item_news_pager.view.*

class SingleNewsFragment : Fragment() {
    private var newsPosition: Int = -1
    private var newsId: Int = -1
    private lateinit var viewModel: NewsViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var itemList: ArrayList<Item>
    private lateinit var addFavBtn: MenuItem
    private var favouriteNews: FavouriteNews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        itemList = arrayListOf()

        if(bundle != null && !bundle.isEmpty) {
           newsPosition = bundle.getInt("position")
            newsId = bundle.getInt("idNews")
        } else parentFragmentManager.beginTransaction().replace(R.id.fragment_container, NewsFragment()).commit()

        activity?.let {
            viewModel = ViewModelProvider(it).get(NewsViewModel::class.java)
            viewModel.viewState.observe(it, Observer { items ->
                itemList.addAll(items)
            })
            viewModel.getFavouriteNews(newsId).observe(it, Observer {favNews ->
                favouriteNews = favNews
            })
        }
        setHasOptionsMenu(true)
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
        activity?.invalidateOptionsMenu()
        addFavBtn = menu.findItem(R.id.add_favourite)
        addFavBtn.isVisible = true
        val item = itemList[viewPager.currentItem]
        addFavBtn.setOnMenuItemClickListener {
            if(favouriteNews == null) {
                addFavBtn.icon.mutate()
                viewModel.insertFavouriteNews(FavouriteNews(item.id, item.title, item.link, item.pubDate, item.guid))
                viewPager.news_webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
                Toast.makeText(context, "Добавлено", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavouriteNews(FavouriteNews(item.id, item.title, item.link, item.pubDate, item.guid))
                Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
            }
            true
        }
        super.onPrepareOptionsMenu(menu)
    }
}
