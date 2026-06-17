package com.example.my_boilerplate.common.bus;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class LoginEventBus_Factory implements Factory<LoginEventBus> {
  @Override
  public LoginEventBus get() {
    return newInstance();
  }

  public static LoginEventBus_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LoginEventBus newInstance() {
    return new LoginEventBus();
  }

  private static final class InstanceHolder {
    private static final LoginEventBus_Factory INSTANCE = new LoginEventBus_Factory();
  }
}
