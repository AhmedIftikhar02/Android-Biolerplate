package com.example.my_boilerplate.core.session;

import android.content.Context;
import com.example.my_boilerplate.common.bus.LoginEventBus;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SessionManager_Factory implements Factory<SessionManager> {
  private final Provider<Context> contextProvider;

  private final Provider<LoginEventBus> loginEventBusProvider;

  public SessionManager_Factory(Provider<Context> contextProvider,
      Provider<LoginEventBus> loginEventBusProvider) {
    this.contextProvider = contextProvider;
    this.loginEventBusProvider = loginEventBusProvider;
  }

  @Override
  public SessionManager get() {
    return newInstance(contextProvider.get(), loginEventBusProvider.get());
  }

  public static SessionManager_Factory create(Provider<Context> contextProvider,
      Provider<LoginEventBus> loginEventBusProvider) {
    return new SessionManager_Factory(contextProvider, loginEventBusProvider);
  }

  public static SessionManager newInstance(Context context, LoginEventBus loginEventBus) {
    return new SessionManager(context, loginEventBus);
  }
}
