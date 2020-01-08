package me.benleggiero.codingtest2020_01_08

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import me.benleggiero.codingtest2020_01_08.thirdParty.EndlessRecyclerViewScrollListener
import androidx.recyclerview.widget.RecyclerView






class ScrollingActivity : AppCompatActivity() {

    private var scrollListener: EndlessRecyclerViewScrollListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Harry Potter Books"
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/BenLeggiero/Coding-Test-2020-01-08")))
        }

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        val newScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                loadNextProduct(pageNumber = page)
            }
        }

        this.scrollListener = newScrollListener
        recyclerView.addOnScrollListener(newScrollListener)
    }

    override fun onResume() {
        super.onResume()
    }
}



// MARK: - Private functionality

private fun ScrollingActivity.loadNextProduct(pageNumber: Int) {
    Log.e("Placeholder", "Not yet implemented; would load page $pageNumber")
}
