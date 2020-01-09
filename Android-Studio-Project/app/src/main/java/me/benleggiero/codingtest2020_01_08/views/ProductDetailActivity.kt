package me.benleggiero.codingtest2020_01_08.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import me.benleggiero.codingtest2020_01_08.R
import me.benleggiero.codingtest2020_01_08.conveniences.Bitmap
import me.benleggiero.codingtest2020_01_08.conveniences.async
import me.benleggiero.codingtest2020_01_08.dataStructures.Product



class ProductDetailActivity(
    var product: Product? = null
)
    : AppCompatActivity()
{
    var multiLineTitle: CharSequence
        get() = product_detail_title.text
        set(newValue) { product_detail_title.text = newValue }


    private val resourceIdForFabIcon get() = when (product?.isFavorited) {
        true -> R.drawable.ic_star_white_24dp
        false -> R.drawable.ic_star_border_white_24dp
        null -> R.drawable.ic_cached_black_24dp
    }



    // MARK: - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        this.product = intent.getParcelableExtra(productExtraKey)

        fab.setOnClickListener { view ->
            product?.isFavorited = product?.isFavorited?.not() ?: return@setOnClickListener
            updateOnlyFavButtonUi()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onResume() {
        super.onResume()
        updateUi()
    }


    override fun finish() {
        saveProductIntoResultIntent()
        super.finish()
    }


    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun saveProductIntoResultIntent() {
        val resultIntent = Intent()
        resultIntent.putExtra(productExtraKey, product)
        setResult(RESULT_OK, resultIntent)
    }


    // MARK: - Functionality

    fun updateUi() {
        multiLineTitle = product?.title ?: getString(R.string.loadingText)

        updateOnlyFavButtonUi()

        val author = product?.author
        if (null == author) {
            productDetailAuthorNameTextView.text = ""
        }
        else {
            productDetailAuthorNameTextView.text = getString(R.string.productDetailAuthorTextTemplate, author)
        }

        when (val image = product?.image) {
            is Product.Image.none, null -> productDetailImageView.visibility = GONE

            is Product.Image.loading -> {
                productDetailImageView.visibility = VISIBLE
                productDetailImageView.setImageResource(R.drawable.ic_cached_black_24dp)
            }

            is Product.Image.url -> {
                productDetailImageView.visibility = VISIBLE
                productDetailImageView.setImageResource(R.drawable.ic_cached_black_24dp)

                async {
                    val bitmap = Bitmap(jpegImageUrl = image.url)

                    runOnUiThread {
                        productDetailImageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    private fun updateOnlyFavButtonUi() {
        fab.setImageResource(resourceIdForFabIcon)
    }



    companion object {
        const val productExtraKey = """product"""
        const val requestCode = 1
    }
}
