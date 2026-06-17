package com.example.my_boilerplate.home.presentation.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.my_boilerplate.R
import com.example.my_boilerplate.base.BaseFragment
import com.example.my_boilerplate.common.extensions.collectLifecycleFlow
import com.example.my_boilerplate.core.result.UiState
import com.example.my_boilerplate.databinding.HomeProductDetailsBinding
import com.example.my_boilerplate.home.domain.model.Product
import com.example.my_boilerplate.home.presentation.viewmodels.HomeProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeProductDetailFragment : BaseFragment<HomeProductDetailsBinding>(HomeProductDetailsBinding::inflate) {

    private val viewModel: HomeProductDetailViewModel by viewModels()
    private val args: HomeProductDetailFragmentArgs by navArgs()

    override fun setupViews() {
        // Toolbar back button
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Add to Cart click
        binding.btnAddToCart.setOnClickListener {
            Timber.d("Add to Cart clicked for product: ${args.productId}")
        }

        // Favorite click
        binding.btnFavorite.setOnClickListener {
            Timber.d("Favorite clicked for product: ${args.productId}")
        }

        // Load details
        viewModel.loadProductDetail(args.productId)
    }

    override fun observeData() {
        viewModel.uiState.collectLifecycleFlow(this) { state ->
            when (state) {
                is UiState.Idle -> Unit
                is UiState.Loading -> {
                    binding.stateLayout.showLoading()
                }
                is UiState.Success -> {
                    binding.stateLayout.showContent()
                    bindProduct(state.data)
                }
                is UiState.Empty -> {
                    binding.stateLayout.showEmpty(getString(R.string.home_empty))
                }
                is UiState.Error -> {
                    binding.stateLayout.showError(state.exception.message) {
                        viewModel.loadProductDetail(args.productId)
                    }
                }
            }
        }
    }

    private fun bindProduct(product: Product) {
        // Collapsing toolbar title
        binding.collapsingToolbar.title = product.title

        // Product image
        binding.ivProductImage.load(product.thumbnailUrl) {
            crossfade(true)
        }

        // Badges
        binding.tvCategory.text = product.category
        binding.tvStockStatus.text = if (product.stock > 0) "In Stock" else "Out of Stock"
        binding.tvDiscount.text = "-%.0f%% OFF".format(product.discountPercentage)

        // Title & Brand
        binding.tvTitle.text = product.title
        binding.tvBrand.text = "by %s".format(product.brand)

        // Price Section
        binding.tvPrice.text = "$%.2f".format(product.discountedPrice)
        binding.tvOriginalPrice.text = product.formattedPrice

        // Highlights Section (Specs)
        binding.tvHighlightDisplay.text = "6.7-inch OLED" // Mocked spec for placeholder
        binding.tvHighlightCategory.text = product.category
        binding.tvHighlightRating.text = "%.2f ★".format(product.rating)
        binding.tvHighlightStock.text = "%d items".format(product.stock)

        // Description
        binding.tvDescription.text = product.description

        // Rating Breakdown Card
        binding.tvRating.text = "%.2f".format(product.rating)
        binding.tvTotalRatings.text = "Based on %d reviews".format((product.stock * 7) + 42) // Mocked total reviews count

        // Distribute progress bars based on rating
        val ratingInt = product.rating.toInt()
        binding.progress5Star.progress = if (ratingInt >= 5) 75 else 20
        binding.progress4Star.progress = if (ratingInt == 4) 60 else 15
        binding.progress3Star.progress = if (ratingInt == 3) 50 else 10
        binding.progress2Star.progress = if (ratingInt == 2) 40 else 5
        binding.progress1Star.progress = if (ratingInt == 1) 30 else 3

        binding.tvCount5Star.text = "%d%%".format(binding.progress5Star.progress)
        binding.tvCount4Star.text = "%d%%".format(binding.progress4Star.progress)
        binding.tvCount3Star.text = "%d%%".format(binding.progress3Star.progress)
        binding.tvCount2Star.text = "%d%%".format(binding.progress2Star.progress)
        binding.tvCount1Star.text = "%d%%".format(binding.progress1Star.progress)
    }
}
