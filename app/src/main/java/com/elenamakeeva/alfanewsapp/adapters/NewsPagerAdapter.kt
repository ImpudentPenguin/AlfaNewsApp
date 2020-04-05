package com.elenamakeeva.alfanewsapp.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.api.Item

class NewsPagerAdapter(private val news: List<Item>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewItem = LayoutInflater.from(container.context).inflate(R.layout.item_news_pager, container, false)
        val webView = viewItem.findViewById(R.id.news_webView) as WebView
        webView.visibility = View.INVISIBLE
        val progressBar = viewItem.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.INVISIBLE

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                webView.visibility = View.GONE

            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                webView.visibility = View.VISIBLE
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('page__header')[0].style.display='none'; " +
                        "document.getElementsByClassName('page__footer')[0].style.display='none'; " +
                        "document.getElementsByClassName('content')[0].style.padding = '5px'; " +
                        "document.getElementsByClassName('layout')[0].style.padding = '10px'; " +
                        "document.getElementsByClassName('page__inner')[0].style.padding = '0px'; " +
                        "document.getElementsByClassName('title')[0].style.padding = '0px';" +
                        "})()")
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(news[position].link)
        container.addView(viewItem)
        return viewItem
    }

    override fun isViewFromObject(view: View, obj : Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int = news.size

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) = container.removeView(obj as View)
}