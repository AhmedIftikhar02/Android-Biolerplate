package com.example.my_boilerplate.core.network

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Provides the OkHttp logging interceptor, with the Authorization header redacted so tokens
 * never end up in logcat / crash reports even in debug builds (easy to forget, easy to leak).
 *
 * Verbosity is controlled by BuildConfig.ENABLE_LOGGING (true for debug, false for release -
 * see app/build.gradle.kts buildTypes). In release it's effectively a no-op interceptor.
 */
object HttpLoggerFactory {

    fun create(enableLogging: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        }).apply {
            level = if (enableLogging) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            redactHeader("Authorization")
            redactHeader("Cookie")
        }
    }
}

/**
 * Debug tree logs everything with file/line info (Timber's default DebugTree behavior).
 * Release tree only logs WARN/ERROR and is where you'd forward to Crashlytics/Sentry -
 * left as a clear extension point below.
 */
class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == android.util.Log.WARN || priority == android.util.Log.ERROR) {
            // TODO: forward to your crash reporting tool of choice, e.g.:
            // Firebase.crashlytics.log("$tag: $message")
            // t?.let { Firebase.crashlytics.recordException(it) }
        }
    }
}
