package com.example.my_boilerplate.di

import com.example.my_boilerplate.home.data.repository.ProductRepositoryImpl
import com.example.my_boilerplate.home.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds each domain Repository interface to its concrete Impl. We use @Binds (not @Provides)
 * because the implementation already has an @Inject constructor that Hilt can call directly -
 * @Binds is more efficient (no extra generated factory method) and is the recommended way to
 * wire an interface to its implementation.
 *
 * As you add more features (auth, profile, etc.) add one more @Binds function here per
 * feature's repository - same pattern as bindProductRepository below.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}
