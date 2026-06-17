package com.example.my_boilerplate.home.presentation.viewmodels;

import com.example.my_boilerplate.home.domain.repository.ProductRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public HomeViewModel_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new HomeViewModel_Factory(productRepositoryProvider);
  }

  public static HomeViewModel newInstance(ProductRepository productRepository) {
    return new HomeViewModel(productRepository);
  }
}
