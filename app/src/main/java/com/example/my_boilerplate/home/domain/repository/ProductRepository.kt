package com.example.my_boilerplate.home.domain.repository

import com.example.my_boilerplate.core.result.Result
import com.example.my_boilerplate.home.domain.model.Product

/**
 * Domain-layer contract. ViewModels (and UseCases, if you add them) depend on THIS interface,
 * never on ProductRepositoryImpl directly - that's what makes it possible to swap the data
 * source later (e.g. add a Room cache-first strategy) without touching the ViewModel at all.
 */
interface ProductRepository {
    suspend fun getProducts(limit: Int = 20, skip: Int = 0): Result<List<Product>>
    suspend fun getProductDetail(id: Int): Result<Product>
}
