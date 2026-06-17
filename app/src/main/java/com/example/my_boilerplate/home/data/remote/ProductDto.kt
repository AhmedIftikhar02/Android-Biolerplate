package com.example.my_boilerplate.home.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Maps 1:1 to DummyJSON's product object. This DTO never leaves the data layer - the
 * Repository converts it into the domain Product model (see home/domain/model/Product.kt)
 * before handing it to the ViewModel. That separation means if the API changes shape later,
 * only this file + the mapper need to change - domain and presentation layers are untouched.
 */
@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String?,
    val price: Double,
    val discountPercentage: Double?,
    val rating: Double?,
    val stock: Int?,
    val brand: String?,
    val category: String?,
    val thumbnail: String?,
    val images: List<String>?
)

@JsonClass(generateAdapter = true)
data class ProductListResponse(
    @Json(name = "products") val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
