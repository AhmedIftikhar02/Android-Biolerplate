package com.example.my_boilerplate.home.data.repository;

import com.example.my_boilerplate.core.network.NetworkMonitor;
import com.example.my_boilerplate.home.data.remote.ProductApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ProductRepositoryImpl_Factory implements Factory<ProductRepositoryImpl> {
  private final Provider<ProductApiService> apiProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public ProductRepositoryImpl_Factory(Provider<ProductApiService> apiProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    this.apiProvider = apiProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public ProductRepositoryImpl get() {
    return newInstance(apiProvider.get(), networkMonitorProvider.get());
  }

  public static ProductRepositoryImpl_Factory create(Provider<ProductApiService> apiProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    return new ProductRepositoryImpl_Factory(apiProvider, networkMonitorProvider);
  }

  public static ProductRepositoryImpl newInstance(ProductApiService api,
      NetworkMonitor networkMonitor) {
    return new ProductRepositoryImpl(api, networkMonitor);
  }
}
