package com.example.my_boilerplate.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * The standard, Google-recommended way to collect a Flow from a Fragment's StateFlow/SharedFlow
 * safely - automatically starts collecting on STARTED and cancels on STOPPED, preventing the
 * classic "Fragment crashes because it tried to update a View after onDestroyView" bug, and
 * also preventing wasted work collecting while the screen isn't visible.
 *
 * Usage in a Fragment:
 *
 *   viewModel.uiState.collectLifecycleFlow(viewLifecycleOwner) { state ->
 *       when (state) { ... }
 *   }
 */
fun <T> Flow<T>.collectLifecycleFlow(
    fragment: Fragment,
    action: suspend (T) -> Unit
) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
        fragment.viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectLifecycleFlow.collect { action(it) }
        }
    }
}

/**
 * Same idea as collectLifecycleFlow above but for Activities (which have no
 * viewLifecycleOwner concept - just their own lifecycle).
 */
fun <T> Flow<T>.collectLifecycleFlowActivity(
    activity: AppCompatActivity,
    action: suspend (T) -> Unit
) {
    activity.lifecycleScope.launch {
        activity.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectLifecycleFlowActivity.collect { action(it) }
        }
    }
}

/**
 * Alternative using flowWithLifecycle, handy when you need to chain further Flow operators
 * before collecting.
 */
fun <T> Flow<T>.flowWithFragmentLifecycle(fragment: Fragment): Flow<T> =
    flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
