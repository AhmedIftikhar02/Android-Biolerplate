package com.example.my_boilerplate.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.my_boilerplate.common.bus.LoginEventBus
import com.example.my_boilerplate.common.bus.SessionEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session_prefs")

/**
 * Single source of truth for "is the user logged in" + token storage.
 *
 * Why DataStore instead of plain SharedPreferences for THIS specific data: tokens are read
 * on every single network request (AuthInterceptor) and we want a Flow-friendly, coroutine-safe
 * API. For general app preferences (not session-critical) see common/managers/SharedPrefsManager.kt
 * which uses classic SharedPreferences as you asked for directly.
 *
 * IMPORTANT - this stores the token in plaintext DataStore, which is fine for most apps but
 * if you need hardened security (banking-grade), swap the read/write here for
 * androidx.security.crypto.EncryptedSharedPreferences or the Jetpack Security library's
 * encrypted DataStore. The rest of the app never needs to change since everything goes
 * through this class - that's the point of centralizing it here.
 */
@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginEventBus: LoginEventBus
) {

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = stringPreferencesKey("user_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveSession(accessToken: String, refreshToken: String?, userId: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            refreshToken?.let { prefs[Keys.REFRESH_TOKEN] = it }
            prefs[Keys.USER_ID] = userId
            prefs[Keys.IS_LOGGED_IN] = true
        }
        loginEventBus.emit(SessionEvent.LoggedIn)
    }

    suspend fun getAccessToken(): String? =
        context.sessionDataStore.data.map { it[Keys.ACCESS_TOKEN] }.first()

    suspend fun getRefreshToken(): String? =
        context.sessionDataStore.data.map { it[Keys.REFRESH_TOKEN] }.first()

    suspend fun getUserId(): String? =
        context.sessionDataStore.data.map { it[Keys.USER_ID] }.first()

    suspend fun isLoggedIn(): Boolean =
        context.sessionDataStore.data.map { it[Keys.IS_LOGGED_IN] ?: false }.first()

    /** Call this on logout, AND from AuthInterceptor/safe-call-site when a 401/403 comes back
     *  and a token refresh attempt (if you implement one) also fails. */
    suspend fun clearSession() {
        context.sessionDataStore.edit { it.clear() }
        loginEventBus.emit(SessionEvent.LoggedOut)
    }
}
