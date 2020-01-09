package me.benleggiero.codingtest2020_01_08.views

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.benleggiero.codingtest2020_01_08.R

/**
 * A placeholder fragment containing a simple view.
 */
class ProductDetailActivityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }
}
