package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseDialogFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.DialogPermissionLocationBinding

class PermissionLocationDialog : BaseDialogFragment<DialogPermissionLocationBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogPermissionLocationBinding {
        return DialogPermissionLocationBinding.inflate(inflater, container, false)
    }

    private var onClick: () -> Unit = {}

    override fun getLayout(): Int {
        return R.layout.dialog_permission_location
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.ivSwitch.safeClick {
            onClick()
            dismiss()
        }
    }

    fun setOnClick(onClick: () -> Unit) = apply {
        this.onClick = onClick
    }

}
