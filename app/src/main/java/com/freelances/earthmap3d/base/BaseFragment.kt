package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.hideSystemBar
import com.earthmap.map.ltv.tracker.extensions.PreferenceHelper
import org.koin.android.ext.android.inject

abstract class BaseFragment<VB: ViewBinding>: Fragment() {
    protected val preferenceHelper: PreferenceHelper by inject()
    protected lateinit var myContext: Context
    protected lateinit var binding: VB
        private set
    protected abstract fun inflateBinding(inflater: LayoutInflater): VB

    abstract fun updateUI(view: View, savedInstanceState: Bundle?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.myContext = context
    }

    open fun isDisplayCutout(): Boolean = true

    open fun initListener() {}

    open fun loadAds() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.hideSystemBar()
        binding = inflateBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewInsets(binding.root, isDisplayCutout())
        loadAds()
        updateUI(view, savedInstanceState)
        initListener()
    }

    fun getContextF(): Context {
        return context ?: activity ?: myContext
    }

    fun setViewInsets(view: View, isDisplayCutout: Boolean = false) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val insetTypes =
                if (!isDisplayCutout) WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars()
                else WindowInsetsCompat.Type.systemBars()
            val systemBars = insets.getInsets(insetTypes)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
