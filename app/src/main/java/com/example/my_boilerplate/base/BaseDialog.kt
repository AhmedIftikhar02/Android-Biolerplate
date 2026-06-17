package com.example.my_boilerplate.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

/**
 * Generic ViewBinding base for plain DialogFragments (use BaseBottomSheet instead if you
 * want a bottom-sheet-style dialog). Extend like:
 *
 *   class ConfirmDialog : BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {
 *       override fun setupViews() { ... }
 *   }
 */
abstract class BaseDialog<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB
) : DialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = bindingInflater(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        setupViews()
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun setupViews()
}
