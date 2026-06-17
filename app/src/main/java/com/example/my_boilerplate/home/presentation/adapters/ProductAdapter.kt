package com.example.my_boilerplate.home.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.my_boilerplate.base.BaseAdapter
import com.example.my_boilerplate.databinding.HomeItemProductBinding
import com.example.my_boilerplate.home.domain.model.Product

/**
 * Real example of extending BaseAdapter (base/BaseAdapter.kt) - copy this shape for any
 * other list screen. Coil's `load` extension handles placeholder/error/caching for the
 * network thumbnail image with one line.
 */
class ProductAdapter(
    private val onItemClick: (Product) -> Unit
) : BaseAdapter<Product, HomeItemProductBinding>(
    bindingInflater = { inflater, parent, attach ->
        HomeItemProductBinding.inflate(inflater, parent, attach)
    },
    areItemsTheSame = { old, new -> old.id == new.id },
    areContentsTheSame = { old, new -> old == new }
) {
    override fun bind(binding: HomeItemProductBinding, item: Product, position: Int) {
        binding.tvTitle.text = item.title
        binding.tvBrand.text = item.brand
        binding.tvPrice.text = item.formattedPrice
        binding.tvRating.text = item.rating.toString()
        binding.ivThumbnail.load(item.thumbnailUrl) {
            crossfade(true)
        }
        binding.root.setOnClickListener { onItemClick(item) }
    }
}
