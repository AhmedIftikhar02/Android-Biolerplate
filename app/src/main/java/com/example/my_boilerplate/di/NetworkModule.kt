package com.example.my_boilerplate.di

import com.example.my_boilerplate.BuildConfig
import com.example.my_boilerplate.core.network.AuthInterceptor
import com.example.my_boilerplate.core.network.HttpLoggerFactory
import com.example.my_boilerplate.home.data.remote.ProductApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * All network-related singletons live here: Moshi, OkHttpClient (with our interceptors wired
 * in the correct order), Retrofit, and every ApiService interface. As you add more features
 * (e.g. AuthApiService for a real login flow), add one more @Provides function here following
 * the same pattern as provideProductApiService below - don't create a new module per feature
 * for this, one NetworkModule is the standard, clean approach.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * No KotlinJsonAdapterFactory needed here: every DTO uses @JsonClass(generateAdapter = true)
     * (see home/data/remote/ProductDto.kt), which means the moshi-kotlin-codegen KSP processor
     * generates a real JsonAdapter class for each DTO at compile time. Moshi finds these
     * generated adapters automatically - reflection-based adapters are only needed for data
     * classes WITHOUT the @JsonClass annotation, which we don't have any of. Codegen is also
     * faster at runtime than reflection, so this is the better default for new DTOs too -
     * just remember to add @JsonClass(generateAdapter = true) to every new DTO you create.
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            // Order matters: auth header gets added first, then the logging interceptor logs
            // the FINAL outgoing request (including the header, which it then redacts - see
            // HttpLoggerFactory). If you reverse the order, the logger won't see the auth header.
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggerFactory.create(BuildConfig.ENABLE_LOGGING))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService =
        retrofit.create(ProductApiService::class.java)
}
