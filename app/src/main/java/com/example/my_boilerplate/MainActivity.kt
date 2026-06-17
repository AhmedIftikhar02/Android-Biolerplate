package com.example.my_boilerplate

import com.example.my_boilerplate.base.BaseActivity
import com.example.my_boilerplate.common.bus.LoginEventBus
import com.example.my_boilerplate.common.bus.SessionEvent
import com.example.my_boilerplate.common.extensions.collectLifecycleFlowActivity
import com.example.my_boilerplate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Single-activity host for the whole app (requirement: single-activity + Navigation
 * Component). Every screen is a Fragment inside navHostFragment - see res/navigation/
 * nav_graph.xml for the destinations.
 *
 * Also the one place that reacts to global session events (LoginEventBus) - e.g. when
 * SessionManager.clearSession() fires LoggedOut from anywhere in the app (a 401 from any
 * API call, a manual logout button, etc.), this is where you'd navigate to an auth flow and
 * clear the back stack, regardless of which screen triggered it.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject lateinit var loginEventBus: LoginEventBus

    override fun setupViews() {
        // Nothing to set up directly - Nav Component handles the Fragment swapping via
        // the FragmentContainerView declared in activity_main.xml.
    }

    override fun observeData() {
        loginEventBus.events.collectLifecycleFlowActivity(this) { event ->
            when (event) {
                is SessionEvent.LoggedOut, is SessionEvent.SessionExpired -> {
                    // TODO once you add a real auth flow + nav graph destinations:
                    // val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                    // navHost.navController.navigate(R.id.authFragment) // popUpTo to clear back stack
                }
                is SessionEvent.LoggedIn -> Unit
            }
        }
    }
}
