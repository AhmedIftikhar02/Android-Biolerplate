package com.example.my_boilerplate.home.presentation.viewmodels

import com.example.my_boilerplate.base.BaseViewModel
import com.example.my_boilerplate.core.result.Result
import com.example.my_boilerplate.core.result.UiState
import com.example.my_boilerplate.home.domain.model.Product
import com.example.my_boilerplate.home.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Canonical example ViewModel - copy this pattern for every new screen in the app:
 *   1. Expose StateFlow<UiState<T>>, never a mutable one publicly.
 *   2. Set Loading, call the repository, map Result -> UiState, set the result.
 *   3. Use launchSafe (from BaseViewModel) so an unexpected crash doesn't take the app down.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        launchSafe {
            _uiState.value = UiState.Loading
            when (val result = productRepository.getProducts()) {
                is Result.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception)
                }
            }
        }
    }

    /** Called from the StateLayout's retry button (see HomeFragment.observeData()). */
    fun retry() = loadProducts()
}
