package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog

import android.content.Context
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseDialog
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.DialogExitAppBinding

class ExitAppDialog(
    ctx: Context,
    private val onAcceptClick: () -> Unit
) :
    BaseDialog<DialogExitAppBinding>(ctx, DialogExitAppBinding::inflate) {


    override fun initView() {
        setContentView(binding.root)

        binding.buttonNo.safeClick {
            onAcceptClick()
            dismiss()
        }

        binding.buttonOk.safeClick {
            dismiss()
        }

        binding.root.safeClick {
            dismiss()
        }

        binding.ctlDialog.safeClick {}

    }


}