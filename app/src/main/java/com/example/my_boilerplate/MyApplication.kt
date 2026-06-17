package com.example.my_boilerplate

import android.app.Application
import com.example.my_boilerplate.common.managers.ThemeManager
import com.example.my_boilerplate.core.network.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * @HiltAndroidApp triggers Hilt's code generation and creates the app-level dependency
 * container that everything else (Activities, Fragments, ViewModels, Workers) hangs off of.
 * This MUST be registered in AndroidManifest.xml as android:name (already done below in the
 * manifest step) or none of the @AndroidEntryPoint / @HiltViewModel annotations will work.
 */
@HiltAndroidApp
class MyApplication : Application() {

    @Inject lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.ENABLE_LOGGING) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        // Apply whatever theme mode the user previously picked (defaults to SYSTEM on first
        // launch). See common/managers/ThemeManager.kt.
        themeManager.applyTheme(themeManager.getSavedTheme())
    }
}
