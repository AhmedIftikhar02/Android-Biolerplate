package com.example.my_boilerplate.home.domain.model

/**
 * Domain model - what the rest of the app (ViewModel, UI) actually works with. Notice it
 * has no nullable fields the UI would need to null-check repeatedly; the mapper
 * (home/data/remote/ProductMapper.kt) is responsible for supplying sane defaults from the
 * nullable DTO fields, exactly once, in one place.
 */
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnailUrl: String,
    val imageUrls: List<String>
) {
    /** Example of domain logic living in the domain model, not scattered across the UI. */
    val discountedPrice: Double
        get() = price - (price * discountPercentage / 100)

    val formattedPrice: String
        get() = "$%.2f".format(price)
}
