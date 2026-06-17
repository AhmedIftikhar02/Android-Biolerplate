package com.example.my_boilerplate.core.network

import com.example.my_boilerplate.core.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

/**
 * Attaches "Authorization: Bearer <token>" to every outgoing request, EXCEPT methods
 * annotated with @NoAuth (see NoAuth.kt for the annotation and usage example).
 *
 * How it finds out which method is being called:
 * Retrofit tags every OkHttp Request it builds with an `Invocation` object that wraps the
 * java.lang.reflect.Method of the interface function that triggered the call. This works
 * for suspend functions too - the tag is attached by Retrofit's RequestFactory regardless
 * of whether the method is suspend, so reflection here is reliable and is the standard,
 * widely-used pattern for this exact problem (annotate-to-skip-auth).
 */
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val invocation = request.tag(Invocation::class.java)
        val skipAuth = invocation?.method()?.isAnnotationPresent(NoAuth::class.java) == true

        if (skipAuth) {
            return chain.proceed(request)
        }

        // runBlocking is safe here: interceptors run on OkHttp's own dispatcher thread,
        // never the main thread, and reading a cached token from DataStore/memory is fast.
        // This is the standard approach since OkHttp Interceptor.intercept() is not a
        // suspend function and can't be made one.
        val token = runBlocking { sessionManager.getAccessToken() }

        val authenticatedRequest = if (!token.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
