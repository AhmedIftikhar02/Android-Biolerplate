# My-Boilerplate — Android Starter Template

Clone this repo for every new project. Everything below explains what's already wired up
and exactly where to add new code for a new feature.

## 0. Before you open this in Android Studio

1. Rename the package if you want something other than `com.example.my_boilerplate` (Android
   Studio: right-click the package → Refactor → Rename → Rename package, then update
   `namespace` and `applicationId` in `app/build.gradle.kts` and `package=` lines if any
   remain).
2. Replace `app/src/main/res/drawable/ic_launcher_foreground.xml` and regenerate proper
   launcher icons via **res → New → Image Asset → Launcher Icons (Adaptive and Legacy)**.
   The placeholder icon files in this repo only exist so the project compiles immediately;
   they are not meant to ship.
3. Change `BASE_URL` in `app/build.gradle.kts` (`defaultConfig.buildConfigField`) to your
   real backend once you have one. It currently points at `https://dummyjson.com/` for the
   sample Home feature.
4. Sync Gradle. First sync will take a while (Hilt + KSP + Room annotation processing).

## 1. What's already set up

| Area | Where | Notes |
|---|---|---|
| Light/Dark theme (Material 3) | `res/values/themes.xml` + `res/values-night/themes.xml` | Same style name in both, different parent + colors. See long comment in the light file. |
| Light/Dark colors | `res/values/colors.xml` + `res/values-night/colors.xml` | Semantic tokens (`color_primary`, `color_surface`, etc) — never reference brand_*/neutral_* directly outside these two files. |
| Manual dark mode toggle | `common/managers/ThemeManager.kt` | Independent of system setting; call `applyTheme(ThemeMode.DARK/LIGHT/SYSTEM)`. |
| Dimens scale (2dp–50dp) | `res/values/dimens.xml` | Plus text sizes, icon sizes, corner radii, elevations. |
| Buttons/EditText/Card styles | `res/values/styles.xml` | `Widget.App.Button.Primary/.Secondary/.Outline/.Text`, `Widget.App.EditText.Filled/.Outline`. |
| Gradient/card/button drawables | `res/drawable/bg_*.xml` | |
| Hilt DI | `di/*.kt` | `NetworkModule`, `RepositoryModule`, `DatabaseModule`, `AppModule`. |
| Retrofit + OkHttp + Moshi | `di/NetworkModule.kt`, `core/network/*` | Codegen-based Moshi adapters (`@JsonClass(generateAdapter = true)` on every DTO). |
| Token header logic | `core/network/NoAuth.kt` + `AuthInterceptor.kt` | Default = token attached. Annotate a Retrofit method with `@NoAuth` to skip it. |
| Logging | `core/network/HttpLoggerFactory.kt` + Timber in `MyApplication` | Auth header redacted from logs. Verbosity controlled by `BuildConfig.ENABLE_LOGGING`. |
| Safe API call wrapper | `core/network/SafeApiCall.kt` | Wrap every Repository's Retrofit call in `safeApiCall(networkMonitor) { ... }`. |
| UI states (loading/success/error/empty) | `core/result/UiState.kt` + `common/widgets/StateLayout.kt` | ViewModel exposes `StateFlow<UiState<T>>`; Fragment collects it and drives `StateLayout`. |
| Session management | `core/session/SessionManager.kt` | DataStore-backed, excluded from auto-backup (see `xml/backup_rules.xml`). |
| Login/session event bus | `common/bus/LoginEventBus.kt` | `SharedFlow`-based; `MainActivity` listens for `LoggedOut`/`SessionExpired`. |
| General app prefs (SharedPreferences) | `common/managers/SharedPrefsManager.kt` | For non-session settings: onboarding flag, theme choice, language. |
| Room scaffold | `database/AppDatabase.kt` + `di/DatabaseModule.kt` | Zero entities yet — see comments for the 5 steps to activate it. |
| Base classes | `base/*.kt` | `BaseActivity`, `BaseFragment`, `BaseViewModel`, `BaseAdapter`, `BaseBottomSheet`, `BaseDialog` — all generic over ViewBinding. |
| Network security | `xml/network_security_config.xml` | Blocks plain HTTP everywhere by default. |
| Sample full feature | `home/**` | Complete vertical slice: API service → DTO → mapper → domain model → repository → ViewModel → Fragment → Adapter. Hits the free DummyJSON products API. |
| Single-activity navigation | `MainActivity.kt` + `res/navigation/nav_graph.xml` | `homeFragment` is the start destination. |

## 2. Adding a brand-new feature (step by step)

Say you're adding an "Auth" feature with a login screen. Copy the `home` package's shape:

1. **Create the package folders**: `auth/data/remote`, `auth/data/repository`,
   `auth/domain/model`, `auth/domain/repository`, `auth/presentation/{ui,viewmodels}`.
2. **DTO** (`auth/data/remote/LoginDto.kt`): `@JsonClass(generateAdapter = true) data class
   LoginRequest(...)` / `LoginResponse(...)`.
3. **API service** (`auth/data/remote/AuthApiService.kt`): annotate the login endpoint with
   `@NoAuth` (no token exists yet at login time!); leave any "get current user" endpoint
   unannotated so the token attaches automatically once you have one.
4. **Domain model** (`auth/domain/model/User.kt`): clean, non-nullable fields.
5. **Mapper** (`auth/data/remote/AuthMapper.kt`): `fun LoginResponseDto.toDomain(): User`.
6. **Repository interface** (`auth/domain/repository/AuthRepository.kt`) +
   **impl** (`auth/data/repository/AuthRepositoryImpl.kt`, wrap calls in `safeApiCall`).
7. **Wire DI**:
   - `di/NetworkModule.kt`: add `provideAuthApiService(retrofit: Retrofit): AuthApiService`.
   - `di/RepositoryModule.kt`: add `@Binds abstract fun bindAuthRepository(impl:
     AuthRepositoryImpl): AuthRepository`.
8. **ViewModel** (`auth/presentation/viewmodels/LoginViewModel.kt`): extend `BaseViewModel`,
   expose `StateFlow<UiState<User>>`, call `sessionManager.saveSession(...)` on success.
9. **Layout + Fragment**: `res/layout/auth_fragment_login.xml` (prefix with `auth_` so it
   sorts together with other auth screens in Android Studio's resource list — see note on
   multi-module structure below if you want true folder separation), extend `BaseFragment`.
10. **Nav graph**: add a `<fragment>` node + `<action>` in `res/navigation/nav_graph.xml`.

## 3. A note on "separate res folders per feature"

True separate `res/` folders per feature (so Auth's XMLs live in a completely different
folder tree than Home's) requires converting each feature into its own Gradle module
(`:feature:home`, `:feature:auth`, each with its own `build.gradle.kts` and `res/` directory)
— a bigger structural change than a single-module boilerplate. What's set up instead: a
naming convention (`home_*.xml`, `auth_*.xml` prefixes) that keeps each feature's resources
grouped and easy to find/sort in Android Studio without the multi-module overhead. If you
outgrow this later (large team, multiple feature owners, faster incremental builds), that's
the natural next step — happy to help set that up when you're there.

## 4. Things intentionally left as TODOs

- **Token refresh on 401**: `AuthInterceptor`/`SafeApiCall` currently surface
  `UnauthorizedError` but don't attempt a silent refresh-token retry. Add an OkHttp
  `Authenticator` if you need that.
- **Room migrations**: `DatabaseModule` uses `fallbackToDestructiveMigration()`, fine for
  development; replace with real `Migration` objects before shipping with real user data.
- **Crash reporting hook**: `ReleaseTree` in `core/network/HttpLoggerFactory.kt` has a clearly
  marked TODO to forward warnings/errors to Crashlytics/Sentry/etc once you pick one.
- **Real launcher icon**: see step 0.2 above.
