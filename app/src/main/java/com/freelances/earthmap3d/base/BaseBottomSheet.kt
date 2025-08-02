package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.setFullScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB : ViewBinding> : BottomSheetDialogFragment() {
    private lateinit var myContext: Context
    protected lateinit var binding: VB
        private set

    protected abstract fun inflateBinding(inflater: LayoutInflater): VB
    abstract fun updateUI(savedInstanceState: Bundle?)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateBinding(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.myContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setOnShowListener {
            dialog.findViewById<View?>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let { container ->
                    container.post {
                        if (container is FrameLayout) {
                            BottomSheetBehavior.from(container).apply {
                                state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                    }
                }
        }
        dialog.window?.setFullScreen()
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(savedInstanceState)
    }

    fun show(fm: FragmentManager) {
        this.show(fm, this::class.java.canonicalName)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            manager.beginTransaction().add(this@BaseBottomSheet, tag).commitAllowingStateLoss()
        }
    }

    fun getContextF(): Context {
        return context ?: activity ?: myContext
    }
}
