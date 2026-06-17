package com.example.my_boilerplate.home.presentation.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_boilerplate.R
import com.example.my_boilerplate.base.BaseFragment
import com.example.my_boilerplate.common.extensions.collectLifecycleFlow
import com.example.my_boilerplate.core.result.UiState
import com.example.my_boilerplate.databinding.HomeFragmentBinding
import com.example.my_boilerplate.home.presentation.adapters.ProductAdapter
import com.example.my_boilerplate.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Full vertical slice example screen (requirement #14). Copy this Fragment + its
 * ViewModel + Repository + ApiService as the template for every new feature you add.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private val adapter = ProductAdapter { product ->
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToHomeProductDetailFragment(product.id)
        )
    }

    override fun setupViews() {
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadProducts()
        }
    }

    override fun observeData() {
        viewModel.uiState.collectLifecycleFlow(this) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is UiState.Idle -> Unit
                is UiState.Loading -> binding.stateLayout.showLoading()
                is UiState.Success -> {
                    binding.stateLayout.showContent()
                    adapter.submitList(state.data)
                }
                is UiState.Empty -> binding.stateLayout.showEmpty(getString(R.string.home_empty))
                is UiState.Error -> binding.stateLayout.showError(state.exception.message) {
                    viewModel.retry()
                }
            }
        }
    }
}
