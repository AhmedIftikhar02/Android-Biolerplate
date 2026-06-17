package com.example.my_boilerplate.di;

import com.example.my_boilerplate.home.data.remote.ProductApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NetworkModule_ProvideProductApiServiceFactory implements Factory<ProductApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideProductApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ProductApiService get() {
    return provideProductApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideProductApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideProductApiServiceFactory(retrofitProvider);
  }

  public static ProductApiService provideProductApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideProductApiService(retrofit));
  }
}
