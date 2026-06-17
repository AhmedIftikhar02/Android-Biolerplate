package com.example.my_boilerplate.home.data.repository

import com.example.my_boilerplate.core.network.NetworkMonitor
import com.example.my_boilerplate.core.network.safeApiCall
import com.example.my_boilerplate.core.result.Result
import com.example.my_boilerplate.core.result.map
import com.example.my_boilerplate.home.data.remote.ProductApiService
import com.example.my_boilerplate.home.data.remote.toDomain
import com.example.my_boilerplate.home.data.remote.toDomainList
import com.example.my_boilerplate.home.domain.model.Product
import com.example.my_boilerplate.home.domain.repository.ProductRepository
import javax.inject.Inject

/**
 * Every single call goes through safeApiCall (core/network/SafeApiCall.kt) - this is the
 * one and only place network exceptions get converted into typed AppException/Result, so
 * the pattern below is exactly what you'd copy for any new feature's repository.
 */
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    private val networkMonitor: NetworkMonitor
) : ProductRepository {

    override suspend fun getProducts(limit: Int, skip: Int): Result<List<Product>> =
        safeApiCall(networkMonitor) {
            api.getProducts(limit, skip).products
        }.map { it.toDomainList() }

    override suspend fun getProductDetail(id: Int): Result<Product> =
        safeApiCall(networkMonitor) {
            api.getProductDetail(id)
        }.map { it.toDomain() }
}
