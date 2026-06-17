package com.example.my_boilerplate.common.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-wide session events. This is the modern, coroutine-native replacement for old
 * "EventBus" libraries (Greenrobot EventBus, RxBus) - SharedFlow does the same job
 * (broadcast an event to anyone listening) without needing a separate library.
 *
 * Typical consumer: MainActivity collects this once at startup and reacts to LoggedOut
 * by navigating to the auth flow and clearing the back stack, no matter which screen
 * triggered the logout (token expiry from a background API call, manual logout button,
 * force-logout from server, etc.) - all of them just call sessionManager.clearSession(),
 * which emits LoggedOut here, and MainActivity is the single place that reacts to it.
 */
@Singleton
class LoginEventBus @Inject constructor() {

    private val _events = MutableSharedFlow<SessionEvent>(replay = 0, extraBufferCapacity = 1)
    val events: SharedFlow<SessionEvent> = _events.asSharedFlow()

    suspend fun emit(event: SessionEvent) {
        _events.emit(event)
    }
}

sealed class SessionEvent {
    data object LoggedIn : SessionEvent()
    data object LoggedOut : SessionEvent()
    data object SessionExpired : SessionEvent()
}
