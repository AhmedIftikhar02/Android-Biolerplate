package com.example.my_boilerplate.core.network

/**
 * Mark a Retrofit API method with this when it should NOT receive the Authorization header.
 * Default behavior (no annotation) = token IS attached, since most endpoints in a typical
 * app require auth and login/register/public endpoints are the exception, not the rule.
 *
 * Example:
 *
 *   interface AuthApiService {
 *       @NoAuth
 *       @POST("auth/login")
 *       suspend fun login(@Body request: LoginRequest): LoginResponse
 *
 *       @GET("user/profile")  // no annotation -> token IS attached automatically
 *       suspend fun getProfile(): ProfileResponse
 *   }
 *
 * See core/network/AuthInterceptor.kt for how this annotation is read at request time.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoAuth
