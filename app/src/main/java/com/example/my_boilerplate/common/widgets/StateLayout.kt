package com.example.my_boilerplate.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.my_boilerplate.R
import com.example.my_boilerplate.common.extensions.gone
import com.example.my_boilerplate.common.extensions.visible
import com.google.android.material.button.MaterialButton

/**
 * Wraps your real content (RecyclerView, ConstraintLayout, whatever) and shows the right
 * thing for each UiState (core/result/UiState.kt) without every Fragment re-implementing
 * loading spinners / error views / empty views from scratch.
 *
 * Usage in a Fragment's observeData():
 *
 *   viewModel.uiState.collectLifecycleFlow(this) { state ->
 *       when (state) {
 *           is UiState.Loading -> binding.stateLayout.showLoading()
 *           is UiState.Success -> { binding.stateLayout.showContent(); adapter.submitList(state.data) }
 *           is UiState.Error   -> binding.stateLayout.showError(state.exception.message) { viewModel.retry() }
 *           is UiState.Empty   -> binding.stateLayout.showEmpty()
 *           is UiState.Idle    -> Unit
 *       }
 *   }
 *
 * The "content" is whatever you put inside this ViewGroup in your layout XML (it's a single
 * extra child after the merge inflate below) - this widget never touches it directly except
 * to toggle its visibility.
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val progressBar: ProgressBar
    private val errorContainer: LinearLayout
    private val tvErrorMessage: TextView
    private val btnRetry: MaterialButton
    private val emptyContainer: LinearLayout
    private val tvEmptyMessage: TextView

    /** The single real content child, captured once after inflation (see init below). */
    private var contentView: View? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_state_layout, this, true)
        progressBar = findViewById(R.id.progressBar)
        errorContainer = findViewById(R.id.errorContainer)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        btnRetry = findViewById(R.id.btnRetry)
        emptyContainer = findViewById(R.id.emptyContainer)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // Whichever child the XML author placed inside <StateLayout>...</StateLayout> in
        // their own layout, besides the 3 views we just inflated, is "the content".
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.id != R.id.progressBar && child.id != R.id.errorContainer && child.id != R.id.emptyContainer) {
                contentView = child
                break
            }
        }
    }

    fun showLoading() {
        progressBar.visible()
        errorContainer.gone()
        emptyContainer.gone()
        contentView?.gone()
    }

    fun showContent() {
        progressBar.gone()
        errorContainer.gone()
        emptyContainer.gone()
        contentView?.visible()
    }

    fun showError(message: String?, onRetry: (() -> Unit)? = null) {
        progressBar.gone()
        contentView?.gone()
        emptyContainer.gone()
        errorContainer.visible()
        tvErrorMessage.text = message ?: context.getString(R.string.error_generic)
        if (onRetry != null) {
            btnRetry.visible()
            btnRetry.setOnClickListener { onRetry() }
        } else {
            btnRetry.gone()
        }
    }

    fun showEmpty(message: String? = null) {
        progressBar.gone()
        errorContainer.gone()
        contentView?.gone()
        emptyContainer.visible()
        message?.let { tvEmptyMessage.text = it }
    }
}
