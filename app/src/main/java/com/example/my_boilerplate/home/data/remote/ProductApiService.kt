package com.example.my_boilerplate.home.data.remote

import com.example.my_boilerplate.core.network.NoAuth
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * DummyJSON's product endpoints don't require auth at all, so BOTH methods below use @NoAuth -
 * this is the demonstration of requirement #8 (token header logic): mark public endpoints
 * with @NoAuth, leave authenticated endpoints unannotated. AuthInterceptor (core/network/
 * AuthInterceptor.kt) reads this annotation via reflection on the Retrofit Invocation and
 * skips attaching the Authorization header for exactly these two methods.
 *
 * If you add a real authenticated endpoint later (e.g. a "favorite this product" call that
 * needs the logged-in user's token), just DON'T add @NoAuth to it - the token gets attached
 * automatically since that's the default behavior.
 */
interface ProductApiService {

    @NoAuth
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductListResponse

    @NoAuth
    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): ProductDto
}
