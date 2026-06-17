package com.example.my_boilerplate.di

import com.example.my_boilerplate.common.providers.DefaultDispatcherProvider
import com.example.my_boilerplate.common.providers.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * General app-wide bindings that don't belong to Network/Database/Repository modules.
 * Classes with their own @Inject constructor (SessionManager, SharedPrefsManager,
 * ThemeManager, ResourceProvider, NetworkMonitor, LoginEventBus, AuthInterceptor) do NOT
 * need an entry here - Hilt finds and constructs them automatically. This module only
 * exists for binding an INTERFACE to its implementation, which Hilt can't infer on its own.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider
}
