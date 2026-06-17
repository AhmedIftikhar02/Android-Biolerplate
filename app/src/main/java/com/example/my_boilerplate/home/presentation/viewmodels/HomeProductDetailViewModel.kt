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

@HiltViewModel
class HomeProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState<Product>>(UiState.Idle)
    val uiState: StateFlow<UiState<Product>> = _uiState.asStateFlow()

    fun loadProductDetail(id: Int) {
        launchSafe {
            _uiState.value = UiState.Loading
            when (val result = productRepository.getProductDetail(id)) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception)
                }
            }
        }
    }
}
