package com.maseria.diary

import android.content.Context
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val newsItems = parseNews(this,"example.xml")
        val lvNews = findViewById<ListView>(R.id.lv_news)

        // Create a list of maps for the SimpleAdapter
        val newsList = newsItems.map { newsItem ->
            mapOf(
                "title" to newsItem.title,
                "keyword" to newsItem.keyword,
                "highlights" to newsItem.highlights
            )
        }

        // Define the keys and the corresponding TextViews in item_news.xml
        val from = arrayOf("title", "keyword", "highlights")
        val to = intArrayOf(R.id.tv_news_title, R.id.tv_news_keyword, R.id.tv_news_highlights)

        // Create and set the SimpleAdapter
        val adapter = SimpleAdapter(this, newsList, R.layout.item_news, from, to)
        lvNews.adapter = adapter
    }

    fun parseNews(context: Context, xmlFileName: String): List<NewsItem> {
        val newsItems = mutableListOf<NewsItem>()
        try {
            val inputStream: InputStream = context.assets.open(xmlFileName)
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (name.equals("newsItem", ignoreCase = true)) {
                            val title = parser.getAttributeValue(null, "title")
                            val keyword = parser.getAttributeValue(null, "keyword")
                            val highlights = parser.getAttributeValue(null, "highlights")
                            newsItems.add(NewsItem(title, keyword, highlights))
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newsItems
    }
}