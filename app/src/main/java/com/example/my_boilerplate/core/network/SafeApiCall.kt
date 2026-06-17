package com.example.my_boilerplate.core.network

import com.example.my_boilerplate.core.result.AppException
import com.example.my_boilerplate.core.result.Result
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Wrap every single Retrofit suspend call made anywhere in the app with this.
 *
 * Usage in a RepositoryImpl:
 *
 *   override suspend fun getProducts(): Result<List<Product>> = safeApiCall(networkMonitor) {
 *       api.getProducts().toDomainList()
 *   }
 *
 * What it does:
 *  1. Checks connectivity up front (fails fast with NetworkError, no need to hit the socket).
 *  2. Runs the actual call on Dispatchers.IO (callers never need to think about threading).
 *  3. Catches every exception type Retrofit/OkHttp/Moshi can throw and converts it into
 *     the correct AppException subtype, so the rest of the app only ever deals with
 *     Result<T> / AppException - never raw IOException/HttpException.
 *  4. Re-throws CancellationException so structured concurrency (cancelling a coroutine
 *     scope, e.g. on fragment destroy) keeps working correctly - we must NEVER swallow it.
 *  5. Logs every failure via Timber so you get one consistent place to look at API errors.
 */
suspend fun <T> safeApiCall(
    networkMonitor: NetworkMonitor,
    apiCall: suspend () -> T
): Result<T> {
    if (!networkMonitor.isOnline()) {
        Timber.tag("API").w("safeApiCall blocked: device is offline")
        return Result.Error(AppException.NetworkError())
    }

    return withContext(Dispatchers.IO) {
        try {
            Result.Success(apiCall())
        } catch (e: CancellationException) {
            throw e // never swallow - required for coroutines to cancel correctly
        } catch (e: HttpException) {
            val code = e.code()
            Timber.tag("API").e(e, "HTTP $code error")
            when (code) {
                401, 403 -> Result.Error(AppException.UnauthorizedError())
                else -> Result.Error(
                    AppException.ServerError(
                        code = code,
                        message = e.message() ?: "Server error ($code)"
                    )
                )
            }
        } catch (e: SocketTimeoutException) {
            Timber.tag("API").e(e, "Request timed out")
            Result.Error(AppException.TimeoutError())
        } catch (e: JsonDataException) {
            Timber.tag("API").e(e, "JSON parsing failed")
            Result.Error(AppException.ParseError())
        } catch (e: IOException) {
            Timber.tag("API").e(e, "Network IO error")
            Result.Error(AppException.NetworkError())
        } catch (e: Exception) {
            Timber.tag("API").e(e, "Unknown error in API call")
            Result.Error(AppException.UnknownError(e.message ?: "Unknown error"))
        }
    }
}
