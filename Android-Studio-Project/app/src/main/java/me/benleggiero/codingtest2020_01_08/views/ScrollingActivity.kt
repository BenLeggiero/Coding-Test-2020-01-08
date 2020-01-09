package me.benleggiero.codingtest2020_01_08

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import me.benleggiero.codingtest2020_01_08.thirdParty.EndlessRecyclerViewScrollListener
import androidx.recyclerview.widget.RecyclerView
import me.benleggiero.codingtest2020_01_08.controllers.ProductsRecyclerViewAdapter
import me.benleggiero.codingtest2020_01_08.dataStructures.Product
import me.benleggiero.codingtest2020_01_08.serialization.ProductsLoader


class ScrollingActivity : AppCompatActivity() {

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    private val recyclerViewAdapter = ProductsRecyclerViewAdapter(
        context = this,
        products = listOf(Product.loading)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Ben Leggiero Coding Test 2020-01-08"
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/BenLeggiero/Coding-Test-2020-01-08")))
        }

        recyclerView.adapter = recyclerViewAdapter

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

        ReadAllProductsTask().execute()
    }

    override fun onResume() {
        super.onResume()
    }



    private inner class ReadAllProductsTask: AsyncTask<Unit, Unit, List<Product>>() {
        override fun doInBackground(vararg params: Unit?) = ProductsLoader.loadProducts(inputStream = resources.openRawResource(R.raw.products))


        override fun onPostExecute(result: List<Product>?) {
            super.onPostExecute(result)

            if (null != result) {
                recyclerViewAdapter.products = result
            }
        }
    }
}



// MARK: - Private functionality

private fun ScrollingActivity.loadNextProduct(pageNumber: Int) {
    // See https://github.com/BenLeggiero/Coding-Test-2020-01-08/tree/feature/Lazy-Loading
}
