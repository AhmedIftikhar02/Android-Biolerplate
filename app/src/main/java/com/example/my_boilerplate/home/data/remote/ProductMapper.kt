package com.example.my_boilerplate.home.data.remote

import com.example.my_boilerplate.home.domain.model.Product

/**
 * The one place nullable DTO fields get resolved into safe defaults. If DummyJSON ever
 * returns a null description, for example, every screen in the app just sees an empty
 * string instead of needing its own null-check - this is the entire point of having a
 * separate DTO and domain model.
 */
fun ProductDto.toDomain(): Product = Product(
    id = id,
    title = title,
    description = description.orEmpty(),
    price = price,
    discountPercentage = discountPercentage ?: 0.0,
    rating = rating ?: 0.0,
    stock = stock ?: 0,
    brand = brand.orEmpty(),
    category = category.orEmpty(),
    thumbnailUrl = thumbnail.orEmpty(),
    imageUrls = images.orEmpty()
)

fun List<ProductDto>.toDomainList(): List<Product> = map { it.toDomain() }
